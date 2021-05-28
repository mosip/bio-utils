package io.mosip.biometrics.util.finger;

/** FingerPosition, Table 6 � Finger position codes and Table 7 � Multiple finger position codes and Table 8 � Palm codes  ISO/IEC 19794-4-2011. 
*/
public enum FingerPosition {

	UNKNOWN(0x00), 
	RIGHT_THUMB(0x01), 
	RIGHT_INDEX_FINGER(0x02), 
	RIGHT_MIDDLE_FINGER(0x03), 
	RIGHT_RING_FINGER(0x04), 
	RIGHT_LITTLE_FINGER(0x05), 
	LEFT_THUMB(0x06), 
	LEFT_INDEX_FINGER(0x07), 
	LEFT_MIDDLE_FINGER(0x08), 
	LEFT_RING_FINGER(0x09),
	LEFT_LITTLE_FINGER(0x0A),
	PLAIN_RIGHT_FOUR_FINGERS(0x0D),
	PLAIN_LEFT_FOUR_FINGERS(0x0E),
	PLAIN_TWO_THUMBS(0x0F),	
	/** Table 8 � Palm codes ISO 19794-4-2011. */
	UNKNOWN_PALM(0x14), 
	RIGHT_FULL_PALM(0x15), 
	RIGHT_WRITER_S_PALM(0x16), 
	LEFT_FULL_PALM(0x17), 
	LEFT_WRITER_S_PALM(0x18), 
	RIGHT_LOWER_PALM(0x19), 
	RIGHT_UPPER_PALM(0x1A), 
	LEFT_LOWER_PALM(0x1B), 
	LEFT_UPPER_PALM(0x1C), 
	RIGHT_OTHER(0x1D), 
	LEFT_OTHER(0x1E), 
	RIGHT_INTERDIGITAL(0x1F), 
	RIGHT_THENAR(0x20), 
	RIGHT_HYPOTHENAR(0x21), 
	LEFT_INTERDIGITAL(0x22), 
	LEFT_THENAR(0x23), 
	LEFT_HYPOTHENAR(0x24),	
	//2-Finger Combinations
	RIGHT_INDEX_AND_MIDDLE_FINGERS(0x28),
	RIGHT_MIDDLE_AND_RING_FINGERS(0x29),
	RIGHT_RING_AND_LITTLE_FINGERS(0x2A),
	LEFT_INDEX_AND_MIDDLE_FINGERS(0x2B),
	LEFT_MIDDLE_AND_RING_FINGERS(0x2C),
	LEFT_RING_AND_LITTLE_FINGERS(0x2D),
	RIGHT_INDEX_AND_LEFT_INDEX_FINGERS(0x2E),
	//3-Finger Combinations
	RIGHT_INDEX_AND_MIDDLE_AND_RING_FINGERS(0x2F),
	RIGHT_MIDDLE_AND_RING_AND_LITTLE_FINGERS(0x30),
	LEFT_INDEX_AND_MIDDLE_AND_RING_FINGERS(0x31),
	LEFT_MIDDLE_AND_RING_AND_LITTLE_FINGERS(0x32);
	
	private final int value;
	FingerPosition(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerPosition fromValue(int value) {
		for (FingerPosition c : FingerPosition.values()) {
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
