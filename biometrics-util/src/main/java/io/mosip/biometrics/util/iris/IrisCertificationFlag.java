package io.mosip.biometrics.util.iris;

/** certification Flag '0', '0', 0x00. Table 3 ISO/IEC 19794-6-2011. */
public class IrisCertificationFlag {
	public static final int UNSPECIFIED = 0x00;

	private int value;

	public IrisCertificationFlag(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == UNSPECIFIED)
			return value;
		throw new IllegalArgumentException(
				"IrisCertificationFlag value can be to 0x00, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
