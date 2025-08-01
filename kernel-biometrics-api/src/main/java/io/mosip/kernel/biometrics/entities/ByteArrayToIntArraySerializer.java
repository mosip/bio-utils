package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * A Jackson serializer that converts a byte array to a JSON array of integers.
 * <p>
 * This serializer iterates through each byte in the array and writes it as an integer
 * value in JSON output. By default, bytes are treated as signed (-128 to 127), but it can be
 * configured to treat them as unsigned (0 to 255).
 * </p>
 *
 * <p>
 * Usage Example:
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * SimpleModule module = new SimpleModule();
 * module.addSerializer(byte[].class, new ByteArrayToIntArraySerializer());
 * mapper.registerModule(module);
 * String json = mapper.writeValueAsString(byteArray);
 * }</pre>
 * </p>
 *
 * @author Auto-generated
 * @version 1.0
 * @since 1.0
 */
public class ByteArrayToIntArraySerializer extends StdSerializer<byte[]> {

	/**
	 * Flag indicating whether to serialize bytes as unsigned integers (0-255) or signed (-128 to 127).
	 */
	private final boolean useUnsigned;

	/**
	 * Constructs a serializer for byte arrays with default signed integer conversion.
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
	 * @param gen      the JSON generator used to write JSON content
	 * @param provider the serializer provider
	 * @throws IOException if the input array is null or if serialization fails
	 */
	@Override
	public void serialize(byte[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null) {
			gen.writeNull();
			return;
		}

		gen.writeStartArray();
		for (byte b : value) {
			int intValue = useUnsigned ? Byte.toUnsignedInt(b) : b;
			gen.writeNumber(intValue);
		}
		gen.writeEndArray();
	}
}