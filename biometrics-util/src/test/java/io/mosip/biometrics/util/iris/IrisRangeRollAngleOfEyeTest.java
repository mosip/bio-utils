package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IrisRangeRollAngleOfEye}.
 */
public class IrisRangeRollAngleOfEyeTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void testConstructorAndValue_Valid() {
        IrisRangeRollAngleOfEye angle = new IrisRangeRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000);
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000, angle.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void testFromValue_MinValid() {
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000, IrisRangeRollAngleOfEye.fromValue(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void testFromValue_MaxValid() {
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED, IrisRangeRollAngleOfEye.fromValue(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void testFromValue_MiddleValid() {
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE, IrisRangeRollAngleOfEye.fromValue(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_BelowRange() {
        IrisRangeRollAngleOfEye.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_AboveRange() {
        IrisRangeRollAngleOfEye.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void testToString() {
        IrisRangeRollAngleOfEye angle = new IrisRangeRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE);
        String str = angle.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE)));
    }
} 