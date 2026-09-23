package io.mosip.biometrics.util.iris;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Coverage for {@link IrisBDIR}.
 */
class IrisBDIRCoverageTest {

    @Test
    void constructWriteReadAndGetters() throws Exception {
        IrisQualityBlock[] qualityBlocks = new IrisQualityBlock[]{new IrisQualityBlock(75)};
        ImageInformation imageInformation = new ImageInformation(EyeLabel.RIGHT, ImageType.UNCROPPED,
                ImageFormat.MONO_JPEG, Orientation.UNDEFINED, Orientation.UNDEFINED,
                IrisImageCompressionType.UNDEFINED, 200, 150, IrisImageBitDepth.BPP_08, IrisRange.UNASSIGNED,
                IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED, IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED,
                0, 0, 0, 0, 0, 0);
        byte[] image = new byte[]{5, 4, 3, 2, 1};

        IrisBDIR bdir = new IrisBDIR(IrisFormatIdentifier.FORMAT_IIR, IrisVersionNumber.VERSION_020,
                IrisCertificationFlag.UNSPECIFIED, IrisCaptureDeviceTechnology.UNSPECIFIED,
                IrisCaptureDeviceVendor.UNSPECIFIED, IrisCaptureDeviceType.UNSPECIFIED, new Date(), 1,
                qualityBlocks, imageInformation, 1, 1, image);

        assertEquals(IrisFormatIdentifier.FORMAT_IIR, bdir.getFormatIdentifier());
        assertEquals(IrisVersionNumber.VERSION_020, bdir.getVersionNumber());
        assertEquals(IrisCertificationFlag.UNSPECIFIED, bdir.getCertificationFlag());
        assertEquals(1, bdir.getNoOfRepresentations());
        assertEquals(1, bdir.getRepresentationNo());
        assertEquals(1, bdir.getNoOfEyesPresent());
        assertEquals(EyeLabel.RIGHT, bdir.getEyeLabel());
        assertEquals(ImageType.UNCROPPED, bdir.getImageType());
        assertEquals(ImageFormat.MONO_JPEG, bdir.getImageFormat());
        assertEquals(200, bdir.getWidth());
        assertEquals(150, bdir.getHeight());
        assertEquals(IrisImageBitDepth.BPP_08, bdir.getBitDepth());
        assertArrayEquals(image, bdir.getImage());
        assertTrue(bdir.getRecordLength() > 0);
        assertTrue(bdir.getRepresentationsLength() > 0);
        assertNotNull(bdir.getCaptureDateTime());
        assertTrue(bdir.getCaptureYear() > 0);
        assertNotNull(bdir.getQualityBlocks());
        assertEquals(Orientation.UNDEFINED, bdir.getHorizontalOrientation());
        assertEquals(Orientation.UNDEFINED, bdir.getVerticalOrientation());
        assertEquals(IrisImageCompressionType.UNDEFINED, bdir.getCompressionType());
        assertEquals(IrisRange.UNASSIGNED, bdir.getRange());
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED, bdir.getRollAngleOfEye());
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED, bdir.getRollAngleUncertainty());
        assertEquals(0, bdir.getIrisCenterSmallestX());
        assertEquals(0, bdir.getIrisCenterLargestX());
        assertEquals(0, bdir.getIrisCenterSmallestY());
        assertEquals(0, bdir.getIrisCenterLargestY());
        assertEquals(0, bdir.getIrisDiameterSmallest());
        assertEquals(0, bdir.getIrisDiameterLargest());
        assertEquals(IrisCaptureDeviceTechnology.UNSPECIFIED, bdir.getCaptureDeviceTechnologyIdentifier());
        assertEquals(IrisCaptureDeviceVendor.UNSPECIFIED, bdir.getCaptureDeviceVendorIdentifier());
        assertEquals(IrisCaptureDeviceType.UNSPECIFIED, bdir.getCaptureDeviceTypeIdentifier());
        assertNotNull(bdir.getRepresentation());
        assertNotNull(bdir.getRepresentation(0));
        assertNotNull(bdir.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(baos)) {
            bdir.writeObject(out);
        }
        byte[] bytes = baos.toByteArray();

        IrisBDIR read = new IrisBDIR(new DataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(bdir.getEyeLabel(), read.getEyeLabel());
        assertArrayEquals(image, read.getImage());

        IrisBDIR imageOnly = new IrisBDIR(new DataInputStream(new ByteArrayInputStream(bytes)), true);
        assertNotNull(imageOnly.getGeneralHeader());

        IrisBDIR simple = new IrisBDIR(IrisFormatIdentifier.FORMAT_IIR, IrisVersionNumber.VERSION_020,
                IrisCertificationFlag.UNSPECIFIED, new Date(), 1, qualityBlocks, imageInformation, 1, 1, image);
        assertNotNull(simple.getRepresentation());
        assertTrue(simple.getRecordLength() > 0);
    }
}
