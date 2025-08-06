package io.mosip.kernel.biometrics.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;
import lombok.Data;

/**
 * Represents the decision of biometric matching for various modalities.
 * <p>
 * This class stores:
 * <ul>
 *   <li>The gallery index position of the matched record within the input gallery.</li>
 *   <li>Match decisions per biometric modality, mapped by {@link BiometricType}.</li>
 *   <li>Optional analytics information providing a detailed breakdown of the matching process.</li>
 * </ul>
 *
 * <p>
 * The {@code decisions} map uses {@link BiometricType} as keys, storing match results
 * for each biometric modality (e.g., fingerprint, iris, face).
 * {@code analyticsInfo} holds additional diagnostic information such as
 * similarity scores, thresholds used, or algorithm-specific notes.
 * </p>
 *
 * <p>
 * This class uses Lombok's {@link Data} annotation to automatically generate
 * getters, setters, {@code equals()}, {@code hashCode()}, and {@code toString()} methods.
 * </p>
 *
 * @author
 * @version 1.1
 * @since 1.0
 */
@Data
public class MatchDecision {

	/**
	 * Refers to the position of the match within the input gallery.
	 */
	private int galleryIndex;

	/**
	 * Map containing match decisions per biometric modality.
	 * Keys are instances of {@link BiometricType}, representing different biometric modalities.
	 * Values are {@link Decision} objects representing match decisions or results.
	 */
	private Map<BiometricType, Decision> decisions;

	/**
	 * Additional analytics information providing a detailed breakdown and context
	 * about the matching process. Useful for debugging and reporting purposes.
	 */
	private Map<String, String> analyticsInfo;

	/**
	 * Default constructor that initializes the {@code decisions} and {@code analyticsInfo} maps as empty.
	 * <p>
	 * This constructor is typically used when creating an empty {@link MatchDecision}
	 * object to be populated later with matching results.
	 * </p>
	 */
	public MatchDecision() {
		this.decisions = new HashMap<>();
		this.analyticsInfo = new HashMap<>();
	}

	/**
	 * Constructs a new {@link MatchDecision} object with a specified gallery index.
	 * Initializes the {@code decisions} map as an {@link EnumMap} for optimal storage.
	 *
	 * @param galleryIndex The position of the match within the input gallery.
	 */
	public MatchDecision(int galleryIndex) {
		this.analyticsInfo = new HashMap<>();
		this.decisions = new EnumMap<>(BiometricType.class);
		this.galleryIndex = galleryIndex;
	}
}