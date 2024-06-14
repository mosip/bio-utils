package io.mosip.kernel.biometrics.constant;

import java.io.Serializable;

/**
 * Enumerates the possible outcomes of a biometric matching operation.
 *
 * This enum defines the three possible results of comparing a biometric sample
 * against a reference template: - MATCHED: The biometric sample matches the
 * reference template within a predefined threshold. - NOT_MATCHED: The
 * biometric sample does not match the reference template within the threshold.
 * - ERROR: An error occurred during the biometric matching process.
 *
 */
public enum Match implements Serializable {
	/**
	 * Indicates a successful biometric match.
	 */
	MATCHED,

	/**
	 * Indicates a failed biometric match.
	 */
	NOT_MATCHED,

	/**
	 * Indicates an error occurred during biometric matching.
	 */
	ERROR;
}