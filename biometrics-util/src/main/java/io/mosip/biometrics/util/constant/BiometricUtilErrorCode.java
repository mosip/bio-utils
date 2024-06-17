package io.mosip.biometrics.util.constant;

/**
 * BiometricUtilErrorCode representing error codes and messages used in
 * biometric utility operations.
 *
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public enum BiometricUtilErrorCode {
	/**
	 * Error code for modality not supported exception.
	 */
	MODALITY_NOT_SUPPORTED_EXCEPTION("MOS-BIOU-001", "Modality not Supported"),
	/**
	 * Error code for conversion exception.
	 */
	CONVERT_EXCEPTION("MOS-BIOU-002", "Could not be converted"),
	/**
	 * Error code for data being null or empty.
	 */
	DATA_NULL_OR_EMPTY_EXCEPTION("MOS-BIOU-003", "Data is null or empty"),
	/**
	 * Error code for image type not supported exception.
	 */
	IMAGE_TYPE_NOT_SUPPORTED_EXCEPTION("MOS-BIOU-004", "Image Type not supported, only JP2 and WSQ allowed"),
	/**
	 * Error code for invalid purpose type exception.
	 */
	INVALID_PURPOSE_TYPE_EXCEPTION("MOS-BIOU-005", "Purpose Type not supported, only Auth and Registration allowed"),

	/**
	 * Generic technical error exception code.
	 */
	TECHNICAL_ERROR_EXCEPTION("MOS-CNV-500", "Technical Error");

	private final String errorCode;
	private final String errorMessage;

	/**
	 * Constructor for BiometricUtilErrorCode enum.
	 *
	 * @param errorCode    The error code associated with the enum constant.
	 * @param errorMessage The error message associated with the enum constant.
	 */
	private BiometricUtilErrorCode(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Retrieves the error code.
	 *
	 * @return The error code as a String.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Retrieves the error message.
	 *
	 * @return The error message as a String.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Retrieves the BiometricUtilErrorCode enum constant based on the error code.
	 * If no matching enum constant is found, returns TECHNICAL_ERROR_EXCEPTION.
	 *
	 * @param errorCode The error code to match.
	 * @return The matching BiometricUtilErrorCode enum constant, or
	 *         TECHNICAL_ERROR_EXCEPTION if not found.
	 */
	public static BiometricUtilErrorCode fromErrorCode(String errorCode) {
		for (BiometricUtilErrorCode paramCode : BiometricUtilErrorCode.values()) {
			if (paramCode.getErrorCode().equalsIgnoreCase(errorCode)) {
				return paramCode;
			}
		}
		return TECHNICAL_ERROR_EXCEPTION;
	}
}