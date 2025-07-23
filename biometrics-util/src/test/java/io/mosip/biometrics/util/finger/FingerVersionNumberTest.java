package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;

public class FingerVersionNumberTest {

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validVersionNumber_returnsCorrectValue() {
        FingerVersionNumber versionNumber = new FingerVersionNumber(FingerVersionNumber.VERSION_020);

        int result = versionNumber.value();

        assertEquals(FingerVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValue_validValue_returnsValue() {
        int result = FingerVersionNumber.fromValue(FingerVersionNumber.VERSION_020);

        assertEquals(FingerVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        FingerVersionNumber.fromValue(0x12345678);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validVersionNumber_returnsFormattedString() {
        FingerVersionNumber versionNumber = new FingerVersionNumber(FingerVersionNumber.VERSION_020);

        String result = versionNumber.toString();

        assertNotNull(result);
        assertTrue(result.contains("30323000"));
    }
}