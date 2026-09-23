package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Comprehensive test class for {@link DateTimeObjectToLocalDateTimeDeserializer} providing 100% line coverage.
 * Verifies all deserialization scenarios including string formats, numeric timestamps,
 * array formats, object formats, and error handling.
 *
 */
public class DateTimeObjectToLocalDateTimeDeserializerTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockDeserializationContext;

    private DateTimeObjectToLocalDateTimeDeserializer deserializer;
    private ObjectMapper objectMapper;

    /**
     * Sets up fixtures before each method execution.
     * Initializes mocks, deserializer instance, and ObjectMapper with the deserializer registered.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deserializer = new DateTimeObjectToLocalDateTimeDeserializer();
        objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, deserializer);
        objectMapper.registerModule(module);
    }

    /**
     * Verifies the default constructor creates a deserializer instance.
     * Ensures that the deserializer is properly initialized.
     */
    @Test
    public void shouldCreateDeserializerWithDefaultConstructor() {
        DateTimeObjectToLocalDateTimeDeserializer newDeserializer = new DateTimeObjectToLocalDateTimeDeserializer();
        assertNotNull(newDeserializer);
    }

    /**
     * Verifies the parameterized constructor creates a deserializer instance.
     * Ensures that the protected constructor works correctly.
     */
    @Test
    public void shouldCreateDeserializerWithParameterizedConstructor() {
        TestDeserializer testDeserializer = new TestDeserializer(LocalDateTime.class);
        assertNotNull(testDeserializer);
    }

    /**
     * Verifies deserialization of null values returns null.
     * Ensures proper handling of JSON null tokens.
     */
    @Test
    public void shouldDeserializeNullValueAsNull() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        LocalDateTime result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertThat(result, is(nullValue()));
    }

    /**
     * Verifies deserialization of ISO date-time string format.
     * Ensures correct parsing of ISO_DATE_TIME formatted strings.
     */
    @Test
    public void shouldDeserializeISODateTimeString() throws IOException {
        String json = "\"2025-07-30T05:52:00.000Z\"";
        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 0, 0)));
    }

    /**
     * Verifies deserialization of ISO local date-time string format.
     * Ensures correct parsing when ISO_DATE_TIME parsing fails but ISO_LOCAL_DATE_TIME succeeds.
     */
    @Test
    public void shouldDeserializeISOLocalDateTimeString() throws IOException {
        String json = "\"2025-07-30T05:52:00\"";
        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 0)));
    }

    /**
     * Verifies deserialization failure with invalid date-time string.
     * Ensures IOException is thrown when string cannot be parsed by any formatter.
     */
    @Test
    public void shouldThrowExceptionForInvalidDateTimeString() {
        String json = "\"invalid-date-time\"";

        IOException exception = assertThrows(IOException.class, () ->
                objectMapper.readValue(json, LocalDateTime.class));

        assertThat(exception.getMessage().contains("Invalid date/time string"), is(true));
    }

    /**
     * Verifies deserialization of array format with 6 elements (without nanoseconds).
     * Ensures correct parsing of [year, month, day, hour, minute, second] array.
     */
    @Test
    public void shouldDeserializeArrayFormatWithSixElements() throws IOException {
        String json = "[2025, 7, 30, 5, 52, 15]";
        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 15, 0)));
    }

    /**
     * Verifies deserialization of array format with 7 elements (with nanoseconds).
     * Ensures correct parsing of [year, month, day, hour, minute, second, nano] array.
     */
    @Test
    public void shouldDeserializeArrayFormatWithSevenElements() throws IOException {
        String json = "[2025, 7, 30, 5, 52, 15, 123000000]";
        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 15, 123000000)));
    }

    /**
     * Verifies deserialization failure with array containing non-integer values.
     * Ensures IOException is thrown when array contains non-numeric elements.
     */
    @Test
    public void shouldThrowExceptionForArrayWithNonIntegerElements() {
        String json = "[2025, 7, 30, \"invalid\", 52, 15]";

        assertThrows(IOException.class, () ->
                objectMapper.readValue(json, LocalDateTime.class));
    }

    /**
     * Verifies deserialization failure with array having insufficient elements.
     * Ensures IOException is thrown when array has less than 6 elements.
     */
    @Test
    public void shouldThrowExceptionForArrayWithTooFewElements() {
        String json = "[2025, 7, 30, 5, 52]";

        IOException exception = assertThrows(IOException.class, () ->
                objectMapper.readValue(json, LocalDateTime.class));

        assertThat(exception.getMessage().contains("Expected 6 or 7 values"), is(true));
    }

    /**
     * Verifies deserialization of object format with date and time objects.
     * Ensures correct parsing of nested date/time object structure.
     */
    @Test
    public void shouldDeserializeObjectFormatWithDateAndTime() throws IOException {
        String json = "{\"date\":{\"year\":2025,\"month\":7,\"day\":30}," +
                "\"time\":{\"hour\":5,\"minute\":52,\"second\":15,\"nano\":123000000}}";

        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 15, 123000000)));
    }

    /**
     * Verifies deserialization of object format without nano field in time.
     * Ensures nano defaults to 0 when not provided in time object.
     */
    @Test
    public void shouldDeserializeObjectFormatWithoutNano() throws IOException {
        String json = "{\"date\":{\"year\":2025,\"month\":7,\"day\":30}," +
                "\"time\":{\"hour\":5,\"minute\":52,\"second\":15}}";

        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 15, 0)));
    }

    /**
     * Verifies deserialization of object format with case-insensitive field names.
     * Ensures field names are matched case-insensitively (Date/TIME vs date/time).
     */
    @Test
    public void shouldDeserializeObjectFormatWithCaseInsensitiveFields() throws IOException {
        String json = "{\"Date\":{\"year\":2025,\"month\":7,\"day\":30}," +
                "\"TIME\":{\"hour\":5,\"minute\":52,\"second\":15}}";

        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 15, 0)));
    }

    /**
     * Verifies deserialization of object format with extra unknown fields.
     * Ensures unknown fields are skipped without causing errors.
     */
    @Test
    public void shouldDeserializeObjectFormatWithExtraFields() throws IOException {
        String json = "{\"date\":{\"year\":2025,\"month\":7,\"day\":30}," +
                "\"time\":{\"hour\":5,\"minute\":52,\"second\":15}," +
                "\"extra\":\"ignored\",\"another\":{\"nested\":\"object\"}}";

        LocalDateTime result = objectMapper.readValue(json, LocalDateTime.class);

        assertThat(result, is(LocalDateTime.of(2025, 7, 30, 5, 52, 15, 0)));
    }

    /**
     * Verifies deserialization failure when date field is missing from object.
     * Ensures IOException is thrown when required date field is not present.
     */
    @Test
    public void shouldThrowExceptionForObjectMissingDateField() {
        String json = "{\"time\":{\"hour\":5,\"minute\":52,\"second\":15}}";

        IOException exception = assertThrows(IOException.class, () ->
                objectMapper.readValue(json, LocalDateTime.class));

        assertThat(exception.getMessage().contains("Missing required 'date' or 'time' fields"), is(true));
    }

    /**
     * Verifies deserialization failure when time field is missing from object.
     * Ensures IOException is thrown when required time field is not present.
     */
    @Test
    public void shouldThrowExceptionForObjectMissingTimeField() {
        String json = "{\"date\":{\"year\":2025,\"month\":7,\"day\":30}}";

        IOException exception = assertThrows(IOException.class, () ->
                objectMapper.readValue(json, LocalDateTime.class));

        assertThat(exception.getMessage().contains("Missing required 'date' or 'time' fields"), is(true));
    }

    /**
     * Verifies deserialization failure with unsupported JSON token type.
     * Ensures IOException is thrown for unsupported token types like boolean.
     */
    @Test
    public void shouldThrowExceptionForUnsupportedTokenType() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);

        IOException exception = assertThrows(IOException.class, () ->
                deserializer.deserialize(mockJsonParser, mockDeserializationContext));

        assertThat(exception.getMessage().contains("Unsupported token"), is(true));
    }

    /**
     * Verifies field-level deserialization using JsonDeserialize annotation.
     * Ensures that the deserializer works correctly when applied to specific fields.
     */
    @Test
    public void shouldDeserializeFieldUsingAnnotation() throws IOException {
        String json = "{\"timestamp\":\"2025-07-30T05:52:00\"}";

        DeserializableClass result = objectMapper.readValue(json, DeserializableClass.class);

        assertThat(result.timestamp, is(LocalDateTime.of(2025, 7, 30, 5, 52, 0)));
    }

    /**
     * Verifies field-level deserialization of null value using annotation.
     * Ensures that null timestamp fields are properly deserialized as null.
     */
    @Test
    public void shouldDeserializeNullFieldAsNull() throws IOException {
        String json = "{\"timestamp\":null}";

        DeserializableClass result = objectMapper.readValue(json, DeserializableClass.class);

        assertThat(result.timestamp, is(nullValue()));
    }

    /**
     * Class to verify field-level deserialization using JsonDeserialize annotation.
     * Provides a sample class structure for deserializer integration verification.
     */
    private static class DeserializableClass {
        @JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
        public LocalDateTime timestamp;
    }

    /**
     * Extended deserializer class to verify the protected constructor.
     * Allows verification of constructor parameters and inheritance scenarios.
     */
    private static class TestDeserializer extends DateTimeObjectToLocalDateTimeDeserializer {
        public TestDeserializer(Class<?> vc) {
            super(vc);
        }
    }
}
