package io.mosip.kernel.biosdk.provider.util;

/**
 * Constants class containing common string constants used throughout the biometric SDK provider module.
 * This class cannot be instantiated as it only contains static fields and a private constructor.
 */
public class ProviderConstants {
	/**
     * Private constructor to prevent instantiation of this utility class.
     * Throws {@link IllegalStateException} if attempted to instantiate.
     */
	private ProviderConstants() {
		throw new IllegalStateException("ProviderConstants class");
	}

	/** The constant key for class name. */
	public static final String CLASSNAME = "classname";
	
	 /** The constant key for version. */
	public static final String VERSION = "version";
	
	 /** The constant key for arguments. */
	public static final String ARGUMENTS = "args";
	
	/** The constant key for threshold. */
	public static final String THRESHOLD = "threshold";

	/** The constant key for logger session ID. */
	public static final String LOGGER_SESSIONID = "BIO-SDK-PROVIDER";
	
	 /** The constant value for logger ID type, typically used for registration or authentication. */
	public static final String LOGGER_IDTYPE = "REGISTRATION / AUTH";
	
	/** The constant value representing an empty string, used in logger messages. */
	public static final String LOGGER_EMPTY = "";
}