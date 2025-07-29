package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link IrisRangeRollAngleUncertainty}.
 */
public class IrisRangeRollAngleUncertaintyTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void constructorAndValue_validRollUncertainty_returnsSameValue() {
        IrisRangeRollAngleUncertainty uncertainty = new IrisRangeRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000);
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000, uncertainty.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void fromValue_minValidValue_returnsCorrectInstance() {
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000,
                IrisRangeRollAngleUncertainty.fromValue(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_0000));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void fromValue_maxValidValue_returnsCorrectInstance() {
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED,
                IrisRangeRollAngleUncertainty.fromValue(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_UNDEFINIED));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void fromValue_middleValidValue_returnsCorrectInstance() {
        assertEquals(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE,
                IrisRangeRollAngleUncertainty.fromValue(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_valueBelowRange_throwsIllegalArgumentException() {
        IrisRangeRollAngleUncertainty.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_valueAboveRange_throwsIllegalArgumentException() {
        IrisRangeRollAngleUncertainty.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void toString_rollUncertaintyValue_containsHexValue() {
        IrisRangeRollAngleUncertainty uncertainty = new IrisRangeRollAngleUncertainty(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE);
        String str = uncertainty.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisRangeRollAngleUncertainty.ROLL_UNCERTAIN_FFFE)));
    }
}