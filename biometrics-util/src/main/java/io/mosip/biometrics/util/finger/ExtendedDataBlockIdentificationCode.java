package io.mosip.biometrics.util.finger;

/**
 * Finger Device Technology Identifier, 0x00. Table 4 Capture device
 * technology of ISO/IEC 19794-4-2011.
 */
public class ExtendedDataBlockIdentificationCode {
	public static final int RESERVED = 0x0000;
	public static final int SEGMENTATION = 0x0001;
	public static final int ANNOTATION = 0x0002;
	public static final int COMMENT_03 = 0x0003;// Minumum
	public static final int COMMENT_FF = 0x00FF;// Maximum
	public static final int VENDOR_0100 = 0x0100;// Minumum
	public static final int VENDOR_FFFF = 0xFFFF;// Maximum

	private int value;

	public ExtendedDataBlockIdentificationCode(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= SEGMENTATION || value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"AnnotationCode value can be between (0x0001 and 0xFFFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
