package io.mosip.kernel.biometrics.entities;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * XML adapter to convert between {@link OthersList} and {@link Map} of
 * {@link String} to {@link String}.
 * <p>
 * This adapter is used for marshalling and unmarshalling XML content between
 * {@link OthersList} and {@link Map} where {@link OthersList} represents a
 * structured XML element containing key-value pairs and is transformed into a
 * {@link Map} of {@link String} keys to {@link String} values and vice versa.
 * </p>
 * 
 * <p>
 * The adapter ensures conversion by iterating through the list of {@link Entry}
 * objects in {@link OthersList} and mapping them to a {@link Map} during
 * unmarshalling. Conversely, during marshalling, it iterates through the
 * {@link Map} and creates corresponding {@link Entry} objects within
 * {@link OthersList}.
 * </p>
 * 
 * <p>
 * This adapter facilitates seamless transformation between XML representation
 * and Java objects, suitable for scenarios where XML data needs to be mapped to
 * and from {@link Map} structures.
 * </p>
 * 
 * @see XmlAdapter
 * @see OthersList
 * @see Entry
 * @since 1.0.0
 */
public class AdapterOthersListToHashMap extends XmlAdapter<OthersList, Map<String, String>> {
	/**
	 * Default constructor for AdapterOthersListToHashMap.
	 */
	public AdapterOthersListToHashMap() {
		super();
	}

	/**
	 * Converts an {@link OthersList} instance to a {@link Map} of {@link String}
	 * keys to {@link String} values.
	 * 
	 * @param v the {@link OthersList} to unmarshal
	 * @return a {@link Map} containing key-value pairs extracted from the
	 *         {@link OthersList}
	 */
	public Map<String, String> unmarshal(OthersList v) {
		Map<String, String> aHashMap = new HashMap<>();
		int cnt = v.entry.size();
		for (int i = 0; i < cnt; i++) {
			Entry tmpE = v.entry.get(i);
			aHashMap.put(tmpE.key, tmpE.value);
		}
		return aHashMap;
	}

	/**
	 * Converts a {@link Map} of {@link String} keys to {@link String} values into
	 * an {@link OthersList} instance.
	 * 
	 * @param v the {@link Map} to marshal
	 * @return an {@link OthersList} containing {@link Entry} objects created from
	 *         the {@link Map}
	 */
	public OthersList marshal(Map<String, String> v) {
		if (v == null)
			return null;

		OthersList pList = new OthersList();
		Map<String, String> tMap = new HashMap<>(v);
		for (Iterator<String> i = tMap.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			pList.entry.add(new Entry(key, tMap.get(key)));
		}
		return pList;
	}
}