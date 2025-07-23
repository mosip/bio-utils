package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class IrisQualityAlgorithmIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsIrisQualityAlgorithmIdentifier() {
        IrisQualityAlgorithmIdentifier identifier = new IrisQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001);

        assertEquals(IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validIdentifier_returnsCorrectValue() {
        IrisQualityAlgorithmIdentifier identifier = new IrisQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.UNSPECIFIED);

        int result = identifier.value();

        assertEquals(IrisQualityAlgorithmIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = IrisQualityAlgorithmIdentifier.fromValue(IrisQualityAlgorithmIdentifier.UNSPECIFIED);

        assertEquals(IrisQualityAlgorithmIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = IrisQualityAlgorithmIdentifier.fromValue(IrisQualityAlgorithmIdentifier.VENDOR_FFFF);

        assertEquals(IrisQualityAlgorithmIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid algorithm identifier value
     */
    @Test
    public void fromValue_validAlgorithmIdentifierValue_returnsValue() {
        int result = IrisQualityAlgorithmIdentifier.fromValue(IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001);

        assertEquals(IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValue_validMiddleValue_returnsValue() {
        int result = IrisQualityAlgorithmIdentifier.fromValue(0x8000);

        assertEquals(0x8000, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        IrisQualityAlgorithmIdentifier.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        IrisQualityAlgorithmIdentifier.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validIdentifier_returnsFormattedString() {
        IrisQualityAlgorithmIdentifier identifier = new IrisQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER_0001);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}