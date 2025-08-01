package io.mosip.kernel.biometrics.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.model.QualityScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link QualityScore}.
 */
class QualityScoreTest {

    private QualityScore qualityScore;

    @BeforeEach
    void setUp() {
        qualityScore = new QualityScore();
    }

    /**
     * Tests default constructor initializes all fields correctly.
     */
    @Test
    void defaultConstructorInitializesFieldsCorrectly() {
        QualityScore newQualityScore = new QualityScore();

        assertEquals(0.0f, newQualityScore.getScore());
        assertNotNull(newQualityScore.getErrors());
        assertTrue(newQualityScore.getErrors().isEmpty());
        assertNotNull(newQualityScore.getAnalyticsInfo());
        assertTrue(newQualityScore.getAnalyticsInfo().isEmpty());
    }

    /**
     * Tests setter and getter for score field.
     */
    @Test
    void scoreSetterGetterWorksCorrectly() {
        qualityScore.setScore(85.5f);
        assertEquals(85.5f, qualityScore.getScore());
    }

    /**
     * Tests setter and getter for errors field.
     */
    @Test
    void errorsSetterGetterWorksCorrectly() {
        List<String> errors = new ArrayList<>();
        errors.add("Low image quality");
        errors.add("Blur detected");

        qualityScore.setErrors(errors);
        assertEquals(errors, qualityScore.getErrors());
        assertEquals(2, qualityScore.getErrors().size());
        assertEquals("Low image quality", qualityScore.getErrors().get(0));
        assertEquals("Blur detected", qualityScore.getErrors().get(1));
    }

    /**
     * Tests setter and getter for analyticsInfo field.
     */
    @Test
    void analyticsInfoSetterGetterWorksCorrectly() {
        Map<String, String> analyticsInfo = new HashMap<>();
        analyticsInfo.put("algorithm", "image_quality_assessment");
        analyticsInfo.put("processingTime", "125ms");
        analyticsInfo.put("threshold", "75.0");

        qualityScore.setAnalyticsInfo(analyticsInfo);
        assertEquals(analyticsInfo, qualityScore.getAnalyticsInfo());
        assertEquals(3, qualityScore.getAnalyticsInfo().size());
        assertEquals("image_quality_assessment", qualityScore.getAnalyticsInfo().get("algorithm"));
        assertEquals("125ms", qualityScore.getAnalyticsInfo().get("processingTime"));
        assertEquals("75.0", qualityScore.getAnalyticsInfo().get("threshold"));
    }

    /**
     * Tests adding items to errors list.
     */
    @Test
    void addingItemsToErrorsListWorksCorrectly() {
        qualityScore.getErrors().add("Insufficient contrast");
        qualityScore.getErrors().add("Lighting issue");

        assertEquals(2, qualityScore.getErrors().size());
        assertTrue(qualityScore.getErrors().contains("Insufficient contrast"));
        assertTrue(qualityScore.getErrors().contains("Lighting issue"));
    }

    /**
     * Tests adding items to analyticsInfo map.
     */
    @Test
    void addingItemsToAnalyticsInfoMapWorksCorrectly() {
        qualityScore.getAnalyticsInfo().put("confidence", "92.3");
        qualityScore.getAnalyticsInfo().put("method", "neural_network");
        qualityScore.getAnalyticsInfo().put("version", "2.1");

        assertEquals(3, qualityScore.getAnalyticsInfo().size());
        assertEquals("92.3", qualityScore.getAnalyticsInfo().get("confidence"));
        assertEquals("neural_network", qualityScore.getAnalyticsInfo().get("method"));
        assertEquals("2.1", qualityScore.getAnalyticsInfo().get("version"));
    }

    /**
     * Tests that errors list is mutable.
     */
    @Test
    void errorsListIsMutable() {
        List<String> originalErrors = qualityScore.getErrors();
        originalErrors.add("Mutable error");

        assertEquals(1, qualityScore.getErrors().size());
        assertTrue(qualityScore.getErrors().contains("Mutable error"));
    }

    /**
     * Tests that analyticsInfo map is mutable.
     */
    @Test
    void analyticsInfoMapIsMutable() {
        Map<String, String> originalAnalytics = qualityScore.getAnalyticsInfo();
        originalAnalytics.put("mutableKey", "mutableValue");

        assertEquals(1, qualityScore.getAnalyticsInfo().size());
        assertTrue(qualityScore.getAnalyticsInfo().containsKey("mutableKey"));
        assertEquals("mutableValue", qualityScore.getAnalyticsInfo().get("mutableKey"));
    }

    /**
     * Tests setting null values for fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        qualityScore.getErrors().add("Some error");
        qualityScore.getAnalyticsInfo().put("key", "value");

        qualityScore.setErrors(null);
        qualityScore.setAnalyticsInfo(null);

        assertNull(qualityScore.getErrors());
        assertNull(qualityScore.getAnalyticsInfo());
    }

    /**
     * Tests score with boundary values.
     */
    @Test
    void scoreWithBoundaryValuesWorksCorrectly() {
        qualityScore.setScore(0.0f);
        assertEquals(0.0f, qualityScore.getScore());

        qualityScore.setScore(100.0f);
        assertEquals(100.0f, qualityScore.getScore());

        qualityScore.setScore(75.25f);
        assertEquals(75.25f, qualityScore.getScore());
    }

