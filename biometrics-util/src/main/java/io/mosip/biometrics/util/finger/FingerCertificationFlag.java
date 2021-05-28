package io.mosip.biometrics.util.finger;

/** certification Flag '0', '0', 0x00. Table 1 � General record header  ISO/IEC 19794-4-2011. 
 * Indicates the presence of
 * any device certification
 * blocks within the
 * representation headers 
 * */
public enum FingerCertificationFlag {

	UNSPECIFIED(0x0000),
	ONE(0x0001);

	private final int value;
	FingerCertificationFlag(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerCertificationFlag fromValue(int value) {
		for (FingerCertificationFlag c : FingerCertificationFlag.values()) {
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
