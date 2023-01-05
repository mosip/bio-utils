package io.mosip.biometrics.util.face;

/**
 * The Landmark Point Type. Table 12 of ISO/IEC 19794-5-2011. The (1 int)
 * Landmark Point Type field represents the type of the Landmark Point stored in
 * the Landmark Point block. This field shall be set to 01HEX to denote that
 * landmark point is an MPEG4 Feature Point as given by Annex C of ISO/IEC
 * 14496-2 and is represented by the 2D image coordinates. The field shall be
 * set to 02HEX to denote that the landmark point is an Anthropometric 2D
 * landmark and is represented by the 2D image coordinates. Finally, the field
 * shall be set to 03HEX to denote that the Landmark Point is an anthropometric
 * 3D landmark and is represented by its 3D coordinates. All other field values
 * are reserved by SC 37 for future definition of Landmark Point Types.
 */
public class LandmarkPointType {
	public static final int MPEG4_FEATURE = 0x01;
	public static final int ANTHROPOMETRIC_2D_LANDMARK = 0x02;
	public static final int ANTHROPOMETRIC_3D_LANDMARK = 0x03;
	public static final int RESERVED_04 = 0x04;
	public static final int RESERVED_FF = 0xFF;

	private int value;

	public LandmarkPointType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= MPEG4_FEATURE && value <= ANTHROPOMETRIC_3D_LANDMARK))
			return value;
		throw new IllegalArgumentException(
				"LandmarkPointType value can be between (0x00 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
