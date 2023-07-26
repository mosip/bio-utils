package io.mosip.biometrics.util.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.NistRequestDto;
import io.mosip.biometrics.util.nist.parser.v2011.constant.FingerPositionCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.IrisEyePositionCodes;
import io.mosip.biometrics.util.nist.parser.v2011.dto.BiometricInformationExchange;
import io.mosip.biometrics.util.nist.parser.v2011.dto.PackageCBEFFBiometricDataRecord;
import io.mosip.biometrics.util.nist.parser.v2011.dto.PackageHighResolutionGrayscaleImageRecord;
import io.mosip.biometrics.util.nist.parser.v2011.dto.PackageNonPhotographicImageryRecord;
import io.mosip.kernel.core.util.DateUtils;

/**
 * NistDataQualityAnalyser
 *
 */
public class NistDataQualityAnalyser {
	private long startTime = System.currentTimeMillis();
	private static Logger LOGGER = LoggerFactory.getLogger(NistDataQualityAnalyser.class);
	private static String REQ_TEMPLATE = "{ \"modality\": \"%s\", \"id\": \"%s\", \"type\": \"%s\", \"data\": \"%s\", \"requestTime\": \"%s\", \"version\": \"%s\"}";

	public static void main(String[] args) {
		if (args != null && args.length >= 1) {
			// Argument 0 should contain
			// "mosip.mock.sbi.biometric.type.nist.folder.path"
			String biometricFolderPath = args[0];
			LOGGER.info("main :: biometricFolderPath :: Argument [0] " + biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_NIST)) {
				biometricFolderPath = biometricFolderPath.split("=")[1];
			}

			// Argument 1 should contain
			// "bqat.server.ipaddress"
			String serverIPAddress = args[1];
			LOGGER.info("main :: serverIPAddress :: Argument [1] " + serverIPAddress);
			if (serverIPAddress.contains(ApplicationConstant.BQAT_SERVER_IPADDRESS)) {
				serverIPAddress = serverIPAddress.split("=")[1];
			}

			// Argument 2 should contain
			// "bqat.server.port"
			String serverPort = args[2];
			LOGGER.info("main :: serverPort :: Argument [2] " + serverPort);
			if (serverPort.contains(ApplicationConstant.BQAT_SERVER_PORT)) {
				serverPort = serverPort.split("=")[1];
			}

			// Argument 3 should contain
			// "bqat.server.path"
			String serverPath = args[3];
			LOGGER.info("main :: serverPath :: Argument [3] " + serverPath);
			if (serverPath.contains(ApplicationConstant.BQAT_SERVER_PATH)) {
				serverPath = serverPath.split("=")[1] + "=" + serverPath.split("=")[2];
			}

			// Argument 4 should contain
			// "bqat.content.type"
			String contentType = args[4];
			LOGGER.info("main :: contentType :: Argument [4] " + contentType);
			if (contentType.contains(ApplicationConstant.BQAT_CONTENT_TYPE)) {
				contentType = contentType.split("=")[1];
			}

			// Argument 5 should contain
			// "bqat.content.charset"
			String contentCharset = args[5];
			LOGGER.info("main :: contentCharset :: Argument [5] " + contentCharset);
			if (contentCharset.contains(ApplicationConstant.BQAT_CONTENT_CHARSET)) {
				contentCharset = contentCharset.split("=")[1];
			}

			// Argument 6 should contain
			// "bqat.json.results"
			String results = args[6];
			LOGGER.info("main :: results :: Argument [6] " + results);
			if (results.contains(ApplicationConstant.BQAT_JSON_RESULTS)) {
				results = results.split("=")[1];
			}

			if (biometricFolderPath.contains("Nist")) {
				processNistFiles(biometricFolderPath, serverIPAddress, serverPort, serverPath, contentType,
						contentCharset, results);
			}
		}
	}

	public static synchronized void processNistFiles(String biometricFolderPath, String serverIPAddress,
			String serverPort, String serverPath, String contentType, String contentCharset, String results) {
		LOGGER.info("processNistFiles :: Started :: biometricFolderPath :: " + biometricFolderPath);
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileFolder = filePath + biometricFolderPath;
			File[] fileList = null;
			try {
				fileList = (new File(fileFolder)).listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(".xml");
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long startTime = System.currentTimeMillis();
			for (File file : fileList) {
				long startTimeOneFile = System.currentTimeMillis();
				String fileName = file.getName();
				String personId = getBaseName(fileName);
				LOGGER.info(" **********************************RESULT Started**********************************");
				try {
					long startTimeNist = System.currentTimeMillis();
					NistRequestDto dto = new NistRequestDto();
					dto.setInputBytes(Files.readAllBytes(file.toPath()));
					BiometricInformationExchange nistRecord = CommonUtil.nistParser(dto);
					LOGGER.info(" Decoded Time >>> Time Taken : " + (System.currentTimeMillis() - startTimeNist)
							+ " milliseconds");
					if (nistRecord != null) {
						// Finger Records
						ProcessFingerprintRecords threadFingerprint = new NistDataQualityAnalyser().new ProcessFingerprintRecords("fingerprint", biometricFolderPath, personId, nistRecord, contentType,
								contentCharset, serverIPAddress, serverPort, serverPath, results);
						threadFingerprint.start();
						  
						// Iris Records
						ProcessIrisRecords threadIris = new NistDataQualityAnalyser().new ProcessIrisRecords("iris", biometricFolderPath, personId, nistRecord, contentType,
								contentCharset, serverIPAddress, serverPort, serverPath, results);
						threadIris.start();

						// Face Records
						ProcessFaceRecords threadFace = new NistDataQualityAnalyser().new ProcessFaceRecords("face", biometricFolderPath, personId, nistRecord, contentType,
								contentCharset, serverIPAddress, serverPort, serverPath, results);
						threadFace.start();
					}
					// LOGGER.info(CommonUtil.nistXml(nistRecord));
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("Error:", e);
				}

				LOGGER.info(file.getAbsolutePath() + " >>> Time Taken : "
						+ (System.currentTimeMillis() - startTimeOneFile) + " milliseconds");
				LOGGER.info("                                                                       ");
				LOGGER.info(" **********************************RESULT Ended**********************************");
			}
			long endTime = System.currentTimeMillis();
			LOGGER.info("Time Taken : " + (endTime - startTime) + " milliseconds");

		} catch (Exception ex) {
			LOGGER.info("readNistFiles :: Error ", ex);
		} finally {
		}
		LOGGER.info("readNistFiles :: Ended :: ");
	}

	public abstract class ProcessBiometricRecords implements Runnable {
		private Thread recordThread;
		private String modality, biometricFolderPath, personId, contentType, contentCharset, serverIPAddress,
		serverPort, serverPath, jsonResults;
		private BiometricInformationExchange nistRecord; 
		public ProcessBiometricRecords(String modality, String biometricFolderPath, String personId,
				BiometricInformationExchange nistRecord, String contentType, String contentCharset, String serverIPAddress,
				String serverPort, String serverPath, String jsonResults) {
			this.modality = modality;
			this.biometricFolderPath = biometricFolderPath;
			this.personId = personId;
			this.nistRecord = nistRecord;
			this.contentType = contentType;
			this.contentCharset = contentCharset;
			this.serverIPAddress = serverIPAddress;
			this.serverPort = serverPort;
			this.serverPath = serverPath;
			this.jsonResults = jsonResults;			
		}
		
		public Thread getRecordThread() {
			return recordThread;
		}

		public void setRecordThread(Thread recordThread) {
			this.recordThread = recordThread;
		}

		public String getModality() {
			return modality;
		}

		public void setModality(String modality) {
			this.modality = modality;
		}

		public String getBiometricFolderPath() {
			return biometricFolderPath;
		}

		public void setBiometricFolderPath(String biometricFolderPath) {
			this.biometricFolderPath = biometricFolderPath;
		}

		public String getPersonId() {
			return personId;
		}

		public void setPersonId(String personId) {
			this.personId = personId;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getContentCharset() {
			return contentCharset;
		}

		public void setContentCharset(String contentCharset) {
			this.contentCharset = contentCharset;
		}

		public String getServerIPAddress() {
			return serverIPAddress;
		}

		public void setServerIPAddress(String serverIPAddress) {
			this.serverIPAddress = serverIPAddress;
		}

		public String getServerPort() {
			return serverPort;
		}

		public void setServerPort(String serverPort) {
			this.serverPort = serverPort;
		}

		public String getServerPath() {
			return serverPath;
		}

		public void setServerPath(String serverPath) {
			this.serverPath = serverPath;
		}

		public String getJsonResults() {
			return jsonResults;
		}

		public void setJsonResults(String jsonResults) {
			this.jsonResults = jsonResults;
		}

		public BiometricInformationExchange getNistRecord() {
			return nistRecord;
		}

		public void setNistRecord(BiometricInformationExchange nistRecord) {
			this.nistRecord = nistRecord;
		}
		
		public void start() {
			/*
			if (getRecordThread() == null) {
				setRecordThread(new Thread(this, getModality()));
				getRecordThread().start();
			}
			*/
			run();
		}

		public synchronized JSONObject getQualityInfoWithJson(String id, String type, String data) {
			OkHttpClient client = new OkHttpClient();
			client.setConnectTimeout(300, TimeUnit.SECONDS); // connect timeout
			client.setReadTimeout(300, TimeUnit.SECONDS);    // socket timeout
			
			String requestBody = String.format(REQ_TEMPLATE, getModality(), id, type, data,
					DateUtils.getUTCCurrentDateTime(), "1.0.0");

			// LOGGER.info("requestBody>>"+ requestBody);

			MediaType mediaType = MediaType.parse(getContentType() + "; charset=" + getContentCharset());
			RequestBody body = RequestBody.create(mediaType, requestBody);
			Request request = new Request.Builder()
					.url("http://" + getServerIPAddress() + "" + getServerPort() + "" + getServerPath()).post(body).build();
			
			Response response = null;
			try {
				response = client.newCall(request).execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (response != null && response.isSuccessful()) {
				String jsonResponse;
				try {
					jsonResponse = response.body().string();
					LOGGER.info("jsonResponse>>"+ jsonResponse);
					JSONObject jsonObject = new JSONObject(jsonResponse);
					jsonObject = jsonObject.getJSONObject(jsonResults);
					return jsonObject;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    return null;
		}
		
		public synchronized void writeToCsvFile(CsvSchema csvSchema,
				JsonNode jsonNode) throws StreamWriteException, DatabindException, IOException {
			synchronized (NistDataQualityAnalyser.class) {
				String filePath = new File(".").getCanonicalPath();
				String fileFolder = filePath + getBiometricFolderPath();

				CsvMapper csvMapper = new CsvMapper();
				csvMapper.configure(Feature.IGNORE_UNKNOWN, true);
				csvMapper.configure(Feature.AUTO_CLOSE_TARGET, true);
				csvMapper.configure(Feature.AUTO_CLOSE_JSON_CONTENT, true);
				csvMapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
				csvMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

				File outputFile = null;
				OutputStream outstream = null;
				switch (getModality()) {
				case "face":
					outputFile = new File(fileFolder + File.separator + "data_face.csv");
					break;
				case "fingerprint":
					outputFile = new File(fileFolder + File.separator + "data_finger.csv");
					break;
				case "iris":
					outputFile = new File(fileFolder + File.separator + "data_iris.csv");
					break;
				default:
					break;
				}
				if (!outputFile.exists()) {
					outputFile.createNewFile();
				}
				outstream = new FileOutputStream(outputFile, true);
				csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValue(outstream, jsonNode);
				outstream.flush();
				outstream.close();
			}
		}
		
		public synchronized CsvSchema getCsvSchema() {
			CsvSchema.Builder builder = null;
			switch (getModality()) {
			case "face":
				builder = CsvSchema.builder().addColumn("file").addColumn("background_deviation")
						.addColumn("background_grayness").addColumn("blur").addColumn("blur_face").addColumn("focus")
						.addColumn("focus_face").addColumn("openbr_IPD").addColumn("openbr_confidence")
						.addColumn("opencv_IPD").addColumn("opencv_eye_count").addColumn("opencv_face_found")
						.addColumn("opencv_face_height").addColumn("opencv_face_width")
						.addColumn("opencv_frontal_face_found").addColumn("opencv_landmarks_count")
						.addColumn("opencv_mouth_count").addColumn("opencv_nose_count")
						.addColumn("opencv_profile_face_found").addColumn("over_exposure").addColumn("over_exposure_face")
						.addColumn("quality").addColumn("sap_code").addColumn("skin_ratio_face")
						.addColumn("skin_ratio_full").addColumn("image_area").addColumn("image_channels")
						.addColumn("image_height").addColumn("image_ratio").addColumn("image_width")
						.addColumn("openbr_left_eye_x").addColumn("openbr_left_eye_y").addColumn("openbr_right_eye_x")
						.addColumn("openbr_right_eye_y").addColumn("opencv_face_center_of_mass_x")
						.addColumn("opencv_face_center_of_mass_y").addColumn("opencv_face_offset_x")
						.addColumn("opencv_face_offset_y").addColumn("opencv_face_x").addColumn("opencv_face_y")
						.addColumn("opencv_left_eye_x").addColumn("opencv_left_eye_y").addColumn("opencv_mouth_x")
						.addColumn("opencv_mouth_y").addColumn("opencv_nose_x").addColumn("opencv_nose_y")
						.addColumn("opencv_right_eye_x").addColumn("opencv_right_eye_y");
				break;
			case "fingerprint":
				builder = CsvSchema.builder().addColumn("file").addColumn("Width").addColumn("Height").addColumn("NFIQ2")
						.addColumn("Quantized").addColumn("Resampled").addColumn("UniformImage")
						.addColumn("EmptyImageOrContrastTooLow").addColumn("FingerprintImageWithMinutiae")
						.addColumn("SufficientFingerprintForeground").addColumn("EdgeStd").addColumn("converted");
				break;
			case "iris":
				builder = CsvSchema.builder().addColumn("file").addColumn("quality").addColumn("contrast")
						.addColumn("iris_diameter").addColumn("iris_pupil_gs").addColumn("iris_sclera_gs")
						.addColumn("normalized_contrast").addColumn("normalized_iris_diameter")
						.addColumn("normalized_iris_pupil_gs").addColumn("normalized_iris_sclera_gs")
						.addColumn("normalized_percent_visible_iris").addColumn("normalized_sharpness")
						.addColumn("percent_visible_iris").addColumn("sharpness").addColumn("image_height")
						.addColumn("image_width").addColumn("iris_center_x").addColumn("iris_center_y");
				break;
			default:
				builder = null;
				break;
			}

			// return builder.build().withHeader();
			return builder.build().withoutHeader();
		}
		
		public synchronized String getBiometricSubTypeName(Integer code) {
			String subTypeCode = "UNKNOWN";
			switch (getModality()) {
			case "fingerprint":
				switch (code) {
				case FingerPositionCodes.UNKNOWN_FINGER:
					subTypeCode = "UNKNOWN";
					break;
				case FingerPositionCodes.RIGHT_THUMB:
					subTypeCode = "RIGHT_THUMB";
					break;
				case FingerPositionCodes.RIGHT_INDEX_FINGER:
					subTypeCode = "RIGHT_INDEX";
					break;
				case FingerPositionCodes.RIGHT_MIDDLE_FINGER:
					subTypeCode = "RIGHT_MIDDLE";
					break;
				case FingerPositionCodes.RIGHT_RING_FINGER:
					subTypeCode = "RIGHT_RING";
					break;
				case FingerPositionCodes.RIGHT_LITTLE_FINGER:
					subTypeCode = "RIGHT_LITTLE";
					break;
				case FingerPositionCodes.LEFT_THUMB:
					subTypeCode = "LEFT_THUMB";
					break;
				case FingerPositionCodes.LEFT_INDEX_FINGER:
					subTypeCode = "LEFT_INDEX";
					break;
				case FingerPositionCodes.LEFT_MIDDLE_FINGER:
					subTypeCode = "LEFT_MIDDLE";
					break;
				case FingerPositionCodes.LEFT_RING_FINGER:
					subTypeCode = "LEFT_RING";
					break;
				case FingerPositionCodes.LEFT_LITTLE_FINGER:
					subTypeCode = "LEFT_LITTLE";
					break;
				}
				break;
			case "iris":
				switch (code) {
				case IrisEyePositionCodes.UNDEFINED:
					subTypeCode = "UNKNOWN";
					break;
				case IrisEyePositionCodes.RIGHT:
					subTypeCode = "RIGHT";
					break;
				case IrisEyePositionCodes.LEFT:
					subTypeCode = "LEFT";
					break;
				}
				break;
			case "face":
				subTypeCode = "FULL";
				break;
			}
			return subTypeCode;
		}
		
		public synchronized JsonNode toJsonNode(JSONObject jsonObject) throws JsonMappingException, JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readTree(jsonObject.toString());
		}
	}
	
	public class ProcessFingerprintRecords extends ProcessBiometricRecords
	{
		public ProcessFingerprintRecords(String modality, String biometricFolderPath, String personId,
				BiometricInformationExchange nistRecord, String contentType, String contentCharset, String serverIPAddress,
				String serverPort, String serverPath, String jsonResults) {
			super (modality, biometricFolderPath, personId, nistRecord, contentType, contentCharset, serverIPAddress, serverPort, serverPath, jsonResults);			
		}

		@Override
		public void run() {
			for (PackageHighResolutionGrayscaleImageRecord fingerRecord : getNistRecord()
					.getHighResolutionGrayscaleImageRecordList()) {
				String biometricSubType = getBiometricSubTypeName(
						fingerRecord.getFingerprintImage().getFpiPos().getFingerPosCodeList().get(0));
				String imageBase64 = fingerRecord.getFingerprintImage().getBinaryBase64();

				JSONObject result = null;
				while (result == null)
				{
					result = getQualityInfoWithJson(getModality() + "#SEP#" + getPersonId() + "#SEP#"  + biometricSubType, "wsq",
							imageBase64);
				}

				try {
					writeToCsvFile(getCsvSchema(), toJsonNode(result));
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}

	public class ProcessIrisRecords extends ProcessBiometricRecords
	{
		public ProcessIrisRecords(String modality, String biometricFolderPath, String personId,
				BiometricInformationExchange nistRecord, String contentType, String contentCharset, String serverIPAddress,
				String serverPort, String serverPath, String jsonResults) {
			super (modality, biometricFolderPath, personId, nistRecord, contentType, contentCharset, serverIPAddress, serverPort, serverPath, jsonResults);			
		}

		@Override
		public void run() {
			for (PackageCBEFFBiometricDataRecord irisRecord : getNistRecord().getIrisImageRecordList()) {
				String biometricSubType = getBiometricSubTypeName(irisRecord.getIrisImage().getIrisEyePosCode().getValue());
				String imageBase64 = irisRecord.getIrisImage().getBinaryBase64();

				JSONObject result = null;
				while (result == null)
				{
					result = getQualityInfoWithJson(getModality() + "#SEP#" + getPersonId() + "#SEP#"  + biometricSubType, "jpeg",
							imageBase64);
				}

				try {
					writeToCsvFile(getCsvSchema(), toJsonNode(result));
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}

	public class ProcessFaceRecords extends ProcessBiometricRecords
	{
		public ProcessFaceRecords(String modality, String biometricFolderPath, String personId,
				BiometricInformationExchange nistRecord, String contentType, String contentCharset, String serverIPAddress,
				String serverPort, String serverPath, String jsonResults) {
			super (modality, biometricFolderPath, personId, nistRecord, contentType, contentCharset, serverIPAddress, serverPort, serverPath, jsonResults);			
		}

		@Override
		public void run() {
			for (PackageNonPhotographicImageryRecord faceRecord : getNistRecord().getFacialAndSMTImageRecordList()) {
				String biometricSubType = "FULL";
				String imageBase64 = faceRecord.getFaceImage().getBinaryBase64();

				JSONObject result = null;
				while (result == null)
				{
					result = getQualityInfoWithJson(getModality() + "#SEP#" + getPersonId() + "#SEP#"  + biometricSubType, "jpeg",
							imageBase64);
				}

				try {
					writeToCsvFile(getCsvSchema(), toJsonNode(result));
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}

	/**
	 * Gets the base name, without extension, of given file name.
	 * <p/>
	 * e.g. getBaseName("file.txt") will return "file"
	 *
	 * @param fileName
	 * @return the base name
	 */
	public static String getBaseName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return fileName;
		} else {
			return fileName.substring(0, index);
		}
	}
}