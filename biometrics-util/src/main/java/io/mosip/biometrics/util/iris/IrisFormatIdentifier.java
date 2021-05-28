package io.mosip.biometrics.util.iris;

/** Facial Record Header 'I', 'I', 'R', 0x02. Table 3 of ISO/IEC 19794-6-2011. */
public enum IrisFormatIdentifier {

	FORMAT_IIR(0x49495200);

	private final int value;
	IrisFormatIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisFormatIdentifier fromValue(int value) {
		for (IrisFormatIdentifier c : IrisFormatIdentifier.values()) {
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
