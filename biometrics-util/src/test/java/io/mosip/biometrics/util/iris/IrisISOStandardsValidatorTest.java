package io.mosip.biometrics.util.iris;

import io.mosip.biometrics.util.ImageDecoderRequestDto;
import io.mosip.biometrics.util.Purposes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link IrisISOStandardsValidator}.
 */
class IrisISOStandardsValidatorTest {

    private final IrisISOStandardsValidator validator = IrisISOStandardsValidator.getInstance();

    /**
     * Test singleton pattern - getInstance should return the same instance.
     */
    @Test
    void getInstanceReturnsSameInstance() {
        IrisISOStandardsValidator instance1 = IrisISOStandardsValidator.getInstance();
        IrisISOStandardsValidator instance2 = IrisISOStandardsValidator.getInstance();
        assertSame(instance1, instance2);
    }

    /**
     * Test for valid format identifier.
     */
    @Test
    void isValidFormatIdentifierReturnsTrue() {
        assertTrue(validator.isValidFormatIdentifier(IrisFormatIdentifier.FORMAT_IIR));
    }

    /**
     * Test for invalid format identifier.
     */
    @Test
    void isValidFormatIdentifierInvalidReturnsFalse() {
        assertFalse(validator.isValidFormatIdentifier(123));
    }

    /**
     * Test for valid version number.
     */
    @Test
    void isValidVersionNumberValidReturnsTrue() {
        assertTrue(validator.isValidVersionNumber(IrisVersionNumber.VERSION_020));
    }

    /**
     * Test for invalid version number.
     */
    @Test
    void isValidVersionNumberInvalidVersionReturnsFalse() {
        assertFalse(validator.isValidVersionNumber(1));
    }

    /**
     * Test for record lengths that match.
     */
    @Test
    void isValidRecordLengthMatchingLengthsReturnsTrue() {
        assertTrue(validator.isValidRecordLength(100L, 100L));
    }

    /**
     * Test for record lengths that don't match.
     */
    @Test
    void isValidRecordLengthMismatchedLengthsReturnsFalse() {
        assertFalse(validator.isValidRecordLength(100L, 200L));
    }

    /**
     * Test for a valid number of representations.
     */
    @Test
    void isValidNoOfRepresentationsValidReturnsTrue() {
        assertTrue(validator.isValidNoOfRepresentations(0x0001));
    }

    /**
     * Test for invalid number of representations.
     */
    @Test
    void isValidNoOfRepresentationsInvalidReturnsFalse() {
        assertFalse(validator.isValidNoOfRepresentations(0x0002));
    }

    /**
     * Test for a valid certification flag.
     */
    @Test
    void isValidCertificationFlagUnspecifiedReturnsTrue() {
        assertTrue(validator.isValidCertificationFlag(IrisCertificationFlag.UNSPECIFIED));
    }

    /**
     * Test for invalid certification flag.
     */
    @Test
    void isValidCertificationFlagInvalidReturnsFalse() {
        assertFalse(validator.isValidCertificationFlag(123));
    }

    /**
     * Test for valid number of eyes represented - UNKNOWN.
     */
    @Test
    void isValidNoOfEyesRepresentedUnknownReturnsTrue() {
        assertTrue(validator.isValidNoOfEyesRepresented(NoOfEyesRepresented.UNKNOWN));
    }

    /**
     * Test for valid number of eyes represented - LEFT_OR_RIGHT_EYE_PRESENT.
     */
    @Test
    void isValidNoOfEyesRepresentedLeftOrRightReturnsTrue() {
        assertTrue(validator.isValidNoOfEyesRepresented(NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT));
    }

    /**
     * Test for invalid number of eyes represented.
     */
    @Test
    void isValidNoOfEyesRepresentedInvalidReturnsFalse() {
        assertFalse(validator.isValidNoOfEyesRepresented(999));
    }

    /**
     * Test for a valid representation length.
     */
    @Test
    void isValidRepresentationLengthWithinRangeReturnsTrue() {
        assertTrue(validator.isValidRepresentationLength(0x00000035));
    }

    /**
     * Test for representation length at upper bound.
     */
    @Test
    void isValidRepresentationLengthUpperBoundReturnsTrue() {
        assertTrue(validator.isValidRepresentationLength(Long.decode("0xFFFFFFEF")));
    }

