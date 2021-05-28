package io.mosip.biometrics.util.face;

/** Expression Mask Table 11 of ISO/IEC 19794-5-2011. */
public enum Expression {

	UNSPECIFIED(0x0000), 
	NEUTRAL(0x0001), 
	SMILE_OPEN(0x0002), 
	RAISED_EYEBROWS(0x0003), 
	EYES_LOOKING_AWAY(0x0004), 
	SQUINTING(0x0005), 
	FROWNING(0x0006);

	private final int value;
	Expression(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static Expression fromValue(int value) {
		for (Expression c : Expression.values()) {
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
