package io.mosip.biometrics.util.finger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.mosip.biometrics.util.ImageDecoderRequestDto;

/**
 * Test class for {@link FingerISOStandardsValidator}
 */
@ExtendWith(MockitoExtension.class)
class FingerISOStandardsValidatorTest {

    private FingerISOStandardsValidator validator;

    @Mock
    private ImageDecoderRequestDto decoderRequestDto;

    // Correct purpose values from the Purposes enum
    private static final String AUTH_PURPOSE = "Auth";
    private static final String REGISTRATION_PURPOSE = "Registration";

    /**
     * Sets up the test environment before each test case
     */
    @BeforeEach
    void setUp() {
        validator = FingerISOStandardsValidator.getInstance();
    }

    /**
     * Tests that getInstance returns the same instance (singleton pattern)
     */
    @Test
    void getInstanceReturnsSameInstance() {
        FingerISOStandardsValidator instance1 = FingerISOStandardsValidator.getInstance();
        FingerISOStandardsValidator instance2 = FingerISOStandardsValidator.getInstance();

        assertSame(instance1, instance2, "Multiple instances of singleton created");
    }

    /**
     * Tests the isValidFormatIdentifier method with valid and invalid format identifiers
     */
    @Test
    void isValidFormatIdentifierReturnsExpectedResult() {
        assertTrue(validator.isValidFormatIdentifier(FingerFormatIdentifier.FORMAT_FIR),
                "Should return true for valid format identifier");
        assertFalse(validator.isValidFormatIdentifier(9999),
                "Should return false for invalid format identifier");
    }

    /**
     * Tests the isValidVersionNumber method with valid and invalid version numbers
     */
    @Test
    void isValidVersionNumberReturnsExpectedResult() {
        assertTrue(validator.isValidVersionNumber(FingerVersionNumber.VERSION_020),
                "Should return true for valid version number");
        assertFalse(validator.isValidVersionNumber(123),
                "Should return false for invalid version number");
    }

    /**
     * Tests the isValidRecordLength method with matching and non-matching lengths
     */
    @Test
    void isValidRecordLengthReturnsExpectedResult() {
        assertTrue(validator.isValidRecordLength(100, 100),
                "Should return true when lengths match");
        assertFalse(validator.isValidRecordLength(100, 200),
                "Should return false when lengths don't match");
    }

    /**
     * Tests the isValidNoOfRepresentations method with valid and invalid values
     */
    @Test
    void isValidNoOfRepresentationsReturnsExpectedResult() {
        assertTrue(validator.isValidNoOfRepresentations(0x0001),
                "Should return true for 0x0001");
        assertFalse(validator.isValidNoOfRepresentations(0x0002),
                "Should return false for values other than 0x0001");
    }

    /**
     * Tests the isValidCertificationFlag method with valid and invalid certification flags
     */
    @Test
    void isValidCertificationFlagReturnsExpectedResult() {
        assertTrue(validator.isValidCertificationFlag(FingerCertificationFlag.UNSPECIFIED),
                "Should return true for UNSPECIFIED flag");
        assertTrue(validator.isValidCertificationFlag(FingerCertificationFlag.ONE),
                "Should return true for ONE flag");
        assertFalse(validator.isValidCertificationFlag(999),
                "Should return false for invalid certification flag");
    }

    /**
     * Tests the isValidNoOfFingerPresent method with valid and invalid values
     */
    @Test
    void isValidNoOfFingerPresentReturnsExpectedResult() {
        assertTrue(validator.isValidNoOfFingerPresent(0x01),
                "Should return true for 0x01");
        assertFalse(validator.isValidNoOfFingerPresent(0x02),
                "Should return false for values other than 0x01");
    }

    /**
     * Tests the isValidRepresentationLength method with boundary and invalid values
     */
    @Test
    void isValidRepresentationLengthReturnsBoundaryResults() {
        assertTrue(validator.isValidRepresentationLength(0x00000029),
                "Should be valid for minimum value 41 bytes");
        assertTrue(validator.isValidRepresentationLength(0x00000100),
                "Should be valid for middle value");
        assertTrue(validator.isValidRepresentationLength(Long.decode("0xFFFFFFEF")),
                "Should be valid for maximum value");

        assertFalse(validator.isValidRepresentationLength(0x00000028),
                "Should be invalid for value below minimum");
        assertFalse(validator.isValidRepresentationLength(Long.decode("0xFFFFFFF0")),
                "Should be invalid for value above maximum");
    }

