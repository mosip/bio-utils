package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AnnotationCodeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsAnnotationCode() {
        AnnotationCode annotationCode = new AnnotationCode(AnnotationCode.AMPUTATED_FINGER);

        assertEquals(AnnotationCode.AMPUTATED_FINGER, annotationCode.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validAnnotationCode_returnsCorrectValue() {
        AnnotationCode annotationCode = new AnnotationCode(AnnotationCode.UNUSABLE_IMAGE);

        int result = annotationCode.value();

        assertEquals(AnnotationCode.UNUSABLE_IMAGE, result);
    }

    /**
     * Tests fromValue method with valid amputated finger value
     */
    @Test
    public void fromValue_validAmputatedFingerValue_returnsValue() {
        int result = AnnotationCode.fromValue(AnnotationCode.AMPUTATED_FINGER);

        assertEquals(AnnotationCode.AMPUTATED_FINGER, result);
    }

    /**
     * Tests fromValue method with valid unusable image value
     */
    @Test
    public void fromValue_validUnusableImageValue_returnsValue() {
        int result = AnnotationCode.fromValue(AnnotationCode.UNUSABLE_IMAGE);

        assertEquals(AnnotationCode.UNUSABLE_IMAGE, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        AnnotationCode.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validAnnotationCode_returnsFormattedString() {
        AnnotationCode annotationCode = new AnnotationCode(AnnotationCode.AMPUTATED_FINGER);

        String result = annotationCode.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}