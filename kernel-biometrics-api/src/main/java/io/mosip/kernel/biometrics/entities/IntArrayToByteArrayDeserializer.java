package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * A custom Jackson {@link StdDeserializer} that converts a JSON array of integers
 * into a byte array ({@code byte[]}).
 *
 * <p>This deserializer is useful when you want to accept binary data in JSON as
 * an array of numeric values instead of the default Base64-encoded string
 * representation typically produced and consumed by Jackson.</p>
 *
 * <p>It supports two modes:
 * <ul>
 *   <li><b>Signed mode</b> (default): Each value is expected to be in the range -128 to 127.</li>
 *   <li><b>Unsigned mode</b>: Each value is expected to be in the range 0 to 255 and will be
 *   converted to a signed byte.</li>
 * </ul>
 *
 * <p>Example input JSON:</p>
 * <pre>{@code
 * {
 *   "data": [70, 65, 67, 0, 48, 51]
 * }
 * }</pre>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * SimpleModule module = new SimpleModule();
 * module.addDeserializer(byte[].class, new IntArrayToByteArrayDeserializer());
 * mapper.registerModule(module);
 *
 * byte[] bytes = mapper.readValue("[70,65,67]", byte[].class);
 * }</pre>
 *
 * @author Janardhan B S
 * @since 1.0
 */
public class IntArrayToByteArrayDeserializer extends StdDeserializer<byte[]> {

	private final boolean useUnsigned;

	/**
	 * Default constructor using signed byte conversion (-128 to 127).
	 */
	public IntArrayToByteArrayDeserializer() {
		this(byte[].class, false);
	}

	/**
	 * Constructs a deserializer with configurable signed/unsigned conversion.
	 *
	 * @param t           the class type handled by this deserializer
	 * @param useUnsigned {@code true} to interpret integers as unsigned (0 to 255),
	 *                    {@code false} for signed (-128 to 127)
	 */
	protected IntArrayToByteArrayDeserializer(Class<byte[]> t, boolean useUnsigned) {
		super(t);
		this.useUnsigned = useUnsigned;
	}

	/**
	 * Deserializes a JSON array of integers into a byte array.
	 *
	 * @param parser  the JSON parser
	 * @param context the deserialization context
	 * @return a byte array corresponding to the numeric JSON input
	 * @throws IOException if the JSON is invalid, not an array, or contains values outside
	 *                     the expected signed/unsigned byte range
	 */
	@Override
	public byte[] deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		JsonToken token = parser.getCurrentToken();
		if (token == JsonToken.VALUE_STRING) {
			// Base64 input
			String base64 = parser.getText();
			if (base64 == null || base64.isEmpty()) {
				return new byte[0]; // return empty array
			}
			return Base64.getDecoder().decode(base64);
		}

		if (token != JsonToken.START_ARRAY) {
			String errorMsg = "Expected JSON array for byte[] deserialization, found: " + parser.getCurrentToken();
			throw new IOException(errorMsg);
		}

		List<Byte> byteList = new ArrayList<>();
		while (parser.nextToken() != JsonToken.END_ARRAY) {
			if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
				throw new IOException("Expected integer value in JSON array, found: " + parser.getCurrentToken());
			}

			int value = parser.getIntValue();
			if (useUnsigned) {
				if (value < 0 || value > 255) {
					throw new IOException("Unsigned byte value out of range (0 to 255): " + value);
				}
			} else {
				if (value < -128 || value > 127) {
					throw new IOException("Signed byte value out of range (-128 to 127): " + value);
				}
			}
			byteList.add((byte) value);
		}

		byte[] result = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++) {
			result[i] = byteList.get(i);
		}
		return result;
	}
}