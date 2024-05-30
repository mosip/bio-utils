package io.mosip.biometrics.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ImageDecoderRequestDto {
	// JPEG2000 or WSQ
	private String imageType = "";
	private int width;
	private int height;
	// [false = lossy, true = lossless]
	private boolean isLossless;
	// [8 = Gray, 24 = RGB]
	private int depth;
	private int horizontalDPI;
	private int verticalDPI;
	private int bitRate;
	private int size;
	//base 64 urlencoded image raw data
	private String imageData = "";
	//GRAY or RGB
	private String imageColorSpace = "";
	private String imageAspectRatio = "";
	//Lossy should between (15 : 1 to 10 : 1), for lossless should between (1 : 1 to 3 : 1)
	private String imageCompressionRatio = "";

	@SuppressWarnings({ "java:S107" })
	public ImageDecoderRequestDto(String imageType, int width, int height, boolean isLossless, int depth,
			int horizontalDPI, int verticalDPI, int bitRate, int size, String imageData, String imageColorSpace,
			String imageAspectRatio, String imageCompressionRatio) {
		super();
		this.imageType = imageType;
		this.width = width;
		this.height = height;
		this.isLossless = isLossless;
		this.depth = depth;
		this.horizontalDPI = horizontalDPI;
		this.verticalDPI = verticalDPI;
		this.bitRate = bitRate;
		this.size = size;
		this.imageData = imageData;
		this.imageColorSpace = imageColorSpace;
		this.imageAspectRatio = imageAspectRatio;
		this.imageCompressionRatio = imageCompressionRatio;
	}
}