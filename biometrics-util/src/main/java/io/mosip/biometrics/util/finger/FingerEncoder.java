package io.mosip.biometrics.util.finger;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class FingerEncoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerEncoder.class);

	private static byte [] convertFingerImageToISO19794_4_2011
 		(
			FingerFormatIdentifier formatIdentifier, FingerVersionNumber versionNumber,
			FingerCertificationFlag certificationFlag, 
			FingerCaptureDeviceTechnology sourceType, FingerCaptureDeviceVendor deviceVendor, FingerCaptureDeviceType deviceType,
			Date captureDate, int noOfRepresentations,
			FingerQualityBlock [] qualityBlocks, FingerCertificationBlock[] certificationBlocks, 
			FingerPosition fingerPosition, int representationNo, 
			FingerScaleUnitType scaleUnitType, 
			int captureDeviceSpatialSamplingRateHorizontal, int captureDeviceSpatialSamplingRateVertical, 
			int imageSpatialSamplingRateHorizontal, int imageSpatialSamplingRateVertical,
			FingerImageBitDepth bitDepth, FingerImageCompressionType compressionType,
			FingerImpressionType impressionType, int lineLengthHorizontal, int lineLengthVertical,
			int noOfFingerPresent, byte [] image, 
			SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock, CommentBlock commentBlock
		) throws Exception
	{	
		 FingerBDIR bdir = new FingerBDIR 
			 (
				 formatIdentifier, versionNumber,
				 certificationFlag, sourceType, deviceVendor, deviceType,
				 captureDate, noOfRepresentations,
				 qualityBlocks, certificationBlocks, 
				 fingerPosition, representationNo, 
				 scaleUnitType, captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical, 
				 imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical,
				 bitDepth, compressionType,
				 impressionType, lineLengthHorizontal, lineLengthVertical,
				 noOfFingerPresent, image, 
				 segmentationBlock, annotationBlock, commentBlock
			  );

		 try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream outputStream = new DataOutputStream (baos);) {
			 bdir.writeObject(outputStream);
			 outputStream.flush();
			 return baos.toByteArray();
		 }
	 }

	public static byte [] convertFingerImageToISO(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_4_2011" :
				FingerFormatIdentifier formatIdentifier = FingerFormatIdentifier.FORMAT_FIR;
				FingerVersionNumber versionNumber = FingerVersionNumber.VERSION_020;
				FingerCertificationFlag certificationFlag = FingerCertificationFlag.UNSPECIFIED;
				FingerCaptureDeviceTechnology sourceType = FingerCaptureDeviceTechnology.UNSPECIFIED;
				FingerCaptureDeviceVendor deviceVendor = FingerCaptureDeviceVendor.UNSPECIFIED;
				FingerCaptureDeviceType deviceType = FingerCaptureDeviceType.UNSPECIFIED;
				Date captureDate = new Date ();// the date instance
				int noOfRepresentations= (int)0x0001;

				FingerQualityAlgorithmVendorIdentifier algorithmVendorIdentifier = FingerQualityAlgorithmVendorIdentifier.NIST;
				FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier = FingerQualityAlgorithmIdentifier.NIST;

				int quality = 80;
				FingerQualityBlock [] qualityBlocks = new FingerQualityBlock [] { new FingerQualityBlock ((byte)quality , algorithmVendorIdentifier, qualityAlgorithmIdentifier)};
				FingerCertificationBlock[] certificationBlocks = null;
				FingerPosition fingerPosition = getFingerPosition(convertRequestDto.getBiometricSubType());
				int representationNo = (int)0x0000;
				FingerScaleUnitType scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH;
				int captureDeviceSpatialSamplingRateHorizontal = 500;
				int captureDeviceSpatialSamplingRateVertical = 500;
				int imageSpatialSamplingRateHorizontal = 500;
				int imageSpatialSamplingRateVertical = 500;
				FingerImageBitDepth bitDepth = FingerImageBitDepth.BPP_08;
				FingerImageCompressionType compressionType = convertRequestDto.getImageType() == 0 ? FingerImageCompressionType.JPEG_2000_LOSS_LESS :
						FingerImageCompressionType.WSQ;
				FingerImpressionType impressionType = FingerImpressionType.UNKNOWN;

				BufferedImage bufferedImage = CommonUtil.getBufferedImage(convertRequestDto);
				int lineLengthHorizontal = bufferedImage.getWidth();
				int lineLengthVertical = bufferedImage.getHeight();

				int noOfFingerPresent = (int)0x0001;
				SegmentationBlock segmentationBlock = null;
				AnnotationBlock annotationBlock = null;
				CommentBlock commentBlock = null;

				LOGGER.info("imageWidth :: {} :: imageHeight :: {} :: Size :: {}", lineLengthHorizontal, lineLengthVertical,
						convertRequestDto.getInputBytes().length);
				return convertFingerImageToISO19794_4_2011
						(
								formatIdentifier, versionNumber, certificationFlag,
								sourceType, deviceVendor, deviceType,
								captureDate, noOfRepresentations,
								qualityBlocks, certificationBlocks,
								fingerPosition, representationNo, scaleUnitType,
								captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical,
								imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical,
								bitDepth, compressionType,
								impressionType, lineLengthHorizontal, lineLengthVertical,
								noOfFingerPresent, convertRequestDto.getInputBytes(),
								segmentationBlock, annotationBlock, commentBlock
						);
		}
		throw new UnsupportedOperationException();
	}

	private static FingerPosition getFingerPosition(String biometricSubType) {
	 	if(biometricSubType == null)
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
