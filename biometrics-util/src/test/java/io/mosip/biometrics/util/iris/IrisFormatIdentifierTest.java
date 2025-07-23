package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class IrisFormatIdentifierTest {

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validFormatIdentifier_returnsCorrectValue() {
        IrisFormatIdentifier formatIdentifier = new IrisFormatIdentifier(IrisFormatIdentifier.FORMAT_IIR);

        int result = formatIdentifier.value();

        assertEquals(IrisFormatIdentifier.FORMAT_IIR, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValue_validValue_returnsValue() {
        int result = IrisFormatIdentifier.fromValue(IrisFormatIdentifier.FORMAT_IIR);

        assertEquals(IrisFormatIdentifier.FORMAT_IIR, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        IrisFormatIdentifier.fromValue(0x12345678);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validFormatIdentifier_returnsFormattedString() {
        IrisFormatIdentifier formatIdentifier = new IrisFormatIdentifier(IrisFormatIdentifier.FORMAT_IIR);

        String result = formatIdentifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("49495200"));
    }
}