package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerVersionNumberTest {

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerVersionNumber versionNumber = new FingerVersionNumber(FingerVersionNumber.VERSION_020);

        int result = versionNumber.value();

        assertEquals(FingerVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValueWithValidValueReturnsValue() {
        int result = FingerVersionNumber.fromValue(FingerVersionNumber.VERSION_020);

        assertEquals(FingerVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueThrowsException() {
        FingerVersionNumber.fromValue(0x12345678);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerVersionNumber versionNumber = new FingerVersionNumber(FingerVersionNumber.VERSION_020);

        String result = versionNumber.toString();

        assertNotNull(result);
        assertTrue(result.contains("30323000"));
    }
}