package io.mosip.biometrics.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ISOStandardsValidator}.
 * Purpose codes must be {@code Auth} / {@code Registration} ({@link Purposes#fromCode}).
 */
class ISOStandardsValidatorTest {

    static class TestValidator extends ISOStandardsValidator {
    }

    private final TestValidator validator = new TestValidator();

    private static final byte[] JP2000_SIG = new byte[] {
            0x00, 0x00, 0x00, 0x0c, 0x6a, 0x70, 0x32, 0x68,
            0x0d, 0x0a, (byte) 0x87, 0x0a, 0x00, 0x00, 0x00, 0x00
    };

    private static final byte[] WSQ_SIG = new byte[] {
            (byte) 0xff, (byte) 0xa0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    @Test
    void isValidCaptureDateTimeValidFullDateTime() {
        assertTrue(validator.isValidCaptureDateTime(2023, 5, 10, 12, 30, 45, 500));
    }

    @Test
    void isValidCaptureDateTimeInvalidMonthDay() {
        assertFalse(validator.isValidCaptureDateTime(2023, 13, 32, 12, 30, 45, 500));
    }

    @Test
    void isValidCaptureDateTimeDateOnlyFieldsProvided() {
        assertTrue(validator.isValidCaptureDateTime(2023, 5, 10, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    @Test
    void isValidCaptureDateTimeYearZero() {
        assertFalse(validator.isValidCaptureDateTime(0, 5, 10, 12, 30, 45, 500));
    }

    @Test
    void isValidCaptureDateTimeAllFieldsUnprovided() {
        assertTrue(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    @Test
    void isValidCaptureDateTimeTimeOnlyFieldsProvided() {
        assertTrue(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 250));
    }

    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidHour() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 25, 15, 30, 250));
    }

    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidMilliseconds() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 4000));
    }

    @Test
    void isValidCaptureDateTimeMinimumValues() {
        assertTrue(validator.isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeDayZero() {
        assertFalse(validator.isValidCaptureDateTime(2023, 1, 0, 0, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeLeapYearFeb29() {
        assertTrue(validator.isValidCaptureDateTime(2024, 2, 29, 10, 10, 10, 10));
    }

    @Test
    void isValidCaptureDateTimeMaximumValues() {
        assertTrue(validator.isValidCaptureDateTime(9999, 12, 31, 23, 59, 59, 999));
    }

    @Test
    void isValidCaptureDateTimeInvalidHourBoundary() {
        assertFalse(validator.isValidCaptureDateTime(2023, 6, 15, 24, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeNegativeValues() {
        assertFalse(validator.isValidCaptureDateTime(-1, -1, -1, -1, -1, -1, -1));
    }

    @Test
    void isValidCaptureDateTimeTimeOnlyUpperLimits() {
        assertTrue(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 23, 59, 59, 999));
    }

    @Test
    void isValidCaptureDateTimePartialInvalidCombination() {
        assertFalse(validator.isValidCaptureDateTime(2023, 5, 10, 25, 60, 60, 1000));
    }

    @Test
    void isValidCaptureDateTimeOnlyYearProvided() {
        assertFalse(validator.isValidCaptureDateTime(2023, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    @Test
    void isValidCaptureDateTimeAllZeros() {
        assertFalse(validator.isValidCaptureDateTime(0, 0, 0, 0, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeValidApril30() {
        assertTrue(validator.isValidCaptureDateTime(2023, 4, 30, 12, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeValidFeb28() {
        assertTrue(validator.isValidCaptureDateTime(2023, 2, 28, 0, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeValidYearEnd() {
        assertTrue(validator.isValidCaptureDateTime(2023, 12, 31, 23, 59, 59, 999));
    }

    @Test
    void isValidCaptureDateTimeValidMidYear() {
        assertTrue(validator.isValidCaptureDateTime(2023, 6, 15, 6, 30, 30, 300));
    }

    @Test
    void isValidCaptureDateTimeLowYear() {
        assertTrue(validator.isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }

    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidMinute() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 10, 60, 30, 250));
    }

    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidSecond() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 10, 30, 60, 250));
    }

    @Test
    void isValidImageDataAuthWithJP2000() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");
        assertTrue(validator.isValidImageData("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageDataAuthWithWSQ() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");
        assertTrue(validator.isValidImageData("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageDataAuthWithInvalidType() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("PNG");
        assertFalse(validator.isValidImageData("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageDataRegistrationWithJP2000() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");
        assertTrue(validator.isValidImageData("Registration", Modality.Finger, dto));
    }

    @Test
    void isValidImageDataRegistrationWithWSQ() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");
        assertFalse(validator.isValidImageData("Registration", Modality.Finger, dto));
    }

    @Test
    void isValidImageDataWithInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");
        assertThrows(Exception.class, () ->
                validator.isValidImageData("INVALID", Modality.Finger, dto));
    }

    @Test
    void getBioDataTypeFingerAuthJP2000() throws Exception {
        assertEquals(ImageType.JPEG2000.value(),
                validator.getBioDataType("Auth", Modality.Finger, JP2000_SIG));
    }

    @Test
    void getBioDataTypeFingerAuthWSQ() throws Exception {
        assertEquals(ImageType.WSQ.value(),
                validator.getBioDataType("Auth", Modality.Finger, WSQ_SIG));
    }

    @Test
    void getBioDataTypeFingerRegistrationJP2000() throws Exception {
        assertEquals(ImageType.JPEG2000.value(),
                validator.getBioDataType("Registration", Modality.Finger, JP2000_SIG));
    }

    @Test
    void getBioDataTypeFingerRegistrationWSQ() throws Exception {
        assertEquals(-1, validator.getBioDataType("Registration", Modality.Finger, WSQ_SIG));
    }

    @Test
    void getBioDataTypeIrisAuth() throws Exception {
        assertEquals(ImageType.JPEG2000.value(),
                validator.getBioDataType("Auth", Modality.Iris, JP2000_SIG));
    }

    @Test
    void getBioDataTypeFaceAuth() throws Exception {
        assertEquals(ImageType.JPEG2000.value(),
                validator.getBioDataType("Auth", Modality.Face, JP2000_SIG));
    }

    @Test
    void getBioDataTypeUnspecifiedModality() throws Exception {
        assertEquals(-1, validator.getBioDataType("Auth", Modality.UnSpecified, JP2000_SIG));
    }

    @Test
    void getBioDataTypeInvalidPurpose() {
        assertThrows(Exception.class, () ->
                validator.getBioDataType("INVALID", Modality.Finger, JP2000_SIG));
    }

    @Test
    void getBioDataTypeUnknownFormat() throws Exception {
        byte[] unknownData = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };
        assertEquals(-1, validator.getBioDataType("Auth", Modality.Finger, unknownData));
    }

    @Test
    void isWSQWithDtoAuthTrue() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");
        assertTrue(validator.isWSQ(true, dto));
    }

    @Test
    void isWSQWithDtoAuthFalse() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");
        assertFalse(validator.isWSQ(false, dto));
    }

    @Test
    void isWSQWithDtoNotWSQ() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");
        assertFalse(validator.isWSQ(true, dto));
    }

    @Test
    void isJP2000WithDto() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");
        assertTrue(validator.isJP2000(true, dto));
        assertTrue(validator.isJP2000(false, dto));
    }

    @Test
    void isJP2000WithDtoNotJP2000() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");
        assertFalse(validator.isJP2000(true, dto));
        assertFalse(validator.isJP2000(false, dto));
    }

    @Test
    void isJP2000WithValidSignature() throws Exception {
        assertTrue(validator.isJP2000(JP2000_SIG));
    }

    @Test
    void isJP2000WithInvalidSignature() throws Exception {
        byte[] invalidData = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };
        assertFalse(validator.isJP2000(invalidData));
    }

    @Test
    void isWSQByteArrayValid() throws Exception {
        assertTrue(validator.isWSQ(WSQ_SIG));
    }

    @Test
    void isWSQByteArrayInvalid() throws Exception {
        byte[] invalidData = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };
        assertFalse(validator.isWSQ(invalidData));
    }

    @Test
    void isValidImageDataLengthValid() {
        assertTrue(validator.isValidImageDataLength(new byte[] {0x01, 0x02, 0x03}, 3));
    }

    @Test
    void isValidImageDataLengthInvalid() {
        assertFalse(validator.isValidImageDataLength(new byte[] {0x01, 0x02, 0x03}, 5));
    }

    @Test
    void isValidImageDataLengthNull() {
        assertFalse(validator.isValidImageDataLength(null, 5));
    }

    @Test
    void isValidImageCompressionRatioAuthValid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("10:1");
        assertTrue(validator.isValidImageCompressionRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageCompressionRatioAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("20:1");
        assertFalse(validator.isValidImageCompressionRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageCompressionRatioAuthNull() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn(null);
        assertFalse(validator.isValidImageCompressionRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageCompressionRatioAuthEmpty() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("");
        assertFalse(validator.isValidImageCompressionRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageCompressionRatioAuthBlank() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("   ");
        assertFalse(validator.isValidImageCompressionRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageCompressionRatioRegistration() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        assertTrue(validator.isValidImageCompressionRatio("Registration", Modality.Finger, dto));
    }

    @Test
    void isValidImageCompressionRatioInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("INVALID", Modality.Finger, dto));
    }

    @Test
    void isValidImageAspectRatioAuthValid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageAspectRatio()).thenReturn("1:1");
        assertTrue(validator.isValidImageAspectRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageAspectRatioAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageAspectRatio()).thenReturn("2:1");
        assertFalse(validator.isValidImageAspectRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageAspectRatioAuthNull() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageAspectRatio()).thenReturn(null);
        assertFalse(validator.isValidImageAspectRatio("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageAspectRatioRegistration() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        assertTrue(validator.isValidImageAspectRatio("Registration", Modality.Finger, dto));
    }

    @Test
    void isValidImageAspectRatioInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        assertThrows(Exception.class, () ->
                validator.isValidImageAspectRatio("INVALID", Modality.Finger, dto));
    }

    @Test
    void isValidImageColorSpaceFingerAuthGray() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");
        assertTrue(validator.isValidImageColorSpace("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageColorSpaceFingerAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("RGB");
        assertFalse(validator.isValidImageColorSpace("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageColorSpaceIrisAuthGray() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");
        assertTrue(validator.isValidImageColorSpace("Auth", Modality.Iris, dto));
    }

    @Test
    void isValidImageColorSpaceFaceAuthRGB() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("RGB");
        assertTrue(validator.isValidImageColorSpace("Auth", Modality.Face, dto));
    }

    @Test
    void isValidImageColorSpaceFaceAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");
        assertFalse(validator.isValidImageColorSpace("Auth", Modality.Face, dto));
    }

    @Test
    void isValidImageColorSpaceRegistrationFinger() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");
        assertTrue(validator.isValidImageColorSpace("Registration", Modality.Finger, dto));
    }

    @Test
    void isValidImageColorSpaceRegistrationIrisFace() {
        ImageDecoderRequestDto iris = mock(ImageDecoderRequestDto.class);
        when(iris.getImageColorSpace()).thenReturn("GRAY");
        assertTrue(validator.isValidImageColorSpace("Registration", Modality.Iris, iris));

        ImageDecoderRequestDto face = mock(ImageDecoderRequestDto.class);
        when(face.getImageColorSpace()).thenReturn("RGB");
        assertTrue(validator.isValidImageColorSpace("Registration", Modality.Face, face));
    }

    @Test
    void isValidImageColorSpaceInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");
        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("INVALID", Modality.Finger, dto));
    }

    @Test
    void isValidImageDPIFingerAuthValid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(500);
        assertTrue(validator.isValidImageDPI("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageDPIFingerAuthInvalidHorizontal() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(400);
        when(dto.getVerticalDPI()).thenReturn(500);
        assertFalse(validator.isValidImageDPI("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageDPIFingerAuthInvalidVertical() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(1100);
        assertFalse(validator.isValidImageDPI("Auth", Modality.Finger, dto));
    }

    @Test
    void isValidImageDPIIrisAuth() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(100);
        when(dto.getVerticalDPI()).thenReturn(200);
        assertTrue(validator.isValidImageDPI("Auth", Modality.Iris, dto));
    }

    @Test
    void isValidImageDPIFaceAuth() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(100);
        when(dto.getVerticalDPI()).thenReturn(200);
        assertTrue(validator.isValidImageDPI("Auth", Modality.Face, dto));
    }

    @Test
    void isValidImageDPIRegistrationFinger() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(500);
        assertTrue(validator.isValidImageDPI("Registration", Modality.Finger, dto));
    }

    @Test
    void isValidImageDPIRegistrationIrisFace() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(100);
        when(dto.getVerticalDPI()).thenReturn(100);
        assertTrue(validator.isValidImageDPI("Registration", Modality.Iris, dto));
        assertTrue(validator.isValidImageDPI("Registration", Modality.Face, dto));
    }

    @Test
    void isValidImageDPIInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(500);
        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("INVALID", Modality.Finger, dto));
    }
}
