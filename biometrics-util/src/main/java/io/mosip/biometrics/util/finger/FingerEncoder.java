package io.mosip.biometrics.util.finger;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class FingerEncoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerEncoder.class);

	public static byte[] convertFingerImageToISO19794_4_2011(long formatIdentifier,
			long versionNumber, int certificationFlag, int sourceType, int deviceVendor, int deviceType,
			Date captureDate, int noOfRepresentations, FingerQualityBlock[] qualityBlocks,
			FingerCertificationBlock[] certificationBlocks, int fingerPosition, int representationNo, int scaleUnitType,
			int captureDeviceSpatialSamplingRateHorizontal, int captureDeviceSpatialSamplingRateVertical,
			int imageSpatialSamplingRateHorizontal, int imageSpatialSamplingRateVertical, int bitDepth,
			int compressionType, int impressionType, int lineLengthHorizontal, int lineLengthVertical,
			int noOfFingerPresent, byte[] image, SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock,
			CommentBlock[] commentBlocks) throws Exception {
		FingerBDIR bdir = new FingerBDIR(formatIdentifier, versionNumber, certificationFlag, sourceType, deviceVendor,
				deviceType, captureDate, noOfRepresentations, qualityBlocks, certificationBlocks, fingerPosition,
				representationNo, scaleUnitType, captureDeviceSpatialSamplingRateHorizontal,
				captureDeviceSpatialSamplingRateVertical, imageSpatialSamplingRateHorizontal,
				imageSpatialSamplingRateVertical, bitDepth, compressionType, impressionType, lineLengthHorizontal,
				lineLengthVertical, noOfFingerPresent, image, segmentationBlock, annotationBlock, commentBlocks);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream outputStream = new DataOutputStream(baos);) {
			bdir.writeObject(outputStream);
			outputStream.flush();
			return baos.toByteArray();
		}
	}

	public static byte[] convertFingerImageToISO(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_4_2011":
			long formatIdentifier = FingerFormatIdentifier.FORMAT_FIR;
			long versionNumber = FingerVersionNumber.VERSION_020;
			int certificationFlag = FingerCertificationFlag.UNSPECIFIED;
			int sourceType = FingerCaptureDeviceTechnology.UNSPECIFIED;
			int deviceVendor = FingerCaptureDeviceVendor.UNSPECIFIED;
			int deviceType = FingerCaptureDeviceType.UNSPECIFIED;
			Date captureDate = new Date();// the date instance
			int noOfRepresentations = 0x0001;

			int algorithmVendorIdentifier = FingerQualityAlgorithmVendorIdentifier.NIST;
			int qualityAlgorithmIdentifier = FingerQualityAlgorithmIdentifier.NIST;

			int quality = 50;
			FingerQualityBlock[] qualityBlocks = new FingerQualityBlock[] {
					new FingerQualityBlock(quality, algorithmVendorIdentifier, qualityAlgorithmIdentifier) };
			FingerCertificationBlock[] certificationBlocks = null;
			int fingerPosition = getFingerPosition(convertRequestDto.getBiometricSubType());
			int representationNo = 0x00;
			int scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH;
			int captureDeviceSpatialSamplingRateHorizontal = 500;
			int captureDeviceSpatialSamplingRateVertical = 500;
			int imageSpatialSamplingRateHorizontal = 500;
			int imageSpatialSamplingRateVertical = 500;
			int bitDepth = FingerImageBitDepth.BPP_08;
			int compressionType =  convertRequestDto.getPurpose().equalsIgnoreCase("AUTH") ? convertRequestDto.getImageType() == 0 ? FingerImageCompressionType.JPEG_2000_LOSSY
					: FingerImageCompressionType.WSQ : FingerImageCompressionType.JPEG_2000_LOSS_LESS;
			int impressionType = FingerImpressionType.UNKNOWN;

			BufferedImage bufferedImage = CommonUtil.getBufferedImage(convertRequestDto);
			int lineLengthHorizontal = bufferedImage.getWidth();
			int lineLengthVertical = bufferedImage.getHeight();

			int noOfFingerPresent = 0x01;
			SegmentationBlock segmentationBlock = null;
			AnnotationBlock annotationBlock = null;
			CommentBlock[] commentBlocks = null;

			//LOGGER.info("imageWidth :: {} :: imageHeight :: {} :: Size :: {}", lineLengthHorizontal, lineLengthVertical, convertRequestDto.getInputBytes().length);
			return convertFingerImageToISO19794_4_2011(formatIdentifier, versionNumber, certificationFlag, sourceType,
					deviceVendor, deviceType, captureDate, noOfRepresentations, qualityBlocks, certificationBlocks,
					fingerPosition, representationNo, scaleUnitType, captureDeviceSpatialSamplingRateHorizontal,
					captureDeviceSpatialSamplingRateVertical, imageSpatialSamplingRateHorizontal,
					imageSpatialSamplingRateVertical, bitDepth, compressionType, impressionType, lineLengthHorizontal,
					lineLengthVertical, noOfFingerPresent, convertRequestDto.getInputBytes(), segmentationBlock,
					annotationBlock, commentBlocks);
		}
		throw new UnsupportedOperationException();
	}

	private static int getFingerPosition(String biometricSubType) {
		if (biometricSubType == null)
			return FingerPosition.UNKNOWN;

		switch (biometricSubType) {
		case "Right Thumb":
			return FingerPosition.RIGHT_THUMB;
		case "Right IndexFinger":
			return FingerPosition.RIGHT_INDEX_FINGER;
		case "Right MiddleFinger":
			return FingerPosition.RIGHT_MIDDLE_FINGER;
		case "Right RingFinger":
			return FingerPosition.RIGHT_RING_FINGER;
		case "Right LittleFinger":
			return FingerPosition.RIGHT_LITTLE_FINGER;
		case "Left Thumb":
			return FingerPosition.LEFT_THUMB;
		case "Left IndexFinger":
			return FingerPosition.LEFT_INDEX_FINGER;
		case "Left MiddleFinger":
			return FingerPosition.LEFT_MIDDLE_FINGER;
		case "Left RingFinger":
			return FingerPosition.LEFT_RING_FINGER;
		case "Left LittleFinger":
			return FingerPosition.LEFT_LITTLE_FINGER;
		default:
			return FingerPosition.UNKNOWN;
		}
	}
}
