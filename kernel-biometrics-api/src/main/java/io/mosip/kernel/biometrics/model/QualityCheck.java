package io.mosip.kernel.biometrics.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;
import lombok.Data;

/**
 * Represents a quality assessment result for various biometric modalities,
 * including scores and analytics information.
 * 
 * <p>
 * The scores map stores quality scores for each supported biometric type, using
 * {@link BiometricType} as keys. Analytics information provides a detailed
 * breakdown and additional context about the quality assessment.
 * </p>
 * 
 * @since 1.0.0
 */

@Data
public class QualityCheck {
	/**
	 * Map containing quality scores per biometric modality. Keys are instances of
	 * {@link BiometricType}, representing different biometric modalities.
	 */
	private Map<BiometricType, QualityScore> scores;

	/**
	 * Additional analytics information providing detailed breakdown and context
	 * about the quality assessment.
	 */
	private Map<String, String> analyticsInfo;

	/**
	 * Instantiates a new QualityCheck object with initial values.
	 */
	public QualityCheck() {
		this.scores = new EnumMap<>(BiometricType.class);
		this.analyticsInfo = new HashMap<>();
	}
}