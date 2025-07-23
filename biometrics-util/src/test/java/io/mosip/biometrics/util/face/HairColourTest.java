package io.mosip.biometrics.util.face;

import static org.junit.Assert.*;
import org.junit.Test;

public class HairColourTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsHairColour() {
        HairColour hairColour = new HairColour(HairColour.BROWN);

        assertEquals(HairColour.BROWN, hairColour.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validHairColour_returnsCorrectValue() {
        HairColour hairColour = new HairColour(HairColour.BLACK);

        int result = hairColour.value();

        assertEquals(HairColour.BLACK, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = HairColour.fromValue(HairColour.UNSPECIFIED);

        assertEquals(HairColour.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value in range
     */
    @Test
    public void fromValue_validMaximumValueInRange_returnsValue() {
        int result = HairColour.fromValue(HairColour.RED);

        assertEquals(HairColour.RED, result);
    }

    /**
     * Tests fromValue method with valid unknown value
     */
    @Test
    public void fromValue_validUnknownValue_returnsValue() {
        int result = HairColour.fromValue(HairColour.UNKNOWN);

        assertEquals(HairColour.UNKNOWN, result);
    }

    /**
     * Tests fromValue method with invalid value in middle range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueInMiddleRange_throwsIllegalArgumentException() {
        HairColour.fromValue(0x08);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        HairColour.fromValue(-1);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validHairColour_returnsFormattedString() {
        HairColour hairColour = new HairColour(HairColour.BLONDE);

        String result = hairColour.toString();

        assertNotNull(result);
        assertTrue(result.contains("3"));
    }
}