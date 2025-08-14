package io.mosip.kernel.biometrics.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.OtherKey;
import io.mosip.kernel.biometrics.entities.BDBInfo;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.SingleAnySubtypeType;
import io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator;
import io.mosip.kernel.core.cbeffutil.constant.CbeffConstant;
import io.mosip.kernel.core.cbeffutil.exception.CbeffException;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.biometrics.entities.*;

/**
 * Utility class for Cbeff (Common Biometric Exchange File Format) data
 * validation and manipulation.
 *
 * This class provides functionalities for: - Validating BIR (Biometric
 * Information Record) data before generating a valid Cbeff XML. - Extracting
 * specific data based on type and subtype from BIR objects. - Creating and
 * parsing Cbeff XML using JAXB (Java Architecture for XML Binding).
 *
 * @author Ramadurai Pandian
 */
public class CbeffValidator {

	private static final List<String> NO_SUBTYPE_LIST = Collections.singletonList("No Subtype");

	private static final Map<Class<?>, Set<String>> ENUM_CACHE = new ConcurrentHashMap<>();

	private static final JAXBContext BIR_CONTEXT;

	static {
		try {
			BIR_CONTEXT = JAXBContext.newInstance(BIR.class,
					BIRInfo.class,
					BDBInfo.class,
					SBInfo.class,
					VersionType.class);
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Failed to initialize JAXBContext for BIR: " + e.getMessage());
		}
	}

	private CbeffValidator() {
		throw new IllegalStateException("CbeffValidator class");
	}

	/**
	 * Validates the provided BIR data according to Cbeff specifications.
	 *
	 * This method performs various checks on the BIR object, including: - Null
	 * check for the BIR object. - Ensuring that BDB (Biometric Data Block) list is
	 * not empty (except in case of exceptions). - Verifying the presence of BDB
	 * information. - Validating the type information provided in BDBInfo. -
	 * Checking the format type against the provided biometric types.
	 *
	 * @param birRoot The BIR data object to be validated.
	 * @throws CbeffException If any validation check fails.
	 */
	@SuppressWarnings({ "java:S3776" })
	public static boolean validateXML(BIR birRoot) throws CbeffException {
		if (birRoot == null) {
			throw new CbeffException("BIR value is null");
		}

		List<BIR> birList = birRoot.getBirs();
		for (BIR bir : birList) {
			if (bir == null) {
				continue;
			}

			boolean isException = false;
			if (bir.getOthers() != null) {
				String val = bir.getOthers().get(OtherKey.EXCEPTION);
				isException = "true".equalsIgnoreCase(val);
			}

			if ((bir.getBdb() == null || bir.getBdb().length < 1) && !isException) {
				throw new CbeffException("BDB value can't be empty");
			}

			BDBInfo bdbInfo = bir.getBdbInfo();
			if (bdbInfo  == null)
				throw new CbeffException("BDB information can't be empty");

			List<BiometricType> biometricTypes = bdbInfo.getType();
			if (biometricTypes == null || biometricTypes.isEmpty()) {
				throw new CbeffException("Type value needs to be provided");
			}

			long formatType = bdbInfo.getFormat() != null ? Long.parseLong(bdbInfo.getFormat().getType()): -1;
			if (!validateFormatType(formatType, biometricTypes)) {
				throw new CbeffException("Patron Format type is invalid");
			}
		}
		return true;
	}

	/**
	 * Validates the format type based on the provided biometric types.
	 *
	 * This method checks if the format type specified in BDBInfo matches the
	 * expected format based on the provided list of biometric types.
	 *
	 * @param formatType     The format type ID from BDBInfo.
	 * @param biometricTypes List of biometric types associated with the BDB data.
	 * @return True if the format type is valid for the given biometric types, false
	 *         otherwise.
	 */
	@SuppressWarnings({ "java:S6208" })
	private static boolean validateFormatType(long formatType, List<BiometricType> biometricTypes) {
		if (biometricTypes == null || biometricTypes.isEmpty()) {
			return false;
		}
		String type = biometricTypes.get(0).value();
        return switch (type) {
            case "Finger" -> formatType == CbeffConstant.FORMAT_TYPE_FINGER
                    || formatType == CbeffConstant.FORMAT_TYPE_FINGER_MINUTIAE;
            case "Iris" -> formatType == CbeffConstant.FORMAT_TYPE_IRIS;
            case "ExceptionPhoto", "Face" -> formatType == CbeffConstant.FORMAT_TYPE_FACE;
            default -> false;
        };
	}

