package io.mosip.biometrics.util.face;

/** The Landmark Point Type. Table 12 of ISO/IEC 19794-5-2011. 
 * The (1 byte) Landmark Point Type field represents the type of the Landmark Point stored in the Landmark
 * Point block. This field shall be set to 01HEX to denote that landmark point is an MPEG4 Feature Point as given
 * by Annex C of ISO/IEC 14496-2 and is represented by the 2D image coordinates. The field shall be set to
 * 02HEX to denote that the landmark point is an Anthropometric 2D landmark and is represented by the 2D
 * image coordinates. Finally, the field shall be set to 03HEX to denote that the Landmark Point is an
 * anthropometric 3D landmark and is represented by its 3D coordinates. All other field values are reserved by
 * SC 37 for future definition of Landmark Point Types. 
*/
public enum LandmarkPointType {

	MPEG4_FEATURE(0x0000), 
	ANTHROPOMETRIC_2D_LANDMARK(0x0001), 
	ANTHROPOMETRIC_3D_LANDMARK(0x0002); 

	private final int value;
	LandmarkPointType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static LandmarkPointType fromValue(int value) {
		for (LandmarkPointType c : LandmarkPointType.values()) {
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
