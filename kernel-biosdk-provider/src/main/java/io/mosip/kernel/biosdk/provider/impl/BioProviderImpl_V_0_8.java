package io.mosip.kernel.biosdk.provider.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biosdk.provider.spi.iBioProviderApi;
import io.mosip.kernel.biosdk.provider.util.BIRConverter;
import io.mosip.kernel.biosdk.provider.util.BioProviderUtil;
import io.mosip.kernel.biosdk.provider.util.BioSDKProviderLoggerFactory;
import io.mosip.kernel.biosdk.provider.util.ProviderConstants;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.core.bioapi.model.CompositeScore;
import io.mosip.kernel.core.bioapi.model.KeyValuePair;
import io.mosip.kernel.core.bioapi.model.MatchDecision;
import io.mosip.kernel.core.bioapi.model.QualityScore;
import io.mosip.kernel.core.bioapi.model.Response;
import io.mosip.kernel.core.bioapi.spi.IBioApi;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * Implementation of the {@link iBioProviderApi} interface for version 0.8 of
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
public class BioProviderImpl_V_0_8 implements iBioProviderApi {

	private static final String API_VERSION = "0.8";
	private Map<BiometricType, Map<BiometricFunction, IBioApi>> sdkRegistry = new EnumMap<>(BiometricType.class);

	/**
	 * Initializes the biometric provider with SDK instances based on provided
	 * parameters.
	 * 
	 * @param params Configuration parameters mapping biometric types to their
	 *               respective SDK parameters.
	 * @return A map of supported biometric types and their associated functions.
	 * @throws BiometricException If initialization fails due to invalid parameters
	 *                            or SDK instantiation errors.
	 */
	@Override
	public Map<BiometricType, List<BiometricFunction>> init(Map<BiometricType, Map<String, String>> params)
			throws BiometricException {
		for (Map.Entry<BiometricType, Map<String, String>> entry : params.entrySet()) {
			BiometricType modality = entry.getKey();
			Map<String, String> modalityParams = params.get(modality);

			// check if version matches supported API version of this provider
			if (modalityParams != null && !modalityParams.isEmpty()
					&& API_VERSION.equals(modalityParams.get(ProviderConstants.VERSION))) {
				IBioApi iBioApi = (IBioApi) BioProviderUtil.getSDKInstance(modalityParams);
				addToRegistry(iBioApi, modality);
			}
		}
		return getSupportedModalities();
	}

	/**
	 * Verifies if a given sample matches a provided biometric record for a
	 * specified modality.
	 * 
	 * @param sample    The sample biometric records to be verified.
	 * @param bioRecord The biometric record against which the sample is verified.
	 * @param modality  The biometric modality being verified.
	 * @param flags     Additional flags for verification.
	 * @return true if verification is successful, false otherwise.
	 */
	@Override
	public boolean verify(List<BIR> sample, List<BIR> bioRecord, BiometricType modality, Map<String, String> flags) {
		sample = sample.stream()
				.filter(obj -> modality == BiometricType.fromValue(obj.getBdbInfo().getType().get(0).value())).toList();
		return match("AUTH", sample, bioRecord.toArray(new BIR[bioRecord.size()]), modality, flags);
	}

	/**
	 * Identifies the presence of sample biometric records in a gallery for a
	 * specified modality.
	 * 
	 * @param sample   The sample biometric records to be identified.
	 * @param gallery  The biometric gallery containing records to be searched.
	 * @param modality The biometric modality being identified.
	 * @param flags    Additional flags for identification.
	 * @return A map indicating the presence of each sample in the gallery.
	 */
	@Override
	public Map<String, Boolean> identify(List<BIR> sample, Map<String, List<BIR>> gallery, BiometricType modality,
			Map<String, String> flags) {
		Map<String, Boolean> result = new HashMap<>();

		sample = sample.stream()
				.filter(obj -> modality == BiometricType.fromValue(obj.getBdbInfo().getType().get(0).value())).toList();

		for (Map.Entry<String, List<BIR>> entryInfo : gallery.entrySet()) {
			String key = entryInfo.getKey();
			result.put(key, match("DEDUPE", sample, gallery.get(key).toArray(new BIR[gallery.get(key).size()]),
					modality, flags));
		}
		return result;
	}

