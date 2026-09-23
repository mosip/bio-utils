package io.mosip.kernel.biometrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.model.QualityCheck;
import io.mosip.kernel.biometrics.model.QualityScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricType;

/**
 * Unit tests for {@link QualityCheck}.
 */
class QualityCheckTest {

    @Mock
    private QualityScore mockQualityScore1;

    @Mock
    private QualityScore mockQualityScore2;

    private QualityCheck qualityCheck;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        qualityCheck = new QualityCheck();
    }

    /**
     * Tests default constructor initializes all fields correctly.
     */
    @Test
    void defaultConstructorInitializesFieldsCorrectly() {
        QualityCheck newQualityCheck = new QualityCheck();

        assertNotNull(newQualityCheck.getScores());
        assertTrue(newQualityCheck.getScores().isEmpty());
        assertTrue(newQualityCheck.getScores() instanceof EnumMap);
        assertNotNull(newQualityCheck.getAnalyticsInfo());
        assertTrue(newQualityCheck.getAnalyticsInfo().isEmpty());
        assertTrue(newQualityCheck.getAnalyticsInfo() instanceof HashMap);
    }

    /**
     * Tests setter and getter for scores field.
     */
    @Test
    void scoresSetterGetterWorksCorrectly() {
        Map<BiometricType, QualityScore> scores = new EnumMap<>(BiometricType.class);
        scores.put(BiometricType.FINGER, mockQualityScore1);
        scores.put(BiometricType.IRIS, mockQualityScore2);

        qualityCheck.setScores(scores);
        assertEquals(scores, qualityCheck.getScores());
        assertEquals(2, qualityCheck.getScores().size());
        assertEquals(mockQualityScore1, qualityCheck.getScores().get(BiometricType.FINGER));
        assertEquals(mockQualityScore2, qualityCheck.getScores().get(BiometricType.IRIS));
    }

    /**
     * Tests setter and getter for analyticsInfo field.
     */
    @Test
    void analyticsInfoSetterGetterWorksCorrectly() {
        Map<String, String> analyticsInfo = new HashMap<>();
        analyticsInfo.put("algorithm", "minutiae_based");
        analyticsInfo.put("threshold", "0.85");
        analyticsInfo.put("processingTime", "125ms");

        qualityCheck.setAnalyticsInfo(analyticsInfo);
        assertEquals(analyticsInfo, qualityCheck.getAnalyticsInfo());
        assertEquals(3, qualityCheck.getAnalyticsInfo().size());
        assertEquals("minutiae_based", qualityCheck.getAnalyticsInfo().get("algorithm"));
        assertEquals("0.85", qualityCheck.getAnalyticsInfo().get("threshold"));
        assertEquals("125ms", qualityCheck.getAnalyticsInfo().get("processingTime"));
    }

    /**
     * Tests adding items to scores map.
     */
    @Test
    void addingItemsToScoresMapWorksCorrectly() {
        qualityCheck.getScores().put(BiometricType.FACE, mockQualityScore1);
        qualityCheck.getScores().put(BiometricType.VOICE, mockQualityScore2);

        assertEquals(2, qualityCheck.getScores().size());
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.FACE));
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.VOICE));
        assertEquals(mockQualityScore1, qualityCheck.getScores().get(BiometricType.FACE));
        assertEquals(mockQualityScore2, qualityCheck.getScores().get(BiometricType.VOICE));
    }

    /**
     * Tests adding items to analyticsInfo map.
     */
    @Test
    void addingItemsToAnalyticsInfoMapWorksCorrectly() {
        qualityCheck.getAnalyticsInfo().put("qualityLevel", "high");
        qualityCheck.getAnalyticsInfo().put("confidence", "95.7");
        qualityCheck.getAnalyticsInfo().put("status", "passed");

        assertEquals(3, qualityCheck.getAnalyticsInfo().size());
        assertEquals("high", qualityCheck.getAnalyticsInfo().get("qualityLevel"));
        assertEquals("95.7", qualityCheck.getAnalyticsInfo().get("confidence"));
        assertEquals("passed", qualityCheck.getAnalyticsInfo().get("status"));
    }

    /**
     * Tests that scores map is mutable.
     */
    @Test
    void scoresMapIsMutable() {
        Map<BiometricType, QualityScore> originalScores = qualityCheck.getScores();
        originalScores.put(BiometricType.FINGER, mockQualityScore1);

        assertEquals(1, qualityCheck.getScores().size());
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.FINGER));
    }

    /**
     * Tests that analyticsInfo map is mutable.
     */
    @Test
    void analyticsInfoMapIsMutable() {
        Map<String, String> originalAnalytics = qualityCheck.getAnalyticsInfo();
        originalAnalytics.put("testMetric", "testValue");

        assertEquals(1, qualityCheck.getAnalyticsInfo().size());
        assertTrue(qualityCheck.getAnalyticsInfo().containsKey("testMetric"));
        assertEquals("testValue", qualityCheck.getAnalyticsInfo().get("testMetric"));
    }

    /**
     * Tests setting null values for fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck.getAnalyticsInfo().put("key", "value");

        qualityCheck.setScores(null);
        qualityCheck.setAnalyticsInfo(null);

        assertNull(qualityCheck.getScores());
        assertNull(qualityCheck.getAnalyticsInfo());
    }

    /**
     * Tests replacing entire scores map.
     */
    @Test
    void replacingEntireScoresMapWorksCorrectly() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        assertEquals(1, qualityCheck.getScores().size());

        Map<BiometricType, QualityScore> newScores = new EnumMap<>(BiometricType.class);
        newScores.put(BiometricType.IRIS, mockQualityScore2);
        newScores.put(BiometricType.FACE, mockQualityScore1);
        qualityCheck.setScores(newScores);

        assertEquals(2, qualityCheck.getScores().size());
        assertFalse(qualityCheck.getScores().containsKey(BiometricType.FINGER));
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.IRIS));
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.FACE));
    }

    /**
     * Tests replacing entire analyticsInfo map.
     */
    @Test
    void replacingEntireAnalyticsInfoMapWorksCorrectly() {
        qualityCheck.getAnalyticsInfo().put("initial", "value");
        assertEquals(1, qualityCheck.getAnalyticsInfo().size());

        Map<String, String> newAnalytics = new HashMap<>();
        newAnalytics.put("new1", "value1");
        newAnalytics.put("new2", "value2");
        qualityCheck.setAnalyticsInfo(newAnalytics);

        assertEquals(2, qualityCheck.getAnalyticsInfo().size());
        assertFalse(qualityCheck.getAnalyticsInfo().containsKey("initial"));
        assertTrue(qualityCheck.getAnalyticsInfo().containsKey("new1"));
        assertTrue(qualityCheck.getAnalyticsInfo().containsKey("new2"));
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(qualityCheck.equals(qualityCheck));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(qualityCheck.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(qualityCheck.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        QualityCheck qualityCheck1 = new QualityCheck();
        qualityCheck1.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck1.getAnalyticsInfo().put("key", "value");

        QualityCheck qualityCheck2 = new QualityCheck();
        qualityCheck2.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck2.getAnalyticsInfo().put("key", "value");

        assertTrue(qualityCheck1.equals(qualityCheck2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        qualityCheck.getScores().put(BiometricType.IRIS, mockQualityScore1);
        int hash1 = qualityCheck.hashCode();
        int hash2 = qualityCheck.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        QualityCheck qualityCheck1 = new QualityCheck();
        qualityCheck1.getScores().put(BiometricType.FINGER, mockQualityScore1);

        QualityCheck qualityCheck2 = new QualityCheck();
        qualityCheck2.getScores().put(BiometricType.FINGER, mockQualityScore1);

        assertEquals(qualityCheck1.hashCode(), qualityCheck2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = qualityCheck.toString();
        assertNotNull(result);
        assertTrue(result.contains("QualityCheck"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck.getAnalyticsInfo().put("testKey", "testValue");

        String result = qualityCheck.toString();
        assertNotNull(result);
        assertTrue(result.contains("scores"));
        assertTrue(result.contains("analyticsInfo"));
    }

    /**
     * Tests that constructor creates new instances for collections.
     */
    @Test
    void constructorCreatesNewInstancesForCollections() {
        QualityCheck qualityCheck1 = new QualityCheck();
        QualityCheck qualityCheck2 = new QualityCheck();

        assertNotSame(qualityCheck1.getScores(), qualityCheck2.getScores());
        assertNotSame(qualityCheck1.getAnalyticsInfo(), qualityCheck2.getAnalyticsInfo());
    }

    /**
     * Tests clearing scores map.
     */
    @Test
    void clearingScoresMapWorksCorrectly() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck.getScores().put(BiometricType.IRIS, mockQualityScore2);
        assertEquals(2, qualityCheck.getScores().size());

        qualityCheck.getScores().clear();
        assertEquals(0, qualityCheck.getScores().size());
        assertTrue(qualityCheck.getScores().isEmpty());
    }

    /**
     * Tests clearing analyticsInfo map.
     */
    @Test
    void clearingAnalyticsInfoMapWorksCorrectly() {
        qualityCheck.getAnalyticsInfo().put("key1", "value1");
        qualityCheck.getAnalyticsInfo().put("key2", "value2");
        assertEquals(2, qualityCheck.getAnalyticsInfo().size());

        qualityCheck.getAnalyticsInfo().clear();
        assertEquals(0, qualityCheck.getAnalyticsInfo().size());
        assertTrue(qualityCheck.getAnalyticsInfo().isEmpty());
    }

    /**
     * Tests removing items from scores map.
     */
    @Test
    void removingItemsFromScoresMapWorksCorrectly() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck.getScores().put(BiometricType.IRIS, mockQualityScore2);
        qualityCheck.getScores().put(BiometricType.FACE, mockQualityScore1);

        qualityCheck.getScores().remove(BiometricType.IRIS);
        assertEquals(2, qualityCheck.getScores().size());
        assertFalse(qualityCheck.getScores().containsKey(BiometricType.IRIS));
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.FINGER));
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.FACE));
    }

    /**
     * Tests removing items from analyticsInfo map.
     */
    @Test
    void removingItemsFromAnalyticsInfoMapWorksCorrectly() {
        qualityCheck.getAnalyticsInfo().put("key1", "value1");
        qualityCheck.getAnalyticsInfo().put("key2", "value2");
        qualityCheck.getAnalyticsInfo().put("key3", "value3");

        qualityCheck.getAnalyticsInfo().remove("key2");
        assertEquals(2, qualityCheck.getAnalyticsInfo().size());
        assertFalse(qualityCheck.getAnalyticsInfo().containsKey("key2"));
        assertTrue(qualityCheck.getAnalyticsInfo().containsKey("key1"));
        assertTrue(qualityCheck.getAnalyticsInfo().containsKey("key3"));
    }

    /**
     * Tests complete quality check object setup.
     */
    @Test
    void completeQualityCheckObjectSetup() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck.getScores().put(BiometricType.IRIS, mockQualityScore2);
        qualityCheck.getAnalyticsInfo().put("overallQuality", "excellent");
        qualityCheck.getAnalyticsInfo().put("assessmentTime", "200ms");
        qualityCheck.getAnalyticsInfo().put("assessmentAlgorithm", "advanced");

        assertEquals(2, qualityCheck.getScores().size());
        assertEquals(3, qualityCheck.getAnalyticsInfo().size());

        assertTrue(qualityCheck.getScores().containsKey(BiometricType.FINGER));
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.IRIS));
        assertEquals("excellent", qualityCheck.getAnalyticsInfo().get("overallQuality"));
        assertEquals("200ms", qualityCheck.getAnalyticsInfo().get("assessmentTime"));
        assertEquals("advanced", qualityCheck.getAnalyticsInfo().get("assessmentAlgorithm"));
    }

    /**
     * Tests EnumMap specific behavior for scores.
     */
    @Test
    void enumMapSpecificBehaviorForScores() {
        qualityCheck.getScores().put(BiometricType.VOICE, mockQualityScore1);
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore2);
        qualityCheck.getScores().put(BiometricType.FACE, mockQualityScore1);

        assertEquals(3, qualityCheck.getScores().size());

        for (BiometricType type : BiometricType.values()) {
            qualityCheck.getScores().put(type, mockQualityScore1);
        }

        assertEquals(BiometricType.values().length, qualityCheck.getScores().size());
    }

    /**
     * Tests analyticsInfo with empty strings.
     */
    @Test
    void analyticsInfoWithEmptyStringsWorksCorrectly() {
        qualityCheck.getAnalyticsInfo().put("", "emptyKey");
        qualityCheck.getAnalyticsInfo().put("emptyValue", "");
        qualityCheck.getAnalyticsInfo().put("valid", "data");

        assertEquals(3, qualityCheck.getAnalyticsInfo().size());
        assertEquals("emptyKey", qualityCheck.getAnalyticsInfo().get(""));
        assertEquals("", qualityCheck.getAnalyticsInfo().get("emptyValue"));
        assertEquals("data", qualityCheck.getAnalyticsInfo().get("valid"));
    }

    /**
     * Tests quality check state after multiple modifications.
     */
    @Test
    void qualityCheckStateAfterMultipleModifications() {
        qualityCheck.getScores().put(BiometricType.FINGER, mockQualityScore1);
        qualityCheck.getAnalyticsInfo().put("initial", "value");

        qualityCheck.getScores().put(BiometricType.IRIS, mockQualityScore2);
        qualityCheck.getScores().remove(BiometricType.FINGER);
        qualityCheck.getAnalyticsInfo().put("updated", "newValue");
        qualityCheck.getAnalyticsInfo().remove("initial");

        assertEquals(1, qualityCheck.getScores().size());
        assertTrue(qualityCheck.getScores().containsKey(BiometricType.IRIS));
        assertFalse(qualityCheck.getScores().containsKey(BiometricType.FINGER));
        assertEquals(1, qualityCheck.getAnalyticsInfo().size());
        assertEquals("newValue", qualityCheck.getAnalyticsInfo().get("updated"));
        assertFalse(qualityCheck.getAnalyticsInfo().containsKey("initial"));
    }
}
