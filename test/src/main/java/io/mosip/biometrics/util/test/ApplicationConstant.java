package io.mosip.biometrics.util.test;

public class ApplicationConstant {
	private ApplicationConstant() {
		throw new IllegalStateException("ApplicationConstant class");
	}

	/** Image File Type */
	public static final String IMAGE_TYPE_JP2000 = "io.mosip.biometrics.util.image.type.jp2000";
	public static final String IMAGE_TYPE_WSQ = "io.mosip.biometrics.util.image.type.wsq";
	public static final String IMAGE_TYPE_JPEG = "io.mosip.biometrics.util.image.type.jpeg";
	public static final String IMAGE_TYPE_PNG = "io.mosip.biometrics.util.image.type.png";

	/** File Conversion Type */
	public static final String MOSIP_CONVERT_ISO_TO_IMAGE = "io.mosip.biometrics.util.convert.image.to.iso";
	public static final String MOSIP_CONVERT_IMAGE_TO_ISO = "io.mosip.biometrics.util.convert.iso.to.image";

	/** Biometric Types Folder Path */
	public static final String MOSIP_BIOMETRIC_TYPE_FINGER = "mosip.mock.sbi.biometric.type.finger.folder.path";
	public static final String MOSIP_BIOMETRIC_TYPE_FACE = "mosip.mock.sbi.biometric.type.face.folder.path";
	public static final String MOSIP_BIOMETRIC_TYPE_IRIS = "mosip.mock.sbi.biometric.type.iris.folder.path";
	public static final String MOSIP_BIOMETRIC_TYPE_NIST = "mosip.mock.sbi.biometric.type.nist.folder.path";

	/** Biometric Sub Types Name values */
	public static final String BIO_SUB_TYPE_UNKNOWN = "mosip.mock.sbi.biometric.subtype.unknown"; // Do not known left
																									// or right iris
	public static final String BIO_SUB_TYPE_LEFT = "mosip.mock.sbi.biometric.subtype.left"; // LEFT IRIS IMAGE
	public static final String BIO_SUB_TYPE_RIGHT = "mosip.mock.sbi.biometric.subtype.right"; // RIGHT IRIS IMAGE
	public static final String BIO_SUB_TYPE_RIGHT_THUMB = "mosip.mock.sbi.biometric.subtype.right.thumb";
	public static final String BIO_SUB_TYPE_RIGHT_INDEX = "mosip.mock.sbi.biometric.subtype.right.index";
	public static final String BIO_SUB_TYPE_RIGHT_MIDDLE = "mosip.mock.sbi.biometric.subtype.right.middle";
	public static final String BIO_SUB_TYPE_RIGHT_RING = "mosip.mock.sbi.biometric.subtype.right.ring";
	public static final String BIO_SUB_TYPE_RIGHT_LITTLE = "mosip.mock.sbi.biometric.subtype.right.little";
	public static final String BIO_SUB_TYPE_LEFT_THUMB = "mosip.mock.sbi.biometric.subtype.left.thumb";
	public static final String BIO_SUB_TYPE_LEFT_INDEX = "mosip.mock.sbi.biometric.subtype.left.index";
	public static final String BIO_SUB_TYPE_LEFT_MIDDLE = "mosip.mock.sbi.biometric.subtype.left.middle";
	public static final String BIO_SUB_TYPE_LEFT_RING = "mosip.mock.sbi.biometric.subtype.left.ring";
	public static final String BIO_SUB_TYPE_LEFT_LITTLE = "mosip.mock.sbi.biometric.subtype.left.little";

	/** Face File Path */
	public static final String MOSIP_BIOMETRIC_TYPE_FILE_IMAGE = "mosip.mock.sbi.biometric.type.file.image";
	public static final String MOSIP_BIOMETRIC_TYPE_FILE_ISO = "mosip.mock.sbi.biometric.type.file.iso";

	/** Purpose */
	public static final String MOSIP_PURPOSE_AUTH = "io.mosip.biometrics.util.purpose.auth";
	public static final String MOSIP_PURPOSE_REGISTRATION = "io.mosip.biometrics.util.purpose.registration";

	/** Rotation */
	public static final String MOSIP_IMAGE_ROTATION = "io.mosip.biometrics.image.rotation";

	/** Auth Decoder Information */
	public static final String MOSIP_TRANSACTION_ID = "mosip.mock.sbi.biometric.transaction.id";
	public static final String MOSIP_TIME_STAMP = "mosip.mock.sbi.biometric.time.stamp";
	public static final String MOSIP_THUMB_PRINT = "mosip.mock.sbi.biometric.thumb.print";
	public static final String MOSIP_SESSION_KEY = "mosip.mock.sbi.biometric.session.key";
	public static final String MOSIP_KEY_SPLITTER = "#KEY_SPLITTER#";

	/** BQAT Details */
	public static final String BQAT_SERVER_IPADDRESS = "bqat.server.ipaddress";
	public static final String BQAT_SERVER_PORT = "bqat.server.port";
	public static final String BQAT_SERVER_PATH = "bqat.server.path";
	public static final String BQAT_CONTENT_TYPE = "bqat.content.type";
	public static final String BQAT_CONTENT_CHARSET = "bqat.content.charset";
	public static final String BQAT_JSONKEY_FINGER_QUALITY_SCORE = "bqat.jsonkey.finger.quality.score";
	public static final String BQAT_JSONKEY_IRIS_QUALITY_SCORE = "bqat.jsonkey.iris.quality.score";
	public static final String BQAT_JSONKEY_FACE_QUALITY_SCORE = "bqat.jsonkey.face.quality.score";
	public static final String BQAT_JSON_RESULTS = "bqat.json.results";
}