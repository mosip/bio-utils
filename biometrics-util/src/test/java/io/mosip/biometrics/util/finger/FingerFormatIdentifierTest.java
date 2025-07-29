package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerFormatIdentifierTest {

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validFormatIdentifier_returnsCorrectValue() {
        FingerFormatIdentifier formatIdentifier = new FingerFormatIdentifier(FingerFormatIdentifier.FORMAT_FIR);

        int result = formatIdentifier.value();

        assertEquals(FingerFormatIdentifier.FORMAT_FIR, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValue_validValue_returnsValue() {
        int result = FingerFormatIdentifier.fromValue(FingerFormatIdentifier.FORMAT_FIR);

        assertEquals(FingerFormatIdentifier.FORMAT_FIR, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        FingerFormatIdentifier.fromValue(0x12345678);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validFormatIdentifier_returnsFormattedString() {
        FingerFormatIdentifier formatIdentifier = new FingerFormatIdentifier(FingerFormatIdentifier.FORMAT_FIR);

        String result = formatIdentifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("46495200"));
    }
}