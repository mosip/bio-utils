package io.mosip.kernel.biometrics.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.RegistryIDType;
import lombok.Data;

/**
 * Represents information about an SDK including API version, SDK version,
 * supported biometric modalities, supported biometric methods per function,
 * additional information, and product owner details.
 * <p>
 * This class encapsulates essential details related to an SDK, such as API
 * version, SDK version, supported biometric modalities, supported methods
 * categorized by {@link BiometricFunction}, additional information stored as
 * key-value pairs, and ownership details represented by {@link RegistryIDType}.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * SDKInfo sdkInfo = new SDKInfo("1.0", "2.5", "MOSIP", "BiometricModule")
 *      .withSupportedMethod(BiometricFunction.CAPTURE, BiometricType.FINGERPRINT)
 *      .withSupportedMethod(BiometricFunction.MATCH, BiometricType.IRIS);
 * }</pre>
 *
 * @see BiometricFunction
 * @see BiometricType
 * @see RegistryIDType
 * @since 1.0
 */
@Data
public class SDKInfo {

	/**
	 * The API version of the SDK.
	 */
	private String apiVersion;

	/**
	 * The version of the SDK.
	 */
	private String sdkVersion;

	/**
	 * The list of supported biometric modalities by the SDK.
	 */
	private List<BiometricType> supportedModalities;

	/**
	 * Mapping of {@link BiometricFunction} to lists of {@link BiometricType}
	 * supported for each function.
	 */
	private Map<BiometricFunction, List<BiometricType>> supportedMethods;

	/**
	 * Additional information associated with the SDK, stored as key-value pairs.
	 * For example, license expiry details can be stored here.
	 */
	private Map<String, String> otherInfo;

	/**
	 * The product owner details represented by {@link RegistryIDType}.
	 */
	private RegistryIDType productOwner;

	/**
	 * Default constructor for {@link SDKInfo}.
	 * <p>
	 * Initializes all string fields as empty strings, supported modalities and
	 * methods as empty collections, and product owner as a new
	 * {@link RegistryIDType} with empty attributes.
	 * </p>
	 */
	public SDKInfo() {
		this.apiVersion = "";
		this.sdkVersion = "";
		this.supportedModalities = new ArrayList<>();
		this.supportedMethods = new HashMap<>();
		this.otherInfo = new HashMap<>();
		this.productOwner = new RegistryIDType("", "");
	}

	/**
	 * Constructs an instance of {@link SDKInfo} with specified API version, SDK
	 * version, organization, and type of the product owner.
	 *
	 * @param apiVersion   the API version of the SDK
	 * @param sdkVersion   the version of the SDK
	 * @param organization the name of the organization owning the product
	 * @param type         the type of product or module within the organization
	 */
	public SDKInfo(String apiVersion, String sdkVersion, String organization, String type) {
		this.apiVersion = apiVersion;
		this.sdkVersion = sdkVersion;
		this.productOwner = new RegistryIDType(organization, type);
		this.supportedModalities = new ArrayList<>();
		this.supportedMethods = new EnumMap<>(BiometricFunction.class);
		this.otherInfo = new HashMap<>();
	}

	/**
	 * Adds a supported {@link BiometricType} under a specific
	 * {@link BiometricFunction}.
	 * <p>
	 * If the provided function does not exist in the {@link #supportedMethods}
	 * map, a new entry will be created before adding the biometric type.
	 * </p>
	 *
	 * @param function      the {@link BiometricFunction} for which the biometric
	 *                      type is supported
	 * @param biometricType the {@link BiometricType} that is supported
	 * @return the current {@link SDKInfo} instance with the updated supported
	 *         methods list for method chaining
	 */
	@SuppressWarnings({ "java:S3824" })
	public SDKInfo withSupportedMethod(BiometricFunction function, BiometricType biometricType) {
		if (!this.supportedMethods.containsKey(function))
			this.supportedMethods.put(function, new ArrayList<>());
		this.supportedMethods.get(function).add(biometricType);
		return this;
	}
}