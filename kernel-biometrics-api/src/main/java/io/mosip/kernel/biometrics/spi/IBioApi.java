package io.mosip.kernel.biometrics.spi;

import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.model.MatchDecision;
import io.mosip.kernel.biometrics.model.QualityCheck;
import io.mosip.kernel.biometrics.model.Response;
import io.mosip.kernel.biometrics.model.SDKInfo;

/**
 * The Interface IBioApi.
 * 
 * <p>
 * This interface defines operations to interact with a biometric API, including
 * initialization, quality checks, matching, template extraction, segmenting,
 * and format conversion of biometric records.
 * </p>
 * 
 * <p>
 * The operations provided by this interface allow clients to integrate
 * biometric functionalities such as quality assessment, biometric matching,
 * template extraction, and format conversion into their applications.
 * </p>
 * 
 * <p>
 * Implementations of this interface are expected to provide robust support for
 * various biometric types and functions, ensuring accurate and reliable
 * biometric operations.
 * </p>
 * 
 * <p>
 * Methods like {@link #checkQuality}, {@link #match}, {@link #extractTemplate},
 * {@link #segment}, and {@link #convertFormat} define specific biometric
 * operations and provide responses encapsulated in {@link Response} objects
 * containing relevant data or error information.
 * </p>
 * 
 * <p>
 * The {@link SDKInfo} returned by {@link #init} method contains information
 * about the SDK version and supported modalities, facilitating proper
 * initialization and usage of the biometric API.
 * </p>
 * 
 * <p>
 * This interface also supports passing additional flags and parameters through
 * {@link Map} objects, enabling customization and control over biometric
 * operations.
 * </p>
 * 
 * <p>
 * Implementing classes should ensure thread safety and proper exception
 * handling to maintain the integrity and reliability of biometric operations
 * across diverse environments and use cases.
 * </p>
 * 
 * <p>
 * For deprecated methods, such as {@link #convertFormat}, consider migrating to
 * the updated version {@link #convertFormatV2} as indicated by the deprecation
 * notice.
 * </p>
 * 
 * <p>
 * 
 * @author Sanjay Murali
 * @author Manoj SP
 *         </p>
 * 
 * @since [Version Number]
 * @see Response
 * @see SDKInfo
 * @see BiometricType
 * @see BiometricRecord
 * @see MatchDecision
 * @see QualityCheck
 */

public interface IBioApi {

	/**
	 * Initializes the biometric API with the provided initialization parameters.
	 * for eg: license initialization
	 * 
	 * @param initParams The initialization parameters required for setting up the
	 *                   SDK.
	 * @return An instance of {@link SDKInfo} containing SDK version and supported
	 *         modalities information.
	 */
	SDKInfo init(Map<String, String> initParams);

	/**
	 * Checks the quality of the provided biometric image and provides the
	 * respective quality score per modality.
	 * 
	 * <p>
	 * If modalitiesToCheck is null/empty, quality scores are provided for each
	 * modality found in the gallery record.
	 * </p>
	 * 
	 * @param sample            The biometric record containing the sample image to
	 *                          check quality.
	 * @param modalitiesToCheck The list of biometric types to check quality
	 *                          against.
	 * @param flags             Additional flags and parameters passed during
	 *                          quality check.
	 * @return A {@link Response} containing {@link QualityCheck} with quality
	 *         scores per modality.
	 */
	Response<QualityCheck> checkQuality(BiometricRecord sample, List<BiometricType> modalitiesToCheck,
			Map<String, String> flags);

	/**
	 * Compares the biometrics and provides the matching decision per modality for
	 * each item in the gallery.
	 * 
	 * <p>
	 * If modalitiesToMatch is null/empty, matching decisions are provided for each
	 * modality found in the gallery record.
	 * </p>
	 * 
	 * <p>
	 * Pass transactionID in flags to log during this method call.
	 * </p>
	 * 
	 * @param sample            The biometric record containing the sample image to
	 *                          match.
	 * @param gallery           The array of biometric records representing the
	 *                          gallery of images to match against.
	 * @param modalitiesToMatch The list of biometric types to match against.
	 * @param flags             Additional flags and parameters passed during
	 *                          matching.
	 * @return A {@link Response} containing an array of {@link MatchDecision}
	 *         representing matching decisions per modality.
	 */
	Response<MatchDecision[]> match(BiometricRecord sample, BiometricRecord[] gallery,
			List<BiometricType> modalitiesToMatch, Map<String, String> flags);

	/**
	 * Extracts biometric template from the provided sample for specified
	 * modalities.
	 * 
	 * <p>
	 * If modalitiesToExtract is null/empty, templates are extracted for each
	 * modality found in the sample.
	 * </p>
	 * 
	 * @param sample              The biometric record containing the sample image
	 *                            to extract template from.
	 * @param modalitiesToExtract The list of biometric types to extract template
	 *                            for.
	 * @param flags               Additional flags and parameters passed during
	 *                            template extraction.
	 * @return A {@link Response} containing {@link BiometricRecord} with extracted
	 *         biometric template.
	 */
	Response<BiometricRecord> extractTemplate(BiometricRecord sample, List<BiometricType> modalitiesToExtract,
			Map<String, String> flags);

	/**
	 * Segments the single biometric image into multiple biometric images based on
	 * specified modalities.
	 * 
	 * <p>
	 * If modalitiesToSegment is null/empty, each modality found in the sample is
	 * segmented.
	 * </p>
	 * 
	 * @param sample              The biometric record containing the sample image
	 *                            to segment.
	 * @param modalitiesToSegment The list of biometric types to segment the image
	 *                            into.
	 * @param flags               Additional flags and parameters passed during
	 *                            segmentation.
	 * @return A {@link Response} containing {@link BiometricRecord} with segmented
	 *         biometric images.
	 */
	Response<BiometricRecord> segment(BiometricRecord sample, List<BiometricType> modalitiesToSegment,
			Map<String, String> flags);

	/**
	 * Converts the provided BDBData from source format to target format for all
	 * segments.
	 * 
	 * <p>
	 * If modalitiesToConvert is null/empty, each modality found in the sample is
	 * converted to target format (sample must be in sourceFormat).
	 * </p>
	 * 
	 * @deprecated Since version 1.2.1, use {@link #convertFormatV2} instead.
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
	 * @return The biometric record in the target format.
	 */
	@Deprecated(since = "1.2.1", forRemoval = true)
	BiometricRecord convertFormat(BiometricRecord sample, String sourceFormat, String targetFormat,
			Map<String, String> sourceParams, Map<String, String> targetParams,
			List<BiometricType> modalitiesToConvert);
}
