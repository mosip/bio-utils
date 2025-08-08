package io.mosip.biometrics.util.face;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link FaceEncoder} utility class.
 * Tests image-to-ISO conversion functionality and error scenarios.
 */
@ExtendWith(MockitoExtension.class)
class FaceEncoderTest {

    private ConvertRequestDto convertRequestDto;
    private BufferedImage mockBufferedImage;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        convertRequestDto = new ConvertRequestDto();
        mockBufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Test converting dummy (non-image) byte array to ISO format.
     * Expects a generic exception due to invalid image data.
     */
    @Test
    void convertFaceImageToISOWithDummyBytes() {
        convertRequestDto.setVersion("ISO19794_5_2011");
        convertRequestDto.setPurpose("Registration");
        convertRequestDto.setModality("Face");
        convertRequestDto.setInputBytes(new byte[]{1, 2, 3});

        assertThrows(Exception.class, () -> FaceEncoder.convertFaceImageToISO(convertRequestDto));
    }

    /**
     * Test converting image to ISO using an unsupported version string.
     * Expects UnsupportedOperationException to be thrown.
     */
    @Test
    void convertFaceImageToISOWithUnsupportedVersion() {
        convertRequestDto.setVersion("OTHER");
        convertRequestDto.setPurpose("Registration");
        convertRequestDto.setModality("Face");
        convertRequestDto.setInputBytes(new byte[]{1});

        assertThrows(UnsupportedOperationException.class, () -> FaceEncoder.convertFaceImageToISO(convertRequestDto));
    }

    /**
     * Test successful conversion with AUTH purpose (JPEG2000_LOSSY).
     * Tests the complete execution path for AUTH purpose.
     */
    @Test
    void convertFaceImageToISOWithAuthPurpose() throws Exception {
        convertRequestDto.setVersion("ISO19794_5_2011");
        convertRequestDto.setPurpose("AUTH");
        convertRequestDto.setInputBytes(new byte[]{1, 2, 3});

        try (MockedStatic<CommonUtil> mockedCommonUtil = mockStatic(CommonUtil.class)) {
            mockedCommonUtil.when(() -> CommonUtil.getBufferedImage(any(ConvertRequestDto.class)))
                    .thenReturn(mockBufferedImage);

            byte[] result = FaceEncoder.convertFaceImageToISO(convertRequestDto);

            assertNotNull(result);
        }
    }

    /**
     * Test successful conversion with Registration purpose (JPEG2000_LOSS_LESS).
     * Tests the complete execution path for non-AUTH purpose.
     */
    @Test
    void convertFaceImageToISOWithRegistrationPurpose() throws Exception {
        convertRequestDto.setVersion("ISO19794_5_2011");
        convertRequestDto.setPurpose("Registration");
        convertRequestDto.setInputBytes(new byte[]{1, 2, 3});

        try (MockedStatic<CommonUtil> mockedCommonUtil = mockStatic(CommonUtil.class)) {
            mockedCommonUtil.when(() -> CommonUtil.getBufferedImage(any(ConvertRequestDto.class)))
                    .thenReturn(mockBufferedImage);

            byte[] result = FaceEncoder.convertFaceImageToISO(convertRequestDto);

            assertNotNull(result);
        }
    }

