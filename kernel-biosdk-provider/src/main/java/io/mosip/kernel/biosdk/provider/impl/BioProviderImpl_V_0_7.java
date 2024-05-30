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

@Component
@SuppressWarnings({ "java:S101" })
public class BioProviderImpl_V_0_7 implements iBioProviderApi {

	private static final Logger LOGGER = BioSDKProviderLoggerFactory.getLogger(BioProviderImpl_V_0_7.class);

	private static final String METHOD_NAME_KEY = "_METHOD_NAME";
	private static final String THRESHOLD_KEY = "_THRESHOLD";
	private static final String API_VERSION = "0.7";

	private static final String TAG_MATCH = "match";
	private static final String TAG_MATCH_COMPOSITE = "compositeMatch";

	private Map<BiometricType, Object> sdkRegistry = new EnumMap<>(BiometricType.class);
	private Map<BiometricType, String> thresholds = new EnumMap<>(BiometricType.class);

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

	/*
	 * compositeMatch --> is intended to be used for match on multiple modalities
	 * NOte: compositeMatch should not be used on multiple segments of same modality
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

	/*
	 * QualityScore checkQuality(BIR sample, KeyValuePair[] flags)
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

	/*
	 * QualityScore checkQuality(BIR sample, KeyValuePair[] flags)
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

	/*
	 * BIR extractTemplate(BIR paramBIR, KeyValuePair[] paramArrayOfKeyValuePair)
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

	private Map<BiometricType, List<BiometricFunction>> getSupportedModalities() {
		Map<BiometricType, List<BiometricFunction>> result = new EnumMap<>(BiometricType.class);
		sdkRegistry.forEach((modality, map) -> result.put(modality, Arrays.asList(BiometricFunction.values())));
		return result;
	}

	private void addToRegistry(Object sdkInstance, BiometricType modality) {
		sdkRegistry.put(modality, sdkInstance);
	}

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