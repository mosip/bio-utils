package io.mosip.kernel.biometrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.model.Decision;
import io.mosip.kernel.biometrics.model.MatchDecision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;

/**
 * Unit tests for {@link MatchDecision}.
 */
class MatchDecisionTest {

    @Mock
    private Decision mockDecision1;

    @Mock
    private Decision mockDecision2;

    private MatchDecision matchDecision;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        matchDecision = new MatchDecision(5);
    }

    /**
     * Tests constructor initializes all fields correctly.
     */
    @Test
    void constructorInitializesFieldsCorrectly() {
        MatchDecision newMatchDecision = new MatchDecision(10);

        assertEquals(10, newMatchDecision.getGalleryIndex());
        assertNotNull(newMatchDecision.getDecisions());
        assertTrue(newMatchDecision.getDecisions().isEmpty());
        assertTrue(newMatchDecision.getDecisions() instanceof EnumMap);
        assertNotNull(newMatchDecision.getAnalyticsInfo());
        assertTrue(newMatchDecision.getAnalyticsInfo().isEmpty());
        assertTrue(newMatchDecision.getAnalyticsInfo() instanceof HashMap);
    }

    /**
     * Tests setter and getter for galleryIndex field.
     */
    @Test
    void galleryIndexSetterGetterWorksCorrectly() {
        matchDecision.setGalleryIndex(15);
        assertEquals(15, matchDecision.getGalleryIndex());
    }

    /**
     * Tests setter and getter for decisions field.
     */
    @Test
    void decisionsSetterGetterWorksCorrectly() {
        Map<BiometricType, Decision> decisions = new EnumMap<>(BiometricType.class);
        decisions.put(BiometricType.FINGER, mockDecision1);
        decisions.put(BiometricType.FACE, mockDecision2);

        matchDecision.setDecisions(decisions);
        assertEquals(decisions, matchDecision.getDecisions());
        assertEquals(2, matchDecision.getDecisions().size());
        assertEquals(mockDecision1, matchDecision.getDecisions().get(BiometricType.FINGER));
        assertEquals(mockDecision2, matchDecision.getDecisions().get(BiometricType.FACE));
    }

    /**
     * Tests setter and getter for analyticsInfo field.
     */
    @Test
    void analyticsInfoSetterGetterWorksCorrectly() {
        Map<String, String> analyticsInfo = new HashMap<>();
        analyticsInfo.put("confidence", "95.7");
        analyticsInfo.put("processingTime", "180ms");
        analyticsInfo.put("algorithm", "minutiae_based");

        matchDecision.setAnalyticsInfo(analyticsInfo);
        assertEquals(analyticsInfo, matchDecision.getAnalyticsInfo());
        assertEquals(3, matchDecision.getAnalyticsInfo().size());
        assertEquals("95.7", matchDecision.getAnalyticsInfo().get("confidence"));
        assertEquals("180ms", matchDecision.getAnalyticsInfo().get("processingTime"));
        assertEquals("minutiae_based", matchDecision.getAnalyticsInfo().get("algorithm"));
    }

    /**
     * Tests adding items to decisions map.
     */
    @Test
    void addingItemsToDecisionsMapWorksCorrectly() {
        matchDecision.getDecisions().put(BiometricType.IRIS, mockDecision1);
        matchDecision.getDecisions().put(BiometricType.VOICE, mockDecision2);

        assertEquals(2, matchDecision.getDecisions().size());
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.IRIS));
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.VOICE));
        assertEquals(mockDecision1, matchDecision.getDecisions().get(BiometricType.IRIS));
        assertEquals(mockDecision2, matchDecision.getDecisions().get(BiometricType.VOICE));
    }

    /**
     * Tests adding items to analyticsInfo map.
     */
    @Test
    void addingItemsToAnalyticsInfoMapWorksCorrectly() {
        matchDecision.getAnalyticsInfo().put("quality", "high");
        matchDecision.getAnalyticsInfo().put("threshold", "0.85");
        matchDecision.getAnalyticsInfo().put("matches", "3");

        assertEquals(3, matchDecision.getAnalyticsInfo().size());
        assertEquals("high", matchDecision.getAnalyticsInfo().get("quality"));
        assertEquals("0.85", matchDecision.getAnalyticsInfo().get("threshold"));
        assertEquals("3", matchDecision.getAnalyticsInfo().get("matches"));
    }

    /**
     * Tests that decisions map is mutable.
     */
    @Test
    void decisionsMapIsMutable() {
        Map<BiometricType, Decision> originalDecisions = matchDecision.getDecisions();
        originalDecisions.put(BiometricType.FACE, mockDecision1);

        assertEquals(1, matchDecision.getDecisions().size());
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.FACE));
    }

    /**
     * Tests that analyticsInfo map is mutable.
     */
    @Test
    void analyticsInfoMapIsMutable() {
        Map<String, String> originalAnalytics = matchDecision.getAnalyticsInfo();
        originalAnalytics.put("performanceMetric", "excellent");

        assertEquals(1, matchDecision.getAnalyticsInfo().size());
        assertTrue(matchDecision.getAnalyticsInfo().containsKey("performanceMetric"));
        assertEquals("excellent", matchDecision.getAnalyticsInfo().get("performanceMetric"));
    }

    /**
     * Tests setting null values for fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision.getAnalyticsInfo().put("key", "value");

        matchDecision.setDecisions(null);
        matchDecision.setAnalyticsInfo(null);

        assertNull(matchDecision.getDecisions());
        assertNull(matchDecision.getAnalyticsInfo());
    }

    /**
     * Tests galleryIndex with negative values.
     */
    @Test
    void galleryIndexWithNegativeValuesWorksCorrectly() {
        matchDecision.setGalleryIndex(-1);
        assertEquals(-1, matchDecision.getGalleryIndex());
    }

    /**
     * Tests galleryIndex with zero value.
     */
    @Test
    void galleryIndexWithZeroValueWorksCorrectly() {
        matchDecision.setGalleryIndex(0);
        assertEquals(0, matchDecision.getGalleryIndex());
    }

    /**
     * Tests galleryIndex with large values.
     */
    @Test
    void galleryIndexWithLargeValuesWorksCorrectly() {
        matchDecision.setGalleryIndex(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, matchDecision.getGalleryIndex());
    }

    /**
     * Tests replacing entire decisions map.
     */
    @Test
    void replacingEntireDecisionsMapWorksCorrectly() {
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        assertEquals(1, matchDecision.getDecisions().size());

        Map<BiometricType, Decision> newDecisions = new EnumMap<>(BiometricType.class);
        newDecisions.put(BiometricType.IRIS, mockDecision2);
        newDecisions.put(BiometricType.FACE, mockDecision1);
        matchDecision.setDecisions(newDecisions);

        assertEquals(2, matchDecision.getDecisions().size());
        assertFalse(matchDecision.getDecisions().containsKey(BiometricType.FINGER));
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.IRIS));
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.FACE));
    }

    /**
     * Tests replacing entire analyticsInfo map.
     */
    @Test
    void replacingEntireAnalyticsInfoMapWorksCorrectly() {
        matchDecision.getAnalyticsInfo().put("initial", "value");
        assertEquals(1, matchDecision.getAnalyticsInfo().size());

        Map<String, String> newAnalytics = new HashMap<>();
        newAnalytics.put("new1", "value1");
        newAnalytics.put("new2", "value2");
        matchDecision.setAnalyticsInfo(newAnalytics);

        assertEquals(2, matchDecision.getAnalyticsInfo().size());
        assertFalse(matchDecision.getAnalyticsInfo().containsKey("initial"));
        assertTrue(matchDecision.getAnalyticsInfo().containsKey("new1"));
        assertTrue(matchDecision.getAnalyticsInfo().containsKey("new2"));
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(matchDecision.equals(matchDecision));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(matchDecision.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(matchDecision.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        MatchDecision matchDecision1 = new MatchDecision(7);
        matchDecision1.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision1.getAnalyticsInfo().put("key", "value");

        MatchDecision matchDecision2 = new MatchDecision(7);
        matchDecision2.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision2.getAnalyticsInfo().put("key", "value");

        assertTrue(matchDecision1.equals(matchDecision2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        matchDecision.setGalleryIndex(8);
        int hash1 = matchDecision.hashCode();
        int hash2 = matchDecision.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        MatchDecision matchDecision1 = new MatchDecision(12);
        MatchDecision matchDecision2 = new MatchDecision(12);

        assertEquals(matchDecision1.hashCode(), matchDecision2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = matchDecision.toString();
        assertNotNull(result);
        assertTrue(result.contains("MatchDecision"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        matchDecision.setGalleryIndex(25);
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision.getAnalyticsInfo().put("testKey", "testValue");

        String result = matchDecision.toString();
        assertNotNull(result);
        assertTrue(result.contains("galleryIndex"));
        assertTrue(result.contains("decisions"));
        assertTrue(result.contains("analyticsInfo"));
    }

    /**
     * Tests that constructor creates new instances for collections.
     */
    @Test
    void constructorCreatesNewInstancesForCollections() {
        MatchDecision matchDecision1 = new MatchDecision(1);
        MatchDecision matchDecision2 = new MatchDecision(2);

        assertNotSame(matchDecision1.getDecisions(), matchDecision2.getDecisions());
        assertNotSame(matchDecision1.getAnalyticsInfo(), matchDecision2.getAnalyticsInfo());
    }

    /**
     * Tests clearing decisions map.
     */
    @Test
    void clearingDecisionsMapWorksCorrectly() {
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision.getDecisions().put(BiometricType.IRIS, mockDecision2);
        assertEquals(2, matchDecision.getDecisions().size());

        matchDecision.getDecisions().clear();
        assertEquals(0, matchDecision.getDecisions().size());
        assertTrue(matchDecision.getDecisions().isEmpty());
    }

    /**
     * Tests clearing analyticsInfo map.
     */
    @Test
    void clearingAnalyticsInfoMapWorksCorrectly() {
        matchDecision.getAnalyticsInfo().put("key1", "value1");
        matchDecision.getAnalyticsInfo().put("key2", "value2");
        assertEquals(2, matchDecision.getAnalyticsInfo().size());

        matchDecision.getAnalyticsInfo().clear();
        assertEquals(0, matchDecision.getAnalyticsInfo().size());
        assertTrue(matchDecision.getAnalyticsInfo().isEmpty());
    }

    /**
     * Tests removing items from decisions map.
     */
    @Test
    void removingItemsFromDecisionsMapWorksCorrectly() {
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision.getDecisions().put(BiometricType.IRIS, mockDecision2);
        matchDecision.getDecisions().put(BiometricType.FACE, mockDecision1);

        matchDecision.getDecisions().remove(BiometricType.IRIS);
        assertEquals(2, matchDecision.getDecisions().size());
        assertFalse(matchDecision.getDecisions().containsKey(BiometricType.IRIS));
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.FINGER));
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.FACE));
    }

    /**
     * Tests removing items from analyticsInfo map.
     */
    @Test
    void removingItemsFromAnalyticsInfoMapWorksCorrectly() {
        matchDecision.getAnalyticsInfo().put("key1", "value1");
        matchDecision.getAnalyticsInfo().put("key2", "value2");
        matchDecision.getAnalyticsInfo().put("key3", "value3");

        matchDecision.getAnalyticsInfo().remove("key2");
        assertEquals(2, matchDecision.getAnalyticsInfo().size());
        assertFalse(matchDecision.getAnalyticsInfo().containsKey("key2"));
        assertTrue(matchDecision.getAnalyticsInfo().containsKey("key1"));
        assertTrue(matchDecision.getAnalyticsInfo().containsKey("key3"));
    }

    /**
     * Tests complete match decision object setup.
     */
    @Test
    void completeMatchDecisionObjectSetup() {
        matchDecision.setGalleryIndex(42);
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision.getDecisions().put(BiometricType.IRIS, mockDecision2);
        matchDecision.getAnalyticsInfo().put("overallConfidence", "92.3");
        matchDecision.getAnalyticsInfo().put("totalProcessingTime", "350ms");
        matchDecision.getAnalyticsInfo().put("matchingAlgorithm", "hybrid");

        assertEquals(42, matchDecision.getGalleryIndex());
        assertEquals(2, matchDecision.getDecisions().size());
        assertEquals(3, matchDecision.getAnalyticsInfo().size());

        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.FINGER));
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.IRIS));
        assertEquals("92.3", matchDecision.getAnalyticsInfo().get("overallConfidence"));
        assertEquals("350ms", matchDecision.getAnalyticsInfo().get("totalProcessingTime"));
        assertEquals("hybrid", matchDecision.getAnalyticsInfo().get("matchingAlgorithm"));
    }

    /**
     * Tests EnumMap specific behavior for decisions.
     */
    @Test
    void enumMapSpecificBehaviorForDecisions() {
        matchDecision.getDecisions().put(BiometricType.VOICE, mockDecision1);
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision2);
        matchDecision.getDecisions().put(BiometricType.FACE, mockDecision1);

        assertEquals(3, matchDecision.getDecisions().size());

        for (BiometricType type : BiometricType.values()) {
            matchDecision.getDecisions().put(type, mockDecision1);
        }

        assertEquals(BiometricType.values().length, matchDecision.getDecisions().size());
    }

    /**
     * Tests analyticsInfo with empty strings.
     */
    @Test
    void analyticsInfoWithEmptyStringsWorksCorrectly() {
        matchDecision.getAnalyticsInfo().put("", "emptyKey");
        matchDecision.getAnalyticsInfo().put("emptyValue", "");
        matchDecision.getAnalyticsInfo().put("valid", "data");

        assertEquals(3, matchDecision.getAnalyticsInfo().size());
        assertEquals("emptyKey", matchDecision.getAnalyticsInfo().get(""));
        assertEquals("", matchDecision.getAnalyticsInfo().get("emptyValue"));
        assertEquals("data", matchDecision.getAnalyticsInfo().get("valid"));
    }

    /**
     * Tests match decision state after multiple modifications.
     */
    @Test
    void matchDecisionStateAfterMultipleModifications() {
        matchDecision.setGalleryIndex(100);
        matchDecision.getDecisions().put(BiometricType.FINGER, mockDecision1);
        matchDecision.getAnalyticsInfo().put("initial", "value");

        matchDecision.setGalleryIndex(200);
        matchDecision.getDecisions().put(BiometricType.IRIS, mockDecision2);
        matchDecision.getDecisions().remove(BiometricType.FINGER);
        matchDecision.getAnalyticsInfo().put("updated", "newValue");
        matchDecision.getAnalyticsInfo().remove("initial");

        assertEquals(200, matchDecision.getGalleryIndex());
        assertEquals(1, matchDecision.getDecisions().size());
        assertTrue(matchDecision.getDecisions().containsKey(BiometricType.IRIS));
        assertFalse(matchDecision.getDecisions().containsKey(BiometricType.FINGER));
        assertEquals(1, matchDecision.getAnalyticsInfo().size());
        assertEquals("newValue", matchDecision.getAnalyticsInfo().get("updated"));
        assertFalse(matchDecision.getAnalyticsInfo().containsKey("initial"));
    }
}