package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Comprehensive test class for {@link ByteArrayToIntArraySerializer} providing 100% line coverage.
 * Tests all serialization scenarios including signed/unsigned modes, null handling,
 * and integration with Jackson ObjectMapper.
 *
 */
public class ByteArrayToIntArraySerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    private ByteArrayToIntArraySerializer signedSerializer;
    private ByteArrayToIntArraySerializer unsignedSerializer;
    private ObjectMapper objectMapper;

    /**
     * Sets up fixtures before each method execution.
     * Initializes mocks and creates both signed and unsigned serializer instances.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signedSerializer = new ByteArrayToIntArraySerializer();
        unsignedSerializer = new UnsignedByteArrayToIntArraySerializer(byte[].class, true);
        objectMapper = new ObjectMapper();
    }

    /**
     * Verifies the default constructor creates a serializer with signed mode.
     * Ensures that the serializer instance is properly initialized.
     */
    @Test
    public void shouldCreateSerializerWithDefaultConstructor() {
        ByteArrayToIntArraySerializer serializer = new ByteArrayToIntArraySerializer();
        assertNotNull(serializer);
    }

    /**
     * Verifies serialization of null byte array writes null to JSON output.
     * Ensures proper handling of null input values.
     */
    @Test
    public void shouldSerializeNullByteArrayAsNull() throws IOException {
        signedSerializer.serialize(null, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNull();
        verifyNoMoreInteractions(mockJsonGenerator);
    }

    /**
     * Verifies serialization of empty byte array produces empty JSON array.
     * Ensures correct handling of zero-length arrays.
     */
    @Test
    public void shouldSerializeEmptyByteArrayAsEmptyArray() throws IOException {
        byte[] emptyArray = new byte[0];

        signedSerializer.serialize(emptyArray, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeEndArray();
        verifyNoMoreInteractions(mockJsonGenerator);
    }

    /**
     * Verifies serialization of positive byte values in signed mode.
     * Ensures that positive bytes are converted to their integer equivalents.
     */
    @Test
    public void shouldSerializePositiveBytesInSignedMode() throws IOException {
        byte[] positiveBytes = {1, 50, 100, 127};

        signedSerializer.serialize(positiveBytes, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeNumber(1);
        verify(mockJsonGenerator).writeNumber(50);
        verify(mockJsonGenerator).writeNumber(100);
        verify(mockJsonGenerator).writeNumber(127);
        verify(mockJsonGenerator).writeEndArray();
    }

    /**
     * Verifies serialization of negative byte values in signed mode.
     * Ensures that negative bytes maintain their signed integer representation.
     */
    @Test
    public void shouldSerializeNegativeBytesInSignedMode() throws IOException {
        byte[] negativeBytes = {-1, -50, -100, -128};

        signedSerializer.serialize(negativeBytes, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeNumber(-1);
        verify(mockJsonGenerator).writeNumber(-50);
        verify(mockJsonGenerator).writeNumber(-100);
        verify(mockJsonGenerator).writeNumber(-128);
        verify(mockJsonGenerator).writeEndArray();
    }

    /**
     * Verifies serialization of negative byte values in unsigned mode.
     * Ensures that negative bytes are converted to their unsigned integer equivalents (0-255).
     */
    @Test
    public void shouldSerializeNegativeBytesInUnsignedMode() throws IOException {
        byte[] negativeBytes = {-1, -50, -100, -128};

        unsignedSerializer.serialize(negativeBytes, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeNumber(255);   // -1 as unsigned
        verify(mockJsonGenerator).writeNumber(206);   // -50 as unsigned
        verify(mockJsonGenerator).writeNumber(156);   // -100 as unsigned
        verify(mockJsonGenerator).writeNumber(128);   // -128 as unsigned
        verify(mockJsonGenerator).writeEndArray();
    }

    /**
     * Verifies serialization of mixed positive and negative bytes in signed mode.
     * Ensures correct handling of arrays containing both positive and negative values.
     */
    @Test
    public void shouldSerializeMixedBytesInSignedMode() throws IOException {
        byte[] mixedBytes = {-128, -1, 0, 1, 127};

        signedSerializer.serialize(mixedBytes, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeNumber(-128);
        verify(mockJsonGenerator).writeNumber(-1);
        verify(mockJsonGenerator).writeNumber(0);
        verify(mockJsonGenerator).writeNumber(1);
        verify(mockJsonGenerator).writeNumber(127);
        verify(mockJsonGenerator).writeEndArray();
    }

    /**
     * Verifies serialization of mixed positive and negative bytes in unsigned mode.
     * Ensures correct conversion of all byte values to unsigned range (0-255).
     */
    @Test
    public void shouldSerializeMixedBytesInUnsignedMode() throws IOException {
        byte[] mixedBytes = {-128, -1, 0, 1, 127};

        unsignedSerializer.serialize(mixedBytes, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeNumber(128);   // -128 as unsigned
        verify(mockJsonGenerator).writeNumber(255);   // -1 as unsigned
        verify(mockJsonGenerator).writeNumber(0);
        verify(mockJsonGenerator).writeNumber(1);
        verify(mockJsonGenerator).writeNumber(127);
        verify(mockJsonGenerator).writeEndArray();
    }

    /**
     * Verifies integration with Jackson ObjectMapper using signed serializer.
     * Ensures end-to-end serialization produces correct JSON output.
     */
    @Test
    public void shouldIntegrateWithObjectMapperInSignedMode() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(byte[].class, new ByteArrayToIntArraySerializer());
        objectMapper.registerModule(module);

        byte[] data = {70, 65, 67, 0, 48, 51, -1, -128};
        String json = objectMapper.writeValueAsString(data);

        assertThat(json, is("[70,65,67,0,48,51,-1,-128]"));
    }

    /**
     * Verifies integration with Jackson ObjectMapper using unsigned serializer.
     * Ensures end-to-end serialization with unsigned conversion produces correct JSON.
     */
    @Test
    public void shouldIntegrateWithObjectMapperInUnsignedMode() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(byte[].class, new UnsignedByteArrayToIntArraySerializer(byte[].class, true));
        objectMapper.registerModule(module);

        byte[] data = {70, 65, 67, 0, 48, 51, -1, -128};
        String json = objectMapper.writeValueAsString(data);

        assertThat(json, is("[70,65,67,0,48,51,255,128]"));
    }

    /**
     * Verifies field-level serialization using JsonSerialize annotation.
     * Ensures that the serializer works correctly when applied to specific fields.
     */
    @Test
    public void shouldSerializeFieldUsingAnnotation() throws Exception {
        SerializableClass object = new SerializableClass();
        object.challengeResponse = new byte[]{-1, 0, 1, 127, -128};

        String json = objectMapper.writeValueAsString(object);

        assertThat(json, is("{\"challengeResponse\":[-1,0,1,127,-128]}"));
    }

    /**
     * Verifies serialization of null field using JsonSerialize annotation.
     * Ensures that null byte array fields are properly serialized as null JSON values.
     */
    @Test
    public void shouldSerializeNullFieldAsNull() throws Exception {
        SerializableClass object = new SerializableClass();
        object.challengeResponse = null;

        String json = objectMapper.writeValueAsString(object);

        assertThat(json, is("{\"challengeResponse\":null}"));
    }

    /**
     * Verifies the protected constructor with unsigned flag set to false.
     * Ensures that explicitly setting signed mode works correctly.
     */
    @Test
    public void shouldCreateSerializerWithProtectedConstructorInSignedMode() throws IOException {
        UnsignedByteArrayToIntArraySerializer serializer = new UnsignedByteArrayToIntArraySerializer(byte[].class, false);
        byte[] data = {-1};

        serializer.serialize(data, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartArray();
        verify(mockJsonGenerator).writeNumber(-1);
        verify(mockJsonGenerator).writeEndArray();
    }

    /**
     * Class to verify field-level serialization using JsonSerialize annotation.
     * Provides a sample class structure for serializer integration verification.
     */
    private static class SerializableClass {
        @JsonSerialize(using = ByteArrayToIntArraySerializer.class)
        public byte[] challengeResponse;
    }

    /**
     * Extended serializer class to verify the protected constructor.
     * Allows verification of constructor parameters and inheritance scenarios.
     */
    private static class UnsignedByteArrayToIntArraySerializer extends ByteArrayToIntArraySerializer {
        public UnsignedByteArrayToIntArraySerializer(Class<byte[]> t, boolean useUnsigned) {
            super(t, useUnsigned);
        }
    }
}
