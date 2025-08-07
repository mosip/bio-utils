package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerImageCompressionTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesCompressionTypeCorrectly() {
        FingerImageCompressionType compressionType = new FingerImageCompressionType(FingerImageCompressionType.WSQ);

        assertEquals(FingerImageCompressionType.WSQ, compressionType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerImageCompressionType compressionType = new FingerImageCompressionType(FingerImageCompressionType.JPEG_LOSSY);

        int result = compressionType.value();

        assertEquals(FingerImageCompressionType.JPEG_LOSSY, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerImageCompressionType.fromValue(FingerImageCompressionType.NONE_NO_BIT_PACKING);

        assertEquals(FingerImageCompressionType.NONE_NO_BIT_PACKING, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerImageCompressionType.fromValue(FingerImageCompressionType.PNG);

        assertEquals(FingerImageCompressionType.PNG, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerImageCompressionType.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerImageCompressionType.fromValue(0x07);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerImageCompressionType compressionType = new FingerImageCompressionType(FingerImageCompressionType.JPEG_2000_LOSSY);

        String result = compressionType.toString();

        assertNotNull(result);
        assertTrue(result.contains("4"));
    }
}