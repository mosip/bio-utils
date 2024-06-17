package io.mosip.biometrics.util;

/**
 * Enum representing different types of image formats with their corresponding
 * values.
 */
public enum ImageType {
	JPEG2000(0x0000), WSQ(0x0001), JPEG(0x0002), PNG(0x0003), WEBP(0x0004);

	private final int value;

	/**
	 * Constructor for ImageType enum.
	 * 
	 * @param value the integer value associated with the enum constant
	 */
	ImageType(int value) {
		this.value = value;
	}

	/**
	 * Retrieves the integer value associated with this enum constant.
	 * 
	 * @return the integer value of the enum constant
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Returns the ImageType enum constant corresponding to the given integer value.
	 * 
	 * @param value the integer value to match against enum constants
	 * @return the ImageType enum constant matching the given value
	 * @throws IllegalArgumentException if no matching enum constant is found for
	 *                                  the given value
	 */
	public static ImageType fromValue(int value) {
		for (ImageType c : ImageType.values()) {
			if (c.value == value) {
				return c;
			}
		}
		throw new IllegalArgumentException(value + "");
	}

	/**
	 * Returns a string representation of the ImageType enum constant, including its
	 * name and hexadecimal value.
	 * 
	 * @return a string representation of the ImageType enum constant
	 */
	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}