package io.mosip.kernel.biometrics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Represents a quality score for biometric data, including score value, errors,
 * and analytics information.
 * 
 * <p>
 * The score is represented as a float between 0 and 100, indicating quality as
 * a percentage. Errors are a list of strings describing issues or problems with
 * the biometric data. Analytics information provides a detailed breakdown and
 * additional context about the quality assessment.
 * </p>
 * 
 * @author Sanjay Murali
 * @since 1.0.0
 */

@Data
public class QualityScore {
	/**
	 * The score - a value between 0 and 100 representing quality as a percentage.
	 */
	private float score;

	/**
	 * List of errors describing issues or problems with the biometric data quality
	 * assessment.
	 */
	private List<String> errors;

	/**
	 * Additional analytics information providing detailed breakdown and context
	 * about the quality assessment.
	 */
	private Map<String, String> analyticsInfo;

	/**
	 * Instantiates a new QualityScore with initial values.
	 */
	public QualityScore() {
		this.score = 0;
		this.errors = new ArrayList<>();
		this.analyticsInfo = new HashMap<>();
	}
}