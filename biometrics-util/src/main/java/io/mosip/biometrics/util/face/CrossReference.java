package io.mosip.biometrics.util.face;

/** Cross reference, 0x00. Table 20 of ISO/IEC 19794-5-2011. 
 * The (1 byte) Cross Reference Data Type field denotes inter-dependencies when multiple representations are
 * stored in the interchange record. This is of particular interest in the case post-processing has been used (see
 * clause 10). Then representations that are of type Post-processed shall code the ordinal number of the
 * representation that they have been derived from, in the Cross Reference Field. The first representation of the
 * interchange format has the code 01HEX.
*/
public enum CrossReference {

	BASIC(0x0000);

	private final int value;
	CrossReference(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static CrossReference fromValue(int value) {
		for (CrossReference c : CrossReference.values()) {
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
