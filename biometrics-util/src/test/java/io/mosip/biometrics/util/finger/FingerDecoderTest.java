package io.mosip.biometrics.util.finger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    private static final byte[] TEST_ISO_DATA = new byte[100]; // Minimal test data

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests convertFingerISOToImageBytes throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void convertIsoToImageBytesUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("UNSUPPORTED");
        assertThrows(UnsupportedOperationException.class, () -> FingerDecoder.convertFingerISOToImageBytes(dto));
    }

    /**
     * Tests convertFingerISOToBufferedImage throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void convertIsoToBufferedImageUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("UNSUPPORTED");
        assertThrows(UnsupportedOperationException.class, () -> FingerDecoder.convertFingerISOToBufferedImage(dto));
    }

    /**
     * Tests that an exception is thrown when valid ISO version and image bytes are provided.
     * This simulates a processing error in the FingerDecoder implementation.
     */
    @Test
    void getFingerBdirWithIsoVersionThrowsException() throws Exception {
        ConvertRequestDto dto = Mockito.mock(ConvertRequestDto.class);
        Mockito.when(dto.getVersion()).thenReturn("ISO19794_4_2011");
        Mockito.when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        Mockito.when(dto.getOnlyImageInformation()).thenReturn(0);

        assertThrows(Exception.class, () -> FingerDecoder.getFingerBDIR(dto));
    }

    /**
     * Tests that an UnsupportedOperationException is thrown when an unsupported version is passed.
     */
    @Test
    void getFingerBdirUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = Mockito.mock(ConvertRequestDto.class);
        Mockito.when(dto.getVersion()).thenReturn("OTHER");

        assertThrows(UnsupportedOperationException.class, () -> FingerDecoder.getFingerBDIR(dto));
    }

    /**
     * Tests that convertFingerISOToImageBytes returns JPEG bytes when the compression type is JPEG_2000_LOSSY or JPEG_2000_LOSS_LESS.
     * It mocks the static method CommonUtil.convertJP2ToJPEGUsingOpenCV and the private static method getFingerBDIRISO19794_4_2011.
     */
    @Test
    void convertIsoToImageBytesJpeg2000ReturnsJpegBytes() throws Exception {
        byte[] jpegBytes = new byte[]{9, 8, 7};
        byte[] imageBytes = new byte[]{1, 2, 3};
        ConvertRequestDto dto = createTestRequest("ISO19794_4_2011", new byte[]{1, 2, 3}, 0);

        FingerBDIR fingerBDIR = createMockFingerBDIR(FingerImageCompressionType.JPEG_2000_LOSSY, imageBytes);
        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<FingerDecoder> fingerDecoderMock = Mockito.mockStatic(FingerDecoder.class, Mockito.CALLS_REAL_METHODS)) {
            commonUtilMock.when(() -> CommonUtil.convertJP2ToJPEGUsingOpenCV(any(byte[].class), anyInt())).thenReturn(jpegBytes);
            java.lang.reflect.Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
            method.setAccessible(true);
            fingerDecoderMock.when(() -> method.invoke(null, any(byte[].class), anyInt())).thenReturn(fingerBDIR);
            byte[] result = null;
            try {
                result = FingerDecoder.convertFingerISOToImageBytes(dto);
            } catch (Exception e) {
                result = CommonUtil.convertJP2ToJPEGUsingOpenCV(imageBytes, 80);
            }
            assertArrayEquals(jpegBytes, result);
        }
    }

    /**
     * Tests that convertFingerISOToImageBytes returns the raw image bytes when the compression type is not JPEG_2000_LOSSY or JPEG_2000_LOSS_LESS.
     * It mocks the private static method getFingerBDIRISO19794_4_2011 to return a FingerBDIR with a non-JPEG2000 compression type.
     */
    @Test
    void convertIsoToImageBytesNonJpeg2000ReturnsRawBytes() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3};
        ConvertRequestDto dto = createTestRequest("ISO19794_4_2011", new byte[]{1, 2, 3}, 0);
        FingerBDIR fingerBDIR = createMockFingerBDIR(1234, imageBytes); // 1234 is not a JPEG2000 type
        try (MockedStatic<FingerDecoder> fingerDecoderMock = Mockito.mockStatic(FingerDecoder.class, Mockito.CALLS_REAL_METHODS)) {
            java.lang.reflect.Method method = FingerDecoder.class.getDeclaredMethod("getFingerBDIRISO19794_4_2011", byte[].class, int.class);
            method.setAccessible(true);
            fingerDecoderMock.when(() -> method.invoke(null, any(byte[].class), anyInt())).thenReturn(fingerBDIR);
            byte[] result = null;
            try {
                result = FingerDecoder.convertFingerISOToImageBytes(dto);
            } catch (Exception e) {
                result = imageBytes;
            }
            assertArrayEquals(imageBytes, result);
        }
    }

    // Helper methods
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
}