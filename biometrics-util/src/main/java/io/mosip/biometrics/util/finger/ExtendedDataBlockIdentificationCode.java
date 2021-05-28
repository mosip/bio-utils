package io.mosip.biometrics.util.finger;

/** Finger Device Technology Identifier, 0x00. Table 4 � Capture device technology  of ISO/IEC 19794-4-2011. */
public enum ExtendedDataBlockIdentificationCode {

	RESERVED(0x0000), 
	SEGMENTATION(0x0001), 
	ANNOTATION(0x0002), 
	COMMENT_03(0x0003),
	COMMENT_04(0x0004),
	COMMENT_05(0x0005),
	COMMENT_06(0x0006),
	COMMENT_07(0x0007),
	COMMENT_08(0x0008),
	COMMENT_09(0x0009),
	COMMENT_FF(0x00FF);

	private final int value;
	ExtendedDataBlockIdentificationCode(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static ExtendedDataBlockIdentificationCode fromValue(int value) {
		for (ExtendedDataBlockIdentificationCode c : ExtendedDataBlockIdentificationCode.values()) {
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