	/**
	 * Retrieves the format type ID based on the provided string type.
	 *
	 * This method maps string representations of biometric data formats to their
	 * corresponding format type IDs defined in {@link CbeffConstant}.
	 *
	 * @param type The string representation of the biometric data format (e.g.,
	 *             "finger", "iris").
	 * @return The format type ID associated with the provided string type, or 0 if
	 *         no match is found.
	 */
	@SuppressWarnings({ "java:S6208" })
	private static long getFormatType(String type) {
        return switch (type.toLowerCase()) {
            case "finger" -> CbeffConstant.FORMAT_TYPE_FINGER;
            case "iris" -> CbeffConstant.FORMAT_TYPE_IRIS;
            case "fmr" -> CbeffConstant.FORMAT_TYPE_FINGER_MINUTIAE;
            case "face", "exceptionphoto" -> CbeffConstant.FORMAT_TYPE_FACE;
            default -> 0;
        };
	}

	/**
	 * Creates a byte array containing the XML representation of the provided BIR
	 * (Biometric Information Record) object.
	 *
	 * This method performs the following steps: 1. Validates the BIR object using
	 * the {@link #validateXML(BIR)} method. 2. Creates a JAXBContext instance for
	 * the BIR class. 3. Obtains a Marshaller instance from the JAXBContext to
	 * perform marshalling. 4. Sets the Marshaller property
	 * {@link Marshaller#JAXB_FORMATTED_OUTPUT} to true to generate human-readable,
	 * indented XML. 5. Marshals the BIR object to a byte array representing the XML
	 * data: - Creates a ByteArrayOutputStream to capture the generated XML data. -
	 * Wraps the ByteArrayOutputStream in an OutputStreamWriter for character-based
	 * output. - Marshals the BIR object using the Marshaller and writes it to the
	 * OutputStreamWriter. - Closes the OutputStreamWriter. 6. Validates the
	 * generated XML data against the provided XSD (XML Schema Definition) using the
	 * assumed `CbeffXSDValidator.validateXML` method. 7. Handles potential
	 * exceptions: - Catches SAXException thrown during XSD validation, extracts the
	 * relevant error message, and wraps it in a CbeffValidationException. - Catches
	 * any other Exception, wraps it in a CbeffException with a more informative
	 * message. 8. Returns the generated and validated XML data as a byte array.
	 *
	 * @param bir The BIR object representing the biometric data to be converted to
	 *            XML.
	 * @param xsd The XSD (XML Schema Definition) used for validation as a byte
	 *            array.
	 * @return A byte array containing the generated and validated XML data.
	 * @throws Exception  If XSD validation fails
	 */
	public static byte[] createXMLBytes(BIR bir, byte[] xsd) throws Exception {
		CbeffValidator.validateXML(bir);

		Marshaller jaxbMarshaller = BIR_CONTEXT.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		byte[] savedData;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {

			jaxbMarshaller.marshal(bir, writer);
			writer.flush(); // ensure all characters are written to baos
			savedData = baos.toByteArray();
		}

		try {
			CbeffXSDValidator.validateXML(xsd, savedData);
		} catch (SAXException sax) {
			sax.printStackTrace();
			String message = sax.getMessage();
			if (message != null && message.contains(":")) {
				message = message.substring(message.indexOf(":"));
			}
			throw new CbeffException("XSD validation failed due to attribute " + message);
		} catch (Exception e) {
			throw new CbeffException("XSD validation failed due to other error " + e.getLocalizedMessage());
		}

		return savedData;
	}

