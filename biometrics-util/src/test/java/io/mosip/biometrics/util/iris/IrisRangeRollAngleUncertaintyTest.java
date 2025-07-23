package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IrisRangeRollAngleUncertainty}.
 */
public class IrisRangeRollAngleUncertaintyTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void testConstructorAndValue_Valid() {
        IrisRangeRollAngleUncertainty uncertainty = new IrisRangeRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000);
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000, uncertainty.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void testFromValue_MinValid() {
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000, IrisRangeRollAngleUncertainty.fromValue(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void testFromValue_MaxValid() {
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED, IrisRangeRollAngleUncertainty.fromValue(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void testFromValue_MiddleValid() {
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE, IrisRangeRollAngleUncertainty.fromValue(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_BelowRange() {
        IrisRangeRollAngleUncertainty.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_AboveRange() {
        IrisRangeRollAngleUncertainty.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void testToString() {
        IrisRangeRollAngleUncertainty uncertainty = new IrisRangeRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE);
        String str = uncertainty.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE)));
    }
} 