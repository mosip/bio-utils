package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * A custom Jackson {@link StdSerializer} implementation that converts
 * a {@code byte[]} field into a JSON array of integers during serialization.
 * <p>
 * By default, Jackson serializes byte arrays as Base64-encoded strings,
 * which is often not desirable when interoperability with other services
 * or APIs (expecting numeric byte values) is required. This serializer
 * overrides the default behavior and converts each byte to its integer
 * representation, producing a JSON array output.
 * <p>
 * The serializer supports both:
 * <ul>
 *     <li><b>Signed mode</b> (default): produces integer values in the range
 *     -128 to 127.</li>
 *     <li><b>Unsigned mode</b>: produces integer values in the range
 *     0 to 255, converting bytes via {@link Byte#toUnsignedInt(byte)}.</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * SimpleModule module = new SimpleModule();
 * module.addSerializer(byte[].class, new ByteArrayToIntArraySerializer());
 * mapper.registerModule(module);
 *
 * byte[] data = {70, 65, 67, 0, 48, 51};
 * String json = mapper.writeValueAsString(data);
 * // Output: [70,65,67,0,48,51]
 * }</pre>
 *
 * To use on a specific field:
 * <pre>{@code
 * @JsonSerialize(using = ByteArrayToIntArraySerializer.class)
 * private byte[] challengeResponse;
 * }</pre>
 *
 * @author Janardhan B S
 * @since 1.0
 */
public class ByteArrayToIntArraySerializer extends StdSerializer<byte[]> {

    private final boolean useUnsigned;

    /**
     * Default constructor using signed integer conversion (-128 to 127).
     */
    public ByteArrayToIntArraySerializer() {
        this(byte[].class, false);
    }

    /**
     * Constructs a serializer for byte arrays with configurable signed/unsigned conversion.
     *
     * @param t           the class type (byte[])
     * @param useUnsigned true to treat bytes as unsigned (0 to 255),
     *                    false for signed (-128 to 127)
     */
    protected ByteArrayToIntArraySerializer(Class<byte[]> t, boolean useUnsigned) {
        super(t);
        this.useUnsigned = useUnsigned;
    }

    /**
     * Serializes a byte array to a JSON array of integers.
     * <p>
     * Writes {@code null} to the output if the byte array itself is null.
     *
     * @param value    the byte array to serialize
     * @param gen      the JSON generator
     * @param provider the serializer provider
     * @throws IOException if serialization fails
     */
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // Convert byte[] to int[] for writing
        int[] intArray = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            intArray[i] = useUnsigned ? Byte.toUnsignedInt(value[i]) : value[i];
        }
        gen.writeArray(intArray, 0, intArray.length);
    }
}