package io.mosip.biometrics.util.face;

/** Face Image Type Table 16 of ISO/IEC 19794-5-2011. */
public enum FaceImageType {

	BASIC(0x0000), 
	FULL_FRONTAL(0x0001), 
	TOKEN_FRONTAL(0x0002), 
	POST_PROCESSED_FRONTAL(0x0003),
	BASIC_3D(0x0080), 
	FULL_FRONTAL_3D(0x0081), 
	TOKEN_FRONTAL_3D(0x0082);

	private final int value;
	FaceImageType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceImageType fromValue(int value) {
		for (FaceImageType c : FaceImageType.values()) {
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
