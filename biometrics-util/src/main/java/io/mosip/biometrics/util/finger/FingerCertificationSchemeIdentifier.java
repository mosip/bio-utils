package io.mosip.biometrics.util.finger;

/** certification Flag '0', '0', 0x00. Table 5 � Identifiers for certification schemes specified in the annexes of ISO/IEC 19794-4-2011. */
public enum FingerCertificationSchemeIdentifier {

	UNSPECIFIED(0x00),
	IMAGE_QUALITY_SPECIFICATION_FOR_AFIS_SYSTEM(0x01),
	IMAGE_QUALITY_SPECIFICATION_FOR_PERSONAL_VERIFICATION(0x02),
	REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER(0x03);

	private final int value;
	FingerCertificationSchemeIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerCertificationSchemeIdentifier fromValue(int value) {
		for (FingerCertificationSchemeIdentifier c : FingerCertificationSchemeIdentifier.values()) {
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
