package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class LaboratoryCategoryCodes {
	public static final String GOVERNMENT = "G";
	public static final String INDUSTRY = "I";
	public static final String OTHER_LABORATORY = "G";
	public static final String UNKNOWN = "G";

	public static final String[] arrValues = new String[] { GOVERNMENT, INDUSTRY, OTHER_LABORATORY, UNKNOWN };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException("LaboratoryCategoryCodes value can be " + arrValues.toString()
				+ ", set value is wrong [" + value + "]");
	}
}