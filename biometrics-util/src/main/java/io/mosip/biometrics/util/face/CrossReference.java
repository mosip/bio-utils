package io.mosip.biometrics.util.face;

/**
 * Cross reference, 0x00. Table 20 of ISO/IEC 19794-5-2011. The (1 byte) Cross
 * Reference Data Type field denotes inter-dependencies when multiple
 * representations are stored in the interchange record. This is of particular
 * interest in the case post-processing has been used (see clause 10). Then
 * representations that are of type Post-processed shall code the ordinal number
 * of the representation that they have been derived from, in the Cross
 * Reference Field. The first representation of the interchange format has the
 * code 01HEX. Example: There are four representations in the overall record.
 * The second representation has been postprocessed and resulted in the fourth
 * representation. Then, the fourth representation shall have Cross Reference
 * set to 2, all other Records shall have set Cross Reference to 0.
 */
public class CrossReference {
	public static final int BASIC = 0x00;
	public static final int CROSSREFERENCE_FF = 0xFF;

	private int value;

	public CrossReference(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= BASIC && value <= CROSSREFERENCE_FF)
			return value;
		throw new IllegalArgumentException(
				"CrossReference value can be between 0x00 and 0xFF, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
