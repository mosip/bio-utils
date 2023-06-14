package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class ColorSpaces {
	public static final String UNK = "UNK";// Undefined
	public static final String GRAY = "GRAY";// Grayscale (monochrome)
	public static final String RGB = "RGB";// Undetermined color space for an RGB image
	public static final String SRGB = "SRGB";// sRGB (IEC 61966-2-1)
	public static final String YCC = "YCC";// YCbCr (legacy)
	public static final String SYCC = "SYCC";// YCbCr (JPEG 2000 compressed)

	public static final String[] arrValues = new String[] { UNK, GRAY, RGB, SRGB, YCC, SYCC };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"ColorSpaces value can be " + arrValues.toString() + ", set value is wrong [" + value + "]");
	}
}