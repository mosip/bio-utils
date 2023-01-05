package io.mosip.biometrics.util.face;

/** certification Flag '0', '0', 0x00. Section 5.3.6 ISO/IEC 19794-5-2011. */
public class FaceCertificationFlag {
	public static final int UNSPECIFIED = 0x00;

	private int value;

	public FaceCertificationFlag(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == UNSPECIFIED)
			return value;
		throw new IllegalArgumentException(
				"FaceCertificationFlag value can be to 0x00, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
