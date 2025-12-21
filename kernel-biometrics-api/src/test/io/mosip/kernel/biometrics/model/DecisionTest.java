package io.mosip.kernel.biometrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.model.Decision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.Match;

/**
 * Unit tests for {@link Decision}.
 */
class DecisionTest {

    private Decision decision;

    @BeforeEach
    void setUp() {
        decision = new Decision();
    }

    /**
     * Tests default constructor initializes all fields correctly.
     */
    @Test
    void defaultConstructorInitializesFieldsCorrectly() {
        Decision newDecision = new Decision();

        assertNull(newDecision.getMatch());
        assertNotNull(newDecision.getErrors());
        assertTrue(newDecision.getErrors().isEmpty());
        assertNotNull(newDecision.getAnalyticsInfo());
        assertTrue(newDecision.getAnalyticsInfo().isEmpty());
    }

    /**
     * Tests setter and getter for match field.
     */
    @Test
    void matchSetterGetterWorksCorrectly() {
        decision.setMatch(Match.MATCHED);
        assertEquals(Match.MATCHED, decision.getMatch());
    }

    /**
     * Tests setter and getter for errors field.
     */
    @Test
    void errorsSetterGetterWorksCorrectly() {
        List<String> errors = new ArrayList<>();
        errors.add("Error 1");
        errors.add("Error 2");

        decision.setErrors(errors);
        assertEquals(errors, decision.getErrors());
        assertEquals(2, decision.getErrors().size());
        assertEquals("Error 1", decision.getErrors().get(0));
        assertEquals("Error 2", decision.getErrors().get(1));
    }

    /**
     * Tests setter and getter for analyticsInfo field.
     */
    @Test
    void analyticsInfoSetterGetterWorksCorrectly() {
        Map<String, String> analyticsInfo = new HashMap<>();
        analyticsInfo.put("confidence", "95.5");
        analyticsInfo.put("algorithm", "minutiae");

        decision.setAnalyticsInfo(analyticsInfo);
        assertEquals(analyticsInfo, decision.getAnalyticsInfo());
        assertEquals(2, decision.getAnalyticsInfo().size());
        assertEquals("95.5", decision.getAnalyticsInfo().get("confidence"));
        assertEquals("minutiae", decision.getAnalyticsInfo().get("algorithm"));
    }

    /**
     * Tests adding items to errors list.
     */
    @Test
    void addingItemsToErrorsListWorksCorrectly() {
        decision.getErrors().add("Network timeout");
        decision.getErrors().add("Invalid template");

        assertEquals(2, decision.getErrors().size());
        assertTrue(decision.getErrors().contains("Network timeout"));
        assertTrue(decision.getErrors().contains("Invalid template"));
    }

    /**
     * Tests adding items to analyticsInfo map.
     */
    @Test
    void addingItemsToAnalyticsInfoMapWorksCorrectly() {
        decision.getAnalyticsInfo().put("processingTime", "150ms");
        decision.getAnalyticsInfo().put("quality", "good");

        assertEquals(2, decision.getAnalyticsInfo().size());
        assertEquals("150ms", decision.getAnalyticsInfo().get("processingTime"));
        assertEquals("good", decision.getAnalyticsInfo().get("quality"));
    }

    /**
     * Tests that errors list is mutable.
     */
    @Test
    void errorsListIsMutable() {
        List<String> originalErrors = decision.getErrors();
        originalErrors.add("Test error");

        assertEquals(1, decision.getErrors().size());
        assertTrue(decision.getErrors().contains("Test error"));
    }

    /**
     * Tests that analyticsInfo map is mutable.
     */
    @Test
    void analyticsInfoMapIsMutable() {
        Map<String, String> originalAnalytics = decision.getAnalyticsInfo();
        originalAnalytics.put("testKey", "testValue");

        assertEquals(1, decision.getAnalyticsInfo().size());
        assertTrue(decision.getAnalyticsInfo().containsKey("testKey"));
        assertEquals("testValue", decision.getAnalyticsInfo().get("testKey"));
    }

