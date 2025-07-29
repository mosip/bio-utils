package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CrossReferenceTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsCrossReference() {
        CrossReference crossReference = new CrossReference(CrossReference.BASIC);

        assertEquals(CrossReference.BASIC, crossReference.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validCrossReference_returnsCorrectValue() {
        CrossReference crossReference = new CrossReference(0x50);

        int result = crossReference.value();

        assertEquals(0x50, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = CrossReference.fromValue(CrossReference.BASIC);

        assertEquals(CrossReference.BASIC, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = CrossReference.fromValue(CrossReference.CROSSREFERENCE_FF);

        assertEquals(CrossReference.CROSSREFERENCE_FF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        CrossReference.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        CrossReference.fromValue(0x100);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validCrossReference_returnsFormattedString() {
        CrossReference crossReference = new CrossReference(0xAB);

        String result = crossReference.toString();

        assertNotNull(result);
        assertTrue(result.contains("ab"));
    }
}