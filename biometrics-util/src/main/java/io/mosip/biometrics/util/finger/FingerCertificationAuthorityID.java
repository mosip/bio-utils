package io.mosip.biometrics.util.finger;

/** certification Flag '0', '0', 0x00. Table 1 � General record header of ISO/IEC 19794-4-2011. */
//https://www.ibia.org/cbeff/iso/biometric-organizations
public enum FingerCertificationAuthorityID {

	UNSPECIFIED(0x0000),
	GREEN_BIT_AMERICAS_INC(0x0040);

	private final int value;
	FingerCertificationAuthorityID(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerCertificationAuthorityID fromValue(int value) {
		for (FingerCertificationAuthorityID c : FingerCertificationAuthorityID.values()) {
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
