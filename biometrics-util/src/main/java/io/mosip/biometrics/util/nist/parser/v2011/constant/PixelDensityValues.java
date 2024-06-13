package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class PixelDensityValues implements Serializable {
	public static final int VALUE_MIN = 1;
	public static final int VALUE_MAX = 9999;

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {

		if (value >= VALUE_MIN || value <= VALUE_MAX)
			return value;
		throw new IllegalArgumentException(
				"PixelDensityValues value between [" + VALUE_MIN + " and " + VALUE_MAX + "], set value is wrong [" + value + "]");
	}
}