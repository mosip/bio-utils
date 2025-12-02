package io.mosip.kernel.biometrics.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive test class for {@link GSONByteArrayAdapter} providing 100% line coverage.
 * Verifies all serialization and deserialization scenarios including Base64 strings,
 * numeric arrays, null handling, and error cases.
 *
 */
public class GSONByteArrayAdapterTest {

    @Mock
    private JsonDeserializationContext mockDeserializationContext;

    @Mock
    private JsonSerializationContext mockSerializationContext;

    private GSONByteArrayAdapter adapter;
    private Gson gson;
    private Type byteArrayType;

    /**
     * Sets up fixtures before each method execution.
     * Initializes mocks, adapter instance, and Gson with the adapter registered.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new GSONByteArrayAdapter();
        byteArrayType = new TypeToken<byte[]>(){}.getType();

        gson = new GsonBuilder()
                .registerTypeAdapter(byte[].class, adapter)
                .create();
    }

    /**
     * Verifies creation of adapter instance.
     * Ensures that the adapter is properly initialized.
     */
    @Test
    public void shouldCreateAdapterInstance() {
        GSONByteArrayAdapter newAdapter = new GSONByteArrayAdapter();
        assertNotNull(newAdapter);
    }

    /**
     * Verifies deserialization of null JSON element returns null.
     * Ensures proper handling of JSON null values.
     */
    @Test
    public void shouldDeserializeNullJsonAsNull() {
        JsonElement nullElement = JsonNull.INSTANCE;

        byte[] result = adapter.deserialize(nullElement, byteArrayType, mockDeserializationContext);

        assertThat(result, is(nullValue()));
    }

    /**
     * Verifies deserialization of Base64-encoded string.
     * Ensures correct decoding of Base64 strings to byte arrays.
     */
    @Test
    public void shouldDeserializeBase64String() {
        String base64Data = Base64.getEncoder().encodeToString(new byte[]{70, 65, 67});
        JsonElement jsonString = new JsonPrimitive(base64Data);

        byte[] result = adapter.deserialize(jsonString, byteArrayType, mockDeserializationContext);

        assertArrayEquals(new byte[]{70, 65, 67}, result);
    }

    /**
     * Verifies deserialization of empty Base64 string.
     * Ensures correct handling of empty byte arrays encoded as Base64.
     */
    @Test
    public void shouldDeserializeEmptyBase64String() {
        String emptyBase64 = Base64.getEncoder().encodeToString(new byte[0]);
        JsonElement jsonString = new JsonPrimitive(emptyBase64);

        byte[] result = adapter.deserialize(jsonString, byteArrayType, mockDeserializationContext);

        assertArrayEquals(new byte[0], result);
    }

    /**
     * Verifies deserialization of numeric array with positive values.
     * Ensures correct conversion of JSON array to byte array.
     */
    @Test
    public void shouldDeserializeNumericArrayWithPositiveValues() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(70);
        jsonArray.add(65);
        jsonArray.add(67);

        byte[] result = adapter.deserialize(jsonArray, byteArrayType, mockDeserializationContext);

