package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisCaptureDeviceTechnologyTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsIrisCaptureDeviceTechnology() {
        IrisCaptureDeviceTechnology technology = new IrisCaptureDeviceTechnology(IrisCaptureDeviceTechnology.CMOS_OR_CCD);

        assertEquals(IrisCaptureDeviceTechnology.CMOS_OR_CCD, technology.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validTechnology_returnsCorrectValue() {
        IrisCaptureDeviceTechnology technology = new IrisCaptureDeviceTechnology(IrisCaptureDeviceTechnology.UNSPECIFIED);

        int result = technology.value();

        assertEquals(IrisCaptureDeviceTechnology.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = IrisCaptureDeviceTechnology.fromValue(IrisCaptureDeviceTechnology.UNSPECIFIED);

        assertEquals(IrisCaptureDeviceTechnology.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = IrisCaptureDeviceTechnology.fromValue(IrisCaptureDeviceTechnology.CMOS_OR_CCD);

        assertEquals(IrisCaptureDeviceTechnology.CMOS_OR_CCD, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        IrisCaptureDeviceTechnology.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        IrisCaptureDeviceTechnology.fromValue(0x02);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validTechnology_returnsFormattedString() {
        IrisCaptureDeviceTechnology technology = new IrisCaptureDeviceTechnology(IrisCaptureDeviceTechnology.CMOS_OR_CCD);

        String result = technology.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}