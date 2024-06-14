package io.mosip.kernel.biosdk.provider.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.mosip.kernel.biosdk.provider.util.BioSDKProviderLoggerFactory;
import io.mosip.kernel.core.logger.spi.Logger;
import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.Match;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.model.Decision;
import io.mosip.kernel.biometrics.model.MatchDecision;
import io.mosip.kernel.biometrics.model.QualityCheck;
import io.mosip.kernel.biometrics.model.Response;
import io.mosip.kernel.biometrics.model.SDKInfo;
import io.mosip.kernel.biometrics.spi.IBioApi;
import io.mosip.kernel.biosdk.provider.spi.iBioProviderApi;
import io.mosip.kernel.biosdk.provider.util.BioProviderUtil;
import io.mosip.kernel.biosdk.provider.util.ErrorCode;
import io.mosip.kernel.biosdk.provider.util.ProviderConstants;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.core.bioapi.model.CompositeScore;
import io.mosip.kernel.core.bioapi.model.KeyValuePair;
import io.mosip.kernel.core.bioapi.model.QualityScore;

/**
 * Implementation of the {@link iBioProviderApi} interface for version 0.9 of
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
public class BioProviderImpl_V_0_9 implements iBioProviderApi {

	private static final Logger logger = BioSDKProviderLoggerFactory.getLogger(BioProviderImpl_V_0_9.class);

	private static final String API_VERSION = "0.9";
	private final Map<BiometricType, Map<BiometricFunction, IBioApi>> sdkRegistry = new EnumMap<>(BiometricType.class);

	/**
	 * Initializes the biometric SDKs based on the provided parameters.
	 *
	 * @param params The map of biometric types to their respective parameters.
	 * @return A map containing supported biometric types and their corresponding
	 *         functions.
	 * @throws BiometricException If an error occurs during SDK initialization or if
	 *                            the SDK version does not match.
	 */
	@Counted(value = "sdk.count", extraTags = { "api_version", API_VERSION })
	@Timed(value = "sdk.time", extraTags = { "api_version", API_VERSION })
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
				SDKInfo sdkInfo = iBioApi.init(modalityParams);

				// cross check loaded SDK version and configured SDK version
				if (!API_VERSION.equals(sdkInfo.getApiVersion()))
					throw new BiometricException(ErrorCode.INVALID_SDK_VERSION.getErrorCode(),
							ErrorCode.INVALID_SDK_VERSION.getErrorCode());

				addToRegistry(sdkInfo, iBioApi, modality);
			}
		}
		return getSupportedModalities();
	}

	/**
	 * Performs biometric verification between a sample and a gallery of biometric
	 * records.
	 *
	 * @param sample     The list of sample biometric records.
	 * @param bioRecords The list of biometric records in the gallery.
	 * @param modality   The biometric type (modality) for which the verification is
	 *                   performed.
	 * @param flags      Additional flags for customization of the verification
	 *                   process.
	 * @return true if the verification is successful, false otherwise.
	 */
	@Counted(value = "sdk.count", extraTags = { "api_version", API_VERSION })
	@Timed(value = "sdk.time", extraTags = { "api_version", API_VERSION })
	@Override
	public boolean verify(List<BIR> sample, List<BIR> bioRecords, BiometricType modality, Map<String, String> flags) {
		BiometricRecord galleryRecord = getBiometricRecord(bioRecords.toArray(new BIR[bioRecords.size()]));
		Response<MatchDecision[]> response = sdkRegistry.get(modality).get(BiometricFunction.MATCH).match(
				getBiometricRecord(sample.toArray(new BIR[sample.size()])), new BiometricRecord[] { galleryRecord },
				Arrays.asList(modality), flags);

		if (isSuccessResponse(response)) {
			Map<BiometricType, Decision> decisions = response.getResponse()[0].getDecisions();
			if (decisions.containsKey(modality)) {
				Match matchResult = decisions.get(modality).getMatch();
				logger.info(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
						ProviderConstants.LOGGER_EMPTY,
						MessageFormat.format("Verify::AnalyticsInfo : {0}, errors : {1}",
								decisions.get(modality).getAnalyticsInfo(), decisions.get(modality).getErrors()));
				return Match.MATCHED.equals(matchResult);
			}
		}

		return false;
	}

	/**
	 * Performs biometric identification between a sample and a gallery of biometric
	 * records.
	 *
	 * @param sample   The list of sample biometric records.
	 * @param gallery  The gallery of biometric records mapped by their respective
	 *                 keys.
	 * @param modality The biometric type (modality) for which the identification is
	 *                 performed.
	 * @param flags    Additional flags for customization of the identification
	 *                 process.
	 * @return A map containing the key from the gallery and a boolean indicating if
	 *         there was a match.
	 */
	@Counted(value = "sdk.count", extraTags = { "api_version", API_VERSION })
	@Timed(value = "sdk.time", extraTags = { "api_version", API_VERSION })
	@Override
	public Map<String, Boolean> identify(List<BIR> sample, Map<String, List<BIR>> gallery, BiometricType modality,
			Map<String, String> flags) {
		Map<String, Integer> keyIndexMapping = new HashMap<>();
		BiometricRecord[] galleryRecords = new BiometricRecord[gallery.size()];
		int i = 0;
		for (Map.Entry<String, List<BIR>> entry : gallery.entrySet()) {
			String key = entry.getKey();
			keyIndexMapping.put(key, i);
			galleryRecords[i++] = getBiometricRecord(gallery.get(key).toArray(new BIR[gallery.get(key).size()]));
		}

		Response<MatchDecision[]> response = sdkRegistry.get(modality).get(BiometricFunction.MATCH).match(
				getBiometricRecord(sample.toArray(new BIR[sample.size()])), galleryRecords, Arrays.asList(modality),
				flags);

		Map<String, Boolean> result = new HashMap<>();
		if (isSuccessResponse(response)) {
			keyIndexMapping.forEach((key, index) -> {
				if (response.getResponse()[index].getDecisions().containsKey(modality)) {
					result.put(key, Match.MATCHED
							.equals(response.getResponse()[index].getDecisions().get(modality).getMatch()));
					logger.info(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
							ProviderConstants.LOGGER_EMPTY,
							MessageFormat.format("Identify::AnalyticsInfo : {0}, errors : {1}",
									response.getResponse()[index].getDecisions().get(modality).getAnalyticsInfo(),
									response.getResponse()[index].getDecisions().get(modality).getErrors()));
				} else
					result.put(key, false);
			});
		}
		return result;
	}

	/**
	 * Retrieves the quality scores for each segment in the provided sample
	 * biometric records.
	 *
	 * @param sample The array of sample biometric records.
	 * @param flags  Additional flags for customization of the quality check
	 *               process.
	 * @return An array of quality scores corresponding to each segment in the
	 *         sample.
	 */
	@Counted(value = "sdk.count", extraTags = { "api_version", API_VERSION })
	@Timed(value = "sdk.time", extraTags = { "api_version", API_VERSION })
	@Override
	public float[] getSegmentQuality(BIR[] sample, Map<String, String> flags) {
		float[] scores = new float[sample.length];
		for (int i = 0; i < sample.length; i++) {
			BiometricType modality = BiometricType.fromValue(sample[i].getBdbInfo().getType().get(0).value());
			Response<QualityCheck> response = sdkRegistry.get(modality).get(BiometricFunction.QUALITY_CHECK)
					.checkQuality(getBiometricRecord(sample[i]), Arrays.asList(modality), flags);

			if (isSuccessResponse(response) && response.getResponse().getScores() != null
					&& response.getResponse().getScores().containsKey(modality)) {
				scores[i] = response.getResponse().getScores().get(modality).getScore();
				logger.info(ProviderConstants.LOGGER_SESSIONID, ProviderConstants.LOGGER_IDTYPE,
						ProviderConstants.LOGGER_EMPTY,
						MessageFormat.format("SegmentQuality::AnalyticsInfo : {0}, errors : {1}",
								response.getResponse().getScores().get(modality).getAnalyticsInfo(),
								response.getResponse().getScores().get(modality).getErrors()));
			} else
				scores[i] = 0;
		}
		return scores;
	}

	/**
	 * Retrieves the overall quality scores for each biometric modality in the
	 * provided sample biometric records.
	 *
	 * @param sample The array of sample biometric records.
	 * @param flags  Additional flags for customization of the quality check
	 *               process.
	 * @return A map containing each biometric modality and its corresponding
	 *         overall quality score.
	 */
	@Counted(value = "sdk.count", extraTags = { "api_version", API_VERSION })
	@Timed(value = "sdk.time", extraTags = { "api_version", API_VERSION })
	@Override
	public Map<BiometricType, Float> getModalityQuality(BIR[] sample, Map<String, String> flags) {
		Set<BiometricType> modalitites = new HashSet<>();
		for (int i = 0; i < sample.length; i++) {
			modalitites.add(BiometricType.fromValue(sample[i].getBdbInfo().getType().get(0).value()));
		}

		Map<BiometricType, Float> scoreMap = new EnumMap<>(BiometricType.class);
		for (BiometricType modality : modalitites) {
			Response<QualityCheck> response = sdkRegistry.get(modality).get(BiometricFunction.QUALITY_CHECK)
					.checkQuality(getBiometricRecord(sample), Arrays.asList(modality), flags);

			if (isSuccessResponse(response) && response.getResponse().getScores() != null
					&& response.getResponse().getScores().containsKey(modality)) {
				scoreMap.put(modality, response.getResponse().getScores().get(modality).getScore());
				logger.info("ModalityQuality::AnalyticsInfo : {}, errors : {}",
						response.getResponse().getScores().get(modality).getAnalyticsInfo(),
						response.getResponse().getScores().get(modality).getErrors());
			} else {
				scoreMap.put(modality, 0f);
			}
		}

		float[] scores = new float[sample.length];
		for (int i = 0; i < sample.length; i++) {
			BiometricType modality = BiometricType.fromValue(sample[i].getBdbInfo().getType().get(0).value());
			if (scoreMap.containsKey(modality))
				scores[i] = scoreMap.get(modality);
			else
				scores[i] = 0;
		}
		return scoreMap;
	}

	/**
	 * Extracts biometric templates from the provided sample biometric records.
	 *
	 * @param sample The list of sample biometric records.
	 * @param flags  Additional flags for customization of the template extraction
	 *               process.
	 * @return The list of extracted biometric templates.
	 */
	@Counted(value = "sdk.count", extraTags = { "api_version", API_VERSION })
	@Timed(value = "sdk.time", extraTags = { "api_version", API_VERSION })
	@Override
	@SuppressWarnings({ "java:S1488" })
	public List<BIR> extractTemplate(List<BIR> sample, Map<String, String> flags) {
		Map<BiometricType, List<BIR>> birsByModality = sample.stream().collect(
				Collectors.groupingBy(bir -> BiometricType.fromValue(bir.getBdbInfo().getType().get(0).value())));

		List<BIR> templates = birsByModality.entrySet().stream().<BIR>flatMap(entry -> {
			BiometricType modality = entry.getKey();
			List<BIR> birsForModality = entry.getValue();

			BiometricRecord sampleRecord = getBiometricRecord(birsForModality.toArray(new BIR[birsForModality.size()]));

			Response<BiometricRecord> response = sdkRegistry.get(modality).get(BiometricFunction.EXTRACT)
					.extractTemplate(sampleRecord, List.of(modality), flags);

			if (isSuccessResponse(response)) {
				return response.getResponse().getSegments().stream();
			}

			return Stream.empty();
		}).toList();

		return templates;
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
	 * Adds the loaded SDK instance to the registry based on the supported biometric
	 * functions.
	 *
	 * @param sdkInfo  The SDK information containing supported biometric methods.
	 * @param iBioApi  The SDK instance to be added to the registry.
	 * @param modality The biometric type (modality) associated with the SDK
	 *                 instance.
	 */
	private void addToRegistry(SDKInfo sdkInfo, IBioApi iBioApi, BiometricType modality) {
		for (BiometricFunction biometricFunction : sdkInfo.getSupportedMethods().keySet()) {
			if (sdkInfo.getSupportedMethods().get(biometricFunction).contains(modality)) {
				sdkRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class));
				sdkRegistry.get(modality).put(biometricFunction, iBioApi);
			}
			logger.info("Successfully registered SDK : {}, BiometricFunction: {}",
					sdkInfo.getProductOwner().getOrganization(), biometricFunction);
		}
	}

	/**
	 * Retrieves supported biometric modalities along with their associated
	 * functions.
	 *
	 * @return A map containing each supported biometric modality and its associated
	 *         list of functions.
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
	 * Converts an array of biometric records into a biometric record containing
	 * segments.
	 *
	 * @param birs The array of biometric records.
	 * @return A biometric record containing segments initialized from the provided
	 *         array.
	 */
	private BiometricRecord getBiometricRecord(BIR[] birs) {
		BiometricRecord biometricRecord = new BiometricRecord();
		biometricRecord.setSegments(Arrays.asList(birs));
		return biometricRecord;
	}

	/**
	 * Converts a single biometric record into a biometric record containing
	 * segments.
	 *
	 * @param bir The single biometric record.
	 * @return A biometric record containing segments initialized from the provided
	 *         record.
	 */
	private BiometricRecord getBiometricRecord(BIR bir) {
		BiometricRecord biometricRecord = new BiometricRecord();
		biometricRecord.getSegments().add(bir);
		return biometricRecord;
	}
}