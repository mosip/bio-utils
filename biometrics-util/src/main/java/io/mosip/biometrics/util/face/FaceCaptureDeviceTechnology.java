package io.mosip.biometrics.util.face;

/**
 * Capture Device Technology Identifier, 0x00. Table 4 Section 5.4.4 of ISO/IEC
 * 19794-5-2011.
 */
public class FaceCaptureDeviceTechnology {
	public static final int UNSPECIFIED = 0x00;
	public static final int STATIC_PHOTO_UNKNOWN_SOURCE = 0x01;
	public static final int STATIC_PHOTO_DIGITAL_CAMERA = 0x02;
	public static final int STATIC_PHOTO_SCANNER = 0x03;
	public static final int VIDEO_FRAME_UNKNOWN_SOURCE = 0x04;
	public static final int VIDEO_FRAME_ANALOG_CAMERA = 0x05;
	public static final int VIDEO_FRAME_DIGITAL_CAMERA = 0x06;
	public static final int FUTURE_07 = 0x07;
	public static final int FUTURE_7F = 0x7F;
	public static final int VENDOR_80 = 0x80;
	public static final int VENDOR_FF = 0xFF;

	private int value;

	public FaceCaptureDeviceTechnology(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= UNSPECIFIED && value <= VIDEO_FRAME_DIGITAL_CAMERA) || (value >= VENDOR_80 && value <= VENDOR_FF))
			return value;
		throw new IllegalArgumentException(
				"FaceCaptureDeviceTechnology value can be between (0x00 and 0x06) or (0x80 and 0xFF), set value is wrong ["
						+ value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
