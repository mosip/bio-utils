package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerSegmentationAlgorithmIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesIdentifierCorrectly() {
        FingerSegmentationAlgorithmIdentifier identifier = new FingerSegmentationAlgorithmIdentifier(FingerSegmentationAlgorithmIdentifier.VENDOR_0001);

        assertEquals(FingerSegmentationAlgorithmIdentifier.VENDOR_0001, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerSegmentationAlgorithmIdentifier identifier = new FingerSegmentationAlgorithmIdentifier(FingerSegmentationAlgorithmIdentifier.UNSPECIFIED);

        int result = identifier.value();

        assertEquals(FingerSegmentationAlgorithmIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValueWithUnspecifiedReturnsValue() {
        int result = FingerSegmentationAlgorithmIdentifier.fromValue(FingerSegmentationAlgorithmIdentifier.UNSPECIFIED);

        assertEquals(FingerSegmentationAlgorithmIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerSegmentationAlgorithmIdentifier.fromValue(FingerSegmentationAlgorithmIdentifier.VENDOR_FFFF);

        assertEquals(FingerSegmentationAlgorithmIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = FingerSegmentationAlgorithmIdentifier.fromValue(0x7FFF);

        assertEquals(0x7FFF, result);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerSegmentationAlgorithmIdentifier identifier = new FingerSegmentationAlgorithmIdentifier(FingerSegmentationAlgorithmIdentifier.VENDOR_0001);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}