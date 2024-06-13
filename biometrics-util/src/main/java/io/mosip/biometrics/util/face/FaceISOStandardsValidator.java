package io.mosip.biometrics.util.face;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.ISOStandardsValidator;
import io.mosip.biometrics.util.ImageDecoderRequestDto;
import io.mosip.biometrics.util.Purposes;

public class FaceISOStandardsValidator extends ISOStandardsValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceISOStandardsValidator.class);

	private static FaceISOStandardsValidator instance = null;

	// Constructor
	private FaceISOStandardsValidator() {
		super();
	}

	// Static method to create instance of Singleton class
	public static FaceISOStandardsValidator getInstance() {
		if (instance == null)
			instance = new FaceISOStandardsValidator();

		return instance;
	}

	public boolean isValidFormatIdentifier(long formatIdentifier) {
		return (formatIdentifier == FaceFormatIdentifier.FORMAT_FAC);
	}

	public boolean isValidVersionNumber(long versionNumber) {
		return (versionNumber == FaceVersionNumber.VERSION_030);
	}

	public boolean isValidRecordLength(long dataLength, long recordLength) {
		return (dataLength == recordLength);
	}

	public boolean isValidNoOfRepresentations(int noOfRepresentations) {
		return (noOfRepresentations == 0x0001);
	}

	public boolean isValidCertificationFlag(int certificationFlag) {
		return (certificationFlag == FaceCertificationFlag.UNSPECIFIED);
	}

	public boolean isValidTemporalSemantics(int temporalSemantics) {
		return (temporalSemantics == TemporalSequenceFlags.ONE_REPRESENTATION);
	}

	public boolean isValidRepresentationLength(long representationLength) {
		// The minimum value of the Representation Length is 51 bytes, consisting of a
		// minimum 51 bytes for the
		// Representation Header plus the size of the Representation Data, i.e. minimum
		// 4 bytes for the Length of Image
		// Data Block field assuming 0 bytes for the variable data.
		return (representationLength >= 0x00000033 && representationLength <= Long.decode("0xFFFFFFEF"));
	}

	public boolean isValidCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		return ((captureDeviceTechnologyIdentifier >= FaceCaptureDeviceTechnology.UNSPECIFIED
				&& captureDeviceTechnologyIdentifier <= FaceCaptureDeviceTechnology.VIDEO_FRAME_DIGITAL_CAMERA)
				|| (captureDeviceTechnologyIdentifier >= FaceCaptureDeviceTechnology.VENDOR_80
						&& captureDeviceTechnologyIdentifier <= FaceCaptureDeviceTechnology.VENDOR_FF));
	}

	public boolean isValidCaptureDeviceVendor(int captureDeviceVendor) {
		return (captureDeviceVendor >= FaceCaptureDeviceVendor.UNSPECIFIED
				&& captureDeviceVendor <= FaceCaptureDeviceVendor.VENDOR_FFFF);
	}

	public boolean isValidCaptureDeviceType(int captureDeviceType, int captureDeviceVendor) {
		if (captureDeviceType >= FaceCaptureDeviceType.UNSPECIFIED
				&& captureDeviceType <= FaceCaptureDeviceType.VENDOR_FFFF) {
			if (captureDeviceType == FaceCaptureDeviceType.UNSPECIFIED) {
				if (captureDeviceVendor == FaceCaptureDeviceVendor.UNSPECIFIED)
					return true;
			} else
				return true;
		}
		return false;
	}

	public boolean isValidNoOfQualityBlocks(int noOfQualityBlocks) {
		return (noOfQualityBlocks >= 0x00 && noOfQualityBlocks <= 0xFF);
	}

	public boolean isValidQualityAlgorithmIdentifier(int qualityAlgorithmIdentifier) {
		return (qualityAlgorithmIdentifier >= FaceQualityAlgorithmIdentifier.UNSPECIFIED
				&& qualityAlgorithmIdentifier <= FaceQualityAlgorithmIdentifier.VENDOR_FFFF);
	}

	public boolean isValidQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		return (qualityAlgorithmVendorIdentifier >= FaceQualityAlgorithmVendorIdentifier.UNSPECIFIED
				&& qualityAlgorithmVendorIdentifier <= FaceQualityAlgorithmVendorIdentifier.VENDOR_FFFF);
	}

	public boolean isValidQualityScore(int qualityScore) {
		return ((qualityScore >= 0x00 && qualityScore <= 0x64) || qualityScore == 0xFF);
	}

	public boolean isValidNoOfLandmarkPoints(int noOfLandmarkPoints) {
		return (noOfLandmarkPoints >= 0x0000 && noOfLandmarkPoints <= 0xFFFF);
	}

	public boolean isValidGender(int gender) {
		return (gender == Gender.UNSPECIFIED || gender == Gender.MALE || gender == Gender.FEMALE || gender == Gender.UNKNOWN);
	}

	public boolean isValidEyeColour(int eyeColour) {
		return ((eyeColour >= EyeColour.UNSPECIFIED && eyeColour <= EyeColour.PINK)
				|| eyeColour == EyeColour.OTHER_OR_UNKNOWN);
	}

	public boolean isValidHairColour(int hairColour) {
		return ((hairColour >= HairColour.UNSPECIFIED && hairColour <= HairColour.RED)
				|| (hairColour == HairColour.UNKNOWN));
	}

	public boolean isValidSubjectHeight(int subjectHeight) {
		return (subjectHeight >= 0x00 && subjectHeight <= 0xFF);
	}

	public boolean isValidFeatureMask(int featureMask) {
		return (featureMask >= 0x000000 && featureMask <= 0xFFFFFF);
	}

	public boolean isValidExpressionMask(int expressionMask) {
		return (expressionMask >= 0x0000 && expressionMask <= 0xFFFF);
	}

	/*
	 * Future implementation
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidPoseAngle(int[] poseAngle) {
		return true;
	}

	/*
	 * Future implementation
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidPoseAngleUncertainty(int[] poseAngleUncertainty) {
		return true;
	}

	/*
	 * Future implementation for LandmarkPointType
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidLandmarkPointType(int landmarkPointType) {
		return true;
	}

	/*
	 * Future implementation for LandmarkPointCode
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidLandmarkPointCode(int landmarkPointType, int landmarkPointCode) {
		return true;
	}

	/*
	 * Future implementation for LandmarkXCooridinate
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidLandmarkXCooridinate(int landmarkPointType, int landmarkPointCode, int xCooridinate) {
		return true;
	}

	/*
	 * Future implementation for LandmarkYCooridinate
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidLandmarkYCooridinate(int landmarkPointType, int landmarkPointCode, int yCooridinate) {
		return true;
	}

	/*
	 * Future implementation for LandmarkZCooridinate
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidLandmarkZCooridinate(int landmarkPointType, int landmarkPointCode, int zCooridinate) {
		return true;
	}

	public boolean isValidFaceImageType(int faceImageType) {
		return ((faceImageType >= FaceImageType.BASIC && faceImageType <= FaceImageType.POST_PROCESSED_FRONTAL)
				|| (faceImageType >= FaceImageType.BASIC_3D && faceImageType <= FaceImageType.TOKEN_FRONTAL_3D));
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageCompressionType(String purpose, int compressionType,
			ImageDecoderRequestDto decoderRequestDto) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				if (compressionType == ImageDataType.JPEG2000_LOSSY)
					return true;
				break;
			case REGISTRATION:
				if (compressionType == ImageDataType.JPEG2000_LOSS_LESS)
					return true;
				break;
			default:
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("isValidFaceImageType", e);
		}

		return false;
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageWidth(String purpose, int imageWidth, ImageDecoderRequestDto decoderRequestDto) {
		return ((imageWidth >= 0x0001 && imageWidth <= 0xFFFF) && decoderRequestDto.getWidth() == imageWidth);
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageHeight(String purpose, int imageHeight, ImageDecoderRequestDto decoderRequestDto) {
		return ((imageHeight >= 0x0001 && imageHeight <= 0xFFFF) && decoderRequestDto.getHeight() == imageHeight);
	}

	public boolean isValidSpatialSamplingRateLevel(int spatialSamplingRateLevel) {
		return (spatialSamplingRateLevel >= SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180
				&& spatialSamplingRateLevel <= SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_750);
	}

	public boolean isValidPostAcquisitionProcessing(int postAcquisitionProcessing) {
		return (postAcquisitionProcessing >= 0x0000 && postAcquisitionProcessing <= 0xFFFF);
	}

	public boolean isValidCrossReference(int crossReference) {
		return (crossReference >= CrossReference.BASIC && crossReference <= CrossReference.CROSSREFERENCE_FF);
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageColourSpace(String purpose, int imageColourSpace,
			ImageDecoderRequestDto decoderRequestDto) {
		return ((imageColourSpace == ImageColourSpace.UNSPECIFIED || imageColourSpace == ImageColourSpace.BIT_24_RGB) &&  decoderRequestDto.getImageColorSpace().equalsIgnoreCase("RGB"));
	}
}