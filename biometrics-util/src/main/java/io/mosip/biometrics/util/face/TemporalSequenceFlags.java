package io.mosip.biometrics.util.face;

/**
 * Temporal sequence flags and values Table 3 of ISO/IEC 19794-5-2011. NOTE 1
 * The minimum interval is 4 * 0,001 seconds = 0,004 seconds. NOTE 2 The maximum
 * interval is 65 534 * 0,001 seconds = 65,534 seconds.
 */
public class TemporalSequenceFlags {
	public static final int ONE_REPRESENTATION = 0x0000;
	public static final int TWO_OR_MORE_REPRESENTATION_TEMPOROL_RELATIONSHIP_UNSPECIFIED = 0x0001;
	public static final int TWO_OR_MORE_REPRESENTATION_REPRESENTATION_TAKEN_IRRUGULAR_INTERVALS_SAME_SESSION = 0x0002;
	public static final int TWO_OR_MORE_REPRESENTATION_REPRESENTATION_TAKEN_IRRUGULAR_INTERVALS_DIFFERENT_SESSION = 0x0003;
	public static final int No_OF_MILLISECONDS_BETWEEN_SEQUENTIAL_REPRESENTATION_0004 = 0x0004;
	public static final int No_OF_MILLISECONDS_BETWEEN_SEQUENTIAL_REPRESENTATION_FFFE = 0xFFFE;
	public static final int TEMPOROL_REPRESENTATION_TAKEN_AT_REGULAR_INTERVAL_EXCEEDING_FFFF = 0xFFFF;

	private int value;

	public TemporalSequenceFlags(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= ONE_REPRESENTATION && value <= TEMPOROL_REPRESENTATION_TAKEN_AT_REGULAR_INTERVAL_EXCEEDING_FFFF)
			return value;
		throw new IllegalArgumentException(
				"TemporalSequenceFlags value can be between 0x0000 and 0xFFFF, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
