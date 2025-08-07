package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class EyeColourTest {

    /**
     * Tests EyeColour constructor with valid value
     */
    @Test
    public void constructorWithValidValue() {
        int value = EyeColour.BLUE;

        EyeColour eyeColour = new EyeColour(value);

        assertNotNull(eyeColour);
        assertEquals(value, eyeColour.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        EyeColour eyeColour = new EyeColour(EyeColour.GREEN);

        int result = eyeColour.value();

        assertEquals(EyeColour.GREEN, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValueWithUnspecified() {
        int result = EyeColour.fromValue(EyeColour.UNSPECIFIED);

        assertEquals(EyeColour.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid black value
     */
    @Test
    public void fromValueWithBlack() {
        int result = EyeColour.fromValue(EyeColour.BLACK);

        assertEquals(EyeColour.BLACK, result);
    }

    /**
     * Tests fromValue method with valid blue value
     */
    @Test
    public void fromValueWithBlue() {
        int result = EyeColour.fromValue(EyeColour.BLUE);

        assertEquals(EyeColour.BLUE, result);
    }

    /**
     * Tests fromValue method with valid brown value
     */
    @Test
    public void fromValueWithBrown() {
        int result = EyeColour.fromValue(EyeColour.BROWN);

        assertEquals(EyeColour.BROWN, result);
    }

    /**
     * Tests fromValue method with valid gray value
     */
    @Test
    public void fromValueWithGray() {
        int result = EyeColour.fromValue(EyeColour.GRAY);

        assertEquals(EyeColour.GRAY, result);
    }

    /**
     * Tests fromValue method with valid green value
     */
    @Test
    public void fromValueWithGreen() {
        int result = EyeColour.fromValue(EyeColour.GREEN);

        assertEquals(EyeColour.GREEN, result);
    }

    /**
     * Tests fromValue method with valid multi colour value
     */
    @Test
    public void fromValueWithMultiColour() {
        int result = EyeColour.fromValue(EyeColour.MULTI_COLOUR);

        assertEquals(EyeColour.MULTI_COLOUR, result);
    }

    /**
     * Tests fromValue method with valid pink value
     */
    @Test
    public void fromValueWithPink() {
        int result = EyeColour.fromValue(EyeColour.PINK);

        assertEquals(EyeColour.PINK, result);
    }

    /**
     * Tests fromValue method with valid other or unknown value
     */
    @Test
    public void fromValueWithOtherOrUnknown() {
        int result = EyeColour.fromValue(EyeColour.OTHER_OR_UNKNOWN);

        assertEquals(EyeColour.OTHER_OR_UNKNOWN, result);
    }

    /**
     * Tests fromValue method with invalid value throws exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueWithInvalidValue() {
        EyeColour.fromValue(0x99);
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringReturnsNonNullString() {
        EyeColour eyeColour = new EyeColour(EyeColour.BLUE);

        String result = eyeColour.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }

    /**
     * Tests all constant values are correct
     */
    @Test
    public void constantsAreCorrect() {
        assertEquals(0x00, EyeColour.UNSPECIFIED);
        assertEquals(0x01, EyeColour.BLACK);
        assertEquals(0x02, EyeColour.BLUE);
        assertEquals(0x03, EyeColour.BROWN);
        assertEquals(0x04, EyeColour.GRAY);
        assertEquals(0x05, EyeColour.GREEN);
        assertEquals(0x06, EyeColour.MULTI_COLOUR);
        assertEquals(0x07, EyeColour.PINK);
        assertEquals(0x08, EyeColour.RESERVED_008);
        assertEquals(0xFE, EyeColour.RESERVED_254);
        assertEquals(0xFF, EyeColour.OTHER_OR_UNKNOWN);
    }
}