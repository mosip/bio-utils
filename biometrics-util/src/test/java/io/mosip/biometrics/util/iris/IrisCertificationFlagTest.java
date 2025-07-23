package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class IrisCertificationFlagTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsIrisCertificationFlag() {
        IrisCertificationFlag flag = new IrisCertificationFlag(IrisCertificationFlag.UNSPECIFIED);

        assertEquals(IrisCertificationFlag.UNSPECIFIED, flag.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validFlag_returnsCorrectValue() {
        IrisCertificationFlag flag = new IrisCertificationFlag(0x00);

        int result = flag.value();

        assertEquals(0x00, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValue_validValue_returnsValue() {
        int result = IrisCertificationFlag.fromValue(IrisCertificationFlag.UNSPECIFIED);

        assertEquals(IrisCertificationFlag.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        IrisCertificationFlag.fromValue(0x01);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validFlag_returnsFormattedString() {
        IrisCertificationFlag flag = new IrisCertificationFlag(IrisCertificationFlag.UNSPECIFIED);

        String result = flag.toString();

        assertNotNull(result);
        assertTrue(result.contains("0"));
    }
}