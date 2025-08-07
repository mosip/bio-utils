package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerPositionTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesFingerPositionCorrectly() {
        FingerPosition fingerPosition = new FingerPosition(FingerPosition.RIGHT_THUMB);

        assertEquals(FingerPosition.RIGHT_THUMB, fingerPosition.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerPosition fingerPosition = new FingerPosition(FingerPosition.LEFT_INDEX_FINGER);

        int result = fingerPosition.value();

        assertEquals(FingerPosition.LEFT_INDEX_FINGER, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerPosition.fromValue(FingerPosition.UNKNOWN);

        assertEquals(FingerPosition.UNKNOWN, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerPosition.fromValue(FingerPosition.LEFT_MIDDLE_AND_RING_AND_LITTLE_FINGERS);

        assertEquals(FingerPosition.LEFT_MIDDLE_AND_RING_AND_LITTLE_FINGERS, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerPosition.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerPosition.fromValue(0x33);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerPosition fingerPosition = new FingerPosition(FingerPosition.RIGHT_MIDDLE_FINGER);

        String result = fingerPosition.toString();

        assertNotNull(result);
        assertTrue(result.contains("3"));
    }
}