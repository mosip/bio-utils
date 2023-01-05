package io.mosip.biometrics.util.iris;

/** Capture Device Type Identifier, 0x0000. Table 4 ISO/IEC 19794-6-2011. */
public class IrisCaptureDeviceType {
	public static final int UNSPECIFIED = 0x0000;
	public static final int VENDOR_FFFF = 0xFFFF;

	private int value;

	public IrisCaptureDeviceType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"IrisCaptureDeviceType value can be between 0x0000 and 0xFFFF, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