    /**
     * Tests the isValidCaptureDeviceTechnologyIdentifier method with valid and invalid values
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifierReturnsExpectedResult() {
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(FingerCaptureDeviceTechnology.UNSPECIFIED),
                "Should return true for UNSPECIFIED technology");
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(FingerCaptureDeviceTechnology.GLASS_FIBER),
                "Should return true for GLASS_FIBER technology");
        assertFalse(validator.isValidCaptureDeviceTechnologyIdentifier(-1),
                "Should return false for value below minimum");
        assertFalse(validator.isValidCaptureDeviceTechnologyIdentifier(999),
                "Should return false for value above maximum");
    }

    /**
     * Tests the isValidCaptureDeviceVendor method with valid and invalid values
     */
    @Test
    void isValidCaptureDeviceVendorReturnsExpectedResult() {
        assertTrue(validator.isValidCaptureDeviceVendor(FingerCaptureDeviceVendor.UNSPECIFIED),
                "Should return true for UNSPECIFIED vendor");
        assertTrue(validator.isValidCaptureDeviceVendor(FingerCaptureDeviceVendor.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF");
        assertFalse(validator.isValidCaptureDeviceVendor(-1),
                "Should return false for value below minimum");
        assertFalse(validator.isValidCaptureDeviceVendor(0x10000),
                "Should return false for invalid vendor identifier");
    }

    /**
     * Tests the isValidCaptureDeviceType method with various scenarios
     */
    @Test
    void isValidCaptureDeviceTypeReturnsExpectedResult() {
        // Valid cases
        assertTrue(validator.isValidCaptureDeviceType(
                        FingerCaptureDeviceType.UNSPECIFIED, FingerCaptureDeviceVendor.UNSPECIFIED),
                "Should return true for UNSPECIFIED type and vendor");

        assertTrue(validator.isValidCaptureDeviceType(
                        FingerCaptureDeviceType.VENDOR_FFFF, FingerCaptureDeviceVendor.VENDOR_FFFF),
                "Should return true for valid non-unspecified type");

        // Invalid cases
        assertFalse(validator.isValidCaptureDeviceType(
                        FingerCaptureDeviceType.UNSPECIFIED, FingerCaptureDeviceVendor.VENDOR_FFFF),
                "Should return false for UNSPECIFIED type with non-unspecified vendor");

        // Test values outside the valid constant ranges
        assertFalse(validator.isValidCaptureDeviceType(-1, FingerCaptureDeviceVendor.UNSPECIFIED),
                "Should return false for device type below minimum range");

        // Use a large value that should be outside the valid range
        int largeInvalidValue = 0x100000;
        assertFalse(validator.isValidCaptureDeviceType(largeInvalidValue, FingerCaptureDeviceVendor.UNSPECIFIED),
                "Should return false for device type above range");
    }

    /**
     * Tests the isValidNoOfQualityBlocks method with boundary and invalid values
     */
    @Test
    void isValidNoOfQualityBlocksReturnsBoundaryResults() {
        assertTrue(validator.isValidNoOfQualityBlocks(0),
                "Should be valid for minimum value 0");
        assertTrue(validator.isValidNoOfQualityBlocks(0xFF),
                "Should be valid for maximum value 255");
        assertFalse(validator.isValidNoOfQualityBlocks(-1),
                "Should be invalid for negative values");
        assertFalse(validator.isValidNoOfQualityBlocks(0x100),
                "Should be invalid for values greater than 255");
    }

    /**
     * Tests the isValidQualityScore method with valid and invalid score values
     */
    @Test
    void isValidQualityScoreReturnsExpectedResult() {
        assertTrue(validator.isValidQualityScore(0),
                "Should be valid for minimum score 0");
        assertTrue(validator.isValidQualityScore(100),
                "Should be valid for maximum score 100");
        assertTrue(validator.isValidQualityScore(0xFF),
                "Should be valid for special value 0xFF (255)");
        assertFalse(validator.isValidQualityScore(-1),
                "Should be invalid for negative scores");
        assertFalse(validator.isValidQualityScore(101),
                "Should be invalid for scores > 100 and != 255");
        assertFalse(validator.isValidQualityScore(254),
                "Should be invalid for scores between 101-254");
    }

    /**
     * Tests the isValidQualityAlgorithmIdentifier method with valid and invalid algorithm identifiers
     */
    @Test
    void isValidQualityAlgorithmIdentifierReturnsExpectedResult() {
        assertTrue(validator.isValidQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.UNSPECIFIED),
                "Should return true for UNSPECIFIED algorithm identifier");
        assertTrue(validator.isValidQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF algorithm identifier");
        assertFalse(validator.isValidQualityAlgorithmIdentifier(-1),
                "Should return false for value below minimum");
        assertFalse(validator.isValidQualityAlgorithmIdentifier(0x10000),
                "Should return false for invalid algorithm identifier");
    }

    /**
     * Tests the isValidQualityAlgorithmVendorIdentifier method with valid and invalid vendor identifiers
     */
    @Test
    void isValidQualityAlgorithmVendorIdentifierReturnsExpectedResult() {
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED),
                "Should return true for UNSPECIFIED vendor identifier");
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF vendor identifier");
        assertFalse(validator.isValidQualityAlgorithmVendorIdentifier(-1),
                "Should return false for value below minimum");
        assertFalse(validator.isValidQualityAlgorithmVendorIdentifier(0x10000),
                "Should return false for invalid vendor identifier");
    }

