package io.mosip.biometrics.util.finger;

/** Finger Device Technology Identifier, 0x00. Table 4 � Capture device technology  of ISO/IEC 19794-4-2011. */
public enum FingerCaptureDeviceTechnology {

	UNSPECIFIED(0x0000), 
	WHITE_LIGHT_OPTICAL_TIR(0x0001), 
	WHITE_LIGHT_OPTICAL_DIRECT_VIEW_ON_PLATEN(0x0002), 
	WHITE_LIGHT_OPTICAL_TOUCHLESS(0x0003), 
	MONOCHROMATIC_VISIBLE_OPTICAL_TIR(0x0004), 
	MONOCHROMATIC_VISIBLE_OPTICAL_DIRECT_VIEW_ON_PLATEN(0x0005), 
	MONOCHROMATIC_VISIBLE_OPTICAL_TOUCHLESS(0x0006),
	MONOCHROMATIC_IR_OPTICAL_TIR(0x0007), 
	MONOCHROMATIC_IR_OPTICAL_DIRECT_VIEW_ON_PLATEN(0x0008), 
	MONOCHROMATIC_IR_OPTICAL_TOUCHLESS(0x0009),
	MULTISPECTRAL_OPTICAL_TIR(0x000A), 
	MULTISPECTRAL_OPTICAL_DIRECT_VIEW_ON_PLATEN(0x000B), 
	ELECTRO_LUMINESCENT(0x000C),
	SEMICONDUCTOR_CAPACITIVE(0x000D),
	SEMICONDUCTOR_RF(0x000E),
	SEMICONDUCTOR_THERMAL(0x000F),
	PRESSURE_SENSITIVE(0x0010),
	ULTRASOUND(0x0011),
	MECHANICAL(0x0012),
	GLASS_FIBER(0x0013);

	private final int value;
	FingerCaptureDeviceTechnology(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerCaptureDeviceTechnology fromValue(int value) {
		for (FingerCaptureDeviceTechnology c : FingerCaptureDeviceTechnology.values()) {
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
