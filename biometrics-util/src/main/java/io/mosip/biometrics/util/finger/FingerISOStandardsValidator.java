package io.mosip.biometrics.util.finger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.ISOStandardsValidator;
import io.mosip.biometrics.util.ImageDecoderRequestDto;
import io.mosip.biometrics.util.Purposes;

public class FingerISOStandardsValidator extends ISOStandardsValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerISOStandardsValidator.class);

	private static FingerISOStandardsValidator instance = null;

	// Constructor
	private FingerISOStandardsValidator() {
		super();
	}

	// Static method to create instance of Singleton class
	public static FingerISOStandardsValidator getInstance() {
		if (instance == null)
			instance = new FingerISOStandardsValidator();

		return instance;
	}

	public boolean isValidFormatIdentifier(long formatIdentifier) {
		return (formatIdentifier == FingerFormatIdentifier.FORMAT_FIR);
	}

	public boolean isValidVersionNumber(long versionNumber) {
		return (versionNumber == FingerVersionNumber.VERSION_020);
	}

	public boolean isValidRecordLength(long dataLength, long recordLength) {
		return (dataLength == recordLength);
	}

	public boolean isValidNoOfRepresentations(int noOfRepresentations) {
		return (noOfRepresentations == 0x0001);
	}

	public boolean isValidCertificationFlag(int certificationFlag) {
		return (certificationFlag == FingerCertificationFlag.UNSPECIFIED
				|| certificationFlag == FingerCertificationFlag.ONE);
	}

	public boolean isValidNoOfFingerPresent(int noOfFingerPresent) {
		return (noOfFingerPresent == 0x01);
	}

	public boolean isValidRepresentationLength(long representationLength) {
		// The minimum value of the Representation Length is 51 bytes, consisting of a
		// minimum 47 bytes for the
		// Representation Header plus the size of the Representation Data, i.e. minimum
		// 4 bytes for the Length of Image
		// Data Block field assuming 0 bytes for the variable data.
		return (representationLength >= 0x00000029 && representationLength <= Long.decode("0xFFFFFFEF"));
	}

	public boolean isValidCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		return (captureDeviceTechnologyIdentifier >= FingerCaptureDeviceTechnology.UNSPECIFIED
				&& captureDeviceTechnologyIdentifier <= FingerCaptureDeviceTechnology.GLASS_FIBER);
	}

	public boolean isValidCaptureDeviceVendor(int captureDeviceVendor) {
		return (captureDeviceVendor >= FingerCaptureDeviceVendor.UNSPECIFIED
				&& captureDeviceVendor <= FingerCaptureDeviceVendor.VENDOR_FFFF);
	}

	public boolean isValidCaptureDeviceType(int captureDeviceType, int captureDeviceVendor) {
		if (captureDeviceType >= FingerCaptureDeviceType.UNSPECIFIED
				&& captureDeviceType <= FingerCaptureDeviceType.VENDOR_FFFF) {
			if (captureDeviceType == FingerCaptureDeviceType.UNSPECIFIED) {
				if (captureDeviceVendor == FingerCaptureDeviceVendor.UNSPECIFIED)
					return true;
			} else
				return true;
		}
		return false;
	}

	public boolean isValidNoOfQualityBlocks(int noOfQualityBlocks) {
		return (noOfQualityBlocks >= 0x00 && noOfQualityBlocks <= 0xFF);
	}

	public boolean isValidQualityScore(int qualityScore) {
		return ((qualityScore >= 0x00 && qualityScore <= 0x64) || qualityScore == 0xFF);
	}

	public boolean isValidQualityAlgorithmIdentifier(int qualityAlgorithmIdentifier) {
		return (qualityAlgorithmIdentifier >= FingerQualityAlgorithmIdentifier.UNSPECIFIED
				&& qualityAlgorithmIdentifier <= FingerQualityAlgorithmIdentifier.VENDOR_FFFF);
	}

	public boolean isValidQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		return (qualityAlgorithmVendorIdentifier >= FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED
				&& qualityAlgorithmVendorIdentifier <= FingerQualityAlgorithmVendorIdentifier.VENDOR_FFFF);
	}

	public boolean isValidNoOfCertificationBlocks(int noOfCertificationBlocks) {
		return (noOfCertificationBlocks >= 0x00 && noOfCertificationBlocks <= 0xFF);
	}

	public boolean isValidCertificationAuthorityID(int certificationAuthorityID) {
		return (certificationAuthorityID >= FingerCertificationAuthorityID.UNSPECIFIED
				&& certificationAuthorityID <= FingerCertificationAuthorityID.VENDOR_FFFF);
	}

	public boolean isValidCertificationSchemeIdentifier(int certificationSchemeIdentifier) {
		return (certificationSchemeIdentifier >= FingerCertificationSchemeIdentifier.UNSPECIFIED
				&& certificationSchemeIdentifier <= FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER);
	}

	@SuppressWarnings({ "java:S6208" })
	public boolean isValidFingerPosition(String purpose, int fingerPosition) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				switch (fingerPosition) {
				case FingerPosition.UNKNOWN:
				case FingerPosition.RIGHT_THUMB:
				case FingerPosition.RIGHT_INDEX_FINGER:
				case FingerPosition.RIGHT_MIDDLE_FINGER:
				case FingerPosition.RIGHT_RING_FINGER:
				case FingerPosition.RIGHT_LITTLE_FINGER:
				case FingerPosition.LEFT_THUMB:
				case FingerPosition.LEFT_INDEX_FINGER:
				case FingerPosition.LEFT_MIDDLE_FINGER:
				case FingerPosition.LEFT_RING_FINGER:
				case FingerPosition.LEFT_LITTLE_FINGER:
					return true;
				default:
					return false;
				}
			case REGISTRATION:
				switch (fingerPosition) {
				case FingerPosition.RIGHT_THUMB:
				case FingerPosition.RIGHT_INDEX_FINGER:
				case FingerPosition.RIGHT_MIDDLE_FINGER:
				case FingerPosition.RIGHT_RING_FINGER:
				case FingerPosition.RIGHT_LITTLE_FINGER:
				case FingerPosition.LEFT_THUMB:
				case FingerPosition.LEFT_INDEX_FINGER:
				case FingerPosition.LEFT_MIDDLE_FINGER:
				case FingerPosition.LEFT_RING_FINGER:
				case FingerPosition.LEFT_LITTLE_FINGER:
					return true;
				default:
					return false;
				}
			default:
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("isValidFingerPosition", e);
		}

		return false;
	}

	public boolean isValidRepresentationsNo(int representationsNo) {
		return (representationsNo >= 0x00 && representationsNo <= 0x0F);
	}

	public boolean isValidScaleUnits(int scaleUnits) {
		return (scaleUnits == 0x01 || scaleUnits == 0x02);
	}

	public boolean isValidScanSpatialSamplingRateHorizontal(int scanSpatialSamplingRateHorizontal) {
		// 490 pixels to 1010 pixels
		return (scanSpatialSamplingRateHorizontal >= 0x01EA && scanSpatialSamplingRateHorizontal <= 0x03F2);
	}

	public boolean isValidScanSpatialSamplingRateVertical(int scanSpatialSamplingRateVertical) {
		// 490 pixels to 1010 pixels
		return (scanSpatialSamplingRateVertical >= 0x01EA && scanSpatialSamplingRateVertical <= 0x03F2);
	}

	public boolean isValidImageSpatialSamplingRateHorizontal(int scanSpatialSamplingRateHorizontal,
			int imageSpatialSamplingRateHorizontal) {
		// 490 pixels to 1010 pixels
		return ((imageSpatialSamplingRateHorizontal >= 0x01EA && imageSpatialSamplingRateHorizontal <= 0x03F2)
				&& (imageSpatialSamplingRateHorizontal <= scanSpatialSamplingRateHorizontal));
	}

	public boolean isValidImageSpatialSamplingRateVertical(int scanSpatialSamplingRateVertical,
			int imageSpatialSamplingRateVertical) {
		// 490 pixels to 1010 pixels
		return ((imageSpatialSamplingRateVertical >= 0x01EA && imageSpatialSamplingRateVertical <= 0x03F2)
				&& (imageSpatialSamplingRateVertical <= scanSpatialSamplingRateVertical));
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
		return (bitDepth == FingerImageBitDepth.BPP_08);
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
		return (bitDepth == FingerImageBitDepth.BPP_08 && decoderRequestDto.getDepth() == FingerImageBitDepth.BPP_08);// GRAY
																														// 8
																														// bit
																														// images
	}

	public boolean isValidImageCompressionType(String purpose, int compressionType,
			ImageDecoderRequestDto decoderRequestDto) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				if ((compressionType == FingerImageCompressionType.JPEG_2000_LOSSY
						|| compressionType == FingerImageCompressionType.WSQ) && (!decoderRequestDto.isLossless()))
					return true;
				break;
			case REGISTRATION:
				if ((compressionType == FingerImageCompressionType.JPEG_2000_LOSS_LESS)
						&& (decoderRequestDto.isLossless()))
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

	public boolean isValidImageImpressionType(int imageImpressionType) {
		return ((imageImpressionType >= FingerImpressionType.LIVE_SCAN_PLAIN
				&& imageImpressionType <= FingerImpressionType.LATENT_PALM_LIFT)
				|| (imageImpressionType == FingerImpressionType.LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN
						|| imageImpressionType == FingerImpressionType.OTHER
						|| imageImpressionType == FingerImpressionType.UNKNOWN));
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageHorizontalLineLength(String purpose, int imageWidth,
			ImageDecoderRequestDto decoderRequestDto) {
		return (imageWidth >= 0x0001 && imageWidth <= 0xFFFF && decoderRequestDto.getWidth() == imageWidth);
	}

	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageVerticalLineLength(String purpose, int imageHeight,
			ImageDecoderRequestDto decoderRequestDto) {
		return (imageHeight >= 0x0001 && imageHeight <= 0xFFFF && decoderRequestDto.getHeight() == imageHeight);
	}
}
