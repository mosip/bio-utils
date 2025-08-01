package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisCaptureDeviceTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesDeviceTypeCorrectly() {
        IrisCaptureDeviceType deviceType = new IrisCaptureDeviceType(IrisCaptureDeviceType.UNSPECIFIED);

        assertEquals(IrisCaptureDeviceType.UNSPECIFIED, deviceType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisCaptureDeviceType deviceType = new IrisCaptureDeviceType(0x5678);

        int result = deviceType.value();

        assertEquals(0x5678, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = IrisCaptureDeviceType.fromValue(IrisCaptureDeviceType.UNSPECIFIED);

        assertEquals(IrisCaptureDeviceType.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = IrisCaptureDeviceType.fromValue(IrisCaptureDeviceType.VENDOR_FFFF);

        assertEquals(IrisCaptureDeviceType.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = IrisCaptureDeviceType.fromValue(0x7FFF);

        assertEquals(0x7FFF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisCaptureDeviceType.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisCaptureDeviceType.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisCaptureDeviceType deviceType = new IrisCaptureDeviceType(0x9ABC);

        String result = deviceType.toString();

        assertNotNull(result);
        assertTrue(result.contains("9abc"));
    }
}