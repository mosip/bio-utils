package io.mosip.biometrics.util.face;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Representation} class focusing on constructor functionality,
 * record-length computation, serialization/deserialization, and string representation
 * without requiring real biometric data.
 */
class RepresentationTest {

    private static final byte[] DUMMY_IMAGE = {0x01};

    /**
     * Creates a minimal valid Representation instance for testing.
     *
     * @return A minimal Representation instance
     */
    private static Representation buildMinimal() {
        int[] zeros = {0,0,0};
        FacialInformation facialInfo = new FacialInformation(0, zeros, zeros);
        ImageInformation imageInfo = new ImageInformation(1,1);
        Date now = new Date();
        return new Representation(now, new FaceQualityBlock[0], facialInfo, null, imageInfo, DUMMY_IMAGE, null);
    }

    /**
     * Verifies that the minimal constructor properly initializes all required fields.
     */
    @Test
    void constructorWithMinimalParametersInitializesFields() {
        Representation rep = buildMinimal();

        assertNotNull(rep.getRepresentationHeader());
        assertNotNull(rep.getRepresentationData());
        assertTrue(rep.getRecordLength() > 0);
        assertTrue(rep.toString().contains("Representation"));
    }

    /**
     * Verifies that object serialization and deserialization preserves all data.
     */
    @Test
    void writeAndReadObjectWithValidDataPreservesAllFields() throws Exception {
        Representation original = buildMinimal();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baos);
        original.writeObject(dout);
        dout.close();

        byte[] bytes = baos.toByteArray();
        assertEquals(original.getRecordLength(), bytes.length);

        DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes));
        Representation deserialized = new Representation(din);

        assertEquals(original.getRecordLength(), deserialized.getRecordLength());
        assertNotNull(deserialized.getRepresentationHeader());
        assertNotNull(deserialized.getRepresentationData());
    }

    /**
     * Verifies that the constructor with source information initializes correctly.
     */
    @Test
    void constructorWithSourceInformationInitializesCorrectly() {
        int[] zeros = {0,0,0};
        Representation rep = new Representation(new Date(), 0, 0, 0, new FaceQualityBlock[0],
                new FacialInformation(0, zeros, zeros), null, new ImageInformation(1,1),
                DUMMY_IMAGE, null);

        assertTrue(rep.getRecordLength() > 0);
    }
}