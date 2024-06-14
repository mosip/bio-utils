package io.mosip.biometrics.util.test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ImageType;
import io.mosip.biometrics.util.Modality;

/**
 * BioUtilConvertApplication
 *
 */
public class BioUtilConvertApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(BioUtilConvertApplication.class);

	@SuppressWarnings({ "java:S1854", "java:S3776", "java:S4165" })
	public static void main(String[] args) {
		if (args != null && args.length >= 2) {
			// Argument 0 should contain
			// io.mosip.biometrics.util.image.type.jpeg/io.mosip.biometrics.util.image.type.png"
			ImageType imageType = ImageType.JPEG;
			String imageTypeFileName = "";
			String conversionImageType = args[0];
			LOGGER.info("main :: conversionImageType :: Argument {} ", conversionImageType);
			// 2 or 3
			if (conversionImageType.contains(ApplicationConstant.IMAGE_TYPE_JPEG)) {
				conversionImageType = conversionImageType.split("=")[1];
				imageTypeFileName = "JPEG";
				imageType = ImageType.JPEG;
			} else if (conversionImageType.contains(ApplicationConstant.IMAGE_TYPE_PNG)) {
				conversionImageType = conversionImageType.split("=")[1];
				imageTypeFileName = "PNG";
				imageType = ImageType.PNG;
			} else {
				System.exit(-1);
			}

			// Argument 1 should contain
			// "mosip.mock.sbi.biometric.type.finger.folder.path/mosip.mock.sbi.biometric.type.face.folder.path/mosip.mock.sbi.biometric.type.iris.folder.path"
			String biometricFolderPath = args[1];
			LOGGER.info("main :: biometricFolderPath :: Argument {} ", biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FINGER)
					|| biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FACE)
					|| biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_IRIS)) {
				biometricFolderPath = biometricFolderPath.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 2 should contain
			// "mosip.mock.sbi.biometric.type.file.image/mosip.mock.sbi.biometric.type.file.iso"
			String converionFile = args[2];
			LOGGER.info("main :: converionFile :: Argument {} ", converionFile);
			if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_IMAGE)
					|| converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO)) {
				converionFile = converionFile.split("=")[1];
			} else {
				System.exit(-1);
			}

			if (biometricFolderPath.contains("Face")) {
				doFaceConversion(imageType, imageTypeFileName, biometricFolderPath, converionFile);
			} else if (biometricFolderPath.contains("Iris")) {
				doIrisConversion(imageType, imageTypeFileName, biometricFolderPath, converionFile);
			} else if (biometricFolderPath.contains("Finger")) {
				doFingerConversion(imageType, imageTypeFileName, biometricFolderPath, converionFile);
			}
		}
	}

	@SuppressWarnings({ "java:S2093" })
	public static void doFaceConversion(ImageType inputImageType, String imageTypeFileName, String biometricFolderPath,
			String converionFile) {
		LOGGER.info("doFaceConversion :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: converionFile :: {}", inputImageType, biometricFolderPath, converionFile);
		FileOutputStream tmpOutputStream = null;
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + converionFile;
			File initialFile = new File(fileName);
			if (initialFile.exists()) {
				LOGGER.info("doFaceConversion :: fileName :: {}", fileName);

				byte[] inIsoData = Files.readAllBytes(Paths.get(fileName));
				if (inIsoData != null) {
					byte[] outIsoData = CommonUtil.decodeURLSafeBase64(CommonUtil.convertISOImageType(
							CommonUtil.encodeToURLSafeBase64(inIsoData), Modality.Face, inputImageType));
					if (outIsoData != null) {
						// Write bytes to tmp file.
						File tmpImageFile = new File(
								filePath + biometricFolderPath + converionFile + "_" + imageTypeFileName + ".iso");
						tmpOutputStream = new FileOutputStream(tmpImageFile);
						tmpOutputStream.write(outIsoData);
					} else {
						LOGGER.error("doFaceConversion :: Could Not convert the ISO To ISO ");
					}
				} else {
					LOGGER.error("doFaceConversion :: Could Not convert the ISO To ISO ");
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doFaceConversion :: Error ", ex);
		} finally {
			try {
				if (tmpOutputStream != null)
					tmpOutputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doFaceConversion :: Error ", ex);
			}
		}
		LOGGER.info("doFaceConversion :: Ended :: ");
	}

	@SuppressWarnings({ "java:S2093" })
	public static void doIrisConversion(ImageType inputImageType, String imageTypeFileName, String biometricFolderPath,
			String converionFile) {
		LOGGER.info("doIrisConversion :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: converionFile :: {}", inputImageType, biometricFolderPath, converionFile);

		FileOutputStream tmpOutputStream = null;
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + converionFile;
			File initialFile = new File(fileName);
			if (initialFile.exists()) {
				LOGGER.info("doIrisConversion :: fileName :: {}", fileName);

				byte[] inIsoData = Files.readAllBytes(Paths.get(fileName));
				if (inIsoData != null) {
					byte[] outIsoData = CommonUtil.decodeURLSafeBase64(CommonUtil.convertISOImageType(
							CommonUtil.encodeToURLSafeBase64(inIsoData), Modality.Iris, inputImageType));
					if (outIsoData != null) {
						// Write bytes to tmp file.
						File tmpImageFile = new File(
								filePath + biometricFolderPath + converionFile + "_" + imageTypeFileName + ".iso");
						tmpOutputStream = new FileOutputStream(tmpImageFile);
						tmpOutputStream.write(outIsoData);
					} else {
						LOGGER.error("doIrisConversion :: Could Not convert the ISO To ISO ");
					}
				} else {
					LOGGER.error("doIrisConversion :: Could Not convert the ISO To ISO ");
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doIrisConversion :: Error ", ex);
		} finally {
			try {
				if (tmpOutputStream != null)
					tmpOutputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doIrisConversion :: Error ", ex);
			}
		}
		LOGGER.info("doIrisConversion :: Ended :: ");
	}

	@SuppressWarnings({ "java:S2093" })
	public static void doFingerConversion(ImageType inputImageType, String imageTypeFileName,
			String biometricFolderPath, String converionFile) {
		LOGGER.info("doFingerConversion :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: converionFile :: {}", inputImageType, biometricFolderPath, converionFile);
		FileOutputStream tmpOutputStream = null;
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + converionFile;
			File initialFile = new File(fileName);
			if (initialFile.exists()) {
				LOGGER.info("doFingerConversion :: fileName ::{}", fileName);

				byte[] inIsoData = Files.readAllBytes(Paths.get(fileName));
				if (inIsoData != null) {
					byte[] outIsoData = CommonUtil.decodeURLSafeBase64(CommonUtil.convertISOImageType(
							CommonUtil.encodeToURLSafeBase64(inIsoData), Modality.Finger, inputImageType));
					if (outIsoData != null) {
						// Write bytes to tmp file.
						File tmpImageFile = new File(
								filePath + biometricFolderPath + converionFile + "_" + imageTypeFileName + ".iso");
						tmpOutputStream = new FileOutputStream(tmpImageFile);
						tmpOutputStream.write(outIsoData);
					} else {
						LOGGER.error("doFingerConversion :: Could Not convert the ISO To ISO ");
					}
				} else {
					LOGGER.error("doFingerConversion :: Could Not convert the ISO To ISO ");
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doFingerConversion :: Error ", ex);
		} finally {
			try {
				if (tmpOutputStream != null)
					tmpOutputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doFingerConversion :: Error ", ex);
			}
		}
		LOGGER.info("doFingerConversion :: Ended :: ");
	}
}