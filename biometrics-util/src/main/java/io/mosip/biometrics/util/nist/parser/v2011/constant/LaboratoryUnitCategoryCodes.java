package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class LaboratoryUnitCategoryCodes implements Serializable {
	public static final int LABORATORY_DNA_PROCESSING_UNIT = 1;
	public static final int RAPID_DNA_OR_MOBILE_PROCESSING_UNIT = 2;
	public static final int OTHER = 3;
	public static final int UNKNOWN = 4;

	protected static final Integer[] arrValues = new Integer[] { LABORATORY_DNA_PROCESSING_UNIT,
			RAPID_DNA_OR_MOBILE_PROCESSING_UNIT, OTHER, UNKNOWN };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {
		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException("LaboratoryUnitCategoryCodes value can be " + Arrays.toString(arrValues)
				+ ", set value is wrong [" + value + "]");
	}
}