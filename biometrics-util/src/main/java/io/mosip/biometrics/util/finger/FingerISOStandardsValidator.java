package io.mosip.biometrics.util.finger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.ISOStandardsValidator;
import io.mosip.biometrics.util.Purposes;

public class FingerISOStandardsValidator extends ISOStandardsValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerISOStandardsValidator.class);

	private static FingerISOStandardsValidator instance = null;
	  
    // Constructor
    private FingerISOStandardsValidator()
    {
    	super();
    }
  
    // Static method to create instance of Singleton class
    public static FingerISOStandardsValidator getInstance()
    {
        if (instance == null)
        	instance = new FingerISOStandardsValidator();
  
        return instance;
    }
    
	public boolean isValidFormatIdentifier(long formatIdentifier) {
		if (formatIdentifier == FingerFormatIdentifier.FORMAT_FIR)
			return true;
		return false;
	}

	public boolean isValidVersionNumber(long versionNumber) {
		if (versionNumber == FingerVersionNumber.VERSION_020)
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
		if (certificationFlag == FingerCertificationFlag.UNSPECIFIED
				|| certificationFlag == FingerCertificationFlag.ONE)
			return true;
		return false;
	}

	public boolean isValidNoOfFingerPresent(int noOfFingerPresent) {
		if (noOfFingerPresent == 0x01)
			return true;
		return false;
	}

	public boolean isValidRepresentationLength(long representationLength) {
		// The minimum value of the Representation Length is 51 bytes, consisting of a
		// minimum 47 bytes for the
		// Representation Header plus the size of the Representation Data, i.e. minimum
		// 4 bytes for the Length of Image
		// Data Block field assuming 0 bytes for the variable data.
		if (representationLength >= 0x00000029 && representationLength <= Long.decode("0xFFFFFFEF"))
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		if (captureDeviceTechnologyIdentifier >= FingerCaptureDeviceTechnology.UNSPECIFIED
				&& captureDeviceTechnologyIdentifier <= FingerCaptureDeviceTechnology.GLASS_FIBER)
			return true;
		return false;
	}

	public boolean isValidCaptureDeviceVendor(int captureDeviceVendor) {
		if (captureDeviceVendor >= FingerCaptureDeviceVendor.UNSPECIFIED
				&& captureDeviceVendor <= FingerCaptureDeviceVendor.VENDOR_FFFF)
			return true;
		return false;
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
		if (noOfQualityBlocks >= 0x00 && noOfQualityBlocks <= 0xFF)
			return true;
		return false;
	}

	public boolean isValidQualityScore(int qualityScore) {
		if ((qualityScore >= 0x00 && qualityScore <= 0x64) || qualityScore == 0xFF)
			return true;
		return false;
	}

	public boolean isValidQualityAlgorithmIdentifier(int qualityAlgorithmIdentifier) {
		if (qualityAlgorithmIdentifier >= FingerQualityAlgorithmIdentifier.UNSPECIFIED
				&& qualityAlgorithmIdentifier <= FingerQualityAlgorithmIdentifier.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		if (qualityAlgorithmVendorIdentifier >= FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED
				&& qualityAlgorithmVendorIdentifier <= FingerQualityAlgorithmVendorIdentifier.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidNoOfCertificationBlocks(int noOfCertificationBlocks) {
		if (noOfCertificationBlocks >= 0x00 && noOfCertificationBlocks <= 0xFF)
			return true;
		return false;
	}

	public boolean isValidCertificationAuthorityID(int certificationAuthorityID) {
		if (certificationAuthorityID >= FingerCertificationAuthorityID.UNSPECIFIED
				&& certificationAuthorityID <= FingerCertificationAuthorityID.VENDOR_FFFF)
			return true;
		return false;
	}

	public boolean isValidCertificationSchemeIdentifier(int certificationSchemeIdentifier) {
		if (certificationSchemeIdentifier >= FingerCertificationSchemeIdentifier.UNSPECIFIED
				&& certificationSchemeIdentifier <= FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER)
			return true;
		return false;
	}

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
				}
				break;
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
				}
				break;
			}
		} catch (Exception e) {
			LOGGER.error ("isValidFingerPosition", e);
		}

		return false;
	}

	public boolean isValidRepresentationsNo(int representationsNo) {
		if (representationsNo >= 0x00 && representationsNo <= 0x0F)
			return true;
		return false;
	}

	public boolean isValidScaleUnits(int scaleUnits) {
		if (scaleUnits == 0x01 || scaleUnits == 0x02)
			return true;
		return false;
	}

	public boolean isValidScanSpatialSamplingRateHorizontal(int scanSpatialSamplingRateHorizontal) {
		// 500 pixels to 1000 pixels
		if (scanSpatialSamplingRateHorizontal >= 0x01F4 && scanSpatialSamplingRateHorizontal <= 0x03E8)
			return true;
		return false;
	}

	public boolean isValidScanSpatialSamplingRateVertical(int scanSpatialSamplingRateVertical) {
		// 500 pixels to 1000 pixels
		if (scanSpatialSamplingRateVertical >= 0x01F4 && scanSpatialSamplingRateVertical <= 0x03E8)
			return true;
		return false;
	}

	public boolean isValidImageSpatialSamplingRateHorizontal(int scanSpatialSamplingRateHorizontal,
			int imageSpatialSamplingRateHorizontal) {
		// 500 pixels to 1000 pixels
		if ((imageSpatialSamplingRateHorizontal >= 0x01F4 && imageSpatialSamplingRateHorizontal <= 0x03E8)
				&& (imageSpatialSamplingRateHorizontal <= scanSpatialSamplingRateHorizontal))
			return true;
		return false;
	}

	public boolean isValidImageSpatialSamplingRateVertical(int scanSpatialSamplingRateVertical, int imageSpatialSamplingRateVertical) {
		// 500 pixels to 1000 pixels
		if ((imageSpatialSamplingRateVertical >= 0x01F4 && imageSpatialSamplingRateVertical <= 0x03E8)
			&& (imageSpatialSamplingRateVertical <= scanSpatialSamplingRateVertical))
			return true;
		return false;
	}

	public boolean isValidBitDepth(byte[] imageData, int bitDepth) {
		if (bitDepth == FingerImageBitDepth.BPP_08)
		{
			// need to check width in image also
			return true;
		}

		return false;
	}

	public boolean isValidImageCompressionType(String purpose, int compressionType) {
		try {
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				if (compressionType == FingerImageCompressionType.JPEG_2000_LOSSY
						|| compressionType == FingerImageCompressionType.WSQ) {
					return true;
				}
				break;
			case REGISTRATION:
				if (compressionType == FingerImageCompressionType.JPEG_2000_LOSS_LESS) {
					return true;
				}
				break;
			}
		} catch (Exception e) {
			LOGGER.error ("isValidImageCompressionType", e);
		}

		return false;
	}

	public boolean isValidImageImpressionType(int imageImpressionType) {
		if ((imageImpressionType >= FingerImpressionType.LIVE_SCAN_PLAIN
				&& imageImpressionType <= FingerImpressionType.LATENT_PALM_LIFT)
				|| (imageImpressionType == FingerImpressionType.LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN
						|| imageImpressionType == FingerImpressionType.OTHER
						|| imageImpressionType == FingerImpressionType.UNKNOWN))
			return true;
		return false;
	}

	public boolean isValidImageHorizontalLineLength(String purpose, byte[] imageData, int imageWidth) {
		if (imageWidth >= 0x0001 && imageWidth <= 0xFFFF)
		{
			// need to check width in image also
			return true;
		}
		return false;
	}

	public boolean isValidImageVerticalLineLength(String purpose, byte[] imageData, int imageHeight) {
		if (imageHeight >= 0x0001 && imageHeight <= 0xFFFF)
		{
			// need to check height in image also
			return true;
		}
		return false;
	}
}
