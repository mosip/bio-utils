package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FingerQualityBlockTest {

    /**
     * Tests constructor with quality score only
     */
    @Test
    public void constructor_qualityScoreOnly_createsFingerQualityBlock() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(80);

        assertEquals(80, qualityBlock.getQualityScore());
        assertEquals(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED, qualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(FingerQualityAlgorithmIdentifier.UNSPECIFIED, qualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Tests constructor with all parameters
     */
    @Test
    public void constructor_allParameters_createsFingerQualityBlock() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(90, 0x0001, 0x0002);

        assertEquals(90, qualityBlock.getQualityScore());
        assertEquals(0x0001, qualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(0x0002, qualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructor_dataInputStream_createsFingerQualityBlock() throws Exception {
        byte[] testData = {0x50, 0x00, 0x01, 0x00, 0x02};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FingerQualityBlock qualityBlock = new FingerQualityBlock(inputStream);

        assertEquals(0x50, qualityBlock.getQualityScore());
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsFingerQualityBlock() throws Exception {
        byte[] testData = {0x50, 0x00, 0x01, 0x00, 0x02};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FingerQualityBlock qualityBlock = new FingerQualityBlock(inputStream, true);

        assertNotNull(qualityBlock);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validQualityBlock_returnsCorrectLength() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(75);

        long recordLength = qualityBlock.getRecordLength();

        assertEquals(5, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObject_validQualityBlock_writesDataSuccessfully() throws Exception {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(85, 0x0003, 0x0004);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        qualityBlock.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals(85, result[0] & 0xFF);
    }

    /**
     * Tests setQualityScore method sets value correctly
     */
    @Test
    public void setQualityScore_validScore_setsValueCorrectly() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(50);
        int newScore = 95;

        qualityBlock.setQualityScore(newScore);

        assertEquals(newScore, qualityBlock.getQualityScore());
    }

    /**
     * Tests setQualityAlgorithmVendorIdentifier method sets value correctly
     */
    @Test
    public void setQualityAlgorithmVendorIdentifier_validIdentifier_setsValueCorrectly() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(60);
        int newIdentifier = 0x0005;

        qualityBlock.setQualityAlgorithmVendorIdentifier(newIdentifier);

        assertEquals(newIdentifier, qualityBlock.getQualityAlgorithmVendorIdentifier());
    }

    /**
     * Tests setQualityAlgorithmIdentifier method sets value correctly
     */
    @Test
    public void setQualityAlgorithmIdentifier_validIdentifier_setsValueCorrectly() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(70);
        int newIdentifier = 0x0006;

        qualityBlock.setQualityAlgorithmIdentifier(newIdentifier);

        assertEquals(newIdentifier, qualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validQualityBlock_returnsNonNullString() {
        FingerQualityBlock qualityBlock = new FingerQualityBlock(65);

        String result = qualityBlock.toString();

        assertNotNull(result);
        assertTrue(result.contains("QualityBlock"));
    }
}