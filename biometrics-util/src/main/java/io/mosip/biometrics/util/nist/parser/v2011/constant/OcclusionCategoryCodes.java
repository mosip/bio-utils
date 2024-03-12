package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class OcclusionCategoryCodes {
	/**
	 * Eyelashes or reflections of eyelashes (iris only)
	 */
	public static final String LASHES = "L";
	/**
	 * Hair, hat, veil, burka, or other head covering (face only)
	 */
	public static final String HEAD_COVERING = "H";
	/**
	 * Specularity, reflection of light
	 */
	public static final String SPECULAR = "S";
	/**
	 * Shadow cast
	 */
	public static final String SHADOW = "C";
	/**
	 * Reflection of an object
	 */
	public static final String REFLECTION = "R";
	/**
	 * Any other occlusion, such as eyeglass frames blocking the image
	 */
	public static final String OTHER = "O";

	public static final String[] arrValues = new String[] { LASHES, HEAD_COVERING, SPECULAR, SHADOW, REFLECTION,
			OTHER };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"OcclusionCategoryCodes value can be " + Arrays.toString(arrValues) + ", set value is wrong [" + value + "]");
	}
}