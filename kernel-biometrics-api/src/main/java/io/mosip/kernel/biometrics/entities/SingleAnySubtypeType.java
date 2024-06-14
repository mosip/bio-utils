package io.mosip.kernel.biometrics.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SingleAnySubtypeType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="SingleAnySubtypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Left"/>
 *     &lt;enumeration value="Right"/>
 *     &lt;enumeration value="Thumb"/>
 *     &lt;enumeration value="IndexFinger"/>
 *     &lt;enumeration value="MiddleFinger"/>
 *     &lt;enumeration value="RingFinger"/>
 *     &lt;enumeration value="LittleFinger"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SingleAnySubtypeType")
@XmlEnum
public enum SingleAnySubtypeType {
	/**
	 * Left: Indicates the left version of a specific finger (e.g., left thumb).
	 */
	@XmlEnumValue("Left")
	LEFT("Left"),

	/**
	 * Right: Indicates the right version of a specific finger (e.g., right index
	 * finger).
	 */
	@XmlEnumValue("Right")
	RIGHT("Right"),

	/**
	 * Thumb: Refers to the thumb finger.
	 */
	@XmlEnumValue("Thumb")
	THUMB("Thumb"),

	/**
	 * IndexFinger: Refers to the index finger.
	 */
	@XmlEnumValue("IndexFinger")
	INDEX_FINGER("IndexFinger"),

	/**
	 * MiddleFinger: Refers to the middle finger.
	 */
	@XmlEnumValue("MiddleFinger")
	MIDDLE_FINGER("MiddleFinger"),

	/**
	 * RingFinger: Refers to the ring finger.
	 */
	@XmlEnumValue("RingFinger")
	RING_FINGER("RingFinger"),

	/**
	 * LittleFinger: Refers to the little finger (pinky).
	 */
	@XmlEnumValue("LittleFinger")
	LITTLE_FINGER("LittleFinger");

	private final String value;

	SingleAnySubtypeType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static SingleAnySubtypeType fromValue(String v) {
		for (SingleAnySubtypeType c : SingleAnySubtypeType.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}