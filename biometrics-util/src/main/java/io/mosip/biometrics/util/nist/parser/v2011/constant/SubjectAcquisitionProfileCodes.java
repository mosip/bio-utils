package io.mosip.biometrics.util.nist.parser.v2011.constant;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class SubjectAcquisitionProfileCodes {
	public static final int UNKNOWN_ACQUISITION_PROFILE = 0;
	public static final int SURVEILLANCE_FACIAL_IMAGE = 1;
	public static final int DRIVER_LICENSE_IMAGE_AAMVA = 10;
	public static final int ANSI_FULL_FRONTAL_FACIAL_IMAGE = 11;
	public static final int ANSI_TOKEN_FACIAL_IMAGE = 12;
	public static final int ISO_FULL_FRONTAL_FACIAL_IMAGE_ISO_IEC_19794_5 = 13;
	public static final int ISO_TOKEN_FACIAL_IMAGE_ISO_IEC_19794_5 = 14;
	public static final int PIV_FACIAL_IMAGE_NIST_SP_800_76 = 15;
	public static final int LEGACY_MUGSHOT = 20;
	public static final int BEST_PRACTICE_APPLICATION_LEVEL_30 = 30;
	public static final int MOBILE_ID_BEST_PRACTICE_LEVEL_32 = 32;
	public static final int BEST_PRACTICE_APPLICATION_LEVEL_40 = 40;
	public static final int MOBILE_ID_BEST_PRACTICE_LEVEL_42 = 42;
	public static final int BEST_PRACTICE_APPLICATION_LEVEL_50 = 50;
	public static final int BEST_PRACTICE_APPLICATION_LEVEL_51 = 51;
	public static final int MOBILE_ID_BEST_PRACTICE_LEVEL_52 = 52;

	public static final Integer[] arrValues = new Integer[] { UNKNOWN_ACQUISITION_PROFILE, SURVEILLANCE_FACIAL_IMAGE,
			SURVEILLANCE_FACIAL_IMAGE, DRIVER_LICENSE_IMAGE_AAMVA, ANSI_FULL_FRONTAL_FACIAL_IMAGE,
			ANSI_TOKEN_FACIAL_IMAGE, ISO_FULL_FRONTAL_FACIAL_IMAGE_ISO_IEC_19794_5,
			ISO_TOKEN_FACIAL_IMAGE_ISO_IEC_19794_5, PIV_FACIAL_IMAGE_NIST_SP_800_76, LEGACY_MUGSHOT,
			BEST_PRACTICE_APPLICATION_LEVEL_30, MOBILE_ID_BEST_PRACTICE_LEVEL_32, BEST_PRACTICE_APPLICATION_LEVEL_40,
			MOBILE_ID_BEST_PRACTICE_LEVEL_42, BEST_PRACTICE_APPLICATION_LEVEL_50, BEST_PRACTICE_APPLICATION_LEVEL_51,
			MOBILE_ID_BEST_PRACTICE_LEVEL_52 };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(int value) {
		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException("SubjectAcquisitionProfileCodes value can be " + arrValues.toString()
				+ ", set value is wrong [" + value + "]");
	}
}