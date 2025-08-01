package io.mosip.biometrics.util;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

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
}