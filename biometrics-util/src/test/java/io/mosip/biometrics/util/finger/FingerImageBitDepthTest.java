package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;

public class FingerImageBitDepthTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsFingerImageBitDepth() {
        FingerImageBitDepth bitDepth = new FingerImageBitDepth(FingerImageBitDepth.BPP_08);

        assertEquals(FingerImageBitDepth.BPP_08, bitDepth.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validBitDepth_returnsCorrectValue() {
        FingerImageBitDepth bitDepth = new FingerImageBitDepth(FingerImageBitDepth.BPP_0A);

        int result = bitDepth.value();

        assertEquals(FingerImageBitDepth.BPP_0A, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = FingerImageBitDepth.fromValue(FingerImageBitDepth.BPP_08);

        assertEquals(FingerImageBitDepth.BPP_08, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = FingerImageBitDepth.fromValue(FingerImageBitDepth.BPP_0F);

        assertEquals(FingerImageBitDepth.BPP_0F, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        FingerImageBitDepth.fromValue(0x07);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        FingerImageBitDepth.fromValue(0x10);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validBitDepth_returnsFormattedString() {
        FingerImageBitDepth bitDepth = new FingerImageBitDepth(FingerImageBitDepth.BPP_0C);

        String result = bitDepth.toString();

        assertNotNull(result);
        assertTrue(result.contains("c"));
    }
}