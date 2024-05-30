package io.mosip.biometrics.util.exception;


import io.mosip.kernel.core.exception.BaseUncheckedException;

/**
 * Custom Exception Class in case of error occurred in services.
 * 
 * @see io.mosip.kernel.core.exception.BaseUncheckedException
 * @author Janardhan B S
 * @since 1.0.0
 */
public class BiometricUtilException extends BaseUncheckedException {
	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 687991492884005033L;

	/**
	 * Constructor the initialize Handler exception
	 * 
	 * @param errorCode    The error code for this exception
	 * @param errorMessage The error message for this exception
	 */
	public BiometricUtilException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	/**
	 * Constructor the initialize Handler exception
	 * 
	 * @param errorCode    The error code for this exception
	 * @param errorMessage The error message for this exception
	 * @param rootCause    the specified cause
	 */
	public BiometricUtilException(String errorCode, String errorMessage, Throwable rootCause) {
		super(errorCode, errorMessage, rootCause);
	}
}