    /**
     * Test for representation length below minimum.
     */
    @Test
    void isValidRepresentationLengthBelowMinimumReturnsFalse() {
        assertFalse(validator.isValidRepresentationLength(0x00000034));
    }

    /**
     * Test for representation length above maximum.
     */
    @Test
    void isValidRepresentationLengthAboveMaximumReturnsFalse() {
        assertFalse(validator.isValidRepresentationLength(Long.decode("0xFFFFFFF0")));
    }

    /**
     * Test for valid capture device technology identifier.
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifierInRangeReturnsTrue() {
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(IrisCaptureDeviceTechnology.CMOS_OR_CCD));
    }

    /**
     * Test for valid capture device technology identifier at lower bound.
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifierLowerBoundReturnsTrue() {
        assertTrue(validator.isValidCaptureDeviceTechnologyIdentifier(IrisCaptureDeviceTechnology.UNSPECIFIED));
    }

    /**
     * Test for invalid capture device technology identifier below range.
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifierBelowRangeReturnsFalse() {
        assertFalse(validator.isValidCaptureDeviceTechnologyIdentifier(-1));
    }

    /**
     * Test for invalid capture device technology identifier above range.
     */
    @Test
    void isValidCaptureDeviceTechnologyIdentifierAboveRangeReturnsFalse() {
        assertFalse(validator.isValidCaptureDeviceTechnologyIdentifier(999));
    }

    /**
     * Test for valid capture device vendor.
     */
    @Test
    void isValidCaptureDeviceVendorInRangeReturnsTrue() {
        assertTrue(validator.isValidCaptureDeviceVendor(IrisCaptureDeviceVendor.UNSPECIFIED));
        assertTrue(validator.isValidCaptureDeviceVendor(IrisCaptureDeviceVendor.VENDOR_FFFF));
    }

    /**
     * Test for invalid capture device vendor.
     */
    @Test
    void isValidCaptureDeviceVendorOutOfRangeReturnsFalse() {
        assertFalse(validator.isValidCaptureDeviceVendor(-1));
        assertFalse(validator.isValidCaptureDeviceVendor(999999));
    }

    /**
     * Tests isValidCaptureDeviceType for valid and invalid combinations.
     */
    @Test
    void isValidCaptureDeviceTypeVariousCases() {
        // Valid: UNSPECIFIED device type with UNSPECIFIED vendor
        assertTrue(validator.isValidCaptureDeviceType(IrisCaptureDeviceType.UNSPECIFIED, IrisCaptureDeviceVendor.UNSPECIFIED));

        // Valid: Non-UNSPECIFIED device type with any vendor
        assertTrue(validator.isValidCaptureDeviceType(IrisCaptureDeviceType.VENDOR_FFFF, IrisCaptureDeviceVendor.UNSPECIFIED));
        assertTrue(validator.isValidCaptureDeviceType(IrisCaptureDeviceType.VENDOR_FFFF, IrisCaptureDeviceVendor.VENDOR_FFFF));

        // Invalid: UNSPECIFIED device type with non-UNSPECIFIED vendor
        assertFalse(validator.isValidCaptureDeviceType(IrisCaptureDeviceType.UNSPECIFIED, IrisCaptureDeviceVendor.VENDOR_FFFF));

        // Invalid: Out of range values
        assertFalse(validator.isValidCaptureDeviceType(-1, -1));
        assertFalse(validator.isValidCaptureDeviceType(999999, 999999));
    }

    /**
     * Tests isValidNoOfQualityBlocks for boundary values.
     */
    @Test
    void isValidNoOfQualityBlocksBoundaries() {
        assertTrue(validator.isValidNoOfQualityBlocks(0x00));
        assertTrue(validator.isValidNoOfQualityBlocks(0xFF));
        assertFalse(validator.isValidNoOfQualityBlocks(-1));
        assertFalse(validator.isValidNoOfQualityBlocks(0x100));
    }

