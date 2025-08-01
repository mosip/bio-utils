package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FingerCertificationBlockTest {

    /**
     * Tests default constructor
     */
    @Test
    public void constructorDefaultCreatesCertificationBlock() {
        FingerCertificationBlock certificationBlock = new FingerCertificationBlock();

        assertNotNull(certificationBlock);
        assertEquals(FingerCertificationAuthorityID.UNSPECIFIED, certificationBlock.getCertificationAuthorityID());
        assertEquals(FingerCertificationSchemeIdentifier.UNSPECIFIED, certificationBlock.getCertificationSchemeIdentifier());
    }

    /**
     * Tests constructor with parameters
     */
    @Test
    public void constructorWithParametersCreatesCertificationBlock() {
        int certificationAuthorityID = FingerCertificationAuthorityID.GREEN_BIT_AMERICAS_INC;
        int certificationSchemeIdentifier = FingerCertificationSchemeIdentifier.IMAGE_QUALITY_SPECIFICATION_FOR_AFIS_SYSTEM;

        FingerCertificationBlock certificationBlock = new FingerCertificationBlock(certificationAuthorityID, certificationSchemeIdentifier);

        assertNotNull(certificationBlock);
        assertEquals(certificationAuthorityID, certificationBlock.getCertificationAuthorityID());
        assertEquals(certificationSchemeIdentifier, certificationBlock.getCertificationSchemeIdentifier());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructorDataInputStreamCreatesCertificationBlock() throws Exception {
        byte[] testData = {0x00, 0x40, 0x01};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FingerCertificationBlock certificationBlock = new FingerCertificationBlock(inputStream);

        assertNotNull(certificationBlock);
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructorDataInputStreamWithImageInfoFlagCreatesCertificationBlock() throws Exception {
        byte[] testData = {0x00, 0x01, 0x02};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FingerCertificationBlock certificationBlock = new FingerCertificationBlock(inputStream, true);

        assertNotNull(certificationBlock);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLengthReturnsCorrectLength() {
        FingerCertificationBlock certificationBlock = new FingerCertificationBlock();

        long recordLength = certificationBlock.getRecordLength();

        assertEquals(3, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObjectWritesDataSuccessfully() throws Exception {
        FingerCertificationBlock certificationBlock = new FingerCertificationBlock(0x1234, 0x02);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        certificationBlock.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    /**
     * Tests setCertificationAuthorityID method sets value correctly
     */
    @Test
    public void setCertificationAuthorityIdSetsValueCorrectly() {
        FingerCertificationBlock certificationBlock = new FingerCertificationBlock();
        int newID = 0x5678;

        certificationBlock.setCertificationAuthorityID(newID);

        assertEquals(newID, certificationBlock.getCertificationAuthorityID());
    }

    /**
     * Tests setCertificationSchemeIdentifier method sets value correctly
     */
    @Test
    public void setCertificationSchemeIdentifierSetsValueCorrectly() {
        FingerCertificationBlock certificationBlock = new FingerCertificationBlock();
        int newIdentifier = FingerCertificationSchemeIdentifier.IMAGE_QUALITY_SPECIFICATION_FOR_PERSONAL_VERIFICATION;

        certificationBlock.setCertificationSchemeIdentifier(newIdentifier);

        assertEquals(newIdentifier, certificationBlock.getCertificationSchemeIdentifier());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringReturnsNonNullString() {
        FingerCertificationBlock certificationBlock = new FingerCertificationBlock(0xABCD, 0x03);

        String result = certificationBlock.toString();

        assertNotNull(result);
        assertTrue(result.contains("FingerCertificationBlock"));
    }
}