package io.mosip.kernel.biometrics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.Match;
import lombok.Data;

/**
 * Represents the decision of a biometric match operation, including match
 * result, errors, and analytics information.
 * 
 * <p>
 * The match field indicates the result of the biometric match operation,
 * represented by {@link Match}. Errors list contains any errors or issues
 * encountered during the match operation. AnalyticsInfo provides additional
 * context and detailed breakdown related to the match process.
 * </p>
 * 
 */
@Data
public class Decision {
	/**
	 * The result of the biometric match operation, represented by {@link Match}.
	 */
	private Match match;

	/**
	 * List containing any errors or issues encountered during the match operation.
	 */
	private List<String> errors;

	/**
	 * Additional analytics information providing detailed breakdown and context
	 * about the match process.
	 */
	private Map<String, String> analyticsInfo;

	/**
	 * Instantiates a new Decision object with initial values.
	 */
	public Decision() {
		this.errors = new ArrayList<>();
		this.analyticsInfo = new HashMap<>();
	}
}