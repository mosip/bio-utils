package io.mosip.kernel.biometrics.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.model.QualityScore;
import io.mosip.kernel.biometrics.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Unit tests for {@link Response}.
 */
class ResponseTest {

    private Response<String> stringResponse;
    private Response<Integer> integerResponse;
    private Response<Object> objectResponse;

    @BeforeEach
    void setUp() {
        stringResponse = new Response<>();
        integerResponse = new Response<>();
        objectResponse = new Response<>();
    }

    /**
     * Tests default constructor creates Response with null fields.
     */
    @Test
    void defaultConstructorCreatesEmptyResponse() {
        Response<String> newResponse = new Response<>();

        assertNull(newResponse.getStatusCode());
        assertNull(newResponse.getStatusMessage());
        assertNull(newResponse.getResponse());
    }

    /**
     * Tests setter and getter for statusCode field.
     */
    @Test
    void statusCodeSetterGetterWorksCorrectly() {
        stringResponse.setStatusCode(200);
        assertEquals(200, stringResponse.getStatusCode());
    }

    /**
     * Tests setter and getter for statusMessage field.
     */
    @Test
    void statusMessageSetterGetterWorksCorrectly() {
        stringResponse.setStatusMessage("Success");
        assertEquals("Success", stringResponse.getStatusMessage());
    }

    /**
     * Tests setter and getter for response field with String type.
     */
    @Test
    void responseSetterGetterWorksCorrectlyWithString() {
        stringResponse.setResponse("Test response data");
        assertEquals("Test response data", stringResponse.getResponse());
    }

    /**
     * Tests setter and getter for response field with Integer type.
     */
    @Test
    void responseSetterGetterWorksCorrectlyWithInteger() {
        integerResponse.setResponse(42);
        assertEquals(42, integerResponse.getResponse());
    }

    /**
     * Tests setter and getter for response field with Object type.
     */
    @Test
    void responseSetterGetterWorksCorrectlyWithObject() {
        Object testObject = new Object();
        objectResponse.setResponse(testObject);
        assertEquals(testObject, objectResponse.getResponse());
    }

    /**
     * Tests setting null values for all fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        stringResponse.setStatusCode(200);
        stringResponse.setStatusMessage("Success");
        stringResponse.setResponse("Data");

        stringResponse.setStatusCode(null);
        stringResponse.setStatusMessage(null);
        stringResponse.setResponse(null);

        assertNull(stringResponse.getStatusCode());
        assertNull(stringResponse.getStatusMessage());
        assertNull(stringResponse.getResponse());
    }

    /**
     * Tests response with complex object types.
     */
    @Test
    void responseWithComplexObjectTypesWorksCorrectly() {
        Response<Map<String, String>> mapResponse = new Response<>();
        Map<String, String> testMap = new HashMap<>();
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");

        mapResponse.setResponse(testMap);
        assertEquals(testMap, mapResponse.getResponse());
        assertEquals("value1", mapResponse.getResponse().get("key1"));
        assertEquals("value2", mapResponse.getResponse().get("key2"));
    }

    /**
     * Tests response with list type.
     */
    @Test
    void responseWithListTypeWorksCorrectly() {
        Response<List<String>> listResponse = new Response<>();
        List<String> testList = new ArrayList<>();
        testList.add("item1");
        testList.add("item2");

        listResponse.setResponse(testList);
        assertEquals(testList, listResponse.getResponse());
        assertEquals(2, listResponse.getResponse().size());
        assertTrue(listResponse.getResponse().contains("item1"));
        assertTrue(listResponse.getResponse().contains("item2"));
    }

    /**
     * Tests response with custom object type.
     */
    @Test
    void responseWithCustomObjectTypeWorksCorrectly() {
        Response<QualityScore> qualityScoreResponse = new Response<>();
        QualityScore qualityScore = new QualityScore();
        qualityScore.setScore(85.5f);

        qualityScoreResponse.setResponse(qualityScore);
        assertEquals(qualityScore, qualityScoreResponse.getResponse());
        assertEquals(85.5f, qualityScoreResponse.getResponse().getScore());
    }

    /**
     * Tests statusCode with different integer values.
     */
    @Test
    void statusCodeWithDifferentIntegerValuesWorksCorrectly() {
        stringResponse.setStatusCode(200);
        assertEquals(200, stringResponse.getStatusCode());

        stringResponse.setStatusCode(404);
        assertEquals(404, stringResponse.getStatusCode());

        stringResponse.setStatusCode(500);
        assertEquals(500, stringResponse.getStatusCode());

        stringResponse.setStatusCode(-1);
        assertEquals(-1, stringResponse.getStatusCode());
    }

