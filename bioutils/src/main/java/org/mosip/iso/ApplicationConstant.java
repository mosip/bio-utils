package org.mosip.iso;

public class ApplicationConstant {
	/** File Conversion Type */
	public static String MOSIP_CONVERT_ISO_TO_JP2000 = "org.mosip.bio.utils.convert.jp2000.to.iso";
	public static String MOSIP_CONVERT_JP2000_TO_ISO = "org.mosip.bio.utils.convert.iso.to.jp2000";

	/** Biometric Types Folder Path */
	public static String MOSIP_BIOMETRIC_TYPE_FINGER = "mosip.mock.sbi.biometric.type.finger.folder.path";
	public static String MOSIP_BIOMETRIC_TYPE_FACE = "mosip.mock.sbi.biometric.type.face.folder.path";
	public static String MOSIP_BIOMETRIC_TYPE_IRIS = "mosip.mock.sbi.biometric.type.iris.folder.path";

	/** Biometric Sub Types Name values */
    public static String BIO_SUB_TYPE_UNKNOWN 		= "mosip.mock.sbi.biometric.subtype.unknown";		// Do not known left or right iris
    public static String BIO_SUB_TYPE_LEFT			= "mosip.mock.sbi.biometric.subtype.left";			// LEFT IRIS IMAGE
    public static String BIO_SUB_TYPE_RIGHT			= "mosip.mock.sbi.biometric.subtype.right";			// RIGHT IRIS IMAGE
    public static String BIO_SUB_TYPE_RIGHT_THUMB 	= "mosip.mock.sbi.biometric.subtype.right.thumb";
    public static String BIO_SUB_TYPE_RIGHT_INDEX 	= "mosip.mock.sbi.biometric.subtype.right.index";
    public static String BIO_SUB_TYPE_RIGHT_MIDDLE 	= "mosip.mock.sbi.biometric.subtype.right.middle";
    public static String BIO_SUB_TYPE_RIGHT_RING 	= "mosip.mock.sbi.biometric.subtype.right.ring";
    public static String BIO_SUB_TYPE_RIGHT_LITTLE 	= "mosip.mock.sbi.biometric.subtype.right.little";
    public static String BIO_SUB_TYPE_LEFT_THUMB 	= "mosip.mock.sbi.biometric.subtype.left.thumb";
    public static String BIO_SUB_TYPE_LEFT_INDEX 	= "mosip.mock.sbi.biometric.subtype.left.index";
    public static String BIO_SUB_TYPE_LEFT_MIDDLE 	= "mosip.mock.sbi.biometric.subtype.left.middle";
    public static String BIO_SUB_TYPE_LEFT_RING 	= "mosip.mock.sbi.biometric.subtype.left.ring";
    public static String BIO_SUB_TYPE_LEFT_LITTLE 	= "mosip.mock.sbi.biometric.subtype.left.little";
    
	/** Face File Path */
	public static String MOSIP_BIOMETRIC_TYPE_FILE_JP2000 = "org.mosip.bio.utils.biometric.type.file.jp2000";
	public static String MOSIP_BIOMETRIC_TYPE_FILE_ISO = "org.mosip.bio.utils.biometric.type.file.iso";
}
