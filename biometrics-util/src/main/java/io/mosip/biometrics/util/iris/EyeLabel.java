package io.mosip.biometrics.util.iris;

/** Eye Label, Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum EyeLabel {

	UNSPECIFIED(0x00), 
	RIGHT(0x01), 
	LEFT(0x02);

	private final int value;
	EyeLabel(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static EyeLabel fromValue(int value) {
		for (EyeLabel c : EyeLabel.values()) {
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
