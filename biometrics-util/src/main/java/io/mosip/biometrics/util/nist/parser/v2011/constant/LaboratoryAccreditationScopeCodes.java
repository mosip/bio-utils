package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class LaboratoryAccreditationScopeCodes implements Serializable {
	public static final String NUCLEAR = "N";
	public static final String MITOCHONDRIAL = "M";
	public static final String DATABASE = "D";
	public static final String OTHER = "O";

	protected static final String[] arrValues = new String[] { NUCLEAR, MITOCHONDRIAL, DATABASE, OTHER };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException("LaboratoryAccreditationScopeCodes value can be " + Arrays.toString(arrValues)
				+ ", set value is wrong [" + value + "]");
	}
}