package io.mosip.biometrics.util.test;

public class ApplicationConstant {
	/** Image File Type */
	public static String IMAGE_TYPE_JP2000 = "io.mosip.biometrics.util.image.type.jp2000";
	public static String IMAGE_TYPE_WSQ = "io.mosip.biometrics.util.image.type.wsq";

	/** File Conversion Type */
	public static String MOSIP_CONVERT_ISO_TO_IMAGE = "io.mosip.biometrics.util.convert.image.to.iso";
	public static String MOSIP_CONVERT_IMAGE_TO_ISO = "io.mosip.biometrics.util.convert.iso.to.image";

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
	public static String MOSIP_BIOMETRIC_TYPE_FILE_IMAGE = "mosip.mock.sbi.biometric.type.file.image";
	public static String MOSIP_BIOMETRIC_TYPE_FILE_ISO = "mosip.mock.sbi.biometric.type.file.iso";

	/** Purpose */
	public static String MOSIP_PURPOSE_AUTH = "io.mosip.biometrics.util.purpose.auth";
	public static String MOSIP_PURPOSE_REGISTRATION = "io.mosip.biometrics.util.purpose.registration";
}
