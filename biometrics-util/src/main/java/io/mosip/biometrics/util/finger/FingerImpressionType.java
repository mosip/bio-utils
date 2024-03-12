package io.mosip.biometrics.util.finger;

/**
 * ImpressionType Table 10 Finger and palm impression codes of ISO/IEC
 * 19794-4-2011.
 */
public class FingerImpressionType {
	public static final int LIVE_SCAN_PLAIN = 0x00;
	public static final int LIVE_SCAN_ROLLED = 0x01;
	public static final int NON_LIVE_SCAN_PLAIN = 0x02;
	public static final int NON_LIVE_SCAN_ROLLED = 0x03;
	public static final int LATENT_IMPRESSION = 0x04;
	public static final int LATENT_TRACING = 0x05;
	public static final int LATENT_PHOTO = 0x06;
	public static final int LATENT_LIFT = 0x07;
	public static final int LIVE_SCAN_SWIPE = 0x08;
	public static final int LIVE_SCAN_VERTICAL_ROLL = 0x09;
	public static final int LIVE_SCAN_PALM = 0x0A;
	public static final int NON_LIVE_SCAN_PALM = 0x0B;
	public static final int LATENT_PALM_IMPRESSION = 0x0C;
	public static final int LATENT_PALM_TRACING = 0x0D;
	public static final int LATENT_PALM_PHOTO = 0x0E;
	public static final int LATENT_PALM_LIFT = 0x0F;
	public static final int RESERVED_20 = 0x14;
	public static final int RESERVED_21 = 0x15;
	public static final int RESERVED_22 = 0x16;
	public static final int RESERVED_23 = 0x17;
	public static final int LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN = 0x18;
	public static final int RESERVED_25 = 0x19;
	public static final int RESERVED_26 = 0x1A;
	public static final int RESERVED_27 = 0x1B;
	public static final int OTHER = 0x1C;
	public static final int UNKNOWN = 0x1D;

	private int value;

	public FingerImpressionType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= LIVE_SCAN_PLAIN && value <= LATENT_PALM_LIFT)
				|| (value == LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN || value == OTHER
						|| value == UNKNOWN))
			return value;
		throw new IllegalArgumentException(
				"FingerImpressionType value can be between (0x00 and 0x0F) or (0x18 or 0x1C or 0x1D), set value is wrong ["
						+ value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
