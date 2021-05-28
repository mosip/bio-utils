package io.mosip.biometrics.util.iris;

/** CompressionType in Iris image properties bit field, Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum IrisImageCompressionType {

	UNDEFINED(0x0000), 
	JPEG_LOSSLESS_OR_NONE(0x0001), 
	JPEG_LOSSY(0x0002);

	private final int value;
	IrisImageCompressionType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisImageCompressionType fromValue(int value) {
		for (IrisImageCompressionType c : IrisImageCompressionType.values()) {
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
