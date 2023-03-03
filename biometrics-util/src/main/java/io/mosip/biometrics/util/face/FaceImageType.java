package io.mosip.biometrics.util.face;

/** Face Image Type Table 16 of ISO/IEC 19794-5-2011. */
public class FaceImageType {
	public static final int BASIC = 0x00;
	public static final int FULL_FRONTAL = 0x01;
	public static final int TOKEN_FRONTAL = 0x02;
	public static final int POST_PROCESSED_FRONTAL = 0x03;
	public static final int RESERVED_004 = 0x04;
	public static final int RESERVED_127 = 0x7F;
	public static final int BASIC_3D = 0x80;
	public static final int FULL_FRONTAL_3D = 0x81;
	public static final int TOKEN_FRONTAL_3D = 0x82;
	public static final int RESERVED_131 = 0x83;
	public static final int RESERVED_255 = 0xFF;

	private int value;

	public FaceImageType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= BASIC && value <= POST_PROCESSED_FRONTAL) || (value >= BASIC_3D && value <= TOKEN_FRONTAL_3D))
			return value;
		throw new IllegalArgumentException(
				"FaceImageType value can be between (0x00 and 0x03) or (0x80 and 0x82), set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value & 0xFF) + ")";
	}
}
