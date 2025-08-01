package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FingerQualityAlgorithmVendorIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesVendorIdentifierCorrectly() {
        FingerQualityAlgorithmVendorIdentifier identifier = new FingerQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.NIST);

        assertEquals(FingerQualityAlgorithmVendorIdentifier.NIST, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerQualityAlgorithmVendorIdentifier identifier = new FingerQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.GREEN_BIT_AMERICAS_INC);

        int result = identifier.value();

        assertEquals(FingerQualityAlgorithmVendorIdentifier.GREEN_BIT_AMERICAS_INC, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValueWithUnspecifiedReturnsValue() {
        int result = FingerQualityAlgorithmVendorIdentifier.fromValue(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED);

        assertEquals(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerQualityAlgorithmVendorIdentifier.fromValue(FingerQualityAlgorithmVendorIdentifier.VENDOR_FFFF);

        assertEquals(FingerQualityAlgorithmVendorIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = FingerQualityAlgorithmVendorIdentifier.fromValue(0x8000);

        assertEquals(0x8000, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerQualityAlgorithmVendorIdentifier identifier = new FingerQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.GREEN_BIT_AMERICAS_INC);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("40"));
    }
}