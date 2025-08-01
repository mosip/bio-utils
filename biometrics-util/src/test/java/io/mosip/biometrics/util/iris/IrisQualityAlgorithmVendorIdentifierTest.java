package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisQualityAlgorithmVendorIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesVendorIdentifierCorrectly() {
        IrisQualityAlgorithmVendorIdentifier identifier = new IrisQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisQualityAlgorithmVendorIdentifier identifier = new IrisQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED);

        int result = identifier.value();

        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid algorithm vendor identifier value
     */
    @Test
    public void fromValueWithAlgorithmVendorIdentifierReturnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(0x7FFF);

        assertEquals(0x7FFF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisQualityAlgorithmVendorIdentifier.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisQualityAlgorithmVendorIdentifier.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisQualityAlgorithmVendorIdentifier identifier = new IrisQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}