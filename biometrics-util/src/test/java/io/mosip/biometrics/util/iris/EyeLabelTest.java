package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class EyeLabelTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesEyeLabelCorrectly() {
        EyeLabel eyeLabel = new EyeLabel(EyeLabel.RIGHT);

        assertEquals(EyeLabel.RIGHT, eyeLabel.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        EyeLabel eyeLabel = new EyeLabel(EyeLabel.LEFT);

        int result = eyeLabel.value();

        assertEquals(EyeLabel.LEFT, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = EyeLabel.fromValue(EyeLabel.UNSPECIFIED);

        assertEquals(EyeLabel.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = EyeLabel.fromValue(EyeLabel.LEFT);

        assertEquals(EyeLabel.LEFT, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        EyeLabel.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        EyeLabel.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        EyeLabel eyeLabel = new EyeLabel(EyeLabel.RIGHT);

        String result = eyeLabel.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}