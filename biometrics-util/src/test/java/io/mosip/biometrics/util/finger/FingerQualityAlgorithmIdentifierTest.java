package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerQualityAlgorithmIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesIdentifierCorrectly() {
        FingerQualityAlgorithmIdentifier identifier = new FingerQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.NIST);

        assertEquals(FingerQualityAlgorithmIdentifier.NIST, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerQualityAlgorithmIdentifier identifier = new FingerQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.GREEN_BIT_AMERICAS_INC);

        int result = identifier.value();

        assertEquals(FingerQualityAlgorithmIdentifier.GREEN_BIT_AMERICAS_INC, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValueWithUnspecifiedReturnsValue() {
        int result = FingerQualityAlgorithmIdentifier.fromValue(FingerQualityAlgorithmIdentifier.UNSPECIFIED);

        assertEquals(FingerQualityAlgorithmIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerQualityAlgorithmIdentifier.fromValue(FingerQualityAlgorithmIdentifier.VENDOR_FFFF);

        assertEquals(FingerQualityAlgorithmIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = FingerQualityAlgorithmIdentifier.fromValue(0x1234);

        assertEquals(0x1234, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerQualityAlgorithmIdentifier identifier = new FingerQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.NIST);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("f"));
    }
}