        assertArrayEquals(new byte[]{70, 65, 67}, result);
    }

    /**
     * Verifies deserialization of numeric array with values requiring byte conversion.
     * Ensures correct handling of values that need to be cast to byte (including negative).
     */
    @Test
    public void shouldDeserializeNumericArrayWithByteConversion() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(255);  // Will become -1 when cast to byte
        jsonArray.add(128);  // Will become -128 when cast to byte
        jsonArray.add(0);

        byte[] result = adapter.deserialize(jsonArray, byteArrayType, mockDeserializationContext);

        assertArrayEquals(new byte[]{-1, -128, 0}, result);
    }

    /**
     * Verifies deserialization of empty numeric array.
     * Ensures correct handling of zero-length JSON arrays.
     */
    @Test
    public void shouldDeserializeEmptyNumericArray() {
        JsonArray emptyArray = new JsonArray();

        byte[] result = adapter.deserialize(emptyArray, byteArrayType, mockDeserializationContext);

        assertArrayEquals(new byte[0], result);
    }

    /**
     * Verifies deserialization failure with unsupported JSON element type.
     * Ensures JsonParseException is thrown for unsupported JSON structures.
     */
    @Test
    public void shouldThrowExceptionForUnsupportedJsonType() {
        JsonElement jsonObject = gson.toJsonTree(new Object()); // Creates JsonObject

        JsonParseException exception = assertThrows(JsonParseException.class, () ->
                adapter.deserialize(jsonObject, byteArrayType, mockDeserializationContext));

        assertThat(exception.getMessage().contains("Invalid format for byte array"), is(true));
    }

    /**
     * Verifies serialization of byte array to Base64 string.
     * Ensures correct encoding of byte arrays as Base64 JSON strings.
     */
    @Test
    public void shouldSerializeByteArrayToBase64String() {
        byte[] data = {70, 65, 67};

        JsonElement result = adapter.serialize(data, byteArrayType, mockSerializationContext);

        assertThat(result.isJsonPrimitive(), is(true));
        assertThat(result.getAsString(), is(Base64.getEncoder().encodeToString(data)));
    }

    /**
     * Verifies serialization of empty byte array to empty Base64 string.
     * Ensures correct handling of zero-length byte arrays.
     */
    @Test
    public void shouldSerializeEmptyByteArrayToEmptyBase64() {
        byte[] emptyData = new byte[0];

        JsonElement result = adapter.serialize(emptyData, byteArrayType, mockSerializationContext);

        assertThat(result.isJsonPrimitive(), is(true));
        assertThat(result.getAsString(), is(""));
    }

    /**
     * Verifies serialization of null byte array to JSON null.
     * Ensures proper handling of null input values.
     */
    @Test
    public void shouldSerializeNullByteArrayAsJsonNull() {
        JsonElement result = adapter.serialize(null, byteArrayType, mockSerializationContext);

        assertThat(result.isJsonNull(), is(true));
        assertThat(result, is(JsonNull.INSTANCE));
    }

    /**
     * Verifies end-to-end serialization and deserialization with positive bytes.
     * Ensures round-trip consistency for normal byte values.
     */
    @Test
    public void shouldPerformRoundTripSerializationWithPositiveBytes() {
        byte[] original = {70, 65, 67, 69};

        String json = gson.toJson(original);
        byte[] deserialized = gson.fromJson(json, byte[].class);

        assertArrayEquals(original, deserialized);
    }

    /**
     * Verifies end-to-end serialization and deserialization with negative bytes.
     * Ensures round-trip consistency for byte values including negative ones.
     */
    @Test
    public void shouldPerformRoundTripSerializationWithNegativeBytes() {
        byte[] original = {-1, -128, 127, 0};

        String json = gson.toJson(original);
        byte[] deserialized = gson.fromJson(json, byte[].class);

        assertArrayEquals(original, deserialized);
    }

    /**
     * Verifies end-to-end serialization and deserialization of null values.
     * Ensures round-trip consistency for null byte arrays.
     */
    @Test
    public void shouldPerformRoundTripSerializationWithNull() {
        byte[] original = null;

        String json = gson.toJson(original);
        byte[] deserialized = gson.fromJson(json, byte[].class);

        assertThat(deserialized, is(nullValue()));
    }

    /**
     * Verifies deserialization from JSON with numeric array format.
     * Ensures the adapter can handle manually created JSON with numeric arrays.
     */
    @Test
    public void shouldDeserializeFromNumericArrayJson() {
        String json = "[70, 65, 67, 69]";

        byte[] result = gson.fromJson(json, byte[].class);

        assertArrayEquals(new byte[]{70, 65, 67, 69}, result);
    }

    /**
     * Verifies deserialization from JSON with Base64 string format.
     * Ensures the adapter can handle manually created JSON with Base64 strings.
     */
    @Test
    public void shouldDeserializeFromBase64StringJson() {
        String base64 = Base64.getEncoder().encodeToString(new byte[]{70, 65, 67, 69});
        String json = "\"" + base64 + "\"";

        byte[] result = gson.fromJson(json, byte[].class);

        assertArrayEquals(new byte[]{70, 65, 67, 69}, result);
    }

    /**
     * Verifies adapter integration with complex object containing byte array field.
     * Ensures the adapter works correctly when byte arrays are fields within larger objects.
     */
    @Test
    public void shouldIntegrateWithComplexObjectContainingByteArray() {
        DataContainer original = new DataContainer();
        original.data = new byte[]{1, 2, 3, 4, 5};
        original.name = "sample";

        String json = gson.toJson(original);
        DataContainer deserialized = gson.fromJson(json, DataContainer.class);

        assertThat(deserialized.name, is("sample"));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, deserialized.data);
    }

    /**
     * Verifies handling of invalid Base64 string during deserialization.
     * Ensures proper exception handling when Base64 decoding fails.
     */
    @Test
    public void shouldThrowExceptionForInvalidBase64String() {
        String invalidBase64Json = "\"invalid-base64!@#$%\"";

        assertThrows(Exception.class, () ->
                gson.fromJson(invalidBase64Json, byte[].class));
    }

    /**
     * Simple container class for testing adapter integration with complex objects.
     * Provides a sample class structure for adapter integration verification.
     */
    private static class DataContainer {
        public String name;
        public byte[] data;
    }
}
