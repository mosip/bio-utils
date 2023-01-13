package io.mosip.biometrics.util.iris;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class IrisEncoder {
	public static byte[] convertIrisImageToISO19794_6_2011(long formatIdentifier, long versionNumber,
			int certificationFlag, Date captureDate, int noOfRepresentations, int representationNo, int noOfEyesPresent,
			int eyeLabel, int imageType, int imageFormat, int horizontalOrientation, int verticalOrientation,
			int compressionType, int width, int height, int bitDepth, int range, int rollAngleOfEye,
			int rollAngleUncertainty, int irisCenterSmallestX, int irisCenterLargestX, int irisCenterSmallestY,
			int irisCenterLargestY, int irisDiameterSmallest, int irisDiameterLargest, int sourceType, int deviceVendor,
			int deviceType, IrisQualityBlock[] qualityBlocks, byte[] imageData, int imageWidth, int imageHeight)
			throws IOException {
		ImageInformation imageInformation = new ImageInformation(eyeLabel, imageType, imageFormat,
				horizontalOrientation, verticalOrientation, compressionType, width, height, bitDepth, range,
				rollAngleOfEye, rollAngleUncertainty, irisCenterSmallestX, irisCenterLargestX, irisCenterSmallestY,
				irisCenterLargestY, irisDiameterSmallest, irisDiameterLargest);

		IrisBDIR irisBDIR = new IrisBDIR(formatIdentifier, versionNumber, certificationFlag, captureDate,
				noOfRepresentations, qualityBlocks, imageInformation, representationNo, noOfEyesPresent, imageData);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(baos);
		irisBDIR.writeObject(outputStream);
		outputStream.flush();
		byte[] data = baos.toByteArray();

		outputStream.close();
		return data;
	}

	public static byte[] convertIrisImageToISO(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_6_2011":
			long formatIdentifier = IrisFormatIdentifier.FORMAT_IIR;
			long versionNumber = IrisVersionNumber.VERSION_020;
			int certificationFlag = IrisCertificationFlag.UNSPECIFIED;

			Date captureDate = new Date();// the date instance

			int algorithmVendorIdentifier = IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED;
			int qualityAlgorithmIdentifier = IrisQualityAlgorithmIdentifier.UNSPECIFIED;
			int eyeLabel = getEyeLabel(convertRequestDto.getBiometricSubType());

			int imageType = convertRequestDto.getPurpose().equalsIgnoreCase("AUTH") ? ImageType.CROPPED_AND_MASKED
					: ImageType.CROPPED;
			int imageFormat = ImageFormat.MONO_JPEG2000;// 0A
			int horizontalOrientation = Orientation.UNDEFINED;
			int verticalOrientation = Orientation.UNDEFINED;
			int compressionType = convertRequestDto.getPurpose().equalsIgnoreCase("AUTH")
					? IrisImageCompressionType.JPEG_LOSSY
					: IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE;
			// System.out.println("Purpose==" + convertRequestDto.getPurpose() + "
			// <<compressionType==" + compressionType);

			int bitDepth = IrisImageBitDepth.BPP_08;

			int range = 0x0000;
			int rollAngleOfEye = 0xFFFF;// ANGLE_UNDEFINED
			int rollAngleUncertainty = 0xFFFF; // UNCERTAIN_UNDEFINED
			int irisCenterSmallestX = 0x0000; // COORDINATE_UNDEFINED
			int irisCenterLargestX = 0x0000; // COORDINATE_UNDEFINED
			int irisCenterSmallestY = 0x0000; // COORDINATE_UNDEFINED
			int irisCenterLargestY = 0x0000; // COORDINATE_UNDEFINED
			int irisDiameterSmallest = 0x0000; // COORDINATE_UNDEFINED
			int irisDiameterLargest = 0x0000; // COORDINATE_UNDEFINED

			int sourceType = IrisCaptureDeviceTechnology.CMOS_OR_CCD;
			int deviceVendor = IrisCaptureDeviceVendor.UNSPECIFIED;
			int deviceType = IrisCaptureDeviceType.UNSPECIFIED;

			int noOfRepresentations = (int) 0x0001;
			int representationNo = (int) 0x0001;
			int noOfEyesPresent = (int) 0x0001;

			int quality = 80;
			IrisQualityBlock[] qualityBlocks = new IrisQualityBlock[] {
					new IrisQualityBlock(quality, algorithmVendorIdentifier, qualityAlgorithmIdentifier) };
			BufferedImage bufferedImage = CommonUtil.getBufferedImage(convertRequestDto);
			int imageWidth = bufferedImage.getWidth();
			int imageHeight = bufferedImage.getHeight();

			return convertIrisImageToISO19794_6_2011(formatIdentifier, versionNumber, certificationFlag, captureDate,
					noOfRepresentations, representationNo, noOfEyesPresent, eyeLabel, imageType, imageFormat,
					horizontalOrientation, verticalOrientation, compressionType, imageWidth, imageHeight, bitDepth,
					range, rollAngleOfEye, rollAngleUncertainty, irisCenterSmallestX, irisCenterLargestX,
					irisCenterSmallestY, irisCenterLargestY, irisDiameterSmallest, irisDiameterLargest, sourceType,
					deviceVendor, deviceType, qualityBlocks, convertRequestDto.getInputBytes(), imageWidth,
					imageHeight);
		}
		throw new UnsupportedOperationException();
	}

	private static byte getEyeLabel(String biometricSubType) {
		if (biometricSubType == null)
			return EyeLabel.UNSPECIFIED;

		switch (biometricSubType) {
		case "Right":
			return EyeLabel.RIGHT;
		case "Left":
			return EyeLabel.LEFT;
		default:
			return EyeLabel.UNSPECIFIED;
		}
	}
}
