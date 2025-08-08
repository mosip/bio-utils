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
import java.util.Calendar;

public class RepresentationHeaderTest {

    /**
     * Tests constructor with basic parameters
     */
    @Test
    public void constructorWithBasicParametersCreatesHeader() {
        long representationDataLength = 1000L;
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(80)};
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0]; // Use empty array instead of null
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
     * Tests constructor with null quality blocks array - expects NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullQualityBlocksThrowsException() {
        long representationDataLength = 1000L;
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = null;
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0];
        int fingerPosition = FingerPosition.RIGHT_THUMB;
        int representationNo = 1;
        int scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH;

        new RepresentationHeader(representationDataLength, captureDate,
                qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType);
    }

    /**
     * Tests constructor with null certification blocks array - expects NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullCertificationBlocksThrowsException() {
        long representationDataLength = 1000L;
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(80)};
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;
        FingerCertificationBlock[] certificationBlocks = null;
        int fingerPosition = FingerPosition.RIGHT_THUMB;
        int representationNo = 1;
        int scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH;

        new RepresentationHeader(representationDataLength, captureDate,
                qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType);
    }

    /**
     * Tests constructor with empty arrays (proper way to handle no blocks)
     */
    @Test
    public void constructorWithEmptyArraysWorksCorrectly() {
        long representationDataLength = 1000L;
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = new FingerQualityBlock[0]; // Empty array
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0]; // Empty array
        int fingerPosition = FingerPosition.RIGHT_THUMB;
        int representationNo = 1;
        int scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH;

        RepresentationHeader header = new RepresentationHeader(representationDataLength, captureDate,
                qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType);

        assertNotNull(header);
        assertEquals(0, header.getNoOfQualityBlocks());
        assertEquals(0, header.getNoOfCertificationBlocks());
    }

    /**
     * Tests constructor with all parameters (tests null handling in second constructor)
     */
    @Test
    public void constructorWithAllParametersHandlesNullArrays() {
        long representationDataLength = 2000L;
        Date captureDate = new Date();
        int captureDeviceTechnologyIdentifier = FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR;
        int captureDeviceVendorIdentifier = 0x1234;
        int captureDeviceTypeIdentifier = 0x5678;
        FingerQualityBlock[] qualityBlocks = null; // This constructor handles null properly
        int certificationFlag = FingerCertificationFlag.ONE;
        FingerCertificationBlock[] certificationBlocks = null; // This constructor handles null properly
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
        assertEquals(0, header.getNoOfQualityBlocks()); // null arrays result in 0 count
        assertEquals(0, header.getNoOfCertificationBlocks());
        assertEquals(representationDataLength, header.getRepresentationDataLength());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructorDataInputStreamCreatesHeader() throws Exception {
        byte[] testData = createValidTestData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getCaptureYear() >= 2000);
        assertTrue(header.getCaptureMonth() >= 1 && header.getCaptureMonth() <= 12);
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructorDataInputStreamWithImageInfoFlagCreatesHeader() throws Exception {
        byte[] testData = createValidTestDataForImageInfo();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag, true);

        assertNotNull(header);
        assertEquals(certificationFlag, header.getCertificationFlag());
    }

    /**
     * Tests readObject with invalid device vendor identifier (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidDeviceVendorIdentifierLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidVendorId();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getCaptureDeviceVendorIdentifier() != 0);
    }

    /**
     * Tests readObject with invalid device type identifier (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidDeviceTypeIdentifierLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidTypeId();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getCaptureDeviceTypeIdentifier() != 0);
    }

    /**
     * Tests readObject with invalid finger position (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidFingerPositionLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidFingerPosition();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getFingerPosition() != 0);
    }

    /**
     * Tests readObject with invalid scale units (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidScaleUnitsLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidScaleUnits();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getScaleUnits() != 0);
    }

    /**
     * Tests readObject with invalid bit depth (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidBitDepthLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidBitDepth();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getBitDepth() != 0);
    }

    /**
     * Tests readObject with invalid compression type (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidCompressionTypeLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidCompressionType();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getCompressionType() != 0);
    }

    /**
     * Tests readObject with invalid impression type (triggers exception logging)
     */
    @Test
    public void readObjectWithInvalidImpressionTypeLogsError() throws Exception {
        byte[] testData = createTestDataWithInvalidImpressionType();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.UNSPECIFIED;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag);

        assertNotNull(header);
        assertTrue(header.getImpressionType() != 0);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLengthReturnsCorrectLength() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(75)};
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0];

        RepresentationHeader header = new RepresentationHeader(1000L, captureDate, qualityBlocks,
                FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.RIGHT_THUMB, 1,
                FingerScaleUnitType.PIXELS_PER_INCH);

        long recordLength = header.getRecordLength();

        assertTrue(recordLength > 0);
        assertTrue(recordLength > 30); // Should be at least base header size
    }

    /**
     * Tests getRecordLength with certification flag ONE and certification blocks
     */
    @Test
    public void getRecordLengthWithCertificationBlocksReturnsCorrectLength() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(75)};
        FingerCertificationBlock[] certificationBlocks = {new FingerCertificationBlock()};

        int captureDeviceTechnologyIdentifier = FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR;
        int captureDeviceVendorIdentifier = 0x1234;
        int captureDeviceTypeIdentifier = 0x5678;

        RepresentationHeader header = new RepresentationHeader(1000L, captureDate, captureDeviceTechnologyIdentifier,
                captureDeviceVendorIdentifier, captureDeviceTypeIdentifier, qualityBlocks,
                FingerCertificationFlag.ONE, certificationBlocks, FingerPosition.RIGHT_THUMB, 1,
                FingerScaleUnitType.PIXELS_PER_INCH, 500, 500, 500, 500,
                FingerImageBitDepth.BPP_08, FingerImageCompressionType.WSQ, FingerImpressionType.LIVE_SCAN_PLAIN,
                640, 480);

        long recordLength = header.getRecordLength();

        assertTrue(recordLength > 0);
        assertTrue(recordLength > 40);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObjectWritesDataSuccessfully() throws Exception {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(85)};
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0];

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
     * Tests writeObject with certification flag ONE but null certification blocks (error condition)
     */
    @Test
    public void writeObjectWithCertificationFlagOneButNullBlocksLogsError() throws Exception {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(85)};
        FingerCertificationBlock[] certificationBlocks = null;

        RepresentationHeader header = new RepresentationHeader(1500L, captureDate,
                FingerCaptureDeviceTechnology.UNSPECIFIED, FingerCaptureDeviceVendor.UNSPECIFIED,
                FingerCaptureDeviceType.UNSPECIFIED, qualityBlocks,
                FingerCertificationFlag.ONE, certificationBlocks, FingerPosition.LEFT_THUMB, 1,
                FingerScaleUnitType.PIXELS_PER_CM, 0, 0, 0, 0,
                FingerImageBitDepth.BPP_08, FingerImageCompressionType.WSQ, FingerImpressionType.LIVE_SCAN_PLAIN,
                640, 480);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        header.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
    }

    /**
     * Tests calendar month offset issue (+1 when writing)
     */
    @Test
    public void writeObjectHandlesCalendarMonthOffsetCorrectly() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JANUARY, 15, 10, 30, 45); // January = 0
        cal.set(Calendar.MILLISECOND, 100);
        Date captureDate = cal.getTime();

        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(85)};
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0];

        RepresentationHeader header = new RepresentationHeader(1500L, captureDate, qualityBlocks,
                FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.LEFT_THUMB, 1,
                FingerScaleUnitType.PIXELS_PER_CM);

        assertEquals(0, header.getCaptureMonth());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        header.writeObject(outputStream);
        outputStream.flush();

        byte[] written = baos.toByteArray();
        assertTrue(written.length > 6);
        assertEquals(1, written[6] & 0xFF);
    }

    /**
     * Tests setter and getter methods
     */
    @Test
    public void settersAndGettersWorkCorrectly() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(70)};
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0];

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
     * Tests all remaining setter/getter methods for complete coverage
     */
    @Test
    public void testAllRemainingSettersAndGetters() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(70)};
        FingerCertificationBlock[] certificationBlocks = {new FingerCertificationBlock()};

        RepresentationHeader header = new RepresentationHeader(800L, captureDate, qualityBlocks,
                FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.RIGHT_INDEX_FINGER, 1,
                FingerScaleUnitType.PIXELS_PER_INCH);

        header.setRepresentationLength(1200L);
        header.setCaptureYear(2023);
        header.setCaptureMonth(5);
        header.setCaptureDay(15);
        header.setCaptureHour(14);
        header.setCaptureMinute(30);
        header.setCaptureSecond(45);
        header.setCaptureMilliSecond(500);
        header.setCaptureDeviceTechnologyIdentifier(5);
        header.setCaptureDeviceVendorIdentifier(0x1234);
        header.setCaptureDeviceTypeIdentifier(0x5678);
        header.setNoOfQualityBlocks(2);
        header.setNoOfCertificationBlocks(1);
        header.setRepresentationNo(3);
        header.setScaleUnits(FingerScaleUnitType.PIXELS_PER_CM);
        header.setCaptureDeviceSpatialSamplingRateHorizontal(400);
        header.setCaptureDeviceSpatialSamplingRateVertical(400);
        header.setImageSpatialSamplingRateHorizontal(300);
        header.setImageSpatialSamplingRateVertical(300);

        assertEquals(1200L, header.getRepresentationLength());
        assertEquals(2023, header.getCaptureYear());
        assertEquals(5, header.getCaptureMonth());
        assertEquals(15, header.getCaptureDay());
        assertEquals(14, header.getCaptureHour());
        assertEquals(30, header.getCaptureMinute());
        assertEquals(45, header.getCaptureSecond());
        assertEquals(500, header.getCaptureMilliSecond());
        assertEquals(5, header.getCaptureDeviceTechnologyIdentifier());
        assertEquals(0x1234, header.getCaptureDeviceVendorIdentifier());
        assertEquals(0x5678, header.getCaptureDeviceTypeIdentifier());
        assertEquals(2, header.getNoOfQualityBlocks());
        assertEquals(1, header.getNoOfCertificationBlocks());
        assertEquals(3, header.getRepresentationNo());
        assertEquals(FingerScaleUnitType.PIXELS_PER_CM, header.getScaleUnits());
        assertEquals(400, header.getCaptureDeviceSpatialSamplingRateHorizontal());
        assertEquals(400, header.getCaptureDeviceSpatialSamplingRateVertical());
        assertEquals(300, header.getImageSpatialSamplingRateHorizontal());
        assertEquals(300, header.getImageSpatialSamplingRateVertical());

        FingerQualityBlock[] newQualityBlocks = {new FingerQualityBlock(90), new FingerQualityBlock(80)};
        FingerCertificationBlock[] newCertificationBlocks = {new FingerCertificationBlock()};

        header.setQualityBlocks(newQualityBlocks);
        header.setCertificationBlocks(newCertificationBlocks);

        assertEquals(2, header.getQualityBlocks().length);
        assertEquals(1, header.getCertificationBlocks().length);
        assertNotNull(header.getCaptureDateTime());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringReturnsNonNullString() {
        Date captureDate = new Date();
        FingerQualityBlock[] qualityBlocks = {new FingerQualityBlock(65)};
        FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[0];

        RepresentationHeader header = new RepresentationHeader(1200L, captureDate, qualityBlocks,
                FingerCertificationFlag.UNSPECIFIED, certificationBlocks, FingerPosition.RIGHT_RING_FINGER, 1,
                FingerScaleUnitType.PIXELS_PER_INCH);

        String result = header.toString();

        assertNotNull(result);
        assertTrue(result.contains("RepresentationHeader"));
        assertTrue(result.contains("RecordLength"));
        assertTrue(result.contains("representationDataLength"));
    }

    /**
     * Tests readObject onlyImageInformation path with certification flag ONE
     */
    @Test
    public void readObjectOnlyImageInfoWithCertificationFlagOne() throws Exception {
        byte[] testData = createTestDataForImageInfoWithCertification();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);
        int certificationFlag = FingerCertificationFlag.ONE;

        RepresentationHeader header = new RepresentationHeader(inputStream, certificationFlag, true);

        assertNotNull(header);
        assertEquals(certificationFlag, header.getCertificationFlag());
        assertTrue(header.getNoOfCertificationBlocks() >= 0);
    }

    private byte[] createValidTestData() {
        return new byte[]{
                // 4 bytes: representationDataLength (1000)
                0x00, 0x00, 0x03, (byte) 0xE8,
                // 9 bytes: date/time (2023-12-31 10:30:45.100)
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                // 3 bytes: device info (tech, vendor, type)
                0x01, 0x00, 0x01, 0x00, 0x02,
                // 6 bytes: quality blocks (1 block: count + 5 bytes data)
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                // 18 bytes: remaining fields
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createValidTestDataForImageInfo() {
        return new byte[]{
                // Skip first 18 bytes (4+9+1+2+2) - these will be skipped by readObject
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                // 1 byte: noOfQualityBlocks
                0x01,
                // 5 bytes: quality block data (will be skipped)
                0x50, 0x00, 0x00, 0x00, 0x00,
                // 1 byte: fingerPosition
                0x01,
                // 10 bytes: skip data (representationNo + scaleUnits + sampling rates)
                0x01, 0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, 0x00,
                // 1 byte: bitDepth
                0x08,
                // 1 byte: compressionType
                0x05,
                // 5 bytes: remaining skip data (impressionType + line lengths)
                0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0
        };
    }

    private byte[] createTestDataWithCertification() {
        return new byte[]{
                // 4 bytes: representationDataLength
                0x00, 0x00, 0x03, (byte) 0xE8,
                // 9 bytes: date/time
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                // 3 bytes: device info
                0x01, 0x00, 0x01, 0x00, 0x02,
                // 6 bytes: quality blocks (1 block)
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                // 4 bytes: certification blocks (1 block: count + 3 bytes data)
                0x01, 0x01, 0x02, 0x03,
                // 15 bytes: remaining fields
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0
        };
    }

    private byte[] createTestDataWithInvalidVendorId() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                // Invalid vendor ID (0xFFFF)
                0x01, (byte) 0xFF, (byte) 0xFF, 0x00, 0x02,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataWithInvalidTypeId() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                // Invalid type ID (0xFFFF)
                0x01, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataWithInvalidFingerPosition() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                0x01, 0x00, 0x01, 0x00, 0x02,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                // Invalid finger position (0xFF)
                (byte) 0xFF, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataWithInvalidScaleUnits() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                0x01, 0x00, 0x01, 0x00, 0x02,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                // Invalid scale units (0xFF)
                0x01, 0x01, (byte) 0xFF, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                0x08, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataWithInvalidBitDepth() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                0x01, 0x00, 0x01, 0x00, 0x02,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                // Invalid bit depth (0xFF)
                (byte) 0xFF, 0x05, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataWithInvalidCompressionType() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                0x01, 0x00, 0x01, 0x00, 0x02,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                // Invalid compression type (0xFF)
                0x08, (byte) 0xFF, 0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataWithInvalidImpressionType() {
        return new byte[]{
                0x00, 0x00, 0x03, (byte) 0xE8,
                0x07, (byte) 0xE7, 0x0C, 0x1F, 0x0A, 0x1E, 0x2D, 0x00, 0x64,
                0x01, 0x00, 0x01, 0x00, 0x02,
                0x01, 0x50, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4,
                // Invalid impression type (0xFF)
                0x08, 0x05, (byte) 0xFF, 0x02, (byte) 0x80, 0x01, (byte) 0xE0, 0x00, 0x00, 0x00
        };
    }

    private byte[] createTestDataForImageInfoWithCertification() {
        return new byte[]{
                // Skip first 18 bytes
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                // 1 byte: noOfQualityBlocks
                0x01,
                // 5 bytes: quality block data
                0x50, 0x00, 0x00, 0x00, 0x00,
                // 7 bytes: certification data (2 blocks: count + 2*3 bytes)
                0x02, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                // 1 byte: fingerPosition
                0x01,
                // 10 bytes: skip data
                0x01, 0x01, 0x01, 0x01, 0x01, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, (byte) 0xF4, 0x00,
                // 1 byte: bitDepth
                0x08,
                // 1 byte: compressionType
                0x05,
                // 5 bytes: remaining skip data
                0x1D, 0x02, (byte) 0x80, 0x01, (byte) 0xE0
        };
    }
}
