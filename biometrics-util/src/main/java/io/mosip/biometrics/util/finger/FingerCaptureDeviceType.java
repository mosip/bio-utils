package io.mosip.biometrics.util.finger;

/** Capture Device Type Identifier, 0x00. Table 4 Section 5.4.5 of ISO/IEC 19794-4-2011. 
 * The (2 byte) Capture Device Type Identifier The capture device type identifier shall identify the product type that created the BDIR. It shall be assigned by
 * the registered product owner or other approved registration authority. A value of all zeros shall indicate that
 * the capture device type is unreported. 
*/
public enum FingerCaptureDeviceType {

	UNSPECIFIED(0x0000);

	private final int value;
	FingerCaptureDeviceType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerCaptureDeviceType fromValue(int value) {
		for (FingerCaptureDeviceType c : FingerCaptureDeviceType.values()) {
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
