package io.mosip.kernel.biometrics.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
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
			if (bir != null) {

				boolean isException = bir.getOthers() != null && bir.getOthers().entrySet().stream()
						.anyMatch(e -> OtherKey.EXCEPTION.equals(e.getKey()) && "true".equals(e.getValue()));

				if ((bir.getBdb() == null || bir.getBdb().length < 1) && !isException)
					throw new CbeffException("BDB value can't be empty");

				if (bir.getBdbInfo() == null)
					throw new CbeffException("BDB information can't be empty");

				BDBInfo bdbInfo = bir.getBdbInfo();
				List<BiometricType> biometricTypes = bdbInfo.getType();
				if (biometricTypes == null || biometricTypes.isEmpty()) {
					throw new CbeffException("Type value needs to be provided");
				}
				if (!validateFormatType(Long.valueOf(bdbInfo.getFormat().getType()), biometricTypes)) {
					throw new CbeffException("Patron Format type is invalid");
				}
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
		BiometricType biometricType = biometricTypes.get(0);
		switch (biometricType.value()) {
		case "Finger":
			return formatType == CbeffConstant.FORMAT_TYPE_FINGER
					|| formatType == CbeffConstant.FORMAT_TYPE_FINGER_MINUTIAE;
		case "Iris":
			return formatType == CbeffConstant.FORMAT_TYPE_IRIS;

		case "ExceptionPhoto":
		case "Face":
			return formatType == CbeffConstant.FORMAT_TYPE_FACE;
		case "HandGeometry":
			return formatType == CbeffConstant.FORMAT_TYPE_FACE;
		default:
			return false;
		}
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
		switch (type.toLowerCase()) {
		case "finger":
			return CbeffConstant.FORMAT_TYPE_FINGER;
		case "iris":
			return CbeffConstant.FORMAT_TYPE_IRIS;
		case "fmr":
			return CbeffConstant.FORMAT_TYPE_FINGER_MINUTIAE;
		case "face":
		case "exceptionphoto":
			return CbeffConstant.FORMAT_TYPE_FACE;
		case "handgeometry":
			return CbeffConstant.FORMAT_TYPE_FACE;
		default:
			return 0;
		}
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
	 * @throws CbeffValidationException If XSD validation fails.
	 * @throws CbeffException           If any other error occurs during the
	 *                                  process.
	 */
	public static byte[] createXMLBytes(BIR bir, byte[] xsd) throws Exception {
		CbeffValidator.validateXML(bir);
		JAXBContext jaxbContext = JAXBContext.newInstance(BIR.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baos);
		jaxbMarshaller.marshal(bir, writer);
		byte[] savedData = baos.toByteArray();
		writer.close();
		try {
			CbeffXSDValidator.validateXML(xsd, savedData);
		} catch (SAXException sax) {
			String message = sax.getMessage();
			message = message.substring(message.indexOf(":"));
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
		JAXBContext jaxbContext = JAXBContext.newInstance(BIR.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		JAXBElement<BIR> jaxBir = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(fileBytes)),
				BIR.class);
		return jaxBir.getValue();
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

		if (type == null && subType == null) {
			return getAllLatestDatafromBIR(bir);
		}
		BiometricType biometricType = null;
		SingleAnySubtypeType singleAnySubType = null;
		Long formatType = null;
		if (type != null) {
			biometricType = getBiometricType(type);
			formatType = getFormatType(type);
		}
		if (subType != null) {
			singleAnySubType = getSingleAnySubtype(subType);
		}
		Map<String, String> bdbMap = new HashMap<>();
		if (bir.getBirs() != null && !bir.getBirs().isEmpty()) {
			populateBDBMap(bir, biometricType, singleAnySubType, formatType, bdbMap);
		}
		Map<String, String> map = new TreeMap<>(bdbMap);
		Map<String, String> finalMap = new HashMap<>();
		for (Map.Entry<String, String> mapEntry : map.entrySet()) {
			String pattern = mapEntry.getKey().substring(0, mapEntry.getKey().lastIndexOf("_"));
			if (mapEntry.getKey().contains(pattern)) {
				finalMap.put(mapEntry.getKey().substring(0, mapEntry.getKey().lastIndexOf("_")), mapEntry.getValue());
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
		for (BIR bir : birRoot.getBirs()) {
			BDBInfo bdbInfo = bir.getBdbInfo();

			if (bdbInfo != null) {
				List<String> singleSubTypeList = bdbInfo.getSubtype() == null ? List.of() : bdbInfo.getSubtype();
				List<BiometricType> biometricTypes = bdbInfo.getType();
				String bdbFormatType = bdbInfo.getFormat().getType();
				boolean formatMatch = Long.valueOf(bdbFormatType).equals(formatType);
				if (singleAnySubType == null && biometricTypes.contains(biometricType) && formatMatch) {
					bdbMap.put(
							(biometricType != null ? biometricType.toString() : null) + "_"
									+ String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_"
									+ bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
							CryptoUtil.encodeBase64String(bir.getBdb()));
				} else if (biometricType == null
						&& singleSubTypeList.contains(singleAnySubType != null ? singleAnySubType.value() : null)) {
					List<String> singleTypeStringList = convertToList(biometricTypes);
					bdbMap.put(String.join(" ", singleTypeStringList) + "_" + String.join(" ", singleSubTypeList) + "_"
							+ bdbFormatType + "_" + bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
							CryptoUtil.encodeBase64String(bir.getBdb()));
				} else if (biometricTypes.contains(biometricType)
						&& singleSubTypeList.contains(singleAnySubType != null ? singleAnySubType.value() : null)
						&& formatMatch) {
					bdbMap.put(
							(biometricType != null ? biometricType.toString() : null) + "_"
									+ (singleAnySubType != null ? singleAnySubType.value() : null) + "_" + bdbFormatType
									+ "_" + bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
							CryptoUtil.encodeBase64String(bir.getBdb()));
				}
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
		Map<String, String> bdbMap = new HashMap<>();
		if (birRoot.getBirs() != null && !birRoot.getBirs().isEmpty()) {
			for (BIR bir : birRoot.getBirs()) {
				BDBInfo bdbInfo = bir.getBdbInfo();

				if (bdbInfo != null) {
					List<String> singleSubTypeList = bdbInfo.getSubtype();
					List<BiometricType> biometricTypes = bdbInfo.getType();
					if (singleSubTypeList.isEmpty()) {
						singleSubTypeList = new ArrayList<>();
						singleSubTypeList.add("No Subtype");
					}
					String bdbFormatType = bdbInfo.getFormat().getType();
					bdbMap.put(
							String.join(" ", biometricTypes.get(0).toString()) + "_"
									+ String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_"
									+ bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
							CryptoUtil.encodeBase64String(bir.getBdb()));
				}
			}
		}
		Map<String, String> map = new TreeMap<>(bdbMap);
		Map<String, String> finalMap = new HashMap<>();
		for (Map.Entry<String, String> mapEntry : map.entrySet()) {
			String pattern = mapEntry.getKey().substring(0, mapEntry.getKey().lastIndexOf("_"));
			if (mapEntry.getKey().contains(pattern)) {
				finalMap.put(mapEntry.getKey().substring(0, mapEntry.getKey().lastIndexOf("_")), mapEntry.getValue());
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
		return biometricTypeList.stream().map(Enum::name).toList();
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
		for (E e : enumClass.getEnumConstants()) {
			if (e.name().equals(value)) {
				return true;
			}
		}
		return false;
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
		BiometricType biometricType = null;
		SingleAnySubtypeType singleAnySubType = null;
		Long formatType = null;
		if (type != null) {
			biometricType = getBiometricType(type);
		}
		if (subType != null) {
			singleAnySubType = getSingleAnySubtype(subType);
		}
		if (type != null) {
			formatType = getFormatType(type);
		}
		Map<String, String> bdbMap = new HashMap<>();
		List<BIR> birs = birRoot.getBirs();
		if (birs != null && !birs.isEmpty()) {
			for (BIR bir : birs) {
				BDBInfo bdbInfo = bir.getBdbInfo();

				if (bdbInfo != null) {
					List<String> singleSubTypeList = bdbInfo.getSubtype();
					List<BiometricType> singleTypeList = bdbInfo.getType();
					String bdbFormatType = bdbInfo.getFormat().getType();
					boolean formatMatch = Long.valueOf(bdbFormatType).equals(formatType);
					if (singleAnySubType == null && singleTypeList.contains(biometricType) && formatMatch) {
						bdbMap.put(
								(biometricType != null ? biometricType.toString() : null) + "_"
										+ String.join(" ", singleSubTypeList) + "_" + bdbFormatType + "_"
										+ bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
								new String(bir.getBdb(), StandardCharsets.UTF_8));
					} else if (biometricType == null
							&& singleSubTypeList.contains(singleAnySubType != null ? singleAnySubType.value() : null)) {
						List<String> singleTypeStringList = convertToList(singleTypeList);
						bdbMap.put(
								String.join(" ", singleSubTypeList) + "_" + String.join(" ", singleTypeStringList) + "_"
										+ bdbFormatType + "_"
										+ bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
								new String(bir.getBdb(), StandardCharsets.UTF_8));
					} else if (singleTypeList.contains(biometricType)
							&& singleSubTypeList.contains(singleAnySubType != null ? singleAnySubType.value() : null)
							&& formatMatch) {
						bdbMap.put((singleAnySubType != null ? singleAnySubType.value() : null) + "_"
								+ (biometricType != null ? biometricType.toString() : null) + "_" + bdbFormatType + "_"
								+ bdbInfo.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
								new String(bir.getBdb(), StandardCharsets.UTF_8));
					}
				}
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
		return subType != null ? SingleAnySubtypeType.fromValue(subType) : null;
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
		BiometricType biometricType = null;
		List<BIR> updatedBIRList = new ArrayList<>();
		JAXBContext jaxbContext = JAXBContext.newInstance(BIR.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		JAXBElement<BIR> jaxBir = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xmlBytes)),
				BIR.class);
		BIR birRoot = jaxBir.getValue();
		for (BIR bir : birRoot.getBirs()) {
			if (type != null) {
				biometricType = getBiometricType(type);
				BDBInfo bdbInfo = bir.getBdbInfo();
				if (bdbInfo != null) {
					List<BiometricType> biometricTypes = bdbInfo.getType();
					if (biometricTypes != null && biometricTypes.contains(biometricType)) {
						updatedBIRList.add(bir);
					}
				}
			}
		}
		return updatedBIRList;
	}
}