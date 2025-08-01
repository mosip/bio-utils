package io.mosip.biometrics.util.face;

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
     * Tests GeneralHeader constructor with basic parameters
     */
    @Test
    public void constructorBasicParametersCreatesGeneralHeader() {
        long totalRepresentationLength = 1000L;
        int noOfRepresentations = 1;

        GeneralHeader header = new GeneralHeader(totalRepresentationLength, noOfRepresentations);

        assertNotNull(header);
        assertEquals(FaceFormatIdentifier.FORMAT_FAC, header.getFormatIdentifier());
        assertEquals(FaceVersionNumber.VERSION_030, header.getVersionNumber());
        assertEquals(totalRepresentationLength, header.getTotalRepresentationLength());
        assertEquals(noOfRepresentations, header.getNoOfRepresentations());
        assertEquals(FaceCertificationFlag.UNSPECIFIED, header.getCertificationFlag());
        assertEquals(TemporalSequenceFlags.ONE_REPRESENTATION, header.getTemporalSemantics());
    }

    /**
     * Tests GeneralHeader constructor with all parameters
     */
    @Test
    public void constructorAllParametersCreatesGeneralHeader() {
        long formatIdentifier = FaceFormatIdentifier.FORMAT_FAC;
        long versionNumber = FaceVersionNumber.VERSION_030;
        long totalRepresentationLength = 2000L;
        int noOfRepresentations = 1;
        int certificationFlag = FaceCertificationFlag.UNSPECIFIED;
        int temporalSemantics = TemporalSequenceFlags.ONE_REPRESENTATION;

        GeneralHeader header = new GeneralHeader(formatIdentifier, versionNumber,
                totalRepresentationLength, noOfRepresentations, certificationFlag, temporalSemantics);

        assertNotNull(header);
        assertEquals(formatIdentifier, header.getFormatIdentifier());
        assertEquals(versionNumber, header.getVersionNumber());
        assertEquals(totalRepresentationLength, header.getTotalRepresentationLength());
        assertEquals(noOfRepresentations, header.getNoOfRepresentations());
        assertEquals(certificationFlag, header.getCertificationFlag());
        assertEquals(temporalSemantics, header.getTemporalSemantics());
    }

    /**
     * Tests GeneralHeader constructor with DataInputStream
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructorDataInputStreamCreatesGeneralHeader() throws Exception {
        byte[] testData = createGeneralHeaderData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        GeneralHeader header = new GeneralHeader(inputStream);

        assertNotNull(header);
    }

    /**
     * Tests GeneralHeader constructor with DataInputStream and onlyImageInformation flag
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructorDataInputStreamWithImageInfoFlagCreatesGeneralHeader() throws Exception {
        byte[] testData = createGeneralHeaderData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        GeneralHeader header = new GeneralHeader(inputStream, true);

        assertNotNull(header);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLengthValidHeaderReturnsCorrectLength() {
        GeneralHeader header = new GeneralHeader(1000L, 1);

        long recordLength = header.getRecordLength();

        assertEquals(17, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     * @throws Exception if writing fails
     */
    @Test
    public void writeObjectValidHeaderWritesDataSuccessfully() throws Exception {
        GeneralHeader header = new GeneralHeader(1500L, 1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        header.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(17, result.length);
    }

    /**
     * Tests setFormatIdentifier method sets value correctly
     */
    @Test
    public void setFormatIdentifierValidIdentifierSetsValueCorrectly() {
        GeneralHeader header = new GeneralHeader(1000L, 1);
        long newIdentifier = 0x46414300L;

        header.setFormatIdentifier(newIdentifier);

        assertEquals(newIdentifier, header.getFormatIdentifier());
    }

    /**
     * Tests setVersionNumber method sets value correctly
     */
    @Test
    public void setVersionNumberValidVersionSetsValueCorrectly() {
        GeneralHeader header = new GeneralHeader(1000L, 1);
        long newVersion = 0x30313000L;

        header.setVersionNumber(newVersion);

        assertEquals(newVersion, header.getVersionNumber());
    }

    /**
     * Tests setTotalRepresentationLength method sets value correctly
     */
    @Test
    public void setTotalRepresentationLengthValidLengthSetsValueCorrectly() {
        GeneralHeader header = new GeneralHeader(1000L, 1);
        long newLength = 2500L;

        header.setTotalRepresentationLength(newLength);

        assertEquals(newLength, header.getTotalRepresentationLength());
    }

    /**
     * Tests setNoOfRepresentations method sets value correctly
     */
    @Test
    public void setNoOfRepresentationsValidCountSetsValueCorrectly() {
        GeneralHeader header = new GeneralHeader(1000L, 1);
        int newCount = 2;

        header.setNoOfRepresentations(newCount);

        assertEquals(newCount, header.getNoOfRepresentations());
    }

    /**
     * Tests setCertificationFlag method sets value correctly
     */
    @Test
    public void setCertificationFlagValidFlagSetsValueCorrectly() {
        GeneralHeader header = new GeneralHeader(1000L, 1);
        int newFlag = 0x01;

        header.setCertificationFlag(newFlag);

        assertEquals(newFlag, header.getCertificationFlag());
    }

    /**
     * Tests setTemporalSemantics method sets value correctly
     */
    @Test
    public void setTemporalSemanticsValidSemanticsSetsValueCorrectly() {
        GeneralHeader header = new GeneralHeader(1000L, 1);
        int newSemantics = 0x0002;

        header.setTemporalSemantics(newSemantics);

        assertEquals(newSemantics, header.getTemporalSemantics());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringValidHeaderReturnsNonNullString() {
        GeneralHeader header = new GeneralHeader(1000L, 1);

        String result = header.toString();

        assertNotNull(result);
        assertTrue(result.contains("FaceGeneralHeader"));
    }

    private byte[] createGeneralHeaderData() {
        return new byte[]{
                0x46, 0x41, 0x43, 0x00,
                0x30, 0x31, 0x30, 0x00,
                0x00, 0x00, 0x04, 0x00,
                0x00, 0x01,
                0x00,
                0x00, 0x01
        };
    }
}