	/**
	 * Retrieves quality scores for segments of biometric samples.
	 * 
	 * @param sample The biometric samples for which quality scores are retrieved.
	 * @param flags  Additional flags for quality check.
	 * @return An array of quality scores corresponding to each segment of the
	 *         sample.
	 */
	@Override
	public float[] getSegmentQuality(BIR[] sample, Map<String, String> flags) {
		float[] score = new float[sample.length];
		for (int i = 0; i < sample.length; i++) {
			Response<QualityScore> response = sdkRegistry
					.get(BiometricType.fromValue(sample[i].getBdbInfo().getType().get(0).value()))
					.get(BiometricFunction.QUALITY_CHECK)
					.checkQuality(BIRConverter.convertToBIR(sample[i]), getKeyValuePairs(flags));

			score[i] = isSuccessResponse(response) ? response.getResponse().getScore() : 0;
		}
		return score;
	}

	/**
	 * Retrieves average quality scores for different biometric modalities based on
	 * sample data.
	 * 
	 * @param sample The biometric samples for which modality quality scores are
	 *               retrieved.
	 * @param flags  Additional flags for quality check.
	 * @return A map of biometric modalities and their corresponding average quality
	 *         scores.
	 */
	@Override
	public Map<BiometricType, Float> getModalityQuality(BIR[] sample, Map<String, String> flags) {
		Map<BiometricType, List<Float>> scoresByModality = new EnumMap<>(BiometricType.class);
		for (int i = 0; i < sample.length; i++) {
			BiometricType modality = BiometricType.fromValue(sample[i].getBdbInfo().getType().get(0).value());
			Response<QualityScore> response = sdkRegistry.get(modality).get(BiometricFunction.QUALITY_CHECK)
					.checkQuality(BIRConverter.convertToBIR(sample[i]), getKeyValuePairs(flags));

			scoresByModality.computeIfAbsent(modality, k -> new ArrayList<>());
			scoresByModality.get(modality).add(isSuccessResponse(response) ? response.getResponse().getScore() : 0);
		}

		Map<BiometricType, Float> result = new EnumMap<>(BiometricType.class);
		scoresByModality.forEach((modality, scores) -> result.put(modality,
				(float) scores.stream().mapToDouble(s -> s).average().getAsDouble()));
		return result;
	}

	/**
	 * Extracts biometric templates from a list of biometric samples.
	 * 
	 * @param sample The biometric samples from which templates are extracted.
	 * @param flags  Additional flags for template extraction.
	 * @return A list of extracted biometric templates.
	 */
	@Override
	public List<BIR> extractTemplate(List<BIR> sample, Map<String, String> flags) {
		List<BIR> templates = new LinkedList<>();
		for (BIR bir : sample) {
			Response<io.mosip.kernel.core.cbeffutil.entity.BIR> response = sdkRegistry
					.get(BiometricType.fromValue(bir.getBdbInfo().getType().get(0).value()))
					.get(BiometricFunction.EXTRACT)
					.extractTemplate(BIRConverter.convertToBIR(bir), getKeyValuePairs(flags));
			templates.add(isSuccessResponse(response) ? BIRConverter.convertToBiometricRecordBIR(response.getResponse())
					: null);
		}
		return templates;
	}

	/**
	 * Matches sample biometric records against a set of bioRecord for a specified
	 * modality and operation.
	 * 
	 * @param operation The operation type ("AUTH" or "DEDUPE") indicating the type
	 *                  of match operation.
	 * @param sample    The list of sample biometric records to match.
	 * @param bioRecord The array of biometric records against which samples are
	 *                  matched.
	 * @param modality  The biometric modality being matched.
	 * @param flags     Additional flags for the matching operation.
	 * @return true if the match operation succeeds based on the specified operation
	 *         type, false otherwise.
	 */
	private boolean match(String operation, List<BIR> sample, BIR[] bioRecord, BiometricType modality,
			Map<String, String> flags) {
		List<MatchDecision[]> result = new LinkedList<>();
		io.mosip.kernel.core.cbeffutil.entity.BIR[] recordBIR = new io.mosip.kernel.core.cbeffutil.entity.BIR[bioRecord.length];
		for (int i = 0; i < bioRecord.length; i++) {
			recordBIR[i] = BIRConverter.convertToBIR(bioRecord[i]);

		}
		for (int i = 0; i < sample.size(); i++) {
			Response<MatchDecision[]> response = sdkRegistry.get(modality).get(BiometricFunction.MATCH)
					.match(BIRConverter.convertToBIR(sample.get(i)), recordBIR, getKeyValuePairs(flags));

			result.add(isSuccessResponse(response) ? response.getResponse() : null);
		}

		return evaluateMatchDecision(operation, sample, result);
	}

