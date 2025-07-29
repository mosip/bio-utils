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
    public void constructor_validValue_createsIrisImageCompressionType() {
        IrisImageCompressionType compressionType = new IrisImageCompressionType(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE);

        assertEquals(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE, compressionType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validCompressionType_returnsCorrectValue() {
        IrisImageCompressionType compressionType = new IrisImageCompressionType(IrisImageCompressionType.JPEG_LOSSY);

        int result = compressionType.value();

        assertEquals(IrisImageCompressionType.JPEG_LOSSY, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = IrisImageCompressionType.fromValue(IrisImageCompressionType.UNDEFINED);

        assertEquals(IrisImageCompressionType.UNDEFINED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = IrisImageCompressionType.fromValue(IrisImageCompressionType.JPEG_LOSSY);

        assertEquals(IrisImageCompressionType.JPEG_LOSSY, result);
    }

    /**
     * Tests fromValue method with valid lossless value
     */
    @Test
    public void fromValue_validLosslessValue_returnsValue() {
        int result = IrisImageCompressionType.fromValue(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE);

        assertEquals(IrisImageCompressionType.JPEG_LOSSLESS_OR_NONE, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        IrisImageCompressionType.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        IrisImageCompressionType.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validCompressionType_returnsFormattedString() {
        IrisImageCompressionType compressionType = new IrisImageCompressionType(IrisImageCompressionType.JPEG_LOSSY);

        String result = compressionType.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }
}