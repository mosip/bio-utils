package io.mosip.biometrics.util.iris;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Unit tests for {@link IrisQualityBlock}.
 */
class IrisQualityBlockTest {

    private IrisQualityBlock irisQualityBlock;

    @BeforeEach
    void setUp() {
        irisQualityBlock = new IrisQualityBlock(100);
    }

    /**
     * Verifies constructor with single quality score parameter.
     */
    @Test
    void constructorWithQualityScoreCreatesBlockWithDefaultValues() {
        IrisQualityBlock block = new IrisQualityBlock(150);

        assertEquals(150, block.getQualityScore());
        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, block.getQualityAlgorithmVendorIdentifier());
        assertEquals(IrisQualityAlgorithmIdentifier.UNSPECIFIED, block.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies constructor with all three parameters sets values correctly.
     */
    @Test
    void constructorWithAllParametersSetsValuesCorrectly() {
        IrisQualityBlock block = new IrisQualityBlock(200, 1000, 2000);

        assertEquals(200, block.getQualityScore());
        assertEquals(1000, block.getQualityAlgorithmVendorIdentifier());
        assertEquals(2000, block.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies constructor with DataInputStream reads values from stream.
     */
    @Test
    void constructorWithDataInputStreamReadsValuesFromStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(120);
        dos.writeShort(3000);
        dos.writeShort(4000);
        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        IrisQualityBlock block = new IrisQualityBlock(dis);

        assertEquals(120, block.getQualityScore());
        assertEquals(3000, block.getQualityAlgorithmVendorIdentifier());
        assertEquals(4000, block.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies constructor with DataInputStream and onlyImageInformation flag skips data reading.
     */
    @Test
    void constructorWithDataInputStreamAndFlagSkipsDataReading() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(80);
        dos.writeShort(1500);
        dos.writeShort(2500);
        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        IrisQualityBlock block = new IrisQualityBlock(dis, true);

        assertEquals(255, block.getQualityScore());
        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, block.getQualityAlgorithmVendorIdentifier());
        assertEquals(IrisQualityAlgorithmIdentifier.UNSPECIFIED, block.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies readObject method with DataInputStream reads values correctly.
     */
    @Test
    void readObjectWithDataInputStreamReadsValuesCorrectly() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(75);
        dos.writeShort(5000);
        dos.writeShort(6000);
        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        irisQualityBlock.readObject(dis);

        assertEquals(75, irisQualityBlock.getQualityScore());
        assertEquals(5000, irisQualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(6000, irisQualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies readObject method with DataInputStream and onlyImageInformation flag preserves original values.
     */
    @Test
    void readObjectWithDataInputStreamAndFlagPreservesOriginalValues() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(90);
        dos.writeShort(7000);
        dos.writeShort(8000);
        dos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);

        int originalQualityScore = irisQualityBlock.getQualityScore();
        int originalVendorId = irisQualityBlock.getQualityAlgorithmVendorIdentifier();
        int originalAlgorithmId = irisQualityBlock.getQualityAlgorithmIdentifier();

        irisQualityBlock.readObject(dis, true);

        assertEquals(originalQualityScore, irisQualityBlock.getQualityScore());
        assertEquals(originalVendorId, irisQualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(originalAlgorithmId, irisQualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies getRecordLength returns expected value of 5.
     */
    @Test
    void getRecordLengthReturnsExpectedValue() {
        assertEquals(5, irisQualityBlock.getRecordLength());
    }

    /**
     * Verifies writeObject method writes data correctly to output stream.
     */
    @Test
    void writeObjectWritesDataCorrectlyToOutputStream() throws IOException {
        irisQualityBlock.setQualityScore(125);
        irisQualityBlock.setQualityAlgorithmVendorIdentifier(9000);
        irisQualityBlock.setQualityAlgorithmIdentifier(10000);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        irisQualityBlock.writeObject(dos);

        byte[] result = baos.toByteArray();
        assertEquals(5, result.length);

        ByteArrayInputStream bais = new ByteArrayInputStream(result);
        DataInputStream dis = new DataInputStream(bais);

        assertEquals(125, dis.readUnsignedByte());
        assertEquals(9000, dis.readUnsignedShort());
        assertEquals(10000, dis.readUnsignedShort());
    }

    /**
     * Verifies getQualityScore returns correct value.
     */
    @Test
    void getQualityScoreReturnsCorrectValue() {
        assertEquals(100, irisQualityBlock.getQualityScore());
    }

    /**
     * Verifies setQualityScore sets value correctly.
     */
    @Test
    void setQualityScoreSetsValueCorrectly() {
        irisQualityBlock.setQualityScore(180);
        assertEquals(180, irisQualityBlock.getQualityScore());
    }

    /**
     * Verifies getQualityAlgorithmVendorIdentifier returns correct value.
     */
    @Test
    void getQualityAlgorithmVendorIdentifierReturnsCorrectValue() {
        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, irisQualityBlock.getQualityAlgorithmVendorIdentifier());
    }

    /**
     * Verifies setQualityAlgorithmVendorIdentifier sets value correctly.
     */
    @Test
    void setQualityAlgorithmVendorIdentifierSetsValueCorrectly() {
        irisQualityBlock.setQualityAlgorithmVendorIdentifier(12000);
        assertEquals(12000, irisQualityBlock.getQualityAlgorithmVendorIdentifier());
    }

    /**
     * Verifies getQualityAlgorithmIdentifier returns correct value.
     */
    @Test
    void getQualityAlgorithmIdentifierReturnsCorrectValue() {
        assertEquals(IrisQualityAlgorithmIdentifier.UNSPECIFIED, irisQualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies setQualityAlgorithmIdentifier sets value correctly.
     */
    @Test
    void setQualityAlgorithmIdentifierSetsValueCorrectly() {
        irisQualityBlock.setQualityAlgorithmIdentifier(15000);
        assertEquals(15000, irisQualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies toString method returns expected string format with hexadecimal values.
     */
    @Test
    void toStringReturnsExpectedStringFormatWithHexadecimalValues() {
        irisQualityBlock.setQualityScore(255);
        irisQualityBlock.setQualityAlgorithmVendorIdentifier(65535);
        irisQualityBlock.setQualityAlgorithmIdentifier(32767);

        String result = irisQualityBlock.toString();

        assertTrue(result.contains("IrisQualityBlock"));
        assertTrue(result.contains("QualityBlockRecordLength=5"));
        assertTrue(result.contains("qualityScore=ff"));
        assertTrue(result.contains("qualityAlgorithmVendorIdentifier=ffff"));
        assertTrue(result.contains("qualityAlgorithmIdentifier=7fff"));
    }

    /**
     * Verifies constructor with DataInputStream throws IOException when stream is invalid.
     */
    @Test
    void constructorWithDataInputStreamThrowsIOExceptionWhenStreamIsInvalid() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        DataInputStream dis = new DataInputStream(bais);

        assertThrows(IOException.class, () -> new IrisQualityBlock(dis));
    }

    /**
     * Verifies readObject throws IOException when stream is invalid.
     */
    @Test
    void readObjectThrowsIOExceptionWhenStreamIsInvalid() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        DataInputStream dis = new DataInputStream(bais);

        assertThrows(IOException.class, () -> irisQualityBlock.readObject(dis));
    }

    /**
     * Verifies constructor with DataInputStream and flag completes successfully even with insufficient data.
     */
    @Test
    void constructorWithDataInputStreamAndFlagCompletesSuccessfullyWithInsufficientData() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        DataInputStream dis = new DataInputStream(bais);

        IrisQualityBlock block = new IrisQualityBlock(dis, true);

        assertEquals(255, block.getQualityScore());
        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, block.getQualityAlgorithmVendorIdentifier());
        assertEquals(IrisQualityAlgorithmIdentifier.UNSPECIFIED, block.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies readObject with flag completes successfully even with insufficient data.
     */
    @Test
    void readObjectWithFlagCompletesSuccessfullyWithInsufficientData() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        DataInputStream dis = new DataInputStream(bais);

        int originalQualityScore = irisQualityBlock.getQualityScore();
        int originalVendorId = irisQualityBlock.getQualityAlgorithmVendorIdentifier();
        int originalAlgorithmId = irisQualityBlock.getQualityAlgorithmIdentifier();

        irisQualityBlock.readObject(dis, true);

        assertEquals(originalQualityScore, irisQualityBlock.getQualityScore());
        assertEquals(originalVendorId, irisQualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(originalAlgorithmId, irisQualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Verifies constructor with DataInputStream and flag throws IOException when DataInputStream itself is null.
     */
    @Test
    void constructorWithDataInputStreamAndFlagThrowsIOExceptionWhenDataInputStreamIsNull() {
        assertThrows(NullPointerException.class, () -> new IrisQualityBlock((DataInputStream) null, true));
    }

    /**
     * Verifies readObject with flag throws IOException when DataInputStream is null.
     */
    @Test
    void readObjectWithFlagThrowsNullPointerExceptionWhenDataInputStreamIsNull() {
        assertThrows(NullPointerException.class, () -> irisQualityBlock.readObject((DataInputStream) null, true));
    }

}