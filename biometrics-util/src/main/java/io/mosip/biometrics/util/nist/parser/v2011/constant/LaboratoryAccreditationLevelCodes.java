package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class LaboratoryAccreditationLevelCodes {
	public static final int NO_ACCREDITATION = 0;
	public static final int ISO_ACCREDITATION = 1;
	public static final int GLP_ACCREDITATION = 2;
	public static final int AABB_ACCREDITATION = 3;
	public static final int ISO_OR_ILAC_GUIDE_19_ACCREDITATION = 4;
	public static final int ASCLD_LAB_ACCREDITATION = 5;
	public static final int OTHER = 6;
	public static final int UNKNOWN = 255;

	public static final Integer[] arrValues = new Integer[] { NO_ACCREDITATION, ISO_ACCREDITATION, GLP_ACCREDITATION,
			AABB_ACCREDITATION, ISO_OR_ILAC_GUIDE_19_ACCREDITATION, ASCLD_LAB_ACCREDITATION, OTHER, UNKNOWN };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException("LaboratoryAccreditationLevelCodes value can be " + Arrays.toString(arrValues)
				+ ", set value is wrong [" + value + "]");
	}
}