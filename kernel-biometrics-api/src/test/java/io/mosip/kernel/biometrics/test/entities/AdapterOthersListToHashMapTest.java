package io.mosip.kernel.biometrics.test.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.entities.AdapterOthersListToHashMap;
import io.mosip.kernel.biometrics.entities.Entry;
import io.mosip.kernel.biometrics.entities.OthersList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for {@link AdapterOthersListToHashMap}.
 */
class AdapterOthersListToHashMapTest {

    private AdapterOthersListToHashMap adapter;

    @Mock
    private OthersList mockOthersList;

    @Mock
    private Entry mockEntry1;

    @Mock
    private Entry mockEntry2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new AdapterOthersListToHashMap();
    }

    /**
     * Tests default constructor creates adapter correctly.
     */
    @Test
    void defaultConstructorCreatesAdapterCorrectly() {
        AdapterOthersListToHashMap newAdapter = new AdapterOthersListToHashMap();
        assertNotNull(newAdapter);
    }

    /**
     * Tests unmarshal with empty OthersList returns empty map.
     */
    @Test
    void unmarshalEmptyOthersListReturnsEmptyMap() {
        OthersList othersList = new OthersList();
        othersList.entry = new ArrayList<>();

        Map<String, String> result = adapter.unmarshal(othersList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests unmarshal with single entry returns correct map.
     */
    @Test
    void unmarshalSingleEntryReturnsCorrectMap() {
        OthersList othersList = new OthersList();
        othersList.entry = new ArrayList<>();

        Entry entry = new Entry();
        entry.key = "key1";
        entry.value = "value1";
        othersList.entry.add(entry);

        Map<String, String> result = adapter.unmarshal(othersList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("value1", result.get("key1"));
    }

    /**
     * Tests unmarshal with multiple entries returns correct map.
     */
    @Test
    void unmarshalMultipleEntriesReturnsCorrectMap() {
        OthersList othersList = new OthersList();
        othersList.entry = new ArrayList<>();

        Entry entry1 = new Entry();
        entry1.key = "key1";
        entry1.value = "value1";

        Entry entry2 = new Entry();
        entry2.key = "key2";
        entry2.value = "value2";

        Entry entry3 = new Entry();
        entry3.key = "key3";
        entry3.value = "value3";

        othersList.entry.add(entry1);
        othersList.entry.add(entry2);
        othersList.entry.add(entry3);

        Map<String, String> result = adapter.unmarshal(othersList);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    /**
     * Tests unmarshal with null values in entries.
     */
    @Test
    void unmarshalWithNullValuesInEntries() {
        OthersList othersList = new OthersList();
        othersList.entry = new ArrayList<>();

        Entry entry1 = new Entry();
        entry1.key = "key1";
        entry1.value = null;

        Entry entry2 = new Entry();
        entry2.key = null;
        entry2.value = "value2";

        othersList.entry.add(entry1);
        othersList.entry.add(entry2);

        Map<String, String> result = adapter.unmarshal(othersList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get("key1"));
        assertEquals("value2", result.get(null));
    }

    /**
     * Tests marshal with null map returns null.
     */
    @Test
    void marshalNullMapReturnsNull() {
        OthersList result = adapter.marshal(null);
        assertNull(result);
    }

    /**
     * Tests marshal with empty map returns empty OthersList.
     */
    @Test
    void marshalEmptyMapReturnsEmptyOthersList() {
        Map<String, String> emptyMap = new HashMap<>();

        OthersList result = adapter.marshal(emptyMap);

        assertNotNull(result);
        assertNotNull(result.entry);
        assertTrue(result.entry.isEmpty());
    }

    /**
     * Tests marshal with single entry map returns correct OthersList.
     */
    @Test
    void marshalSingleEntryMapReturnsCorrectOthersList() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");

        OthersList result = adapter.marshal(map);

        assertNotNull(result);
        assertNotNull(result.entry);
        assertEquals(1, result.entry.size());

        Entry entry = result.entry.get(0);
        assertEquals("key1", entry.key);
        assertEquals("value1", entry.value);
    }

    /**
     * Tests marshal with multiple entries map returns correct OthersList.
     */
    @Test
    void marshalMultipleEntriesMapReturnsCorrectOthersList() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        OthersList result = adapter.marshal(map);

        assertNotNull(result);
        assertNotNull(result.entry);
        assertEquals(3, result.entry.size());

        Map<String, String> resultMap = new HashMap<>();
        for (Entry entry : result.entry) {
            resultMap.put(entry.key, entry.value);
        }

        assertEquals("value1", resultMap.get("key1"));
        assertEquals("value2", resultMap.get("key2"));
        assertEquals("value3", resultMap.get("key3"));
    }

    /**
     * Tests marshal with null values in map.
     */
    @Test
    void marshalWithNullValuesInMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", null);
        map.put(null, "value2");

        OthersList result = adapter.marshal(map);

        assertNotNull(result);
        assertNotNull(result.entry);
        assertEquals(2, result.entry.size());

        Map<String, String> resultMap = new HashMap<>();
        for (Entry entry : result.entry) {
            resultMap.put(entry.key, entry.value);
        }

        assertNull(resultMap.get("key1"));
        assertEquals("value2", resultMap.get(null));
    }

    /**
     * Tests marshal creates defensive copy of input map.
     */
    @Test
    void marshalCreatesDefensiveCopyOfInputMap() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");

        OthersList result = adapter.marshal(originalMap);

        originalMap.put("key2", "value2");

        assertEquals(1, result.entry.size());
        assertEquals("key1", result.entry.get(0).key);
        assertEquals("value1", result.entry.get(0).value);
    }

    /**
     * Tests round-trip conversion maintains data integrity.
     */
    @Test
    void roundTripConversionMaintainsDataIntegrity() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        originalMap.put("key3", "value3");

        OthersList othersList = adapter.marshal(originalMap);
        Map<String, String> resultMap = adapter.unmarshal(othersList);

        assertEquals(originalMap.size(), resultMap.size());
        assertEquals(originalMap.get("key1"), resultMap.get("key1"));
        assertEquals(originalMap.get("key2"), resultMap.get("key2"));
        assertEquals(originalMap.get("key3"), resultMap.get("key3"));
    }

    /**
     * Tests unmarshal returns new HashMap instance.
     */
    @Test
    void unmarshalReturnsNewHashMapInstance() {
        OthersList othersList = new OthersList();
        othersList.entry = new ArrayList<>();

        Map<String, String> result1 = adapter.unmarshal(othersList);
        Map<String, String> result2 = adapter.unmarshal(othersList);

        assertNotSame(result1, result2);
    }

    /**
     * Tests marshal returns new OthersList instance.
     */
    @Test
    void marshalReturnsNewOthersListInstance() {
        Map<String, String> map = new HashMap<>();

        OthersList result1 = adapter.marshal(map);
        OthersList result2 = adapter.marshal(map);

        assertNotSame(result1, result2);
        assertNotSame(result1.entry, result2.entry);
    }

    /**
     * Tests unmarshal with empty string keys and values.
     */
    @Test
    void unmarshalWithEmptyStringKeysAndValues() {
        OthersList othersList = new OthersList();
        othersList.entry = new ArrayList<>();

        Entry entry1 = new Entry();
        entry1.key = "";
        entry1.value = "value1";

        Entry entry2 = new Entry();
        entry2.key = "key2";
        entry2.value = "";

        othersList.entry.add(entry1);
        othersList.entry.add(entry2);

        Map<String, String> result = adapter.unmarshal(othersList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get(""));
        assertEquals("", result.get("key2"));
    }

    /**
     * Tests marshal with empty string keys and values.
     */
    @Test
    void marshalWithEmptyStringKeysAndValues() {
        Map<String, String> map = new HashMap<>();
        map.put("", "value1");
        map.put("key2", "");

        OthersList result = adapter.marshal(map);

        assertNotNull(result);
        assertEquals(2, result.entry.size());

        Map<String, String> resultMap = new HashMap<>();
        for (Entry entry : result.entry) {
            resultMap.put(entry.key, entry.value);
        }

        assertEquals("value1", resultMap.get(""));
        assertEquals("", resultMap.get("key2"));
    }

    /**
     * Tests adapter extends XmlAdapter correctly.
     */
    @Test
    void adapterExtendsXmlAdapterCorrectly() {
        assertTrue(adapter instanceof javax.xml.bind.annotation.adapters.XmlAdapter);
    }
}