package io.mosip.biometrics.util.finger;

/** Version number '0', '2', '0', 0x020. Table 1 � General record header of ISO/IEC 19794-4-2011. */
public enum FingerVersionNumber {

	VERSION_020(0x30323000);

	private final int value;
	FingerVersionNumber(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerVersionNumber fromValue(int value) {
		for (FingerVersionNumber c : FingerVersionNumber.values()) {
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