    /**
     * Tests replacing entire errors list.
     */
    @Test
    void replacingEntireErrorsListWorksCorrectly() {
        qualityScore.getErrors().add("Initial error");
        assertEquals(1, qualityScore.getErrors().size());
        List<String> newErrors = new ArrayList<>();
        newErrors.add("New error 1");
        newErrors.add("New error 2");
        qualityScore.setErrors(newErrors);

        assertEquals(2, qualityScore.getErrors().size());
        assertFalse(qualityScore.getErrors().contains("Initial error"));
        assertTrue(qualityScore.getErrors().contains("New error 1"));
        assertTrue(qualityScore.getErrors().contains("New error 2"));
    }

    /**
     * Tests replacing entire analyticsInfo map.
     */
    @Test
    void replacingEntireAnalyticsInfoMapWorksCorrectly() {
        qualityScore.getAnalyticsInfo().put("initial", "value");
        assertEquals(1, qualityScore.getAnalyticsInfo().size());

        Map<String, String> newAnalytics = new HashMap<>();
        newAnalytics.put("new1", "value1");
        newAnalytics.put("new2", "value2");
        qualityScore.setAnalyticsInfo(newAnalytics);

        assertEquals(2, qualityScore.getAnalyticsInfo().size());
        assertFalse(qualityScore.getAnalyticsInfo().containsKey("initial"));
        assertTrue(qualityScore.getAnalyticsInfo().containsKey("new1"));
        assertTrue(qualityScore.getAnalyticsInfo().containsKey("new2"));
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(qualityScore.equals(qualityScore));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(qualityScore.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(qualityScore.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        QualityScore qualityScore1 = new QualityScore();
        qualityScore1.setScore(88.5f);
        qualityScore1.getErrors().add("Error");
        qualityScore1.getAnalyticsInfo().put("key", "value");

        QualityScore qualityScore2 = new QualityScore();
        qualityScore2.setScore(88.5f);
        qualityScore2.getErrors().add("Error");
        qualityScore2.getAnalyticsInfo().put("key", "value");

        assertTrue(qualityScore1.equals(qualityScore2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        qualityScore.setScore(95.0f);
        int hash1 = qualityScore.hashCode();
        int hash2 = qualityScore.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        QualityScore qualityScore1 = new QualityScore();
        qualityScore1.setScore(75.0f);

        QualityScore qualityScore2 = new QualityScore();
        qualityScore2.setScore(75.0f);

        assertEquals(qualityScore1.hashCode(), qualityScore2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = qualityScore.toString();
        assertNotNull(result);
        assertTrue(result.contains("QualityScore"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        qualityScore.setScore(82.7f);
        qualityScore.getErrors().add("Test error");
        qualityScore.getAnalyticsInfo().put("testKey", "testValue");

        String result = qualityScore.toString();
        assertNotNull(result);
        assertTrue(result.contains("score"));
        assertTrue(result.contains("errors"));
        assertTrue(result.contains("analyticsInfo"));
    }

    /**
     * Tests that constructor creates new instances for collections.
     */
    @Test
    void constructorCreatesNewInstancesForCollections() {
        QualityScore qualityScore1 = new QualityScore();
        QualityScore qualityScore2 = new QualityScore();

        assertNotSame(qualityScore1.getErrors(), qualityScore2.getErrors());
        assertNotSame(qualityScore1.getAnalyticsInfo(), qualityScore2.getAnalyticsInfo());
    }

    /**
     * Tests clearing errors list.
     */
    @Test
    void clearingErrorsListWorksCorrectly() {
        qualityScore.getErrors().add("Error 1");
        qualityScore.getErrors().add("Error 2");
        assertEquals(2, qualityScore.getErrors().size());

        qualityScore.getErrors().clear();
        assertEquals(0, qualityScore.getErrors().size());
        assertTrue(qualityScore.getErrors().isEmpty());
    }

    /**
     * Tests clearing analyticsInfo map.
     */
    @Test
    void clearingAnalyticsInfoMapWorksCorrectly() {
        qualityScore.getAnalyticsInfo().put("key1", "value1");
        qualityScore.getAnalyticsInfo().put("key2", "value2");
        assertEquals(2, qualityScore.getAnalyticsInfo().size());

        qualityScore.getAnalyticsInfo().clear();
        assertEquals(0, qualityScore.getAnalyticsInfo().size());
        assertTrue(qualityScore.getAnalyticsInfo().isEmpty());
    }

    /**
     * Tests removing items from errors list.
     */
    @Test
    void removingItemsFromErrorsListWorksCorrectly() {
        qualityScore.getErrors().add("Error 1");
        qualityScore.getErrors().add("Error 2");
        qualityScore.getErrors().add("Error 3");

        qualityScore.getErrors().remove("Error 2");
        assertEquals(2, qualityScore.getErrors().size());
        assertFalse(qualityScore.getErrors().contains("Error 2"));
        assertTrue(qualityScore.getErrors().contains("Error 1"));
        assertTrue(qualityScore.getErrors().contains("Error 3"));
    }

    /**
     * Tests removing items from analyticsInfo map.
     */
    @Test
    void removingItemsFromAnalyticsInfoMapWorksCorrectly() {
        qualityScore.getAnalyticsInfo().put("key1", "value1");
        qualityScore.getAnalyticsInfo().put("key2", "value2");
        qualityScore.getAnalyticsInfo().put("key3", "value3");

        qualityScore.getAnalyticsInfo().remove("key2");
        assertEquals(2, qualityScore.getAnalyticsInfo().size());
        assertFalse(qualityScore.getAnalyticsInfo().containsKey("key2"));
        assertTrue(qualityScore.getAnalyticsInfo().containsKey("key1"));
        assertTrue(qualityScore.getAnalyticsInfo().containsKey("key3"));
    }

    /**
     * Tests errors list with empty strings.
     */
    @Test
    void errorsListWithEmptyStringsWorksCorrectly() {
        qualityScore.getErrors().add("");
        qualityScore.getErrors().add("   ");
        qualityScore.getErrors().add("Valid error");

        assertEquals(3, qualityScore.getErrors().size());
        assertEquals("", qualityScore.getErrors().get(0));
        assertEquals("   ", qualityScore.getErrors().get(1));
        assertEquals("Valid error", qualityScore.getErrors().get(2));
    }

    /**
     * Tests analyticsInfo map with empty strings.
     */
    @Test
    void analyticsInfoMapWithEmptyStringsWorksCorrectly() {
        qualityScore.getAnalyticsInfo().put("", "emptyKey");
        qualityScore.getAnalyticsInfo().put("emptyValue", "");
        qualityScore.getAnalyticsInfo().put("valid", "data");

        assertEquals(3, qualityScore.getAnalyticsInfo().size());
        assertEquals("emptyKey", qualityScore.getAnalyticsInfo().get(""));
        assertEquals("", qualityScore.getAnalyticsInfo().get("emptyValue"));
        assertEquals("data", qualityScore.getAnalyticsInfo().get("valid"));
    }

    /**
     * Tests complete quality score object setup.
     */
    @Test
    void completeQualityScoreObjectSetup() {
        qualityScore.setScore(87.3f);
        qualityScore.getErrors().add("Image quality degraded");
        qualityScore.getErrors().add("Insufficient lighting");
        qualityScore.getAnalyticsInfo().put("confidence", "87.3");
        qualityScore.getAnalyticsInfo().put("assessmentTime", "180ms");
        qualityScore.getAnalyticsInfo().put("qualityAlgorithm", "advanced_analysis");

        assertEquals(87.3f, qualityScore.getScore());
        assertEquals(2, qualityScore.getErrors().size());
        assertEquals(3, qualityScore.getAnalyticsInfo().size());

        assertTrue(qualityScore.getErrors().contains("Image quality degraded"));
        assertTrue(qualityScore.getErrors().contains("Insufficient lighting"));
        assertEquals("87.3", qualityScore.getAnalyticsInfo().get("confidence"));
        assertEquals("180ms", qualityScore.getAnalyticsInfo().get("assessmentTime"));
        assertEquals("advanced_analysis", qualityScore.getAnalyticsInfo().get("qualityAlgorithm"));
    }

    /**
     * Tests quality score state after multiple modifications.
     */
    @Test
    void qualityScoreStateAfterMultipleModifications() {
        qualityScore.setScore(50.0f);
        qualityScore.getErrors().add("Initial error");
        qualityScore.getAnalyticsInfo().put("initial", "value");

        qualityScore.setScore(90.0f);
        qualityScore.getErrors().add("Second error");
        qualityScore.getErrors().remove("Initial error");
        qualityScore.getAnalyticsInfo().put("updated", "newValue");
        qualityScore.getAnalyticsInfo().remove("initial");

        assertEquals(90.0f, qualityScore.getScore());
        assertEquals(1, qualityScore.getErrors().size());
        assertEquals("Second error", qualityScore.getErrors().get(0));
        assertEquals(1, qualityScore.getAnalyticsInfo().size());
        assertEquals("newValue", qualityScore.getAnalyticsInfo().get("updated"));
        assertFalse(qualityScore.getAnalyticsInfo().containsKey("initial"));
    }

    /**
     * Tests score range validation scenarios.
     */
    @Test
    void scoreRangeValidationScenarios() {
        qualityScore.setScore(0.0f);
        assertEquals(0.0f, qualityScore.getScore());

        qualityScore.setScore(50.5f);
        assertEquals(50.5f, qualityScore.getScore());

        qualityScore.setScore(100.0f);
        assertEquals(100.0f, qualityScore.getScore());

        qualityScore.setScore(99.99f);
        assertEquals(99.99f, qualityScore.getScore(), 0.001f);
    }
}
