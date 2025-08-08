package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerCertificationAuthorityIDTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesAuthorityIdCorrectly() {
        FingerCertificationAuthorityID authorityID = new FingerCertificationAuthorityID(FingerCertificationAuthorityID.GREEN_BIT_AMERICAS_INC);

        assertEquals(FingerCertificationAuthorityID.GREEN_BIT_AMERICAS_INC, authorityID.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerCertificationAuthorityID authorityID = new FingerCertificationAuthorityID(FingerCertificationAuthorityID.UNSPECIFIED);

        int result = authorityID.value();

        assertEquals(FingerCertificationAuthorityID.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerCertificationAuthorityID.fromValue(FingerCertificationAuthorityID.UNSPECIFIED);

        assertEquals(FingerCertificationAuthorityID.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerCertificationAuthorityID.fromValue(FingerCertificationAuthorityID.VENDOR_FFFF);

        assertEquals(FingerCertificationAuthorityID.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = FingerCertificationAuthorityID.fromValue(0x8000);

        assertEquals(0x8000, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerCertificationAuthorityID.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerCertificationAuthorityID.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerCertificationAuthorityID authorityID = new FingerCertificationAuthorityID(FingerCertificationAuthorityID.GREEN_BIT_AMERICAS_INC);

        String result = authorityID.toString();

        assertNotNull(result);
        assertTrue(result.contains("40"));
    }
}