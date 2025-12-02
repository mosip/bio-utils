package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Comprehensive test class for {@link LocalDateTimeToDateTimeObjectSerializer} providing 100% line coverage.
 * Verifies all serialization scenarios including structured date-time objects, null handling,
 * and integration with Jackson ObjectMapper.
 *
 */
public class LocalDateTimeToDateTimeObjectSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    private LocalDateTimeToDateTimeObjectSerializer serializer;
    private ObjectMapper objectMapper;

    /**
     * Sets up fixtures before each method execution.
     * Initializes mocks, serializer instance, and ObjectMapper with the serializer registered.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        serializer = new LocalDateTimeToDateTimeObjectSerializer();
        objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, serializer);
        objectMapper.registerModule(module);
    }

    /**
     * Verifies the default constructor creates a serializer instance.
     * Ensures that the serializer is properly initialized.
     */
    @Test
    public void shouldCreateSerializerWithDefaultConstructor() {
        LocalDateTimeToDateTimeObjectSerializer newSerializer = new LocalDateTimeToDateTimeObjectSerializer();
        assertNotNull(newSerializer);
    }

    /**
     * Verifies the protected constructor creates a serializer instance.
     * Ensures that the protected constructor works correctly with parameters.
     */
    @Test
    public void shouldCreateSerializerWithProtectedConstructor() {
        TestLocalDateTimeToDateTimeObjectSerializer testSerializer =
                new TestLocalDateTimeToDateTimeObjectSerializer(LocalDateTime.class);
        assertNotNull(testSerializer);
    }

    /**
     * Verifies serialization of null LocalDateTime writes null to JSON output.
     * Ensures proper handling of null input values.
     */
    @Test
    public void shouldSerializeNullLocalDateTimeAsNull() throws IOException {
        serializer.serialize(null, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNull();
        verifyNoMoreInteractions(mockJsonGenerator);
    }

    /**
     * Verifies integration with Jackson ObjectMapper for complete serialization.
     * Ensures end-to-end serialization produces correct JSON structure.
     */
    @Test
    public void shouldIntegrateWithObjectMapperForCompleteSerialization() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2025, 8, 2, 15, 45, 12, 123000000);

        String json = objectMapper.writeValueAsString(dateTime);

        String expectedJson = "{\"date\":{\"year\":2025,\"month\":8,\"day\":2}," +
                "\"time\":{\"hour\":15,\"minute\":45,\"second\":12,\"nano\":123000000}}";

        assertThat(json, is(expectedJson));
    }

    /**
     * Verifies integration with ObjectMapper for null LocalDateTime serialization.
     * Ensures null values are properly serialized as JSON null.
     */
    @Test
    public void shouldIntegrateWithObjectMapperForNullSerialization() throws Exception {
        LocalDateTime dateTime = null;

        String json = objectMapper.writeValueAsString(dateTime);

        assertThat(json, is("null"));
    }

    /**
     * Verifies field-level serialization using JsonSerialize annotation.
     * Ensures that the serializer works correctly when applied to specific fields.
     */
    @Test
    public void shouldSerializeFieldUsingAnnotation() throws Exception {
        SerializableClass object = new SerializableClass();
        object.timestamp = LocalDateTime.of(2025, 8, 2, 15, 45, 12, 123000000);

        String json = objectMapper.writeValueAsString(object);

        assertTrue(json.contains("\"timestamp\""));
        assertTrue(json.contains("\"date\":{\"year\":2025,\"month\":8,\"day\":2}"));
        assertTrue(json.contains("\"time\":{\"hour\":15,\"minute\":45,\"second\":12,\"nano\":123000000}"));
    }

    /**
     * Verifies field-level serialization of null value using annotation.
     * Ensures that null timestamp fields are properly serialized as null JSON values.
     */
    @Test
    public void shouldSerializeNullFieldAsNull() throws Exception {
        SerializableClass object = new SerializableClass();
        object.timestamp = null;

        String json = objectMapper.writeValueAsString(object);

        assertThat(json, is("{\"timestamp\":null}"));
    }

    /**
     * Verifies serialization with leap year date.
     * Ensures correct handling of February 29 in leap years.
     */
    @Test
    public void shouldSerializeLeapYearDate() throws IOException {
        LocalDateTime leapYearDateTime = LocalDateTime.of(2024, 2, 29, 12, 30, 45, 500000000);

        InOrder inOrder = inOrder(mockJsonGenerator);

        serializer.serialize(leapYearDateTime, mockJsonGenerator, mockSerializerProvider);

        inOrder.verify(mockJsonGenerator).writeStartObject();
        inOrder.verify(mockJsonGenerator).writeFieldName("date");
        inOrder.verify(mockJsonGenerator).writeStartObject();
        inOrder.verify(mockJsonGenerator).writeNumberField("year", 2024);
        inOrder.verify(mockJsonGenerator).writeNumberField("month", 2);
        inOrder.verify(mockJsonGenerator).writeNumberField("day", 29);
    }

    /**
     * Verifies serialization preserves nanosecond precision.
     * Ensures that nanosecond values are correctly serialized without loss of precision.
     */
    @Test
    public void shouldSerializeWithNanosecondPrecision() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0, 123456789);

        InOrder inOrder = inOrder(mockJsonGenerator);

        serializer.serialize(dateTime, mockJsonGenerator, mockSerializerProvider);

        inOrder.verify(mockJsonGenerator).writeStartObject();
        inOrder.verify(mockJsonGenerator).writeFieldName("date");
        inOrder.verify(mockJsonGenerator).writeStartObject();
        inOrder.verify(mockJsonGenerator).writeNumberField("year", 2025);
        inOrder.verify(mockJsonGenerator).writeNumberField("month", 1);
        inOrder.verify(mockJsonGenerator).writeNumberField("day", 1);
        inOrder.verify(mockJsonGenerator).writeEndObject();
        inOrder.verify(mockJsonGenerator).writeFieldName("time");
        inOrder.verify(mockJsonGenerator).writeStartObject();
        inOrder.verify(mockJsonGenerator).writeNumberField("hour", 0);
        inOrder.verify(mockJsonGenerator).writeNumberField("minute", 0);
        inOrder.verify(mockJsonGenerator).writeNumberField("second", 0);
        inOrder.verify(mockJsonGenerator).writeNumberField("nano", 123456789);
    }

    /**
     * Verifies serialization of current timestamp.
     * Ensures the serializer works with dynamically created LocalDateTime instances.
     */
    @Test
    public void shouldSerializeCurrentTimestamp() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        String json = objectMapper.writeValueAsString(now);

        assertTrue(json.contains("\"date\""));
        assertTrue(json.contains("\"time\""));
        assertTrue(json.contains("\"year\":" + now.getYear()));
        assertTrue(json.contains("\"month\":" + now.getMonthValue()));
        assertTrue(json.contains("\"day\":" + now.getDayOfMonth()));
    }

    /**
     * Verifies serialization within complex object structure.
     * Ensures the serializer works correctly when LocalDateTime is part of a larger object.
     */
    @Test
    public void shouldSerializeWithinComplexObjectStructure() throws Exception {
        ComplexObject complex = new ComplexObject();
        complex.id = 123;
        complex.name = "sample";
        complex.createdAt = LocalDateTime.of(2025, 8, 2, 15, 45, 12);

        String json = objectMapper.writeValueAsString(complex);

        assertTrue(json.contains("\"id\":123"));
        assertTrue(json.contains("\"name\":\"sample\""));
        assertTrue(json.contains("\"createdAt\":{\"date\":{\"year\":2025,\"month\":8,\"day\":2}"));
    }

    /**
     * Class to verify field-level serialization using JsonSerialize annotation.
     * Provides a sample class structure for serializer integration verification.
     */
    private static class SerializableClass {
        @JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
        public LocalDateTime timestamp;
    }

    /**
     * Complex class to verify serialization within larger object structures.
     * Provides a comprehensive example of serializer integration in real-world scenarios.
     */
    private static class ComplexObject {
        public int id;
        public String name;

        @JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
        public LocalDateTime createdAt;
    }

    /**
     * Extended serializer class to verify the protected constructor.
     * Allows verification of constructor parameters and inheritance scenarios.
     */
    private static class TestLocalDateTimeToDateTimeObjectSerializer extends LocalDateTimeToDateTimeObjectSerializer {
        public TestLocalDateTimeToDateTimeObjectSerializer(Class<LocalDateTime> t) {
            super(t);
        }
    }
}
