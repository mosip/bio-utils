package io.mosip.biometrics.util.face;

/** Capture Device Technology Identifier, 0x00. Table 4 Section 5.4.4 of ISO/IEC 19794-5-2011. */
public enum FaceCaptureDeviceTechnology {

	UNSPECIFIED(0x0000), 
	STATIC_PHOTO_UNKNOWN_SOURCE(0x0001), 
	STATIC_PHOTO_DIGITAL_CAMERA(0x0002), 
	STATIC_PHOTO_SCANNER(0x0003), 
	VIDEO_FRAME_UNKNOWN_SOURCE(0x0004), 
	VIDEO_FRAME_ANALOG_CAMERA(0x0005), 
	VIDEO_FRAME_DIGITAL_CAMERA(0x0006),
	UNKNOWN(0x0007);

	private final int value;
	FaceCaptureDeviceTechnology(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceCaptureDeviceTechnology fromValue(int value) {
		for (FaceCaptureDeviceTechnology c : FaceCaptureDeviceTechnology.values()) {
			if (c.value == value) {
				return c;
			}
		}
		return UNSPECIFIED;
		//throw new IllegalArgumentException(value + "");
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
