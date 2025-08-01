package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerSegmentationAlgorithmVendorIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesVendorIdentifierCorrectly() {
        FingerSegmentationAlgorithmVendorIdentifier identifier = new FingerSegmentationAlgorithmVendorIdentifier(FingerSegmentationAlgorithmVendorIdentifier.GREEN_BIT_AMERICAS_INC);

        assertEquals(FingerSegmentationAlgorithmVendorIdentifier.GREEN_BIT_AMERICAS_INC, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerSegmentationAlgorithmVendorIdentifier identifier = new FingerSegmentationAlgorithmVendorIdentifier(FingerSegmentationAlgorithmVendorIdentifier.UNSPECIFIED);

        int result = identifier.value();

        assertEquals(FingerSegmentationAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValueWithUnspecifiedReturnsValue() {
        int result = FingerSegmentationAlgorithmVendorIdentifier.fromValue(FingerSegmentationAlgorithmVendorIdentifier.UNSPECIFIED);

        assertEquals(FingerSegmentationAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerSegmentationAlgorithmVendorIdentifier.fromValue(FingerSegmentationAlgorithmVendorIdentifier.VENDOR_FFFF);

        assertEquals(FingerSegmentationAlgorithmVendorIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = FingerSegmentationAlgorithmVendorIdentifier.fromValue(0x5000);

        assertEquals(0x5000, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerSegmentationAlgorithmVendorIdentifier identifier = new FingerSegmentationAlgorithmVendorIdentifier(FingerSegmentationAlgorithmVendorIdentifier.GREEN_BIT_AMERICAS_INC);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("40"));
    }
}