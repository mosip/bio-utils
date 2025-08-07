package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerCertificationSchemeIdentifierTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesIdentifierCorrectly() {
        FingerCertificationSchemeIdentifier identifier = new FingerCertificationSchemeIdentifier(FingerCertificationSchemeIdentifier.IMAGE_QUALITY_SPECIFICATION_FOR_AFIS_SYSTEM);

        assertEquals(FingerCertificationSchemeIdentifier.IMAGE_QUALITY_SPECIFICATION_FOR_AFIS_SYSTEM, identifier.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerCertificationSchemeIdentifier identifier = new FingerCertificationSchemeIdentifier(FingerCertificationSchemeIdentifier.IMAGE_QUALITY_SPECIFICATION_FOR_PERSONAL_VERIFICATION);

        int result = identifier.value();

        assertEquals(FingerCertificationSchemeIdentifier.IMAGE_QUALITY_SPECIFICATION_FOR_PERSONAL_VERIFICATION, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerCertificationSchemeIdentifier.fromValue(FingerCertificationSchemeIdentifier.UNSPECIFIED);

        assertEquals(FingerCertificationSchemeIdentifier.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerCertificationSchemeIdentifier.fromValue(FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER);

        assertEquals(FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER, result);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerCertificationSchemeIdentifier.fromValue(0x04);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerCertificationSchemeIdentifier.fromValue(-1);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerCertificationSchemeIdentifier identifier = new FingerCertificationSchemeIdentifier(FingerCertificationSchemeIdentifier.REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER);

        String result = identifier.toString();

        assertNotNull(result);
        assertTrue(result.contains("3"));
    }
}