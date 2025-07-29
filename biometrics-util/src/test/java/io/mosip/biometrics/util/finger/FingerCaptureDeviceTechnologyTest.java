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
    public void constructor_validValue_createsFingerCaptureDeviceTechnology() {
        FingerCaptureDeviceTechnology technology = new FingerCaptureDeviceTechnology(FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR);

        assertEquals(FingerCaptureDeviceTechnology.WHITE_LIGHT_OPTICAL_TIR, technology.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validTechnology_returnsCorrectValue() {
        FingerCaptureDeviceTechnology technology = new FingerCaptureDeviceTechnology(FingerCaptureDeviceTechnology.SEMICONDUCTOR_CAPACITIVE);

        int result = technology.value();

        assertEquals(FingerCaptureDeviceTechnology.SEMICONDUCTOR_CAPACITIVE, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = FingerCaptureDeviceTechnology.fromValue(FingerCaptureDeviceTechnology.UNSPECIFIED);

        assertEquals(FingerCaptureDeviceTechnology.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = FingerCaptureDeviceTechnology.fromValue(FingerCaptureDeviceTechnology.GLASS_FIBER);

        assertEquals(FingerCaptureDeviceTechnology.GLASS_FIBER, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        FingerCaptureDeviceTechnology.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        FingerCaptureDeviceTechnology.fromValue(0x15);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validTechnology_returnsFormattedString() {
        FingerCaptureDeviceTechnology technology = new FingerCaptureDeviceTechnology(FingerCaptureDeviceTechnology.ULTRASOUND);

        String result = technology.toString();

        assertNotNull(result);
        assertTrue(result.contains("12"));
    }
}