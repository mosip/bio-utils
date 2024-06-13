package io.mosip.kernel.biometrics.entities;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.Vector;

/**
 * Represents a list of additional entries associated with biometric data.
 *
 * This class serves as a container for a list of {@link Entry} objects. These
 * entries are used to store supplementary information beyond the core biometric
 * sample itself. The specific use case for these entries depends on the
 * application context.
 *
 */
@XmlRootElement
@XmlType(name = "OthersListType")
public class OthersList {
	/**
	 * List of entries containing additional information related to biometric data.
	 */
	@SuppressWarnings({ "java:S1004" })
	public List<Entry> entry;

	public OthersList() {
		entry = new Vector<>();
	}
}