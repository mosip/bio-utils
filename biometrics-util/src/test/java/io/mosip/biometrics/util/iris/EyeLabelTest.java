package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class EyeLabelTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsEyeLabel() {
        EyeLabel eyeLabel = new EyeLabel(EyeLabel.RIGHT);

        assertEquals(EyeLabel.RIGHT, eyeLabel.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validEyeLabel_returnsCorrectValue() {
        EyeLabel eyeLabel = new EyeLabel(EyeLabel.LEFT);

        int result = eyeLabel.value();

        assertEquals(EyeLabel.LEFT, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = EyeLabel.fromValue(EyeLabel.UNSPECIFIED);

        assertEquals(EyeLabel.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = EyeLabel.fromValue(EyeLabel.LEFT);

        assertEquals(EyeLabel.LEFT, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        EyeLabel.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        EyeLabel.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validEyeLabel_returnsFormattedString() {
        EyeLabel eyeLabel = new EyeLabel(EyeLabel.RIGHT);

        String result = eyeLabel.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}