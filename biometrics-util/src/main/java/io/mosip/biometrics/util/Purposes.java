package io.mosip.biometrics.util;

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

	public static Purposes fromCode(String code) throws Exception {
		 for (Purposes paramCode : Purposes.values()) {
	     	if (paramCode.getCode().equals(code)) {
	        	return paramCode;
	    	}
		 }
		 throw new Exception("Invalid Purpose Code");
	}
}
