package io.mosip.kernel.biometrics.entities;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Base64;

/**
 * A custom Gson {@link JsonSerializer} and {@link JsonDeserializer} implementation
 * for handling byte arrays in JSON.
 * <p>
 * This adapter allows Gson to correctly serialize and deserialize {@code byte[]}
 * fields by supporting two formats:
 * <ul>
 *     <li><b>Deserialization</b>:
 *         <ul>
 *             <li>If the JSON value is a Base64-encoded string, it decodes it to a byte array.</li>
 *             <li>If the JSON value is a numeric array, it converts each integer element (0–255)
 *                 into a byte value.</li>
 *         </ul>
 *     </li>
 *     <li><b>Serialization</b>:
 *         <ul>
 *             <li>Encodes the {@code byte[]} as a Base64 string for compact representation.</li>
 *         </ul>
 *     </li>
 * </ul>
 * <p>
 * This adapter ensures compatibility between systems where binary data may be
 * represented either as a Base64 string or as an array of numeric values in JSON.
 * It is particularly useful when integrating with services that expect Base64 strings
 * but also need to tolerate JSON numeric arrays (for example, interoperability
 * with Jackson or manual byte array representations).
 *
 * <p>Usage Example:
 * <pre>{@code
 * Gson gson = new GsonBuilder()
 *     .registerTypeAdapter(byte[].class, new GSONByteArrayAdapter())
 *     .create();
 *
 * byte[] original = {70, 65, 67};
 * String json = gson.toJson(original);   // Produces: "RkFD"
 *
 * byte[] decoded = gson.fromJson(json, byte[].class);
 * }</pre>
 *
 * @author Janardhan B S
 * @since 1.0
 */
public class GSONByteArrayAdapter implements JsonDeserializer<byte[]>, JsonSerializer<byte[]> {

	/**
	 * Deserializes a JSON element into a byte array.
	 * <p>
	 * Supports:
	 * <ul>
	 *     <li>Base64-encoded string input.</li>
	 *     <li>Numeric JSON array input (integers 0–255).</li>
	 * </ul>
	 *
	 * @param json the input JSON element
	 * @param typeOfT the type of the object to deserialize to
	 * @param context the deserialization context
	 * @return a decoded byte array, or {@code null} if the input is JSON null
	 * @throws JsonParseException if the JSON format is invalid or unsupported
	 */
	@Override
	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (json.isJsonNull()) {
			return null;
		}

		// Case 1: JSON is a Base64 string
		if (json.isJsonPrimitive()) {
			String base64 = json.getAsString();
			return Base64.getDecoder().decode(base64);
		}

		// Case 2: JSON is a numeric array
		if (json.isJsonArray()) {
			JsonArray array = json.getAsJsonArray();
			byte[] bytes = new byte[array.size()];
			for (int i = 0; i < array.size(); i++) {
				bytes[i] = (byte) array.get(i).getAsInt();
			}
			return bytes;
		}

		throw new JsonParseException("Invalid format for byte array: " + json.toString());
	}

	/**
	 * Serializes a byte array into a Base64-encoded JSON string.
	 * <p>
	 * If the input byte array is {@code null}, a JSON null value is returned.
	 *
	 * @param src the byte array to serialize
	 * @param typeOfSrc the type of the source object
	 * @param context the serialization context
	 * @return a {@link JsonElement} containing the Base64 string representation of the byte array
	 */
	@Override
	public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return JsonNull.INSTANCE;
		}
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}
}