    /**
     * Tests isValidQualityAlgorithmIdentifier for valid and invalid values.
     */
    @Test
    void isValidQualityAlgorithmIdentifierVariousCases() {
        assertTrue(validator.isValidQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.UNSPECIFIED));
        assertTrue(validator.isValidQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.VENDOR_FFFF));
        assertFalse(validator.isValidQualityAlgorithmIdentifier(-1));
        assertFalse(validator.isValidQualityAlgorithmIdentifier(999999));
    }

    /**
     * Tests isValidQualityAlgorithmVendorIdentifier for valid and invalid values.
     */
    @Test
    void isValidQualityAlgorithmVendorIdentifierVariousCases() {
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED));
        assertTrue(validator.isValidQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF));
        assertFalse(validator.isValidQualityAlgorithmVendorIdentifier(-1));
        assertFalse(validator.isValidQualityAlgorithmVendorIdentifier(999999));
    }

    /**
     * Tests isValidQualityScore for valid and invalid values.
     */
    @Test
    void isValidQualityScoreVariousCases() {
        assertTrue(validator.isValidQualityScore(0x00));
        assertTrue(validator.isValidQualityScore(0x64));
        assertTrue(validator.isValidQualityScore(0xFF));
        assertFalse(validator.isValidQualityScore(-1));
        assertFalse(validator.isValidQualityScore(0x65));
        assertFalse(validator.isValidQualityScore(0xFE));
    }

    /**
     * Tests isValidNoOfRepresentation for valid and invalid values.
     */
    @Test
    void isValidNoOfRepresentationVariousCases() {
        assertTrue(validator.isValidNoOfRepresentation(0x0001));
        assertFalse(validator.isValidNoOfRepresentation(0x0002));
        assertFalse(validator.isValidNoOfRepresentation(0x0000));
    }

    /**
     * Tests isValidEyeLabel for AUTH purpose with valid values.
     */
    @Test
    void isValidEyeLabelAuthPurposeValidCases() {
        assertTrue(validator.isValidEyeLabel(Purposes.AUTH.getCode(), EyeLabel.UNSPECIFIED));
        assertTrue(validator.isValidEyeLabel(Purposes.AUTH.getCode(), EyeLabel.RIGHT));
        assertTrue(validator.isValidEyeLabel(Purposes.AUTH.getCode(), EyeLabel.LEFT));
    }

    /**
     * Tests isValidEyeLabel for REGISTRATION purpose with valid values.
     */
    @Test
    void isValidEyeLabelRegistrationPurposeValidCases() {
        assertTrue(validator.isValidEyeLabel(Purposes.REGISTRATION.getCode(), EyeLabel.RIGHT));
        assertTrue(validator.isValidEyeLabel(Purposes.REGISTRATION.getCode(), EyeLabel.LEFT));
    }

    /**
     * Tests isValidEyeLabel for REGISTRATION purpose with invalid values.
     */
    @Test
    void isValidEyeLabelRegistrationPurposeInvalidCases() {
        assertFalse(validator.isValidEyeLabel(Purposes.REGISTRATION.getCode(), EyeLabel.UNSPECIFIED));
    }

    /**
     * Tests isValidEyeLabel with invalid purpose.
     */
    @Test
    void isValidEyeLabelInvalidPurpose() {
        assertFalse(validator.isValidEyeLabel("INVALID", EyeLabel.RIGHT));
    }

    /**
     * Tests isValidEyeLabel with exception scenario.
     */
    @Test
    void isValidEyeLabelExceptionScenario() {
        assertFalse(validator.isValidEyeLabel(null, EyeLabel.RIGHT));
    }

    /**
     * Tests isValidImageType for AUTH purpose.
     */
    @Test
    void isValidImageTypeAuthPurpose() {
        assertTrue(validator.isValidImageType(Purposes.AUTH.getCode(), ImageType.CROPPED_AND_MASKED));
        assertFalse(validator.isValidImageType(Purposes.AUTH.getCode(), ImageType.CROPPED));
    }

    /**
     * Tests isValidImageType for REGISTRATION purpose.
     */
    @Test
    void isValidImageTypeRegistrationPurpose() {
        assertTrue(validator.isValidImageType(Purposes.REGISTRATION.getCode(), ImageType.CROPPED));
        assertFalse(validator.isValidImageType(Purposes.REGISTRATION.getCode(), ImageType.CROPPED_AND_MASKED));
    }

    /**
     * Tests isValidImageType with invalid purpose.
     */
    @Test
    void isValidImageTypeInvalidPurpose() {
        assertFalse(validator.isValidImageType("INVALID", ImageType.CROPPED));
    }

    /**
     * Tests isValidImageType with exception scenario.
     */
    @Test
    void isValidImageTypeExceptionScenario() {
        assertFalse(validator.isValidImageType(null, ImageType.CROPPED));
    }

    /**
     * Tests isValidImageFromat for valid and invalid values.
     */
    @Test
    void isValidImageFromatVariousCases() {
        assertTrue(validator.isValidImageFromat(ImageFormat.MONO_JPEG2000));
        assertFalse(validator.isValidImageFromat(1234));
    }

    /**
     * Tests isValidImageHorizontalOrientation for valid and invalid values.
     */
    @Test
    void isValidImageHorizontalOrientationVariousCases() {
        assertTrue(validator.isValidImageHorizontalOrientation(HorizontalOrientation.ORIENTATION_UNDEFINIED));
        assertTrue(validator.isValidImageHorizontalOrientation(HorizontalOrientation.ORIENTATION_FLIPPED));
        assertFalse(validator.isValidImageHorizontalOrientation(-1));
        assertFalse(validator.isValidImageHorizontalOrientation(999));
    }

    /**
     * Tests isValidImageVerticalOrientation for valid and invalid values.
     */
    @Test
    void isValidImageVerticalOrientationVariousCases() {
        assertTrue(validator.isValidImageVerticalOrientation(VerticalOrientation.ORIENTATION_UNDEFINIED));
        assertTrue(validator.isValidImageVerticalOrientation(VerticalOrientation.ORIENTATION_FLIPPED));
        assertFalse(validator.isValidImageVerticalOrientation(-1));
        assertFalse(validator.isValidImageVerticalOrientation(999));
    }

    /**
     * Tests isValidImageCompressionType for AUTH purpose with valid lossy compression.
     */
    @Test
    void isValidImageCompressionTypeAuthPurposeLossy() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.isLossless()).thenReturn(false);

        assertTrue(validator.isValidImageCompressionType(Purposes.AUTH.getCode(),
                IrisImageCompressionType.JPEG_LOSSY, dto));
    }

    /**
     * Tests isValidImageCompressionType for AUTH purpose with invalid lossless compression.
     */
    @Test
    void isValidImageCompressionTypeAuthPurposeLossless() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.isLossless()).thenReturn(true);

        assertFalse(validator.isValidImageCompressionType(Purposes.AUTH.getCode(),
                IrisImageCompressionType.JPEG_LOSSY, dto));
    }

    /**
     * Tests isValidImageCompressionType for REGISTRATION purpose with valid lossless compression.
     */
    @Test
    void isValidImageCompressionTypeRegistrationPurposeLossless() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.isLossless()).thenReturn(true);

        assertTrue(validator.isValidImageCompressionType(Purposes.REGISTRATION.getCode(),
                IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE, dto));
    }

    /**
     * Tests isValidImageCompressionType for REGISTRATION purpose with invalid lossy compression.
     */
    @Test
    void isValidImageCompressionTypeRegistrationPurposeLossy() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.isLossless()).thenReturn(false);

        assertFalse(validator.isValidImageCompressionType(Purposes.REGISTRATION.getCode(),
                IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE, dto));
    }

    /**
     * Tests isValidImageCompressionType with invalid purpose.
     */
    @Test
    void isValidImageCompressionTypeInvalidPurpose() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        assertFalse(validator.isValidImageCompressionType("INVALID",
                IrisImageCompressionType.JPEG_LOSSY, dto));
    }

    /**
     * Tests isValidImageCompressionType with exception scenario.
     */
    @Test
    void isValidImageCompressionTypeExceptionScenario() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        assertFalse(validator.isValidImageCompressionType(null,
                IrisImageCompressionType.JPEG_LOSSY, dto));
    }

    /**
     * Tests isValidImageWidth for invalid width.
     */
    @Test
    void isValidImageWidthInvalidCases() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.getWidth()).thenReturn(640);

        assertFalse(validator.isValidImageWidth(Purposes.AUTH.getCode(), 480, dto));
        assertFalse(validator.isValidImageWidth(Purposes.AUTH.getCode(), 0x0000, dto));
        assertFalse(validator.isValidImageWidth(Purposes.AUTH.getCode(), 0x10000, dto));
    }

    /**
     * Tests isValidImageHeight for invalid height.
     */
    @Test
    void isValidImageHeightInvalidCases() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.getHeight()).thenReturn(480);

        assertFalse(validator.isValidImageHeight(Purposes.AUTH.getCode(), 640, dto));
        assertFalse(validator.isValidImageHeight(Purposes.AUTH.getCode(), 0x0000, dto));
        assertFalse(validator.isValidImageHeight(Purposes.AUTH.getCode(), 0x10000, dto));
    }


    /**
     * Tests isValidBitDepth(int, ImageDecoderRequestDto) for valid and invalid values.
     */
    @Test
    void isValidBitDepthWithDtoVariousCases() {
        ImageDecoderRequestDto dto = Mockito.mock(ImageDecoderRequestDto.class);
        Mockito.when(dto.getDepth()).thenReturn(IrisImageBitDepth.BPP_08);
        assertTrue(validator.isValidBitDepth(IrisImageBitDepth.BPP_08, dto));

        Mockito.when(dto.getDepth()).thenReturn(16);
        assertFalse(validator.isValidBitDepth(IrisImageBitDepth.BPP_08, dto));

        assertFalse(validator.isValidBitDepth(16, dto));
    }

    /**
     * Tests isValidRange for valid and invalid values.
     */
    @Test
    void isValidRangeVariousCases() {
        assertTrue(validator.isValidRange(IrisRange.UNASSIGNED));
        assertTrue(validator.isValidRange(IrisRange.OVERFLOW_FFFF));
        assertFalse(validator.isValidRange(-1));
        assertFalse(validator.isValidRange(999999));
    }

    /**
     * Tests isValidRollAngleOfEye for valid and invalid values.
     */
    @Test
    void isValidRollAngleOfEyeVariousCases() {
        assertTrue(validator.isValidRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000));
        assertTrue(validator.isValidRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED));
        assertFalse(validator.isValidRollAngleOfEye(-1));
        assertFalse(validator.isValidRollAngleOfEye(999999));
    }

    /**
     * Tests isValidRollAngleUncertainty for valid and invalid values.
     */
    @Test
    void isValidRollAngleUncertaintyVariousCases() {
        assertTrue(validator.isValidRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000));
        assertTrue(validator.isValidRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED));
        assertFalse(validator.isValidRollAngleUncertainty(-1));
        assertFalse(validator.isValidRollAngleUncertainty(999999));
    }

    /**
     * Tests isValidIrisCenterSmallestX for valid and invalid values.
     */
    @Test
    void isValidIrisCenterSmallestXVariousCases() {
        assertTrue(validator.isValidIrisCenterSmallestX(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterSmallestX(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterSmallestX(-1));
        assertFalse(validator.isValidIrisCenterSmallestX(999999));
    }

    /**
     * Tests isValidIrisCenterLargestX for valid and invalid values.
     */
    @Test
    void isValidIrisCenterLargestXVariousCases() {
        assertTrue(validator.isValidIrisCenterLargestX(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterLargestX(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterLargestX(-1));
        assertFalse(validator.isValidIrisCenterLargestX(999999));
    }

    /**
     * Tests isValidIrisCenterSmallestY for valid and invalid values.
     */
    @Test
    void isValidIrisCenterSmallestYVariousCases() {
        assertTrue(validator.isValidIrisCenterSmallestY(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterSmallestY(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterSmallestY(-1));
        assertFalse(validator.isValidIrisCenterSmallestY(999999));
    }

    /**
     * Tests isValidIrisCenterLargestY for valid and invalid values.
     */
    @Test
    void isValidIrisCenterLargestYVariousCases() {
        assertTrue(validator.isValidIrisCenterLargestY(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisCenterLargestY(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisCenterLargestY(-1));
        assertFalse(validator.isValidIrisCenterLargestY(999999));
    }

    /**
     * Tests isValidIrisDiameterSmallest for valid and invalid values.
     */
    @Test
    void isValidIrisDiameterSmallestVariousCases() {
        assertTrue(validator.isValidIrisDiameterSmallest(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisDiameterSmallest(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisDiameterSmallest(-1));
        assertFalse(validator.isValidIrisDiameterSmallest(999999));
    }

    /**
     * Tests isValidIrisDiameterLargest for valid and invalid values.
     */
    @Test
    void isValidIrisDiameterLargestVariousCases() {
        assertTrue(validator.isValidIrisDiameterLargest(IrisCoordinate.COORDINATE_UNDEFINIED));
        assertTrue(validator.isValidIrisDiameterLargest(IrisCoordinate.COORDINATE_FFFF));
        assertFalse(validator.isValidIrisDiameterLargest(-1));
        assertFalse(validator.isValidIrisDiameterLargest(999999));
    }
}
