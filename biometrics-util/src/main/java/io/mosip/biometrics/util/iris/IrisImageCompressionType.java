package io.mosip.biometrics.util.iris;

/**
 * CompressionType in Iris image properties bit field, Table 4 of ISO/IEC
 * 19794-6-2011.
 */
public class IrisImageCompressionType {
	public static final int UNDEFINED = 0x00;
	public static final int JPEG_LOSSLESS_OR_NONE = 0x01;
	public static final int JPEG_LOSSY = 0x02;

	private int value;

	public IrisImageCompressionType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNDEFINED && value <= JPEG_LOSSY)
			return value;
		throw new IllegalArgumentException(
				"IrisImageCompressionType value can be between (0x00 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
