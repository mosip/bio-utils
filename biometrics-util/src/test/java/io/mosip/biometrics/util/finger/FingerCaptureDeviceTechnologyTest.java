package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerCaptureDeviceTechnologyTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesTechnologyCorrectly() {
        FingerCaptureDeviceTechnology technology = new FingerCaptureDeviceTechnology(FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR);

        assertEquals(FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR, technology.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerCaptureDeviceTechnology technology = new FingerCaptureDeviceTechnology(FingerCaptureDeviceTechnology.SEMICONDUCTOR_CAPACITIVE);

        int result = technology.value();

        assertEquals(FingerCaptureDeviceTechnology.SEMICONDUCTOR_CAPACITIVE, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerCaptureDeviceTechnology.fromValue(FingerCaptureDeviceTechnology.UNSPECIFIED);

        assertEquals(FingerCaptureDeviceTechnology.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerCaptureDeviceTechnology.fromValue(FingerCaptureDeviceTechnology.GLASS_FIBER);

        assertEquals(FingerCaptureDeviceTechnology.GLASS_FIBER, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerCaptureDeviceTechnology.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerCaptureDeviceTechnology.fromValue(0x15);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerCaptureDeviceTechnology technology = new FingerCaptureDeviceTechnology(FingerCaptureDeviceTechnology.ULTRASOUND);

        String result = technology.toString();

        assertNotNull(result);
        assertTrue(result.contains("12"));
    }
}