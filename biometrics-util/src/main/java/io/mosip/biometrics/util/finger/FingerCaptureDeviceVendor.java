package io.mosip.biometrics.util.finger;

/** Capture Device Vendor Identifier, The capture device vendor identifier shall identify the biometric organization that owns the product that
* created the BDIR. The capture device algorithm vendor identifier shall be encoded in two bytes carrying a
* CBEFF biometric organization identifier (registered by IBIA or other approved registration authority). A value
* of all zeros shall indicate that the capture device vendor is unreported. 
*/
public enum FingerCaptureDeviceVendor {

	UNSPECIFIED(0x0000);

	private final int value;
	FingerCaptureDeviceVendor(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerCaptureDeviceVendor fromValue(int value) {
		for (FingerCaptureDeviceVendor c : FingerCaptureDeviceVendor.values()) {
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
