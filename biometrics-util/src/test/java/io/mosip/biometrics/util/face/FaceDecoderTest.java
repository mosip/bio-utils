package io.mosip.biometrics.util.face;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.mosip.biometrics.util.ConvertRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Unit tests for {@link FaceDecoder}
 */
@ExtendWith(MockitoExtension.class)
class FaceDecoderTest {

    private ConvertRequestDto convertRequestDto;
    private byte[] testImageBytes;
    private static final String ISO_VERSION = "ISO19794_5_2011";
    private Method getFaceBDIRISO19794_5_2011Method;
    private Method convertFaceISO19794_5_2011ToImageMethod;

    /**
     * Setup reflection methods and test data before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        testImageBytes = baos.toByteArray();

        convertRequestDto = new ConvertRequestDto();
        convertRequestDto.setVersion(ISO_VERSION);
        convertRequestDto.setInputBytes(testImageBytes);
        convertRequestDto.setOnlyImageInformation(0);
        convertRequestDto.setCompressionRatio(100);

        getFaceBDIRISO19794_5_2011Method = FaceDecoder.class.getDeclaredMethod(
                "getFaceBDIRISO19794_5_2011", byte[].class, int.class);
        getFaceBDIRISO19794_5_2011Method.setAccessible(true);

        convertFaceISO19794_5_2011ToImageMethod = FaceDecoder.class.getDeclaredMethod(
                "convertFaceISO19794_5_2011ToImage", byte[].class);
        convertFaceISO19794_5_2011ToImageMethod.setAccessible(true);
    }

    /**
     * Test for unsupported version in getFaceBDIR().
     */
    @Test
    void getFaceBDIRWithUnsupportedVersion() {
        convertRequestDto.setVersion("UNSUPPORTED");
        assertThrows(UnsupportedOperationException.class, () ->
                FaceDecoder.getFaceBDIR(convertRequestDto)
        );
    }

    /**
     * Test when input bytes are null in convertFaceISOToImageBytes().
     */
    @Test
    void convertFaceISOToImageBytesWithNullInputBytes() {
        convertRequestDto.setInputBytes(null);
        assertThrows(NullPointerException.class, () ->
                FaceDecoder.convertFaceISOToImageBytes(convertRequestDto)
        );
    }

    /**
     * Test private constructor throws IllegalStateException.
     */
    @Test
    void constructorWithPrivateAccess() throws Exception {
        Constructor<FaceDecoder> constructor = FaceDecoder.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertEquals("FaceDecoder class", exception.getCause().getMessage());
    }

