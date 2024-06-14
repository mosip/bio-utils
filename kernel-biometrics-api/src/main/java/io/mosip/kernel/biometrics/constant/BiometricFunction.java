package io.mosip.kernel.biometrics.constant;

import java.io.Serializable;

/**
 * Enumerates the different functions supported by a biometric system.
 *
 * This enum defines the various functionalities that a biometric system can
 * perform on biometric data. It covers common processing steps like quality
 * check, segmentation, feature extraction, format conversion, and matching.
 *
 * @author (Your Name/Team)
 */
public enum BiometricFunction implements Serializable {
	/**
	 * QUALITY_CHECK: Assesses the quality of a captured biometric sample to ensure
	 * it meets predefined standards.
	 */
	QUALITY_CHECK,

	/**
	 * SEGMENT: Divides a biometric sample into smaller, more manageable units for
	 * further processing (e.g., fingerprint minutiae).
	 */
	SEGMENT,

	/**
	 * EXTRACT: Extracts relevant features or characteristics from a biometric
	 * sample for comparison or storage.
	 */
	EXTRACT,

	/**
	 * CONVERT_FORMAT: Converts a biometric sample from one format to another for
	 * compatibility or storage purposes.
	 */
	CONVERT_FORMAT,

	/**
	 * MATCH: Compares a biometric sample against a reference template to determine
	 * similarity or identity.
	 */
	MATCH;
}