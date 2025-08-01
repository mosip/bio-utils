package io.mosip.biometrics.util.iris;

import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link IrisEncoder}.
 */
class IrisEncoderTest {

    /**
     * Verifies that an exception is thrown when invalid byte data is passed.
     */
    @Test
    void convertIrisImageToIsoInvalidBytesThrowsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setVersion("ISO19794_6_2011");
        dto.setPurpose("Registration");
        dto.setModality("Iris");
        dto.setInputBytes(new byte[]{1, 2, 3});

        assertThrows(Exception.class, () -> IrisEncoder.convertIrisImageToISO(dto));
    }

    /**
     * Verifies that an UnsupportedOperationException is thrown for an unsupported version.
     */
    @Test
    void convertIrisImageToIsoUnsupportedVersionThrowsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setVersion("OTHER");

        assertThrows(UnsupportedOperationException.class, () -> IrisEncoder.convertIrisImageToISO(dto));
    }

    /**
     * Verifies successful conversion to ISO byte array when valid inputs are provided.
     */
    @Test
    void convertIrisImageToIsoValidInputsReturnsIsoByteArray() throws Exception {
        ConvertRequestDto dto = mock(ConvertRequestDto.class);
        when(dto.getVersion()).thenReturn("ISO19794_6_2011");
        when(dto.getPurpose()).thenReturn("Registration");
        when(dto.getModality()).thenReturn("Iris");
        when(dto.getBiometricSubType()).thenReturn("Right");
        when(dto.getInputBytes()).thenReturn(new byte[100]);

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);

        try (MockedStatic<CommonUtil> mockedCommonUtil = mockStatic(CommonUtil.class)) {
            mockedCommonUtil.when(() -> CommonUtil.getBufferedImage(dto)).thenReturn(image);
            byte[] result = IrisEncoder.convertIrisImageToISO(dto);
            assertNotNull(result);
            assertTrue(result.length > 0);
        }
    }

    /**
     * Tests getEyeLabel with Right input returns right eye label.
     */
    @Test
    void getEyeLabelWithRightReturnsRightEyeLabel() {
        byte label = invokeGetEyeLabel("Right");
        assertEquals(EyeLabel.RIGHT, label);
    }

    /**
     * Tests getEyeLabel with Left input returns left eye label.
     */
    @Test
    void getEyeLabelWithLeftReturnsLeftEyeLabel() {
        byte label = invokeGetEyeLabel("Left");
        assertEquals(EyeLabel.LEFT, label);
    }

    /**
     * Tests getEyeLabel with null input returns unspecified eye label.
     */
    @Test
    void getEyeLabelWithNullReturnsUnspecifiedEyeLabel() {
        byte label = invokeGetEyeLabel(null);
        assertEquals(EyeLabel.UNSPECIFIED, label);
    }

    /**
     * Tests getEyeLabel with unknown input returns unspecified eye label.
     */
    @Test
    void getEyeLabelWithUnknownReturnsUnspecifiedEyeLabel() {
        byte label = invokeGetEyeLabel("Unknown");
        assertEquals(EyeLabel.UNSPECIFIED, label);
    }

    // Utility to access private method via reflection
    private byte invokeGetEyeLabel(String subtype) {
        try {
            var method = IrisEncoder.class.getDeclaredMethod("getEyeLabel", String.class);
            method.setAccessible(true);
            return (byte) method.invoke(null, subtype);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}