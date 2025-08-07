package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisVersionNumberTest {

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisVersionNumber versionNumber = new IrisVersionNumber(IrisVersionNumber.VERSION_020);

        int result = versionNumber.value();

        assertEquals(IrisVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValueWithValidValueReturnsValue() {
        int result = IrisVersionNumber.fromValue(IrisVersionNumber.VERSION_020);

        assertEquals(IrisVersionNumber.VERSION_020, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueThrowsException() {
        IrisVersionNumber.fromValue(0x12345678);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisVersionNumber versionNumber = new IrisVersionNumber(IrisVersionNumber.VERSION_020);

        String result = versionNumber.toString();

        assertNotNull(result);
        assertTrue(result.contains("30323000"));
    }
}