package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class PalmPositionCodes implements Serializable {
	public static final int UNKNOWN_PALM = 20;
	public static final int RIGHT_FULL_PALM = 21;
	public static final int RIGHT_WRITERS_PALM = 22;
	public static final int LEFT_FULL_PALM = 23;
	public static final int LEFT_WRITERS_PALM = 24;
	public static final int RIGHT_LOWER_PALM = 25;
	public static final int RIGHT_UPPER_PALM = 26;
	public static final int LEFT_LOWER_PALM = 27;
	public static final int LEFT_UPPER_PALM = 28;
	public static final int RIGHT_OTHER = 29;
	public static final int LEFT_OTHER = 30;
	public static final int RIGHT_INTERDIGITAL = 31;
	public static final int RIGHT_THENAR = 32;
	public static final int RIGHT_HYPOTHENAR = 33;
	public static final int LEFT_INTERDIGITAL = 34;
	public static final int LEFT_THENAR = 35;
	public static final int LEFT_HYPOTHENAR = 36;
	public static final int RIGHT_GRASP = 37;
	public static final int LEFT_GRASP = 38;
	public static final int RIGHT_CARPAL_DELTA_AREA = 81;
	public static final int LEFT_CARPAL_DELTA_AREA = 82;
	public static final int RIGHT_FULL_PALM_INCLUDING_WRITERS_PALM = 83;
	public static final int LEFT_FULL_PALM_INCLUDING_WRITERS_PALM = 84;
	public static final int RIGHT_WRIST_BRACELET = 85;
	public static final int LEFT_WRIST_BRACELET = 86;

	protected static final Integer[] arrValues = new Integer[] { UNKNOWN_PALM, RIGHT_FULL_PALM, RIGHT_WRITERS_PALM,
			LEFT_FULL_PALM, LEFT_WRITERS_PALM, RIGHT_LOWER_PALM, RIGHT_UPPER_PALM, LEFT_LOWER_PALM, LEFT_UPPER_PALM,
			RIGHT_OTHER, LEFT_OTHER, RIGHT_INTERDIGITAL, RIGHT_THENAR, RIGHT_HYPOTHENAR, LEFT_INTERDIGITAL, LEFT_THENAR,
			LEFT_HYPOTHENAR, RIGHT_GRASP, LEFT_GRASP, RIGHT_CARPAL_DELTA_AREA, LEFT_CARPAL_DELTA_AREA,
			RIGHT_FULL_PALM_INCLUDING_WRITERS_PALM, LEFT_FULL_PALM_INCLUDING_WRITERS_PALM, RIGHT_WRIST_BRACELET,
			LEFT_WRIST_BRACELET };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {
		if (ArrayUtils.contains(arrValues, value))
			return value;
		
		throw new IllegalArgumentException(
				"PlantarPositionCodes value can be " + Arrays.toString(arrValues) + ", set value is wrong [" + value + "]");
	}
}