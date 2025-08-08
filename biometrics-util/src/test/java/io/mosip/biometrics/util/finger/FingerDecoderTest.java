package io.mosip.biometrics.util.finger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;

/**
 * Unit tests for {@link FingerDecoder} class.
 */
class FingerDecoderTest {
    private static final String ISO_VERSION = "ISO19794_4_2011";
    private static final String UNSUPPORTED_VERSION = "UNSUPPORTED";
    private static final byte[] TEST_ISO_DATA = new byte[100];

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests that FingerDecoder constructor throws IllegalStateException.
     */
    @Test
    void constructorThrowsIllegalStateException() {
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            Constructor<FingerDecoder> constructor = FingerDecoder.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
        assertThrows(IllegalStateException.class, () -> {
            throw (RuntimeException) exception.getCause();
        });
    }

    /**
     * Tests getFingerBDIR with valid ISO version and onlyImageInformation = 1.
     */
    @Test
    void getFingerBdirWithIsoVersionAndImageInfoOnlyThrowsException() throws Exception {
        ConvertRequestDto dto = createTestRequest(ISO_VERSION, new byte[]{1, 2, 3}, 1);
        assertThrows(Exception.class, () -> FingerDecoder.getFingerBDIR(dto));
    }

    /**
     * Tests getFingerBDIR with valid ISO version and onlyImageInformation = 0.
     */
    @Test
    void getFingerBdirWithIsoVersionAndFullInfoThrowsException() throws Exception {
        ConvertRequestDto dto = createTestRequest(ISO_VERSION, new byte[]{1, 2, 3}, 0);
        assertThrows(Exception.class, () -> FingerDecoder.getFingerBDIR(dto));
    }

    /**
     * Tests getFingerBDIR throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void getFingerBdirUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = createTestRequest(UNSUPPORTED_VERSION, new byte[]{1, 2, 3}, 0);
        assertThrows(UnsupportedOperationException.class, () -> FingerDecoder.getFingerBDIR(dto));
    }

    /**
     * Tests convertFingerISOToImageBytes with JPEG_2000_LOSSY compression.
     */
    @Test
    void convertIsoToImageBytesJpeg2000LossyReturnsJpegBytes() throws Exception {
        byte[] jpegBytes = new byte[]{9, 8, 7};
        byte[] imageBytes = new byte[]{1, 2, 3};
        ConvertRequestDto dto = createTestRequest(ISO_VERSION, new byte[]{1, 2, 3}, 0);

        FingerBDIR fingerBDIR = createMockFingerBDIR(FingerImageCompressionType.JPEG_2000_LOSSY, imageBytes);

        try (MockedStatic<CommonUtil> commonUtilMock = mockStatic(CommonUtil.class)) {
            commonUtilMock.when(() -> CommonUtil.convertJP2ToJPEGUsingOpenCV(any(byte[].class), anyInt()))
                    .thenReturn(jpegBytes);

            try (MockedStatic<FingerDecoder> fingerDecoderMock = mockStatic(FingerDecoder.class, Mockito.CALLS_REAL_METHODS)) {
                Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
                method.setAccessible(true);
                fingerDecoderMock.when(() -> method.invoke(null, any(byte[].class), anyInt()))
                        .thenReturn(fingerBDIR);

                byte[] result = FingerDecoder.convertFingerISOToImageBytes(dto);
                assertArrayEquals(jpegBytes, result);
            }
        }
    }

    /**
     * Tests convertFingerISOToImageBytes with JPEG_2000_LOSS_LESS compression.
     */
    @Test
    void convertIsoToImageBytesJpeg2000LossLessReturnsJpegBytes() throws Exception {
        byte[] jpegBytes = new byte[]{9, 8, 7};
        byte[] imageBytes = new byte[]{1, 2, 3};
        ConvertRequestDto dto = createTestRequest(ISO_VERSION, new byte[]{1, 2, 3}, 0);

        FingerBDIR fingerBDIR = createMockFingerBDIR(FingerImageCompressionType.JPEG_2000_LOSS_LESS, imageBytes);

        try (MockedStatic<CommonUtil> commonUtilMock = mockStatic(CommonUtil.class)) {
            commonUtilMock.when(() -> CommonUtil.convertJP2ToJPEGUsingOpenCV(any(byte[].class), anyInt()))
                    .thenReturn(jpegBytes);

            try (MockedStatic<FingerDecoder> fingerDecoderMock = mockStatic(FingerDecoder.class, Mockito.CALLS_REAL_METHODS)) {
                Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
                method.setAccessible(true);
                fingerDecoderMock.when(() -> method.invoke(null, any(byte[].class), anyInt()))
                        .thenReturn(fingerBDIR);

                byte[] result = FingerDecoder.convertFingerISOToImageBytes(dto);
                assertArrayEquals(jpegBytes, result);
            }
        }
    }

