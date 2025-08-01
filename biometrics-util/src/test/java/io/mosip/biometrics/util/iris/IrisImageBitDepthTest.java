package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisImageBitDepthTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesBitDepthCorrectly() {
        IrisImageBitDepth bitDepth = new IrisImageBitDepth(IrisImageBitDepth.BPP_08);

        assertEquals(IrisImageBitDepth.BPP_08, bitDepth.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisImageBitDepth bitDepth = new IrisImageBitDepth(IrisImageBitDepth.BPP_0A);

        int result = bitDepth.value();

        assertEquals(IrisImageBitDepth.BPP_0A, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = IrisImageBitDepth.fromValue(IrisImageBitDepth.BPP_08);

        assertEquals(IrisImageBitDepth.BPP_08, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = IrisImageBitDepth.fromValue(IrisImageBitDepth.BPP_0F);

        assertEquals(IrisImageBitDepth.BPP_0F, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = IrisImageBitDepth.fromValue(IrisImageBitDepth.BPP_0C);

        assertEquals(IrisImageBitDepth.BPP_0C, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisImageBitDepth.fromValue(0x07);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisImageBitDepth.fromValue(0x10);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisImageBitDepth bitDepth = new IrisImageBitDepth(IrisImageBitDepth.BPP_0E);

        String result = bitDepth.toString();

        assertNotNull(result);
        assertTrue(result.contains("e"));
    }
}