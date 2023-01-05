package io.mosip.biometrics.util;

import lombok.Data;

@Data
public class ConvertRequestDto {
	// currently supported ISO19794_4_2011, ISO19794_5_2011, ISO19794_6_2011
	private String version; 
	//// Registration or Auth
	private String purpose; 
	// JP2000 --> 0 or WSQ --> 1
	private int imageType; 
	// Iso or image Data
	private byte[] inputBytes;

	//// Face or Iris or Finger
	private String modality; 
	
	private String biometricSubType;
	
	// Default --> 0 or onlyImageInformation --> 1
	private int onlyImageInformation = 0; 

	// use only for lossless images
	//a) For JPEG, it can be a quality from 0 to 100 (the higher is the better). Default value is 95.
	//b) For PNG, it can be the compression level from 0 to 9. A higher value means a smaller size and longer compression time. 
	// 		If specified, strategy is changed to IMWRITE_PNG_STRATEGY_DEFAULT (Z_DEFAULT_STRATEGY). Default value is 1 (best speed setting).
	//c) For WEBP, it can be a quality from 1 to 100 (the higher is the better). 
	//		By default (without any parameter) and for quality above 100 the lossless compression is used.
	//d) For WSQ we do not use opencv 
	//e) For JPEG2000, use to specify the target compression rate (multiplied by 1000). 
	//		The value can be from 0 to 1000. Default is 1000.
	private int compressionRatio = 95; 
}
