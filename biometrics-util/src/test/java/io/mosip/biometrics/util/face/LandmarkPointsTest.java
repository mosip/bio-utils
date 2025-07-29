package io.mosip.biometrics.util.face;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link LandmarkPoints} class.
 */
class LandmarkPointsTest {

    private LandmarkPoints landmarkPoints;
    private final int testType = 0x01;
    private final int testCode = 0x02;
    private final int testX = 0x1234;
    private final int testY = 0x5678;
    private final int testZ = 0x9ABC;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        landmarkPoints = new LandmarkPoints(testType, testCode, testX, testY, testZ);
    }

    /**
     * Verifies that the constructor properly initializes all landmark point values.
     */
    @Test
    void constructor_WithValidParameters_InitializesCorrectly() {
        assertNotNull(landmarkPoints);
        assertEquals(testType, landmarkPoints.getLandmarkPointType());
        assertEquals(testCode, landmarkPoints.getLandmarkPointCode());
        assertEquals(testX, landmarkPoints.getXCoordinate());
        assertEquals(testY, landmarkPoints.getYCoordinate());
        assertEquals(testZ, landmarkPoints.getZCoordinate());
    }

    /**
     * Verifies that getRecordLength returns the correct byte count.
     */
    @Test
    void getRecordLength_WhenCalled_ReturnsEightBytes() {
        assertEquals(8L, landmarkPoints.getRecordLength());
    }

    /**
     * Verifies object serialization and deserialization.
     */
    @Test
    void writeAndReadObject_WithValidData_PreservesValues() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            landmarkPoints.writeObject(dos);
        }

        byte[] bytes = baos.toByteArray();
        assertEquals(8, bytes.length);

        LandmarkPoints readPoints;
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
            readPoints = new LandmarkPoints(dis);
        }

        assertNotNull(readPoints);
        assertEquals(landmarkPoints.getLandmarkPointType(), readPoints.getLandmarkPointType());
        assertEquals(landmarkPoints.getLandmarkPointCode(), readPoints.getLandmarkPointCode());
        assertEquals(landmarkPoints.getXCoordinate(), readPoints.getXCoordinate());
        assertEquals(landmarkPoints.getYCoordinate(), readPoints.getYCoordinate());
        assertEquals(landmarkPoints.getZCoordinate(), readPoints.getZCoordinate());
    }

    /**
     * Verifies that reading with onlyImageInformation flag returns default values.
     */
    @Test
    void readObject_WithOnlyImageInformationFlag_ReturnsDefaultValues() throws IOException {
        byte[] testData = new byte[8];
        testData[0] = 0x01;
        testData[1] = 0x02;
        testData[2] = 0x12;
        testData[3] = 0x34;
        testData[4] = 0x56;
        testData[5] = 0x78;
        testData[6] = (byte)0x9A;
        testData[7] = (byte)0xBC;

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(testData))) {
            LandmarkPoints points = new LandmarkPoints(dis, true);
            assertEquals(0, points.getLandmarkPointType());
            assertEquals(0, points.getLandmarkPointCode());
            assertEquals(0, points.getXCoordinate());
            assertEquals(0, points.getYCoordinate());
            assertEquals(0, points.getZCoordinate());
        }
    }

    /**
     * Verifies that setters and getters work correctly.
     */
    @Test
    void settersAndGetters_WithValidValues_UpdateFieldsCorrectly() {
        int newType = 0x03;
        int newCode = 0x04;
        int newX = 0x1111;
        int newY = 0x2222;
        int newZ = 0x3333;

        landmarkPoints.setLandmarkPointType(newType);
        landmarkPoints.setLandmarkPointCode(newCode);
        landmarkPoints.setXCoordinate(newX);
        landmarkPoints.setYCoordinate(newY);
        landmarkPoints.setZCoordinate(newZ);

        assertEquals(newType, landmarkPoints.getLandmarkPointType());
        assertEquals(newCode, landmarkPoints.getLandmarkPointCode());
        assertEquals(newX, landmarkPoints.getXCoordinate());
        assertEquals(newY, landmarkPoints.getYCoordinate());
        assertEquals(newZ, landmarkPoints.getZCoordinate());
    }

    /**
     * Verifies that toString includes all required fields.
     */
    @Test
    void toString_WhenCalled_ContainsAllFields() {
        String str = landmarkPoints.toString();
        assertTrue(str.contains("LandmarkPointRecordLength=8"));
        assertTrue(str.contains("landmarkPointType=1"));
        assertTrue(str.contains("landmarkPointCode=2"));
        assertTrue(str.contains("xCoordinate=1234"));
        assertTrue(str.contains("yCoordinate=5678"));
        assertTrue(str.contains("zCoordinate=9abc"));
    }

    /**
     * Verifies handling of minimum and maximum boundary values.
     */
    @Test
    void setters_WithBoundaryValues_StoreCorrectly() {
        landmarkPoints.setLandmarkPointType(0);
        landmarkPoints.setLandmarkPointCode(0);
        landmarkPoints.setXCoordinate(0);
        landmarkPoints.setYCoordinate(0);
        landmarkPoints.setZCoordinate(0);

        assertEquals(0, landmarkPoints.getLandmarkPointType());
        assertEquals(0, landmarkPoints.getLandmarkPointCode());
        assertEquals(0, landmarkPoints.getXCoordinate());
        assertEquals(0, landmarkPoints.getYCoordinate());
        assertEquals(0, landmarkPoints.getZCoordinate());

        landmarkPoints.setLandmarkPointType(0xFF);
        landmarkPoints.setLandmarkPointCode(0xFF);
        landmarkPoints.setXCoordinate(0xFFFF);
        landmarkPoints.setYCoordinate(0xFFFF);
        landmarkPoints.setZCoordinate(0xFFFF);

        assertEquals(0xFF, landmarkPoints.getLandmarkPointType());
        assertEquals(0xFF, landmarkPoints.getLandmarkPointCode());
        assertEquals(0xFFFF, landmarkPoints.getXCoordinate());
        assertEquals(0xFFFF, landmarkPoints.getYCoordinate());
        assertEquals(0xFFFF, landmarkPoints.getZCoordinate());
    }

    /**
     * Verifies that negative values are handled as unsigned values.
     */
    @Test
    void setters_WithNegativeValues_HandleAsUnsigned() {
        landmarkPoints.setLandmarkPointType(-1);
        landmarkPoints.setLandmarkPointCode(-1);
        landmarkPoints.setXCoordinate(-1);
        landmarkPoints.setYCoordinate(-1);
        landmarkPoints.setZCoordinate(-1);

        assertEquals(0xFF, landmarkPoints.getLandmarkPointType() & 0xFF);
        assertEquals(0xFF, landmarkPoints.getLandmarkPointCode() & 0xFF);
        assertEquals(0xFFFF, landmarkPoints.getXCoordinate() & 0xFFFF);
        assertEquals(0xFFFF, landmarkPoints.getYCoordinate() & 0xFFFF);
        assertEquals(0xFFFF, landmarkPoints.getZCoordinate() & 0xFFFF);
    }
}