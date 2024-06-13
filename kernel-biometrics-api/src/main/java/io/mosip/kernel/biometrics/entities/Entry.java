package io.mosip.kernel.biometrics.entities;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Represents a key-value pair for storing additional information related to
 * biometric data.
 *
 * This class defines a simple structure to hold supplementary data associated
 * with a biometric sample. It consists of a `key` attribute that identifies the
 * data element and a `value` element that stores the corresponding information.
 *
 */
public class Entry implements Serializable {
	/**
	 * Key that identifies the specific data element.
	 */
	@XmlAttribute
	public String key;

	/**
	 * Value associated with the key, representing the additional information.
	 */
	@XmlValue
	public String value;

	public Entry() {
	}

	public Entry(String tKey, String tValue) {
		key = tKey;
		value = tValue;
	}
}