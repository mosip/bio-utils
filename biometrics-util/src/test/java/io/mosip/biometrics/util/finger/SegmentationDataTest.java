package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SegmentationDataTest {

    /**
     * Tests constructor with quality score only
     */
    @Test
    public void constructor_qualityScoreOnly_createsSegmentationData() {
        int qualityScore = 80;

        SegmentationData segmentationData = new SegmentationData(qualityScore);

        assertNotNull(segmentationData);
        assertEquals(FingerPosition.UNKNOWN, segmentationData.getFingerPosition());
        assertEquals(qualityScore, segmentationData.getQualityScore());
        assertEquals(2, segmentationData.getNoOfCoordinates());
        assertEquals(0, segmentationData.getFingerOrientation());
        assertNotNull(segmentationData.getXCoordinates());
        assertNotNull(segmentationData.getYCoordinates());
        assertEquals(2, segmentationData.getXCoordinates().length);
        assertEquals(2, segmentationData.getYCoordinates().length);
    }

    /**
     * Tests constructor with all parameters
     */
    @Test
    public void constructor_allParameters_createsSegmentationData() {
        int fingerPosition = FingerPosition.RIGHT_THUMB;
        int qualityScore = 90;
        int noOfCoordinates = 3;
        int[] xCoordinates = {10, 20, 30};
        int[] yCoordinates = {40, 50, 60};
        int fingerOrientation = 45;

        SegmentationData segmentationData = new SegmentationData(fingerPosition, qualityScore, 
            noOfCoordinates, xCoordinates, yCoordinates, fingerOrientation);

        assertNotNull(segmentationData);
        assertEquals(fingerPosition, segmentationData.getFingerPosition());
        assertEquals(qualityScore, segmentationData.getQualityScore());
        assertEquals(noOfCoordinates, segmentationData.getNoOfCoordinates());
        assertArrayEquals(xCoordinates, segmentationData.getXCoordinates());
        assertArrayEquals(yCoordinates, segmentationData.getYCoordinates());
        assertEquals(fingerOrientation, segmentationData.getFingerOrientation());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructor_dataInputStream_createsSegmentationData() throws Exception {
        byte[] testData = {0x01, 0x50, 0x02, 0x00, 0x0A, 0x00, 0x14, 0x00, 0x1E, 0x00, 0x28, 0x2D};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        SegmentationData segmentationData = new SegmentationData(inputStream);

        assertNotNull(segmentationData);
        assertEquals(0x01, segmentationData.getFingerPosition());
        assertEquals(0x50, segmentationData.getQualityScore());
        assertEquals(2, segmentationData.getNoOfCoordinates());
        assertEquals(0x2D, segmentationData.getFingerOrientation());
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsSegmentationData() throws Exception {
        byte[] testData = {0x02, 0x60, 0x03, 0x00, 0x05, 0x00, 0x0F, 0x00, 0x19, 0x00, 0x23, 0x00, 0x2D, 0x00, 0x37, 0x3C};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        SegmentationData segmentationData = new SegmentationData(inputStream, true);

        assertNotNull(segmentationData);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validSegmentationData_returnsCorrectLength() {
        SegmentationData segmentationData = new SegmentationData(75);

        long recordLength = segmentationData.getRecordLength();

        assertEquals(12, recordLength); // 1 + 1 + 1 + (2 * 4) + 1
    }

    /**
     * Tests getRecordLength method with custom coordinates
     */
    @Test
    public void getRecordLength_customCoordinates_returnsCorrectLength() {
        int[] xCoords = {1, 2, 3};
        int[] yCoords = {4, 5, 6};
        SegmentationData segmentationData = new SegmentationData(FingerPosition.LEFT_INDEX_FINGER, 85, 3, xCoords, yCoords, 90);

        long recordLength = segmentationData.getRecordLength();

        assertEquals(16, recordLength); // 1 + 1 + 1 + (3 * 4) + 1
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObject_validSegmentationData_writesDataSuccessfully() throws Exception {
        SegmentationData segmentationData = new SegmentationData(FingerPosition.RIGHT_MIDDLE_FINGER, 95, 2, 
            new int[]{100, 200}, new int[]{300, 400}, 180);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        segmentationData.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(12, result.length);
        assertEquals(FingerPosition.RIGHT_MIDDLE_FINGER, result[0] & 0xFF);
        assertEquals(95, result[1] & 0xFF);
        assertEquals(2, result[2] & 0xFF);
    }

    /**
     * Tests setter and getter methods
     */
    @Test
    public void settersAndGetters_validValues_workCorrectly() {
        SegmentationData segmentationData = new SegmentationData(70);

        segmentationData.setFingerPosition(FingerPosition.LEFT_THUMB);
        segmentationData.setQualityScore(85);
        segmentationData.setNoOfCoordinates(4);
        int[] newXCoords = {5, 10, 15, 20};
        int[] newYCoords = {25, 30, 35, 40};
        segmentationData.setXCoordinates(newXCoords);
        segmentationData.setYCoordinates(newYCoords);
        segmentationData.setFingerOrientation(270);

        assertEquals(FingerPosition.LEFT_THUMB, segmentationData.getFingerPosition());
        assertEquals(85, segmentationData.getQualityScore());
        assertEquals(4, segmentationData.getNoOfCoordinates());
        assertArrayEquals(newXCoords, segmentationData.getXCoordinates());
        assertArrayEquals(newYCoords, segmentationData.getYCoordinates());
        assertEquals(270, segmentationData.getFingerOrientation());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validSegmentationData_returnsNonNullString() {
        SegmentationData segmentationData = new SegmentationData(FingerPosition.RIGHT_RING_FINGER, 88, 2, 
            new int[]{50, 100}, new int[]{150, 200}, 135);

        String result = segmentationData.toString();

        assertNotNull(result);
        assertTrue(result.contains("SegmentationData"));
    }
}