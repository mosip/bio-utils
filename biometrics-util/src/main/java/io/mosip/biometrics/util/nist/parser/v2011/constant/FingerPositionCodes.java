package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class FingerPositionCodes {
	public static final int UNKNOWN_FINGER = 0;
	public static final int RIGHT_THUMB = 1;
	public static final int RIGHT_INDEX_FINGER = 2;
	public static final int RIGHT_MIDDLE_FINGER = 3;
	public static final int RIGHT_RING_FINGER = 4;
	public static final int RIGHT_LITTLE_FINGER = 5;
	public static final int LEFT_THUMB = 6;
	public static final int LEFT_INDEX_FINGER = 7;
	public static final int LEFT_MIDDLE_FINGER = 8;
	public static final int LEFT_RING_FINGER = 9;
	public static final int LEFT_LITTLE_FINGER = 10;
	public static final int PLAIN_RIGHT_THUMB = 11;
	public static final int PLAIN_LEFT_THUMB = 12;
	public static final int PLAIN_RIGHT_FOUR_FINGERS_MAY_INCLUDE_EXTRA_DIGITS = 13;
	public static final int PLAIN_LEFT_FOUR_FINGERS_MAY_INCLUDE_EXTRA_DIGITS = 14;
	public static final int LEFT_AND_RIGHT_THUMBS = 15;
	public static final int RIGHT_EXTRA_DIGIT = 16;
	public static final int LEFT_EXTRA_DIGIT = 17;
	public static final int UNKNOWN_FRICTION_RIDGE = 18;
	public static final int EJI_OR_TIP = 19;

	/*
	 * 2-Finger Combinations
	 */
	public static final int RIGHT_INDEX_AND_MIDDLE = 40;
	public static final int RIGHT_MIDDLE_AND_RING = 41;
	public static final int RIGHT_RING_AND_LITTLE = 42;
	public static final int LEFT_INDEX_AND_MIDDLE = 43;
	public static final int LEFT_MIDDLE_AND_RING = 44;
	public static final int LEFT_RING_AND_LITTLE = 45;
	public static final int RIGHT_INDEX_AND_LEFT_INDEX = 46;

	/*
	 * 3-Finger Combinations
	 */
	public static final int RIGHT_INDEX_AND_MIDDLE_AND_RING = 47;
	public static final int RIGHT_MIDDLE_AND_RING_AND_LITTLE = 48;
	public static final int LEFT_INDEX_AND_MIDDLE_AND_RING = 49;
	public static final int LEFT_MIDDLE_AND_RING_AND_LITTLE = 50;
	/*
	 * 4-Finger Combinations
	 */
	public static final int FINGERTIPS_FOUR_FINGERS_SIMULTANEOUSLY_NO_THUMB_RIGHT_HAND_PLAIN = 51;
	public static final int FINGERTIPS_FOUR_FINGERS_SIMULTANEOUSLY_NO_THUMB_LEFT_HAND_PLAIN = 52;

	/*
	 * 5-Finger Combinations
	 */
	public static final int FINGERTIPS_FOUR_FINGERS_AND_THUMB_SIMULTANEOUSLY_RIGHT_HAND_PLAIN = 53;
	public static final int FINGERTIPS_FOUR_FINGERS_AND_THUMB_SIMULTANEOUSLY_LEFT_HAND_PLAIN = 54;

	public static final Integer[] arrValues = new Integer[] { UNKNOWN_FINGER, RIGHT_THUMB, RIGHT_INDEX_FINGER,
			RIGHT_MIDDLE_FINGER, RIGHT_RING_FINGER, RIGHT_LITTLE_FINGER, LEFT_THUMB, LEFT_INDEX_FINGER,
			LEFT_MIDDLE_FINGER, LEFT_RING_FINGER, LEFT_LITTLE_FINGER, PLAIN_RIGHT_THUMB, PLAIN_LEFT_THUMB,
			PLAIN_RIGHT_FOUR_FINGERS_MAY_INCLUDE_EXTRA_DIGITS, PLAIN_LEFT_FOUR_FINGERS_MAY_INCLUDE_EXTRA_DIGITS,
			LEFT_AND_RIGHT_THUMBS, RIGHT_EXTRA_DIGIT, LEFT_EXTRA_DIGIT, UNKNOWN_FRICTION_RIDGE, EJI_OR_TIP,
			RIGHT_INDEX_AND_MIDDLE, RIGHT_MIDDLE_AND_RING, RIGHT_RING_AND_LITTLE, LEFT_INDEX_AND_MIDDLE,
			LEFT_MIDDLE_AND_RING, LEFT_RING_AND_LITTLE, RIGHT_INDEX_AND_LEFT_INDEX, RIGHT_INDEX_AND_MIDDLE_AND_RING,
			RIGHT_MIDDLE_AND_RING_AND_LITTLE, LEFT_INDEX_AND_MIDDLE_AND_RING, LEFT_MIDDLE_AND_RING_AND_LITTLE,
			FINGERTIPS_FOUR_FINGERS_SIMULTANEOUSLY_NO_THUMB_RIGHT_HAND_PLAIN,
			FINGERTIPS_FOUR_FINGERS_SIMULTANEOUSLY_NO_THUMB_LEFT_HAND_PLAIN,
			FINGERTIPS_FOUR_FINGERS_AND_THUMB_SIMULTANEOUSLY_RIGHT_HAND_PLAIN,
			FINGERTIPS_FOUR_FINGERS_AND_THUMB_SIMULTANEOUSLY_LEFT_HAND_PLAIN };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"FingerPositionCodes value can be " + arrValues.toString() + ", set value is wrong [" + value + "]");
	}
}