package io.mosip.biometrics.util.finger;

/** FingerImageBitDepth, Table 2 � Finger image representation header record of ISO/IEC 19794-6-2011. 
*/
public enum FingerImageBitDepth {

	BPP_01(0x01),
	BPP_02(0x02),
	BPP_03(0x03),
	BPP_04(0x04),
	BPP_05(0x05),
	BPP_06(0x06),
	BPP_07(0x07),
	BPP_08(0x08);

	private final int value;
	FingerImageBitDepth(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerImageBitDepth fromValue(int value) {
		for (FingerImageBitDepth c : FingerImageBitDepth.values()) {
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
