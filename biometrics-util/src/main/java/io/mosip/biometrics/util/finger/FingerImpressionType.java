package io.mosip.biometrics.util.finger;

/** ImpressionType Table 10 � Finger and palm impression codes of ISO/IEC 19794-4-2011. 
*/
public enum FingerImpressionType {
	
	LIVE_SCAN_PLAIN(0x0000), 
	LIVE_SCAN_ROLLED(0x0001), 
	NON_LIVE_SCAN_PLAIN(0x0002), 
	NON_LIVE_SCAN_ROLLED(0x0003), 
	LATENT_IMPRESSION(0x0004), 
	LATENT_TRACING(0x0005),
	LATENT_PHOTO(0x0006), 
	LATENT_LIFT(0x0007),
	LIVE_SCAN_SWIPE(0x0008), 
	LIVE_SCAN_VERTICAL_ROLL(0x0009), 
	LIVE_SCAN_PALM(0x000A), 
	NON_LIVE_SCAN_PALM(0x000B), 
	LATENT_PALM_IMPRESSION(0x000C), 
	LATENT_PALM_TRACING(0x000D), 
	LATENT_PALM_PHOTO(0x000E), 
	LATENT_PALM_LIFT(0x000F), 
	UNKNOWN(0x001D); 
	
	private final int value;
	FingerImpressionType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerImpressionType fromValue(int value) {
		for (FingerImpressionType c : FingerImpressionType.values()) {
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
