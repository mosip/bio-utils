package io.mosip.kernel.biosdk.provider.factory;

import java.util.*;

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

		Set<String> vendorIds = new HashSet<>(getVendorIds());
		for (String vendorId : vendorIds) {
			if (isProviderRegistryFilled()) {
				logger.info("Provider registry is already filled : {}", providerRegistry.keySet());
				break;
			}

			Map<BiometricType, Map<String, String>> params = getParamsForVendorId(vendorId);
			if (params.isEmpty()) continue;

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
		return (providerApis == null|| providerApis.isEmpty());
	}

	/**
     * Retrieves all unique vendor IDs from the configured maps of finger, iris, and face.
     *
     * @return A list of unique vendor IDs.
     */
	private List<String> getVendorIds() {
		List<String> vendorIds = new ArrayList<>();
		if (finger != null) vendorIds.addAll(finger.keySet());
		if (iris != null) vendorIds.addAll(iris.keySet());
		if (face != null) vendorIds.addAll(face.keySet());
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
		params.put(BiometricType.FINGER, getEntry(finger, vendorId));
		params.put(BiometricType.IRIS, getEntry(iris, vendorId));
		params.put(BiometricType.FACE, getEntry(face, vendorId));

		logger.info("Starting initialization for vendor {} with params >> {}", vendorId, params);

		if (params.values().stream().allMatch(Map::isEmpty)) {
			throw new BiometricException(ErrorCode.NO_SDK_CONFIG.getErrorCode(),
					ErrorCode.NO_SDK_CONFIG.getErrorMessage());
		}

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
		Map<BiometricFunction, iBioProviderApi> providers = providerRegistry.get(modality);
		if (providers != null) {
			iBioProviderApi provider = providers.get(biometricFunction);
			if (provider != null) return provider;
		}

		throw new BiometricException(ErrorCode.NO_PROVIDERS.getErrorCode(), ErrorCode.NO_PROVIDERS.getErrorMessage());
	}

	/** Helper to get map entries safely. */
	private Map<String, String> getEntry(Map<String, Map<String, String>> source, String key) {
		return (source != null) ? source.getOrDefault(key, Collections.emptyMap()) : Collections.emptyMap();
	}

	/**
     * Adds a BioAPI provider to the provider registry for a specified modality and function.
     *
     * @param modality   The biometric modality (finger, iris, face).
     * @param function   The function associated with the modality.
     * @param provider   The BioAPI provider implementing the modality and function.
     */
	private void addToRegistry(BiometricType modality, BiometricFunction function, iBioProviderApi provider) {
		providerRegistry.computeIfAbsent(modality, k -> new EnumMap<>(BiometricFunction.class))
				.putIfAbsent(function, provider);
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
        return switch (modality) {
            case FINGER -> this.finger != null && !this.finger.isEmpty();
            case IRIS -> this.iris != null && !this.iris.isEmpty();
            case FACE -> this.face != null && !this.face.isEmpty();
            default -> false;
        };
	}
}