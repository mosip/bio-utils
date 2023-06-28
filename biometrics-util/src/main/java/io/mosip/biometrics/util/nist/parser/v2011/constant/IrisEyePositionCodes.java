package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class IrisEyePositionCodes {
	// An entry of “0” in this field indicates that it is undefined which eye is present in this record
	public static final int UNDEFINED = 0;
	// An entry of “1” in this field indicates that the image in this record is the subject’s right eye. 
	public static final int RIGHT = 1;
	// An entry of “2” in this field indicates that the image in this record is the subject’s left eye. 
	public static final int LEFT = 2;

	public static final Integer[] arrValues = new Integer[] { UNDEFINED, RIGHT, LEFT };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {
		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"IrisEyePositionCodes value can be " + arrValues.toString() + ", set value is wrong [" + value + "]");
	}
}