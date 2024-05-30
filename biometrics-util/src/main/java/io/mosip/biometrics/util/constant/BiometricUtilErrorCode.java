package io.mosip.biometrics.util.constant;
/**
 * BiometricUtilErrorCode Enum for the errors.
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public enum BiometricUtilErrorCode {
	MODALITY_NOT_SUPPORTED_EXCEPTION("MOS-BIOU-001", "Modality not Supported"),
	CONVERT_EXCEPTION("MOS-BIOU-002", "Could not be converted"),
	DATA_NULL_OR_EMPTY_EXCEPTION("MOS-BIOU-003", "Data is null or empty"),
	IMAGE_TYPE_NOT_SUPPORTED_EXCEPTION("MOS-BIOU-004", "Image Type not supported, only JP2 and WSQ allowed"),
	INVALID_PURPOSE_TYPE_EXCEPTION("MOS-BIOU-005", "Purpose Type not supported, only Auth and Registration allowed"),

	TECHNICAL_ERROR_EXCEPTION("MOS-CNV-500", "Technical Error");

	private final String errorCode;
	private final String errorMessage;

	private BiometricUtilErrorCode(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public static BiometricUtilErrorCode fromErrorCode(String errorCode) {
		 for (BiometricUtilErrorCode paramCode : BiometricUtilErrorCode.values()) {
	     	if (paramCode.getErrorCode().equalsIgnoreCase(errorCode)) {
	        	return paramCode;
	    	}
	    }
		return TECHNICAL_ERROR_EXCEPTION;
	}
}