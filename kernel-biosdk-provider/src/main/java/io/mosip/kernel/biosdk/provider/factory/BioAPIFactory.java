package io.mosip.kernel.biosdk.provider.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biosdk.provider.spi.iBioProviderApi;
import io.mosip.kernel.biosdk.provider.util.BioSDKProviderLoggerFactory;
import io.mosip.kernel.biosdk.provider.util.ErrorCode;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.core.logger.spi.Logger;
import jakarta.annotation.PostConstruct;
import lombok.Data;

/**
 * Factory class to manage and initialize BioAPI providers based on configuration properties.
 */
@ConfigurationProperties(prefix = "mosip.biometric.sdk.providers")
@Component
@Data
public class BioAPIFactory {
	private static final Logger logger = BioSDKProviderLoggerFactory.getLogger(BioAPIFactory.class);

	private Map<String, Map<String, String>> finger;
	private Map<String, Map<String, String>> iris;
	private Map<String, Map<String, String>> face;

	@Autowired
	@SuppressWarnings({ "java:S6813" })
	private List<iBioProviderApi> providerApis;

	private Map<BiometricType, Map<BiometricFunction, iBioProviderApi>> providerRegistry = new EnumMap<>(
			BiometricType.class);

	 /**
     * Initializes the BioAPI providers based on the configured provider APIs and vendor IDs.
     *
     * @throws BiometricException if no valid provider APIs are available or if the provider registry cannot be filled.
     */
	@PostConstruct
	public void initializeBioAPIProviders() throws BiometricException {
		if (isValidProviderApis(providerApis)) {
			throw new BiometricException(ErrorCode.NO_PROVIDERS.getErrorCode(),
					ErrorCode.NO_PROVIDERS.getErrorMessage());
		}

		List<String> vendorIds = getVendorIds();
		for (String vendorId : new HashSet<>(vendorIds)) {
			if (isProviderRegistryFilled()) {
				logger.info("Provider registry is already filled : {}", providerRegistry.keySet());
				break;
			}

			Map<BiometricType, Map<String, String>> params = getParamsForVendorId(vendorId);

			// pass params per modality to each provider, each providers will initialize
			// supported SDK's
			for (iBioProviderApi provider : providerApis) {
				try {
					Map<BiometricType, List<BiometricFunction>> supportedModalities = provider.init(params);
					if (supportedModalities != null && !supportedModalities.isEmpty()) {
						supportedModalities.forEach((modality, functions) -> functions
								.forEach(function -> addToRegistry(modality, function, provider)));
					}
				} catch (BiometricException ex) {
					logger.error("Failed to initialize SDK instance", ex);
				}
			}
		}
		if (!isProviderRegistryFilled())
			throw new BiometricException(ErrorCode.SDK_REGISTRY_EMPTY.getErrorCode(),
					ErrorCode.SDK_REGISTRY_EMPTY.getErrorMessage());
	}

	 /**
     * Checks if the list of provider APIs is valid (not null or empty).
     *
     * @param providerApis The list of provider APIs to check.
     * @return {@code true} if the list is invalid (null or empty), {@code false} otherwise.
     */
	private boolean isValidProviderApis(List<iBioProviderApi> providerApis) {
		return (Objects.isNull(providerApis)|| providerApis.isEmpty());
	}

	/**
     * Retrieves all unique vendor IDs from the configured maps of finger, iris, and face.
     *
     * @return A list of unique vendor IDs.
     */
	private List<String> getVendorIds() {
		List<String> vendorIds = new ArrayList<>();
		vendorIds.addAll(Objects.isNull(this.finger) ? Collections.emptyList() : this.finger.keySet());
		vendorIds.addAll(Objects.isNull(this.iris) ? Collections.emptyList() : this.iris.keySet());
		vendorIds.addAll(Objects.isNull(this.face) ? Collections.emptyList() : this.face.keySet());

		return vendorIds;
	}

