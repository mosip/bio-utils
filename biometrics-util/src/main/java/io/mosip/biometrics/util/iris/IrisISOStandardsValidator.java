package io.mosip.biometrics.util.iris;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.ISOStandardsValidator;
import io.mosip.biometrics.util.ImageDecoderRequestDto;
import io.mosip.biometrics.util.Purposes;

public class IrisISOStandardsValidator extends ISOStandardsValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(IrisISOStandardsValidator.class);

	private static IrisISOStandardsValidator instance = null;
	  
    // Constructor
    private IrisISOStandardsValidator()
    {
    	super();
    }
  
    // Static method to create instance of Singleton class
    public static IrisISOStandardsValidator getInstance()
    {
        if (instance == null)
        	instance = new IrisISOStandardsValidator();
  
        return instance;
    }
    
	public boolean isValidFormatIdentifier(long formatIdentifier) {
		if (formatIdentifier == IrisFormatIdentifier.FORMAT_IIR)
			return true;
		return false;
	}

	public boolean isValidVersionNumber(long versionNumber) {
		if (versionNumber == IrisVersionNumber.VERSION_020)
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
		if (certificationFlag == IrisCertificationFlag.UNSPECIFIED)
			return true;
		return false;
	}

	public boolean isValidNoOfEyesRepresented(int noOfEyesRepresented) {
		if (noOfEyesRepresented == NoOfEyesRepresented.UNKNOWN
				|| noOfEyesRepresented == NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT)
			return true;
		return false;
	}

	public boolean isValidRepresentationLength(long representationLength) {
		// The minimum value of the Representation Length is 53 bytes, consisting of a
		// minimum 53 bytes for the
		// Representation Header plus the size of the Representation Data, i.e. minimum
		// 4 bytes for the Length of Image
		// Data Block field assuming 0 bytes for the variable data.
		if (representationLength >= 0x00000035 && representationLength <= Long.decode("0xFFFFFFEF"))
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		if (captureDeviceTechnologyIdentifier >= IrisCaptureDeviceTechnology.UNSPECIFIED
				&& captureDeviceTechnologyIdentifier <= IrisCaptureDeviceTechnology.CMOS_OR_CCD)
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceVendor(int captureDeviceVendor) {
		if (captureDeviceVendor >= IrisCaptureDeviceVendor.UNSPECIFIED
				&& captureDeviceVendor <= IrisCaptureDeviceVendor.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceType(int captureDeviceType, int captureDeviceVendor) {
		if (captureDeviceType >= IrisCaptureDeviceType.UNSPECIFIED
				&& captureDeviceType <= IrisCaptureDeviceType.VENDOR_FFFF) {
			if (captureDeviceType == IrisCaptureDeviceType.UNSPECIFIED) {
				if (captureDeviceVendor == IrisCaptureDeviceVendor.UNSPECIFIED)
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
		if (qualityAlgorithmIdentifier >= IrisQualityAlgorithmIdentifier.UNSPECIFIED
				&& qualityAlgorithmIdentifier <= IrisQualityAlgorithmIdentifier.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		if (qualityAlgorithmVendorIdentifier >= IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED
				&& qualityAlgorithmVendorIdentifier <= IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidQualityScore(int qualityScore) {
		if ((qualityScore >= 0x00 && qualityScore <= 0x64) || qualityScore == 0xFF)
			return true;
		return false;
	}

	public boolean isValidNoOfRepresentation(int noOfRepresentation) {
		if (noOfRepresentation == 0x0001)
			return true;
		return false;
	}

	public boolean isValidEyeLabel(String purpose, int eyeLabel) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				switch (eyeLabel) {
				case EyeLabel.UNSPECIFIED:
				case EyeLabel.RIGHT:
				case EyeLabel.LEFT:
					return true;
				}
				break;
			case REGISTRATION:
				switch (eyeLabel) {
				case EyeLabel.RIGHT:
				case EyeLabel.LEFT:
					return true;
				}
				break;
			}
		} catch (Exception e) {
			LOGGER.error ("isValidEyeLabel", e);
		}

		return false;
	}

	public boolean isValidImageType(String purpose, int imageType) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				switch (imageType) {
				case ImageType.CROPPED_AND_MASKED:
					return true;
				}
				break;
			case REGISTRATION:
				switch (imageType) {
				case ImageType.CROPPED:
					return true;
				}
				break;
			}
		} catch (Exception e) {
			LOGGER.error ("isValidImageType", e);
		}

		return false;
	}

	public boolean isValidImageFromat(int imageFormat) {
		if (imageFormat == ImageFormat.MONO_JPEG2000)
			return true;
		return false;
	}

	public boolean isValidImageHorizontalOrientation(int horizontalOrientation) {
		if (horizontalOrientation >= HorizontalOrientation.ORIENTATION_UNDEFINIED
				&& horizontalOrientation <= HorizontalOrientation.ORIENTATION_FLIPPED)
			return true;
		return false;
	}

	public boolean isValidImageVerticalOrientation(int verticalOrientation) {
		if (verticalOrientation >= VerticalOrientation.ORIENTATION_UNDEFINIED
				&& verticalOrientation <= VerticalOrientation.ORIENTATION_FLIPPED)
			return true;
		return false;
	}

	public boolean isValidImageCompressionType(String purpose, int compressionType, ImageDecoderRequestDto decoderRequestDto) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				if (compressionType == IrisImageCompressionType.JPEG_LOSSY)
					//checking lossy from imagedata
					if (!decoderRequestDto.isLossless())
						return true;
				break;
			case REGISTRATION:
				if (compressionType == IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE)
					//checking lossless from imagedata
					if (decoderRequestDto.isLossless())
						return true;
				break;
			}
		} catch (Exception e) {
			LOGGER.error ("isValidImageCompressionType", e);
		}

		return false;
	}

	public boolean isValidImageWidth(String purpose, int imageWidth, ImageDecoderRequestDto decoderRequestDto) {
		if (imageWidth >= 0x0001 && imageWidth <= 0xFFFF)
		{
			// need to check width in image also
			if (imageWidth == decoderRequestDto.getWidth())
				return true;			
		}
		return false;	
	}

	public boolean isValidImageHeight(String purpose, int imageHeight, ImageDecoderRequestDto decoderRequestDto) {
		if (imageHeight >= 0x0001 && imageHeight <= 0xFFFF)
		{
			// need to check height in image also
			if (imageHeight == decoderRequestDto.getHeight())
				return true;			
		}
		return false;	
	}

	 /**
     * Validates bit depth of a given image byte array
     * @deprecated
     * This method will not be  acceptable in future versions.
     * <p> Use {@link isValidBitDepth(bitDepth, ImageDecoderRequestDto)} instead.
     *
     * @param imageData image byte array
     * @param bitDepth image bit depth
     * @return true or false 
     */
	@Deprecated
	public boolean isValidBitDepth(byte[] imageData, int bitDepth) {
		if (bitDepth == IrisImageBitDepth.BPP_08)
		{
			// need to check depth in image also
			return true;			
		}
		return false;	
	}

	 /**
     * Validates bit depth of a using decoded image information
     * @param bitDepth image bit depth
     * @param decoderRequestDto ImageDecoderRequestDto contains decoded image information
     * @return true or false 
     */
	public boolean isValidBitDepth(int bitDepth, ImageDecoderRequestDto decoderRequestDto) {
		if (bitDepth == IrisImageBitDepth.BPP_08)
		{
			if (decoderRequestDto.getDepth() == IrisImageBitDepth.BPP_08) // gray scale 8 bit depth
				return true;			
		}
		return false;	
	}

	public boolean isValidRange(int range) {
		if (range >= IrisRange.UNASSIGNED && range <= IrisRange.OVERFLOW_FFFF)
			return true;
		return false;
	}

	public boolean isValidRollAngleOfEye(int rollAngleOfEye) {
		if (rollAngleOfEye >= IrisRangeRollAngleOfEye.ROLL_ANGLE_0000
				&& rollAngleOfEye <= IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED)
			return true;
		return false;
	}

	public boolean isValidRollAngleUncertainty(int rollAngleUncertainty) {
		if (rollAngleUncertainty >= IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000
				&& rollAngleUncertainty <= IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED)
			return true;
		return false;
	}

	public boolean isValidIrisCenterSmallestX(int centerSmallestX) {
		if (centerSmallestX >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerSmallestX <= IrisCoordinate.COORDINATE_FFFF)
			return true;
		return false;
	}

	public boolean isValidIrisCenterLargestX(int centerLargestX) {
		if (centerLargestX >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerLargestX <= IrisCoordinate.COORDINATE_FFFF)
			return true;
		return false;
	}

	public boolean isValidIrisCenterSmallestY(int centerSmallestY) {
		if (centerSmallestY >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerSmallestY <= IrisCoordinate.COORDINATE_FFFF)
			return true;
		return false;
	}

	public boolean isValidIrisCenterLargestY(int centerLargestY) {
		if (centerLargestY >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerLargestY <= IrisCoordinate.COORDINATE_FFFF)
			return true;
		return false;
	}

	public boolean isValidIrisDiameterSmallest(int diameterSmallest) {
		if (diameterSmallest >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& diameterSmallest <= IrisCoordinate.COORDINATE_FFFF)
			return true;
		return false;
	}

	public boolean isValidIrisDiameterLargest(int diameterLargest) {
		if (diameterLargest >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& diameterLargest <= IrisCoordinate.COORDINATE_FFFF)
			return true;
		return false;
	}
}
