package io.mosip.kernel.biosdk.provider.spi;

import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.logger.logback.util.MetricTag;

@SuppressWarnings({ "java:S114" })
public interface iBioProviderApi {

	/**
	 * Initializes the SDK based on the provided parameters.
	 *
	 * @param params The map of biometric types to their respective parameters.
	 * @return A map containing supported biometric types and their corresponding
	 *         functions.
	 * @throws BiometricException If an error occurs during SDK initialization.
	 */
	Map<BiometricType, List<BiometricFunction>> init(Map<BiometricType, Map<String, String>> params)
			throws BiometricException;

	/**
	 * Performs a 1:1 biometric verification (match).
	 *
	 * @param sample    The list of sample biometric records.
	 * @param bioRecord The list of biometric records in the gallery for
	 *                  verification.
	 * @param modality  The biometric type (modality) for which the verification is
	 *                  performed.
	 * @param flags     Additional flags for customization of the verification
	 *                  process.
	 * @return true if the verification is successful, false otherwise.
	 */
	boolean verify(List<BIR> sample, List<BIR> bioRecord,
			@MetricTag(value = "modality", extractor = "arg.value") BiometricType modality, Map<String, String> flags);

	/**
	 * Performs a 1:n biometric identification.
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
	Map<String, Boolean> identify(List<BIR> sample, Map<String, List<BIR>> gallery,
			@MetricTag(value = "modality", extractor = "arg.value") BiometricType modality, Map<String, String> flags);

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
	float[] getSegmentQuality(
			@MetricTag(value = "modality", extractor = "int size = arg.length; String[] names = new String[size];for(int i=0;i<size;i++){ names[i] = "
					+ "arg[i].bdbInfo.getSubtype().toString().replaceAll('\\\\[|\\\\]|,','');}"
					+ "return java.util.Arrays.toString(names);") BIR[] sample,
			Map<String, String> flags);

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
	Map<BiometricType, Float> getModalityQuality(
			@MetricTag(value = "modality", extractor = "int size = arg.length; String[] names = new String[size];for(int i=0;i<size;i++){ names[i] = "
					+ "arg[i].bdbInfo.getSubtype().toString().replaceAll('\\\\[|\\\\]|,','');}"
					+ "return java.util.Arrays.toString(names);") BIR[] sample,
			Map<String, String> flags);

	/**
	 * Extracts biometric templates from the provided sample biometric records.
	 *
	 * @param sample The list of sample biometric records.
	 * @param flags  Additional flags for customization of the template extraction
	 *               process.
	 * @return The list of extracted biometric templates.
	 */
	List<BIR> extractTemplate(
			@MetricTag(value = "modality", extractor = "int size = arg.size(); String[] names = new String[size];for(int i=0;i<size;i++){ names[i] = "
					+ "arg.get(i).bdbInfo.getSubtype().toString().replaceAll('\\\\[|\\\\]|,','');}"
					+ "return java.util.Arrays.toString(names);") List<BIR> sample,
			Map<String, String> flags);

}