package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IrisCaptureDeviceVendorTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesVendorCorrectly() {
        IrisCaptureDeviceVendor vendor = new IrisCaptureDeviceVendor(IrisCaptureDeviceVendor.UNSPECIFIED);

        assertEquals(IrisCaptureDeviceVendor.UNSPECIFIED, vendor.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisCaptureDeviceVendor vendor = new IrisCaptureDeviceVendor(0x1234);

        int result = vendor.value();

        assertEquals(0x1234, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = IrisCaptureDeviceVendor.fromValue(IrisCaptureDeviceVendor.UNSPECIFIED);

        assertEquals(IrisCaptureDeviceVendor.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = IrisCaptureDeviceVendor.fromValue(IrisCaptureDeviceVendor.VENDOR_FFFF);

        assertEquals(IrisCaptureDeviceVendor.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = IrisCaptureDeviceVendor.fromValue(0x8000);

        assertEquals(0x8000, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisCaptureDeviceVendor.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisCaptureDeviceVendor.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        IrisCaptureDeviceVendor vendor = new IrisCaptureDeviceVendor(0xABCD);

        String result = vendor.toString();

        assertNotNull(result);
        assertTrue(result.contains("abcd"));
    }
}