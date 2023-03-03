package io.mosip.biometrics.util;

public enum ImageType {
	JPEG2000(0x0000), 
	WSQ(0x0001), 
	JPEG(0x0002), 
	PNG(0x0003),
	WEBP(0x0004);

	private final int value;
	ImageType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static ImageType fromValue(int value) {
		for (ImageType c : ImageType.values()) {
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
