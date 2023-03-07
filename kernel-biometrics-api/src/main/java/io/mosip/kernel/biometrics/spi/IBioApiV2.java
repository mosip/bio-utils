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
 * The Interface IBioApiV2.
 * 
 * @author Janardhan B S
 * 
 */
public interface IBioApiV2 extends IBioApi{
	/**
	 * Converts the provided BDBData from source format to target format for all segments
	 * if modalitiesToConvert is null/empty, each modality found in the sample is converted to target format(sample must be in sourceFormat).
	 * 
	 * @param sample
	 * @param sourceFormat
	 * @param targetFormat
	 * @param sourceParams
	 * @param targetParams
	 * @param modalitiesToConvert
	 * @return
	 */
	Response<BiometricRecord> convertFormatV2(BiometricRecord sample, String sourceFormat, String targetFormat, Map<String, String> sourceParams, 
			Map<String, String> targetParams, List<BiometricType> modalitiesToConvert);		
}
