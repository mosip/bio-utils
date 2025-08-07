package io.mosip.biometrics.util.iris;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Unit tests for {@link RepresentationHeader}.
 */
class RepresentationHeaderTest {

    private RepresentationHeader representationHeader;
    private Date captureDate;
    private IrisQualityBlock[] qualityBlocks;
    private ImageInformation imageInformation;

    @BeforeEach
    void setUp() {
        captureDate = new Date();
        qualityBlocks = new IrisQualityBlock[]{new IrisQualityBlock(100)};
        imageInformation = createValidImageInformation();
        representationHeader = new RepresentationHeader(1000L, captureDate, qualityBlocks, imageInformation, 1);
    }

    /**
     * Creates a valid ImageInformation for testing purposes using the full constructor.
     */
    private ImageInformation createValidImageInformation() {
        return new ImageInformation(1, 2, 3, 1, 2, 1, 640, 480, 8, 255, 10, 5, 100, 200, 150, 250, 50, 75);
    }

    /**
     * Creates ImageInformation from byte stream for DataInputStream constructor testing.
     */
    private byte[] createValidImageInformationBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeByte(1);  // eyeLabel
        dos.writeByte(2);  // imageType
        dos.writeByte(3);  // imageFormat
        dos.writeByte(0x41);  // imagePropertiesBits (horizontalOrientation=1, verticalOrientation=2, compressionType=1)
        dos.writeShort(640);  // width
        dos.writeShort(480);  // height
        dos.writeByte(8);     // bitDepth
        dos.writeShort(255);  // range
        dos.writeShort(10);   // rollAngleOfEye
        dos.writeShort(5);    // rollAngleUncertainty
        dos.writeShort(100);  // irisCenterSmallestX
        dos.writeShort(200);  // irisCenterLargestX
        dos.writeShort(150);  // irisCenterSmallestY
        dos.writeShort(250);  // irisCenterLargestY
        dos.writeShort(50);   // irisDiameterSmallest
        dos.writeShort(75);   // irisDiameterLargest
        dos.flush();

