package io.mosip.biometrics.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Comprehensive unit tests for {@link ISOStandardsValidator}
 * Tests all methods including isValidCaptureDateTime, isValidImageData, getBioDataType,
 * and various image validation methods with proper exception handling.
 */
class ISOStandardsValidatorTest {

    static class TestValidator extends ISOStandardsValidator {
    }

    private TestValidator validator = new TestValidator();

    /**
     * Tests isValidCaptureDateTime with a valid full date and time configuration.
     */
    @Test
    void isValidCaptureDateTimeValidFullDateTime() {
        assertTrue(validator.isValidCaptureDateTime(2023, 5, 10, 12, 30, 45, 500));
    }

    /**
     * Tests isValidCaptureDateTime with invalid month and day combination.
     */
    @Test
    void isValidCaptureDateTimeInvalidMonthDay() {
        assertFalse(validator.isValidCaptureDateTime(2023, 13, 32, 12, 30, 45, 500));
    }

    /**
     * Tests isValidCaptureDateTime when only date fields are provided with time fields set to 0xFF.
     */
    @Test
    void isValidCaptureDateTimeDateOnlyFieldsProvided() {
        assertTrue(validator.isValidCaptureDateTime(2023, 5, 10, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /**
     * Tests isValidCaptureDateTime with year set to zero, which should be invalid.
     */
    @Test
    void isValidCaptureDateTimeYearZero() {
        assertFalse(validator.isValidCaptureDateTime(0, 5, 10, 12, 30, 45, 500));
    }

    /**
     * Tests isValidCaptureDateTime when all fields are set to unprovided values (0xFF/0xFFFF).
     */
    @Test
    void isValidCaptureDateTimeAllFieldsUnprovided() {
        assertTrue(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /**
     * Tests isValidCaptureDateTime when only time fields are provided with date fields set to 0xFF.
     */
    @Test
    void isValidCaptureDateTimeTimeOnlyFieldsProvided() {
        assertTrue(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 250));
    }

    /**
     * Tests isValidCaptureDateTime with time-only configuration but invalid hour value.
     */
    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidHour() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 25, 15, 30, 250));
    }

    /**
     * Tests isValidCaptureDateTime with time-only configuration but invalid milliseconds value.
     */
    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidMilliseconds() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 4000));
    }

    /**
     * Tests isValidCaptureDateTime with minimum valid values for all fields.
     */
    @Test
    void isValidCaptureDateTimeMinimumValues() {
        assertTrue(validator.isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with day set to zero, which should be invalid.
     */
    @Test
    void isValidCaptureDateTimeDayZero() {
        assertFalse(validator.isValidCaptureDateTime(2023, 1, 0, 0, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with February 29th on a leap year.
     */
    @Test
    void isValidCaptureDateTimeLeapYearFeb29() {
        assertTrue(validator.isValidCaptureDateTime(2024, 2, 29, 10, 10, 10, 10));
    }

    /**
     * Tests isValidCaptureDateTime with maximum valid values for all fields.
     */
    @Test
    void isValidCaptureDateTimeMaximumValues() {
        assertTrue(validator.isValidCaptureDateTime(9999, 12, 31, 23, 59, 59, 999));
    }

    /**
     * Tests isValidCaptureDateTime with hour value at invalid boundary (24).
     */
    @Test
    void isValidCaptureDateTimeInvalidHourBoundary() {
        assertFalse(validator.isValidCaptureDateTime(2023, 6, 15, 24, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with all negative values, which should be invalid.
     */
    @Test
    void isValidCaptureDateTimeNegativeValues() {
        assertFalse(validator.isValidCaptureDateTime(-1, -1, -1, -1, -1, -1, -1));
    }

    /**
     * Tests isValidCaptureDateTime with time-only configuration at upper valid limits.
     */
    @Test
    void isValidCaptureDateTimeTimeOnlyUpperLimits() {
        assertTrue(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 23, 59, 59, 999));
    }

    /**
     * Tests isValidCaptureDateTime with partially invalid time values.
     */
    @Test
    void isValidCaptureDateTimePartialInvalidCombination() {
        assertFalse(validator.isValidCaptureDateTime(2023, 5, 10, 25, 60, 60, 1000));
    }

    /**
     * Tests isValidCaptureDateTime when only year is provided with other fields set to 0xFF.
     */
    @Test
    void isValidCaptureDateTimeOnlyYearProvided() {
        assertFalse(validator.isValidCaptureDateTime(2023, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /**
     * Tests isValidCaptureDateTime with all fields set to zero, which should be invalid.
     */
    @Test
    void isValidCaptureDateTimeAllZeros() {
        assertFalse(validator.isValidCaptureDateTime(0, 0, 0, 0, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with valid April 30th date.
     */
    @Test
    void isValidCaptureDateTimeValidApril30() {
        assertTrue(validator.isValidCaptureDateTime(2023, 4, 30, 12, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with valid February 28th on non-leap year.
     */
    @Test
    void isValidCaptureDateTimeValidFeb28() {
        assertTrue(validator.isValidCaptureDateTime(2023, 2, 28, 0, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with valid end-of-year date and time.
     */
    @Test
    void isValidCaptureDateTimeValidYearEnd() {
        assertTrue(validator.isValidCaptureDateTime(2023, 12, 31, 23, 59, 59, 999));
    }

    /**
     * Tests isValidCaptureDateTime with valid mid-year date and time.
     */
    @Test
    void isValidCaptureDateTimeValidMidYear() {
        assertTrue(validator.isValidCaptureDateTime(2023, 6, 15, 6, 30, 30, 300));
    }

    /**
     * Tests isValidCaptureDateTime with minimum valid year value.
     */
    @Test
    void isValidCaptureDateTimeLowYear() {
        assertTrue(validator.isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }

    /**
     * Tests isValidCaptureDateTime with time-only fields but invalid minute value.
     */
    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidMinute() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 10, 60, 30, 250));
    }

    /**
     * Tests isValidCaptureDateTime with time-only fields but invalid second value.
     */
    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidSecond() {
        assertFalse(validator.isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 10, 30, 60, 250));
    }

    /**
     * Tests isValidImageData for AUTH purpose with JP2000 image type, expecting exception.
     */
    @Test
    void isValidImageDataAuthWithJP2000() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");

        assertThrows(Exception.class, () ->
                validator.isValidImageData("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageData for AUTH purpose with WSQ image type, expecting exception.
     */
    @Test
    void isValidImageDataAuthWithWSQ() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");

        assertThrows(Exception.class, () ->
                validator.isValidImageData("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageData for AUTH purpose with invalid image type, expecting exception.
     */
    @Test
    void isValidImageDataAuthWithInvalidType() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("PNG");

        assertThrows(Exception.class, () ->
                validator.isValidImageData("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageData for REGISTRATION purpose with JP2000 image type, expecting exception.
     */
    @Test
    void isValidImageDataRegistrationWithJP2000() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");

        assertThrows(Exception.class, () ->
                validator.isValidImageData("REGISTRATION", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageData for REGISTRATION purpose with WSQ image type, expecting exception.
     */
    @Test
    void isValidImageDataRegistrationWithWSQ() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");

        assertThrows(Exception.class, () ->
                validator.isValidImageData("REGISTRATION", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageData with invalid purpose parameter, expecting exception.
     */
    @Test
    void isValidImageDataWithInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");

        assertThrows(Exception.class, () ->
                validator.isValidImageData("INVALID", Modality.Finger, dto));
    }

    /**
     * Tests getBioDataType for Finger modality with AUTH purpose and JP2000 format, expecting exception.
     */
    @Test
    void getBioDataTypeFingerAuthJP2000() {
        byte[] jp2000Data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x6a, 0x70, 0x32, 0x68,
                0x0d, 0x0a, (byte)0x87, 0x0a, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("AUTH", Modality.Finger, jp2000Data));
    }

    /**
     * Tests getBioDataType for Finger modality with AUTH purpose and WSQ format, expecting exception.
     */
    @Test
    void getBioDataTypeFingerAuthWSQ() {
        byte[] wsqData = new byte[] {
                (byte)0xff, (byte)0xa0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("AUTH", Modality.Finger, wsqData));
    }

    /**
     * Tests getBioDataType for Finger modality with REGISTRATION purpose and JP2000 format, expecting exception.
     */
    @Test
    void getBioDataTypeFingerRegistrationJP2000() {
        byte[] jp2000Data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x6a, 0x70, 0x32, 0x68,
                0x0d, 0x0a, (byte)0x87, 0x0a, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("REGISTRATION", Modality.Finger, jp2000Data));
    }

    /**
     * Tests getBioDataType for Finger modality with REGISTRATION purpose and WSQ format, expecting exception.
     */
    @Test
    void getBioDataTypeFingerRegistrationWSQ() {
        byte[] wsqData = new byte[] {
                (byte)0xff, (byte)0xa0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("REGISTRATION", Modality.Finger, wsqData));
    }

    /**
     * Tests getBioDataType for Iris modality with AUTH purpose, expecting exception.
     */
    @Test
    void getBioDataTypeIrisAuth() {
        byte[] jp2000Data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x6a, 0x70, 0x32, 0x68,
                0x0d, 0x0a, (byte)0x87, 0x0a, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("AUTH", Modality.Iris, jp2000Data));
    }

    /**
     * Tests getBioDataType for Face modality with AUTH purpose, expecting exception.
     */
    @Test
    void getBioDataTypeFaceAuth() {
        byte[] jp2000Data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x6a, 0x70, 0x32, 0x68,
                0x0d, 0x0a, (byte)0x87, 0x0a, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("AUTH", Modality.Face, jp2000Data));
    }

    /**
     * Tests getBioDataType with UnSpecified modality, should return -1 without throwing exception.
     */
    @Test
    void getBioDataTypeUnspecifiedModality() throws Exception {
        byte[] data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        int result = validator.getBioDataType("AUTH", Modality.UnSpecified, data);
        assertEquals(-1, result);
    }

    /**
     * Tests getBioDataType with invalid purpose parameter, expecting exception.
     */
    @Test
    void getBioDataTypeInvalidPurpose() {
        byte[] data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("INVALID", Modality.Finger, data));
    }

    /**
     * Tests getBioDataType with unknown image format, expecting exception.
     */
    @Test
    void getBioDataTypeUnknownFormat() {
        byte[] unknownData = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        assertThrows(Exception.class, () ->
                validator.getBioDataType("AUTH", Modality.Finger, unknownData));
    }

    /**
     * Tests isWSQ with ImageDecoderRequestDto when auth is true and image type is WSQ.
     */
    @Test
    void isWSQWithDtoAuthTrue() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");

        assertTrue(validator.isWSQ(true, dto));
    }

    /**
     * Tests isWSQ with ImageDecoderRequestDto when auth is false but image type is WSQ.
     */
    @Test
    void isWSQWithDtoAuthFalse() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");

        assertFalse(validator.isWSQ(false, dto));
    }

    /**
     * Tests isWSQ with ImageDecoderRequestDto when image type is not WSQ.
     */
    @Test
    void isWSQWithDtoNotWSQ() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");

        assertFalse(validator.isWSQ(true, dto));
    }

    /**
     * Tests isJP2000 with ImageDecoderRequestDto when image type is JP2000.
     */
    @Test
    void isJP2000WithDto() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("JP2000");

        assertTrue(validator.isJP2000(true, dto));
        assertTrue(validator.isJP2000(false, dto));
    }

    /**
     * Tests isJP2000 with ImageDecoderRequestDto when image type is not JP2000.
     */
    @Test
    void isJP2000WithDtoNotJP2000() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageType()).thenReturn("WSQ");

        assertFalse(validator.isJP2000(true, dto));
        assertFalse(validator.isJP2000(false, dto));
    }

    /**
     * Tests isJP2000 with byte array containing proper JP2000 signature.
     */
    @Test
    void isJP2000WithValidSignature() throws Exception {
        byte[] jp2000Data = new byte[] {
                0x00, 0x00, 0x00, 0x0c, 0x6a, 0x70, 0x32, 0x68,
                0x0d, 0x0a, (byte)0x87, 0x0a, 0x00, 0x00, 0x00, 0x00
        };

        assertTrue(validator.isJP2000(jp2000Data));
    }

    /**
     * Tests isJP2000 with byte array containing invalid signature.
     */
    @Test
    void isJP2000WithInvalidSignature() throws Exception {
        byte[] invalidData = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        assertFalse(validator.isJP2000(invalidData));
    }

    /**
     * Tests isWSQ with byte array containing valid WSQ signature.
     */
    @Test
    void isWSQByteArrayValid() throws Exception {
        byte[] wsqData = new byte[] {
                (byte)0xff, (byte)0xa0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        assertTrue(validator.isWSQ(wsqData));
    }

    /**
     * Tests isWSQ with byte array containing invalid signature.
     */
    @Test
    void isWSQByteArrayInvalid() throws Exception {
        byte[] invalidData = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        assertFalse(validator.isWSQ(invalidData));
    }

    /**
     * Tests isValidImageDataLength with matching image data length.
     */
    @Test
    void isValidImageDataLengthValid() {
        byte[] data = new byte[] {0x01, 0x02, 0x03};

        assertTrue(validator.isValidImageDataLength(data, 3));
    }

    /**
     * Tests isValidImageDataLength with non-matching image data length.
     */
    @Test
    void isValidImageDataLengthInvalid() {
        byte[] data = new byte[] {0x01, 0x02, 0x03};

        assertFalse(validator.isValidImageDataLength(data, 5));
    }

    /**
     * Tests isValidImageDataLength with null image data.
     */
    @Test
    void isValidImageDataLengthNull() {
        assertFalse(validator.isValidImageDataLength(null, 5));
    }

    /**
     * Tests isValidImageCompressionRatio for AUTH purpose with valid compression ratio, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioAuthValid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("10:1");

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageCompressionRatio for AUTH purpose with invalid compression ratio, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("20:1");

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageCompressionRatio for AUTH purpose with null compression ratio, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioAuthNull() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn(null);

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageCompressionRatio for AUTH purpose with empty compression ratio, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioAuthEmpty() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("");

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageCompressionRatio for AUTH purpose with blank compression ratio, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioAuthBlank() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageCompressionRatio()).thenReturn("   ");

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageCompressionRatio for REGISTRATION purpose, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioRegistration() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("REGISTRATION", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageCompressionRatio with invalid purpose parameter, expecting exception.
     */
    @Test
    void isValidImageCompressionRatioInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);

        assertThrows(Exception.class, () ->
                validator.isValidImageCompressionRatio("INVALID", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageAspectRatio for AUTH purpose with valid 1:1 aspect ratio, expecting exception.
     */
    @Test
    void isValidImageAspectRatioAuthValid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageAspectRatio()).thenReturn("1:1");

        assertThrows(Exception.class, () ->
                validator.isValidImageAspectRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageAspectRatio for AUTH purpose with invalid aspect ratio, expecting exception.
     */
    @Test
    void isValidImageAspectRatioAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageAspectRatio()).thenReturn("2:1");

        assertThrows(Exception.class, () ->
                validator.isValidImageAspectRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageAspectRatio for AUTH purpose with null aspect ratio, expecting exception.
     */
    @Test
    void isValidImageAspectRatioAuthNull() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageAspectRatio()).thenReturn(null);

        assertThrows(Exception.class, () ->
                validator.isValidImageAspectRatio("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageAspectRatio for REGISTRATION purpose, expecting exception.
     */
    @Test
    void isValidImageAspectRatioRegistration() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);

        assertThrows(Exception.class, () ->
                validator.isValidImageAspectRatio("REGISTRATION", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageAspectRatio with invalid purpose parameter, expecting exception.
     */
    @Test
    void isValidImageAspectRatioInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);

        assertThrows(Exception.class, () ->
                validator.isValidImageAspectRatio("INVALID", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageColorSpace for Finger modality with AUTH purpose and GRAY color space, expecting exception.
     */
    @Test
    void isValidImageColorSpaceFingerAuthGray() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageColorSpace for Finger modality with AUTH purpose and invalid RGB color space, expecting exception.
     */
    @Test
    void isValidImageColorSpaceFingerAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("RGB");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageColorSpace for Iris modality with AUTH purpose and GRAY color space, expecting exception.
     */
    @Test
    void isValidImageColorSpaceIrisAuthGray() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("AUTH", Modality.Iris, dto));
    }

    /**
     * Tests isValidImageColorSpace for Face modality with AUTH purpose and RGB color space, expecting exception.
     */
    @Test
    void isValidImageColorSpaceFaceAuthRGB() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("RGB");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("AUTH", Modality.Face, dto));
    }

    /**
     * Tests isValidImageColorSpace for Face modality with AUTH purpose and invalid GRAY color space, expecting exception.
     */
    @Test
    void isValidImageColorSpaceFaceAuthInvalid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("AUTH", Modality.Face, dto));
    }

    /**
     * Tests isValidImageColorSpace for REGISTRATION purpose with Finger modality, expecting exception.
     */
    @Test
    void isValidImageColorSpaceRegistrationFinger() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("REGISTRATION", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageColorSpace with invalid purpose parameter, expecting exception.
     */
    @Test
    void isValidImageColorSpaceInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getImageColorSpace()).thenReturn("GRAY");

        assertThrows(Exception.class, () ->
                validator.isValidImageColorSpace("INVALID", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageDPI for Finger modality with AUTH purpose and valid DPI values, expecting exception.
     */
    @Test
    void isValidImageDPIFingerAuthValid() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(500);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageDPI for Finger modality with AUTH purpose and invalid horizontal DPI, expecting exception.
     */
    @Test
    void isValidImageDPIFingerAuthInvalidHorizontal() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(400);
        when(dto.getVerticalDPI()).thenReturn(500);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageDPI for Finger modality with AUTH purpose and invalid vertical DPI, expecting exception.
     */
    @Test
    void isValidImageDPIFingerAuthInvalidVertical() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(1100);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("AUTH", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageDPI for Iris modality with AUTH purpose, expecting exception.
     */
    @Test
    void isValidImageDPIIrisAuth() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(100);
        when(dto.getVerticalDPI()).thenReturn(200);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("AUTH", Modality.Iris, dto));
    }

    /**
     * Tests isValidImageDPI for Face modality with AUTH purpose, expecting exception.
     */
    @Test
    void isValidImageDPIFaceAuth() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(100);
        when(dto.getVerticalDPI()).thenReturn(200);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("AUTH", Modality.Face, dto));
    }

    /**
     * Tests isValidImageDPI for REGISTRATION purpose with Finger modality, expecting exception.
     */
    @Test
    void isValidImageDPIRegistrationFinger() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(500);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("REGISTRATION", Modality.Finger, dto));
    }

    /**
     * Tests isValidImageDPI with invalid purpose parameter, expecting exception.
     */
    @Test
    void isValidImageDPIInvalidPurpose() {
        ImageDecoderRequestDto dto = mock(ImageDecoderRequestDto.class);
        when(dto.getHorizontalDPI()).thenReturn(500);
        when(dto.getVerticalDPI()).thenReturn(500);

        assertThrows(Exception.class, () ->
                validator.isValidImageDPI("INVALID", Modality.Finger, dto));
    }
}