    /**
     * Tests the isValidNoOfCertificationBlocks method with boundary and invalid values
     */
    @Test
    void isValidNoOfCertificationBlocksReturnsBoundaryResults() {
        assertTrue(validator.isValidNoOfCertificationBlocks(0),
                "Should be valid for minimum value 0");
        assertTrue(validator.isValidNoOfCertificationBlocks(0xFF),
                "Should be valid for maximum value 255");
        assertFalse(validator.isValidNoOfCertificationBlocks(-1),
                "Should be invalid for negative values");
        assertFalse(validator.isValidNoOfCertificationBlocks(0x100),
                "Should be invalid for values greater than 255");
    }

    /**
     * Tests the isValidCertificationAuthorityID method with valid and invalid authority IDs
     */
    @Test
    void isValidCertificationAuthorityIdReturnsExpectedResult() {
        assertTrue(validator.isValidCertificationAuthorityID(FingerCertificationAuthorityID.UNSPECIFIED),
                "Should return true for UNSPECIFIED authority ID");
        assertTrue(validator.isValidCertificationAuthorityID(FingerCertificationAuthorityID.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF authority ID");
        assertFalse(validator.isValidCertificationAuthorityID(-1),
                "Should return false for value below minimum");
        assertFalse(validator.isValidCertificationAuthorityID(0x10000),
                "Should return false for invalid authority ID");
    }

    /**
     * Tests the isValidCertificationSchemeIdentifier method with valid and invalid scheme identifiers
     */
    @Test
    void isValidCertificationSchemeIdentifierReturnsExpectedResult() {
        assertTrue(validator.isValidCertificationSchemeIdentifier(FingerCertificationSchemeIdentifier.UNSPECIFIED),
                "Should return true for UNSPECIFIED scheme identifier");
        assertTrue(validator.isValidCertificationSchemeIdentifier(
                        FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER),
                "Should return true for valid scheme identifier");
        assertFalse(validator.isValidCertificationSchemeIdentifier(-1),
                "Should return false for value below minimum");
        assertFalse(validator.isValidCertificationSchemeIdentifier(999),
                "Should return false for invalid scheme identifier");
    }

    /**
     * Tests the isValidFingerPosition method for Auth purpose
     */
    @Test
    void isValidFingerPositionForAuthPurposeReturnsExpectedResult() {
        // Valid finger positions for Auth
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.UNKNOWN),
                "Should return true for UNKNOWN position in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.RIGHT_THUMB),
                "Should return true for RIGHT_THUMB in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.RIGHT_INDEX_FINGER),
                "Should return true for RIGHT_INDEX_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.RIGHT_MIDDLE_FINGER),
                "Should return true for RIGHT_MIDDLE_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.RIGHT_RING_FINGER),
                "Should return true for RIGHT_RING_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.RIGHT_LITTLE_FINGER),
                "Should return true for RIGHT_LITTLE_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.LEFT_THUMB),
                "Should return true for LEFT_THUMB in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.LEFT_INDEX_FINGER),
                "Should return true for LEFT_INDEX_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.LEFT_MIDDLE_FINGER),
                "Should return true for LEFT_MIDDLE_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.LEFT_RING_FINGER),
                "Should return true for LEFT_RING_FINGER in Auth");
        assertTrue(validator.isValidFingerPosition(AUTH_PURPOSE, FingerPosition.LEFT_LITTLE_FINGER),
                "Should return true for LEFT_LITTLE_FINGER in Auth");

        // Invalid finger position for Auth
        assertFalse(validator.isValidFingerPosition(AUTH_PURPOSE, 999),
                "Should return false for invalid finger position in Auth");
    }

