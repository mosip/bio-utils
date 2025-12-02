package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerScaleUnitTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesScaleUnitTypeCorrectly() {
        FingerScaleUnitType scaleUnitType = new FingerScaleUnitType(FingerScaleUnitType.PIXELS_PER_INCH);

        assertEquals(FingerScaleUnitType.PIXELS_PER_INCH, scaleUnitType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerScaleUnitType scaleUnitType = new FingerScaleUnitType(FingerScaleUnitType.PIXELS_PER_CM);

        int result = scaleUnitType.value();

        assertEquals(FingerScaleUnitType.PIXELS_PER_CM, result);
    }

    /**
     * Tests fromValue method with valid pixels per inch value
     */
    @Test
    public void fromValueWithPixelsPerInchReturnsValue() {
        int result = FingerScaleUnitType.fromValue(FingerScaleUnitType.PIXELS_PER_INCH);

        assertEquals(FingerScaleUnitType.PIXELS_PER_INCH, result);
    }

    /**
     * Tests fromValue method with valid pixels per cm value
     */
    @Test
    public void fromValueWithPixelsPerCmReturnsValue() {
        int result = FingerScaleUnitType.fromValue(FingerScaleUnitType.PIXELS_PER_CM);

        assertEquals(FingerScaleUnitType.PIXELS_PER_CM, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueThrowsException() {
        FingerScaleUnitType.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerScaleUnitType scaleUnitType = new FingerScaleUnitType(FingerScaleUnitType.PIXELS_PER_INCH);

        String result = scaleUnitType.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}