package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisCertificationFlagTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesFlagCorrectly() {
        IrisCertificationFlag flag = new IrisCertificationFlag(IrisCertificationFlag.UNSPECIFIED);

        assertEquals(IrisCertificationFlag.UNSPECIFIED, flag.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisCertificationFlag flag = new IrisCertificationFlag(0x00);

        int result = flag.value();

        assertEquals(0x00, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValueWithValidValueReturnsValue() {
        int result = IrisCertificationFlag.fromValue(IrisCertificationFlag.UNSPECIFIED);

        assertEquals(IrisCertificationFlag.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidValueThrowsException() {
        IrisCertificationFlag.fromValue(0x01);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisCertificationFlag flag = new IrisCertificationFlag(IrisCertificationFlag.UNSPECIFIED);

        String result = flag.toString();

        assertNotNull(result);
        assertTrue(result.contains("0"));
    }
}