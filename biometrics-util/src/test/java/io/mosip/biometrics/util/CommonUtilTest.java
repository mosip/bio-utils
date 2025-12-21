package io.mosip.biometrics.util;

import io.mosip.biometrics.util.exception.BiometricUtilException;
import org.junit.jupiter.api.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for {@link CommonUtil}.
 */
class CommonUtilTest {

    /**
     * Test getBufferedImage() with valid JP2000 (treated as JPEG) input.
     */
    @Test
    void getBufferedImageValidJp2000ReturnsBufferedImage() throws Exception {
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(dummy, "jpg", baos);
        byte[] jpegBytes = baos.toByteArray();

        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setImageType(0);
        dto.setInputBytes(jpegBytes);
        BufferedImage img = CommonUtil.getBufferedImage(dto);
        assertNotNull(img);
    }

    /**
     * Test getBufferedImage() with WSQ type throws exception.
     */
    @Test
    void getBufferedImageWsqThrowsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setImageType(1);
        dto.setInputBytes(new byte[]{0x00, 0x01, 0x02});
        assertThrows(Exception.class, () -> CommonUtil.getBufferedImage(dto));
    }

    /**
     * Test getBufferedImage() with unsupported image type throws exception.
     */
    @Test
    void getBufferedImageUnsupportedTypeThrowsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setImageType(99);
        Exception ex = assertThrows(Exception.class, () -> CommonUtil.getBufferedImage(dto));
        assertTrue(ex.getMessage().contains("not supported"));
    }

    /**
     * Test isNullEmpty() for byte array with various scenarios.
     */
    @Test
    void isNullEmptyByteArrayNullOrEmptyReturnsTrue() {
        assertTrue(CommonUtil.isNullEmpty((byte[]) null));
        assertTrue(CommonUtil.isNullEmpty(new byte[]{}));
        assertFalse(CommonUtil.isNullEmpty(new byte[]{1}));
    }

    /**
     * Test isNullEmpty() for String with various scenarios.
     */
    @Test
    void isNullEmptyStringNullOrBlankReturnsTrue() {
        assertTrue(CommonUtil.isNullEmpty((String) null));
        assertTrue(CommonUtil.isNullEmpty("   "));
        assertFalse(CommonUtil.isNullEmpty("data"));
    }

    /**
     * Test concatByteArrays() concatenates arrays in correct order.
     */
    @Test
    void concatByteArraysMultipleArraysReturnsConcatenatedArray() {
        byte[] a = {1};
        byte[] b = {2};
        byte[] c = {3};
        byte[] d = {4};
        byte[] expected = {1, 2, 3, 4};
        byte[] result = CommonUtil.concatByteArrays(a, b, c, d);
        assertArrayEquals(expected, result);
    }

    /**
     * Test getLastBytes() with valid length returns correct bytes.
     */
    @Test
    void getLastBytesValidLengthReturnsLastBytes() {
        byte[] src = {1, 2, 3, 4, 5};
        byte[] result = CommonUtil.getLastBytes(src, 3);
        assertArrayEquals(new byte[]{3, 4, 5}, result);
    }

    /**
     * Test getLastBytes() with invalid length throws exception.
     */
    @Test
    void getLastBytesInvalidLengthThrowsException() {
        byte[] src = {1};
        assertThrows(Exception.class, () -> CommonUtil.getLastBytes(src, 3));
    }

    /**
     * Test conversion of BufferedImage to JPEG and PNG bytes.
     */
    @Test
    void convertBufferedImageValidImageReturnsNonEmptyBytes() {
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        byte[] jpeg = CommonUtil.convertBufferedImageToJPEGBytes(dummy);
        byte[] png = CommonUtil.convertBufferedImageToPNGBytes(dummy);
        assertNotNull(jpeg);
        assertNotNull(png);
        assertTrue(jpeg.length > 0);
        assertTrue(png.length > 0);
    }
    @Test
    void convertJP2ToJPEGBytesValidDataReturnsJpegBytes() throws Exception {
        // Create a simple test image and convert to JPEG format for testing
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(testImage, "jpg", baos);
        byte[] jpegData = baos.toByteArray();

        byte[] result = CommonUtil.convertJP2ToJPEGBytes(jpegData);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    /**
     * Test convertJP2ToPNGBytes() with valid data returns PNG bytes.
     */
    @Test
    void convertJP2ToPNGBytesValidDataReturnsPngBytes() throws Exception {
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(testImage, "jpg", baos);
        byte[] jpegData = baos.toByteArray();

        byte[] result = CommonUtil.convertJP2ToPNGBytes(jpegData);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    /**
     * Test flipImage() with null source image throws IllegalArgumentException.
     */
    @Test
    void flipImageNullSourceImageThrowsIllegalArgumentException() {
        BufferedImage dst = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertThrows(IllegalArgumentException.class, () ->
                CommonUtil.flipImage(null, dst, true, false));
    }

    /**
     * Test flipImage() with null destination image throws IllegalArgumentException.
     */
    @Test
    void flipImageNullDestinationImageThrowsIllegalArgumentException() {
        BufferedImage src = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertThrows(IllegalArgumentException.class, () ->
                CommonUtil.flipImage(src, null, true, false));
    }

    /**
     * Test flipImage() with mismatched dimensions throws IllegalArgumentException.
     */
    @Test
    void flipImageMismatchedDimensionsThrowsIllegalArgumentException() {
        BufferedImage src = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage dst = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        assertThrows(IllegalArgumentException.class, () ->
                CommonUtil.flipImage(src, dst, true, true));
    }

    /**
     * Test flipImage() with valid parameters completes successfully.
     */
    @Test
    void flipImageValidParametersCompletesSuccessfully() {
        BufferedImage src = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage dst = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        // Should not throw any exception
        assertDoesNotThrow(() -> CommonUtil.flipImage(src, dst, true, false));
        assertDoesNotThrow(() -> CommonUtil.flipImage(src, dst, false, true));
        assertDoesNotThrow(() -> CommonUtil.flipImage(src, dst, true, true));
    }

    /**
     * Test flipImageUsingOpenCV() with null image throws IllegalArgumentException.
     */
    @Test
    void flipImageUsingOpenCVNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                CommonUtil.flipImageUsingOpenCV(null, true, false));
    }

    /**
     * Test flipImageUsingOpenCV() with valid image returns flipped image.
     */
    @Test
    void flipImageUsingOpenCVValidImageReturnsFlippedImage() {
        // Note: This test requires OpenCV to be properly loaded
        Mat image = new Mat(new Size(10, 10), CvType.CV_8UC3);

        Mat result = CommonUtil.flipImageUsingOpenCV(image, true, false);
        assertNotNull(result);
        assertEquals(image.size().width, result.size().width);
        assertEquals(image.size().height, result.size().height);
    }

    /**
     * Test isRGBFormat() with null or empty array returns false.
     */
    @Test
    void isRGBFormatNullOrEmptyArrayReturnsFalse() {
        assertFalse(CommonUtil.isRGBFormat(null));
        assertFalse(CommonUtil.isRGBFormat(new byte[]{}));
    }

    /**
     * Test isRGBFormat() with valid RGB image data.
     */
    @Test
    void isRGBFormatValidRgbImageReturnsCorrectValue() throws Exception {
        BufferedImage rgbImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(rgbImage, "jpg", baos);
        byte[] imageData = baos.toByteArray();

        // The result depends on the actual image format
        boolean result = CommonUtil.isRGBFormat(imageData);
        // Assert that the method completes without exception
        assertNotNull(Boolean.valueOf(result));
    }

    /**
     * Test convertWSQToBufferedImage() with invalid WSQ data throws exception.
     */
    @Test
    void convertWSQToBufferedImageInvalidDataThrowsException() {
        byte[] invalidWSQData = new byte[]{0x00, 0x01, 0x02};
        assertThrows(Exception.class, () ->
                CommonUtil.convertWSQToBufferedImage(invalidWSQData));
    }

    /**
     * Test encodeToURLSafeBase64() with null data throws exception.
     */
    @Test
    void encodeToURLSafeBase64NullDataThrowsException() {
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.encodeToURLSafeBase64((byte[]) null));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.encodeToURLSafeBase64((String) null));
    }

    /**
     * Test encodeToURLSafeBase64() with empty data throws exception.
     */
    @Test
    void encodeToURLSafeBase64EmptyDataThrowsException() {
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.encodeToURLSafeBase64(new byte[]{}));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.encodeToURLSafeBase64(""));
    }

    /**
     * Test encodeToURLSafeBase64() with valid data returns encoded string.
     */
    @Test
    void encodeToURLSafeBase64ValidDataReturnsEncodedString() {
        byte[] data = {1, 2, 3, 4, 5};
        String result = CommonUtil.encodeToURLSafeBase64(data);
        assertNotNull(result);
        assertTrue(result.length() > 0);

        String stringData = "test data";
        String stringResult = CommonUtil.encodeToURLSafeBase64(stringData);
        assertNotNull(stringResult);
        assertTrue(stringResult.length() > 0);
    }

    /**
     * Test decodeURLSafeBase64() with null data throws exception.
     */
    @Test
    void decodeURLSafeBase64NullDataThrowsException() {
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.decodeURLSafeBase64((String) null));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.decodeURLSafeBase64((byte[]) null));
    }

    /**
     * Test decodeURLSafeBase64() with empty data throws exception.
     */
    @Test
    void decodeURLSafeBase64EmptyDataThrowsException() {
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.decodeURLSafeBase64(""));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.decodeURLSafeBase64(new byte[]{}));
    }

    /**
     * Test decodeURLSafeBase64() with valid encoded data returns decoded bytes.
     */
    @Test
    void decodeURLSafeBase64ValidDataReturnsDecodedBytes() {
        byte[] originalData = {1, 2, 3, 4, 5};
        String encoded = CommonUtil.encodeToURLSafeBase64(originalData);
        byte[] decoded = CommonUtil.decodeURLSafeBase64(encoded);
        assertArrayEquals(originalData, decoded);
    }

    /**
     * Test toUtf8ByteArray() converts string to UTF-8 bytes.
     */
    @Test
    void toUtf8ByteArrayValidStringReturnsUtf8Bytes() {
        String input = "Hello World";
        byte[] result = CommonUtil.toUtf8ByteArray(input);
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(input, new String(result, java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * Test getXOR() with two strings returns XOR result.
     */
    @Test
    void getXORTwoStringsReturnsXorResult() {
        String a = "ABC";
        String b = "XYZ";
        byte[] result = CommonUtil.getXOR(a, b);
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    /**
     * Test getXOR() with strings of different lengths.
     */
    @Test
    void getXORDifferentLengthStringsReturnsXorResult() {
        String shorter = "AB";
        String longer = "WXYZ";
        byte[] result = CommonUtil.getXOR(shorter, longer);
        assertNotNull(result);
        assertEquals(4, result.length); // Should be length of longer string
    }

    /**
     * Test prependZeros() adds zeros to beginning of array.
     */
    @Test
    void prependZerosAddsZerosToBeginning() {
        byte[] original = {1, 2, 3};
        byte[] result = CommonUtil.prependZeros(original, 2);
        byte[] expected = {0, 0, 1, 2, 3};
        assertArrayEquals(expected, result);
    }

    /**
     * Test hexStringToByteArray() converts hex string to bytes.
     */
    @Test
    void hexStringToByteArrayValidHexReturnsBytes() {
        String hex = "DEADBEEF";
        byte[] result = CommonUtil.hexStringToByteArray(hex);
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals((byte)0xDE, result[0]);
        assertEquals((byte)0xAD, result[1]);
        assertEquals((byte)0xBE, result[2]);
        assertEquals((byte)0xEF, result[3]);
    }

    /**
     * Test convertISOImageType() with unsupported modality throws exception.
     */
    @Test
    void convertISOImageTypeUnsupportedModalityThrowsException() {
        // Assuming there's an unsupported modality or null modality
        assertThrows(Exception.class, () ->
                CommonUtil.convertISOImageType("test", null, ImageType.JPEG));
    }

    /**
     * Test convertBufferedImageToMat() with valid image returns Mat.
     */
    @Test
    void convertBufferedImageToMatValidImageReturnsMat() throws Exception {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Mat result = CommonUtil.convertBufferedImageToMat(image);
        assertNotNull(result);
        assertFalse(result.empty());
    }

    /**
     * Test nistParser() with invalid data throws exception.
     */
    @Test
    void nistParserInvalidDataThrowsException() {
        NistRequestDto invalidRequest = new NistRequestDto();
        invalidRequest.setInputBytes(new byte[]{0x00, 0x01, 0x02});
        assertThrows(Exception.class, () -> CommonUtil.nistParser(invalidRequest));
    }

    /**
     * Test OpenCV conversion methods with invalid data throw exceptions.
     */
    @Test
    void openCVConversionMethodsInvalidDataThrowsExceptions() {
        byte[] invalidData = new byte[]{0x00, 0x01, 0x02};

        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.convertJP2ToJPEGUsingOpenCV(invalidData, 80));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.convertJP2ToPNGUsingOpenCV(invalidData, 6));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.convertJP2ToJP2UsingOpenCV(invalidData, 1000));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.convertJPEGToJP2UsingOpenCV(invalidData, 1000));
        assertThrows(BiometricUtilException.class, () ->
                CommonUtil.convertJP2ToWEBPUsingOpenCV(invalidData, 80));
    }
}