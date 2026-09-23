package io.mosip.biometrics.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Coverage for {@link ImageDecoderRequestDto} and {@link ImageType}.
 */
class ImageDecoderRequestDtoTest {

    @Test
    void allArgsConstructorAndLombokAccessors() {
        ImageDecoderRequestDto dto = new ImageDecoderRequestDto(
                "JP2000", 100, 200, true, 8, 500, 500, 10, 1024,
                "abc", "GRAY", "1:1", "10:1");
        assertEquals("JP2000", dto.getImageType());
        assertEquals(100, dto.getWidth());
        assertEquals(200, dto.getHeight());
        assertTrue(dto.isLossless());
        assertEquals(8, dto.getDepth());
        assertEquals(500, dto.getHorizontalDPI());
        assertEquals(500, dto.getVerticalDPI());
        assertEquals(10, dto.getBitRate());
        assertEquals(1024, dto.getSize());
        assertEquals("abc", dto.getImageData());
        assertEquals("GRAY", dto.getImageColorSpace());
        assertEquals("1:1", dto.getImageAspectRatio());
        assertEquals("10:1", dto.getImageCompressionRatio());

        dto.setImageType("WSQ");
        dto.setLossless(false);
        assertEquals("WSQ", dto.getImageType());
        assertFalse(dto.isLossless());
    }

    @Test
    void imageTypeFromValueAndToString() {
        assertEquals(ImageType.JPEG, ImageType.fromValue(ImageType.JPEG.value()));
        assertEquals(ImageType.PNG, ImageType.fromValue(ImageType.PNG.value()));
        assertTrue(ImageType.WEBP.toString().contains("webp".toLowerCase()) || ImageType.WEBP.toString().contains("4"));
        assertThrows(IllegalArgumentException.class, () -> ImageType.fromValue(99));
    }
}
