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
    public void constructorCreatesTechnologyCorrectly() {
        IrisCaptureDeviceTechnology technology = new IrisCaptureDeviceTechnology(IrisCaptureDeviceTechnology.CMOS_OR_CCD);

        assertEquals(IrisCaptureDeviceTechnology.CMOS_OR_CCD, technology.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisCaptureDeviceTechnology technology = new IrisCaptureDeviceTechnology(IrisCaptureDeviceTechnology.UNSPECIFIED);

        int result = technology.value();

        assertEquals(IrisCaptureDeviceTechnology.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = IrisCaptureDeviceTechnology.fromValue(IrisCaptureDeviceTechnology.UNSPECIFIED);

        assertEquals(IrisCaptureDeviceTechnology.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = IrisCaptureDeviceTechnology.fromValue(IrisCaptureDeviceTechnology.CMOS_OR_CCD);

        assertEquals(IrisCaptureDeviceTechnology.CMOS_OR_CCD, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisCaptureDeviceTechnology.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisCaptureDeviceTechnology.fromValue(0x02);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisCaptureDeviceTechnology technology = new IrisCaptureDeviceTechnology(IrisCaptureDeviceTechnology.CMOS_OR_CCD);

        String result = technology.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }
}