package io.mosip.kernel.biosdk.provider.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.LongStream;

import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biosdk.provider.spi.iBioProviderApi;
import io.mosip.kernel.biosdk.provider.util.BioProviderUtil;
import io.mosip.kernel.biosdk.provider.util.BioSDKProviderLoggerFactory;
import io.mosip.kernel.biosdk.provider.util.ProviderConstants;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.core.bioapi.model.CompositeScore;
import io.mosip.kernel.core.bioapi.model.KeyValuePair;
import io.mosip.kernel.core.bioapi.model.QualityScore;
import io.mosip.kernel.core.bioapi.model.Score;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * Implementation of the {@link iBioProviderApi} interface for version 0.7 of
 * the BioSDK provider. This class provides methods to initialize, verify,
 * identify, and extract biometric templates using specific SDK instances for
 * supported modalities like fingerprint, iris, and face.
 * 
 * <p>
 * It supports operations like matching biometric samples against stored
 * records, composite matching for multiple modalities, quality assessment of
 * biometric samples, and extraction of biometric templates. The class utilizes
 * reflection to dynamically invoke methods on the underlying SDK instances.
 * </p>
 * 
 * <p>
 * The class manages a registry of SDK instances for each supported modality and
 * their associated threshold values for verification and identification
 * processes.
 * </p>
 * 
 * <p>
 * SDK initialization is based on provided parameters, which include SDK
 * instance details and configuration for each supported modality.
 * </p>
 * 
 * <p>
 * Error handling is implemented to log exceptions encountered during method
 * invocations on SDK instances.
 * </p>
 * 
 * <p>
 * Note: This class is thread-safe for concurrent invocations.
 * </p>
 * 
 * @see iBioProviderApi
 * @see BiometricType
 * @see BiometricFunction
 * @see BIR
 * @see QualityScore
 * @see CompositeScore
 * @see KeyValuePair
 * @see BiometricException
 * @see Logger
 * @see BioProviderUtil
 * @see BioSDKProviderLoggerFactory
 * @see ProviderConstants
 */

@Component
@SuppressWarnings({ "java:S101" })
public class BioProviderImpl_V_0_7 implements iBioProviderApi {

	private static final Logger LOGGER = BioSDKProviderLoggerFactory.getLogger(BioProviderImpl_V_0_7.class);

	// Constants for method names and API version
	private static final String METHOD_NAME_KEY = "_METHOD_NAME";
	private static final String THRESHOLD_KEY = "_THRESHOLD";
	private static final String API_VERSION = "0.7";

	// Constants for method tags and default values
	private static final String TAG_MATCH = "match";
	private static final String TAG_MATCH_COMPOSITE = "compositeMatch";

	// Registry to store SDK instances for each modality
	private Map<BiometricType, Object> sdkRegistry = new EnumMap<>(BiometricType.class);
	// Thresholds for each modality
	private Map<BiometricType, String> thresholds = new EnumMap<>(BiometricType.class);

	/**
	 * Initializes the SDK instances for supported modalities based on provided
	 * parameters.
	 * 
	 * @param params Configuration parameters for each supported biometric modality.
	 * @return Map of supported modalities and their corresponding functions.
	 * @throws BiometricException If there are issues initializing the SDK
	 *                            instances.
	 */
	@Override
	public Map<BiometricType, List<BiometricFunction>> init(Map<BiometricType, Map<String, String>> params)
			throws BiometricException {
		for (Entry<BiometricType, Map<String, String>> entryInfo : params.entrySet()) {
			BiometricType modality = entryInfo.getKey();
			Map<String, String> modalityParams = params.get(modality);

			// check if version matches supported API version of this provider
			if (modalityParams != null && !modalityParams.isEmpty()
					&& API_VERSION.equals(modalityParams.get(ProviderConstants.VERSION))) {
				Object instance = BioProviderUtil.getSDKInstance(modalityParams);
				addToRegistry(instance, modality);
				thresholds.put(modality, modalityParams.getOrDefault(ProviderConstants.THRESHOLD, "60"));
			}
		}
		return getSupportedModalities();
	}

