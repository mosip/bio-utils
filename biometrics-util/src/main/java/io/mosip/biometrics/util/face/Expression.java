package io.mosip.biometrics.util.face;

/** Expression Mask Table 11 of ISO/IEC 19794-5-2011. */
public class Expression {
	public static final int SPECIFIED = 0x0000;
	public static final int NEUTRAL = 0x0001;
	public static final int SMILE_OPEN = 0x0002;
	public static final int RAISED_EYEBROWS = 0x0003;
	public static final int EYES_LOOKING_AWAY = 0x0004;
	public static final int SQUINTING = 0x0005;
	public static final int FROWNING = 0x0006;
	public static final int RESERVED_0007 = 0x0007;
	public static final int RESERVED_0011 = 0x0011;
	public static final int VENDOR_0012 = 0x0012;
	public static final int VENDOR_0015 = 0x0015;

	private int value;

	public Expression(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= SPECIFIED && value <= FROWNING) || (value >= VENDOR_0012 && value <= VENDOR_0015))
			return value;
		throw new IllegalArgumentException(
				"Expression value can be between (0x0000 and 0x0006) or (0x0012 and 0x0015), set value is wrong ["
						+ value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
