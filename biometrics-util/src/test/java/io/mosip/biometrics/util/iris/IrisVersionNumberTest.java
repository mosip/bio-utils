package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class IrisVersionNumberTest {

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validVersionNumber_returnsCorrectValue() {
        IrisVersionNumber versionNumber = new IrisVersionNumber(IrisVersionNumber.VERSION_020);

        int result = versionNumber.value();

        assertEquals(IrisVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValue_validValue_returnsValue() {
        int result = IrisVersionNumber.fromValue(IrisVersionNumber.VERSION_020);

        assertEquals(IrisVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        IrisVersionNumber.fromValue(0x12345678);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validVersionNumber_returnsFormattedString() {
        IrisVersionNumber versionNumber = new IrisVersionNumber(IrisVersionNumber.VERSION_020);

        String result = versionNumber.toString();

        assertNotNull(result);
        assertTrue(result.contains("30323000"));
    }
}