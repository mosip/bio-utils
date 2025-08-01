package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FaceQualityBlockTest {

    /**
     * Tests FaceQualityBlock constructor with quality score only
     */
    @Test
    public void constructorWithQualityScoreOnly() {
        int qualityScore = 80;

        FaceQualityBlock qualityBlock = new FaceQualityBlock(qualityScore);

        assertNotNull(qualityBlock);
        assertEquals(qualityScore, qualityBlock.getQualityScore());
        assertEquals(FaceQualityAlgorithmVendorIdentifier.UNSPECIFIED,
                qualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(FaceQualityAlgorithmIdentifier.UNSPECIFIED,
                qualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Tests FaceQualityBlock constructor with all parameters
     */
    @Test
    public void constructorWithAllParameters() {
        int qualityScore = 90;
        int vendorIdentifier = FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001;
        int algorithmIdentifier = FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001;

        FaceQualityBlock qualityBlock = new FaceQualityBlock(qualityScore, vendorIdentifier, algorithmIdentifier);

        assertNotNull(qualityBlock);
        assertEquals(qualityScore, qualityBlock.getQualityScore());
        assertEquals(vendorIdentifier, qualityBlock.getQualityAlgorithmVendorIdentifier());
        assertEquals(algorithmIdentifier, qualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Tests FaceQualityBlock constructor with DataInputStream
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructorWithDataInputStream() throws Exception {
        byte[] testData = createQualityBlockData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FaceQualityBlock qualityBlock = new FaceQualityBlock(inputStream);

        assertNotNull(qualityBlock);
    }

    /**
     * Tests FaceQualityBlock constructor with DataInputStream and onlyImageInformation flag
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructorWithDataInputStreamAndImageInfoFlag() throws Exception {
        byte[] testData = createQualityBlockData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FaceQualityBlock qualityBlock = new FaceQualityBlock(inputStream, true);

        assertNotNull(qualityBlock);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLengthReturnsCorrectLength() {
        FaceQualityBlock qualityBlock = new FaceQualityBlock(50);

        long recordLength = qualityBlock.getRecordLength();

        assertEquals(5, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     * @throws Exception if writing fails
     */
    @Test
    public void writeObjectWritesDataSuccessfully() throws Exception {
        FaceQualityBlock qualityBlock = new FaceQualityBlock(75,
                FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001,
                FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        qualityBlock.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(5, result.length);
    }

    /**
     * Tests setQualityScore method sets value correctly
     */
    @Test
    public void setQualityScoreSetsValueCorrectly() {
        FaceQualityBlock qualityBlock = new FaceQualityBlock(50);
        int newScore = 85;

        qualityBlock.setQualityScore(newScore);

        assertEquals(newScore, qualityBlock.getQualityScore());
    }

    /**
     * Tests setQualityAlgorithmVendorIdentifier method sets value correctly
     */
    @Test
    public void setQualityAlgorithmVendorIdentifierSetsValueCorrectly() {
        FaceQualityBlock qualityBlock = new FaceQualityBlock(50);
        int newIdentifier = FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001;

        qualityBlock.setQualityAlgorithmVendorIdentifier(newIdentifier);

        assertEquals(newIdentifier, qualityBlock.getQualityAlgorithmVendorIdentifier());
    }

    /**
     * Tests setQualityAlgorithmIdentifier method sets value correctly
     */
    @Test
    public void setQualityAlgorithmIdentifierSetsValueCorrectly() {
        FaceQualityBlock qualityBlock = new FaceQualityBlock(50);
        int newIdentifier = FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001;

        qualityBlock.setQualityAlgorithmIdentifier(newIdentifier);

        assertEquals(newIdentifier, qualityBlock.getQualityAlgorithmIdentifier());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringReturnsNonNullString() {
        FaceQualityBlock qualityBlock = new FaceQualityBlock(60);

        String result = qualityBlock.toString();

        assertNotNull(result);
        assertTrue(result.contains("QualityBlock"));
    }

    /**
     * Tests quality score boundary values
     */
    @Test
    public void qualityScoreBoundaryValuesHandledCorrectly() {
        FaceQualityBlock qualityBlock1 = new FaceQualityBlock(0);
        FaceQualityBlock qualityBlock2 = new FaceQualityBlock(100);
        FaceQualityBlock qualityBlock3 = new FaceQualityBlock(255);

        assertEquals(0, qualityBlock1.getQualityScore());
        assertEquals(100, qualityBlock2.getQualityScore());
        assertEquals(255, qualityBlock3.getQualityScore());
    }

    private byte[] createQualityBlockData() {
        return new byte[]{50, 0, 1, 0, 1};
    }
}