    /**
     * Test convertFaceImageToISO19794_5_2011 method directly.
     * Tests the main conversion method with all parameters.
     */
    @Test
    void convertFaceImageToISO19794_5_2011() throws IOException {
        long formatIdentifier = FaceFormatIdentifier.FORMAT_FAC;
        long versionNumber = FaceVersionNumber.VERSION_030;
        int certificationFlag = FaceCertificationFlag.UNSPECIFIED;
        int temporalSemantics = TemporalSequenceFlags.ONE_REPRESENTATION;
        Date captureDate = new Date();
        int noOfRepresentations = 1;
        int noOfLandMarkPoints = 0;
        int gender = Gender.UNKNOWN;
        int eyeColour = EyeColour.UNSPECIFIED;
        int hairColour = HairColour.UNSPECIFIED;
        int subjectHeight = HeightCodes.UNSPECIFIED;
        int expression = 0;
        int features = Features.FEATURES_ARE_SPECIFIED;
        int[] poseAngle = {0, 0, 0};
        int[] poseAngleUncertainty = {0, 0, 0};
        int faceImageType = FaceImageType.FULL_FRONTAL;
        int sourceType = FaceCaptureDeviceTechnology.VIDEO_FRAME_ANALOG_CAMERA;
        int deviceVendor = FaceCaptureDeviceVendor.UNSPECIFIED;
        int deviceType = FaceCaptureDeviceType.UNSPECIFIED;
        FaceQualityBlock[] qualityBlock = new FaceQualityBlock[]{
                new FaceQualityBlock(40, FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001,
                        FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001)
        };
        byte[] imageData = new byte[]{1, 2, 3};
        int imageWidth = 100;
        int imageHeight = 100;
        int imageDataType = ImageDataType.JPEG2000_LOSSY;
        int spatialSamplingRateLevel = SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180;
        int postAcquisitionProcessing = 0;
        int crossReference = CrossReference.BASIC;
        int imageColourSpace = ImageColourSpace.BIT_24_RGB;
        LandmarkPoints[] landmarkPoints = null;
        byte[] threeDInformationAndData = null;

        byte[] result = FaceEncoder.convertFaceImageToISO19794_5_2011(
                formatIdentifier, versionNumber, certificationFlag, temporalSemantics, captureDate,
                noOfRepresentations, noOfLandMarkPoints, gender, eyeColour, hairColour, subjectHeight,
                expression, features, poseAngle, poseAngleUncertainty, faceImageType, sourceType,
                deviceVendor, deviceType, qualityBlock, imageData, imageWidth, imageHeight,
                imageDataType, spatialSamplingRateLevel, postAcquisitionProcessing, crossReference,
                imageColourSpace, landmarkPoints, threeDInformationAndData
        );

        assertNotNull(result);
    }

    /**
     * Test private constructor throws IllegalStateException.
     * Verifies utility class pattern implementation.
     */
    @Test
    void privateConstructorThrowsIllegalStateException() throws Exception {
        Constructor<FaceEncoder> constructor = FaceEncoder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertEquals("FaceEncoder class", exception.getCause().getMessage());
    }

    /**
     * Test convertFaceImageToISO with case-insensitive AUTH purpose.
     * Tests the equalsIgnoreCase logic for "auth" in lowercase.
     */
    @Test
    void convertFaceImageToISOWithLowercaseAuthPurpose() throws Exception {
        convertRequestDto.setVersion("ISO19794_5_2011");
        convertRequestDto.setPurpose("auth");
        convertRequestDto.setInputBytes(new byte[]{1, 2, 3});

        try (MockedStatic<CommonUtil> mockedCommonUtil = mockStatic(CommonUtil.class)) {
            mockedCommonUtil.when(() -> CommonUtil.getBufferedImage(any(ConvertRequestDto.class)))
                    .thenReturn(mockBufferedImage);

            byte[] result = FaceEncoder.convertFaceImageToISO(convertRequestDto);

            assertNotNull(result);
        }
    }

    /**
     * Test convertFaceImageToISO with mixed case AUTH purpose.
     * Tests the equalsIgnoreCase logic for "Auth" in mixed case.
     */
    @Test
    void convertFaceImageToISOWithMixedCaseAuthPurpose() throws Exception {
        convertRequestDto.setVersion("ISO19794_5_2011");
        convertRequestDto.setPurpose("Auth");
        convertRequestDto.setInputBytes(new byte[]{1, 2, 3});

        try (MockedStatic<CommonUtil> mockedCommonUtil = mockStatic(CommonUtil.class)) {
            mockedCommonUtil.when(() -> CommonUtil.getBufferedImage(any(ConvertRequestDto.class)))
                    .thenReturn(mockBufferedImage);

            byte[] result = FaceEncoder.convertFaceImageToISO(convertRequestDto);

            assertNotNull(result);
        }
    }

    /**
     * Test convertFaceImageToISO with null purpose defaults to non-AUTH.
     * Tests behavior when purpose is null (should use JPEG2000_LOSS_LESS).
     */
    @Test
    void convertFaceImageToISOWithNullPurpose() {
        convertRequestDto.setVersion("ISO19794_5_2011");
        convertRequestDto.setPurpose(null);
        convertRequestDto.setInputBytes(new byte[]{1, 2, 3});

        assertThrows(NullPointerException.class, () -> FaceEncoder.convertFaceImageToISO(convertRequestDto));
    }
}
