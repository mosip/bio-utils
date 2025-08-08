package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HorizontalOrientationTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesOrientationCorrectly() {
        HorizontalOrientation orientation = new HorizontalOrientation(HorizontalOrientation.ORIENTATION_BASE);

        assertEquals(HorizontalOrientation.ORIENTATION_BASE, orientation.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        HorizontalOrientation orientation = new HorizontalOrientation(HorizontalOrientation.ORIENTATION_FLIPPED);

        int result = orientation.value();

        assertEquals(HorizontalOrientation.ORIENTATION_FLIPPED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = HorizontalOrientation.fromValue(HorizontalOrientation.ORIENTATION_UNDEFINIED);

        assertEquals(HorizontalOrientation.ORIENTATION_UNDEFINIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = HorizontalOrientation.fromValue(HorizontalOrientation.ORIENTATION_FLIPPED);

        assertEquals(HorizontalOrientation.ORIENTATION_FLIPPED, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        HorizontalOrientation.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        HorizontalOrientation.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        HorizontalOrientation orientation = new HorizontalOrientation(HorizontalOrientation.ORIENTATION_BASE);

        String result = orientation.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}