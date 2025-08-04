package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A custom Jackson serializer that converts a {@link LocalDateTime} instance into a structured JSON object
 * with nested date and time fields.
 *
 * <p>Output JSON format:</p>
 * <pre>{@code
 * {
 *   "date": {
 *     "year":   2025,
 *     "month":  8,
 *     "day":    2
 *   },
 *   "time": {
 *     "hour":   15,
 *     "minute": 45,
 *     "second": 12,
 *     "nano":   123000000
 *   }
 * }
 * }</pre>
 *
 * <p>This serializer is particularly useful in systems where a structured, human-readable date-time representation
 * is preferred over a standard ISO-8601 string or timestamp. It also helps achieve compatibility with legacy services
 * expecting separate date and time objects in JSON payloads.</p>
 *
 * <h3>Usage Example:</h3>
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * SimpleModule module = new SimpleModule();
 * module.addSerializer(LocalDateTime.class, new LocalDateTimeToDateTimeObjectSerializer());
 * mapper.registerModule(module);
 *
 * LocalDateTime now = LocalDateTime.now();
 * String json = mapper.writeValueAsString(now);
 * }</pre>
 *
 * @author
 * @since 1.0
 */
public class LocalDateTimeToDateTimeObjectSerializer extends StdSerializer<LocalDateTime> {

	/**
	 * Default constructor that initializes the serializer for {@link LocalDateTime}.
	 */
	public LocalDateTimeToDateTimeObjectSerializer() {
		this(null);
	}

	/**
	 * Constructs a serializer for {@link LocalDateTime} with a specific class type.
	 *
	 * @param t the class type handled by this serializer
	 */
	protected LocalDateTimeToDateTimeObjectSerializer(Class<LocalDateTime> t) {
		super(t);
	}

	/**
	 * Serializes a {@link LocalDateTime} into a JSON object with nested date and time fields.
	 *
	 * @param value    the {@link LocalDateTime} value to serialize; if {@code null}, a JSON null is written
	 * @param gen      the {@link JsonGenerator} used to write JSON content
	 * @param provider the {@link SerializerProvider} that can be used to get serializers for
	 *                 serializing Objects value contains, if any
	 * @throws IOException if an I/O error occurs while writing to the JSON output
	 */
	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null) {
			gen.writeNull();
			return;
		}

		gen.writeStartObject();

		// Write date fields
		gen.writeFieldName("date");
		gen.writeStartObject();
		gen.writeNumberField("year", value.getYear());
		gen.writeNumberField("month", value.getMonthValue());
		gen.writeNumberField("day", value.getDayOfMonth());
		gen.writeEndObject();

		// Write time fields
		gen.writeFieldName("time");
		gen.writeStartObject();
		gen.writeNumberField("hour", value.getHour());
		gen.writeNumberField("minute", value.getMinute());
		gen.writeNumberField("second", value.getSecond());
		gen.writeNumberField("nano", value.getNano());
		gen.writeEndObject();

		gen.writeEndObject();
	}
}