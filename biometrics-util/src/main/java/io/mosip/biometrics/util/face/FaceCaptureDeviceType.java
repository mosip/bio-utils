package io.mosip.biometrics.util.face;

/** Capture Device Type Identifier, 0x00. Table 4 Section 5.4.5 of ISO/IEC 19794-5-2011. 
 * The (2 byte) Capture Device Type Identifier shall identify the product type that created the BDIR. It shall be
 * assigned by the registered product owner or other approved registration authority. A value of all zeros shall
 * indicate that the capture device type is unreported. If the capture device vendor identifier is 0000Hex, then also
 * the capture device type identifier shall be 0000Hex
*/
public enum FaceCaptureDeviceType {

	UNSPECIFIED(0x0000);

	private final int value;
	FaceCaptureDeviceType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceCaptureDeviceType fromValue(int value) {
		for (FaceCaptureDeviceType c : FaceCaptureDeviceType.values()) {
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
