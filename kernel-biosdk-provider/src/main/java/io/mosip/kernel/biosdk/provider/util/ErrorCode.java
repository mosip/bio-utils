package io.mosip.kernel.biosdk.provider.util;

/**
 * Enumeration of error codes and corresponding error messages related to
 * biometric SDK operations. Each enum constant encapsulates a specific error
 * code and its descriptive error message.
 */
public enum ErrorCode {
	/** Error code: BIO_SDK_001 */
	NO_PROVIDERS("BIO_SDK_001", "No Biometric provider API implementations found"),

	/** Error code: BIO_SDK_002 */
	SDK_INITIALIZATION_FAILED("BIO_SDK_002", "Failed to initialize %s due to %s"),

	/** Error code: BIO_SDK_003 */
	NO_CONSTRUCTOR_FOUND("BIO_SDK_003", "Constructor not found for %s with args %s"),

	/** Error code: BIO_SDK_004 */
	NO_SDK_CONFIG("BIO_SDK_004", "SDK Configurations not found"),

	/** Error code: BIO_SDK_005 */
	INVALID_SDK_VERSION("BIO_SDK_005", "Configured SDK version is different"),

	/** Error code: BIO_SDK_006 */
	UNSUPPORTED_OPERATION("BIO_SDK_006", "Unsupported Operation"),

	/** Error code: BIO_SDK_007 */
	SDK_REGISTRY_EMPTY("BIO_SDK_007", "SDK provider registry is empty!");

	/** The error code string. */
	private final String errorCode;

	/** The error message string. */
	private final String errorMessage;

	/**
	 * Constructor for ErrorCode enum.
	 *
	 * @param errorCode    The unique error code string associated with the error.
	 * @param errorMessage The descriptive error message explaining the error.
	 */
	ErrorCode(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Retrieves the error code.
	 *
	 * @return The error code string.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Retrieves the error message.
	 *
	 * @return The error message string.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}