	/**
	 * Adds an SDK instance to the registry for a specific biometric modality.
	 * 
	 * @param iBioApi  The SDK instance implementing IBioApi for the given modality.
	 * @param modality The biometric modality for which the SDK instance is added.
	 */
	private void addToRegistry(IBioApi iBioApi, BiometricType modality) {
		sdkRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class))
				.put(BiometricFunction.EXTRACT, iBioApi);
		sdkRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class))
				.put(BiometricFunction.QUALITY_CHECK, iBioApi);
		sdkRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class)).put(BiometricFunction.MATCH,
				iBioApi);
		sdkRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class))
				.put(BiometricFunction.SEGMENT, iBioApi);
	}

	/**
	 * Retrieves the supported biometric modalities and their associated functions.
	 * 
	 * @return Mapping of supported biometric modalities to their associated
	 *         functions.
	 */
	private Map<BiometricType, List<BiometricFunction>> getSupportedModalities() {
		Map<BiometricType, List<BiometricFunction>> result = new EnumMap<>(BiometricType.class);
		sdkRegistry.forEach((modality, map) -> {
			if (result.get(modality) == null)
				result.put(modality, new ArrayList<>());

			result.get(modality).addAll(map.keySet());
		});
		return result;
	}

	/**
	 * Checks if the response is successful based on the HTTP status code and
	 * non-null response object.
	 * 
	 * @param response The response object to be checked.
	 * @return true if the response is successful (HTTP status code between 200 and
	 *         299 and non-null response object), false otherwise.
	 */
	private boolean isSuccessResponse(Response<?> response) {
		return (response != null && (response.getStatusCode() >= 200 && response.getStatusCode() <= 299)
				&& response.getResponse() != null);
	}

	/**
	 * Evaluates the match decisions based on the operation type ("AUTH" or
	 * "DEDUPE").
	 * 
	 * @param operation The operation type ("AUTH" or "DEDUPE").
	 * @param sample    The list of sample biometric records used for matching.
	 * @param result    The list of match decisions for each sample against the
	 *                  bioRecord.
	 * @return true if the match operation succeeds based on the specified operation
	 *         type, false otherwise.
	 */
	@SuppressWarnings({ "java:S3011" })
	private boolean evaluateMatchDecision(String operation, List<BIR> sample, List<MatchDecision[]> result) {
		int segmentCount = sample.size();
		result = result.stream().filter(Objects::nonNull).toList();

		switch (operation) {
		case "AUTH":
			if (result.size() < segmentCount)
				return false;

			return result.stream().allMatch(decision -> Arrays.stream(decision).anyMatch(d -> d.isMatch()));

		case "DEDUPE":
			return result.stream().anyMatch(decision -> Arrays.stream(decision).anyMatch(d -> d.isMatch()));

		default:
			return false;
		}
	}

	/**
	 * Retrieves key-value pairs from the provided flags map and converts them into
	 * KeyValuePair array.
	 * 
	 * @param flags Additional flags to be converted into KeyValuePair array.
	 * @return Array of KeyValuePair extracted from flags.
	 */
	private KeyValuePair[] getKeyValuePairs(Map<String, String> flags) {
		if (flags == null)
			return new KeyValuePair[0];

		int i = 0;
		KeyValuePair[] kvp = new KeyValuePair[flags.size()];
		for (Map.Entry<String, String> entryInfo : flags.entrySet()) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setKey(entryInfo.getKey());
			keyValuePair.setValue(entryInfo.getValue());
			kvp[i++] = keyValuePair;
		}
		return kvp;
	}
}