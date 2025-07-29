package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class VerticalOrientationTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsVerticalOrientation() {
        VerticalOrientation orientation = new VerticalOrientation(VerticalOrientation.ORIENTATION_BASE);

        assertEquals(VerticalOrientation.ORIENTATION_BASE, orientation.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validOrientation_returnsCorrectValue() {
        VerticalOrientation orientation = new VerticalOrientation(VerticalOrientation.ORIENTATION_FLIPPED);

        int result = orientation.value();

        assertEquals(VerticalOrientation.ORIENTATION_FLIPPED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = VerticalOrientation.fromValue(VerticalOrientation.ORIENTATION_UNDEFINIED);

        assertEquals(VerticalOrientation.ORIENTATION_UNDEFINIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = VerticalOrientation.fromValue(VerticalOrientation.ORIENTATION_FLIPPED);

        assertEquals(VerticalOrientation.ORIENTATION_FLIPPED, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        VerticalOrientation.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        VerticalOrientation.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validOrientation_returnsFormattedString() {
        VerticalOrientation orientation = new VerticalOrientation(VerticalOrientation.ORIENTATION_BASE);

        String result = orientation.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}