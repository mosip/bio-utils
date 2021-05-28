package io.mosip.biometrics.util.face;

/** Facial Record Header 'F', 'A', 'C', 0x00. Section 5.3.1 of ISO/IEC 19794-5-2011. */
public enum FaceFormatIdentifier {

	FORMAT_FAC(0x46414300);

	private final int value;
	FaceFormatIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceFormatIdentifier fromValue(int value) {
		for (FaceFormatIdentifier c : FaceFormatIdentifier.values()) {
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
