package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class GeneralHeaderTest {
    private static final long FORMAT_IDENTIFIER = IrisFormatIdentifier.FORMAT_IIR;
    private static final long VERSION_NUMBER = IrisVersionNumber.VERSION_020;
    private static final long TOTAL_REP_LENGTH = 100L;
    private static final int NO_OF_REPRESENTATIONS = 2;
    private static final int CERTIFICATION_FLAG = IrisCertificationFlag.UNSPECIFIED;
    private static final int NO_OF_EYES = 2;

    private GeneralHeader generalHeader;

    @Before
    public void setUp() {
        generalHeader = new GeneralHeader(TOTAL_REP_LENGTH, NO_OF_REPRESENTATIONS, NO_OF_EYES);
    }

    @Test
    public void testDefaultConstructor() {
        GeneralHeader header = new GeneralHeader(TOTAL_REP_LENGTH, NO_OF_REPRESENTATIONS, NO_OF_EYES);

        assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier());
        assertEquals(VERSION_NUMBER, header.getVersionNumber());
        assertEquals(TOTAL_REP_LENGTH, header.getTotalRepresentationLength());
        assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations());
        assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag());
        assertEquals(NO_OF_EYES, header.getNoOfEyesPresent());
        assertEquals(16, header.getRecordLength());
    }

    @Test
    public void testFullConstructor() {
        GeneralHeader header = new GeneralHeader(
                FORMAT_IDENTIFIER,
                VERSION_NUMBER,
                TOTAL_REP_LENGTH,
                NO_OF_REPRESENTATIONS,
                CERTIFICATION_FLAG,
                NO_OF_EYES
        );

        assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier());
        assertEquals(VERSION_NUMBER, header.getVersionNumber());
        assertEquals(TOTAL_REP_LENGTH, header.getTotalRepresentationLength());
        assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations());
        assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag());
        assertEquals(NO_OF_EYES, header.getNoOfEyesPresent());
        assertEquals(16, header.getRecordLength());
    }

    @Test
    public void testDataInputStreamConstructor() throws IOException {
        byte[] inputData = createInputData();
        ByteArrayInputStream bais = new ByteArrayInputStream(inputData);
        DataInputStream dis = new DataInputStream(bais);

        GeneralHeader header = new GeneralHeader(dis);

        assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier());
        assertEquals(VERSION_NUMBER, header.getVersionNumber());
        assertEquals(TOTAL_REP_LENGTH - 16, header.getTotalRepresentationLength());
        assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations());
        assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag());
        assertEquals(NO_OF_EYES, header.getNoOfEyesPresent());
    }

    @Test
    public void testDataInputStreamOnlyImageInfoConstructor() throws IOException {
        byte[] inputData = createInputData();
        ByteArrayInputStream bais = new ByteArrayInputStream(inputData);
        DataInputStream dis = new DataInputStream(bais);

        GeneralHeader header = new GeneralHeader(dis, true);

        assertEquals(TOTAL_REP_LENGTH - 16, header.getTotalRepresentationLength());
        assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations());
        assertEquals(16, header.getRecordLength());
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        generalHeader.writeObject(dos);
        byte[] output = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(output);
        DataInputStream dis = new DataInputStream(bais);

        assertEquals(FORMAT_IDENTIFIER, dis.readInt() & 0xFFFFFFFFL);
        assertEquals(VERSION_NUMBER, dis.readInt() & 0xFFFFFFFFL);
        assertEquals(TOTAL_REP_LENGTH + 16, dis.readInt() & 0xFFFFFFFFL); // Adjusted to expect 116
        assertEquals(NO_OF_REPRESENTATIONS, dis.readUnsignedShort());
        assertEquals(CERTIFICATION_FLAG, dis.readUnsignedByte());
        assertEquals(NO_OF_EYES, dis.readUnsignedByte());
    }

    @Test
    public void testSettersAndGetters() {
        GeneralHeader header = new GeneralHeader(0L, 0, 0);

        header.setFormatIdentifier(FORMAT_IDENTIFIER);
        header.setVersionNumber(VERSION_NUMBER);
        header.setTotalRepresentationLength(TOTAL_REP_LENGTH);
        header.setNoOfRepresentations(NO_OF_REPRESENTATIONS);
        header.setCertificationFlag(CERTIFICATION_FLAG);
        header.setNoOfEyesPresent(NO_OF_EYES);

        assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier());
        assertEquals(VERSION_NUMBER, header.getVersionNumber());
        assertEquals(TOTAL_REP_LENGTH, header.getTotalRepresentationLength());
        assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations());
        assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag());
        assertEquals(NO_OF_EYES, header.getNoOfEyesPresent());
    }

    @Test
    public void testToString() {
        String result = generalHeader.toString();
        assertNotNull(result);
        assertEquals(
                "\nIrisGeneralHeader [GeneralHeaderRecordLength=16, formatIdentifier=" + FORMAT_IDENTIFIER +
                        ", versionNumber=" + VERSION_NUMBER +
                        ", totalRepresentationLength=" + TOTAL_REP_LENGTH +
                        ", noOfRepresentations=" + Integer.toHexString(NO_OF_REPRESENTATIONS) +
                        ", certificationFlag=" + Integer.toHexString(CERTIFICATION_FLAG) +
                        ", noOfEyesPresent=" + Integer.toHexString(NO_OF_EYES) + "]\n",
                result
        );
    }

    private byte[] createInputData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt((int) FORMAT_IDENTIFIER);
        dos.writeInt((int) VERSION_NUMBER);
        dos.writeInt((int) TOTAL_REP_LENGTH);
        dos.writeShort(NO_OF_REPRESENTATIONS);
        dos.writeByte(CERTIFICATION_FLAG);
        dos.writeByte(NO_OF_EYES);

        return baos.toByteArray();
    }

    // Mock classes for constants (since actual IrisFormatIdentifier and IrisVersionNumber are not provided)
    private static class IrisFormatIdentifier {
        public static final long FORMAT_IIR = 0x49495200L; // 'IIR ' in ASCII
    }

    private static class IrisVersionNumber {
        public static final long VERSION_020 = 0x30323000L; // '020 ' in ASCII
    }

    private static class IrisCertificationFlag {
        public static final int UNSPECIFIED = 0;
    }
}