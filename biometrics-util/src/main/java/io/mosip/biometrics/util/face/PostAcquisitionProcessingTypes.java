package io.mosip.biometrics.util.face;

/**
 * Post Acquistion processing, 0x01. Table 19 of ISO/IEC 19794-5-2011. The (2
 * byte) Post-acquisition Processing bit field allows the specification of the
 * kind of post processing that has been applied to the original captured image.
 * Each bit of the mask position listed in Table 19 shall be set to 1 if the
 * corresponding processing has been applied, and set to 0 if not. The mask
 * position starts from 0 at the lowest bit. All bits set to zero indicates that
 * no post-acquisition processing has been applied at all. All reserved bits
 * shall be zero.
 */
public class PostAcquisitionProcessingTypes {
	public static final int ROTATED = 0x00;
	public static final int CROPPED = 0x01;
	public static final int DOWN_SAMPLED = 0x02;
	public static final int WHITE_BALANCE_ADJUSTED = 0x03;
	public static final int MULTIPLY_COMPRESSED = 0x04;
	public static final int INTERPOLATED = 0x05;
	public static final int CONTRAST_STRETCHED = 0x06;
	public static final int POSE_CORRECTED = 0x07;
	public static final int MULTI_VIEW_IMAGE = 0x08;
	public static final int AGE_PROCESSED = 0x09;
	public static final int SUPER_RESOLUTION_PROCESSED = 0x10;
	public static final int RESERVED_11 = 0x11;
	public static final int RESERVED_15 = 0x15;

	private int value;

	public PostAcquisitionProcessingTypes(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= ROTATED && value <= SUPER_RESOLUTION_PROCESSED)
			return value;
		throw new IllegalArgumentException(
				"PostAcquisitionProcessingTypes value can be between (0x00 and 0x10), set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
