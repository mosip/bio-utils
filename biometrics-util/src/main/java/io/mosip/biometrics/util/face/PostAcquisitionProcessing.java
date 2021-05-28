package io.mosip.biometrics.util.face;

/** Post Acquistion processing, 0x01. Table 19 of ISO/IEC 19794-5-2011. 
 * The (2 byte) Post-acquisition Processing bit field allows the specification of the kind of post processing that
 * has been applied to the original captured image. Each bit of the mask position listed in Table 19 shall be set to
 * 1 if the corresponding processing has been applied, and set to 0 if not. The mask position starts from 0 at the
 * lowest bit. All bits set to zero indicates that no post-acquisition processing has been applied at all. All reserved
 * bits shall be zero. 
 * */
public enum PostAcquisitionProcessing {

	ROTATED(0x0000), 
	CROPPED(0x0001), 
	DOWN_SAMPLED(0x0002), 
	WHITE_BALANCE_ADJUSTED(0x0003),
	MULTIPLY_COMPRESSED(0x0004),
	INTERPOLATED(0x0005),
	CONTRAST_STRETCHED(0x0006),
	POSE_CORRECTED(0x0007),
	MULTI_VIEW_IMAGE(0x0008),
	AGE_PROCESSED(0x0009),
	SUPER_RESOLUTION_PROCESSED(0x0010);

	private final int value;
	PostAcquisitionProcessing(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static PostAcquisitionProcessing fromValue(int value) {
		for (PostAcquisitionProcessing c : PostAcquisitionProcessing.values()) {
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
