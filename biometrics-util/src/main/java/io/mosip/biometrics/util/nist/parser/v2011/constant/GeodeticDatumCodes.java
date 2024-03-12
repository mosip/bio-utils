package io.mosip.biometrics.util.nist.parser.v2011.constant;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class GeodeticDatumCodes {
	public static final String AIRY = "AIRY";// Airy
	public static final String AUST = "AUST";// Australian National
	public static final String BES = "BES";// Bessel 1841
	public static final String BESN = "BESN";// Bessel 1841 (Namibia)
	public static final String CLK66 = "CLK66";// Clarke 1866
	public static final String CLK80 = "CLK80";// Clarke 1880
	public static final String EVER = "EVER";// Everest
	public static final String FIS60 = "FIS60";// Fischer 1960 (Mercury)
	public static final String FIS68 = "FIS68";// Fischer 1968
	public static final String GRS67 = "GRS67";// GRS 1967
	public static final String HELM = "HELM";// Helmert 1906
	public static final String HOUG = "HOUG";// Hough
	public static final String INT = "INT";// International
	public static final String KRAS = "KRAS";// Krassovsky
	public static final String AIRYM = "AIRYM";// Modified Airy
	public static final String EVERM = "EVERM";// Modified Everest
	public static final String FIS60M = "FIS60M";// Modified Fischer 1960
	public static final String SA69 = "SA69";// South American 1969
	public static final String WGS60 = "WGS60";// WGS-60
	public static final String WGS66 = "WGS66";// WGS-66
	public static final String WGS72 = "WGS72";// WGS-72
	public static final String WGS84 = "WGS84";// WGS-84 / NAD-83
	/**
	 *  <entry up to 6 characters> Other
	 */

	public static final String[] arrValues = new String[] { AIRY, AUST, BES, BESN, CLK66, CLK80, CLK80, EVER, FIS60,
			FIS68, GRS67, HELM, HOUG, INT, KRAS, AIRYM, EVERM, FIS60M, SA69, WGS60, WGS66, WGS72, WGS84 };

	@JacksonXmlText
	private String value;

	@JsonCreator
	public static String fromValue(String value) {

		if (ArrayUtils.contains(arrValues, value))
			return value;
		throw new IllegalArgumentException(
				"GeodeticDatumCodes value can be " + Arrays.toString(arrValues) + ", set value is wrong [" + value + "]");
	}
}