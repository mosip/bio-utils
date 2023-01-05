package io.mosip.biometrics.util.finger;

/**
 * CompressionType Table 9 � Compression algorithm codes of ISO/IEC
 * 19794-4-2011.
 */
public class FingerImageCompressionType {
	public static final int NONE_NO_BIT_PACKING = 0x00;
	public static final int NONE_BIT_PACKED = 0x01;
	public static final int WSQ = 0x02;
	public static final int JPEG_LOSSY = 0x03;
	public static final int JPEG_2000_LOSSY = 0x04;
	public static final int JPEG_2000_LOSS_LESS = 0x05;
	public static final int PNG = 0x06;

	private int value;

	public FingerImageCompressionType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= NONE_NO_BIT_PACKING && value <= PNG)
			return value;
		throw new IllegalArgumentException(
				"FingerImageCompressionType value can be between (0x00 and 0x06), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
