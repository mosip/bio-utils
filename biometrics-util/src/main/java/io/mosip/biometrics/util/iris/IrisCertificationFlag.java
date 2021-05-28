package io.mosip.biometrics.util.iris;

/** certification Flag '0', '0', 0x00. Table 3 ISO/IEC 19794-6-2011. */
public enum IrisCertificationFlag {

	UNSPECIFIED(0x0000);

	private final int value;
	IrisCertificationFlag(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisCertificationFlag fromValue(int value) {
		for (IrisCertificationFlag c : IrisCertificationFlag.values()) {
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
