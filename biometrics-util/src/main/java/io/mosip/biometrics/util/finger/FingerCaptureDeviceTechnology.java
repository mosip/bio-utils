package io.mosip.biometrics.util.finger;

/**
 * Finger Device Technology Identifier, 0x00. Table 4 � Capture device
 * technology of ISO/IEC 19794-4-2011.
 */
public class FingerCaptureDeviceTechnology {
	public static final int UNSPECIFIED = 0x00;
	public static final int WHITE_LIGHT_OPTICAL_TIR = 0x01;
	public static final int WHITE_LIGHT_OPTICAL_DIRECT_VIEW_ON_PLATEN = 0x02;
	public static final int WHITE_LIGHT_OPTICAL_TOUCHLESS = 0x03;
	public static final int MONOCHROMATIC_VISIBLE_OPTICAL_TIR = 0x04;
	public static final int MONOCHROMATIC_VISIBLE_OPTICAL_DIRECT_VIEW_ON_PLATEN = 0x05;
	public static final int MONOCHROMATIC_VISIBLE_OPTICAL_TOUCHLESS = 0x06;
	public static final int MONOCHROMATIC_IR_OPTICAL_TIR = 0x07;
	public static final int MONOCHROMATIC_IR_OPTICAL_DIRECT_VIEW_ON_PLATEN = 0x08;
	public static final int MONOCHROMATIC_IR_OPTICAL_TOUCHLESS = 0x09;
	public static final int MULTISPECTRAL_OPTICAL_TIR = 0x0A;
	public static final int MULTISPECTRAL_OPTICAL_DIRECT_VIEW_ON_PLATEN = 0x0B;
	public static final int MULTISPECTRAL_OPTICAL_TOUCHLESS = 0x0C;
	public static final int ELECTRO_LUMINESCENT = 0x0D;
	public static final int SEMICONDUCTOR_CAPACITIVE = 0x0E;
	public static final int SEMICONDUCTOR_RF = 0x0F;
	public static final int SEMICONDUCTOR_THERMAL = 0x10;
	public static final int PRESSURE_SENSITIVE = 0x11;
	public static final int ULTRASOUND = 0x12;
	public static final int MECHANICAL = 0x13;
	public static final int GLASS_FIBER = 0x14;

	private int value;

	public FingerCaptureDeviceTechnology(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= GLASS_FIBER)
			return value;
		throw new IllegalArgumentException(
				"FingerCaptureDeviceTechnology value can be between (0x00 and 0x14), set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
