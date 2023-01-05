package io.mosip.biometrics.util.finger;

/**
 * Capture Device Type Identifier, 0x0000. Table 4 Section 5.4.5 of ISO/IEC
 * 19794-4-2011. The (2 byte) Capture Device Type Identifier The capture device
 * type identifier shall identify the product type that created the BDIR. It
 * shall be assigned by the registered product owner or other approved
 * registration authority. A value of all zeros shall indicate that the capture
 * device type is unreported.
 */
public class FingerCaptureDeviceType {
	public static final int UNSPECIFIED = 0x0000;
	public static final int VENDOR_FFFF = 0xFFFF;// Maximum

	private int value;

	public FingerCaptureDeviceType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"FingerCaptureDeviceType value can be between 0x0000 and 0xFFFF, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
