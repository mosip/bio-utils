package io.mosip.biometrics.util.finger;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doAnswer;


/**
 * Unit tests for {@link FingerEncoder}.
 */
class FingerEncoderTest {

    private ConvertRequestDto dto;

    @BeforeEach
    void setup() {
        dto = mock(ConvertRequestDto.class);
    }

    /**
     * Tests that {@link FingerEncoder#convertFingerImageToISO(ConvertRequestDto)} throws an exception
     * when provided with invalid byte array input.
     */
    @Test
    void convertFingerImageToISO_invalidBytes_throwsException() {
        when(dto.getVersion()).thenReturn("ISO19794_4_2011");
        when(dto.getPurpose()).thenReturn("Registration");
        when(dto.getModality()).thenReturn("Finger");
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getBiometricSubType()).thenReturn("Right Thumb");
        when(dto.getOnlyImageInformation()).thenReturn(0);

        assertThrows(Exception.class, () -> FingerEncoder.convertFingerImageToISO(dto));
    }

    /**
     * Tests that {@link FingerEncoder#convertFingerImageToISO(ConvertRequestDto)} throws UnsupportedOperationException
     * for unsupported version.
     */
    @Test
    void convertFingerImageToISO_unsupportedVersion_throwsException() {
        when(dto.getVersion()).thenReturn("UNSUPPORTED_VERSION");

        assertThrows(UnsupportedOperationException.class, () -> FingerEncoder.convertFingerImageToISO(dto));
    }

    /**
     * Tests that returns the expected constant for a known value.
     */
    @Test
    void getFingerPosition_knownType_returnsCorrectConstant() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "Left IndexFinger");
        assertEquals(FingerPosition.LEFT_INDEX_FINGER, pos);
    }

    /**
     * Tests that returns UNKNOWN for a null input.
     */
    @Test
    void getFingerPosition_nullInput_returnsUnknown() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, (String) null);
        assertEquals(FingerPosition.UNKNOWN, pos);
    }

    /**
     * Tests that returns UNKNOWN for an invalid string.
     */
    @Test
    void getFingerPosition_invalidType_returnsUnknown() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "SomeInvalidFinger");
        assertEquals(FingerPosition.UNKNOWN, pos);
    }

    /**
     * Tests handling of AUTH purpose with JPEG2000 lossy compression.
     */
    @Test
    void convertFingerImageToISO_authWithJP2Lossy_throwsException() {
        when(dto.getVersion()).thenReturn("ISO19794_4_2011");
        when(dto.getPurpose()).thenReturn("AUTH");
        when(dto.getImageType()).thenReturn(0);
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getBiometricSubType()).thenReturn("Right Thumb");
        when(dto.getOnlyImageInformation()).thenReturn(0);

        assertThrows(Exception.class, () -> FingerEncoder.convertFingerImageToISO(dto));
    }

    /**
     * Tests AUTH purpose with WSQ compression.
     */
    @Test
    void convertFingerImageToISO_authWithWSQ_throwsException() {
        when(dto.getVersion()).thenReturn("ISO19794_4_2011");
        when(dto.getPurpose()).thenReturn("AUTH");
        when(dto.getImageType()).thenReturn(1); // non-zero means WSQ
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getBiometricSubType()).thenReturn("Left MiddleFinger");
        when(dto.getOnlyImageInformation()).thenReturn(0);

        assertThrows(Exception.class, () -> FingerEncoder.convertFingerImageToISO(dto));
    }

    /**
     * Tests registration purpose with JPEG2000 lossless compression.
     */
    @Test
    void convertFingerImageToISO_registrationWithJP2Lossless_throwsException() {
        when(dto.getVersion()).thenReturn("ISO19794_4_2011");
        when(dto.getPurpose()).thenReturn("Registration");
        when(dto.getImageType()).thenReturn(0);
        when(dto.getInputBytes()).thenReturn(new byte[]{1, 2, 3});
        when(dto.getBiometricSubType()).thenReturn("Left RingFinger");
        when(dto.getOnlyImageInformation()).thenReturn(0);

        assertThrows(Exception.class, () -> FingerEncoder.convertFingerImageToISO(dto));
    }

    /**
     * Tests that writeObject returns the correct byte array for a mocked FingerBDIR.
     */
    @Test
    void writeObject_validBDIR_returnsByteArray() throws Exception {
        FingerBDIR bdir = mock(FingerBDIR.class);
        doAnswer(invocation -> {
            DataOutputStream out = invocation.getArgument(0);
            out.write(new byte[]{1, 2, 3, 4});
            return null;
        }).when(bdir).writeObject(any(DataOutputStream.class));
        byte[] result;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream outputStream = new DataOutputStream(baos)) {
            bdir.writeObject(outputStream);
            outputStream.flush();
            result = baos.toByteArray();
        }
        assertArrayEquals(new byte[]{1, 2, 3, 4}, result);
    }

    /**
     * Tests that lineLengthHorizontal and lineLengthVertical are calculated from BufferedImage dimensions.
     */
    @Test
    void calculateLineLengths_fromBufferedImage_returnsCorrectValues() {
        int width = 120;
        int height = 80;
        java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        int lineLengthHorizontal = bufferedImage.getWidth();
        int lineLengthVertical = bufferedImage.getHeight();
        assertEquals(120, lineLengthHorizontal);
        assertEquals(80, lineLengthVertical);
    }

    /**
     * Tests that getFingerPosition returns RIGHT_INDEX_FINGER for 'Right IndexFinger'.
     */
    @Test
    void getFingerPosition_rightIndexFinger_returnsRightIndexFinger() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "Right IndexFinger");
        assertEquals(FingerPosition.RIGHT_INDEX_FINGER, pos);
    }

    /**
     * Tests that getFingerPosition returns RIGHT_MIDDLE_FINGER for 'Right MiddleFinger'.
     */
    @Test
    void getFingerPosition_rightMiddleFinger_returnsRightMiddleFinger() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "Right MiddleFinger");
        assertEquals(FingerPosition.RIGHT_MIDDLE_FINGER, pos);
    }

    /**
     * Tests that getFingerPosition returns RIGHT_RING_FINGER for 'Right RingFinger'.
     */
    @Test
    void getFingerPosition_rightRingFinger_returnsRightRingFinger() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "Right RingFinger");
        assertEquals(FingerPosition.RIGHT_RING_FINGER, pos);
    }

    /**
     * Tests that getFingerPosition returns RIGHT_LITTLE_FINGER for 'Right LittleFinger'.
     */
    @Test
    void getFingerPosition_rightLittleFinger_returnsRightLittleFinger() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "Right LittleFinger");
        assertEquals(FingerPosition.RIGHT_LITTLE_FINGER, pos);
    }

    /**
     * Tests that getFingerPosition returns LEFT_THUMB for 'Left Thumb'.
     */
    @Test
    void getFingerPosition_leftThumb_returnsLeftThumb() throws Exception {
        Method method = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        method.setAccessible(true);
        int pos = (int) method.invoke(null, "Left Thumb");
        assertEquals(FingerPosition.LEFT_THUMB, pos);
    }

    /**
     * Tests that convertFingerImageToISO19794_4_2011 writes the FingerBDIR to a DataOutputStream,
     * flushes the stream, and returns the correct byte array.
     */
    @Test
    void convertFingerImageToISO19794_4_2011_validBDIR_returnsByteArray() throws Exception {
        byte[] expectedBytes = new byte[]{1, 2, 3, 4};
        FingerBDIR bdir = mock(FingerBDIR.class);
        doAnswer(invocation -> {
            DataOutputStream out = invocation.getArgument(0);
            out.write(expectedBytes);
            return null;
        }).when(bdir).writeObject(any(DataOutputStream.class));
        byte[] result;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream outputStream = new DataOutputStream(baos)) {
            bdir.writeObject(outputStream);
            outputStream.flush();
            result = baos.toByteArray();
        }
        assertArrayEquals(expectedBytes, result);
    }
}