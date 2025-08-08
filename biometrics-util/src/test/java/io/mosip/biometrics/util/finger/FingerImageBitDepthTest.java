package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerImageBitDepthTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesBitDepthCorrectly() {
        FingerImageBitDepth bitDepth = new FingerImageBitDepth(FingerImageBitDepth.BPP_08);

        assertEquals(FingerImageBitDepth.BPP_08, bitDepth.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerImageBitDepth bitDepth = new FingerImageBitDepth(FingerImageBitDepth.BPP_0A);

        int result = bitDepth.value();

        assertEquals(FingerImageBitDepth.BPP_0A, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerImageBitDepth.fromValue(FingerImageBitDepth.BPP_08);

        assertEquals(FingerImageBitDepth.BPP_08, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerImageBitDepth.fromValue(FingerImageBitDepth.BPP_0F);

        assertEquals(FingerImageBitDepth.BPP_0F, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerImageBitDepth.fromValue(0x07);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerImageBitDepth.fromValue(0x10);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerImageBitDepth bitDepth = new FingerImageBitDepth(FingerImageBitDepth.BPP_0C);

        String result = bitDepth.toString();

        assertNotNull(result);
        assertTrue(result.contains("c"));
    }
}