package io.mosip.biometrics.util.face;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.mosip.biometrics.util.ImageDecoderRequestDto;

/**
 * Unit tests for {@link FaceISOStandardsValidator} class.
 * This class validates the ISO/IEC 19794-5 standard compliance for face biometric data.
 */
@ExtendWith(MockitoExtension.class)
class FaceISOStandardsValidatorTest {

    private FaceISOStandardsValidator validator;

    @Mock
    private ImageDecoderRequestDto decoderRequestDto;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        validator = FaceISOStandardsValidator.getInstance();
    }

    /**
     * Verifies that getInstance() returns the same singleton instance.
     */
    @Test
    void getInstance_WhenCalledMultipleTimes_ReturnsSameInstance() {
        FaceISOStandardsValidator anotherInstance = FaceISOStandardsValidator.getInstance();

        assertNotNull(anotherInstance);
        assertSame(validator, anotherInstance);
    }

    /**
     * Verifies format identifier validation accepts FORMAT_FAC constant.
     */
    @Test
    void isValidFormatIdentifier_WithValidFormatFAC_ReturnsTrue() {
        assertTrue(validator.isValidFormatIdentifier(0x46414300L));
    }

    /**
     * Verifies format identifier validation rejects invalid values.
     */
    @Test
    void isValidFormatIdentifier_WithInvalidFormat_ReturnsFalse() {
        assertFalse(validator.isValidFormatIdentifier(0x12345678L));
    }

    /**
     * Verifies version number validation accepts VERSION_030 constant.
     */
    @Test
    void isValidVersionNumber_WithVersion030_ReturnsTrue() {
        assertTrue(validator.isValidVersionNumber(0x30333000L));
    }

    /**
     * Verifies version number validation rejects invalid values.
     */
    @Test
    void isValidVersionNumber_WithInvalidVersion_ReturnsFalse() {
        assertFalse(validator.isValidVersionNumber(0x01020304L));
    }

    /**
     * Verifies record length validation accepts matching lengths.
     */
    @Test
    void isValidRecordLength_WithMatchingLengths_ReturnsTrue() {
        assertTrue(validator.isValidRecordLength(100L, 100L));
    }

    /**
     * Verifies number of representations validation accepts 0x0001.
     */
    @Test
    void isValidNoOfRepresentations_WithSingleRepresentation_ReturnsTrue() {
        assertTrue(validator.isValidNoOfRepresentations(0x0001));
    }

    /**
     * Verifies number of representations validation rejects invalid values.
     */
    @Test
    void isValidNoOfRepresentations_WithInvalidValues_ReturnsFalse() {
        assertFalse(validator.isValidNoOfRepresentations(0x0002));
        assertFalse(validator.isValidNoOfRepresentations(0x0000));
    }

    /**
     * Verifies gender validation accepts all valid gender constants.
     */
    @Test
    void isValidGender_WithValidGenderValues_ReturnsTrue() {
        assertTrue(validator.isValidGender(Gender.UNSPECIFIED));
        assertTrue(validator.isValidGender(Gender.MALE));
        assertTrue(validator.isValidGender(Gender.FEMALE));
        assertTrue(validator.isValidGender(Gender.UNKNOWN));
    }

    /**
     * Verifies gender validation rejects invalid values.
     */
    @Test
    void isValidGender_WithInvalidGender_ReturnsFalse() {
        assertFalse(validator.isValidGender(0x0003));
    }

    /**
     * Verifies eye color validation accepts all valid constants.
     */
    @Test
    void isValidEyeColour_WithValidColors_ReturnsTrue() {
        assertTrue(validator.isValidEyeColour(EyeColour.UNSPECIFIED));
        assertTrue(validator.isValidEyeColour(EyeColour.BLACK));
        assertTrue(validator.isValidEyeColour(EyeColour.PINK));
        assertTrue(validator.isValidEyeColour(EyeColour.OTHER_OR_UNKNOWN));
    }

    /**
     * Verifies hair color validation accepts all valid constants.
     */
    @Test
    void isValidHairColour_WithValidColors_ReturnsTrue() {
        assertTrue(validator.isValidHairColour(HairColour.UNSPECIFIED));
        assertTrue(validator.isValidHairColour(HairColour.WHITE));
        assertTrue(validator.isValidHairColour(HairColour.RED));
        assertTrue(validator.isValidHairColour(HairColour.UNKNOWN));
    }

    /**
     * Verifies subject height validation accepts valid range and rejects invalid values.
     */
    @Test
    void isValidSubjectHeight_WithValidAndInvalidValues_ValidatesCorrectly() {
        assertTrue(validator.isValidSubjectHeight(0));
        assertTrue(validator.isValidSubjectHeight(128));
        assertTrue(validator.isValidSubjectHeight(255));
        assertFalse(validator.isValidSubjectHeight(-1));
        assertFalse(validator.isValidSubjectHeight(256));
    }

    /**
     * Verifies face image type validation accepts valid ranges.
     */
    @Test
    void isValidFaceImageType_WithValidRanges_ReturnsTrue() {
        for (int i = FaceImageType.BASIC; i <= FaceImageType.POST_PROCESSED_FRONTAL; i++) {
            assertTrue(validator.isValidFaceImageType(i));
        }
        for (int i = FaceImageType.BASIC_3D; i <= FaceImageType.TOKEN_FRONTAL_3D; i++) {
            assertTrue(validator.isValidFaceImageType(i));
        }
    }

    /**
     * Verifies image compression type validation based on purpose.
     */
    @Test
    void isValidImageCompressionType_WithValidAndInvalidInputs_ValidatesCorrectly() {
        assertTrue(validator.isValidImageCompressionType("Auth", ImageDataType.JPEG2000_LOSSY, decoderRequestDto));
        assertTrue(validator.isValidImageCompressionType("Registration", ImageDataType.JPEG2000_LOSS_LESS, decoderRequestDto));
        assertFalse(validator.isValidImageCompressionType("INVALID", ImageDataType.JPEG2000_LOSS_LESS, decoderRequestDto));
        assertFalse(validator.isValidImageCompressionType("Auth", ImageDataType.JPEG2000_LOSS_LESS, decoderRequestDto));
    }

    /**
     * Verifies image color space validation for RGB input.
     */
    @Test
    void isValidImageColourSpace_WithRGBInput_ValidatesCorrectly() {
        when(decoderRequestDto.getImageColorSpace()).thenReturn("RGB");

        assertTrue(validator.isValidImageColourSpace("Registration", ImageColourSpace.BIT_24_RGB, decoderRequestDto));
        assertTrue(validator.isValidImageColourSpace("Registration", ImageColourSpace.UNSPECIFIED, decoderRequestDto));
        assertFalse(validator.isValidImageColourSpace("Registration", 0x0002, decoderRequestDto));
    }

    /**
     * Verifies spatial sampling rate level validation.
     */
    @Test
    void isValidSpatialSamplingRateLevel_WithValidAndInvalidInputs_ValidatesCorrectly() {
        assertTrue(validator.isValidSpatialSamplingRateLevel(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180));
        assertTrue(validator.isValidSpatialSamplingRateLevel(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_300_TO_370));
        assertTrue(validator.isValidSpatialSamplingRateLevel(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_480_TO_610));
        assertTrue(validator.isValidSpatialSamplingRateLevel(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_750));
        assertFalse(validator.isValidSpatialSamplingRateLevel(-1));
        assertFalse(validator.isValidSpatialSamplingRateLevel(SpatialSamplingRateLevel.RESERVED_FF));
        assertFalse(validator.isValidSpatialSamplingRateLevel(9));
    }

    /**
     * Verifies quality score validation.
     */
    @Test
    void isValidQualityScore_WithValidAndInvalidScores_ValidatesCorrectly() {
        assertTrue(validator.isValidQualityScore(0));
        assertTrue(validator.isValidQualityScore(50));
        assertTrue(validator.isValidQualityScore(100));
        assertTrue(validator.isValidQualityScore(0xFF));
        assertFalse(validator.isValidQualityScore(-1));
        assertFalse(validator.isValidQualityScore(101));
        assertFalse(validator.isValidQualityScore(0xFE));
    }

    /**
     * Verifies capture device type validation with vendor.
     */
    @Test
    void isValidCaptureDeviceType_WithValidAndInvalidTypes_ValidatesCorrectly() {
        assertTrue(validator.isValidCaptureDeviceType(FaceCaptureDeviceType.UNSPECIFIED, FaceCaptureDeviceVendor.UNSPECIFIED));
        assertTrue(validator.isValidCaptureDeviceType(FaceCaptureDeviceType.VENDOR_FFFF, FaceCaptureDeviceVendor.VENDOR_FFFF));
        assertTrue(validator.isValidCaptureDeviceType(FaceCaptureDeviceType.VENDOR_FFFF, FaceCaptureDeviceVendor.UNSPECIFIED));
        assertFalse(validator.isValidCaptureDeviceType(-1, FaceCaptureDeviceVendor.UNSPECIFIED));
        assertFalse(validator.isValidCaptureDeviceType(0x10000, FaceCaptureDeviceVendor.UNSPECIFIED));
    }

    /**
     * Verifies certification flag validation.
     */
    @Test
    void isValidCertificationFlag_WithValidAndInvalidFlags_ValidatesCorrectly() {
        assertTrue(validator.isValidCertificationFlag(FaceCertificationFlag.UNSPECIFIED));
        assertFalse(validator.isValidCertificationFlag(0x01));
    }

    /**
     * Verifies temporal semantics validation.
     */
    @Test
    void isValidTemporalSemantics_WithValidAndInvalidValues_ValidatesCorrectly() {
        assertTrue(validator.isValidTemporalSemantics(TemporalSequenceFlags.ONE_REPRESENTATION));
        assertFalse(validator.isValidTemporalSemantics(0x02));
    }

    /**
     * Verifies representation length validation.
     */
    @Test
    void isValidRepresentationLength_WithValidAndInvalidLengths_ValidatesCorrectly() {
        assertTrue(validator.isValidRepresentationLength(0x33));
        assertTrue(validator.isValidRepresentationLength(0x1000));
        assertFalse(validator.isValidRepresentationLength(0x32));
        assertFalse(validator.isValidRepresentationLength(0xFFFFFFF0L));
        assertFalse(validator.isValidRepresentationLength(-1));
    }

    /**
     * Verifies quality blocks count validation.
     */
    @Test
    void isValidNoOfQualityBlocks_WithValidAndInvalidCounts_ValidatesCorrectly() {
        assertTrue(validator.isValidNoOfQualityBlocks(0));
        assertTrue(validator.isValidNoOfQualityBlocks(128));
        assertTrue(validator.isValidNoOfQualityBlocks(255));
        assertFalse(validator.isValidNoOfQualityBlocks(-1));
        assertFalse(validator.isValidNoOfQualityBlocks(256));
    }

    /**
     * Verifies quality algorithm identifier validation.
     */
    @Test
    void isValidQualityAlgorithmIdentifier_WithValidAndInvalidIdentifiers_ValidatesCorrectly() {
        assertTrue(validator.isValidQualityAlgorithmIdentifier(FaceQualityAlgorithmIdentifier.UNSPECIFIED));
        assertTrue(validator.isValidQualityAlgorithmIdentifier(FaceQualityAlgorithmIdentifier.VENDOR_FFFF));
        assertFalse(validator.isValidQualityAlgorithmIdentifier(-1));
        assertFalse(validator.isValidQualityAlgorithmIdentifier(0x10000));
    }

    /**
     * Verifies feature mask validation.
     */
    @Test
    void isValidFeatureMask_WithValidAndInvalidMasks_ValidatesCorrectly() {
        assertTrue(validator.isValidFeatureMask(0x000000));
        assertTrue(validator.isValidFeatureMask(0x7FFFFF));
        assertTrue(validator.isValidFeatureMask(0xFFFFFF));
        assertFalse(validator.isValidFeatureMask(-1));
        assertFalse(validator.isValidFeatureMask(0x1000000));
    }

    /**
     * Verifies expression mask validation.
     */
    @Test
    void isValidExpressionMask_WithValidAndInvalidMasks_ValidatesCorrectly() {
        assertTrue(validator.isValidExpressionMask(0x0000));
        assertTrue(validator.isValidExpressionMask(0x7FFF));
        assertTrue(validator.isValidExpressionMask(0xFFFF));
        assertFalse(validator.isValidExpressionMask(-1));
        assertFalse(validator.isValidExpressionMask(0x10000));
    }
}