package io.mosip.biometrics.util.iris;

/** Version number '0', '2', '0', 0x00. Table 3 of ISO/IEC 19794-6-2011. */
public enum IrisVersionNumber {

	VERSION_020(0x30323000);

	private final int value;
	IrisVersionNumber(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisVersionNumber fromValue(int value) {
		for (IrisVersionNumber c : IrisVersionNumber.values()) {
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
