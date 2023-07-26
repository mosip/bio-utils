package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class PlantarPositionCodes {
	public static final int UNKNOWN_SOLE = 60;
	public static final int SOLE_RIGHT_FOOT = 61;
	public static final int SOLE_LEFT_FOOT = 62;
	public static final int UNKNOWN_TOE = 63;
	public static final int RIGHT_BIG_TOE = 64;
	public static final int RIGHT_SECOND_TOE = 65;
	public static final int RIGHT_MIDDLE_TOE = 66;
	public static final int RIGHT_FOURTH_TOE = 67;
	public static final int RIGHT_LITTLE_TOE = 68;
	public static final int LEFT_BIG_TOE = 69;
	public static final int LEFT_SECOND_TOE = 70;
	public static final int LEFT_MIDDLE_TOE = 71;
	public static final int LEFT_FOURTH_TOE = 72;
	public static final int LEFT_LITTLE_TOE = 73;
	public static final int FRONT_BALL_OF_RIGHT_FOOT = 74;
	public static final int BACK_HEEL_OF_RIGHT_FOOT = 75;
	public static final int FRONT_BALL_OF_LEFT_FOOT = 76;
	public static final int BACK_HEEL_OF_LEFT_FOOT = 77;
	public static final int RIGHT_MIDDLE_OF_FOOT = 78;
	public static final int LEFT_MIDDLE_OF_FOOT = 79;

	public static final Integer[] arrValues = new Integer[] { UNKNOWN_SOLE, SOLE_RIGHT_FOOT, SOLE_LEFT_FOOT,
			UNKNOWN_TOE, RIGHT_BIG_TOE, RIGHT_SECOND_TOE, RIGHT_MIDDLE_TOE, RIGHT_FOURTH_TOE, RIGHT_LITTLE_TOE,
			LEFT_BIG_TOE, LEFT_SECOND_TOE, LEFT_MIDDLE_TOE, LEFT_FOURTH_TOE, LEFT_LITTLE_TOE, FRONT_BALL_OF_RIGHT_FOOT,
			BACK_HEEL_OF_RIGHT_FOOT, FRONT_BALL_OF_LEFT_FOOT, BACK_HEEL_OF_LEFT_FOOT, RIGHT_MIDDLE_OF_FOOT,
			LEFT_MIDDLE_OF_FOOT };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"PlantarPositionCodes value can be " + arrValues.toString() + ", set value is wrong [" + value + "]");
	}
}