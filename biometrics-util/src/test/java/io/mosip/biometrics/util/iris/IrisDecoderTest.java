package io.mosip.biometrics.util.iris;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Unit tests for {@link IrisDecoder}.
 */
class IrisDecoderTest {

    /**
     * Verifies that an exception is thrown when processing ISO19794_6_2011 version.
     */
    @Test
    void getIrisBdirWithIsoVersionThrowsException() throws Exception {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("ISO19794_6_2011");
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getOnlyImageInformation()).thenReturn(0);

        assertThrows(Exception.class, () -> IrisDecoder.getIrisBDIR(dto));
    }

    /**
     * Verifies that UnsupportedOperationException is thrown for unsupported version.
     */
    @Test
    void getIrisBdirWithUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("OTHER");

        assertThrows(UnsupportedOperationException.class, () -> IrisDecoder.getIrisBDIR(dto));
    }

    /**
     * Tests that convertIrisISOToBufferedImage returns BufferedImage for valid ISO version.
     */
    @Test
    void convertIrisIsoToBufferedImageReturnsImage() throws Exception {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("ISO19794_6_2011");
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getOnlyImageInformation()).thenReturn(0);
        BufferedImage expectedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        IrisBDIR irisBDIR = mock(IrisBDIR.class);
        Representation rep = mock(Representation.class);
        RepresentationData repData = mock(RepresentationData.class);
        ImageData imageData = mock(ImageData.class);
        when(imageData.getImage()).thenReturn(new byte[]{1, 2, 3});
        when(repData.getImageData()).thenReturn(imageData);
        when(rep.getRepresentationData()).thenReturn(repData);
        when(irisBDIR.getRepresentation()).thenReturn(rep);
        try (org.mockito.MockedStatic<IrisDecoder> irisDecoderMock = org.mockito.Mockito.mockStatic(IrisDecoder.class, org.mockito.Mockito.CALLS_REAL_METHODS);
             org.mockito.MockedStatic<ImageIO> imageIOMock = org.mockito.Mockito.mockStatic(ImageIO.class)) {
            irisDecoderMock.when(() -> invokeGetIrisBDIRISO19794_6_2011(any(byte[].class), any(Integer.class))).thenReturn(irisBDIR);
            imageIOMock.when(() -> ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(expectedImage);
            BufferedImage result = IrisDecoder.convertIrisISOToBufferedImage(dto);
            assertEquals(expectedImage, result);
        }
    }

    /**
     * Tests that convertIrisISOToBufferedImage throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void convertIrisIsoToBufferedImageUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("UNSUPPORTED");
        assertThrows(UnsupportedOperationException.class, () -> IrisDecoder.convertIrisISOToBufferedImage(dto));
    }

    /**
     * Tests that convertIrisISOToImageBytes returns JPEG bytes for JPEG2000 image format.
     */
    @Test
    void convertIrisIsoToImageBytesJpeg2000ReturnsJpegBytes() throws Exception {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("ISO19794_6_2011");
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getOnlyImageInformation()).thenReturn(0);
        when(dto.getCompressionRatio()).thenReturn(80);
        IrisBDIR irisBDIR = mock(IrisBDIR.class);
        Representation rep = mock(Representation.class);
        RepresentationHeader header = mock(RepresentationHeader.class);
        ImageInformation info = mock(ImageInformation.class);
        RepresentationData repData = mock(RepresentationData.class);
        ImageData imageData = mock(ImageData.class);
        when(imageData.getImage()).thenReturn(new byte[]{1, 2, 3});
        when(repData.getImageData()).thenReturn(imageData);
        when(rep.getRepresentationData()).thenReturn(repData);
        when(rep.getRepresentationHeader()).thenReturn(header);
        when(header.getImageInformation()).thenReturn(info);
        when(info.getImageFormat()).thenReturn(ImageFormat.MONO_JPEG2000);
        when(irisBDIR.getRepresentation()).thenReturn(rep);
        byte[] jpegBytes = new byte[]{9, 8, 7};
        try (org.mockito.MockedStatic<IrisDecoder> irisDecoderMock = org.mockito.Mockito.mockStatic(IrisDecoder.class, org.mockito.Mockito.CALLS_REAL_METHODS);
             org.mockito.MockedStatic<io.mosip.biometrics.util.CommonUtil> commonUtilMock = org.mockito.Mockito.mockStatic(io.mosip.biometrics.util.CommonUtil.class)) {
            irisDecoderMock.when(() -> invokeGetIrisBDIRISO19794_6_2011(any(byte[].class), any(Integer.class))).thenReturn(irisBDIR);
            commonUtilMock.when(() -> io.mosip.biometrics.util.CommonUtil.convertJP2ToJPEGUsingOpenCV(any(byte[].class), anyInt())).thenReturn(jpegBytes);
            byte[] result = IrisDecoder.convertIrisISOToImageBytes(dto);
            assertArrayEquals(jpegBytes, result);
        }
    }

    /**
     * Tests that convertIrisISOToImageBytes returns raw image bytes for non-JPEG2000 image format.
     */
    @Test
    void convertIrisIsoToImageBytesNonJpeg2000ReturnsRawBytes() throws Exception {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("ISO19794_6_2011");
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getOnlyImageInformation()).thenReturn(0);
        when(dto.getCompressionRatio()).thenReturn(80);
        IrisBDIR irisBDIR = mock(IrisBDIR.class);
        Representation rep = mock(Representation.class);
        RepresentationHeader header = mock(RepresentationHeader.class);
        ImageInformation info = mock(ImageInformation.class);
        RepresentationData repData = mock(RepresentationData.class);
        ImageData imageData = mock(ImageData.class);
        byte[] rawBytes = new byte[]{4, 5, 6};
        when(imageData.getImage()).thenReturn(rawBytes);
        when(repData.getImageData()).thenReturn(imageData);
        when(rep.getRepresentationData()).thenReturn(repData);
        when(rep.getRepresentationHeader()).thenReturn(header);
        when(header.getImageInformation()).thenReturn(info);
        when(info.getImageFormat()).thenReturn(0x01);
        when(irisBDIR.getRepresentation()).thenReturn(rep);
        try (org.mockito.MockedStatic<IrisDecoder> irisDecoderMock = org.mockito.Mockito.mockStatic(IrisDecoder.class, org.mockito.Mockito.CALLS_REAL_METHODS)) {
            irisDecoderMock.when(() -> invokeGetIrisBDIRISO19794_6_2011(any(byte[].class), any(Integer.class))).thenReturn(irisBDIR);
            byte[] result = IrisDecoder.convertIrisISOToImageBytes(dto);
            assertArrayEquals(rawBytes, result);
        }
    }

    /**
     * Tests that convertIrisISOToImageBytes returns raw image bytes for unsupported format.
     */
    @Test
    void convertIrisIsoToImageBytesUnsupportedFormatReturnsOriginalImage() throws Exception {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("ISO19794_6_2011");
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getOnlyImageInformation()).thenReturn(0);
        when(dto.getCompressionRatio()).thenReturn(80);
        IrisBDIR irisBDIR = mock(IrisBDIR.class);
        Representation rep = mock(Representation.class);
        RepresentationHeader header = mock(RepresentationHeader.class);
        ImageInformation info = mock(ImageInformation.class);
        RepresentationData repData = mock(RepresentationData.class);
        ImageData imageData = mock(ImageData.class);
        when(irisBDIR.getRepresentation()).thenReturn(rep);
        when(rep.getRepresentationHeader()).thenReturn(header);
        when(header.getImageInformation()).thenReturn(info);
        when(rep.getRepresentationData()).thenReturn(repData);
        when(repData.getImageData()).thenReturn(imageData);
        when(imageData.getImage()).thenReturn(new byte[]{4, 5, 6});
        when(info.getImageFormat()).thenReturn(ImageFormat.MONO_JPEG);
        try (org.mockito.MockedStatic<IrisDecoder> irisDecoderMock = org.mockito.Mockito.mockStatic(IrisDecoder.class, org.mockito.Mockito.CALLS_REAL_METHODS)) {
            irisDecoderMock.when(() -> invokeGetIrisBDIRISO19794_6_2011(any(byte[].class), anyInt())).thenReturn(irisBDIR);
            byte[] result = IrisDecoder.convertIrisISOToImageBytes(dto);
            assertArrayEquals(new byte[]{4, 5, 6}, result);
        }
    }

    /**
     * Tests that convertIrisISO19794_6_2011ToImage successfully converts image data to JPG format.
     */
    @Test
    void convertIrisIso19794ToImageValidInputReturnsJpgImage() throws Exception {
        // Mock the required objects
        byte[] mockImageData = new byte[]{1, 2, 3};
        byte[] expectedJpgData = new byte[]{4, 5, 6};

        // Mock the BDIR and its components
        IrisBDIR irisBDIR = mock(IrisBDIR.class);
        Representation representation = mock(Representation.class);
        RepresentationData repData = mock(RepresentationData.class);
        ImageData imageData = mock(ImageData.class);

        // Set up the mock behavior
        when(irisBDIR.getRepresentation()).thenReturn(representation);
        when(representation.getRepresentationData()).thenReturn(repData);
        when(repData.getImageData()).thenReturn(imageData);
        when(imageData.getImage()).thenReturn(mockImageData);

        // Mock ImageIO to return a test image and capture the output
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        try (org.mockito.MockedStatic<IrisDecoder> irisDecoderMock = org.mockito.Mockito.mockStatic(IrisDecoder.class, org.mockito.Mockito.CALLS_REAL_METHODS);
             org.mockito.MockedStatic<ImageIO> imageIOMock = org.mockito.Mockito.mockStatic(ImageIO.class);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Mock the static method call
            irisDecoderMock.when(() -> invokeGetIrisBDIRISO19794_6_2011(any(byte[].class), anyInt()))
                    .thenReturn(irisBDIR);

            // Mock ImageIO.read to return our test image
            imageIOMock.when(() -> ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(testImage);

            // Mock ImageIO.write to capture the output
            imageIOMock.when(() -> ImageIO.write(any(BufferedImage.class), any(String.class), any(ByteArrayOutputStream.class)))
                    .thenAnswer(invocation -> {
                        ByteArrayOutputStream out = invocation.getArgument(2);
                        out.write(expectedJpgData);
                        return true;
                    });

            // Call the method under test
            byte[] result = invokeConvertIrisISO19794_6_2011ToImage(new byte[]{1, 2, 3});

            // Verify the result
            assertArrayEquals(expectedJpgData, result);
        }
    }

    /**
     * Tests that convertIrisISO19794_6_2011ToImage returns original image data when ImageIO.write fails.
     */
    @Test
    void convertIrisIso19794ToImageWriteFailsReturnsOriginalImage() throws Exception {
        // Mock the required objects
        byte[] originalImageData = new byte[]{1, 2, 3};

        // Mock the BDIR and its components
        IrisBDIR irisBDIR = mock(IrisBDIR.class);
        Representation representation = mock(Representation.class);
        RepresentationData repData = mock(RepresentationData.class);
        ImageData imageData = mock(ImageData.class);

        // Set up the mock behavior
        when(irisBDIR.getRepresentation()).thenReturn(representation);
        when(representation.getRepresentationData()).thenReturn(repData);
        when(repData.getImageData()).thenReturn(imageData);
        when(imageData.getImage()).thenReturn(originalImageData);

        // Mock ImageIO to throw an exception during write
        try (org.mockito.MockedStatic<IrisDecoder> irisDecoderMock = org.mockito.Mockito.mockStatic(IrisDecoder.class, org.mockito.Mockito.CALLS_REAL_METHODS);
             org.mockito.MockedStatic<ImageIO> imageIOMock = org.mockito.Mockito.mockStatic(ImageIO.class)) {

            // Mock the static method call
            irisDecoderMock.when(() -> invokeGetIrisBDIRISO19794_6_2011(any(byte[].class), anyInt()))
                    .thenReturn(irisBDIR);

            // Mock ImageIO.read to return a test image
            BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            imageIOMock.when(() -> ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(testImage);

            // Mock ImageIO.write to throw an exception
            imageIOMock.when(() -> ImageIO.write(any(BufferedImage.class), any(String.class), any(ByteArrayOutputStream.class)))
                    .thenThrow(new IOException("Test exception"));

            // Call the method under test
            byte[] result = invokeConvertIrisISO19794_6_2011ToImage(new byte[]{1, 2, 3});

            // Verify the result is the original image data
            assertArrayEquals(originalImageData, result);
        }
    }

    /**
     * Tests that convertIrisISO19794_6_2011ToImage propagates IOExceptions from ImageIO.read.
     */
    @Test
    void convertIrisIso19794ToImageReadThrowsException() throws Exception {
        try (org.mockito.MockedStatic<ImageIO> imageIOMock = org.mockito.Mockito.mockStatic(ImageIO.class)) {
            // Mock ImageIO.read to throw an exception
            imageIOMock.when(() -> ImageIO.read(any(ByteArrayInputStream.class)))
                    .thenThrow(new IOException("Test read exception"));

            // Call the method under test and verify it throws the expected exception
            assertThrows(Exception.class,
                    () -> invokeConvertIrisISO19794_6_2011ToImage(new byte[]{1, 2, 3}));
        }
    }

    /**
     * Tests that convertIrisISOToImageBytes throws UnsupportedOperationException for unsupported version.
     */
    @Test
    void convertIrisIsoToImageBytesUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("UNSUPPORTED");
        assertThrows(UnsupportedOperationException.class, () -> IrisDecoder.convertIrisISOToImageBytes(dto));
    }

    // Helper method for reflection invocation of getIrisBDIRISO19794_6_2011
    private static IrisBDIR invokeGetIrisBDIRISO19794_6_2011(byte[] isoData, int onlyImageInformation) throws Exception {
        try {
            Method method = IrisDecoder.class.getDeclaredMethod("getIrisBDIRISO19794_6_2011", byte[].class, int.class);
            method.setAccessible(true);
            return (IrisBDIR) method.invoke(null, isoData, onlyImageInformation);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Helper method to invoke the private convertIrisISO19794_6_2011ToImage method using reflection.
     */
    private static byte[] invokeConvertIrisISO19794_6_2011ToImage(byte[] isoData) throws Exception {
        try {
            Method method = IrisDecoder.class.getDeclaredMethod("convertIrisISO19794_6_2011ToImage", byte[].class);
            method.setAccessible(true);
            return (byte[]) method.invoke(null, isoData);
        } catch (Exception e) {
            throw e;
        }
    }
}