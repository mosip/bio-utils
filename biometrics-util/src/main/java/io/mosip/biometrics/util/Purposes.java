package io.mosip.biometrics.util;

import io.mosip.biometrics.util.constant.BiometricUtilErrorCode;
import io.mosip.biometrics.util.exception.BiometricUtilException;

/**
 * Enum representing different purposes.
 */
public enum Purposes {
	AUTH("Auth"), // Purpose for authentication
	REGISTRATION("Registration"); // Purpose for registration

	private final String code; // Code associated with the purpose

	/**
	 * Constructs a purpose enum with the given code.
	 *
	 * @param code The code associated with the purpose.
	 */
	private Purposes(final String code) {
		this.code = code;
	}

	/**
	 * Gets the code associated with the purpose.
	 *
	 * @return The code of the purpose.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns the Purposes enum constant associated with the given code.
	 *
	 * @param code The code to match.
	 * @return The Purposes enum constant corresponding to the given code.
	 * @throws BiometricUtilException If the provided code does not match any of the
	 *                                enum constants.
	 */
	public static Purposes fromCode(String code) {
		for (Purposes paramCode : Purposes.values()) {
			if (paramCode.getCode().equals(code)) {
				return paramCode;
			}
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorMessage());
	}
}