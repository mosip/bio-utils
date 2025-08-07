package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link IrisRangeRollAngleOfEye}.
 */
public class IrisRangeRollAngleOfEyeTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void constructorAndValueReturnsCorrectValue() {
        IrisRangeRollAngleOfEye angle = new IrisRangeRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000);
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000, angle.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000, IrisRangeRollAngleOfEye.fromValue(IrisRangeRollAngleOfEye.ROLL_ANGLE_0000));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED, IrisRangeRollAngleOfEye.fromValue(IrisRangeRollAngleOfEye.ROLL_ANGLE_UNDEFINIED));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        assertEquals(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE, IrisRangeRollAngleOfEye.fromValue(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisRangeRollAngleOfEye.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisRangeRollAngleOfEye.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void toStringReturnsNonNullHexString() {
        IrisRangeRollAngleOfEye angle = new IrisRangeRollAngleOfEye(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE);
        String str = angle.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisRangeRollAngleOfEye.ROLL_ANGLE_FFFE)));
    }
}