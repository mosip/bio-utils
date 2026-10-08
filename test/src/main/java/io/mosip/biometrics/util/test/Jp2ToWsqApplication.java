package io.mosip.biometrics.util.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.finger.FingerDecoder;

/**
 * Sample CLI: convert a finger JP2000 image (or JP2000 payload inside a finger
 * ISO) to both lossy WSQ ({@link CommonUtil#convertJP2ToWSQ(byte[])}) and 8-bit
 * lossless WSQ ({@link CommonUtil#convertJP2ToWSQLossless(byte[])}). Writes
 * {@code .wsq} and {@code .lossless.wsq}, and verifies both with jnbis.
 */
public class Jp2ToWsqApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(Jp2ToWsqApplication.class);

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			LOGGER.error(
					"Usage: Jp2ToWsqApplication <image.type.jp2000=0> <finger.folder.path=...> <file.image=*.jp2|file.iso=*.iso>");
			System.exit(-1);
			return;
		}

		String imageType = args[0];
		LOGGER.info("main :: imageType :: Argument {} ", imageType);
		if (!imageType.contains(ApplicationConstant.IMAGE_TYPE_JP2000)) {
			System.exit(-1);
			return;
		}

		String biometricFolderPath = args[1];
		LOGGER.info("main :: biometricFolderPath :: Argument {} ", biometricFolderPath);
		if (!biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FINGER)) {
			System.exit(-1);
			return;
		}
		biometricFolderPath = biometricFolderPath.split("=")[1];

		String conversionFile = args[2];
		LOGGER.info("main :: conversionFile :: Argument {} ", conversionFile);
		boolean isoInput = conversionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO);
		if (!isoInput && !conversionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_IMAGE)) {
			System.exit(-1);
			return;
		}
		conversionFile = conversionFile.split("=")[1];

		int status = convertToWsq(biometricFolderPath, conversionFile, isoInput);
		if (status != 0) {
			System.exit(status);
		}
	}

	static int convertToWsq(String biometricFolderPath, String conversionFile, boolean isoInput) {
		LOGGER.info("convertToWsq :: Started :: biometricFolderPath :: {} :: conversionFile :: {} :: isoInput :: {}",
				biometricFolderPath, conversionFile, isoInput);
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + conversionFile;
			File initialFile = new File(fileName);
			if (!initialFile.exists()) {
				LOGGER.error("convertToWsq :: file not found :: {}", fileName);
				return 1;
			}

			byte[] source = Files.readAllBytes(Paths.get(fileName));
			byte[] jp2 = isoInput ? extractFingerJp2(source) : source;
			byte[] lossy = CommonUtil.convertJP2ToWSQ(jp2);
			byte[] lossless = CommonUtil.convertJP2ToWSQLossless(jp2);
			if (!isWsq(lossy) || !isWsq(lossless)) {
				LOGGER.error("convertToWsq :: conversion did not produce a WSQ SOI marker");
				return 1;
			}

			BufferedImage decodedLossy = CommonUtil.convertWSQToBufferedImage(lossy);
			BufferedImage decodedLossless = CommonUtil.convertWSQToBufferedImage(lossless);
			if (decodedLossy == null || decodedLossless == null || decodedLossy.getWidth() <= 0
					|| decodedLossless.getWidth() <= 0) {
				LOGGER.error("convertToWsq :: jnbis could not decode the WSQ output");
				return 1;
			}

			File lossyFile = new File(wsqOutputPath(fileName, false));
			File losslessFile = new File(wsqOutputPath(fileName, true));
			try (FileOutputStream out = new FileOutputStream(lossyFile)) {
				out.write(lossy);
			}
			try (FileOutputStream out = new FileOutputStream(losslessFile)) {
				out.write(lossless);
			}
			LOGGER.info(
					"convertToWsq :: wrote lossy {} ({} bytes) and lossless {} ({} bytes) decoded {}x{} from JP2 {} bytes",
					lossyFile.getAbsolutePath(), lossy.length, losslessFile.getAbsolutePath(), lossless.length,
					decodedLossy.getWidth(), decodedLossy.getHeight(), jp2.length);
			return 0;
		} catch (Exception ex) {
			LOGGER.error("convertToWsq :: Error ", ex);
			return 1;
		} finally {
			LOGGER.info("convertToWsq :: Ended :: ");
		}
	}

	private static byte[] extractFingerJp2(byte[] isoData) throws Exception {
		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Finger");
		requestDto.setVersion("ISO19794_4_2011");
		requestDto.setInputBytes(isoData);
		return FingerDecoder.getFingerBDIR(requestDto).getImage();
	}

	private static boolean isWsq(byte[] wsq) {
		return wsq != null && wsq.length >= 4 && (wsq[0] & 0xff) == 0xff && (wsq[1] & 0xff) == 0xa0;
	}

	private static String wsqOutputPath(String inputPath, boolean lossless) {
		String suffix = lossless ? ".lossless.wsq" : ".wsq";
		String lower = inputPath.toLowerCase();
		if (lower.endsWith(".jp2")) {
			return inputPath.substring(0, inputPath.length() - 4) + suffix;
		}
		if (lower.endsWith(".iso")) {
			return inputPath + suffix;
		}
		return inputPath + suffix;
	}
}