    /**
     * Test convertFaceISOToBufferedImage with unsupported version.
     */
    @Test
    void convertFaceISOToBufferedImageWithUnsupportedVersion() {
        convertRequestDto.setVersion("INVALID");
        assertThrows(UnsupportedOperationException.class, () ->
                FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto)
        );
    }

    /**
     * Test convertFaceISOToImageBytes with unsupported version.
     */
    @Test
    void convertFaceISOToImageBytesWithUnsupportedVersion() {
        convertRequestDto.setVersion("INVALID");
        assertThrows(UnsupportedOperationException.class, () ->
                FaceDecoder.convertFaceISOToImageBytes(convertRequestDto)
        );
    }

    /**
     * Test convertFaceISOToBufferedImage with null input bytes.
     */
    @Test
    void convertFaceISOToBufferedImageWithNullInputBytes() {
        convertRequestDto.setInputBytes(null);
        assertThrows(NullPointerException.class, () ->
                FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto)
        );
    }

    /**
     * Test getFaceBDIR with null input bytes.
     */
    @Test
    void getFaceBDIRWithNullInputBytes() {
        convertRequestDto.setInputBytes(null);
        assertThrows(NullPointerException.class, () ->
                FaceDecoder.getFaceBDIR(convertRequestDto)
        );
    }

    /**
     * Test getFaceBDIR with invalid ISO data.
     */
    @Test
    void getFaceBDIRWithInvalidISOData() {
        convertRequestDto.setInputBytes("invalid".getBytes());
        assertThrows(Exception.class, () -> FaceDecoder.getFaceBDIR(convertRequestDto));
    }

    /**
     * Test convertFaceISOToBufferedImage with invalid data.
     */
    @Test
    void convertFaceISOToBufferedImageWithInvalidData() {
        convertRequestDto.setInputBytes("invalid".getBytes());
        assertThrows(Exception.class, () -> FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto));
    }

    /**
     * Test convertFaceISOToImageBytes with invalid data.
     */
    @Test
    void convertFaceISOToImageBytesWithInvalidData() {
        convertRequestDto.setInputBytes("invalid".getBytes());
        assertThrows(Exception.class, () -> FaceDecoder.convertFaceISOToImageBytes(convertRequestDto));
    }

    /**
     * Test with empty byte array.
     */
    @Test
    void testWithEmptyByteArray() {
        convertRequestDto.setInputBytes(new byte[0]);
        assertThrows(Exception.class, () -> FaceDecoder.getFaceBDIR(convertRequestDto));
    }

    /**
     * Test private method getFaceBDIRISO19794_5_2011 with invalid data via reflection.
     */
    @Test
    void testPrivateGetFaceBDIRMethodWithInvalidData() throws Exception {
        byte[] invalidData = "invalid".getBytes();

        assertThrows(Exception.class, () -> {
            try {
                getFaceBDIRISO19794_5_2011Method.invoke(null, invalidData, 0);
            } catch (Exception e) {
                if (e.getCause() != null) {
                    throw e.getCause();
                }
                throw e;
            }
        });
    }

    /**
     * Test private method getFaceBDIRISO19794_5_2011 with onlyImageInformation = 1 via reflection.
     */
    @Test
    void testPrivateGetFaceBDIRMethodWithOnlyImageInformation() throws Exception {
        byte[] invalidData = "invalid".getBytes();

        assertThrows(Exception.class, () -> {
            try {
                getFaceBDIRISO19794_5_2011Method.invoke(null, invalidData, 1);
            } catch (Exception e) {
                if (e.getCause() != null) {
                    throw e.getCause();
                }
                throw e;
            }
        });
    }

    /**
     * Test private method convertFaceISO19794_5_2011ToImage via reflection.
     */
    @Test
    void testPrivateConvertToImageMethod() throws Exception {
        byte[] invalidData = "invalid".getBytes();

        assertThrows(Exception.class, () -> {
            try {
                convertFaceISO19794_5_2011ToImageMethod.invoke(null, invalidData);
            } catch (Exception e) {
                if (e.getCause() != null) {
                    throw e.getCause();
                }
                throw e;
            }
        });
    }

    /**
     * Test reflection method accessibility.
     */
    @Test
    void testReflectionMethodsAccessible() {
        assertNotNull(getFaceBDIRISO19794_5_2011Method);
        assertNotNull(convertFaceISO19794_5_2011ToImageMethod);
    }

    /**
     * Test with different compression ratios.
     */
    @Test
    void testWithDifferentCompressionRatios() {
        convertRequestDto.setInputBytes("invalid".getBytes());
        convertRequestDto.setCompressionRatio(50);
        assertThrows(Exception.class, () -> FaceDecoder.convertFaceISOToImageBytes(convertRequestDto));

        convertRequestDto.setCompressionRatio(75);
        assertThrows(Exception.class, () -> FaceDecoder.convertFaceISOToImageBytes(convertRequestDto));
    }

    /**
     * Test parameter variations with only invalid data to avoid false positives.
     */
    @Test
    void testParameterVariationsWithInvalidData() {
        byte[][] invalidDataArrays = {
                new byte[0],
                "test".getBytes(),
                new byte[]{1, 2, 3}
        };

        for (byte[] data : invalidDataArrays) {
            convertRequestDto.setInputBytes(data);

            assertThrows(Exception.class, () -> FaceDecoder.getFaceBDIR(convertRequestDto));
            assertThrows(Exception.class, () -> FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto));
            assertThrows(Exception.class, () -> FaceDecoder.convertFaceISOToImageBytes(convertRequestDto));
        }
    }

    /**
     * Test with various onlyImageInformation values using invalid data.
     */
    @Test
    void testOnlyImageInformationValues() {
        convertRequestDto.setInputBytes("test".getBytes());

        convertRequestDto.setOnlyImageInformation(0);
        assertThrows(Exception.class, () -> FaceDecoder.getFaceBDIR(convertRequestDto));

        convertRequestDto.setOnlyImageInformation(1);
        assertThrows(Exception.class, () -> FaceDecoder.getFaceBDIR(convertRequestDto));
    }
}
