package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class OcclusionOpacityCodes {
	/**
	 * There is no detail in the area of the occlusion.
	 */
	public static final String TOTAL = "T";
	/**
	 * The occlusion contains interfering texture such as eyelashes, hair or
	 * reflection.
	 */
	public static final String INTERFERENCE = "I";
	/**
	 * There is detail in the area of the occlusion that is lighter than the rest of
	 * the face or iris.
	 */
	public static final String PARTIAL_LIGHT = "L";
	/**
	 * There is detail in the area of the occlusion that is darker than the rest of
	 * the face or iris.
	 */
	public static final String PARTIAL_SHADOW = "S";

	public static final String[] arrValues = new String[] { TOTAL, INTERFERENCE, PARTIAL_LIGHT, PARTIAL_SHADOW };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"OcclusionOpacityCodes value can be " + Arrays.toString(arrValues) + ", set value is wrong [" + value + "]");
	}
}