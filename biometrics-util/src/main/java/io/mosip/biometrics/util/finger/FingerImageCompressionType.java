package io.mosip.biometrics.util.finger;

/** CompressionType Table 9 � Compression algorithm codes of ISO/IEC 19794-4-2011. 
*/
public enum FingerImageCompressionType {

	NONE_NO_BIT_PACKING(0x0000), 
	NONE_BIT_PACKED(0x0001), 
	WSQ(0x0002), 
	JPEG_LOSSY(0x0003), 
	JPEG_2000_LOSSY(0x0004), 
	JPEG_2000_LOSS_LESS(0x0005),
	PNG(0x0006); 
	
	private final int value;
	FingerImageCompressionType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerImageCompressionType fromValue(int value) {
		for (FingerImageCompressionType c : FingerImageCompressionType.values()) {
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
