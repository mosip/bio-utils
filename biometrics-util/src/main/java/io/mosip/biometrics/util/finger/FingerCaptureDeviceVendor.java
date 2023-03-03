package io.mosip.biometrics.util.finger;

/**
 * Capture Device Vendor Identifier, The capture device vendor identifier shall
 * identify the biometric organization that owns the product that created the
 * BDIR. The capture device algorithm vendor identifier shall be encoded in two
 * bytes carrying a CBEFF biometric organization identifier (registered by IBIA
 * or other approved registration authority). A value of all zeros shall
 * indicate that the capture device vendor is unreported.
 */
public class FingerCaptureDeviceVendor {
	public static final int UNSPECIFIED = 0x0000;
	public static final int VENDOR_FFFF = 0xFFFF;// Maximum

	private int value;

	public FingerCaptureDeviceVendor(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"FingerCaptureDeviceVendor value can be between 0x0000 and 0xFFFF, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
