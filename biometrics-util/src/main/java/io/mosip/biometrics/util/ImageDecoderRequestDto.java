package io.mosip.biometrics.util;

import lombok.Data;

/**
 * DTO class representing a request to decode an image, containing various image
 * attributes.
 */
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
	// base 64 urlencoded image raw data
	private String imageData = "";
	// GRAY or RGB
	private String imageColorSpace = "";
	private String imageAspectRatio = "";
	// Lossy should between (15 : 1 to 10 : 1), for lossless should between (1 : 1
	// to 3 : 1)
	private String imageCompressionRatio = "";

	/**
	 * Constructor for initializing ImageDecoderRequestDto object with all
	 * attributes.
	 * 
	 * @param imageType             the type of image (e.g., JPEG2000, WSQ)
	 * @param width                 the width of the image
	 * @param height                the height of the image
	 * @param isLossless            indicates if the image is lossless (true) or
	 *                              lossy (false)
	 * @param depth                 the color depth of the image (8 for Gray, 24 for
	 *                              RGB)
	 * @param horizontalDPI         the horizontal DPI (dots per inch) of the image
	 * @param verticalDPI           the vertical DPI (dots per inch) of the image
	 * @param bitRate               the bit rate of the image
	 * @param size                  the size of the image
	 * @param imageData             the base64 urlencoded raw data of the image
	 * @param imageColorSpace       the color space of the image (GRAY or RGB)
	 * @param imageAspectRatio      the aspect ratio of the image
	 * @param imageCompressionRatio the compression ratio of the image, formatted as
	 *                              "x:y"
	 */
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