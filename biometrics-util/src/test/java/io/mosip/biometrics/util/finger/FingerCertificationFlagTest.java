package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerCertificationFlagTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsFingerCertificationFlag() {
        FingerCertificationFlag flag = new FingerCertificationFlag(FingerCertificationFlag.UNSPECIFIED);

        assertEquals(FingerCertificationFlag.UNSPECIFIED, flag.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validFlag_returnsCorrectValue() {
        FingerCertificationFlag flag = new FingerCertificationFlag(FingerCertificationFlag.ONE);

        int result = flag.value();

        assertEquals(FingerCertificationFlag.ONE, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValue_validUnspecifiedValue_returnsValue() {
        int result = FingerCertificationFlag.fromValue(FingerCertificationFlag.UNSPECIFIED);

        assertEquals(FingerCertificationFlag.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid one value
     */
    @Test
    public void fromValue_validOneValue_returnsValue() {
        int result = FingerCertificationFlag.fromValue(FingerCertificationFlag.ONE);

        assertEquals(FingerCertificationFlag.ONE, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        FingerCertificationFlag.fromValue(0x02);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validFlag_returnsFormattedString() {
        FingerCertificationFlag flag = new FingerCertificationFlag(FingerCertificationFlag.ONE);

        String result = flag.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}