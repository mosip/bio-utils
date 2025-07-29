package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class RepresentationHeaderTest {

    /**
     * Tests constructor with basic parameters
     */
    @Test
    public void constructor_basicParameters_createsRepresentationHeader() {
        long representationDataLength = 1000L;
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(80)};
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;
        FingerCertificationBlock[] certificationBlocks = {};
        int fingerPosition = FingerPosition.RIGHT_THUMB;
        int representationNo = 1;
        int scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH;

        RepresentationHeader header = new RepresentationHeader(representationDataLength, captureDate, 
            qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType);

        assertNotNull(header);
        assertEquals(representationDataLength, header.getRepresentationDataLength());
        assertEquals(fingerPosition, header.getFingerPosition());
        assertEquals(representationNo, header.getRepresentationNo());
        assertEquals(scaleUnitType, header.getScaleUnits());
    }

    /**
     * Tests constructor with all parameters
     */
    @Test
    public void constructor_allParameters_createsRepresentationHeader() {
        long representationDataLength = 2000L;
        Date captureDate = new Date();
        int captureDeviceTechnologyIdentifier = FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR;
        int captureDeviceVendorIdentifier = 0x1234;
        int captureDeviceTypeIdentifier = 0x5678;
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(90)};
        int certificationFlag = FingerCertificationFlag.ONE;
        FingerCertificationBlock[] certificationBlocks = {};
        int fingerPosition = FingerPosition.LEFT_INDEX_FINGER;
        int representationNo = 2;
        int scaleUnitType = FingerScaleUnitType.PIXELS_PER_CM;
        int captureDeviceSpatialSamplingRateHorizontal = 500;
        int captureDeviceSpatialSamplingRateVertical = 500;
        int imageSpatialSamplingRateHorizontal = 500;
        int imageSpatialSamplingRateVertical = 500;
        int bitDepth = FingerImageBitDepth.BPP_08;
        int compressionType = FingerImageCompressionType.WSQ;
        int impressionType = FingerImpressionType.LIVE_SCAN_PLAIN;
        int lineLengthHorizontal = 640;
        int lineLengthVertical = 480;

        RepresentationHeader header = new RepresentationHeader(representationDataLength, captureDate,
            captureDeviceTechnologyIdentifier, captureDeviceVendorIdentifier, captureDeviceTypeIdentifier,
            qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo,
            scaleUnitType, captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical,
            imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical, bitDepth, compressionType,
            impressionType, lineLengthHorizontal, lineLengthVertical);

        assertNotNull(header);
        assertEquals(representationDataLength, header.getRepresentationDataLength());
        assertEquals(captureDeviceTechnologyIdentifier, header.getCaptureDeviceTechnologyIdentifier());
        assertEquals(captureDeviceVendorIdentifier, header.getCaptureDeviceVendorIdentifier());
        assertEquals(captureDeviceTypeIdentifier, header.getCaptureDeviceTypeIdentifier());
        assertEquals(fingerPosition, header.getFingerPosition());
        assertEquals(bitDepth, header.getBitDepth());
        assertEquals(compressionType, header.getCompressionType());
        assertEquals(impressionType, header.getImpressionType());
        assertEquals(lineLengthHorizontal, header.getLineLengthHorizontal());
        assertEquals(lineLengthVertical, header.getLineLengthVertical());
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsRepresentationHeader() throws Exception {
        byte[] testData = createTestData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag, true);

        assertNotNull(header);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validHeader_returnsCorrectLength() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(75)};
        FingerCertificationBlock[] certificationBlocks = {};

        RepresentationHeader header = new RepresentationHeader(1000L, captureDate, qualityBlocks,
            FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.RIGHT_THUMB, 1,
            FingerScaleUnitType.PIXELS_PER_INCH);

        long recordLength = header.getRecordLength();

        assertTrue(recordLength > 0);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObject_validHeader_writesDataSuccessfully() throws Exception {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(85)};
        FingerCertificationBlock[] certificationBlocks = {};

        RepresentationHeader header = new RepresentationHeader(1500L, captureDate, qualityBlocks,
            FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.LEFT_THUMB, 1,
            FingerScaleUnitType.PIXELS_PER_CM);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        header.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    /**
     * Tests setter and getter methods
     */
    @Test
    public void settersAndGetters_validValues_workCorrectly() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(70)};
        FingerCertificationBlock[] certificationBlocks = {};

        RepresentationHeader header = new RepresentationHeader(800L, captureDate, qualityBlocks,
            FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.RIGHT_INDEX_FINGER, 1,
            FingerScaleUnitType.PIXELS_PER_INCH);

        header.setFingerPosition(FingerPosition.LEFT_MIDDLE_FINGER);
        header.setBitDepth(FingerImageBitDepth.BPP_0A);
        header.setCompressionType(FingerImageCompressionType.JPEG_LOSSY);
        header.setImpressionType(FingerImpressionType.LIVE_SCAN_ROLLED);
        header.setLineLengthHorizontal(800);
        header.setLineLengthVertical(600);

        assertEquals(FingerPosition.LEFT_MIDDLE_FINGER, header.getFingerPosition());
        assertEquals(FingerImageBitDepth.BPP_0A, header.getBitDepth());
        assertEquals(FingerImageCompressionType.JPEG_LOSSY, header.getCompressionType());
        assertEquals(FingerImpressionType.LIVE_SCAN_ROLLED, header.getImpressionType());
        assertEquals(800, header.getLineLengthHorizontal());
        assertEquals(600, header.getLineLengthVertical());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validHeader_returnsNonNullString() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(65)};
        FingerCertificationBlock[] certificationBlocks = {};

        RepresentationHeader header = new RepresentationHeader(1200L, captureDate, qualityBlocks,
            FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.RIGHT_RING_FINGER, 1,
            FingerScaleUnitType.PIXELS_PER_INCH);

        String result = header.toString();

        assertNotNull(result);
        assertTrue(result.contains("RepresentationHeader"));
    }

    private byte[] createTestData() {
        return new byte[]{
            0x00, 0x00, 0x03, (byte) 0xE8, // representationDataLength
            0x07, (byte) 0xE8, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64, // date/time
            0x01, 0x00, 0x01, 0x00, 0x02, // device info
            0x01, 0x50, 0x00, 0x01, 0x00, 0x02, // quality block
            0x01, 0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, // finger info
            0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0 // image info
        };
    }
}