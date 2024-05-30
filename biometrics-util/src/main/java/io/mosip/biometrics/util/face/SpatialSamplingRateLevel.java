package io.mosip.biometrics.util.face;

/**
 * Spatial Sampling Rate Level, 0x00. Table 18 of ISO/IEC 19794-5-2011. For
 * specific application domains different minimal spatial sampling rates of the
 * interchange data may be required. For example using higher spatial sampling
 * rate images allow for specific human as well as machine inspection methods
 * that depend on the analysis of very small details. The (1 int) Spatial
 * Sampling Rate Level field (see Table 18) provides information on the number
 * of pixels in the image across the width of the head. (The Width of Head (CC))
 */
public class SpatialSamplingRateLevel {
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_180 = 0x00;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_180_TO_240 = 0x01;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_240_TO_300 = 0x02;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_300_TO_370 = 0x03;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_370_TO_480 = 0x04;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_480_TO_610 = 0x05;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_610_TO_750 = 0x06;
	public static final int SPATIAL_SAMPLING_RATE_LEVEL_750 = 0x07;
	public static final int RESERVED_08 = 0x08;
	public static final int RESERVED_FF = 0xFF;

	private int value;

	public SpatialSamplingRateLevel(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= SPATIAL_SAMPLING_RATE_LEVEL_180 && value <= SPATIAL_SAMPLING_RATE_LEVEL_750)
			return value;
		throw new IllegalArgumentException(
				"SpatialSamplingRateLevel value can be between (0x00 and 0x07), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
