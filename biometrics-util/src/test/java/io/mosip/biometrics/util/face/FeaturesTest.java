package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FeaturesTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorValidValueCreatesFeatures() {
        Features features = new Features(Features.FEATURES_ARE_SPECIFIED);

        assertEquals(Features.FEATURES_ARE_SPECIFIED, features.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueValidFeaturesReturnsCorrectValue() {
        Features features = new Features(Features.GLASSES);

        int result = features.value();

        assertEquals(Features.GLASSES, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueValidMinimumValueReturnsValue() {
        int result = Features.fromValue(Features.FEATURES_ARE_SPECIFIED);

        assertEquals(Features.FEATURES_ARE_SPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueValidMaximumValueReturnsValue() {
        int result = Features.fromValue(Features.DISTORTING_MEDICAL_CONDITION);

        assertEquals(Features.DISTORTING_MEDICAL_CONDITION, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueBelowRangeThrowsIllegalArgumentException() {
        Features.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueAboveRangeThrowsIllegalArgumentException() {
        Features.fromValue(0x000020);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringValidFeaturesReturnsFormattedString() {
        Features features = new Features(Features.MOUSTACHE);

        String result = features.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }
}