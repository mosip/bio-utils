package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;

public class FingerCaptureDeviceVendorTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsFingerCaptureDeviceVendor() {
        FingerCaptureDeviceVendor vendor = new FingerCaptureDeviceVendor(FingerCaptureDeviceVendor.UNSPECIFIED);

        assertEquals(FingerCaptureDeviceVendor.UNSPECIFIED, vendor.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validVendor_returnsCorrectValue() {
        FingerCaptureDeviceVendor vendor = new FingerCaptureDeviceVendor(0x1234);

        int result = vendor.value();

        assertEquals(0x1234, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = FingerCaptureDeviceVendor.fromValue(FingerCaptureDeviceVendor.UNSPECIFIED);

        assertEquals(FingerCaptureDeviceVendor.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = FingerCaptureDeviceVendor.fromValue(FingerCaptureDeviceVendor.VENDOR_FFFF);

        assertEquals(FingerCaptureDeviceVendor.VENDOR_FFFF, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        FingerCaptureDeviceVendor.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        FingerCaptureDeviceVendor.fromValue(0x10000);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validVendor_returnsFormattedString() {
        FingerCaptureDeviceVendor vendor = new FingerCaptureDeviceVendor(0xABCD);

        String result = vendor.toString();

        assertNotNull(result);
        assertTrue(result.contains("abcd"));
    }
}