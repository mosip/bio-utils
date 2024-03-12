package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class DeviceMonitoringModes {
	/**
	 *  Operator physically controls the subject to acquire the biometric sample
	 */
	public static final String CONTROLLED = "CONTROLLED";
	/**
	 *  Person available to provide assistance to subject submitting the biometric
	 */
	public static final String ASSISTED = "ASSISTED";
	/**
	 *  Person present to observe operation of the device but provides no assistance
	 */
	public static final String OBSERVED = "OBSERVED";
	/**
	 *  No one is present to observe or provide assistance
	 */
	public static final String UNATTENDED = "UNATTENDED";
	/**
	 *  No information is known
	 */
	public static final String UNKNOWN = "UNKNOWN";

	public static final String[] arrValues = new String[] { CONTROLLED, ASSISTED, OBSERVED, UNATTENDED, UNKNOWN };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"DeviceMonitoringModes value can be " + Arrays.toString(arrValues) + ", set value is wrong [" + value + "]");
	}
}