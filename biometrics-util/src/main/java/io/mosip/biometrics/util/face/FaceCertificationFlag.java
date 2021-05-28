package io.mosip.biometrics.util.face;

/** certification Flag '0', '0', 0x00. Section 5.3.6 ISO/IEC 19794-5-2011. */
public enum FaceCertificationFlag {

	UNSPECIFIED(0x0000);

	private final int value;
	FaceCertificationFlag(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceCertificationFlag fromValue(int value) {
		for (FaceCertificationFlag c : FaceCertificationFlag.values()) {
			if (c.value == value) {
				return c;
			}
		}
		throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