    /**
     * Tests statusMessage with different string values.
     */
    @Test
    void statusMessageWithDifferentStringValuesWorksCorrectly() {
        stringResponse.setStatusMessage("Operation successful");
        assertEquals("Operation successful", stringResponse.getStatusMessage());

        stringResponse.setStatusMessage("Error occurred");
        assertEquals("Error occurred", stringResponse.getStatusMessage());

        stringResponse.setStatusMessage("");
        assertEquals("", stringResponse.getStatusMessage());

        stringResponse.setStatusMessage("   ");
        assertEquals("   ", stringResponse.getStatusMessage());
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(stringResponse.equals(stringResponse));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(stringResponse.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(stringResponse.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        Response<String> response1 = new Response<>();
        response1.setStatusCode(200);
        response1.setStatusMessage("Success");
        response1.setResponse("Data");

        Response<String> response2 = new Response<>();
        response2.setStatusCode(200);
        response2.setStatusMessage("Success");
        response2.setResponse("Data");

        assertTrue(response1.equals(response2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        stringResponse.setStatusCode(200);
        stringResponse.setStatusMessage("Success");
        int hash1 = stringResponse.hashCode();
        int hash2 = stringResponse.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        Response<String> response1 = new Response<>();
        response1.setStatusCode(200);
        response1.setStatusMessage("Success");

        Response<String> response2 = new Response<>();
        response2.setStatusCode(200);
        response2.setStatusMessage("Success");

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = stringResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("Response"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        stringResponse.setStatusCode(200);
        stringResponse.setStatusMessage("Success");
        stringResponse.setResponse("Test data");

        String result = stringResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("statusCode"));
        assertTrue(result.contains("statusMessage"));
        assertTrue(result.contains("response"));
    }

    /**
     * Tests complete response object setup.
     */
    @Test
    void completeResponseObjectSetup() {
        stringResponse.setStatusCode(201);
        stringResponse.setStatusMessage("Created successfully");
        stringResponse.setResponse("New resource created");

        assertEquals(201, stringResponse.getStatusCode());
        assertEquals("Created successfully", stringResponse.getStatusMessage());
        assertEquals("New resource created", stringResponse.getResponse());
    }

    /**
     * Tests response state after multiple modifications.
     */
    @Test
    void responseStateAfterMultipleModifications() {
        // Initial setup
        stringResponse.setStatusCode(200);
        stringResponse.setStatusMessage("Initial message");
        stringResponse.setResponse("Initial data");

        // Modifications
        stringResponse.setStatusCode(404);
        stringResponse.setStatusMessage("Updated message");
        stringResponse.setResponse("Updated data");

        // Verify final state
        assertEquals(404, stringResponse.getStatusCode());
        assertEquals("Updated message", stringResponse.getStatusMessage());
        assertEquals("Updated data", stringResponse.getResponse());
    }

    /**
     * Tests generic type safety with different types.
     */
    @Test
    void genericTypeSafetyWithDifferentTypes() {
        Response<Boolean> booleanResponse = new Response<>();
        booleanResponse.setResponse(true);
        assertTrue(booleanResponse.getResponse());

        Response<Double> doubleResponse = new Response<>();
        doubleResponse.setResponse(3.14159);
        assertEquals(3.14159, doubleResponse.getResponse());

        Response<Character> charResponse = new Response<>();
        charResponse.setResponse('A');
        assertEquals('A', charResponse.getResponse());
    }

    /**
     * Tests different response instances are independent.
     */
    @Test
    void differentResponseInstancesAreIndependent() {
        Response<String> response1 = new Response<>();
        Response<String> response2 = new Response<>();

        response1.setStatusCode(200);
        response1.setStatusMessage("Success");
        response1.setResponse("Data1");

        response2.setStatusCode(404);
        response2.setStatusMessage("Not Found");
        response2.setResponse("Data2");

        assertNotSame(response1, response2);
        assertEquals(200, response1.getStatusCode());
        assertEquals(404, response2.getStatusCode());
        assertEquals("Success", response1.getStatusMessage());
        assertEquals("Not Found", response2.getStatusMessage());
        assertEquals("Data1", response1.getResponse());
        assertEquals("Data2", response2.getResponse());
    }

    /**
     * Tests response with nested generic types.
     */
    @Test
    void responseWithNestedGenericTypesWorksCorrectly() {
        Response<Response<String>> nestedResponse = new Response<>();
        Response<String> innerResponse = new Response<>();
        innerResponse.setStatusCode(200);
        innerResponse.setStatusMessage("Inner success");
        innerResponse.setResponse("Inner data");

        nestedResponse.setStatusCode(200);
        nestedResponse.setStatusMessage("Outer success");
        nestedResponse.setResponse(innerResponse);

        assertEquals(200, nestedResponse.getStatusCode());
        assertEquals("Outer success", nestedResponse.getStatusMessage());
        assertNotNull(nestedResponse.getResponse());
        assertEquals(200, nestedResponse.getResponse().getStatusCode());
        assertEquals("Inner success", nestedResponse.getResponse().getStatusMessage());
        assertEquals("Inner data", nestedResponse.getResponse().getResponse());
    }

    /**
     * Tests typical HTTP response scenarios.
     */
    @Test
    void typicalHttpResponseScenarios() {
        // Success response
        Response<String> successResponse = new Response<>();
        successResponse.setStatusCode(200);
        successResponse.setStatusMessage("OK");
        successResponse.setResponse("Operation completed successfully");

        assertEquals(200, successResponse.getStatusCode());
        assertEquals("OK", successResponse.getStatusMessage());
        assertEquals("Operation completed successfully", successResponse.getResponse());

        // Error response
        Response<String> errorResponse = new Response<>();
        errorResponse.setStatusCode(400);
        errorResponse.setStatusMessage("Bad Request");
        errorResponse.setResponse("Invalid input parameters");

        assertEquals(400, errorResponse.getStatusCode());
        assertEquals("Bad Request", errorResponse.getStatusMessage());
        assertEquals("Invalid input parameters", errorResponse.getResponse());
    }

    /**
     * Tests response with empty and whitespace values.
     */
    @Test
    void responseWithEmptyAndWhitespaceValuesWorksCorrectly() {
        stringResponse.setStatusCode(0);
        stringResponse.setStatusMessage("");
        stringResponse.setResponse("");

        assertEquals(0, stringResponse.getStatusCode());
        assertEquals("", stringResponse.getStatusMessage());
        assertEquals("", stringResponse.getResponse());

        stringResponse.setStatusMessage("   ");
        stringResponse.setResponse("   ");

        assertEquals("   ", stringResponse.getStatusMessage());
        assertEquals("   ", stringResponse.getResponse());
    }
}
