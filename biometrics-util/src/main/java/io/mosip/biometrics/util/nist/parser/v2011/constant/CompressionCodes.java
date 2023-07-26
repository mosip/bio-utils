package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class CompressionCodes {
	/**
	 *  Uncompressed Lossless NA
	 */
	public static final String NONE_LOSSLESS = "NONE";
	/**
	 *  "WSQ (Wavelet Scalar Quantization) Lossy Version 3.1:2010
	 */
	public static final String WSQ20_LOSSY = "WSQ20";
	/**
	 *  JPEG (Joint Photographic Experts Group) Lossy ISO/IEC 10918, JFIF 1.02:1992
	 */
	public static final String JPEG_LOSSY = "JPEGB";
	/**
	 * JPEG Lossless ISO/IEC 10918, JFIF 1.02:1992
	 */
	public static final String JPEG_LOSSLESS = "JPEGL";
	/**
	 * JPEG 2000 Lossy ISO 15444-1:2004
	 */
	public static final String JP2_LOSSY = "JP2";
	/**
	 * JPEG 2000 Lossless ISO 15444-1:2004
	 */
	public static final String JP2_LOSSLESS = "JP2L";
	/**

	 */
	public static final String PNG_LOSSLESS = "PNG";

	public static final String[] arrValues = new String[] { NONE_LOSSLESS, WSQ20_LOSSY, JPEG_LOSSY, JPEG_LOSSLESS,
			JP2_LOSSY, JP2_LOSSLESS, PNG_LOSSLESS };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"CompressionCodes value can be " + arrValues.toString() + ", set value is wrong [" + value + "]");
	}
}