package io.mosip.biometrics.util.face;

/** Temporal sequence flags and values Table 3 of ISO/IEC 19794-5-2011. */
public enum TemporalSequenceFlags {

	ONE_REPRESENTATION(0x0000), 
	TWO_OR_MORE_REPRESENTATION_TEMPOROL_RELATIONSHIP_UNSPECIFIED(0x0001), 
	TWO_OR_MORE_REPRESENTATION_REPRESENTATION_TAKEN_IRRUGULAR_INTERVALS_SAME_SESSION(0x0002), 
	TWO_OR_MORE_REPRESENTATION_REPRESENTATION_TAKEN_IRRUGULAR_INTERVALS_DIFFERENT_SESSION(0x0003); 

	private final int value;
	TemporalSequenceFlags(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static TemporalSequenceFlags fromValue(int value) {
		for (TemporalSequenceFlags c : TemporalSequenceFlags.values()) {
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
