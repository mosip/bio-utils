package io.mosip.biometrics.util;

import lombok.Data;

/**
 * Represents a request for converting biometric data to a specific format.
 */
@Data
public class ConvertRequestDto {
	/**
	 * The version of the biometric standard supported for conversion.
	 * <p>
	 * Examples include ISO19794_4_2011, ISO19794_5_2011, ISO19794_6_2011.
	 */
	private String version;

	/**
	 * The purpose of the conversion.
	 * <p>
	 * Valid values are "Registration" or "Auth".
	 */
	private String purpose;

	/**
	 * The type of image format to convert to.
	 * <p>
	 * Use 0 for JP2000 and 1 for WSQ.
	 */
	private int imageType;

	/**
	 * The biometric data input, either ISO format or raw image data.
	 */
	private byte[] inputBytes;

	/**
	 * The modality of the biometric data.
	 * <p>
	 * Examples include "Face", "Iris", or "Finger".
	 */
	private String modality;

	/**
	 * Optional subtype of the biometric data.
	 */
	private String biometricSubType;

	 /**
     * Flag indicating whether to include only image information.
     * <p>
     * Default is 0 (false), set to 1 (true) to request only image information.
     */
	private int onlyImageInformation = 0;

	 /**
     * Compression ratio or quality level for lossy images.
     * <p>
     * For JPEG, specify a quality from 0 to 100 (higher is better), default is 95.
     * For PNG, specify a compression level from 0 to 9 (higher value means smaller size and longer compression time).
     * For WEBP, specify a quality from 1 to 100 (higher is better), lossless compression used for quality above 100 or default.
     * For WSQ, this parameter is not applicable.
     * For JPEG2000, specify the target compression rate (multiplied by 1000) from 0 to 1000, default is 1000.
     */
	private int compressionRatio = 95;
}