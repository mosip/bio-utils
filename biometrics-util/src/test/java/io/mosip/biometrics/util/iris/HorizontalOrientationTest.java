package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class HorizontalOrientationTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsHorizontalOrientation() {
        HorizontalOrientation orientation = new HorizontalOrientation(HorizontalOrientation.ORIENTATION_BASE);

        assertEquals(HorizontalOrientation.ORIENTATION_BASE, orientation.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validOrientation_returnsCorrectValue() {
        HorizontalOrientation orientation = new HorizontalOrientation(HorizontalOrientation.ORIENTATION_FLIPPED);

        int result = orientation.value();

        assertEquals(HorizontalOrientation.ORIENTATION_FLIPPED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = HorizontalOrientation.fromValue(HorizontalOrientation.ORIENTATION_UNDEFINIED);

        assertEquals(HorizontalOrientation.ORIENTATION_UNDEFINIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = HorizontalOrientation.fromValue(HorizontalOrientation.ORIENTATION_FLIPPED);

        assertEquals(HorizontalOrientation.ORIENTATION_FLIPPED, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        HorizontalOrientation.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        HorizontalOrientation.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validOrientation_returnsFormattedString() {
        HorizontalOrientation orientation = new HorizontalOrientation(HorizontalOrientation.ORIENTATION_BASE);

        String result = orientation.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}