package io.mosip.biometrics.util.face;

/** Spatial Sampling Rate Level, 0x00. Table 18 of ISO/IEC 19794-5-2011. 
 * For specific application domains different minimal spatial sampling rates of the interchange data may be
 * required. For example using higher spatial sampling rate images allow for specific human as well as machine
 * inspection methods that depend on the analysis of very small details. The (1 byte) Spatial Sampling Rate
 * Level field (see Table 18) provides information on the number of pixels in the image across the width of the
 * head. (The Width of Head (CC)) 
*/
public enum SpatialSamplingRateLevel {

	SPATIAL_SAMPLING_RATE_LEVEL_180(0x0000), 
	SPATIAL_SAMPLING_RATE_LEVEL_180_TO_240(0x0001), 
	SPATIAL_SAMPLING_RATE_LEVEL_240_TO_300(0x0002), 
	SPATIAL_SAMPLING_RATE_LEVEL_300_TO_370(0x0003),
	SPATIAL_SAMPLING_RATE_LEVEL_370_TO_480(0x0004),
	SPATIAL_SAMPLING_RATE_LEVEL_480_TO_610(0x0005),
	SPATIAL_SAMPLING_RATE_LEVEL_610_TO_750(0x0006),
	SPATIAL_SAMPLING_RATE_LEVEL_750(0x0007);

	private final int value;
	SpatialSamplingRateLevel(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static SpatialSamplingRateLevel fromValue(int value) {
		for (SpatialSamplingRateLevel c : SpatialSamplingRateLevel.values()) {
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
