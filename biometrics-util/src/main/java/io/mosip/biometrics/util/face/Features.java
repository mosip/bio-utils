package io.mosip.biometrics.util.face;

/** Feature/Property flags meaning based on Table 10 of ISO 19794-5. */
public enum Features {

	FEATURES_ARE_SPECIFIED(0x000000), 
	GLASSES(0x000001), 
	MOUSTACHE(0x000002), 
	BEARD(0x000003), 
	TEETH_VISIBLE(0x000004), 
	BLINK(0x000005), 
	MOUTH_OPEN(0x000006), 
	LEFT_EYE_PATCH(0x000007), 
	RIGHT_EYE_PATCH(0x000008), 
	DARK_GLASSES(0x000009), 
	HEAD_COVERING_PRESENT(0x000010), 
	DISTORTING_MEDICAL_CONDITION(0x000011);

	private final int value;
	Features(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static Features fromValue(int value) {
		for (Features c : Features.values()) {
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
