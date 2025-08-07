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
import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Comprehensive test class for {@link IntArrayToByteArrayDeserializer} providing 100% line coverage.
 * Verifies all deserialization scenarios including integer arrays, Base64 strings,
 * signed/unsigned modes, and error handling.
 */
public class IntArrayToByteArrayDeserializerTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockDeserializationContext;

    private IntArrayToByteArrayDeserializer signedDeserializer;
    private IntArrayToByteArrayDeserializer unsignedDeserializer;
    private ObjectMapper objectMapper;

    /**
     * Sets up fixtures before each method execution.
     * Initializes mocks, deserializer instances, and ObjectMapper with deserializers registered.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signedDeserializer = new IntArrayToByteArrayDeserializer();
        unsignedDeserializer = new UnsignedIntArrayToByteArrayDeserializer(byte[].class, true);
        objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(byte[].class, signedDeserializer);
        objectMapper.registerModule(module);
    }

    /**
     * Verifies the default constructor creates a deserializer with signed mode.
     * Ensures that the deserializer instance is properly initialized.
     */
    @Test
    public void shouldCreateDeserializerWithDefaultConstructor() {
        IntArrayToByteArrayDeserializer deserializer = new IntArrayToByteArrayDeserializer();
        assertNotNull(deserializer);
    }

    /**
     * Verifies the protected constructor creates a deserializer instance.
     * Ensures that the protected constructor works correctly with parameters.
     */
    @Test
    public void shouldCreateDeserializerWithProtectedConstructor() {
        UnsignedIntArrayToByteArrayDeserializer deserializer =
                new UnsignedIntArrayToByteArrayDeserializer(byte[].class, false);
        assertNotNull(deserializer);
    }

    /**
     * Verifies deserialization of Base64 string to byte array.
     * Ensures correct decoding of Base64-encoded strings.
     */
    @Test
    public void shouldDeserializeBase64String() throws IOException {
        byte[] originalData = {70, 65, 67, 69};
        String base64String = Base64.getEncoder().encodeToString(originalData);
        String json = "\"" + base64String + "\"";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(originalData, result);
    }

    /**
     * Verifies deserialization of empty Base64 string returns empty array.
     * Ensures correct handling of empty Base64 strings.
     */
    @Test
    public void shouldDeserializeEmptyBase64StringAsEmptyArray() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("");

        byte[] result = signedDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertArrayEquals(new byte[0], result);
    }

    /**
     * Verifies deserialization of null Base64 string returns empty array.
     * Ensures correct handling of null Base64 strings.
     */
    @Test
    public void shouldDeserializeNullBase64StringAsEmptyArray() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(null);

        byte[] result = signedDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertArrayEquals(new byte[0], result);
    }

    /**
     * Verifies deserialization of positive integer array in signed mode.
     * Ensures correct conversion of positive integers to bytes.
     */
    @Test
    public void shouldDeserializePositiveIntegerArrayInSignedMode() throws IOException {
        String json = "[70, 65, 67, 69, 127]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{70, 65, 67, 69, 127}, result);
    }

    /**
     * Verifies deserialization of negative integer array in signed mode.
     * Ensures correct conversion of negative integers to bytes.
     */
    @Test
    public void shouldDeserializeNegativeIntegerArrayInSignedMode() throws IOException {
        String json = "[-1, -50, -100, -128]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{-1, -50, -100, -128}, result);
    }

    /**
     * Verifies deserialization of mixed integer array in signed mode.
     * Ensures correct conversion of both positive and negative integers.
     */
    @Test
    public void shouldDeserializeMixedIntegerArrayInSignedMode() throws IOException {
        String json = "[-128, -1, 0, 1, 127]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{-128, -1, 0, 1, 127}, result);
    }

    /**
     * Verifies deserialization of integer array in unsigned mode.
     * Ensures correct conversion of unsigned integers (0-255) to bytes.
     */
    @Test
    public void shouldDeserializeIntegerArrayInUnsignedMode() throws IOException {
        ObjectMapper unsignedMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(byte[].class, unsignedDeserializer);
        unsignedMapper.registerModule(module);

        String json = "[0, 128, 255]";
        byte[] result = unsignedMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{0, -128, -1}, result);
    }

    /**
     * Verifies deserialization of empty integer array returns empty byte array.
     * Ensures correct handling of zero-length arrays.
     */
    @Test
    public void shouldDeserializeEmptyIntegerArrayAsEmptyByteArray() throws IOException {
        String json = "[]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[0], result);
    }

    /**
     * Verifies deserialization failure with non-array JSON token.
     * Ensures IOException is thrown for unsupported JSON structures.
     */
    @Test
    public void shouldThrowExceptionForNonArrayToken() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        IOException exception = assertThrows(IOException.class, () ->
                signedDeserializer.deserialize(mockJsonParser, mockDeserializationContext));

        assertThat(exception.getMessage().contains("Expected JSON array"), is(true));
    }

    /**
     * Verifies deserialization failure with non-integer values in array.
     * Ensures IOException is thrown when array contains non-numeric elements.
     */
    @Test
    public void shouldThrowExceptionForNonIntegerValuesInArray() {
        String json = "[70, \"invalid\", 67]";

        assertThrows(IOException.class, () ->
                objectMapper.readValue(json, byte[].class));
    }

    /**
     * Verifies deserialization failure with out-of-range positive value in signed mode.
     * Ensures IOException is thrown for values exceeding signed byte range.
     */
    @Test
    public void shouldThrowExceptionForOutOfRangePositiveValueInSignedMode() {
        String json = "[128]";

        IOException exception = assertThrows(IOException.class, () ->
                objectMapper.readValue(json, byte[].class));

        assertThat(exception.getMessage().contains("Signed byte value out of range"), is(true));
    }

    /**
     * Verifies deserialization failure with out-of-range negative value in signed mode.
     * Ensures IOException is thrown for values below signed byte range.
     */
    @Test
    public void shouldThrowExceptionForOutOfRangeNegativeValueInSignedMode() {
        String json = "[-129]";

        IOException exception = assertThrows(IOException.class, () ->
                objectMapper.readValue(json, byte[].class));

        assertThat(exception.getMessage().contains("Signed byte value out of range"), is(true));
    }

    /**
     * Verifies deserialization failure with negative value in unsigned mode.
     * Ensures IOException is thrown for negative values in unsigned mode.
     */
    @Test
    public void shouldThrowExceptionForNegativeValueInUnsignedMode() throws IOException {
        when(mockJsonParser.getCurrentToken())
                .thenReturn(JsonToken.START_ARRAY)
                .thenReturn(JsonToken.VALUE_NUMBER_INT)
                .thenReturn(JsonToken.END_ARRAY);
        when(mockJsonParser.nextToken())
                .thenReturn(JsonToken.VALUE_NUMBER_INT)
                .thenReturn(JsonToken.END_ARRAY);
        when(mockJsonParser.getIntValue()).thenReturn(-1);

        IOException exception = assertThrows(IOException.class, () ->
                unsignedDeserializer.deserialize(mockJsonParser, mockDeserializationContext));

        assertThat(exception.getMessage().contains("Unsigned byte value out of range"), is(true));
    }

    /**
     * Verifies deserialization failure with out-of-range positive value in unsigned mode.
     * Ensures IOException is thrown for values exceeding unsigned byte range.
     */
    @Test
    public void shouldThrowExceptionForOutOfRangePositiveValueInUnsignedMode() throws IOException {
        when(mockJsonParser.getCurrentToken())
                .thenReturn(JsonToken.START_ARRAY)
                .thenReturn(JsonToken.VALUE_NUMBER_INT)
                .thenReturn(JsonToken.END_ARRAY);
        when(mockJsonParser.nextToken())
                .thenReturn(JsonToken.VALUE_NUMBER_INT)
                .thenReturn(JsonToken.END_ARRAY);
        when(mockJsonParser.getIntValue()).thenReturn(256);

        IOException exception = assertThrows(IOException.class, () ->
                unsignedDeserializer.deserialize(mockJsonParser, mockDeserializationContext));

        assertThat(exception.getMessage().contains("Unsigned byte value out of range"), is(true));
    }

    /**
     * Verifies field-level deserialization using JsonDeserialize annotation.
     * Ensures that the deserializer works correctly when applied to specific fields.
     */
    @Test
    public void shouldDeserializeFieldUsingAnnotation() throws IOException {
        String json = "{\"data\":[70, 65, 67, 69]}";

        DeserializableClass result = objectMapper.readValue(json, DeserializableClass.class);

        assertArrayEquals(new byte[]{70, 65, 67, 69}, result.data);
    }

    /**
     * Verifies handling of single element array.
     * Ensures correct processing of arrays with just one element.
     */
    @Test
    public void shouldDeserializeSingleElementArray() throws IOException {
        String json = "[42]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{42}, result);
    }

    /**
     * Verifies handling of large arrays.
     * Ensures correct processing of arrays with multiple elements.
     */
    @Test
    public void shouldDeserializeLargeArray() throws IOException {
        String json = "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, result);
    }

    /**
     * Verifies boundary values in signed mode.
     * Ensures correct handling of minimum and maximum signed byte values.
     */
    @Test
    public void shouldDeserializeBoundaryValuesInSignedMode() throws IOException {
        String json = "[-128, 127]";

        byte[] result = objectMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{-128, 127}, result);
    }

    /**
     * Verifies boundary values in unsigned mode.
     * Ensures correct handling of minimum and maximum unsigned byte values.
     */
    @Test
    public void shouldDeserializeBoundaryValuesInUnsignedMode() throws IOException {
        ObjectMapper unsignedMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(byte[].class, unsignedDeserializer);
        unsignedMapper.registerModule(module);

        String json = "[0, 255]";
        byte[] result = unsignedMapper.readValue(json, byte[].class);

        assertArrayEquals(new byte[]{0, -1}, result);
    }

    /**
     * Class to verify field-level deserialization using JsonDeserialize annotation.
     * Provides a sample class structure for deserializer integration verification.
     */
    private static class DeserializableClass {
        @JsonDeserialize(using = IntArrayToByteArrayDeserializer.class)
        public byte[] data;
    }

    /**
     * Extended deserializer class to verify the protected constructor.
     * Allows verification of constructor parameters and inheritance scenarios.
     */
    private static class UnsignedIntArrayToByteArrayDeserializer extends IntArrayToByteArrayDeserializer {
        public UnsignedIntArrayToByteArrayDeserializer(Class<byte[]> t, boolean useUnsigned) {
            super(t, useUnsigned);
        }
    }
}
