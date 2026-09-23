package io.mosip.biometrics.util.face;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Coverage for {@link FaceBDIR}, {@link FeaturePoint}, and {@link LandmarkPointType}.
 */
class FaceBDIRCoverageTest {

    @Test
    void featurePointAndLandmarkPointType() {
        FeaturePoint point = new FeaturePoint(1, 2, 3, 10, 20);
        assertEquals(1, point.getType());
        assertEquals(2, point.getMajorCode());
        assertEquals(3, point.getMinorCode());
        assertEquals(10, point.getX());
        assertEquals(20, point.getY());
        assertTrue(point.toString().contains("2.3"));

        FeaturePoint packed = new FeaturePoint(1, 0x23, 5, 6);
        assertEquals(2, packed.getMajorCode());
        assertEquals(3, packed.getMinorCode());

        LandmarkPointType type = new LandmarkPointType(LandmarkPointType.MPEG4_FEATURE);
        assertEquals(LandmarkPointType.MPEG4_FEATURE, type.value());
        assertEquals(LandmarkPointType.ANTHROPOMETRIC_3D_LANDMARK,
                LandmarkPointType.fromValue(LandmarkPointType.ANTHROPOMETRIC_3D_LANDMARK));
        assertNotNull(type.toString());
        assertThrows(IllegalArgumentException.class, () -> LandmarkPointType.fromValue(0x04));
    }

    @Test
    void faceBdirConstructWriteReadAndGetters() throws Exception {
        FaceQualityBlock[] qualityBlocks = new FaceQualityBlock[]{
                new FaceQualityBlock(40, FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001,
                        FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001)
        };
        FacialInformation facialInformation = new FacialInformation(0, Gender.MALE, EyeColour.BROWN,
                HairColour.BLACK, HeightCodes.UNSPECIFIED, Features.FEATURES_ARE_SPECIFIED, Expression.NEUTRAL,
                new int[]{1, 2, 3}, new int[]{0, 0, 0});
        ImageInformation imageInformation = new ImageInformation(FaceImageType.BASIC, ImageDataType.JPEG,
                64, 64, SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180, 0, CrossReference.BASIC,
                ImageColourSpace.BIT_24_RGB);
        byte[] image = new byte[]{1, 2, 3, 4, 5};

        FaceBDIR bdir = new FaceBDIR(FaceFormatIdentifier.FORMAT_FAC, FaceVersionNumber.VERSION_030,
                FaceCertificationFlag.UNSPECIFIED, TemporalSequenceFlags.ONE_REPRESENTATION,
                FaceCaptureDeviceTechnology.UNSPECIFIED, FaceCaptureDeviceVendor.UNSPECIFIED,
                FaceCaptureDeviceType.UNSPECIFIED, new Date(), 1, qualityBlocks, facialInformation, null,
                imageInformation, image, null);

        assertEquals(FaceFormatIdentifier.FORMAT_FAC, bdir.getFormatIdentifier());
        assertEquals(FaceVersionNumber.VERSION_030, bdir.getVersionNumber());
        assertEquals(FaceCertificationFlag.UNSPECIFIED, bdir.getCertificationFlag());
        assertEquals(TemporalSequenceFlags.ONE_REPRESENTATION, bdir.getTemporalSemantics());
        assertEquals(1, bdir.getNoOfRepresentations());
        assertEquals(Gender.MALE, bdir.getGender());
        assertEquals(EyeColour.BROWN, bdir.getEyeColor());
        assertEquals(HairColour.BLACK, bdir.getHairColor());
        assertEquals(FaceImageType.BASIC, bdir.getFaceImageType());
        assertEquals(ImageDataType.JPEG, bdir.getImageDataType());
        assertEquals(64, bdir.getWidth());
        assertEquals(64, bdir.getHeight());
        assertArrayEquals(image, bdir.getImage());
        assertEquals(image.length, bdir.getImageLength());
        assertNotNull(bdir.getCaptureDateTime());
        assertTrue(bdir.getCaptureYear() > 0);
        assertTrue(bdir.getRecordLength() > 0);
        assertTrue(bdir.getRepresentationsLength() > 0);
        assertNotNull(bdir.getRepresentation());
        assertNotNull(bdir.getRepresentation(0));
        assertNotNull(bdir.getQualityBlocks());
        assertEquals(1, bdir.getNoOfQualityBlocks());
        assertEquals(0, bdir.getNoOfLandMarkPoints());
        assertNotNull(bdir.getPoseAngle());
        assertNotNull(bdir.getPoseAngleUncertainty());
        assertNotNull(bdir.toString());
        assertEquals(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180, bdir.getSpatialSamplingRateLevel());
        assertEquals(CrossReference.BASIC, bdir.getCrossReference());
        assertEquals(ImageColourSpace.BIT_24_RGB, bdir.getImageColorSpace());
        assertEquals(0, bdir.getPostAcquistionProcessing());
        assertEquals(FaceCaptureDeviceTechnology.UNSPECIFIED, bdir.getCaptureDeviceTechnologyIdentifier());
        assertEquals(FaceCaptureDeviceVendor.UNSPECIFIED, bdir.getCaptureDeviceVendorIdentifier());
        assertEquals(FaceCaptureDeviceType.UNSPECIFIED, bdir.getCaptureDeviceTypeIdentifier());
        assertEquals(Features.FEATURES_ARE_SPECIFIED, bdir.getFeaturesMask());
        assertEquals(Expression.NEUTRAL, bdir.getExpressionMask());
        assertEquals(HeightCodes.UNSPECIFIED, bdir.getSubjectHeight());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(baos)) {
            bdir.writeObject(out);
        }
        byte[] bytes = baos.toByteArray();

        FaceBDIR read = new FaceBDIR(new DataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(bdir.getWidth(), read.getWidth());
        assertArrayEquals(image, read.getImage());

        FaceBDIR imageOnly = new FaceBDIR(new DataInputStream(new ByteArrayInputStream(bytes)), true);
        assertNotNull(imageOnly.getGeneralHeader());

        FaceBDIR simple = new FaceBDIR(FaceFormatIdentifier.FORMAT_FAC, FaceVersionNumber.VERSION_030,
                FaceCertificationFlag.UNSPECIFIED, TemporalSequenceFlags.ONE_REPRESENTATION, new Date(), 1,
                qualityBlocks, facialInformation, null, imageInformation, image, null);
        assertNotNull(simple.getRepresentation());
        assertTrue(simple.getRecordLength() > 0);
    }
}
