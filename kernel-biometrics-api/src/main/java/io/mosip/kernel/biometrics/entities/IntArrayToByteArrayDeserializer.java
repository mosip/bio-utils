package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Jackson deserializer that converts a JSON array of integers into a byte array.
 * Supports signed (-128 to 127) or unsigned (0 to 255) byte conversion based on configuration.
 *
 * <p>Usage Example:
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     SimpleModule module = new SimpleModule();
 *     module.addDeserializer(byte[].class, new IntArrayToByteArrayDeserializer());
 *     mapper.registerModule(module);
 * </pre>
 * </p>
 *
 * @author [Your Name]
 */
public class IntArrayToByteArrayDeserializer extends StdDeserializer<byte[]> {

	private final boolean useUnsigned;

	/**
	 * Constructs a deserializer for byte arrays with default signed integer conversion.
	 */
	public IntArrayToByteArrayDeserializer() {
		this(byte[].class, false);
	}

	/**
	 * Constructs a deserializer for byte arrays with configurable signed/unsigned conversion.
	 *
	 * @param t           the class type (byte[])
	 * @param useUnsigned true to interpret integers as unsigned bytes (0 to 255),
	 *                    false for signed bytes (-128 to 127)
	 */
	protected IntArrayToByteArrayDeserializer(Class<byte[]> t, boolean useUnsigned) {
		super(t);
		this.useUnsigned = useUnsigned;
	}

	/**
	 * Deserializes a JSON array of integers into a byte array.
	 *
	 * @param parser  the JSON parser used to read the input JSON.
	 * @param context the deserialization context provided by Jackson.
	 * @return the deserialized byte array.
	 * @throws IOException if the JSON is invalid, not an array, or contains out-of-range values.
	 */
	@Override
	public byte[] deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		JsonToken token = parser.getCurrentToken();

		if (token == JsonToken.VALUE_STRING) {
			// Handle Base64-encoded string
			return parser.getBinaryValue();
		}

		// Ensure the current token is the start of an array
		if (token == JsonToken.START_ARRAY) {

			List<Byte> byteList = new ArrayList<>();
			int index = 0;

			// Iterate through each element in the JSON array
			while (parser.nextToken() != JsonToken.END_ARRAY) {
				if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
					throw new IOException("deserialize::Expected integer value in JSON array at index " + index + ", found: " + parser.getCurrentToken());
				}

				int value = parser.getIntValue();

				// Validate the integer based on signed/unsigned configuration
				if (useUnsigned) {
					if (value < 0 || value > 255) {
						throw new IOException("deserialize::Unsigned byte value out of range (0 to 255): " + value);
					}
				} else {
					if (value < -128 || value > 127) {
						throw new IOException("deserialize::Signed byte value out of range (-128 to 127): " + value);
					}
				}

				// Convert integer to byte and add to list
				byteList.add((byte) value);
				index++;
			}

			// Convert list to byte array
			byte[] result = new byte[byteList.size()];
			for (int i = 0; i < byteList.size(); i++) {
				result[i] = byteList.get(i);
			}

			return result;
		}
		throw new IOException("deserialize::Unsupported token for byte[]: " + token);
	}
}