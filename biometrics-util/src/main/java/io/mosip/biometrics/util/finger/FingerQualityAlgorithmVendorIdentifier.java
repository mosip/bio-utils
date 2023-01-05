package io.mosip.biometrics.util.finger;

/** Table 17 of ISO/IEC 19794-4-2011. 
*/
//https://www.ibia.org/cbeff/iso/biometric-organizations

public class FingerQualityAlgorithmVendorIdentifier {
	public static final int UNSPECIFIED = 0x0000;
	public static final int NIST = 0x000F;
	public static final int GREEN_BIT_AMERICAS_INC = 0x0040;
	public static final int VENDOR_FFFF = 0xFFFF;// Maximum
	
	private int value;
	public FingerQualityAlgorithmVendorIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED || value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException("FingerQualityAlgorithmVendorIdentifier value can be between (0x0000 and 0xFFFF), set value is wrong [" + value + "]");
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
