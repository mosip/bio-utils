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
    public void constructorCreatesFlagCorrectly() {
        FingerCertificationFlag flag = new FingerCertificationFlag(FingerCertificationFlag.UNSPECIFIED);

        assertEquals(FingerCertificationFlag.UNSPECIFIED, flag.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerCertificationFlag flag = new FingerCertificationFlag(FingerCertificationFlag.ONE);

        int result = flag.value();

        assertEquals(FingerCertificationFlag.ONE, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValueWithUnspecifiedReturnsValue() {
        int result = FingerCertificationFlag.fromValue(FingerCertificationFlag.UNSPECIFIED);

        assertEquals(FingerCertificationFlag.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid one value
     */
    @Test
    public void fromValueWithOneReturnsValue() {
        int result = FingerCertificationFlag.fromValue(FingerCertificationFlag.ONE);

        assertEquals(FingerCertificationFlag.ONE, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueThrowsException() {
        FingerCertificationFlag.fromValue(0x02);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerCertificationFlag flag = new FingerCertificationFlag(FingerCertificationFlag.ONE);

        String result = flag.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}