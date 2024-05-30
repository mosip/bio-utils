package io.mosip.biometrics.util;

import io.mosip.biometrics.util.constant.BiometricUtilErrorCode;
import io.mosip.biometrics.util.exception.BiometricUtilException;

public enum Purposes {
	AUTH("Auth"),
	REGISTRATION("Registration");

	private final String code;

	private Purposes(final String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static Purposes fromCode(String code) {
		 for (Purposes paramCode : Purposes.values()) {
	     	if (paramCode.getCode().equals(code)) {
	        	return paramCode;
	    	}
		 }
		 throw new BiometricUtilException(BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorCode(), BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorMessage());
	}
}