    /**
     * Tests setting null values for all fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        decision.setMatch(Match.MATCHED);
        decision.getErrors().add("Some error");
        decision.getAnalyticsInfo().put("key", "value");

        decision.setMatch(null);
        decision.setErrors(null);
        decision.setAnalyticsInfo(null);

        assertNull(decision.getMatch());
        assertNull(decision.getErrors());
        assertNull(decision.getAnalyticsInfo());
    }

    /**
     * Tests replacing entire errors list.
     */
    @Test
    void replacingEntireErrorsListWorksCorrectly() {
        // Add initial errors
        decision.getErrors().add("Initial error");
        assertEquals(1, decision.getErrors().size());

        // Replace with new list
        List<String> newErrors = new ArrayList<>();
        newErrors.add("New error 1");
        newErrors.add("New error 2");
        decision.setErrors(newErrors);

        assertEquals(2, decision.getErrors().size());
        assertFalse(decision.getErrors().contains("Initial error"));
        assertTrue(decision.getErrors().contains("New error 1"));
        assertTrue(decision.getErrors().contains("New error 2"));
    }

    /**
     * Tests replacing entire analyticsInfo map.
     */
    @Test
    void replacingEntireAnalyticsInfoMapWorksCorrectly() {
        // Add initial analytics
        decision.getAnalyticsInfo().put("initial", "value");
        assertEquals(1, decision.getAnalyticsInfo().size());

        // Replace with new map
        Map<String, String> newAnalytics = new HashMap<>();
        newAnalytics.put("new1", "value1");
        newAnalytics.put("new2", "value2");
        decision.setAnalyticsInfo(newAnalytics);

        assertEquals(2, decision.getAnalyticsInfo().size());
        assertFalse(decision.getAnalyticsInfo().containsKey("initial"));
        assertTrue(decision.getAnalyticsInfo().containsKey("new1"));
        assertTrue(decision.getAnalyticsInfo().containsKey("new2"));
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(decision.equals(decision));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(decision.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(decision.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        Decision decision1 = new Decision();
        decision1.setMatch(Match.MATCHED);
        decision1.getErrors().add("Error");
        decision1.getAnalyticsInfo().put("key", "value");

        Decision decision2 = new Decision();
        decision2.setMatch(Match.MATCHED);
        decision2.getErrors().add("Error");
        decision2.getAnalyticsInfo().put("key", "value");

        assertTrue(decision1.equals(decision2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        decision.setMatch(Match.NOT_MATCHED);
        int hash1 = decision.hashCode();
        int hash2 = decision.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        Decision decision1 = new Decision();
        decision1.setMatch(Match.MATCHED);

        Decision decision2 = new Decision();
        decision2.setMatch(Match.MATCHED);

        assertEquals(decision1.hashCode(), decision2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = decision.toString();
        assertNotNull(result);
        assertTrue(result.contains("Decision"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        decision.setMatch(Match.MATCHED);
        decision.getErrors().add("Test error");
        decision.getAnalyticsInfo().put("testKey", "testValue");

        String result = decision.toString();
        assertNotNull(result);
        assertTrue(result.contains("match"));
        assertTrue(result.contains("errors"));
        assertTrue(result.contains("analyticsInfo"));
    }

    /**
     * Tests that constructor creates new instances for collections.
     */
    @Test
    void constructorCreatesNewInstancesForCollections() {
        Decision decision1 = new Decision();
        Decision decision2 = new Decision();

        assertNotSame(decision1.getErrors(), decision2.getErrors());
        assertNotSame(decision1.getAnalyticsInfo(), decision2.getAnalyticsInfo());
    }

    /**
     * Tests clearing errors list.
     */
    @Test
    void clearingErrorsListWorksCorrectly() {
        decision.getErrors().add("Error 1");
        decision.getErrors().add("Error 2");
        assertEquals(2, decision.getErrors().size());

        decision.getErrors().clear();
        assertEquals(0, decision.getErrors().size());
        assertTrue(decision.getErrors().isEmpty());
    }

    /**
     * Tests clearing analyticsInfo map.
     */
    @Test
    void clearingAnalyticsInfoMapWorksCorrectly() {
        decision.getAnalyticsInfo().put("key1", "value1");
        decision.getAnalyticsInfo().put("key2", "value2");
        assertEquals(2, decision.getAnalyticsInfo().size());

        decision.getAnalyticsInfo().clear();
        assertEquals(0, decision.getAnalyticsInfo().size());
        assertTrue(decision.getAnalyticsInfo().isEmpty());
    }

    /**
     * Tests removing items from errors list.
     */
    @Test
    void removingItemsFromErrorsListWorksCorrectly() {
        decision.getErrors().add("Error 1");
        decision.getErrors().add("Error 2");
        decision.getErrors().add("Error 3");

        decision.getErrors().remove("Error 2");
        assertEquals(2, decision.getErrors().size());
        assertFalse(decision.getErrors().contains("Error 2"));
        assertTrue(decision.getErrors().contains("Error 1"));
        assertTrue(decision.getErrors().contains("Error 3"));
    }

    /**
     * Tests removing items from analyticsInfo map.
     */
    @Test
    void removingItemsFromAnalyticsInfoMapWorksCorrectly() {
        decision.getAnalyticsInfo().put("key1", "value1");
        decision.getAnalyticsInfo().put("key2", "value2");
        decision.getAnalyticsInfo().put("key3", "value3");

        decision.getAnalyticsInfo().remove("key2");
        assertEquals(2, decision.getAnalyticsInfo().size());
        assertFalse(decision.getAnalyticsInfo().containsKey("key2"));
        assertTrue(decision.getAnalyticsInfo().containsKey("key1"));
        assertTrue(decision.getAnalyticsInfo().containsKey("key3"));
    }

    /**
     * Tests errors list with empty strings.
     */
    @Test
    void errorsListWithEmptyStringsWorksCorrectly() {
        decision.getErrors().add("");
        decision.getErrors().add("   ");
        decision.getErrors().add("Valid error");

        assertEquals(3, decision.getErrors().size());
        assertEquals("", decision.getErrors().get(0));
        assertEquals("   ", decision.getErrors().get(1));
        assertEquals("Valid error", decision.getErrors().get(2));
    }

    /**
     * Tests analyticsInfo map with empty strings.
     */
    @Test
    void analyticsInfoMapWithEmptyStringsWorksCorrectly() {
        decision.getAnalyticsInfo().put("", "emptyKey");
        decision.getAnalyticsInfo().put("emptyValue", "");
        decision.getAnalyticsInfo().put("valid", "data");

        assertEquals(3, decision.getAnalyticsInfo().size());
        assertEquals("emptyKey", decision.getAnalyticsInfo().get(""));
        assertEquals("", decision.getAnalyticsInfo().get("emptyValue"));
        assertEquals("data", decision.getAnalyticsInfo().get("valid"));
    }

    /**
     * Tests decision with different Match enum values.
     */
    @Test
    void decisionWithDifferentMatchEnumValues() {
        decision.setMatch(Match.MATCHED);
        assertEquals(Match.MATCHED, decision.getMatch());
        decision.setMatch(Match.NOT_MATCHED);
        assertEquals(Match.NOT_MATCHED, decision.getMatch());

        decision.setMatch(null);
        assertNull(decision.getMatch());
    }

    /**
     * Tests complete decision object setup.
     */
    @Test
    void completeDecisionObjectSetup() {
        decision.setMatch(Match.MATCHED);
        decision.getErrors().add("Timeout error");
        decision.getErrors().add("Quality check failed");
        decision.getAnalyticsInfo().put("confidence", "87.5");
        decision.getAnalyticsInfo().put("processingTime", "245ms");
        decision.getAnalyticsInfo().put("algorithm", "neural_network");

        assertEquals(Match.MATCHED, decision.getMatch());
        assertEquals(2, decision.getErrors().size());
        assertEquals(3, decision.getAnalyticsInfo().size());

        assertTrue(decision.getErrors().contains("Timeout error"));
        assertTrue(decision.getErrors().contains("Quality check failed"));
        assertEquals("87.5", decision.getAnalyticsInfo().get("confidence"));
        assertEquals("245ms", decision.getAnalyticsInfo().get("processingTime"));
        assertEquals("neural_network", decision.getAnalyticsInfo().get("algorithm"));
    }

    /**
     * Tests decision state after multiple modifications.
     */
    @Test
    void decisionStateAfterMultipleModifications() {
        decision.setMatch(Match.NOT_MATCHED);
        decision.getErrors().add("Initial error");
        decision.getAnalyticsInfo().put("initial", "value");

        decision.setMatch(Match.MATCHED);
        decision.getErrors().add("Second error");
        decision.getErrors().remove("Initial error");
        decision.getAnalyticsInfo().put("updated", "newValue");
        decision.getAnalyticsInfo().remove("initial");

        assertEquals(Match.MATCHED, decision.getMatch());
        assertEquals(1, decision.getErrors().size());
        assertEquals("Second error", decision.getErrors().get(0));
        assertEquals(1, decision.getAnalyticsInfo().size());
        assertEquals("newValue", decision.getAnalyticsInfo().get("updated"));
        assertFalse(decision.getAnalyticsInfo().containsKey("initial"));
    }
}