package io.mosip.kernel.biometrics.spi;

import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.model.Response;

/**
 * The Interface IBioApiV2.
 * 
 * <p>
 * This interface extends {@link IBioApi} and provides additional operations for
 * biometric API version 2, including converting biometric data from source
 * format to target format.
 * </p>
 * 
 * <p>
 * The {@link #convertFormatV2} method allows converting the provided biometric
 * data from a specified source format to a target format for all segments. If
 * {@code modalitiesToConvert} is null or empty, each modality found in the
 * sample is converted to the target format, assuming the sample is in
 * {@code sourceFormat}.
 * </p>
 * 
 * <p>
 * Implementing classes are expected to handle various biometric types and
 * formats, ensuring accurate conversion of biometric data while maintaining
 * compatibility and integrity.
 * </p>
 * 
 * <p>
 * Authors: Janardhan B S
 * </p>
 * 
 * @since [Version Number]
 * @see Response
 * @see BiometricRecord
 * @see BiometricType
 * @see IBioApi
 */
public interface IBioApiV2 extends IBioApi {
	/**
	 * Converts the provided biometric data from source format to target format for
	 * all segments.
	 * 
	 * <p>
	 * If {@code modalitiesToConvert} is null or empty, each modality found in the
	 * sample is converted to the target format, assuming the sample is in
	 * {@code sourceFormat}.
	 * </p>
	 * 
	 * @param sample              The biometric record containing the sample data to
	 *                            convert.
	 * @param sourceFormat        The source format of the biometric data.
	 * @param targetFormat        The target format to convert the biometric data
	 *                            into.
	 * @param sourceParams        Additional parameters related to the source
	 *                            format.
	 * @param targetParams        Additional parameters related to the target
	 *                            format.
	 * @param modalitiesToConvert The list of biometric types to convert to the
	 *                            target format.
	 * @return A {@link Response} containing {@link BiometricRecord} in the target
	 *         format.
	 */
	Response<BiometricRecord> convertFormatV2(BiometricRecord sample, String sourceFormat, String targetFormat,
			Map<String, String> sourceParams, Map<String, String> targetParams,
			List<BiometricType> modalitiesToConvert);
}