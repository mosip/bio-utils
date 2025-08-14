package io.mosip.kernel.biometrics.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BIRInfo;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.entities.VersionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Unit tests for {@link BiometricRecord}.
 */
class BiometricRecordTest {

    @Mock
    private VersionType mockVersion;

    @Mock
    private VersionType mockCbeffVersion;

    @Mock
    private BIRInfo mockBirInfo;

    @Mock
    private BIR mockBir;

    private BiometricRecord biometricRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        biometricRecord = new BiometricRecord();
    }

    /**
     * Tests default constructor initializes all fields correctly.
     */
    @Test
    void defaultConstructorInitializesFieldsCorrectly() {
        assertNull(biometricRecord.getVersion());
        assertNull(biometricRecord.getCbeffversion());
        assertNull(biometricRecord.getBirInfo());
        assertNotNull(biometricRecord.getSegments());
        assertTrue(biometricRecord.getSegments().isEmpty());
        assertNotNull(biometricRecord.getOthers());
        assertTrue(biometricRecord.getOthers().isEmpty());
    }

    /**
     * Tests parameterized constructor sets fields correctly.
     */
    @Test
    void parameterizedConstructorSetsFieldsCorrectly() {
        BiometricRecord record = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);

        assertEquals(mockVersion, record.getVersion());
        assertEquals(mockCbeffVersion, record.getCbeffversion());
        assertEquals(mockBirInfo, record.getBirInfo());
        assertNotNull(record.getSegments());
        assertTrue(record.getSegments().isEmpty());
        assertNotNull(record.getOthers());
        assertTrue(record.getOthers().isEmpty());
    }

    /**
     * Tests setter and getter for version field.
     */
    @Test
    void versionSetterGetterWorksCorrectly() {
        biometricRecord.setVersion(mockVersion);
        assertEquals(mockVersion, biometricRecord.getVersion());
    }

    /**
     * Tests setter and getter for cbeffversion field.
     */
    @Test
    void cbeffVersionSetterGetterWorksCorrectly() {
        biometricRecord.setCbeffversion(mockCbeffVersion);
        assertEquals(mockCbeffVersion, biometricRecord.getCbeffversion());
    }

    /**
     * Tests setter and getter for birInfo field.
     */
    @Test
    void birInfoSetterGetterWorksCorrectly() {
        biometricRecord.setBirInfo(mockBirInfo);
        assertEquals(mockBirInfo, biometricRecord.getBirInfo());
    }

    /**
     * Tests setter and getter for segments field.
     */
    @Test
    void segmentsSetterGetterWorksCorrectly() {
        List<BIR> segments = new ArrayList<>();
        segments.add(mockBir);

        biometricRecord.setSegments(segments);
        assertEquals(segments, biometricRecord.getSegments());
        assertEquals(1, biometricRecord.getSegments().size());
        assertEquals(mockBir, biometricRecord.getSegments().get(0));
    }

    /**
     * Tests setter and getter for others field.
     */
    @Test
    void othersSetterGetterWorksCorrectly() {
        HashMap<String, String> others = new HashMap<>();
        others.put("key1", "value1");
        others.put("key2", "value2");

        biometricRecord.setOthers(others);
        assertEquals(others, biometricRecord.getOthers());
        assertEquals(2, biometricRecord.getOthers().size());
        assertEquals("value1", biometricRecord.getOthers().get("key1"));
        assertEquals("value2", biometricRecord.getOthers().get("key2"));
    }

    /**
     * Tests adding items to segments list.
     */
    @Test
    void addingItemsToSegmentsListWorksCorrectly() {
        biometricRecord.getSegments().add(mockBir);

        assertEquals(1, biometricRecord.getSegments().size());
        assertEquals(mockBir, biometricRecord.getSegments().get(0));
    }

    /**
     * Tests adding items to others map.
     */
    @Test
    void addingItemsToOthersMapWorksCorrectly() {
        biometricRecord.getOthers().put("customKey", "customValue");

        assertEquals(1, biometricRecord.getOthers().size());
        assertEquals("customValue", biometricRecord.getOthers().get("customKey"));
    }

    /**
     * Tests that segments list is mutable.
     */
    @Test
    void segmentsListIsMutable() {
        List<BIR> originalSegments = biometricRecord.getSegments();
        originalSegments.add(mockBir);

        assertEquals(1, biometricRecord.getSegments().size());
        assertTrue(biometricRecord.getSegments().contains(mockBir));
    }

    /**
     * Tests that others map is mutable.
     */
    @Test
    void othersMapIsMutable() {
        HashMap<String, String> originalOthers = biometricRecord.getOthers();
        originalOthers.put("testKey", "testValue");

        assertEquals(1, biometricRecord.getOthers().size());
        assertTrue(biometricRecord.getOthers().containsKey("testKey"));
        assertEquals("testValue", biometricRecord.getOthers().get("testKey"));
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(biometricRecord.equals(biometricRecord));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(biometricRecord.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(biometricRecord.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        BiometricRecord record1 = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);
        BiometricRecord record2 = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);

        assertTrue(record1.equals(record2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        int hash1 = biometricRecord.hashCode();
        int hash2 = biometricRecord.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        BiometricRecord record1 = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);
        BiometricRecord record2 = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);

        assertEquals(record1.hashCode(), record2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = biometricRecord.toString();
        assertNotNull(result);
        assertTrue(result.contains("BiometricRecord"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        biometricRecord.setVersion(mockVersion);
        biometricRecord.setCbeffversion(mockCbeffVersion);
        biometricRecord.setBirInfo(mockBirInfo);

        String result = biometricRecord.toString();
        assertNotNull(result);
        assertTrue(result.contains("version"));
        assertTrue(result.contains("cbeffversion"));
        assertTrue(result.contains("birInfo"));
        assertTrue(result.contains("segments"));
        assertTrue(result.contains("others"));
    }

    /**
     * Tests serialization compatibility by checking serialVersionUID.
     */
    @Test
    void serialVersionUidIsCorrect() throws Exception {
        java.lang.reflect.Field field = BiometricRecord.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        long actualSerialVersionUID = (Long) field.get(null);
        assertEquals(3688026570163725740L, actualSerialVersionUID);
    }

    /**
     * Tests that default constructor creates new instances for collections.
     */
    @Test
    void defaultConstructorCreatesNewInstances() {
        BiometricRecord record1 = new BiometricRecord();
        BiometricRecord record2 = new BiometricRecord();

        assertNotSame(record1.getSegments(), record2.getSegments());
        assertNotSame(record1.getOthers(), record2.getOthers());
    }

    /**
     * Tests that parameterized constructor creates new instances for collections.
     */
    @Test
    void parameterizedConstructorCreatesNewInstances() {
        BiometricRecord record1 = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);
        BiometricRecord record2 = new BiometricRecord(mockVersion, mockCbeffVersion, mockBirInfo);

        assertNotSame(record1.getSegments(), record2.getSegments());
        assertNotSame(record1.getOthers(), record2.getOthers());
    }

    /**
     * Tests setting null values for all fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        biometricRecord.setVersion(null);
        biometricRecord.setCbeffversion(null);
        biometricRecord.setBirInfo(null);
        biometricRecord.setSegments(null);
        biometricRecord.setOthers(null);

        assertNull(biometricRecord.getVersion());
        assertNull(biometricRecord.getCbeffversion());
        assertNull(biometricRecord.getBirInfo());
        assertNull(biometricRecord.getSegments());
        assertNull(biometricRecord.getOthers());
    }

    /**
     * Tests that BiometricRecord implements Serializable.
     */
    @Test
    void implementsSerializable() {
        assertTrue(biometricRecord instanceof java.io.Serializable);
    }
}