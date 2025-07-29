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
    public void constructor_validValue_createsExtendedDataBlockIdentificationCode() {
        ExtendedDataBlockIdentificationCode code = new ExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.SEGMENTATION);

        assertEquals(ExtendedDataBlockIdentificationCode.SEGMENTATION, code.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validCode_returnsCorrectValue() {
        ExtendedDataBlockIdentificationCode code = new ExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);

        int result = code.value();

        assertEquals(ExtendedDataBlockIdentificationCode.ANNOTATION, result);
    }

    /**
     * Tests fromValue method with valid segmentation value
     */
    @Test
    public void fromValue_validSegmentationValue_returnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.SEGMENTATION);

        assertEquals(ExtendedDataBlockIdentificationCode.SEGMENTATION, result);
    }

    /**
     * Tests fromValue method with valid annotation value
     */
    @Test
    public void fromValue_validAnnotationValue_returnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.ANNOTATION);

        assertEquals(ExtendedDataBlockIdentificationCode.ANNOTATION, result);
    }

    /**
     * Tests fromValue method with valid comment value
     */
    @Test
    public void fromValue_validCommentValue_returnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.COMMENT_03);

        assertEquals(ExtendedDataBlockIdentificationCode.COMMENT_03, result);
    }

    /**
     * Tests fromValue method with valid vendor value
     */
    @Test
    public void fromValue_validVendorValue_returnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.VENDOR_0100);

        assertEquals(ExtendedDataBlockIdentificationCode.VENDOR_0100, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = ExtendedDataBlockIdentificationCode.fromValue(ExtendedDataBlockIdentificationCode.VENDOR_FFFF);

        assertEquals(ExtendedDataBlockIdentificationCode.VENDOR_FFFF, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validCode_returnsFormattedString() {
        ExtendedDataBlockIdentificationCode code = new ExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);

        String result = code.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }
}