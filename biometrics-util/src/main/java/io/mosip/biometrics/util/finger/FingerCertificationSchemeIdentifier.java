package io.mosip.biometrics.util.finger;

/**
 * certification Flag '0', '0', 0x00. Table 5 Identifiers for certification
 * schemes specified in the annexes of ISO/IEC 19794-4-2011.
 */
public class FingerCertificationSchemeIdentifier {
	public static final int UNSPECIFIED = 0x00;
	public static final int IMAGE_QUALITY_SPECIFICATION_FOR_AFIS_SYSTEM = 0x01;
	public static final int IMAGE_QUALITY_SPECIFICATION_FOR_PERSONAL_VERIFICATION = 0x02;
	public static final int REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER = 0x03;
	public static final int RESERVED_04 = 0x04;
	public static final int RESERVED_FF = 0xFF;

	private int value;

	public FingerCertificationSchemeIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED
				&& value <= REQUIREMENTS_AND_TEST_PROCEDURES_FOR_OPTICAL_FINGERPRINT_SCANNER)
			return value;
		throw new IllegalArgumentException(
				"FingerCertificationSchemeIdentifier value can be between (0x00 and 0x03), set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
