package io.mosip.biometrics.util.test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.CommonUtil;

/**
 * BioAuthDecoderValueCreaterApplication
 *
 */
public class BioAuthDecoderValueCreaterApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(BioAuthDecoderValueCreaterApplication.class);

	@SuppressWarnings({ "java:S3776" })
	public static void main(String[] args) {
		if (args != null && args.length >= 6) {
			// Argument 0 should contain "mosip.mock.sbi.biometric.transaction.id"
			String transactionId = args[0];
			LOGGER.info("main :: MOSIP_TRANSACTION_ID :: Argument {} ", transactionId);
			if (transactionId.contains(ApplicationConstant.MOSIP_TRANSACTION_ID)) {
				transactionId = transactionId.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 1 should contain "mosip.mock.sbi.biometric.time.stamp"
			String timeStamp = args[1];
			LOGGER.info("main :: MOSIP_TIME_STAMP :: Argument {} ", timeStamp);
			if (timeStamp.contains(ApplicationConstant.MOSIP_TIME_STAMP)) {
				timeStamp = timeStamp.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 2 should contain "mosip.mock.sbi.biometric.thumb.print"
			String thumbPrint = args[2];
			LOGGER.info("main :: MOSIP_THUMB_PRINT :: Argument {} ", thumbPrint);
			if (thumbPrint.contains(ApplicationConstant.MOSIP_THUMB_PRINT)) {
				thumbPrint = thumbPrint.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 3 should contain "mosip.mock.sbi.biometric.session.key"
			String sessionKey = args[3];
			LOGGER.info("main :: MOSIP_SESSION_KEY :: Argument {} ", sessionKey);
			if (sessionKey.contains(ApplicationConstant.MOSIP_SESSION_KEY)) {
				sessionKey = sessionKey.split("=")[1];
			} else {
				System.exit(-1);
			}

			String keySplitter = ApplicationConstant.MOSIP_KEY_SPLITTER;
			LOGGER.info("main :: MOSIP_KEY_SPLITTER {}", keySplitter);

			// Argument 4 should contain
			// "mosip.mock.sbi.biometric.type.finger.folder.path/mosip.mock.sbi.biometric.type.face.folder.path/mosip.mock.sbi.biometric.type.iris.folder.path"
			String biometricFolderPath = args[4];
			LOGGER.info("main :: biometricFolderPath :: Argument {}", biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FINGER) || 
					biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FACE) || 
					biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_IRIS)) {
				biometricFolderPath = biometricFolderPath.split("=")[1];
			}

			// Argument 5 should contain "mosip.mock.sbi.biometric.type.file.iso"
			String decodeFile = args[5];
			LOGGER.info("main :: converionFile :: Argument {} ", decodeFile);
			if (decodeFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO)) {
				decodeFile = decodeFile.split("=")[1];
			}

			doBioAuthDecodeValueCreator(biometricFolderPath, decodeFile, timeStamp, transactionId, thumbPrint,
					sessionKey, keySplitter);
		}
	}

	@SuppressWarnings({ "java:S2629" })
	public static void doBioAuthDecodeValueCreator(String biometricFolderPath, String decodeFile, String timestamp,
			String transactionId, String thumbprint, String sessionKey, String keySplitter) {
		LOGGER.info("doBioAuthDecodeValueCreator :: Started :: biometricFolderPath :: {} :: decodeFile :: {}",
				biometricFolderPath, decodeFile);
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + decodeFile;
			File initialFile = new File(fileName);
			if (initialFile.exists()) {
				LOGGER.info("doBioAuthDecodeValueCreator :: fileName :: {}", fileName);

				byte[] encryptedData = Files.readAllBytes(Paths.get(fileName));

				byte[] xorResult = CommonUtil.getXOR(timestamp, transactionId);
				byte[] aadBytes = CommonUtil.getLastBytes(xorResult, 16);
				byte[] ivBytes = CommonUtil.getLastBytes(xorResult, 12);

				byte[] thumbprintArr = CommonUtil.hexStringToByteArray(thumbprint);
				byte[] sessionKeyArr = CommonUtil.decodeURLSafeBase64(sessionKey);
				byte[] keySplitterArr = CommonUtil.toUtf8ByteArray(keySplitter);
				byte[] dataArr = CommonUtil.decodeURLSafeBase64(encryptedData);

				String encodedData = CommonUtil.encodeToURLSafeBase64(
						CommonUtil.concatByteArrays(thumbprintArr, sessionKeyArr, keySplitterArr, dataArr));

				LOGGER.info("doBioAuthDecodeValueCreator :: Data :: Starts");
				LOGGER.info("Salt ::\t{}", CommonUtil.encodeToURLSafeBase64(ivBytes));
				LOGGER.info("aad ::\t{}", CommonUtil.encodeToURLSafeBase64(aadBytes));
				LOGGER.info("EncodedData ::\t{}", encodedData);

				LOGGER.info("doBioAuthDecodeValueCreator :: Data :: Ends");
			}
		} catch (Exception ex) {
			LOGGER.info("doBioAuthDecodeValueCreator :: Error ", ex);
		}
		LOGGER.info("doBioAuthDecodeValueCreator :: Ended :: ");
	}
}