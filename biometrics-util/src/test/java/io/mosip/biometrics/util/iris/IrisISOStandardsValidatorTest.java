package io.mosip.biometrics.util.iris;

import io.mosip.biometrics.util.ImageDecoderRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link IrisISOStandardsValidator}.
 */
class IrisISOStandardsValidatorTest {

    private final IrisISOStandardsValidator validator = IrisISOStandardsValidator.getInstance();

    /**
     * Test for valid format identifier.
     */
    @Test
    void isValidFormatIdentifier_validFormatIdentifier_true() {
        assertTrue(validator.isValidFormatIdentifier(IrisFormatIdentifier.FORMAT_IIR));
    }

    /**
     * Test for invalid version number.
     */
    @Test
    void isValidVersionNumber_invalidVersion_false() {
        assertFalse(validator.isValidVersionNumber(1));
    }

    /**
     * Test for record lengths that match.
     */
    @Test
    void isValidRecordLength_matchingLengths_true() {
        assertTrue(validator.isValidRecordLength(100L, 100L));
    }

    /**
     * Test for a valid number of representations.
     */
    @Test
    void isValidNoOfRepresentations_validRepresentation_true() {
        assertTrue(validator.isValidNoOfRepresentations(0x0001));
    }

    /**
     * Test for a valid certification flag.
     */
    @Test
    void isValidCertificationFlag_unspecifiedFlag_true() {
        assertTrue(validator.isValidCertificationFlag(IrisCertificationFlag.UNSPECIFIED));
    }

    /**
     * Test for a valid representation length.
     */
    @Test
    void isValidRepresentationLength_withinValidRange_true() {
        assertTrue(validator.isValidRepresentationLength(0x00000035));
    }

    /**
     * Test for valid capture device technology identifier.
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifier_inRange_true() {
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(IrisCaptureDeviceTechnology.CMOS_OR_CCD));
    }

    /**
     * Test for invalid capture device vendor.
     */
    @Test
    void isValidCaptureDeviceVendor_outOfRange_false() {
        assertFalse(validator.isValidCaptureDeviceVendor(999999));
    }

    /**
     * Tests isValidCaptureDeviceType for valid and invalid combinations.
     */
    @Test
    void isValidCaptureDeviceType_variousCases() {
        assertTrue(validator.isValidCaptureDeviceType(IrisCaptureDeviceType.UNSPECIFIED, IrisCaptureDeviceVendor.UNSPECIFIED));
        assertTrue(validator.isValidCaptureDeviceType(IrisCaptureDeviceType.VENDOR_FFFF, IrisCaptureDeviceVendor.UNSPECIFIED));
        assertFalse(validator.isValidCaptureDeviceType(-1, -1));
    }

    /**
     * Tests isValidNoOfQualityBlocks for boundary values.
     */
    @Test
    void isValidNoOfQualityBlocks_boundaries() {
        assertTrue(validator.isValidNoOfQualityBlocks(0x00));
        assertTrue(validator.isValidNoOfQualityBlocks(0xFF));
        assertFalse(validator.isValidNoOfQualityBlocks(-1));
        assertFalse(validator.isValidNoOfQualityBlocks(0x100));
    }

