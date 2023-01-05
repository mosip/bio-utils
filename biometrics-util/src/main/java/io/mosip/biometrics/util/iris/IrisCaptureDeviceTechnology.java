package io.mosip.biometrics.util.iris;

/** Capture Device Technology Identifier, Table 4 of ISO/IEC 19794-6-2011. */
public class IrisCaptureDeviceTechnology {
	public static final int UNSPECIFIED = 0x00;
	public static final int CMOS_OR_CCD = 0x01;

	private int value;

	public IrisCaptureDeviceTechnology(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= CMOS_OR_CCD)
			return value;
		throw new IllegalArgumentException(
				"IrisCaptureDeviceTechnology value can be between (0x00 and 0x01), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
