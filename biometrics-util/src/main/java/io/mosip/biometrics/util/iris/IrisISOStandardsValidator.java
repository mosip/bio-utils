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
	private IrisISOStandardsValidator() {
		super();
	}

	// Static method to create instance of Singleton class
	public static IrisISOStandardsValidator getInstance() {
		if (instance == null)
			instance = new IrisISOStandardsValidator();

		return instance;
	}

	public boolean isValidFormatIdentifier(long formatIdentifier) {
		return (formatIdentifier == IrisFormatIdentifier.FORMAT_IIR);
	}

	public boolean isValidVersionNumber(long versionNumber) {
		return (versionNumber == IrisVersionNumber.VERSION_020);
	}

	public boolean isValidRecordLength(long dataLength, long recordLength) {
		return (dataLength == recordLength);
	}

	public boolean isValidNoOfRepresentations(int noOfRepresentations) {
		return (noOfRepresentations == 0x0001);
	}

	public boolean isValidCertificationFlag(int certificationFlag) {
		return (certificationFlag == IrisCertificationFlag.UNSPECIFIED);
	}

	public boolean isValidNoOfEyesRepresented(int noOfEyesRepresented) {
		return (noOfEyesRepresented == NoOfEyesRepresented.UNKNOWN
				|| noOfEyesRepresented == NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT);
	}

	public boolean isValidRepresentationLength(long representationLength) {
		// The minimum value of the Representation Length is 53 bytes, consisting of a
		// minimum 53 bytes for the
		// Representation Header plus the size of the Representation Data, i.e. minimum
		// 4 bytes for the Length of Image
		// Data Block field assuming 0 bytes for the variable data.
		return (representationLength >= 0x00000035 && representationLength <= Long.decode("0xFFFFFFEF"));
	}

	public boolean isValidCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		return (captureDeviceTechnologyIdentifier >= IrisCaptureDeviceTechnology.UNSPECIFIED
				&& captureDeviceTechnologyIdentifier <= IrisCaptureDeviceTechnology.CMOS_OR_CCD);
	}

	public boolean isValidCaptureDeviceVendor(int captureDeviceVendor) {
		return (captureDeviceVendor >= IrisCaptureDeviceVendor.UNSPECIFIED
				&& captureDeviceVendor <= IrisCaptureDeviceVendor.VENDOR_FFFF);
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
		return (noOfQualityBlocks >= 0x00 && noOfQualityBlocks <= 0xFF);
	}

	public boolean isValidQualityAlgorithmIdentifier(int qualityAlgorithmIdentifier) {
		return (qualityAlgorithmIdentifier >= IrisQualityAlgorithmIdentifier.UNSPECIFIED
				&& qualityAlgorithmIdentifier <= IrisQualityAlgorithmIdentifier.VENDOR_FFFF);
	}

	public boolean isValidQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		return (qualityAlgorithmVendorIdentifier >= IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED
				&& qualityAlgorithmVendorIdentifier <= IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF);
	}

	public boolean isValidQualityScore(int qualityScore) {
		return ((qualityScore >= 0x00 && qualityScore <= 0x64) || qualityScore == 0xFF);
	}

	public boolean isValidNoOfRepresentation(int noOfRepresentation) {
		return (noOfRepresentation == 0x0001);
	}

	public boolean isValidEyeLabel(String purpose, int eyeLabel) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				return (eyeLabel == EyeLabel.UNSPECIFIED || eyeLabel == EyeLabel.RIGHT || eyeLabel == EyeLabel.LEFT);
			case REGISTRATION:
				return (eyeLabel == EyeLabel.RIGHT || eyeLabel == EyeLabel.LEFT);
			default:
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("isValidEyeLabel", e);
		}

		return false;
	}

	public boolean isValidImageType(String purpose, int imageType) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				return (imageType == ImageType.CROPPED_AND_MASKED);
			case REGISTRATION:
				return (imageType == ImageType.CROPPED);
			default:
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("isValidImageType", e);
		}

		return false;
	}

	public boolean isValidImageFromat(int imageFormat) {
		return (imageFormat == ImageFormat.MONO_JPEG2000);
	}

	public boolean isValidImageHorizontalOrientation(int horizontalOrientation) {
		return (horizontalOrientation >= HorizontalOrientation.ORIENTATION_UNDEFINIED
				&& horizontalOrientation <= HorizontalOrientation.ORIENTATION_FLIPPED);
	}

	public boolean isValidImageVerticalOrientation(int verticalOrientation) {
		return (verticalOrientation >= VerticalOrientation.ORIENTATION_UNDEFINIED
				&& verticalOrientation <= VerticalOrientation.ORIENTATION_FLIPPED);
	}

	public boolean isValidImageCompressionType(String purpose, int compressionType,
			ImageDecoderRequestDto decoderRequestDto) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				// checking lossy from imagedata
				if (compressionType == IrisImageCompressionType.JPEG_LOSSY && !decoderRequestDto.isLossless())
					return true;
				break;
			case REGISTRATION:
				// checking lossless from imagedata
				if (compressionType == IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE && decoderRequestDto.isLossless())
					return true;
				break;
			default:
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("isValidImageCompressionType", e);
		}

		return false;
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageWidth(String purpose, int imageWidth, ImageDecoderRequestDto decoderRequestDto) {
		return (imageWidth >= 0x0001 && imageWidth <= 0xFFFF && imageWidth == decoderRequestDto.getWidth());
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageHeight(String purpose, int imageHeight, ImageDecoderRequestDto decoderRequestDto) {
		return (imageHeight >= 0x0001 && imageHeight <= 0xFFFF && imageHeight == decoderRequestDto.getHeight());
	}

	/**
	 * Validates bit depth of a given image byte array
	 * 
	 * @deprecated This method will not be acceptable in future versions.
	 *             <p>
	 *             Use {@link isValidBitDepth(bitDepth, ImageDecoderRequestDto)}
	 *             instead.
	 *
	 * @param imageData image byte array
	 * @param bitDepth  image bit depth
	 * @return true or false
	 */
	@Deprecated(since = "1.2.1", forRemoval = true)
	public boolean isValidBitDepth(byte[] imageData, int bitDepth) {
		return (bitDepth == IrisImageBitDepth.BPP_08);
	}

	/**
	 * Validates bit depth of a using decoded image information
	 * 
	 * @param bitDepth          image bit depth
	 * @param decoderRequestDto ImageDecoderRequestDto contains decoded image
	 *                          information
	 * @return true or false
	 */
	public boolean isValidBitDepth(int bitDepth, ImageDecoderRequestDto decoderRequestDto) {
		return (bitDepth == IrisImageBitDepth.BPP_08 && decoderRequestDto.getDepth() == IrisImageBitDepth.BPP_08);
	}

	public boolean isValidRange(int range) {
		return (range >= IrisRange.UNASSIGNED && range <= IrisRange.OVERFLOW_FFFF);
	}

	public boolean isValidRollAngleOfEye(int rollAngleOfEye) {
		return (rollAngleOfEye >= IrisRangeRollAngleOfEye.ROLL_ANGLE_0000
				&& rollAngleOfEye <= IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED);
	}

	public boolean isValidRollAngleUncertainty(int rollAngleUncertainty) {
		return (rollAngleUncertainty >= IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000
				&& rollAngleUncertainty <= IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED);
	}

	public boolean isValidIrisCenterSmallestX(int centerSmallestX) {
		return (centerSmallestX >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerSmallestX <= IrisCoordinate.COORDINATE_FFFF);
	}

	public boolean isValidIrisCenterLargestX(int centerLargestX) {
		return (centerLargestX >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerLargestX <= IrisCoordinate.COORDINATE_FFFF);
	}

	public boolean isValidIrisCenterSmallestY(int centerSmallestY) {
		return (centerSmallestY >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerSmallestY <= IrisCoordinate.COORDINATE_FFFF);
	}

	public boolean isValidIrisCenterLargestY(int centerLargestY) {
		return (centerLargestY >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& centerLargestY <= IrisCoordinate.COORDINATE_FFFF);
	}

	public boolean isValidIrisDiameterSmallest(int diameterSmallest) {
		return (diameterSmallest >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& diameterSmallest <= IrisCoordinate.COORDINATE_FFFF);
	}

	public boolean isValidIrisDiameterLargest(int diameterLargest) {
		return (diameterLargest >= IrisCoordinate.COORDINATE_UNDEFINIED
				&& diameterLargest <= IrisCoordinate.COORDINATE_FFFF);
	}
}