	/**
	 * Verifies if a given sample matches a biometric record for a specific
	 * modality.
	 * 
	 * @param sample    Biometric sample to be verified.
	 * @param bioRecord Biometric record against which the sample is verified.
	 * @param modality  Biometric modality (e.g., fingerprint, iris).
	 * @param flags     Additional flags to control verification.
	 * @return True if the sample matches the biometric record; false otherwise.
	 */
	@Override
	public boolean verify(List<BIR> sample, List<BIR> bioRecord, BiometricType modality, Map<String, String> flags) {
		LOGGER.info(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE, ProviderConstants.LOGGER_EMPTY,
				MessageFormat.format("verify invoked modality {0} ", modality));

		if (Objects.isNull(flags)) {
			flags = new HashMap<>();
		}

		String methodName = flags.getOrDefault(METHOD_NAME_KEY, TAG_MATCH);
		String threshold = flags.getOrDefault(THRESHOLD_KEY, thresholds.getOrDefault(modality, "60"));

		sample = sample.stream()
				.filter(obj -> modality == BiometricType.fromValue(obj.getBdbInfo().getType().get(0).value())).toList();

		bioRecord = bioRecord.stream()
				.filter(obj -> modality == BiometricType.fromValue(obj.getBdbInfo().getType().get(0).value())).toList();

		switch (methodName) {
		case TAG_MATCH:
			return getSDKMatchResult(sample, bioRecord.toArray(new BIR[bioRecord.size()]), modality, flags, threshold);

		case TAG_MATCH_COMPOSITE:
			return getSDKCompositeMatchResult(sample, bioRecord.toArray(new BIR[bioRecord.size()]), modality, flags,
					threshold);
		default:
			return false;
		}
	}

	/**
	 * Identifies a sample against a gallery of biometric records for a specific
	 * modality.
	 * 
	 * @param sample   Biometric sample to be identified.
	 * @param gallery  Gallery of biometric records against which the sample is
	 *                 identified.
	 * @param modality Biometric modality (e.g., fingerprint, iris).
	 * @param flags    Additional flags to control identification.
	 * @return Map of record identifiers and their corresponding match results.
	 */
	@Override
	public Map<String, Boolean> identify(List<BIR> sample, Map<String, List<BIR>> gallery, BiometricType modality,
			Map<String, String> flags) {
		LOGGER.info(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE, ProviderConstants.LOGGER_EMPTY,
				MessageFormat.format("identify invoked modality {0} ", modality));

		if (Objects.isNull(flags)) {
			flags = new HashMap<>();
		}

		String methodName = flags.getOrDefault(METHOD_NAME_KEY, TAG_MATCH_COMPOSITE);
		String threshold = flags.getOrDefault(THRESHOLD_KEY, thresholds.getOrDefault(modality, "60"));

		sample = sample.stream()
				.filter(obj -> modality == BiometricType.fromValue(obj.getBdbInfo().getType().get(0).value())).toList();

		Map<String, Boolean> result = new HashMap<>();
		for (Entry<String, List<BIR>> entry : gallery.entrySet()) {

			if (Objects.nonNull(entry.getValue())) {
				List<BIR> bioRecord = entry.getValue().stream()
						.filter(obj -> modality == BiometricType.fromValue(obj.getBdbInfo().getType().get(0).value()))
						.toList();

				switch (methodName) {
				case TAG_MATCH:
					result.put(entry.getKey(), getSDKMatchResult(sample, bioRecord.toArray(new BIR[bioRecord.size()]),
							modality, flags, threshold));
					break;

				case TAG_MATCH_COMPOSITE:
					result.put(entry.getKey(), getSDKCompositeMatchResult(sample,
							bioRecord.toArray(new BIR[bioRecord.size()]), modality, flags, threshold));
					break;
				default:
					result = new HashMap<>();
				}
			}
		}
		return result;
	}

