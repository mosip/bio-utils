package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class GeneralHeaderTest {

    /**
     * Tests constructor with basic parameters
     */
    @Test
    public void constructorWithBasicParametersCreatesHeader() {
        long totalRepresentationLength = 1000L;
        int noOfRepresentations = 2;
        int noOfFingerPresent = 5;

        GeneralHeader generalHeader = new GeneralHeader(totalRepresentationLength, noOfRepresentations, noOfFingerPresent);

        assertNotNull(generalHeader);
        assertEquals(FingerFormatIdentifier.FORMAT_FIR, generalHeader.getFormatIdentifier());
        assertEquals(FingerVersionNumber.VERSION_020, generalHeader.getVersionNumber());
        assertEquals(totalRepresentationLength, generalHeader.getTotalRepresentationLength());
        assertEquals(noOfRepresentations, generalHeader.getNoOfRepresentations());
        assertEquals(FingerCertificationFlag.UNSPECIFIED, generalHeader.getCertificationFlag());
        assertEquals(noOfFingerPresent, generalHeader.getNoOfFingerPresent());
    }

    /**
     * Tests constructor with all parameters
     */
    @Test
    public void constructorWithAllParametersCreatesHeader() {
        long formatIdentifier = FingerFormatIdentifier.FORMAT_FIR;
        long versionNumber = FingerVersionNumber.VERSION_020;
        long totalRepresentationLength = 2000L;
        int noOfRepresentations = 3;
        int certificationFlag = FingerCertificationFlag.ONE;
        int noOfFingerPresent = 10;

        GeneralHeader generalHeader = new GeneralHeader(formatIdentifier, versionNumber,
                totalRepresentationLength, noOfRepresentations, certificationFlag, noOfFingerPresent);

        assertNotNull(generalHeader);
        assertEquals(formatIdentifier, generalHeader.getFormatIdentifier());
        assertEquals(versionNumber, generalHeader.getVersionNumber());
        assertEquals(totalRepresentationLength, generalHeader.getTotalRepresentationLength());
        assertEquals(noOfRepresentations, generalHeader.getNoOfRepresentations());
        assertEquals(certificationFlag, generalHeader.getCertificationFlag());
        assertEquals(noOfFingerPresent, generalHeader.getNoOfFingerPresent());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructorDataInputStreamCreatesHeader() throws Exception {
        byte[] testData = createTestData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        GeneralHeader generalHeader = new GeneralHeader(inputStream);

        assertNotNull(generalHeader);
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructorDataInputStreamWithImageInfoFlagCreatesHeader() throws Exception {
        byte[] testData = createTestData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        GeneralHeader generalHeader = new GeneralHeader(inputStream, true);

        assertNotNull(generalHeader);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLengthReturnsCorrectLength() {
        GeneralHeader generalHeader = new GeneralHeader(1500L, 1, 3);

        long recordLength = generalHeader.getRecordLength();

        assertEquals(16, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObjectWritesDataSuccessfully() throws Exception {
        GeneralHeader generalHeader = new GeneralHeader(800L, 2, 4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        generalHeader.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(16, result.length);
    }

    /**
     * Tests setter and getter methods
     */
    @Test
    public void settersAndGettersWorkCorrectly() {
        GeneralHeader generalHeader = new GeneralHeader(1000L, 1, 2);

        generalHeader.setFormatIdentifier(0x12345678L);
        generalHeader.setVersionNumber(0x87654321L);
        generalHeader.setTotalRepresentationLength(3000L);
        generalHeader.setNoOfRepresentations(5);
        generalHeader.setCertificationFlag(FingerCertificationFlag.ONE);
        generalHeader.setNoOfFingerPresent(8);

        assertEquals(0x12345678L, generalHeader.getFormatIdentifier());
        assertEquals(0x87654321L, generalHeader.getVersionNumber());
        assertEquals(3000L, generalHeader.getTotalRepresentationLength());
        assertEquals(5, generalHeader.getNoOfRepresentations());
        assertEquals(FingerCertificationFlag.ONE, generalHeader.getCertificationFlag());
        assertEquals(8, generalHeader.getNoOfFingerPresent());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringReturnsNonNullString() {
        GeneralHeader generalHeader = new GeneralHeader(1200L, 3, 6);

        String result = generalHeader.toString();

        assertNotNull(result);
        assertTrue(result.contains("IrisGeneralHeader"));
    }

    private byte[] createTestData() {
        return new byte[]{
                0x46, 0x49, 0x52, 0x00, // Format identifier
                0x30, 0x32, 0x30, 0x00, // Version number
                0x00, 0x00, 0x04, 0x00, // Total representation length
                0x00, 0x02, // Number of representations
                0x00, // Certification flag
                0x05  // Number of fingers present
        };
    }
}