package io.mosip.biometrics.util.face;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.mosip.biometrics.util.ConvertRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FaceDecoder}.
 */
@ExtendWith(MockitoExtension.class)
class FaceDecoderTest {

    @Mock private FaceBDIR faceBDIR;
    @Mock private Representation representation;
    @Mock private RepresentationData representationData;
    @Mock private RepresentationHeader representationHeader;
    @Mock private ImageData imageData;
    @Mock private ImageInformation imageInformation;

    private ConvertRequestDto convertRequestDto;
    private byte[] testImageBytes;
    private static final String ISO_VERSION = "ISO19794_5_2011";

    /**
     * Setup a 1x1 black JPEG test image and default ConvertRequestDto.
     */
    @BeforeEach
    void setUp() throws IOException {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        testImageBytes = baos.toByteArray();

        convertRequestDto = new ConvertRequestDto();
        convertRequestDto.setVersion(ISO_VERSION);
        convertRequestDto.setInputBytes(testImageBytes);
        convertRequestDto.setOnlyImageInformation(0);
        convertRequestDto.setCompressionRatio(100);
    }

    /**
     * Test for unsupported version in getFaceBDIR().
     */
    @Test
    void getFaceBDIR_whenUnsupportedVersion_thenThrowException() {
        convertRequestDto.setVersion("UNSUPPORTED");
        assertThrows(UnsupportedOperationException.class, () ->
                FaceDecoder.getFaceBDIR(convertRequestDto)
        );
    }

    /**
     * Test when input bytes are null in convertFaceISOToImageBytes().
     */
    @Test
    void convertFaceISOToImageBytes_whenNullInputBytes_thenThrowException() {
        convertRequestDto.setInputBytes(null);
        assertThrows(NullPointerException.class, () ->
                FaceDecoder.convertFaceISOToImageBytes(convertRequestDto)
        );
    }

    /**
     * Test private constructor throws IllegalStateException.
     */
    @Test
    void constructor_whenPrivateAccess_thenThrowIllegalStateException() throws Exception {
        Constructor<FaceDecoder> constructor = FaceDecoder.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertEquals("FaceDecoder class", exception.getCause().getMessage());
    }

    /**
     * Test convertFaceISOToBufferedImage with unsupported version.
     */
    @Test
    void convertFaceISOToBufferedImage_whenUnsupportedVersion_thenThrowException() {
        convertRequestDto.setVersion("INVALID");
        assertThrows(UnsupportedOperationException.class, () ->
                FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto)
        );
    }

    /**
     * Test convertFaceISOToImageBytes with unsupported version.
     */
    @Test
    void convertFaceISOToImageBytes_whenUnsupportedVersion_thenThrowException() {
        convertRequestDto.setVersion("INVALID");
        assertThrows(UnsupportedOperationException.class, () ->
                FaceDecoder.convertFaceISOToImageBytes(convertRequestDto)
        );
    }
}