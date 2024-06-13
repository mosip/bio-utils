package io.mosip.biometrics.util;

/**
 * Enumeration representing different biometric modalities.
 */
@SuppressWarnings({ "java:S115" })
public enum Modality {
	UnSpecified(0x0000), Finger(0x0001), Face(0x0002), Iris(0x0003);

	private final int value;

	/**
	 * Constructs a Modality enum with the specified integer value.
	 * 
	 * @param value the integer value associated with the modality
	 */
	Modality(int value) {
		this.value = value;
	}

	/**
	 * Returns the integer value associated with this modality.
	 * 
	 * @return the integer value of the modality
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Retrieves the Modality enum constant associated with the given integer value.
	 * 
	 * @param value the integer value of the modality to retrieve
	 * @return the Modality enum constant corresponding to the provided value
	 * @throws IllegalArgumentException if the value does not match any Modality
	 *                                  enum constant
	 */
	public static Modality fromValue(int value) {
		for (Modality c : Modality.values()) {
			if (c.value == value) {
				return c;
			}
		}
		throw new IllegalArgumentException(value + "");
	}

	/**
	 * Returns a string representation of the Modality enum constant, including its
	 * name and hexadecimal value.
	 * 
	 * @return a string representation of the Modality enum constant
	 */
	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
