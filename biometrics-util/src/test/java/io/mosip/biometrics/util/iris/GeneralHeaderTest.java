package io.mosip.biometrics.util.iris;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link GeneralHeader} class.
 * This class verifies the functionality of the GeneralHeader class which represents
 * the general header record in an ISO/IEC 19794-6:2011 compliant iris image data.
 */
class GeneralHeaderTest {

    private static final long FORMAT_IDENTIFIER = 0x49495200L; // 'IIR ' in ASCII
    private static final long VERSION_NUMBER = 0x30323000L;    // '020 ' in ASCII
    private static final long TOTAL_REP_LENGTH = 100L;
    private static final int NO_OF_REPRESENTATIONS = 2;
    private static final int CERTIFICATION_FLAG = 0; // UNSPECIFIED
    private static final int NO_OF_EYES = 2;

    private GeneralHeader generalHeader;

    @BeforeEach
    void setup() {
        generalHeader = new GeneralHeader(TOTAL_REP_LENGTH, NO_OF_REPRESENTATIONS, NO_OF_EYES);
    }

    /**
     * Tests that the default constructor initializes all fields with expected default values.
     */
    @Test
    void constructorUsingDefaultInitializesFieldsWithDefaults() {
        assertEquals(FORMAT_IDENTIFIER, generalHeader.getFormatIdentifier());
        assertEquals(VERSION_NUMBER, generalHeader.getVersionNumber());
        assertEquals(TOTAL_REP_LENGTH, generalHeader.getTotalRepresentationLength());
        assertEquals(NO_OF_REPRESENTATIONS, generalHeader.getNoOfRepresentations());
        assertEquals(CERTIFICATION_FLAG, generalHeader.getCertificationFlag());
        assertEquals(NO_OF_EYES, generalHeader.getNoOfEyesPresent());
        assertEquals(16, generalHeader.getRecordLength());
    }

    /**
     * Tests that the full constructor correctly sets all provided field values.
     */
    @Test
    void constructorUsingAllParametersSetsAllFields() {
        GeneralHeader header = new GeneralHeader(
                FORMAT_IDENTIFIER,
                VERSION_NUMBER,
                TOTAL_REP_LENGTH,
                NO_OF_REPRESENTATIONS,
                CERTIFICATION_FLAG,
                NO_OF_EYES
        );

        assertAll(
                () -> assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier()),
                () -> assertEquals(VERSION_NUMBER, header.getVersionNumber()),
                () -> assertEquals(TOTAL_REP_LENGTH, header.getTotalRepresentationLength()),
                () -> assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations()),
                () -> assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag()),
                () -> assertEquals(NO_OF_EYES, header.getNoOfEyesPresent())
        );
    }

    /**
     * Tests that the constructor with DataInputStream correctly parses and sets all fields.
     */
    @Test
    void constructorUsingDataInputStreamParsesHeaderCorrectly() throws IOException {
        byte[] inputData = createInputData();
        GeneralHeader header = new GeneralHeader(new DataInputStream(new ByteArrayInputStream(inputData)));

        assertAll(
                () -> assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier()),
                () -> assertEquals(VERSION_NUMBER, header.getVersionNumber()),
                () -> assertEquals(TOTAL_REP_LENGTH - 16, header.getTotalRepresentationLength()),
                () -> assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations()),
                () -> assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag()),
                () -> assertEquals(NO_OF_EYES, header.getNoOfEyesPresent())
        );
    }

    /**
     * Tests that the imageInfoOnly constructor only parses essential fields.
     */
    @Test
    void constructorImageInfoOnlyParsesEssentialFields() throws IOException {
        byte[] inputData = createInputData();
        GeneralHeader header = new GeneralHeader(new DataInputStream(new ByteArrayInputStream(inputData)), true);

        assertAll(
                () -> assertEquals(TOTAL_REP_LENGTH - 16, header.getTotalRepresentationLength()),
                () -> assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations()),
                () -> assertEquals(16, header.getRecordLength())
        );
    }

    /**
     * Tests that writeObject correctly serializes the header to binary format.
     */
    @Test
    void writeObjectSerializesHeaderCorrectly() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        generalHeader.writeObject(new DataOutputStream(baos));
        byte[] output = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(output));

        assertAll(
                () -> assertEquals(FORMAT_IDENTIFIER, dis.readInt() & 0xFFFFFFFFL),
                () -> assertEquals(VERSION_NUMBER, dis.readInt() & 0xFFFFFFFFL),
                () -> assertEquals(TOTAL_REP_LENGTH + 16, dis.readInt() & 0xFFFFFFFFL),
                () -> assertEquals(NO_OF_REPRESENTATIONS, dis.readUnsignedShort()),
                () -> assertEquals(CERTIFICATION_FLAG, dis.readUnsignedByte()),
                () -> assertEquals(NO_OF_EYES, dis.readUnsignedByte())
        );
    }

    /**
     * Tests that all setter methods correctly update their respective fields.
     */
    @Test
    void settersUpdateFieldsCorrectly() {
        GeneralHeader header = new GeneralHeader(0L, 0, 0);

        header.setFormatIdentifier(FORMAT_IDENTIFIER);
        header.setVersionNumber(VERSION_NUMBER);
        header.setTotalRepresentationLength(TOTAL_REP_LENGTH);
        header.setNoOfRepresentations(NO_OF_REPRESENTATIONS);
        header.setCertificationFlag(CERTIFICATION_FLAG);
        header.setNoOfEyesPresent(NO_OF_EYES);

        assertAll(
                () -> assertEquals(FORMAT_IDENTIFIER, header.getFormatIdentifier()),
                () -> assertEquals(VERSION_NUMBER, header.getVersionNumber()),
                () -> assertEquals(TOTAL_REP_LENGTH, header.getTotalRepresentationLength()),
                () -> assertEquals(NO_OF_REPRESENTATIONS, header.getNoOfRepresentations()),
                () -> assertEquals(CERTIFICATION_FLAG, header.getCertificationFlag()),
                () -> assertEquals(NO_OF_EYES, header.getNoOfEyesPresent())
        );
    }

    /**
     * Tests that toString returns a non-null string containing all field values.
     */
    @Test
    void toStringReturnsFormattedString() {
        String result = generalHeader.toString();

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("GeneralHeaderRecordLength=16")),
                () -> assertTrue(result.contains("formatIdentifier=" + FORMAT_IDENTIFIER)),
                () -> assertTrue(result.contains("versionNumber=" + VERSION_NUMBER)),
                () -> assertTrue(result.contains("totalRepresentationLength=" + TOTAL_REP_LENGTH)),
                () -> assertTrue(result.contains("noOfRepresentations=" + Integer.toHexString(NO_OF_REPRESENTATIONS))),
                () -> assertTrue(result.contains("certificationFlag=" + Integer.toHexString(CERTIFICATION_FLAG))),
                () -> assertTrue(result.contains("noOfEyesPresent=" + Integer.toHexString(NO_OF_EYES)))
        );
    }

    /**
     * Creates a byte array representing a valid GeneralHeader for testing.
     */
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
}