	/**
	 * Calculates quality scores for each segment of biometric samples.
	 * 
	 * @param sample Biometric samples for which quality scores are calculated.
	 * @param flags  Additional flags to control quality assessment.
	 * @return Array of quality scores for each segment.
	 */
	@Override
	@SuppressWarnings({ "java:S3011" })
	public float[] getSegmentQuality(BIR[] sample, Map<String, String> flags) {
		float[] scores = new float[sample.length];
		for (int i = 0; i < sample.length; i++) {
			BiometricType modality = BiometricType.fromValue(sample[i].getBdbInfo().getType().get(0).value());
			Method method = BioProviderUtil.findRequiredMethod(this.sdkRegistry.get(modality).getClass(),
					"checkQuality", BIR.class, KeyValuePair[].class);
			method.setAccessible(true);

			try {
				Object response = method.invoke(this.sdkRegistry.get(modality), sample[i], getKeyValuePairs(flags));
				if (Objects.nonNull(response)) {
					QualityScore qualityScore = (QualityScore) response;
					scores[i] = qualityScore.getInternalScore();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.error(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
						ProviderConstants.LOGGER_EMPTY, "getSegmentQuality invoked", e);
			}
		}

		return scores;
	}

	/**
	 * Calculates quality scores for each modality based on the provided biometric
	 * samples.
	 * 
	 * @param sample Biometric samples for which quality scores are calculated.
	 * @param flags  Additional flags to control quality assessment.
	 * @return Map of biometric modalities and their corresponding average quality
	 *         scores.
	 */
	@Override
	@SuppressWarnings({ "java:S3011" })
	public Map<BiometricType, Float> getModalityQuality(BIR[] sample, Map<String, String> flags) {
		Map<BiometricType, LongStream.Builder> result = new EnumMap<>(BiometricType.class);
		for (BIR bir : sample) {
			BiometricType modality = BiometricType.fromValue(bir.getBdbInfo().getType().get(0).value());
			Method method = BioProviderUtil.findRequiredMethod(this.sdkRegistry.get(modality).getClass(),
					"checkQuality", BIR.class, KeyValuePair[].class);
			method.setAccessible(true);

			try {
				Object response = method.invoke(this.sdkRegistry.get(modality), bir, getKeyValuePairs(flags));
				if (Objects.nonNull(response)) {
					QualityScore qualityScore = (QualityScore) response;
					result.computeIfAbsent(modality, k -> LongStream.builder()).add(qualityScore.getInternalScore());
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.error(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
						ProviderConstants.LOGGER_EMPTY, "getModalityQuality invoked", e);
			}
		}

		Map<BiometricType, Float> finalResult = new EnumMap<>(BiometricType.class);
		result.forEach((k, v) -> {
			OptionalDouble avg = v.build().average();
			if (avg.isPresent())
				finalResult.put(k, (float) avg.getAsDouble());
		});
		return finalResult;
	}

	/**
	 * Extracts biometric templates from the provided samples for a specific
	 * modality.
	 * 
	 * @param sample Biometric samples from which templates are to be extracted.
	 * @param flags  Additional flags to control template extraction.
	 * @return List of extracted biometric templates.
	 */
	@Override
	@SuppressWarnings({ "java:S3011" })
	public List<BIR> extractTemplate(List<BIR> sample, Map<String, String> flags) {
		List<BIR> extracts = new ArrayList<>();
		for (BIR bir : sample) {
			BiometricType modality = BiometricType.fromValue(bir.getBdbInfo().getType().get(0).value());
			Method method = BioProviderUtil.findRequiredMethod(this.sdkRegistry.get(modality).getClass(),
					"extractTemplate", BIR.class, KeyValuePair[].class);
			if (Objects.nonNull(method)) {
				method.setAccessible(true);

				try {
					Object response = method.invoke(this.sdkRegistry.get(modality), bir, getKeyValuePairs(flags));
					extracts.add(Objects.nonNull(response) ? (BIR) response : null);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.error(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
							ProviderConstants.LOGGER_EMPTY, "extractTemplate invoked", e);
				}
			}
		}
		return extracts;
	}

	/**
	 * Performs a standard match operation using the SDK instance for the specified
	 * biometric modality. Computes match scores and determines if the match meets
	 * the specified threshold.
	 *
	 * @param sample    The sample biometric records to be verified.
	 * @param bioRecord The bio record against which the sample is verified.
	 * @param modality  The type of biometric modality being verified.
	 * @param flags     Additional flags for verification.
	 * @param threshold The threshold score above which the match is considered
	 *                  successful.
	 * @return true if the verification is successful and meets the threshold, false
	 *         otherwise.
	 */
	// Score[] match(BIR sample, BIR[] gallery, KeyValuePair[] flags)
	@SuppressWarnings({ "java:S3011" })
	private boolean getSDKMatchResult(List<BIR> sample, BIR[] bioRecord, BiometricType modality,
			Map<String, String> flags, String threshold) {
		Method method = BioProviderUtil.findRequiredMethod(this.sdkRegistry.get(modality).getClass(), TAG_MATCH,
				BIR.class, BIR[].class, KeyValuePair[].class);

		boolean isMatched = false;
		if (Objects.nonNull(method)) {
			LOGGER.debug(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
					ProviderConstants.LOGGER_EMPTY, "verify invoked Match method found");

			method.setAccessible(true);
			LongStream.Builder scaleScores = LongStream.builder();
			for (int i = 0; i < sample.size(); i++) {
				try {
					Object[] response = (Object[]) method.invoke(this.sdkRegistry.get(modality), sample.get(i),
							bioRecord, getKeyValuePairs(flags));

					if (Objects.nonNull(response)) {
						Score[] scores = Arrays.copyOf(response, response.length, Score[].class);
						Optional<Score> result = Arrays.stream(scores)
								.max((s1, s2) -> (int) (s1.getScaleScore() - s2.getScaleScore()));
						scaleScores.add(result.isPresent() ? (long) result.get().getScaleScore() : 0L);
					}

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.error(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
							"getSDKMatchResult invoked", ExceptionUtils.getStackTrace(e));
				}
			}
			OptionalDouble result = scaleScores.build().average();
			isMatched = (result.isPresent() && result.getAsDouble() >= Float.valueOf(threshold));
		}
		return isMatched;
	}

	/**
	 * Performs a composite match operation using the SDK instance for the specified
	 * biometric modality. Computes composite match scores and determines if the
	 * match meets the specified threshold.
	 *
	 * @param sample    The sample biometric records to be identified.
	 * @param bioRecord The bio record against which the sample is verified.
	 * @param modality  The type of biometric modality being verified.
	 * @param flags     Additional flags for identification.
	 * @param threshold The threshold score above which the match is considered
	 *                  successful.
	 * @return true if the identification is successful and meets the threshold,
	 *         false otherwise.
	 */
	// CompositeScore compositeMatch(BIR[] sampleList, BIR[] recordList,
	// KeyValuePair[] flags)
	@SuppressWarnings({ "java:S3011" })
	private boolean getSDKCompositeMatchResult(List<BIR> sample, BIR[] bioRecord, BiometricType modality,
			Map<String, String> flags, String threshold) {
		Method method = BioProviderUtil.findRequiredMethod(this.sdkRegistry.get(modality).getClass(),
				TAG_MATCH_COMPOSITE, BIR[].class, BIR[].class, KeyValuePair[].class);

		boolean isMatched = false;
		if (Objects.nonNull(method)) {

			LOGGER.debug(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
					ProviderConstants.LOGGER_EMPTY, "verify invoked", "CompositeMatch method found");

			method.setAccessible(true);
			try {
				Object response = method.invoke(this.sdkRegistry.get(modality), sample.toArray(new BIR[sample.size()]),
						bioRecord, getKeyValuePairs(flags));

				if (Objects.nonNull(response)) {
					CompositeScore compositeScore = (CompositeScore) response;
					if (compositeScore.getScaledScore() >= Float.valueOf(threshold))
						isMatched = true;
				}

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.error(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
						ProviderConstants.LOGGER_EMPTY, "getSDKCompositeMatchResult invoked", e);
			}
		}
		return isMatched;
	}

	/**
	 * Retrieves the supported biometric modalities and their associated functions.
	 * 
	 * @return Mapping of supported biometric modalities to their associated
	 *         functions.
	 */
	private Map<BiometricType, List<BiometricFunction>> getSupportedModalities() {
		Map<BiometricType, List<BiometricFunction>> result = new EnumMap<>(BiometricType.class);
		sdkRegistry.forEach((modality, map) -> result.put(modality, Arrays.asList(BiometricFunction.values())));
		return result;
	}

	/**
	 * Adds a SDK instance to the registry for a specific biometric modality.
	 * 
	 * @param sdkInstance The SDK instance to be added.
	 * @param modality    The biometric modality associated with the SDK instance.
	 */
	private void addToRegistry(Object sdkInstance, BiometricType modality) {
		sdkRegistry.put(modality, sdkInstance);
	}

	/**
	 * Retrieves key-value pairs from the provided flags map and converts them into
	 * KeyValuePair array.
	 * 
	 * @param flags Additional flags to be converted into KeyValuePair array.
	 * @return Array of KeyValuePair extracted from flags.
	 */
	@SuppressWarnings({ "java:S1168" })
	private KeyValuePair[] getKeyValuePairs(Map<String, String> flags) {
		if (Objects.isNull(flags))
			return null;

		flags.remove(METHOD_NAME_KEY);
		flags.remove(THRESHOLD_KEY);

		int i = 0;
		KeyValuePair[] kvp = new KeyValuePair[flags.size()];
		for (Map.Entry<String, String> entry : flags.entrySet()) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setKey(entry.getKey());
			keyValuePair.setValue(entry.getValue());
			kvp[i++] = keyValuePair;
		}
		return kvp;
	}
}