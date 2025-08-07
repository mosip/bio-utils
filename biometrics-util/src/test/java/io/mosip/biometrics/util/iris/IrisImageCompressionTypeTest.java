package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisImageCompressionTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesCompressionTypeCorrectly() {
        IrisImageCompressionType compressionType = new IrisImageCompressionType(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE);

        assertEquals(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE, compressionType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisImageCompressionType compressionType = new IrisImageCompressionType(IrisImageCompressionType.JPEG_LOSSY);

        int result = compressionType.value();

        assertEquals(IrisImageCompressionType.JPEG_LOSSY, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = IrisImageCompressionType.fromValue(IrisImageCompressionType.UNDEFINED);

        assertEquals(IrisImageCompressionType.UNDEFINED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = IrisImageCompressionType.fromValue(IrisImageCompressionType.JPEG_LOSSY);

        assertEquals(IrisImageCompressionType.JPEG_LOSSY, result);
    }

    /**
     * Tests fromValue method with valid lossless value
     */
    @Test
    public void fromValueWithLosslessReturnsValue() {
        int result = IrisImageCompressionType.fromValue(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE);

        assertEquals(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisImageCompressionType compressionType = new IrisImageCompressionType(IrisImageCompressionType.JPEG_LOSSY);

        String result = compressionType.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }
}