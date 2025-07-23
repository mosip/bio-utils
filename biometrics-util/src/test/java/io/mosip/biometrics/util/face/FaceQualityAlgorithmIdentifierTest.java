package io.mosip.biometrics.util.face;

import static org.junit.Assert.*;
import org.junit.Test;

public class FaceQualityAlgorithmIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsFaceQualityAlgorithmIdentifier() {
        FaceQualityAlgorithmIdentifier identifier = new FaceQualityAlgorithmIdentifier(FaceQualityAlgorithmIdentifier.UNSPECIFIED);

        assertEquals(FaceQualityAlgorithmIdentifier.UNSPECIFIED, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validIdentifier_returnsCorrectValue() {
        FaceQualityAlgorithmIdentifier identifier = new FaceQualityAlgorithmIdentifier(0x0001);

        int result = identifier.value();

        assertEquals(0x0001, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = FaceQualityAlgorithmIdentifier.fromValue(FaceQualityAlgorithmIdentifier.UNSPECIFIED);

        assertEquals(FaceQualityAlgorithmIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = FaceQualityAlgorithmIdentifier.fromValue(FaceQualityAlgorithmIdentifier.VENDOR_FFFF);

        assertEquals(FaceQualityAlgorithmIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        FaceQualityAlgorithmIdentifier.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        FaceQualityAlgorithmIdentifier.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validIdentifier_returnsFormattedString() {
        FaceQualityAlgorithmIdentifier identifier = new FaceQualityAlgorithmIdentifier(0x0103);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("103"));
    }
}