package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FaceCertificationFlagTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorWithValidValue() {
        FaceCertificationFlag flag = new FaceCertificationFlag(FaceCertificationFlag.UNSPECIFIED);

        assertEquals(FaceCertificationFlag.UNSPECIFIED, flag.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FaceCertificationFlag flag = new FaceCertificationFlag(0x00);

        int result = flag.value();

        assertEquals(0x00, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValueWithValidValue() {
        int result = FaceCertificationFlag.fromValue(FaceCertificationFlag.UNSPECIFIED);

        assertEquals(FaceCertificationFlag.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueWithInvalidValue() {
        FaceCertificationFlag.fromValue(0x01);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FaceCertificationFlag flag = new FaceCertificationFlag(FaceCertificationFlag.UNSPECIFIED);

        String result = flag.toString();

        assertNotNull(result);
        assertTrue(result.contains("0"));
    }
}