package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FingerCaptureDeviceTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesDeviceTypeCorrectly() {
        FingerCaptureDeviceType deviceType = new FingerCaptureDeviceType(FingerCaptureDeviceType.UNSPECIFIED);

        assertEquals(FingerCaptureDeviceType.UNSPECIFIED, deviceType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        FingerCaptureDeviceType deviceType = new FingerCaptureDeviceType(0x5678);

        int result = deviceType.value();

        assertEquals(0x5678, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = FingerCaptureDeviceType.fromValue(FingerCaptureDeviceType.UNSPECIFIED);

        assertEquals(FingerCaptureDeviceType.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = FingerCaptureDeviceType.fromValue(FingerCaptureDeviceType.VENDOR_FFFF);

        assertEquals(FingerCaptureDeviceType.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        FingerCaptureDeviceType.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        FingerCaptureDeviceType.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        FingerCaptureDeviceType deviceType = new FingerCaptureDeviceType(0x9ABC);

        String result = deviceType.toString();

        assertNotNull(result);
        assertTrue(result.contains("9abc"));
    }
}