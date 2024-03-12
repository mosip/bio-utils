package io.mosip.biometrics.util.face;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class FaceEncoder {
	public static byte[] convertFaceImageToISO19794_5_2011(long formatIdentifier,
			long versionNumber, int certificationFlag, int temporalSemantics, Date captureDate,
			int noOfRepresentations, int noOfLandMarkPoints, int gender, int eyeColour, int hairColour,
			int subjectHeight, int expression, int features, int[] poseAngle, int[] poseAngleUncertainty,
			int faceImageType, int sourceType, int deviceVendor, int deviceType, FaceQualityBlock[] qualityBlock,
			byte[] imageData, int imageWidth, int imageHeight, int imageDataType, int spatialSamplingRateLevel,
			int postAcquisitionProcessing, int crossReference, int imageColourSpace, LandmarkPoints[] landmarkPoints,
			byte[] threeDInformationAndData) throws IOException {
		FacialInformation facialInformation = new FacialInformation(noOfLandMarkPoints, gender, eyeColour, hairColour,
				subjectHeight, features, expression, poseAngle, poseAngleUncertainty);

		ImageInformation imageInformation = new ImageInformation(faceImageType, imageDataType, imageWidth, imageHeight,
				spatialSamplingRateLevel, postAcquisitionProcessing, crossReference, imageColourSpace);

		FaceBDIR faceBDIR = new FaceBDIR(formatIdentifier, versionNumber, certificationFlag, temporalSemantics,
				sourceType, deviceVendor, deviceType, captureDate, noOfRepresentations, qualityBlock, facialInformation,
				landmarkPoints, imageInformation, imageData, threeDInformationAndData);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(baos);
		faceBDIR.writeObject(outputStream);
		outputStream.flush();
		byte[] data = baos.toByteArray();

		outputStream.close();
		return data;
	}

	public static byte[] convertFaceImageToISO(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_5_2011":
			long formatIdentifier = FaceFormatIdentifier.FORMAT_FAC;
			long versionNumber = FaceVersionNumber.VERSION_030;
			int certificationFlag = FaceCertificationFlag.UNSPECIFIED;
			int temporalSequenceFlags = TemporalSequenceFlags.ONE_REPRESENTATION;

			Date captureDate = new Date();// the date instance

			int noOfLandMarkPoints = 0x00;
			int algorithmVendorIdentifier = FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001;
			int qualityAlgorithmIdentifier = FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001;

			int gender = Gender.UNKNOWN;
			int eyeColour = EyeColour.UNSPECIFIED;
			int subjectHeight = HeightCodes.UNSPECIFIED;
			int hairColour = HairColour.UNSPECIFIED;
			int expression = 0;
			int features = Features.FEATURES_ARE_SPECIFIED;
			int[] poseAngle = { 0, 0, 0 };
			int[] poseAngleUncertainty = { 0, 0, 0 };
			int faceImageType = FaceImageType.FULL_FRONTAL;
			int imageColourSpace = ImageColourSpace.BIT_24_RGB;
			int sourceType = FaceCaptureDeviceTechnology.VIDEO_FRAME_ANALOG_CAMERA;
			int deviceVendor = FaceCaptureDeviceVendor.UNSPECIFIED;
			int deviceType = FaceCaptureDeviceType.UNSPECIFIED;

			int spatialSamplingRateLevel = SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180;
			int postAcquisitionProcessing = 0;
			int crossReference = CrossReference.BASIC;
			LandmarkPoints[] landmarkPoints = null;
			int noOfRepresentations = 0x0001;

			int quality = 40;
			FaceQualityBlock[] qualityBlock = new FaceQualityBlock[] {
					new FaceQualityBlock(quality, algorithmVendorIdentifier, qualityAlgorithmIdentifier) };
			int imageDataType = convertRequestDto.getPurpose().equalsIgnoreCase("AUTH") ? ImageDataType.JPEG2000_LOSSY :
				ImageDataType.JPEG2000_LOSS_LESS;
			
			BufferedImage bufferedImage = CommonUtil.getBufferedImage(convertRequestDto);
			int imageWidth = bufferedImage.getWidth();
			int imageHeight = bufferedImage.getHeight();
			byte[] threeDInformationAndData = null;

			return convertFaceImageToISO19794_5_2011(formatIdentifier, versionNumber, certificationFlag,
					temporalSequenceFlags, captureDate, noOfRepresentations, noOfLandMarkPoints, gender, eyeColour,
					hairColour, subjectHeight, expression, features, poseAngle, poseAngleUncertainty,
					faceImageType, sourceType, deviceVendor, deviceType, qualityBlock,
					convertRequestDto.getInputBytes(), imageWidth, imageHeight, imageDataType, spatialSamplingRateLevel,
					postAcquisitionProcessing, crossReference, imageColourSpace, landmarkPoints,
					threeDInformationAndData);
		}
		throw new UnsupportedOperationException();
	}
}