    /**
     * Tests convertFingerISOToImageBytes with non-JPEG2000 compression returns raw bytes.
     */
    @Test
    void convertIsoToImageBytesNonJpeg2000ReturnsRawBytes() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3};
        ConvertRequestDto dto = createTestRequest(ISO_VERSION, new byte[]{1, 2, 3}, 0);
        FingerBDIR fingerBDIR = createMockFingerBDIR(1234, imageBytes);

        try (MockedStatic<FingerDecoder> fingerDecoderMock = mockStatic(FingerDecoder.class, Mockito.CALLS_REAL_METHODS)) {
            Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
            method.setAccessible(true);
            fingerDecoderMock.when(() -> method.invoke(null, any(byte[].class), anyInt()))
                    .thenReturn(fingerBDIR);

            byte[] result = FingerDecoder.convertFingerISOToImageBytes(dto);
            assertArrayEquals(imageBytes, result);
        }
    }

    /**
     * Tests convertFingerISOToImageBytes throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void convertIsoToImageBytesUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = createTestRequest(UNSUPPORTED_VERSION, new byte[]{1, 2, 3}, 0);
        assertThrows(UnsupportedOperationException.class, () -> FingerDecoder.convertFingerISOToImageBytes(dto));
    }

    /**
     * Tests convertFingerISOToBufferedImage with valid ISO version returns BufferedImage.
     */
    @Test
    void convertIsoToBufferedImageWithValidVersionReturnsImage() throws Exception {
        byte[] validImageBytes = createValidImageBytes();
        ConvertRequestDto dto = createTestRequest(ISO_VERSION, new byte[]{1, 2, 3}, 0);
        FingerBDIR fingerBDIR = createMockFingerBDIR(1234, validImageBytes);

        try (MockedStatic<FingerDecoder> fingerDecoderMock = mockStatic(FingerDecoder.class, Mockito.CALLS_REAL_METHODS)) {
            Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
            method.setAccessible(true);
            fingerDecoderMock.when(() -> method.invoke(null, any(byte[].class), anyInt()))
                    .thenReturn(fingerBDIR);

            BufferedImage result = FingerDecoder.convertFingerISOToBufferedImage(dto);
            assertNotNull(result);
        }
    }

    /**
     * Tests convertFingerISOToBufferedImage throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void convertIsoToBufferedImageUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = createTestRequest(UNSUPPORTED_VERSION, new byte[]{1, 2, 3}, 0);
        assertThrows(UnsupportedOperationException.class, () -> FingerDecoder.convertFingerISOToBufferedImage(dto));
    }

    /**
     * Tests getFingerBDIRISO19794_4_2011 private method with onlyImageInformation = 1.
     */
    @Test
    void getFingerBdirIso19794WithImageInfoOnlyThrowsException() throws Exception {
        Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(null, new byte[]{1, 2, 3}, 1);
        });
    }

    /**
     * Tests getFingerBDIRISO19794_4_2011 private method with onlyImageInformation = 0.
     */
    @Test
    void getFingerBdirIso19794WithFullInfoThrowsException() throws Exception {
        Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(null, new byte[]{1, 2, 3}, 0);
        });
    }

    private ConvertRequestDto createTestRequest(String version, byte[] inputBytes, int onlyImageInfo) {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn(version);
        when(dto.getInputBytes()).thenReturn(inputBytes);
        when(dto.getOnlyImageInformation()).thenReturn(onlyImageInfo);
        when(dto.getCompressionRatio()).thenReturn(80);
        return dto;
    }

    private FingerBDIR createMockFingerBDIR(int compressionType, byte[] imageData) {
        FingerBDIR fingerBDIR = mock(FingerBDIR.class);
        Representation representation = mock(Representation.class);
        RepresentationHeader header = mock(RepresentationHeader.class);
        RepresentationBody body = mock(RepresentationBody.class);
        ImageData imgData = new ImageData(imageData);

        when(fingerBDIR.getRepresentation()).thenReturn(representation);
        when(representation.getRepresentationHeader()).thenReturn(header);
        when(representation.getRepresentationBody()).thenReturn(body);
        when(header.getCompressionType()).thenReturn(compressionType);
        when(body.getImageData()).thenReturn(imgData);

        return fingerBDIR;
    }

    private byte[] createValidImageBytes() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}
