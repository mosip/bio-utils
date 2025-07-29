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
     * convertIrisImageToISO_withInvalidInputBytes_throwsException
     * Verifies that an exception is thrown when invalid byte data is passed.
     */
    @Test
    void convertIrisImageToISO_withInvalidInputBytes_throwsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setVersion("ISO19794_6_2011");
        dto.setPurpose("Registration");
        dto.setModality("Iris");
        dto.setInputBytes(new byte[]{1, 2, 3});

        assertThrows(Exception.class, () -> IrisEncoder.convertIrisImageToISO(dto));
    }

    /**
     * convertIrisImageToISO_withUnsupportedVersion_throwsUnsupportedOperationException
     * Verifies that an UnsupportedOperationException is thrown for an unsupported version.
     */
    @Test
    void convertIrisImageToISO_withUnsupportedVersion_throwsUnsupportedOperationException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setVersion("OTHER");

        assertThrows(UnsupportedOperationException.class, () -> IrisEncoder.convertIrisImageToISO(dto));
    }

    /**
     * convertIrisImageToISO_withValidInputs_returnsISOByteArray
     * Verifies successful conversion to ISO byte array when valid inputs are provided.
     */
    @Test
    void convertIrisImageToISO_withValidInputs_returnsISOByteArray() throws Exception {
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
     * getEyeLabel_withRight_returnsRightEyeLabel
     */
    @Test
    void getEyeLabel_withRight_returnsRightEyeLabel() {
        byte label = invokeGetEyeLabel("Right");
        assertEquals(EyeLabel.RIGHT, label);
    }

    /**
     * getEyeLabel_withLeft_returnsLeftEyeLabel
     */
    @Test
    void getEyeLabel_withLeft_returnsLeftEyeLabel() {
        byte label = invokeGetEyeLabel("Left");
        assertEquals(EyeLabel.LEFT, label);
    }

    /**
     * getEyeLabel_withNull_returnsUnspecifiedEyeLabel
     */
    @Test
    void getEyeLabel_withNull_returnsUnspecifiedEyeLabel() {
        byte label = invokeGetEyeLabel(null);
        assertEquals(EyeLabel.UNSPECIFIED, label);
    }

    /**
     * getEyeLabel_withUnknown_returnsUnspecifiedEyeLabel
     */
    @Test
    void getEyeLabel_withUnknown_returnsUnspecifiedEyeLabel() {
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