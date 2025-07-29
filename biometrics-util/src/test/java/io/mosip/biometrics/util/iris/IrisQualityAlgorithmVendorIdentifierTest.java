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
    public void constructor_validValue_createsIrisQualityAlgorithmVendorIdentifier() {
        IrisQualityAlgorithmVendorIdentifier identifier = new IrisQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validIdentifier_returnsCorrectValue() {
        IrisQualityAlgorithmVendorIdentifier identifier = new IrisQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED);

        int result = identifier.value();

        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid algorithm vendor identifier value
     */
    @Test
    public void fromValue_validAlgorithmVendorIdentifierValue_returnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001);

        assertEquals(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValue_validMiddleValue_returnsValue() {
        int result = IrisQualityAlgorithmVendorIdentifier.fromValue(0x7FFF);

        assertEquals(0x7FFF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        IrisQualityAlgorithmVendorIdentifier.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        IrisQualityAlgorithmVendorIdentifier.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validIdentifier_returnsFormattedString() {
        IrisQualityAlgorithmVendorIdentifier identifier = new IrisQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER_0001);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}