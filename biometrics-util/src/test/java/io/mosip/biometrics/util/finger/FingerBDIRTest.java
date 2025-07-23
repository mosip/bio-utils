package io.mosip.biometrics.util.finger;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.mosip.biometrics.util.exception.BiometricUtilException;
import io.mosip.biometrics.util.constant.BiometricUtilErrorCode;

/**
 * Unit tests for {@link FingerBDIR}, covering constructors, stream reads, and getters.
 */
class FingerBDIRTest {

    private static final long FMT_ID = 0xABCDEL;
    private static final long VER_NUM = 0x1234L;
    private static final int CERT_FLAG = 7;
    private static final int NO_REPS = 1;
    private static final int FINGER_POS = 2;
    private static final int REP_NO = 3;
    private static final int SCALE_UNIT = 4;
    private static final int NO_FINGERS = 1;
    private static final byte[] IMAGE = new byte[] { 0x0A, 0x0B };
    private static final Date FIXED_DATE = new GregorianCalendar(2020, Calendar.MARCH, 15, 10, 20, 30).getTime();
    private static final FingerQualityBlock[] QBS = {};
    private static final FingerCertificationBlock[] CBS = {};

    private FingerBDIR simpleBdir;

    @BeforeEach
    void setUp() {
        simpleBdir = new FingerBDIR(
                FMT_ID, VER_NUM, CERT_FLAG, FIXED_DATE, NO_REPS,
                QBS, CBS, FINGER_POS, REP_NO, SCALE_UNIT, NO_FINGERS, IMAGE
        );
    }

    /**
     * Tests the simple constructor and basic getters.
     */
    @Test
    void constructor_simpleParameters_setsHeaderAndRepresentation() {
        assertEquals(FMT_ID, simpleBdir.getFormatIdentifier());
        assertArrayEquals(IMAGE, simpleBdir.getImage());
    }

    /**
     * Stream constructor should throw EOFException for incomplete stream.
     */
    @Test
    void constructor_dataInputStream_incompleteStream_throwsEOFException() throws Exception {
        byte[] partial = new byte[] {0x00};
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(partial))) {
            assertThrows(EOFException.class, () -> new FingerBDIR(dis));
        }
    }

    /**
     * Stream constructor with onlyImageInformation=true should throw EOFException.
     */
    @Test
    void constructor_dataInputStream_onlyImageInfoTrue_incompleteStream_throwsEOFException() throws Exception {
        byte[] partial = new byte[] {0x00};
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(partial))) {
            assertThrows(EOFException.class, () -> new FingerBDIR(dis, true));
        }
    }

    /**
     * Ensures getRepresentation throws when internal list is null.
     */
    @Test
    void getRepresentation_noRepresentation_throwsBiometricUtilException() throws Exception {
        Field reprField = FingerBDIR.class.getDeclaredField("representation");
        reprField.setAccessible(true);
        reprField.set(simpleBdir, null);

        BiometricUtilException ex = assertThrows(BiometricUtilException.class,
                () -> simpleBdir.getRepresentation());
        assertEquals(
                BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorCode(),
                ex.getErrorCode()
        );
    }

    /**
     * Tests indexed setRepresentation and retrieval.
     */
    @Test
    void setRepresentation_indexed_thenGetByIndex_returnsCorrect() {
        simpleBdir.setRepresentation(simpleBdir.getRepresentation(), 0);
        assertNotNull(simpleBdir.getRepresentation(0));
    }

    /**
     * Delegated getters should return valid primitives and null for optional blocks.
     */
    @Test
    void delegatedGetters_validatePrimitiveAndNullFields() {
        assertEquals(FIXED_DATE, simpleBdir.getCaptureDateTime());
        assertEquals(NO_REPS, simpleBdir.getNoOfRepresentations());
        // optional blocks not set in simple constructor
        assertNull(simpleBdir.getSegmentationBlock());
        assertNull(simpleBdir.getAnnotationBlock());
        assertNull(simpleBdir.getCommentBlocks());
    }

    /**
     * toString should include nested fields.
     */
    @Test
    void toString_containsNestedFields() {
        String out = simpleBdir.toString();
        assertTrue(out.contains("generalHeader="));
        assertTrue(out.contains("representation="));
    }
}
