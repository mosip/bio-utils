package io.mosip.kernel.biometrics.constant;

/**
 * Utility class containing constants used as keys for additional information
 * stored in biometric data.
 *
 * This class provides static final string constants that serve as keys to
 * access additional information associated with biometric data. These keys are
 * typically used within a map or other data structure to store details beyond
 * the core biometric sample itself.
 *
 */
public class OtherKey {
	private OtherKey() {
		super();
	}

	/**
	 * Key for indicating whether an exception occurred during biometric processing
	 * (value: "true" or "false").
	 */
	public static final String EXCEPTION = "EXCEPTION";

	/**
	 * Key for specifying the number of retries allowed for biometric capture
	 * (value: "3" as a string).
	 */
	public static final String RETRIES = "RETRIES";

	/**
	 * Key for indicating whether forced capture mode is enabled (value: "true" or
	 * "false").
	 */
	public static final String FORCE_CAPTURED = "FORCE_CAPTURED";

	/**
	 * Key for storing the SDK score associated with the biometric sample (value:
	 * "80" as a string, range: 1-100).
	 */
	public static final String SDK_SCORE = "SDK_SCORE";

	/**
	 * Key for indicating configured biometric attributes (value: string
	 * representing a list of configured bio-attributes).
	 */
	public static final String CONFIGURED = "CONFIGURED";

	/**
	 * Key for storing a payload string with a placeholder for the actual biometric
	 * value.
	 */
	public static final String PAYLOAD = "PAYLOAD";

	/**
	 * Key for storing the biometric data specification version (value: string).
	 */
	public static final String SPEC_VERSION = "SPEC_VERSION";
}