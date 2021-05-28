package io.mosip.biometrics.util.face;

/** Capture Device Vendor Identifier, 0x00. Table 4 Section 5.4.5 of ISO/IEC 19794-5-2011. 
 *  The (2 byte) Capture Device Vendor Identifier shall identify the biometric organization that owns the product
 * that created the BDIR. The capture device algorithm vendor identifier shall be encoded in two bytes carrying a
 * CBEFF biometric organization identifier (registered by IBIA or other approved registration authority). A value
 * of all zeros shall indicate that the capture device vendor is unreported
*/
public enum FaceCaptureDeviceVendor {

	UNSPECIFIED(0x0000);

	private final int value;
	FaceCaptureDeviceVendor(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceCaptureDeviceVendor fromValue(int value) {
		for (FaceCaptureDeviceVendor c : FaceCaptureDeviceVendor.values()) {
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
