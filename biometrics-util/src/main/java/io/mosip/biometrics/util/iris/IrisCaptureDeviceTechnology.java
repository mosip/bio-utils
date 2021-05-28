package io.mosip.biometrics.util.iris;

/** Capture Device Technology Identifier, Table 4  of ISO/IEC 19794-6-2011. */
public enum IrisCaptureDeviceTechnology {

	UNSPECIFIED(0x0000), 
	CMOS_OR_CCD(0x0001);

	private final int value;
	IrisCaptureDeviceTechnology(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisCaptureDeviceTechnology fromValue(int value) {
		for (IrisCaptureDeviceTechnology c : IrisCaptureDeviceTechnology.values()) {
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
