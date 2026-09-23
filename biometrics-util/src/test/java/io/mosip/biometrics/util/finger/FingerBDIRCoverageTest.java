package io.mosip.biometrics.util.finger;

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
 * Coverage for {@link FingerBDIR}.
 */
class FingerBDIRCoverageTest {

    @Test
    void constructWriteReadAndGetters() throws Exception {
        FingerQualityBlock[] qualityBlocks = new FingerQualityBlock[]{new FingerQualityBlock(80)};
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[]{};
        byte[] image = new byte[]{9, 8, 7, 6};

        FingerBDIR bdir = new FingerBDIR(FingerFormatIdentifier.FORMAT_FIR, FingerVersionNumber.VERSION_020,
                FingerCertificationFlag.UNSPECIFIED, FingerCaptureDeviceTechnology.UNSPECIFIED,
                FingerCaptureDeviceVendor.UNSPECIFIED, FingerCaptureDeviceType.UNSPECIFIED, new Date(), 1,
                qualityBlocks, certificationBlocks, FingerPosition.RIGHT_INDEX_FINGER, 1,
                FingerScaleUnitType.PIXELS_PER_INCH, 500, 500, 500, 500, 8,
                FingerImageCompressionType.JPEG_LOSSY, FingerImpressionType.LIVE_SCAN_PLAIN, 100, 200, 1, image,
                null, null, null);

        assertEquals(FingerFormatIdentifier.FORMAT_FIR, bdir.getFormatIdentifier());
        assertEquals(FingerVersionNumber.VERSION_020, bdir.getVersionNumber());
        assertEquals(FingerCertificationFlag.UNSPECIFIED, bdir.getCertificationFlag());
        assertEquals(1, bdir.getNoOfRepresentations());
        assertEquals(FingerPosition.RIGHT_INDEX_FINGER, bdir.getFingerPosition());
        assertEquals(FingerImageCompressionType.JPEG_LOSSY, bdir.getCompressionType());
        assertEquals(8, bdir.getBitDepth());
        assertEquals(100, bdir.getLineLengthHorizontal());
        assertEquals(200, bdir.getLineLengthVertical());
        assertEquals(1, bdir.getNoOfFingerPresent());
        assertArrayEquals(image, bdir.getImage());
        assertEquals(image.length, bdir.getImageLength());
        assertTrue(bdir.getRecordLength() > 0);
        assertTrue(bdir.getRepresentationsLength() > 0);
        assertNotNull(bdir.getCaptureDateTime());
        assertTrue(bdir.getCaptureYear() > 0);
        assertNotNull(bdir.getQualityBlocks());
        assertEquals(1, bdir.getNoOfQualityBlocks());
        assertNotNull(bdir.getCertificationBlocks());
        assertEquals(0, bdir.getNoOfCertificationBlocks());
        assertEquals(1, bdir.getRepresentationNo());
        assertEquals(FingerScaleUnitType.PIXELS_PER_INCH, bdir.getScaleUnits());
        assertEquals(500, bdir.getCaptureDeviceSpatialSamplingRateHorizontal());
        assertEquals(500, bdir.getCaptureDeviceSpatialSamplingRateVertical());
        assertEquals(500, bdir.getImageSpatialSamplingRateHorizontal());
        assertEquals(500, bdir.getImageSpatialSamplingRateVertical());
        assertEquals(FingerImpressionType.LIVE_SCAN_PLAIN, bdir.getImpressionType());
        assertEquals(FingerCaptureDeviceTechnology.UNSPECIFIED, bdir.getCaptureDeviceTechnologyIdentifier());
        assertEquals(FingerCaptureDeviceVendor.UNSPECIFIED, bdir.getCaptureDeviceVendorIdentifier());
        assertEquals(FingerCaptureDeviceType.UNSPECIFIED, bdir.getCaptureDeviceTypeIdentifier());
        assertNotNull(bdir.getRepresentation());
        assertNotNull(bdir.getRepresentation(0));
        assertNotNull(bdir.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(baos)) {
            bdir.writeObject(out);
        }
        byte[] bytes = baos.toByteArray();

        FingerBDIR read = new FingerBDIR(new DataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(bdir.getFingerPosition(), read.getFingerPosition());
        assertArrayEquals(image, read.getImage());

        FingerBDIR imageOnly = new FingerBDIR(new DataInputStream(new ByteArrayInputStream(bytes)), true);
        assertNotNull(imageOnly.getGeneralHeader());

        FingerBDIR simple = new FingerBDIR(FingerFormatIdentifier.FORMAT_FIR, FingerVersionNumber.VERSION_020,
                FingerCertificationFlag.UNSPECIFIED, new Date(), 1, qualityBlocks, certificationBlocks,
                FingerPosition.LEFT_THUMB, 1, FingerScaleUnitType.PIXELS_PER_INCH, 1, image);
        assertNotNull(simple.getRepresentation());
        assertTrue(simple.getRecordLength() > 0);
    }
}
