package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class FrictionRidgeImpressionTypes {
	/**
	 * Exemplar Prints
	 */
	/**
	 * Contact Impressions
	 */	
	/**
	 * Finger(s) presented on platen or paper without rolling
	 */
	public static final int PLAIN_CONTACT = 0;
	/**
	 * Finger rolled on platen or paper
	 */
	public static final int ROLLED_CONTACT = 1;
	/**
	 * Finger swiped on platen
	 */
	public static final int LIVE_SCAN_SWIPE = 8;
	/**
	 * Contactless Acquistions
	 */
	/*
	 * 	Finger(s) / palm / plantar presented stationary, in view of a stationary sensor and sensor captures plain contact equivalent.
	 */
	public static final int PLAIN_CONTACTLESS_STATIONARY_SUBJECT = 24;
	/**
	 * Finger(s) / palm / plantar presented stationary, in view of a stationary sensor and sensor captures rolled equivalent.
	 */
	public static final int ROLLED_CONTACTLESS_STATIONARY_SUBJECT = 25;
	/**
	 * Finger(s) / palm / plantar move through the capture volume of a sensor and sensor captures rolled equivalent.
	 */
	public static final int ROLLED_CONTACTLESS_MOVING_SUBJECT = 41;
	/**
	 * Finger(s) / palm / plantar move through the capture volume of a sensor and sensor captures plain equivalent.
	 */
	public static final int PLAIN_CONTACTLESS_MOVING_SUBJECT = 42;		
	/**
	 * System integration exceptions
	 */
	public static final int OTHER = 28;
	/**
	 * Unknown
	 */
	public static final int UNKNOWN = 29;

	/*
	 * Latent prints
	 */
	/*
	 * Image or impression of friction skin deposited on a surface
	 */
	public static final int LATENT_IMAGE = 4;
	
	
	public static final Integer[] arrValues = new Integer[] { PLAIN_CONTACT, ROLLED_CONTACT, LIVE_SCAN_SWIPE, PLAIN_CONTACTLESS_STATIONARY_SUBJECT, ROLLED_CONTACTLESS_STATIONARY_SUBJECT, ROLLED_CONTACTLESS_MOVING_SUBJECT, PLAIN_CONTACTLESS_MOVING_SUBJECT, OTHER, LATENT_IMAGE };

	@JacksonXmlText
	private Integer value;

	@JsonCreator
	public static Integer fromValue(Integer value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException("FrictionRidgeImpressionTypes value can be " + Arrays.toString(arrValues)
				+ ", set value is wrong [" + value + "]");
	}
}