        return baos.toByteArray();
    }

    /**
     * Verifies constructor with basic parameters sets values correctly.
     */
    @Test
    void constructorWithBasicParametersSetsValuesCorrectly() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(captureDate);

        assertEquals(1000L, representationHeader.getRepresentationDataLength());
        assertEquals(captureDate, representationHeader.getCaptureDateTime());
        assertEquals(cal.get(Calendar.YEAR), representationHeader.getCaptureYear());
        assertEquals(cal.get(Calendar.MONTH), representationHeader.getCaptureMonth());
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), representationHeader.getCaptureDay());
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), representationHeader.getCaptureHour());
        assertEquals(cal.get(Calendar.MINUTE), representationHeader.getCaptureMinute());
        assertEquals(cal.get(Calendar.SECOND), representationHeader.getCaptureSecond());
        assertEquals(cal.get(Calendar.MILLISECOND), representationHeader.getCaptureMilliSecond());
        assertEquals(IrisCaptureDeviceTechnology.UNSPECIFIED, representationHeader.getCaptureDeviceTechnologyIdentifier());
        assertEquals(IrisCaptureDeviceVendor.UNSPECIFIED, representationHeader.getCaptureDeviceVendorIdentifier());
        assertEquals(IrisCaptureDeviceType.UNSPECIFIED, representationHeader.getCaptureDeviceTypeIdentifier());
        assertEquals(1, representationHeader.getNoOfQualityBlocks());
        assertArrayEquals(qualityBlocks, representationHeader.getQualityBlocks());
        assertEquals(imageInformation, representationHeader.getImageInformation());
        assertEquals(1, representationHeader.getRepresentationNo());
    }

    /**
     * Verifies constructor with all parameters sets device identifiers correctly.
     */
    @Test
    void constructorWithAllParametersSetsDeviceIdentifiersCorrectly() {
        RepresentationHeader header = new RepresentationHeader(2000L, captureDate, 1, 2, 3, qualityBlocks, imageInformation, 2);

        assertEquals(2000L, header.getRepresentationDataLength());
        assertEquals(1, header.getCaptureDeviceTechnologyIdentifier());
        assertEquals(2, header.getCaptureDeviceVendorIdentifier());
        assertEquals(3, header.getCaptureDeviceTypeIdentifier());
        assertEquals(2, header.getRepresentationNo());
    }

    /**
     * Verifies constructor with DataInputStream reads data correctly.
     */
    @Test
    void constructorWithDataInputStreamReadsDataCorrectly() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(1000);
        dos.writeShort(2023);
        dos.writeByte(12);
        dos.writeByte(25);
        dos.writeByte(14);
        dos.writeByte(30);
        dos.writeByte(45);
        dos.writeShort(500);
        dos.writeByte(1);
        dos.writeShort(100);
        dos.writeShort(200);
        dos.writeByte(1);

        IrisQualityBlock qualityBlock = new IrisQualityBlock(150);
        qualityBlock.writeObject(dos);

        dos.writeShort(5);

        byte[] imageInfoBytes = createValidImageInformationBytes();
        dos.write(imageInfoBytes);

        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        RepresentationHeader header = new RepresentationHeader(dis);

        assertEquals(1000L, header.getRepresentationDataLength());
        assertEquals(2023, header.getCaptureYear());
        assertEquals(12, header.getCaptureMonth());
        assertEquals(25, header.getCaptureDay());
        assertEquals(14, header.getCaptureHour());
        assertEquals(30, header.getCaptureMinute());
        assertEquals(45, header.getCaptureSecond());
        assertEquals(500, header.getCaptureMilliSecond());
        assertEquals(1, header.getCaptureDeviceTechnologyIdentifier());
        assertEquals(100, header.getCaptureDeviceVendorIdentifier());
        assertEquals(200, header.getCaptureDeviceTypeIdentifier());
        assertEquals(1, header.getNoOfQualityBlocks());
        assertEquals(5, header.getRepresentationNo());
        assertNotNull(header.getCaptureDateTime());
    }

    /**
     * Verifies constructor with DataInputStream and flag skips non-image information.
     */
    @Test
    void constructorWithDataInputStreamAndFlagSkipsNonImageInformation() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(2000);
        for (int i = 0; i < 14; i++) {
            dos.writeByte(i);
        }
        dos.writeByte(1);

        IrisQualityBlock qualityBlock = new IrisQualityBlock(180);
        qualityBlock.writeObject(dos);

        dos.writeShort(10);

        byte[] imageInfoBytes = createValidImageInformationBytes();
        dos.write(imageInfoBytes);

        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        RepresentationHeader header = new RepresentationHeader(dis, true);

        assertEquals(2000L, header.getRepresentationDataLength());
        assertEquals(1, header.getNoOfQualityBlocks());
        assertEquals(10, header.getRepresentationNo());
        assertNotNull(header.getImageInformation());
    }

    /**
     * Verifies readObject with zero quality blocks creates empty array.
     */
    @Test
    void readObjectWithZeroQualityBlocksCreatesEmptyArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(1000);
        dos.writeShort(2023);
        dos.writeByte(12);
        dos.writeByte(25);
        dos.writeByte(14);
        dos.writeByte(30);
        dos.writeByte(45);
        dos.writeShort(500);
        dos.writeByte(1);
        dos.writeShort(100);
        dos.writeShort(200);
        dos.writeByte(0);
        dos.writeShort(5);

        byte[] imageInfoBytes = createValidImageInformationBytes();
        dos.write(imageInfoBytes);

        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        RepresentationHeader header = new RepresentationHeader(dis);

        assertEquals(0, header.getNoOfQualityBlocks());
        assertEquals(0, header.getQualityBlocks().length);
    }

    /**
     * Verifies getRecordLength calculates correct length with quality blocks.
     */
    @Test
    void getRecordLengthCalculatesCorrectLengthWithQualityBlocks() {
        long expectedLength = 4 + 9 + 1 + 2 + 2 + 1 + 5 + 2 + 27;
        assertEquals(expectedLength, representationHeader.getRecordLength());
    }

    /**
     * Verifies getRecordLength calculates correct length with null quality blocks.
     */
    @Test
    void getRecordLengthCalculatesCorrectLengthWithNullQualityBlocks() {
        representationHeader.setQualityBlocks(null);
        long expectedLength = 4 + 9 + 1 + 2 + 2 + 1 + 0 + 2 + 27;
        assertEquals(expectedLength, representationHeader.getRecordLength());
    }

    /**
     * Verifies getRecordLength calculates correct length with empty quality blocks.
     */
    @Test
    void getRecordLengthCalculatesCorrectLengthWithEmptyQualityBlocks() {
        representationHeader.setQualityBlocks(new IrisQualityBlock[0]);
        long expectedLength = 4 + 9 + 1 + 2 + 2 + 1 + 0 + 2 + 27;
        assertEquals(expectedLength, representationHeader.getRecordLength());
    }

    /**
     * Verifies writeObject writes data correctly to output stream.
     */
    @Test
    void writeObjectWritesDataCorrectlyToOutputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        representationHeader.writeObject(dos);

        byte[] result = baos.toByteArray();
        assertTrue(result.length > 0);

        ByteArrayInputStream bais = new ByteArrayInputStream(result);
        DataInputStream dis = new DataInputStream(bais);

        int recordLength = dis.readInt();
        assertEquals(representationHeader.getRecordLength() + representationHeader.getRepresentationDataLength(), recordLength);
    }

    /**
     * Verifies writeObject handles null quality blocks correctly.
     */
    @Test
    void writeObjectHandlesNullQualityBlocksCorrectly() throws IOException {
        representationHeader.setQualityBlocks(null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        representationHeader.writeObject(dos);

        byte[] result = baos.toByteArray();
        assertTrue(result.length > 0);
    }

    /**
     * Verifies all getter and setter methods work correctly.
     */
    @Test
    void allGetterAndSetterMethodsWorkCorrectly() {
        representationHeader.setRepresentationLength(5000L);
        assertEquals(5000L, representationHeader.getRepresentationLength());

        Date newDate = new Date(System.currentTimeMillis() + 10000);
        representationHeader.setCaptureDateTime(newDate);
        assertEquals(newDate, representationHeader.getCaptureDateTime());

        representationHeader.setCaptureYear(2024);
        assertEquals(2024, representationHeader.getCaptureYear());

        representationHeader.setCaptureMonth(6);
        assertEquals(6, representationHeader.getCaptureMonth());

        representationHeader.setCaptureDay(15);
        assertEquals(15, representationHeader.getCaptureDay());

        representationHeader.setCaptureHour(10);
        assertEquals(10, representationHeader.getCaptureHour());

        representationHeader.setCaptureMinute(25);
        assertEquals(25, representationHeader.getCaptureMinute());

        representationHeader.setCaptureSecond(40);
        assertEquals(40, representationHeader.getCaptureSecond());

        representationHeader.setCaptureMilliSecond(750);
        assertEquals(750, representationHeader.getCaptureMilliSecond());

        representationHeader.setCaptureDeviceTechnologyIdentifier(99);
        assertEquals(99, representationHeader.getCaptureDeviceTechnologyIdentifier());

        representationHeader.setCaptureDeviceVendorIdentifier(500);
        assertEquals(500, representationHeader.getCaptureDeviceVendorIdentifier());

        representationHeader.setCaptureDeviceTypeIdentifier(300);
        assertEquals(300, representationHeader.getCaptureDeviceTypeIdentifier());

        representationHeader.setNoOfQualityBlocks(5);
        assertEquals(5, representationHeader.getNoOfQualityBlocks());

        IrisQualityBlock[] newQualityBlocks = {new IrisQualityBlock(200), new IrisQualityBlock(250)};
        representationHeader.setQualityBlocks(newQualityBlocks);
        assertArrayEquals(newQualityBlocks, representationHeader.getQualityBlocks());

        ImageInformation newImageInfo = createValidImageInformation();
        representationHeader.setImageInformation(newImageInfo);
        assertEquals(newImageInfo, representationHeader.getImageInformation());

        representationHeader.setRepresentationNo(10);
        assertEquals(10, representationHeader.getRepresentationNo());
    }

    /**
     * Verifies toString method returns expected string format.
     */
    @Test
    void toStringReturnsExpectedStringFormat() {
        String result = representationHeader.toString();

        assertTrue(result.contains("RepresentationHeader"));
        assertTrue(result.contains("RecordLength="));
        assertTrue(result.contains("representationDataLength="));
        assertTrue(result.contains("representationLength="));
        assertTrue(result.contains("captureDateTime="));
        assertTrue(result.contains("captureYear="));
        assertTrue(result.contains("captureMonth="));
        assertTrue(result.contains("captureDay="));
        assertTrue(result.contains("captureHour="));
        assertTrue(result.contains("captureMinute="));
        assertTrue(result.contains("captureSecond="));
        assertTrue(result.contains("captureMilliSecond="));
        assertTrue(result.contains("captureDeviceTechnologyIdentifier="));
        assertTrue(result.contains("captureDeviceVendorIdentifier="));
        assertTrue(result.contains("captureDeviceTypeIdentifier="));
        assertTrue(result.contains("noOfQualityBlocks="));
        assertTrue(result.contains("qualityBlocks="));
        assertTrue(result.contains("imageInformation="));
        assertTrue(result.contains("representationNo="));
    }

    /**
     * Verifies constructor with DataInputStream throws IOException when stream is invalid.
     */
    @Test
    void constructorWithDataInputStreamThrowsIOExceptionWhenStreamIsInvalid() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[5]);
        DataInputStream dis = new DataInputStream(bais);

        assertThrows(IOException.class, () -> new RepresentationHeader(dis));
    }

    /**
     * Verifies constructor with DataInputStream and flag throws IOException when stream is invalid.
     */
    @Test
    void constructorWithDataInputStreamAndFlagThrowsIOExceptionWhenStreamIsInvalid() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[10]);
        DataInputStream dis = new DataInputStream(bais);

        assertThrows(IOException.class, () -> new RepresentationHeader(dis, true));
    }

    /**
     * Verifies readObject with DataInputStream and flag creates valid quality blocks with flag.
     */
    @Test
    void readObjectWithDataInputStreamAndFlagCreatesValidQualityBlocksWithFlag() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(2000);
        for (int i = 0; i < 14; i++) {
            dos.writeByte(i);
        }
        dos.writeByte(2);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                dos.writeByte(j);
            }
        }

        dos.writeShort(15);

        byte[] imageInfoBytes = createValidImageInformationBytes();
        dos.write(imageInfoBytes);

        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        RepresentationHeader header = new RepresentationHeader(dis, true);

        assertEquals(2000L, header.getRepresentationDataLength());
        assertEquals(2, header.getNoOfQualityBlocks());
        assertEquals(15, header.getRepresentationNo());
        assertNotNull(header.getQualityBlocks());
        assertEquals(2, header.getQualityBlocks().length);
    }
}
