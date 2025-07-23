package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;

public class FingerImageCompressionTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsFingerImageCompressionType() {
        FingerImageCompressionType compressionType = new FingerImageCompressionType(FingerImageCompressionType.WSQ);

        assertEquals(FingerImageCompressionType.WSQ, compressionType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validCompressionType_returnsCorrectValue() {
        FingerImageCompressionType compressionType = new FingerImageCompressionType(FingerImageCompressionType.JPEG_LOSSY);

        int result = compressionType.value();

        assertEquals(FingerImageCompressionType.JPEG_LOSSY, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = FingerImageCompressionType.fromValue(FingerImageCompressionType.NONE_NO_BIT_PACKING);

        assertEquals(FingerImageCompressionType.NONE_NO_BIT_PACKING, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = FingerImageCompressionType.fromValue(FingerImageCompressionType.PNG);

        assertEquals(FingerImageCompressionType.PNG, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        FingerImageCompressionType.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        FingerImageCompressionType.fromValue(0x07);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validCompressionType_returnsFormattedString() {
        FingerImageCompressionType compressionType = new FingerImageCompressionType(FingerImageCompressionType.JPEG_2000_LOSSY);

        String result = compressionType.toString();

        assertNotNull(result);
        assertTrue(result.contains("4"));
    }
}