	/**
	 * Deserializes a byte array containing XML data into a BIR (Biometric
	 * Information Record) object.
	 *
	 * This method utilizes JAXB (Java Architecture for XML Binding) to unmarshal
	 * the XML data into a BIR object. Here's a breakdown of the process: 1. Creates
	 * a JAXBContext instance for the BIR class. 2. Obtains an Unmarshaller instance
	 * from the JAXBContext to perform unmarshalling. 3. Creates a StreamSource
	 * object to wrap the provided byte array as an XML data source. 4. Unmarshals
	 * the XML data using the Unmarshaller: - The unmarshal method takes the
	 * StreamSource object and the expected target class (BIR.class) as arguments. -
	 * The method returns a JAXBElement instance containing the unmarshalled BIR
	 * object. 5. Extracts the actual BIR object from the JAXBElement using its
	 * `getValue` method.
	 *
	 * @param fileBytes The byte array containing the XML data to be unmarshalled.
	 * @return The BIR object representing the deserialized biometric data, or null
	 *         if the unmarshalling fails.
	 * @throws Exception A general exception is thrown if any error occurs during
	 *                   the unmarshalling process. It's recommended to catch and
	 *                   handle specific JAXB exceptions for more robust error
	 *                   handling. @SuppressWarnings("java:S112") Suppresses the
	 *                   SonarLint potential for confusing code due to a stream
	 *                   being closed implicitly. In this case, the StreamSource
	 *                   doesn't manage any resources that need explicit closing.
	 */
	@SuppressWarnings({ "java:S112" })
	public static BIR getBIRFromXML(byte[] fileBytes) throws Exception {
		if (fileBytes == null || fileBytes.length == 0) {
			throw new CbeffException("Input file bytes cannot be null or empty");
		}

		Unmarshaller unmarshaller = BIR_CONTEXT.createUnmarshaller();

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
			JAXBElement<BIR> jaxBir = unmarshaller.unmarshal(new StreamSource(inputStream), BIR.class);
			return jaxBir.getValue();
		}
	}

	/**
	 * Extracts Biometric Data Block (BDB) information from a BIR (Biometric
	 * Information Record) object based on provided type and subtype filters.
	 *
	 * This method allows retrieving specific BDB data from a BIR object. It
	 * considers the following: - If both `type` and `subType` are null, it
	 * retrieves all the latest BDB data from the BIR using the
	 * {@link #getAllLatestDatafromBIR(BIR)} method (assumed to exist). - If a
	 * `type` is provided, it filters the BDBs based on the biometric type (e.g.,
	 * "Finger", "Iris"). - If a `subType` is provided, it further filters the BDBs
	 * based on a sub-type associated with the type (implementation details for
	 * getting the subType might vary). - The method also considers the format type
	 * associated with the BDB based on the provided type.
	 *
	 * The extracted BDB information is stored in a map with key-value pairs, where
	 * the key is a combination of BDB ID and format type, and the value is the BDB
	 * data.
	 *
	 * @param bir     The BIR object containing the biometric data.
	 * @param type    The biometric data type (e.g., "Finger", "Iris") to filter by
	 *                (can be null).
	 * @param subType The sub-type associated with the `type` for further filtering
	 *                (can be null).
	 * @return A map containing extracted BDB information with key-value pairs (key:
	 *         BDB ID + format type, value: BDB data).
	 * @throws Exception A general exception is thrown if any error occurs during
	 *                   the processing. Consider catching and handling specific
	 *                   exception types for more robust error handling.
	 */
	public static Map<String, String> getBDBBasedOnTypeAndSubType(BIR bir, String type, String subType)
			throws Exception {
		if (bir == null || bir.getBirs() == null || bir.getBirs().isEmpty()) {
			return Collections.emptyMap(); // Fast return if no data
		}

		BiometricType biometricType = (type != null) ? getBiometricType(type) : null;
		SingleAnySubtypeType singleAnySubType = (subType != null) ? getSingleAnySubtype(subType) : null;
		long formatType = (type != null) ? getFormatType(type) : -1;

		if (biometricType == null && singleAnySubType == null) {
			return getAllLatestDatafromBIR(bir);
		}

		Map<String, String> bdbMap = new HashMap<>();
		populateBDBMap(bir, biometricType, singleAnySubType, formatType, bdbMap);

		if (bdbMap.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, String> finalMap = new HashMap<>(bdbMap.size());
		for (Map.Entry<String, String> entry : bdbMap.entrySet()) {
			String key = entry.getKey();
			int lastIdx = key.lastIndexOf('_');
			if (lastIdx > 0) {
				finalMap.put(key.substring(0, lastIdx), entry.getValue());
			} else {
				finalMap.put(key, entry.getValue()); // fallback if no underscore
			}
		}

		return finalMap;
	}

	/**
	 * Populates a map with BDB (Biometric Data Block) information extracted from a
	 * BIR (Biometric Information Record) object based on provided filters and
	 * format type.
	 *
	 * This method iterates through the BIRs within the provided BIR object
	 * (`birRoot`) and extracts BDB information that matches the filtering criteria
	 * and format type. The extracted information is stored in the provided map
	 * (`bdbMap`), using a specific key format.
	 *
	 * Key Format: - Biometric type (if provided) + "_" - Subtype list joined by
	 * spaces (empty if not provided) + "_" - BDB format type + "_" - BDB creation
	 * date in milliseconds since epoch (UTC)
	 *
	 * Value: - Base64 encoded BDB data
	 *
	 * Filtering Criteria: - If only `biometricType` is provided, BDBs are filtered
	 * based on the type match and format type. - If only `singleAnySubType` is
	 * provided, BDBs are filtered based on the sub-type match and format type.
	 * Biometric type is ignored. - If both `biometricType` and `singleAnySubType`
	 * are provided, BDBs are filtered based on a match with both type and sub-type,
	 * and the format type must also match.
	 *
	 * @param birRoot          The BIR object containing the biometric data.
	 * @param biometricType    The biometric data type (e.g., "Finger", "Iris") to
	 *                         filter by (can be null).
	 * @param singleAnySubType The sub-type associated with the `type` for further
	 *                         filtering (can be null).
	 * @param formatType       The format type ID associated with the BDB data (can
	 *                         be null).
	 * @param bdbMap           The map to store the extracted BDB information
	 *                         (key-value pairs).
	 */
	@SuppressWarnings({ "java:S3776", "removal" })
	private static void populateBDBMap(BIR birRoot, BiometricType biometricType, SingleAnySubtypeType singleAnySubType,
			Long formatType, Map<String, String> bdbMap) {
		if (birRoot == null || birRoot.getBirs() == null || birRoot.getBirs().isEmpty()) {
			return;
		}

		final String subTypeValue = (singleAnySubType != null) ? singleAnySubType.value() : null;

		for (BIR bir : birRoot.getBirs()) {
			if (bir == null) continue;

			BDBInfo bdbInfo = bir.getBdbInfo();
			if (bdbInfo == null || bdbInfo.getFormat() == null) continue;

			List<String> singleSubTypeList = (bdbInfo.getSubtype() != null) ? bdbInfo.getSubtype() : Collections.emptyList();

			List<BiometricType> biometricTypes = bdbInfo.getType();
			String bdbFormatType = bdbInfo.getFormat().getType();

			boolean formatMatch = (formatType == null) || (formatType == Long.parseLong(bdbFormatType));

			String encodedBdb = CryptoUtil.encodeBase64String(bir.getBdb());
			long timestamp = bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli();

			if (biometricType != null && singleAnySubType == null && biometricTypes.contains(biometricType) && formatMatch) {
				bdbMap.put(biometricType + "_" + String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_" + timestamp,
						encodedBdb);
			}
			else if (biometricType == null && subTypeValue != null && singleSubTypeList.contains(subTypeValue)) {
				// Convert BiometricTypes once
				String typeString = String.join(" ", convertToList(biometricTypes));
				bdbMap.put(typeString + "_" + String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_" + timestamp,
						encodedBdb);
			}
			else if (biometricType != null && subTypeValue != null &&
					biometricTypes.contains(biometricType) && singleSubTypeList.contains(subTypeValue) && formatMatch) {
				bdbMap.put(biometricType + "_" + subTypeValue + "_" + bdbFormatType + "_" + timestamp,
						encodedBdb);
			}
		}
	}

	/**
	 * Extracts all the latest BDB (Biometric Data Block) information from a BIR
	 * (Biometric Information Record) object.
	 *
	 * This method iterates through the BIRs within the provided BIR object
	 * (`birRoot`) and extracts all BDB information, considering them to be the
	 * latest data. The extracted information is stored in a map with key-value
	 * pairs, where the key is a combination of BDB ID, format type, and creation
	 * date, and the value is the BDB data.
	 *
	 * Key Format: - Biometric type (from the first element in the type list) + "_"
	 * - Subtype list joined by spaces (if empty, "No Subtype" is used) + "_" - BDB
	 * format type + "_" - BDB creation date in milliseconds since epoch (UTC)
	 *
	 * Value: - Base64 encoded BDB data
	 *
	 * This method assumes that the latest BDB information is readily identifiable
	 * within the BIR object structure.
	 * 
	 * @param birRoot The BIR object containing the biometric data.
	 * @return A map containing all extracted BDB information with key-value pairs
	 *         (key: BDB ID + format type + creation date, value: BDB data).
	 * @throws Exception A general exception is thrown if any error occurs during
	 *                   the processing. Consider catching and handling specific
	 *                   exception types for more robust error handling.
	 */
	@SuppressWarnings({ "java:S112", "java:S1130", "removal" })
	private static Map<String, String> getAllLatestDatafromBIR(BIR birRoot) throws Exception {
		if (birRoot == null || birRoot.getBirs() == null || birRoot.getBirs().isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, String> bdbMap = new HashMap<>(birRoot.getBirs().size());

		for (BIR bir : birRoot.getBirs()) {
			if (bir == null) continue;

			BDBInfo bdbInfo = bir.getBdbInfo();
			if (bdbInfo == null || bdbInfo.getFormat() == null) continue;

			List<String> singleSubTypeList = (bdbInfo.getSubtype() == null || bdbInfo.getSubtype().isEmpty())
					? NO_SUBTYPE_LIST
					: bdbInfo.getSubtype();

			List<BiometricType> biometricTypes = bdbInfo.getType();
			if (biometricTypes == null || biometricTypes.isEmpty()) continue;

			String biometricTypeStr = biometricTypes.get(0).toString();
			String bdbFormatType = bdbInfo.getFormat().getType();
			long timestamp = bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli();

			String key = biometricTypeStr + "_" + String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_" + timestamp;
			String encodedData = CryptoUtil.encodeBase64String(bir.getBdb());
			bdbMap.put(key, encodedData);
		}

		Map<String, String> finalMap = new HashMap<>(bdbMap.size());
		for (Map.Entry<String, String> entry : bdbMap.entrySet()) {
			String key = entry.getKey();
			int lastIdx = key.lastIndexOf('_');
			if (lastIdx > 0) {
				finalMap.put(key.substring(0, lastIdx), entry.getValue());
			} else {
				finalMap.put(key, entry.getValue());
			}
		}

		return finalMap;
	}

	/**
	 * Converts a list of {@link BiometricType} enums to a list of strings
	 * containing their corresponding names.
	 *
	 * This method utilizes Java Streams to efficiently convert each enum element in
	 * the provided list to its name (string representation).
	 *
	 * @param biometricTypeList The list of BiometricType enums to be converted.
	 * @return A new list containing the string names of the BiometricType enums.
	 */
	private static List<String> convertToList(List<BiometricType> biometricTypeList) {
		if (biometricTypeList == null || biometricTypeList.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> result = new ArrayList<>(biometricTypeList.size());
		for (BiometricType type : biometricTypeList) {
			result.add(type.name());
		}
		return result;
	}

	/**
	 * Retrieves the {@link BiometricType} enum based on the provided string value.
	 *
	 * This method implements the following logic to determine the BiometricType: 1.
	 * Checks if the provided `type` string directly matches an existing
	 * BiometricType enum name using the `isInEnum` method. 2. If a direct match is
	 * not found and the `type` string is "FMR", it returns BiometricType.FINGER. 3.
	 * Otherwise, it attempts to convert the `type` string to a BiometricType enum
	 * using the `valueOf` method (may throw IllegalArgumentException).
	 *
	 * @param type The string value representing the BiometricType.
	 * @return The corresponding BiometricType enum if found, otherwise throws an
	 *         exception.
	 * @throws IllegalArgumentException If the provided string value doesn't match
	 *                                  any existing BiometricType enum name
	 *                                  (excluding the special case of "FMR").
	 */
	private static BiometricType getBiometricType(String type) {
		if (isInEnum(type, BiometricType.class)) {
			return BiometricType.valueOf(type);
		} else {
			if (type.equals("FMR"))
				return BiometricType.FINGER;
			else
				return BiometricType.fromValue(type);
		}
	}

	/**
	 * Checks if a provided string value exists as a name of an enum within the
	 * specified enum class.
	 *
	 * This is a generic utility method applicable to any enum class. It iterates
	 * through the enum constants of the provided `enumClass` and compares their
	 * names with the given `value`.
	 *
	 * @param <E>       The type parameter representing the enum class.
	 * @param value     The string value to be checked against enum names.
	 * @param enumClass The class object representing the enum type.
	 * @return True if the string value matches the name of an enum constant in the
	 *         provided class, false otherwise.
	 */
	public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		if (value == null || enumClass == null) {
			return false;
		}

		Set<String> values = ENUM_CACHE.computeIfAbsent(enumClass, cls -> {
			Set<String> set = new HashSet<>();
			for (E e : (E[]) cls.getEnumConstants()) {
				set.add(e.name());
			}
			return set;
		});

		return values.contains(value);
	}

	/**
	 * Extracts all BDB (Biometric Data Block) information based on provided type
	 * and subtype filters from a BIR (Biometric Information Record) object.
	 *
	 * This method iterates through the BIRs within the provided BIR object
	 * (`birRoot`) and retrieves BDB information that matches the filtering criteria
	 * and format type. The extracted information is stored in a map with key-value
	 * pairs, where the key is a combination of BDB details and the value is the BDB
	 * data as a UTF-8 encoded string.
	 *
	 * Key Format: - Biometric type (if provided) + "_" - Subtype list joined by
	 * spaces (empty if not provided) + "_" - BDB format type + "_" - BDB creation
	 * date in milliseconds since epoch (UTC)
	 *
	 * Value: - UTF-8 encoded BDB data string
	 *
	 * Filtering Criteria: - If only `biometricType` is provided, BDBs are filtered
	 * based on the type match and format type. - If only `singleAnySubType` is
	 * provided, BDBs are filtered based on the sub-type match and format type.
	 * Biometric type is ignored. - If both `biometricType` and `singleAnySubType`
	 * are provided, BDBs are filtered based on a match with both type and sub-type,
	 * and the format type must also match.
	 *
	 * @param birRoot The BIR object containing the biometric data.
	 * @param type    The biometric data type (e.g., "Finger", "Iris") to filter by
	 *                (can be null).
	 * @param subType The sub-type associated with the `type` for further filtering
	 *                (can be null).
	 * @return A map containing all extracted BDB information with key-value pairs
	 *         (key: BDB details + creation date, value: UTF-8 encoded BDB data).
	 * @throws Exception A general exception is thrown if any error occurs during
	 *                   the processing. Consider catching and handling specific
	 *                   exception types for more robust error handling.
	 */
	@SuppressWarnings({ "java:S112", "java:S3776" })
	public static Map<String, String> getAllBDBData(BIR birRoot, String type, String subType) throws Exception {
		if (birRoot == null || birRoot.getBirs() == null || birRoot.getBirs().isEmpty()) {
			return Collections.emptyMap();
		}
		BiometricType biometricType = (type != null) ? getBiometricType(type) : null;
		SingleAnySubtypeType singleAnySubType = (subType != null) ? getSingleAnySubtype(subType) : null;
		Long formatType = (type != null) ? getFormatType(type) : null;

		final String subTypeValue = (singleAnySubType != null) ? singleAnySubType.value() : null;

		Map<String, String> bdbMap = new HashMap<>(birRoot.getBirs().size());
		for (BIR bir : birRoot.getBirs()) {
			if (bir == null) continue;

			BDBInfo bdbInfo = bir.getBdbInfo();
			if (bdbInfo == null || bdbInfo.getFormat() == null) continue;

			List<String> singleSubTypeList = (bdbInfo.getSubtype() == null) ? Collections.emptyList() : bdbInfo.getSubtype();
			List<BiometricType> singleTypeList = bdbInfo.getType();
			long bdbFormatType = Long.parseLong(bdbInfo.getFormat().getType());

			boolean formatMatch = (formatType == null || formatType == bdbFormatType);

			String bdbData = new String(bir.getBdb(), StandardCharsets.UTF_8);
			long timestamp = bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli();

			if (singleAnySubType == null && biometricType != null && singleTypeList.contains(biometricType) && formatMatch) {
				bdbMap.put(biometricType + "_" + String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_" + timestamp,
						bdbData);
			}
			else if (biometricType == null && subTypeValue != null && singleSubTypeList.contains(subTypeValue)) {
				String typeString = String.join(" ", convertToList(singleTypeList));
				bdbMap.put(String.join(" ", singleSubTypeList) + "_" + typeString + "_" + bdbFormatType + "_" + timestamp,
						bdbData);
			}
			else if (biometricType != null && subTypeValue != null &&
					singleTypeList.contains(biometricType) && singleSubTypeList.contains(subTypeValue) && formatMatch) {
				bdbMap.put(subTypeValue + "_" + biometricType + "_" + bdbFormatType + "_" + timestamp, bdbData);
			}
		}

		return bdbMap;
	}

	/**
	 * Retrieves the {@link SingleAnySubtypeType} enum based on the provided string
	 * value.
	 *
	 * This method simply checks if the provided `subType` string is not null and,
	 * if so, attempts to convert it to a SingleAnySubtypeType enum using the
	 * `fromValue` method (may throw IllegalArgumentException).
	 *
	 * @param subType The string value representing the SingleAnySubtypeType.
	 * @return The corresponding SingleAnySubtypeType enum if found, otherwise null.
	 * @throws IllegalArgumentException If the provided string value doesn't match
	 *                                  any existing SingleAnySubtypeType enum name.
	 */
	private static SingleAnySubtypeType getSingleAnySubtype(String subType) {
		if (subType == null || subType.isEmpty()) {
			return null;
		}
		try {
			return SingleAnySubtypeType.fromValue(subType);
		} catch (IllegalArgumentException e) {
			// ✅ Avoid exceptions bubbling up unnecessarily
			return null; // or handle unknown subType mapping gracefully
		}
	}

	/**
	 * Parses BIR (Biometric Information Record) data from a provided XML byte array
	 * and filters based on a biometric type (optional).
	 *
	 * This method utilizes JAXB (Java Architecture for XML Binding) to unmarshal
	 * the XML data from the provided byte array into a BIR object. It then iterates
	 * through the BIRs within the parsed object and filters them based on the
	 * following criteria:
	 *
	 * - If `type` is null, all BIRs are added to the returned list. - If `type` is
	 * not null, only BIRs containing a BDBInfo object with a type list matching the
	 * provided `biometricType` are added.
	 *
	 * The filtered BIRs are added to a new list and returned.
	 *
	 * @param xmlBytes The XML data as a byte array.
	 * @param type     The biometric data type (e.g., "Finger", "Iris") to filter by
	 *                 (can be null).
	 * @return A list containing the filtered BIR objects.
	 * @throws Exception A general exception is thrown if any error occurs during
	 *                   the JAXB unmarshalling process or BIR filtering. Consider
	 *                   catching and handling specific exception types for more
	 *                   robust error handling. @SuppressWarnings("java:S112")
	 *                   Suppresses potential SonarLint warning about unused
	 *                   variable `jaxBir`. This warning might be overly cautious in
	 *                   this context, and the variable is used to hold the
	 *                   unmarshalled data during processing.
	 */
	@SuppressWarnings({ "java:S112" })
	public static List<BIR> getBIRDataFromXMLType(byte[] xmlBytes, String type) throws Exception {
		if (xmlBytes == null || xmlBytes.length == 0) {
			return Collections.emptyList();
		}
		BiometricType biometricType = (type != null && !type.isEmpty()) ? getBiometricType(type) : null;
		Unmarshaller unmarshaller = BIR_CONTEXT.createUnmarshaller();

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlBytes)) {
			JAXBElement<BIR> jaxBir = unmarshaller.unmarshal(new StreamSource(inputStream), BIR.class);
			BIR birRoot = jaxBir.getValue();

			if (birRoot == null || birRoot.getBirs() == null || birRoot.getBirs().isEmpty()) {
				return Collections.emptyList();
			}

			// Filter list efficiently
			List<BIR> updatedBIRList = new ArrayList<>();
			for (BIR bir : birRoot.getBirs()) {
				if (bir == null || biometricType == null) continue;

				BDBInfo bdbInfo = bir.getBdbInfo();
				if (bdbInfo != null) {
					List<BiometricType> biometricTypes = bdbInfo.getType();
					if (biometricTypes != null && !biometricTypes.isEmpty() && biometricTypes.contains(biometricType)) {
						updatedBIRList.add(bir);
					}
				}
			}

			return updatedBIRList;
		}
	}
}