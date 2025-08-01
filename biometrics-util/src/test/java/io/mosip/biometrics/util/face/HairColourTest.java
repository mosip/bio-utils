package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for the {@link HairColour} class.
 * <p>
 * This class validates the behavior of HairColour’s constructor,
 * value accessor, fromValue validation, and toString formatting.
 * It also verifies that unsupported values raise the expected exceptions.
 */
public class HairColourTest {

    /**
     * Tests the {@link HairColour} constructor with a valid value.
     * Verifies that construction succeeds and value() returns the correct constant.
     */
    @Test
    public void constructorValidValueCreatesHairColour() {
        HairColour hairColour = new HairColour(HairColour.BROWN);
        assertEquals(HairColour.BROWN, hairColour.value());
    }

    /**
     * Tests that value() correctly returns the assigned hair colour constant.
     */
    @Test
    public void valueValidHairColourReturnsCorrectValue() {
        HairColour hairColour = new HairColour(HairColour.BLACK);
        int result = hairColour.value();
        assertEquals(HairColour.BLACK, result);
    }

    /**
     * Tests that fromValue() accepts the minimum valid value.
     */
    @Test
    public void fromValueValidMinimumValueReturnsValue() {
        int result = HairColour.fromValue(HairColour.UNSPECIFIED);
        assertEquals(HairColour.UNSPECIFIED, result);
    }

    /**
     * Tests that fromValue() accepts the maximum in-range value.
     */
    @Test
    public void fromValueValidMaximumValueInRangeReturnsValue() {
        int result = HairColour.fromValue(HairColour.RED);
        assertEquals(HairColour.RED, result);
    }

    /**
     * Tests that fromValue() accepts the 'unknown' constant value.
     */
    @Test
    public void fromValueValidUnknownValueReturnsValue() {
        int result = HairColour.fromValue(HairColour.UNKNOWN);
        assertEquals(HairColour.UNKNOWN, result);
    }

    /**
     * Tests that fromValue() throws IllegalArgumentException for a
     * middle-range invalid value (not mapped to any hair colour).
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueInMiddleRangeThrowsIllegalArgumentException() {
        HairColour.fromValue(0x08);
    }

    /**
     * Tests that fromValue() throws IllegalArgumentException for a value below the valid range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueBelowRangeThrowsIllegalArgumentException() {
        HairColour.fromValue(-1);
    }

    /**
     * Tests that toString() returns a non-null formatted string containing the value,
     * for a valid hair colour.
     */
    @Test
    public void toStringValidHairColourReturnsFormattedString() {
        HairColour hairColour = new HairColour(HairColour.BLONDE);
        String result = hairColour.toString();
        assertNotNull(result);
        assertTrue(result.contains("3"));
    }
}