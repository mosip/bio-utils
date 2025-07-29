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
    public void constructor_validValue_createsFaceCertificationFlag() {
        FaceCertificationFlag flag = new FaceCertificationFlag(FaceCertificationFlag.UNSPECIFIED);

        assertEquals(FaceCertificationFlag.UNSPECIFIED, flag.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validFlag_returnsCorrectValue() {
        FaceCertificationFlag flag = new FaceCertificationFlag(0x00);

        int result = flag.value();

        assertEquals(0x00, result);
    }

    /**
     * Tests fromValue method with valid value
     */
    @Test
    public void fromValue_validValue_returnsValue() {
        int result = FaceCertificationFlag.fromValue(FaceCertificationFlag.UNSPECIFIED);

        assertEquals(FaceCertificationFlag.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with invalid value
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        FaceCertificationFlag.fromValue(0x01);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validFlag_returnsFormattedString() {
        FaceCertificationFlag flag = new FaceCertificationFlag(FaceCertificationFlag.UNSPECIFIED);

        String result = flag.toString();

        assertNotNull(result);
        assertTrue(result.contains("0"));
    }
}