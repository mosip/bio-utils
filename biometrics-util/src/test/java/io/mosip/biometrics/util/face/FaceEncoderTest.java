package io.mosip.biometrics.util.face;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link FaceEncoder} utility class.
 * Tests image-to-ISO conversion with expected failure scenarios.
 */
class FaceEncoderTest {

    /**
     * Test converting dummy (non-image) byte array to ISO format.
     * Expects a generic exception due to invalid image data.
     */
    @Test
    void convertFaceImageToISO_dummyBytes_throwsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setVersion("ISO19794_5_2011");
        dto.setPurpose("Registration");
        dto.setModality("Face");
        dto.setInputBytes(new byte[]{1, 2, 3});

        assertThrows(Exception.class, () -> FaceEncoder.convertFaceImageToISO(dto));
    }

    /**
     * Test converting image to ISO using an unsupported version string.
     * Expects UnsupportedOperationException to be thrown.
     */
    @Test
    void convertFaceImageToISO_unsupportedVersion_throwsException() {
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setVersion("OTHER");
        dto.setPurpose("Registration");
        dto.setModality("Face");
        dto.setInputBytes(new byte[]{1});

        assertThrows(UnsupportedOperationException.class, () -> FaceEncoder.convertFaceImageToISO(dto));
    }
}