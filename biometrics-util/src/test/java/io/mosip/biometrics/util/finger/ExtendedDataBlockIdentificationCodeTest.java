package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ExtendedDataBlockIdentificationCodeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesCodeCorrectly() {
        ExtendedDataBlockIdentificationCode code = new ExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.SEGMENTATION);

        assertEquals(ExtendedDataBlockIdentificationCode.SEGMENTATION, code.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        ExtendedDataBlockIdentificationCode code = new ExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);

        int result = code.value();

        assertEquals(ExtendedDataBlockIdentificationCode.ANNOTATION, result);
    }

    /**
     * Tests fromValue method with valid segmentation value
     */
    @Test
    public void fromValueWithSegmentationReturnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.SEGMENTATION);

        assertEquals(ExtendedDataBlockIdentificationCode.SEGMENTATION, result);
    }

    /**
     * Tests fromValue method with valid annotation value
     */
    @Test
    public void fromValueWithAnnotationReturnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.ANNOTATION);

        assertEquals(ExtendedDataBlockIdentificationCode.ANNOTATION, result);
    }

    /**
     * Tests fromValue method with valid comment value
     */
    @Test
    public void fromValueWithCommentReturnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.COMMENT_03);

        assertEquals(ExtendedDataBlockIdentificationCode.COMMENT_03, result);
    }

    /**
     * Tests fromValue method with valid vendor value
     */
    @Test
    public void fromValueWithVendorReturnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.VENDOR_0100);

        assertEquals(ExtendedDataBlockIdentificationCode.VENDOR_0100, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.VENDOR_FFFF);

        assertEquals(ExtendedDataBlockIdentificationCode.VENDOR_FFFF, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        ExtendedDataBlockIdentificationCode code = new ExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);

        String result = code.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }
}