    /**
     * Tests the isValidFingerPosition method for Registration purpose
     */
    @Test
    void isValidFingerPositionForRegistrationPurposeReturnsExpectedResult() {
        // Valid finger positions for Registration (excluding UNKNOWN)
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.RIGHT_THUMB),
                "Should return true for RIGHT_THUMB in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.RIGHT_INDEX_FINGER),
                "Should return true for RIGHT_INDEX_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.RIGHT_MIDDLE_FINGER),
                "Should return true for RIGHT_MIDDLE_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.RIGHT_RING_FINGER),
                "Should return true for RIGHT_RING_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.RIGHT_LITTLE_FINGER),
                "Should return true for RIGHT_LITTLE_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.LEFT_THUMB),
                "Should return true for LEFT_THUMB in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.LEFT_INDEX_FINGER),
                "Should return true for LEFT_INDEX_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.LEFT_MIDDLE_FINGER),
                "Should return true for LEFT_MIDDLE_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.LEFT_RING_FINGER),
                "Should return true for LEFT_RING_FINGER in Registration");
        assertTrue(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.LEFT_LITTLE_FINGER),
                "Should return true for LEFT_LITTLE_FINGER in Registration");

        // Invalid finger positions for Registration
        assertFalse(validator.isValidFingerPosition(REGISTRATION_PURPOSE, FingerPosition.UNKNOWN),
                "Should return false for UNKNOWN position in Registration");
        assertFalse(validator.isValidFingerPosition(REGISTRATION_PURPOSE, 999),
                "Should return false for invalid finger position in Registration");
    }

    /**
     * Tests the isValidFingerPosition method with an invalid purpose
     */
    @Test
    void isValidFingerPositionWithInvalidPurposeReturnsFalse() {
        assertFalse(validator.isValidFingerPosition("INVALID_PURPOSE", FingerPosition.RIGHT_THUMB),
                "Should return false for invalid purpose");
    }

    /**
     * Tests the isValidFingerPosition method with null purpose (exception handling)
     */
    @Test
    void isValidFingerPositionWithNullPurposeReturnsFalse() {
        assertFalse(validator.isValidFingerPosition(null, FingerPosition.RIGHT_THUMB),
                "Should return false when exception occurs (null purpose)");
    }

    /**
     * Tests the isValidRepresentationsNo method with boundary and invalid values
     */
    @Test
    void isValidRepresentationsNoReturnsBoundaryResults() {
        assertTrue(validator.isValidRepresentationsNo(0),
                "Should be valid for minimum value 0");
        assertTrue(validator.isValidRepresentationsNo(0x0F),
                "Should be valid for maximum value 15 (0x0F)");
        assertFalse(validator.isValidRepresentationsNo(-1),
                "Should be invalid for negative values");
        assertFalse(validator.isValidRepresentationsNo(0x10),
                "Should be invalid for values greater than 15");
    }

    /**
     * Tests the isValidScaleUnits method with valid and invalid scale unit values
     */
    @Test
    void isValidScaleUnitsReturnsExpectedResult() {
        assertTrue(validator.isValidScaleUnits(0x01),
                "Should be valid for 0x01 (PPI)");
        assertTrue(validator.isValidScaleUnits(0x02),
                "Should be valid for 0x02 (PPCM)");
        assertFalse(validator.isValidScaleUnits(0x00),
                "Should be invalid for 0x00");
        assertFalse(validator.isValidScaleUnits(0x03),
                "Should be invalid for 0x03");
    }

    /**
     * Tests the isValidScanSpatialSamplingRateHorizontal method with boundary and invalid values
     */
    @Test
    void isValidScanSpatialSamplingRateHorizontalReturnsBoundaryResults() {
        assertTrue(validator.isValidScanSpatialSamplingRateHorizontal(490),
                "Should be valid for minimum value 490");
        assertTrue(validator.isValidScanSpatialSamplingRateHorizontal(750),
                "Should be valid for middle value 750");
        assertTrue(validator.isValidScanSpatialSamplingRateHorizontal(1010),
                "Should be valid for maximum value 1010");

        assertFalse(validator.isValidScanSpatialSamplingRateHorizontal(489),
                "Should be invalid for value below minimum (489)");
        assertFalse(validator.isValidScanSpatialSamplingRateHorizontal(1011),
                "Should be invalid for value above maximum (1011)");
    }

    /**
     * Tests the isValidScanSpatialSamplingRateVertical method with boundary and invalid values
     */
    @Test
    void isValidScanSpatialSamplingRateVerticalReturnsBoundaryResults() {
        assertTrue(validator.isValidScanSpatialSamplingRateVertical(490),
                "Should be valid for minimum value 490");
        assertTrue(validator.isValidScanSpatialSamplingRateVertical(750),
                "Should be valid for middle value 750");
        assertTrue(validator.isValidScanSpatialSamplingRateVertical(1010),
                "Should be valid for maximum value 1010");

        assertFalse(validator.isValidScanSpatialSamplingRateVertical(489),
                "Should be invalid for value below minimum (489)");
        assertFalse(validator.isValidScanSpatialSamplingRateVertical(1011),
                "Should be invalid for value above maximum (1011)");
    }

    /**
     * Tests the isValidImageSpatialSamplingRateHorizontal method
     */
    @Test
    void isValidImageSpatialSamplingRateHorizontalReturnsExpectedResult() {
        // Valid cases
        assertTrue(validator.isValidImageSpatialSamplingRateHorizontal(1000, 500),
                "Should be valid when image rate is within range and <= scan rate");
        assertTrue(validator.isValidImageSpatialSamplingRateHorizontal(1000, 1000),
                "Should be valid when image rate equals scan rate");

        // Invalid cases - image rate out of range
        assertFalse(validator.isValidImageSpatialSamplingRateHorizontal(1000, 400),
                "Should be invalid when image rate is below minimum (490)");
        assertFalse(validator.isValidImageSpatialSamplingRateHorizontal(1000, 1100),
                "Should be invalid when image rate is above maximum (1010)");

        // Invalid cases - image rate > scan rate
        assertFalse(validator.isValidImageSpatialSamplingRateHorizontal(500, 600),
                "Should be invalid when image rate > scan rate");
    }

    /**
     * Tests the isValidImageSpatialSamplingRateVertical method
     */
    @Test
    void isValidImageSpatialSamplingRateVerticalReturnsExpectedResult() {
        // Valid cases
        assertTrue(validator.isValidImageSpatialSamplingRateVertical(1000, 500),
                "Should be valid when image rate is within range and <= scan rate");
        assertTrue(validator.isValidImageSpatialSamplingRateVertical(1000, 1000),
                "Should be valid when image rate equals scan rate");

        // Invalid cases - image rate out of range
        assertFalse(validator.isValidImageSpatialSamplingRateVertical(1000, 400),
                "Should be invalid when image rate is below minimum (490)");
        assertFalse(validator.isValidImageSpatialSamplingRateVertical(1000, 1100),
                "Should be invalid when image rate is above maximum (1010)");

        // Invalid cases - image rate > scan rate
        assertFalse(validator.isValidImageSpatialSamplingRateVertical(500, 600),
                "Should be invalid when image rate > scan rate");
    }

    /**
     * Tests the deprecated isValidBitDepth method
     */
    @Test
    void deprecatedIsValidBitDepthReturnsExpectedResult() {
        byte[] imageData = new byte[]{1, 2, 3};

        assertTrue(validator.isValidBitDepth(imageData, FingerImageBitDepth.BPP_08),
                "Should return true for 8-bit depth");
        assertFalse(validator.isValidBitDepth(imageData, 16),
                "Should return false for non-8-bit depth");
    }

    /**
     * Tests the new isValidBitDepth method with ImageDecoderRequestDto
     */
    @Test
    void isValidBitDepthWithDecoderReturnsExpectedResult() {
        when(decoderRequestDto.getDepth()).thenReturn(FingerImageBitDepth.BPP_08);

        assertTrue(validator.isValidBitDepth(FingerImageBitDepth.BPP_08, decoderRequestDto),
                "Should return true when both bit depths are 8");

        assertFalse(validator.isValidBitDepth(16, decoderRequestDto),
                "Should return false when bit depths don't match");

        when(decoderRequestDto.getDepth()).thenReturn(16);
        assertFalse(validator.isValidBitDepth(FingerImageBitDepth.BPP_08, decoderRequestDto),
                "Should return false when decoder depth is not 8");
    }

    /**
     * Tests the isValidImageCompressionType method for Auth purpose
     */
    @Test
    void isValidImageCompressionTypeForAuthReturnsExpectedResult() {
        when(decoderRequestDto.isLossless()).thenReturn(false);

        assertTrue(validator.isValidImageCompressionType(AUTH_PURPOSE,
                        FingerImageCompressionType.JPEG_2000_LOSSY, decoderRequestDto),
                "Should return true for JPEG 2000 lossy in Auth with lossy decoder");

        assertTrue(validator.isValidImageCompressionType(AUTH_PURPOSE,
                        FingerImageCompressionType.WSQ, decoderRequestDto),
                "Should return true for WSQ in Auth with lossy decoder");

        when(decoderRequestDto.isLossless()).thenReturn(true);
        assertFalse(validator.isValidImageCompressionType(AUTH_PURPOSE,
                        FingerImageCompressionType.JPEG_2000_LOSSY, decoderRequestDto),
                "Should return false for lossy compression with lossless decoder");
    }

    /**
     * Tests the isValidImageCompressionType method for Registration purpose
     */
    @Test
    void isValidImageCompressionTypeForRegistrationReturnsExpectedResult() {
        when(decoderRequestDto.isLossless()).thenReturn(true);

        assertTrue(validator.isValidImageCompressionType(REGISTRATION_PURPOSE,
                        FingerImageCompressionType.JPEG_2000_LOSS_LESS, decoderRequestDto),
                "Should return true for JPEG 2000 lossless in Registration with lossless decoder");

        when(decoderRequestDto.isLossless()).thenReturn(false);
        assertFalse(validator.isValidImageCompressionType(REGISTRATION_PURPOSE,
                        FingerImageCompressionType.JPEG_2000_LOSS_LESS, decoderRequestDto),
                "Should return false for lossless compression with lossy decoder");
    }

    /**
     * Tests the isValidImageCompressionType method with invalid purpose
     */
    @Test
    void isValidImageCompressionTypeWithInvalidPurposeReturnsFalse() {
        assertFalse(validator.isValidImageCompressionType("INVALID",
                        FingerImageCompressionType.JPEG_2000_LOSSY, decoderRequestDto),
                "Should return false for invalid purpose");
    }

    /**
     * Tests the isValidImageCompressionType method with null purpose (exception handling)
     */
    @Test
    void isValidImageCompressionTypeWithNullPurposeReturnsFalse() {
        assertFalse(validator.isValidImageCompressionType(null,
                        FingerImageCompressionType.JPEG_2000_LOSSY, decoderRequestDto),
                "Should return false when exception occurs (null purpose)");
    }

    /**
     * Tests the isValidImageImpressionType method with valid and invalid impression types
     */
    @Test
    void isValidImageImpressionTypeReturnsExpectedResult() {
        assertTrue(validator.isValidImageImpressionType(FingerImpressionType.LIVE_SCAN_PLAIN),
                "Should be valid for LIVE_SCAN_PLAIN impression type");
        assertTrue(validator.isValidImageImpressionType(FingerImpressionType.LATENT_PALM_LIFT),
                "Should be valid for LATENT_PALM_LIFT impression type");
        assertTrue(validator.isValidImageImpressionType(FingerImpressionType.LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN),
                "Should be valid for LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN impression type");
        assertTrue(validator.isValidImageImpressionType(FingerImpressionType.OTHER),
                "Should be valid for OTHER impression type");
        assertTrue(validator.isValidImageImpressionType(FingerImpressionType.UNKNOWN),
                "Should be valid for UNKNOWN impression type");

        assertFalse(validator.isValidImageImpressionType(999),
                "Should be invalid for unknown impression type");
    }
}
