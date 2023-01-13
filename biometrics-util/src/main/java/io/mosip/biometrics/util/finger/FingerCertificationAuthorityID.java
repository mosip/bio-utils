package io.mosip.biometrics.util.finger;

/**
 * certification Flag '0', '0', 0x00. Table 1 General record header of ISO/IEC
 * 19794-4-2011.
 */
//https://www.ibia.org/cbeff/iso/biometric-organizations
public class FingerCertificationAuthorityID {
	public static final int UNSPECIFIED = 0x0000;
	public static final int GREEN_BIT_AMERICAS_INC = 0x0040;
	public static final int VENDOR_FFFF = 0xFFFF;// Maximum

	private int value;

	public FingerCertificationAuthorityID(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"FingerCertificationAuthorityID value can be between 0x0000 and 0xFFFF, set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
