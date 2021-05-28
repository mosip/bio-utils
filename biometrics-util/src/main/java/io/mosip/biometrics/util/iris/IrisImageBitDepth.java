package io.mosip.biometrics.util.iris;

/** Bit Depth, Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum IrisImageBitDepth {

	BPP_8(0x0008);

	private final int value;
	IrisImageBitDepth(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisImageBitDepth fromValue(int value) {
		for (IrisImageBitDepth c : IrisImageBitDepth.values()) {
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
