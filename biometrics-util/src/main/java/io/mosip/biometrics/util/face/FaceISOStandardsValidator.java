package io.mosip.biometrics.util.face;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.ISOStandardsValidator;
import io.mosip.biometrics.util.Purposes;

public class FaceISOStandardsValidator extends ISOStandardsValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceISOStandardsValidator.class);

	private static FaceISOStandardsValidator instance = null;
	  
    // Constructor
    private FaceISOStandardsValidator()
    {
    	super();
    }
  
    // Static method to create instance of Singleton class
    public static FaceISOStandardsValidator getInstance()
    {
        if (instance == null)
        	instance = new FaceISOStandardsValidator();
  
        return instance;
    }
    
	public boolean isValidFormatIdentifier(long formatIdentifier) {
		if (formatIdentifier == FaceFormatIdentifier.FORMAT_FAC)
			return true;
		return false;
	}

	public boolean isValidVersionNumber(long versionNumber) {
		if (versionNumber == FaceVersionNumber.VERSION_030)
			return true;
		return false;
	}

	public boolean isValidRecordLength(long dataLength, long recordLength) {
		if (dataLength == recordLength)
			return true;
		return false;
	}

	public boolean isValidNoOfRepresentations(int noOfRepresentations) {
		if (noOfRepresentations == 0x0001)
			return true;
		return false;
	}

	public boolean isValidCertificationFlag(int certificationFlag) {
		if (certificationFlag == FaceCertificationFlag.UNSPECIFIED)
			return true;
		return false;
	}

	public boolean isValidTemporalSemantics(int temporalSemantics) {
		if (temporalSemantics == TemporalSequenceFlags.ONE_REPRESENTATION)
			return true;
		return false;
	}

	public boolean isValidRepresentationLength(long representationLength) {
		// The minimum value of the Representation Length is 51 bytes, consisting of a
		// minimum 51 bytes for the
		// Representation Header plus the size of the Representation Data, i.e. minimum
		// 4 bytes for the Length of Image
		// Data Block field assuming 0 bytes for the variable data.
		if (representationLength >= 0x00000033 && representationLength <= Long.decode("0xFFFFFFEF"))
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		if ((captureDeviceTechnologyIdentifier >= FaceCaptureDeviceTechnology.UNSPECIFIED
				&& captureDeviceTechnologyIdentifier <= FaceCaptureDeviceTechnology.VIDEO_FRAME_DIGITAL_CAMERA)
				|| (captureDeviceTechnologyIdentifier >= FaceCaptureDeviceTechnology.VENDOR_80
						&& captureDeviceTechnologyIdentifier <= FaceCaptureDeviceTechnology.VENDOR_FF))
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceVendor(int captureDeviceVendor) {
		if (captureDeviceVendor >= FaceCaptureDeviceVendor.UNSPECIFIED
				&& captureDeviceVendor <= FaceCaptureDeviceVendor.VENDOR_FFFF)
			return true;
		return false;
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
		if (noOfQualityBlocks >= 0x00 && noOfQualityBlocks <= 0xFF)
			return true;
		return false;
	}

	public boolean isValidQualityAlgorithmIdentifier(int qualityAlgorithmIdentifier) {
		if (qualityAlgorithmIdentifier >= FaceQualityAlgorithmIdentifier.UNSPECIFIED
				&& qualityAlgorithmIdentifier <= FaceQualityAlgorithmIdentifier.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		if (qualityAlgorithmVendorIdentifier >= FaceQualityAlgorithmVendorIdentifier.UNSPECIFIED
				&& qualityAlgorithmVendorIdentifier <= FaceQualityAlgorithmVendorIdentifier.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidQualityScore(int qualityScore) {
		if ((qualityScore >= 0x00 && qualityScore <= 0x64) || qualityScore == 0xFF)
			return true;
		return false;
	}

	public boolean isValidNoOfLandmarkPoints(int noOfLandmarkPoints) {
		if (noOfLandmarkPoints >= 0x0000 && noOfLandmarkPoints <= 0xFFFF)
			return true;
		return false;
	}

	public boolean isValidGender(int gender) {
		switch (gender) {
		case Gender.UNSPECIFIED:
		case Gender.MALE:
		case Gender.FEMALE:
		case Gender.UNKNOWN:
			return true;
		}
		return false;
	}

	public boolean isValidEyeColour(int eyeColour) {
		if ((eyeColour >= EyeColour.UNSPECIFIED && eyeColour <= EyeColour.PINK)
				|| eyeColour == EyeColour.OTHER_OR_UNKNOWN)
			return true;
		return false;
	}

	public boolean isValidHairColour(int hairColour) {
		if ((hairColour >= HairColour.UNSPECIFIED && hairColour <= HairColour.RED)
				|| (hairColour == HairColour.UNKNOWN))
			return true;
		return false;
	}

	public boolean isValidSubjectHeight(int subjectHeight) {
		if (subjectHeight >= 0x00 && subjectHeight <= 0xFF)
			return true;
		return false;
	}

	public boolean isValidFeatureMask(int featureMask) {
		if (featureMask >= 0x000000 && featureMask <= 0xFFFFFF)
			return true;
		return false;
	}

	public boolean isValidExpressionMask(int expressionMask) {
		if (expressionMask >= 0x0000 && expressionMask <= 0xFFFF)
			return true;
		return false;
	}

	/*
	 * Future implementation 
	*/
	public boolean isValidPoseAngle(int[] poseAngle) {
		return true;
	}

	/*
	 * Future implementation 
	*/
	public boolean isValidPoseAngleUncertainty(int[] poseAngleUncertainty) {
		return true;
	}

	/*
	 * Future implementation for LandmarkPointType
	*/
	public boolean isValidLandmarkPointType(int landmarkPointType) {
		return true;
	}

	/*
	 * Future implementation for LandmarkPointCode
	*/
	public boolean isValidLandmarkPointCode(int landmarkPointType, int landmarkPointCode) {
		return true;
	}
	
	/*
	 * Future implementation for LandmarkXCooridinate
	*/
	public boolean isValidLandmarkXCooridinate(int landmarkPointType, int landmarkPointCode, int xCooridinate) {
		return true;
	}

	/*
	 * Future implementation for LandmarkYCooridinate
	*/
	public boolean isValidLandmarkYCooridinate(int landmarkPointType, int landmarkPointCode, int yCooridinate) {
		return true;
	}

	/*
	 * Future implementation for LandmarkZCooridinate
	*/
	public boolean isValidLandmarkZCooridinate(int landmarkPointType, int landmarkPointCode, int zCooridinate) {
		return true;
	}

	public boolean isValidFaceImageType(int faceImageType) {
		if ((faceImageType >= FaceImageType.BASIC && faceImageType <= FaceImageType.POST_PROCESSED_FRONTAL)
				|| (faceImageType >= FaceImageType.BASIC_3D && faceImageType <= FaceImageType.TOKEN_FRONTAL_3D))
			return true;
		return false;
	}

	public boolean isValidImageCompressionType(String purpose, int compressionType) {
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
			}
		} catch (Exception e) {
			LOGGER.error ("isValidFaceImageType", e);
		}

		return false;
	}

	public boolean isValidImageWidth(String purpose, byte[] imageData, int imageWidth) {
		if (imageWidth >= 0x0001 && imageWidth <= 0xFFFF)
			return true;

		// need to check width in image also
		return true;
	}

	public boolean isValidImageHeight(String purpose, byte[] imageData, int imageHeight) {
		if (imageHeight >= 0x0001 && imageHeight <= 0xFFFF)
			return true;

		// need to check height in image also
		return true;
	}

	public boolean isValidSpatialSamplingRateLevel(int spatialSamplingRateLevel) {
		if (spatialSamplingRateLevel >= SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180
				&& spatialSamplingRateLevel <= SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_750)
			return true;
		return false;
	}

	public boolean isValidPostAcquisitionProcessing(int postAcquisitionProcessing) {
		if (postAcquisitionProcessing >= 0x0000 && postAcquisitionProcessing <= 0xFFFF)
			return true;
		return false;
	}

	public boolean isValidCrossReference(int crossReference) {
		if (crossReference >= CrossReference.BASIC
				&& crossReference <= CrossReference.CROSSREFERENCE_FF)
			return true;
		return false;
	}

	public boolean isValidImageColourSpace(String purpose, byte[] imageData, int imageColourSpace) {
		if (imageColourSpace == ImageColourSpace.BIT_24_RGB)
			return true;

		// need to check height in image also
		return false;
	}
}
