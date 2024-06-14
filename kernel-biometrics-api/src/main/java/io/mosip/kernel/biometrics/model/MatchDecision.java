package io.mosip.kernel.biometrics.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;
import lombok.Data;

/**
 * Represents the decision of biometric matching for various modalities,
 * including match results and analytics information.
 * 
 * <p>
 * The galleryIndex indicates the position of the match within the input
 * gallery. Decisions map stores match decisions for each supported biometric
 * type, using {@link BiometricType} as keys. AnalyticsInfo provides a detailed
 * breakdown and additional context about the matching process.
 * </p>
 * 
 */
@Data
public class MatchDecision {
	/**
	 * Refers to the position of the match within the input gallery.
	 */
	private int galleryIndex;

	/**
	 * Map containing match decisions per biometric modality. Keys are instances of
	 * {@link BiometricType}, representing different biometric modalities.
	 */
	private Map<BiometricType, Decision> decisions;

	/**
	 * Additional analytics information providing detailed breakdown and context
	 * about the matching process.
	 */
	private Map<String, String> analyticsInfo;

	/**
	 * Instantiates a new MatchDecision object with initial values.
	 * 
	 * @param galleryIndex The position of the match within the input gallery.
	 */
	public MatchDecision(int galleryIndex) {
		this.analyticsInfo = new HashMap<>();
		this.decisions = new EnumMap<>(BiometricType.class);
		this.galleryIndex = galleryIndex;
	}
}