    /**
     * Tests isValidQualityAlgorithmIdentifier for valid and invalid values.
     */
    @Test
    void isValidQualityAlgorithmIdentifier_variousCases() {
        assertTrue(validator.isValidQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.UNSPECIFIED));
        assertTrue(validator.isValidQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.VENDOR_FFFF));
        assertFalse(validator.isValidQualityAlgorithmIdentifier(-1));
    }

    /**
     * Tests isValidQualityAlgorithmVendorIdentifier for valid and invalid values.
     */
    @Test
    void isValidQualityAlgorithmVendorIdentifier_variousCases() {
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED));
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF));
        assertFalse(validator.isValidQualityAlgorithmVendorIdentifier(-1));
    }

    /**
     * Tests isValidQualityScore for valid and invalid values.
     */
    @Test
    void isValidQualityScore_variousCases() {
        assertTrue(validator.isValidQualityScore(0x00));
        assertTrue(validator.isValidQualityScore(0x64));
        assertTrue(validator.isValidQualityScore(0xFF));
        assertFalse(validator.isValidQualityScore(-1));
        assertFalse(validator.isValidQualityScore(0x65));
    }

    /**
     * Tests isValidNoOfRepresentation for valid and invalid values.
     */
    @Test
    void isValidNoOfRepresentation_variousCases() {
        assertTrue(validator.isValidNoOfRepresentation(0x0001));
        assertFalse(validator.isValidNoOfRepresentation(0x0002));
    }

    /**
     * Tests isValidImageFromat for valid and invalid values.
     */
    @Test
    void isValidImageFromat_variousCases() {
        assertTrue(validator.isValidImageFromat(ImageFormat.MONO_JPEG2000));
        assertFalse(validator.isValidImageFromat(1234));
    }

    /**
     * Tests isValidImageHorizontalOrientation for valid and invalid values.
     */
    @Test
    void isValidImageHorizontalOrientation_variousCases() {
        assertTrue(validator.isValidImageHorizontalOrientation(HorizontalOrientation.ORIENTATION_UNDEFINIED));
        assertTrue(validator.isValidImageHorizontalOrientation(HorizontalOrientation.ORIENTATION_FLIPPED));
        assertFalse(validator.isValidImageHorizontalOrientation(-1));
        assertFalse(validator.isValidImageHorizontalOrientation(999));
    }

    /**
     * Tests isValidImageVerticalOrientation for valid and invalid values.
     */
    @Test
    void isValidImageVerticalOrientation_variousCases() {
        assertTrue(validator.isValidImageVerticalOrientation(VerticalOrientation.ORIENTATION_UNDEFINIED));
        assertTrue(validator.isValidImageVerticalOrientation(VerticalOrientation.ORIENTATION_FLIPPED));
        assertFalse(validator.isValidImageVerticalOrientation(-1));
        assertFalse(validator.isValidImageVerticalOrientation(999));
    }

    /**
     * Tests isValidBitDepth(int, ImageDecoderRequestDto) for valid and invalid values.
     */
    @Test
    void isValidBitDepth_withDto_variousCases() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.getDepth()).thenReturn(IrisImageBitDepth.BPP_08);
        assertTrue(validator.isValidBitDepth(IrisImageBitDepth.BPP_08, dto));
        Mockito.when(dto.getDepth()).thenReturn(123);
        assertFalse(validator.isValidBitDepth(IrisImageBitDepth.BPP_08, dto));
    }

    /**
     * Tests isValidRange for valid and invalid values.
     */
    @Test
    void isValidRange_variousCases() {
        assertTrue(validator.isValidRange(IrisRange.UNASSIGNED));
        assertTrue(validator.isValidRange(IrisRange.OVERFLOW_FFFF));
        assertFalse(validator.isValidRange(-1));
        assertFalse(validator.isValidRange(999999));
    }

    /**
     * Tests isValidRollAngleOfEye for valid and invalid values.
     */
    @Test
    void isValidRollAngleOfEye_variousCases() {
        assertTrue(validator.isValidRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000));
        assertTrue(validator.isValidRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED));
        assertFalse(validator.isValidRollAngleOfEye(-1));
        assertFalse(validator.isValidRollAngleOfEye(999999));
    }

    /**
     * Tests isValidRollAngleUncertainty for valid and invalid values.
     */
    @Test
    void isValidRollAngleUncertainty_variousCases() {
        assertTrue(validator.isValidRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000));
        assertTrue(validator.isValidRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED));
        assertFalse(validator.isValidRollAngleUncertainty(-1));
        assertFalse(validator.isValidRollAngleUncertainty(999999));
    }

    /**
     * Tests isValidIrisCenterSmallestX for valid and invalid values.
     */
    @Test
    void isValidIrisCenterSmallestX_variousCases() {
        assertTrue(validator.isValidIrisCenterSmallestX(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterSmallestX(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterSmallestX(-1));
        assertFalse(validator.isValidIrisCenterSmallestX(999999));
    }

    /**
     * Tests isValidIrisCenterLargestX for valid and invalid values.
     */
    @Test
    void isValidIrisCenterLargestX_variousCases() {
        assertTrue(validator.isValidIrisCenterLargestX(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterLargestX(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterLargestX(-1));
        assertFalse(validator.isValidIrisCenterLargestX(999999));
    }

    /**
     * Tests isValidIrisCenterSmallestY for valid and invalid values.
     */
    @Test
    void isValidIrisCenterSmallestY_variousCases() {
        assertTrue(validator.isValidIrisCenterSmallestY(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterSmallestY(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterSmallestY(-1));
        assertFalse(validator.isValidIrisCenterSmallestY(999999));
    }

    /**
     * Tests isValidIrisCenterLargestY for valid and invalid values.
     */
    @Test
    void isValidIrisCenterLargestY_variousCases() {
        assertTrue(validator.isValidIrisCenterLargestY(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterLargestY(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterLargestY(-1));
        assertFalse(validator.isValidIrisCenterLargestY(999999));
    }

    /**
     * Tests isValidIrisDiameterSmallest for valid and invalid values.
     */
    @Test
    void isValidIrisDiameterSmallest_variousCases() {
        assertTrue(validator.isValidIrisDiameterSmallest(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisDiameterSmallest(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisDiameterSmallest(-1));
        assertFalse(validator.isValidIrisDiameterSmallest(999999));
    }

    /**
     * Tests isValidIrisDiameterLargest for valid and invalid values.
     */
    @Test
    void isValidIrisDiameterLargest_variousCases() {
        assertTrue(validator.isValidIrisDiameterLargest(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisDiameterLargest(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisDiameterLargest(-1));
        assertFalse(validator.isValidIrisDiameterLargest(999999));
    }
}