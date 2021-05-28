package io.mosip.biometrics.util.finger;

/** Facial Record Header 'F', 'I', 'R', Table 1 � General record header of ISO/IEC 19794-4-2011. */
public enum FingerFormatIdentifier {

	FORMAT_FIR(0x46495200);

	private final int value;
	FingerFormatIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerFormatIdentifier fromValue(int value) {
		for (FingerFormatIdentifier c : FingerFormatIdentifier.values()) {
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