	 /**
     * Retrieves configuration parameters (params) for a specific vendor ID.
     *
     * @param vendorId The vendor ID for which parameters are retrieved.
     * @return A map containing parameters for each biometric type (finger, iris, face).
     * @throws BiometricException if no valid configuration is found for the given vendor ID.
     */
	private Map<BiometricType, Map<String, String>> getParamsForVendorId(String vendorId) throws BiometricException {
		Map<BiometricType, Map<String, String>> params = new EnumMap<>(BiometricType.class);
		params.put(BiometricType.FINGER, getFingerEntry(vendorId));
		params.put(BiometricType.IRIS, getIrisEntry(vendorId));
		params.put(BiometricType.FACE, getFaceEntry(vendorId));

		logger.info("Starting initialization for vendor {} with params >> {}", vendorId, params);

		if (params.isEmpty())
			throw new BiometricException(ErrorCode.NO_SDK_CONFIG.getErrorCode(),
					ErrorCode.NO_SDK_CONFIG.getErrorMessage());

		return params;
	}

	 /**
     * Retrieves the BioAPI provider for the specified modality and function.
     *
     * @param modality           The biometric modality (finger, iris, face).
     * @param biometricFunction  The function associated with the modality.
     * @return The BioAPI provider implementing the specified modality and function.
     * @throws BiometricException if no valid provider is found for the specified modality and function.
     */
	public iBioProviderApi getBioProvider(BiometricType modality, BiometricFunction biometricFunction)
			throws BiometricException {
		if (providerRegistry.get(modality) != null && providerRegistry.get(modality).get(biometricFunction) != null)
			return providerRegistry.get(modality).get(biometricFunction);

		throw new BiometricException(ErrorCode.NO_PROVIDERS.getErrorCode(), ErrorCode.NO_PROVIDERS.getErrorMessage());
	}

	/**
     * Adds a BioAPI provider to the provider registry for a specified modality and function.
     *
     * @param modality   The biometric modality (finger, iris, face).
     * @param function   The function associated with the modality.
     * @param provider   The BioAPI provider implementing the modality and function.
     */
	private void addToRegistry(BiometricType modality, BiometricFunction function, iBioProviderApi provider) {
		providerRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class));
		providerRegistry.get(modality).put(function, provider);
	}

	/**
     * Checks if the provider registry is fully filled based on configured modalities (finger, iris, face).
     *
     * @return {@code true} if the provider registry is fully filled, {@code false} otherwise.
     */
	private boolean isProviderRegistryFilled() {
		if (isModalityConfigured(BiometricType.FINGER) && !providerRegistry.containsKey(BiometricType.FINGER))
			return false;

		if (isModalityConfigured(BiometricType.IRIS) && !providerRegistry.containsKey(BiometricType.IRIS))
			return false;

		return !(isModalityConfigured(BiometricType.FACE) && !providerRegistry.containsKey(BiometricType.FACE));
	}

	/**
     * Checks if a specific biometric modality (finger, iris, face) is configured.
     *
     * @param modality The biometric modality to check.
     * @return {@code true} if the modality is configured, {@code false} otherwise.
     */
	private boolean isModalityConfigured(BiometricType modality) {
		switch (modality) {
		case FINGER:
			return this.finger != null && !this.finger.isEmpty();
		case IRIS:
			return this.iris != null && !this.iris.isEmpty();
		case FACE:
			return this.face != null && !this.face.isEmpty();
		default:
			return false;
		}
	}

	 /**
     * Retrieves the configuration entry for the 'finger' modality based on a specific vendor ID.
     *
     * @param key The vendor ID for which the configuration entry is retrieved.
     * @return The configuration entry for the 'finger' modality associated with the vendor ID.
     */
	private Map<String, String> getFingerEntry(String key) {
		return this.finger == null ? Collections.emptyMap() : this.finger.getOrDefault(key, Collections.emptyMap());
	}

	/**
     * Retrieves the configuration entry for the 'iris' modality based on a specific vendor ID.
     *
     * @param key The vendor ID for which the configuration entry is retrieved.
     * @return The configuration entry for the 'iris' modality associated with the vendor ID.
     */
	private Map<String, String> getIrisEntry(String key) {
		return this.iris == null ? Collections.emptyMap() : this.iris.getOrDefault(key, Collections.emptyMap());
	}

	/**
     * Retrieves the configuration entry for the 'face' modality based on a specific vendor ID.
     *
     * @param key The vendor ID for which the configuration entry is retrieved.
     * @return The configuration entry for the 'face' modality associated with the vendor ID.
     */
	private Map<String, String> getFaceEntry(String key) {
		return this.face == null ? Collections.emptyMap() : this.face.getOrDefault(key, Collections.emptyMap());
	}
}