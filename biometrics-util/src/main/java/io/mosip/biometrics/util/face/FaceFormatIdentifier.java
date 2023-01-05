package io.mosip.biometrics.util.face;

/**
 * Facial Record Header 'F', 'A', 'C', 0x00. Section 5.3.1 of ISO/IEC
 * 19794-5-2011.
 */
public class FaceFormatIdentifier {
	public static final long FORMAT_FAC = 0x46414300;

	private final long value;

	public FaceFormatIdentifier(int value) {
		this.value = value;
	}

	public long value() {
		return this.value;
	}

	public static long fromValue(long value) {
		if (value == FORMAT_FAC)
			return value;
		throw new IllegalArgumentException(
				"FaceFormatIdentifier value can be 0x46414300, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Long.toHexString(value) + ")";
	}
}
