package io.mosip.biometrics.util.finger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

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
    void getInstance_always_returnsSameInstance() {
        FingerISOStandardsValidator instance1 = FingerISOStandardsValidator.getInstance();
        FingerISOStandardsValidator instance2 = FingerISOStandardsValidator.getInstance();
        
        assertSame(instance1, instance2, "Multiple instances of singleton created");
    }

    /**
     * Tests the isValidFormatIdentifier method with valid and invalid format identifiers
     */
    @Test
    void isValidFormatIdentifier_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidFormatIdentifier(FingerFormatIdentifier.FORMAT_FIR),
                "Should return true for valid format identifier");
        assertFalse(validator.isValidFormatIdentifier(9999),
                "Should return false for invalid format identifier");
    }

    /**
     * Tests the isValidVersionNumber method with valid and invalid version numbers
     */
    @Test
    void isValidVersionNumber_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidVersionNumber(FingerVersionNumber.VERSION_020),
                "Should return true for valid version number");
        assertFalse(validator.isValidVersionNumber(123),
                "Should return false for invalid version number");
    }

    /**
     * Tests the isValidRecordLength method with matching and non-matching lengths
     */
    @Test
    void isValidRecordLength_withDifferentInputs_returnsExpectedResult() {
        assertTrue(validator.isValidRecordLength(100, 100),
                "Should return true when lengths match");
        assertFalse(validator.isValidRecordLength(100, 200),
                "Should return false when lengths don't match");
    }

    /**
     * Tests the isValidNoOfRepresentations method with valid and invalid values
     */
    @Test
    void isValidNoOfRepresentations_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidNoOfRepresentations(0x0001),
                "Should return true for 0x0001");
        assertFalse(validator.isValidNoOfRepresentations(0x0002),
                "Should return false for values other than 0x0001");
    }

    /**
     * Tests the isValidCertificationFlag method with valid and invalid certification flags
     */
    @Test
    void isValidCertificationFlag_withVariousValues_returnsExpectedResult() {
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
    void isValidNoOfFingerPresent_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidNoOfFingerPresent(0x01),
                "Should return true for 0x01");
        assertFalse(validator.isValidNoOfFingerPresent(0x02),
                "Should return false for values other than 0x01");
    }

    /**
     * Tests the isValidCaptureDeviceTechnologyIdentifier method with valid and invalid values
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifier_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(FingerCaptureDeviceTechnology.UNSPECIFIED),
                "Should return true for UNSPECIFIED technology");
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(FingerCaptureDeviceTechnology.GLASS_FIBER),
                "Should return true for GLASS_FIBER technology");
        assertFalse(validator.isValidCaptureDeviceTechnologyIdentifier(999),
                "Should return false for invalid technology identifier");
    }

    /**
     * Tests the isValidCaptureDeviceVendor method with valid and invalid values
     */
    @Test
    void isValidCaptureDeviceVendor_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidCaptureDeviceVendor(FingerCaptureDeviceVendor.UNSPECIFIED),
                "Should return true for UNSPECIFIED vendor");
        assertTrue(validator.isValidCaptureDeviceVendor(FingerCaptureDeviceVendor.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF");
        assertFalse(validator.isValidCaptureDeviceVendor(0x10000),
                "Should return false for invalid vendor identifier");
    }

    /**
     * Tests the isValidNoOfQualityBlocks method with boundary and invalid values
     */
    @Test
    void isValidNoOfQualityBlocks_withBoundaryValues_returnsExpectedResult() {
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
    void isValidQualityScore_withVariousValues_returnsExpectedResult() {
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
    }

    /**
     * Tests the isValidQualityAlgorithmIdentifier method with valid and invalid algorithm identifiers
     */
    @Test
    void isValidQualityAlgorithmIdentifier_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.UNSPECIFIED),
                "Should return true for UNSPECIFIED algorithm identifier");
        assertTrue(validator.isValidQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF algorithm identifier");
        assertFalse(validator.isValidQualityAlgorithmIdentifier(0x10000),
                "Should return false for invalid algorithm identifier");
    }

    /**
     * Tests the isValidQualityAlgorithmVendorIdentifier method with valid and invalid vendor identifiers
     */
    @Test
    void isValidQualityAlgorithmVendorIdentifier_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED),
                "Should return true for UNSPECIFIED vendor identifier");
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF vendor identifier");
        assertFalse(validator.isValidQualityAlgorithmVendorIdentifier(0x10000),
                "Should return false for invalid vendor identifier");
    }

    /**
     * Tests the isValidNoOfCertificationBlocks method with boundary and invalid values
     */
    @Test
    void isValidNoOfCertificationBlocks_withBoundaryValues_returnsExpectedResult() {
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
    void isValidCertificationAuthorityID_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidCertificationAuthorityID(FingerCertificationAuthorityID.UNSPECIFIED),
                "Should return true for UNSPECIFIED authority ID");
        assertTrue(validator.isValidCertificationAuthorityID(FingerCertificationAuthorityID.VENDOR_FFFF),
                "Should return true for VENDOR_FFFF authority ID");
        assertFalse(validator.isValidCertificationAuthorityID(0x10000),
                "Should return false for invalid authority ID");
    }

    /**
     * Tests the isValidCertificationSchemeIdentifier method with valid and invalid scheme identifiers
     */
    @Test
    void isValidCertificationSchemeIdentifier_withVariousValues_returnsExpectedResult() {
        assertTrue(validator.isValidCertificationSchemeIdentifier(FingerCertificationSchemeIdentifier.UNSPECIFIED),
                "Should return true for UNSPECIFIED scheme identifier");
        assertTrue(validator.isValidCertificationSchemeIdentifier(
                FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER),
                "Should return true for valid scheme identifier");
        assertFalse(validator.isValidCertificationSchemeIdentifier(999),
                "Should return false for invalid scheme identifier");
    }

    /**
     * Tests the isValidFingerPosition method with an invalid purpose
     */
    @Test
    void isValidFingerPosition_withInvalidPurpose_returnsFalse() {
        assertFalse(validator.isValidFingerPosition("INVALID_PURPOSE", FingerPosition.RIGHT_THUMB),
                "Should return false for invalid purpose");
    }

    /**
     * Tests the isValidRepresentationsNo method with boundary and invalid values
     */
    @Test
    void isValidRepresentationsNo_withBoundaryValues_returnsExpectedResult() {
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
    void isValidScaleUnits_withVariousValues_returnsExpectedResult() {
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
    void isValidScanSpatialSamplingRateHorizontal_withBoundaryValues_returnsExpectedResult() {
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
    void isValidScanSpatialSamplingRateVertical_withBoundaryValues_returnsExpectedResult() {
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
     * Tests the isValidImageImpressionType method with valid and invalid impression types
     */
    @Test
    void isValidImageImpressionType_withVariousValues_returnsExpectedResult() {
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