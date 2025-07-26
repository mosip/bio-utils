package io.mosip.biosdk.client.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * A Jackson serializer that converts a byte array to a JSON array of integers.
 * Each byte is treated as an unsigned integer (0 to 255) by default.
 *
 */
public class ByteArrayToIntArraySerializer extends StdSerializer<byte[]> {

    private final boolean useUnsigned;

    /**
     * Constructs a serializer for byte arrays with default unsigned integer conversion.
     */
    public ByteArrayToIntArraySerializer() {
        this(byte[].class, false);
    }

    /**
     * Constructs a serializer for byte arrays with configurable signed/unsigned conversion.
     *
     * @param t           the class type (byte[])
     * @param useUnsigned true to treat bytes as unsigned (0 to 255), false for signed (-128 to 127)
     */
    protected ByteArrayToIntArraySerializer(Class<byte[]> t, boolean useUnsigned) {
        super(t);
        this.useUnsigned = useUnsigned;
    }

    /**
     * Serializes a byte array to a JSON array of integers.
     *
     * @param value    the byte array to serialize
     * @param gen      the JSON generator
     * @param provider the serializer provider
     * @throws IOException if the input is null or serialization fails
     */
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray();
        for (byte b : value) {
            // Convert byte to int: unsigned (0 to 255) or signed (-128 to 127)
            int intValue = useUnsigned ? Byte.toUnsignedInt(b) : b;
            gen.writeNumber(intValue);
        }
        gen.writeEndArray();
    }
}