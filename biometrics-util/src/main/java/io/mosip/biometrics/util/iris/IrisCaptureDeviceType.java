package io.mosip.biometrics.util.iris;

/** Capture Device Type Identifier, 0x00. Table 4 ISO/IEC 19794-6-2011. */
public enum IrisCaptureDeviceType {

	UNSPECIFIED(0x0000);

	private final int value;
	IrisCaptureDeviceType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisCaptureDeviceType fromValue(int value) {
		for (IrisCaptureDeviceType c : IrisCaptureDeviceType.values()) {
			if (c.value == value) {
				return c;
			}
		}
		return UNSPECIFIED;
		//throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
