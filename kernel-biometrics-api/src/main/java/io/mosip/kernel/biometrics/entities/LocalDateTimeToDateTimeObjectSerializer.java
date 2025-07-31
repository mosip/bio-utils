package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A Jackson serializer that converts a {@link LocalDateTime} into a JSON object
 * with nested date and time fields.
 * <p>
 * Output JSON format:
 * {"date": {"year": int, "month": int, "day": int},
 *  "time": {"hour": int, "minute": int, "second": int, "nano": int}}
 * </p>
 *
 * <p>Usage Example:
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     SimpleModule module = new SimpleModule();
 *     module.addSerializer(LocalDateTime.class, new LocalDateTimeToDateTimeObjectSerializer());
 *     mapper.registerModule(module);
 * </pre>
 * </p>
 *
 * @author [Your Name]
 */
public class LocalDateTimeToDateTimeObjectSerializer extends StdSerializer<LocalDateTime> {

	/**
	 * Constructs a serializer for LocalDateTime.
	 */
	public LocalDateTimeToDateTimeObjectSerializer() {
		this(null);
	}

	/**
	 * Constructs a serializer for LocalDateTime with the specified class type.
	 *
	 * @param t the class type (LocalDateTime)
	 */
	protected LocalDateTimeToDateTimeObjectSerializer(Class<LocalDateTime> t) {
		super(t);
	}

	/**
	 * Serializes a {@link LocalDateTime} into a JSON object with date and time fields.
	 *
	 * @param value    the LocalDateTime to serialize. If null, a null value is written.
	 * @param gen      the JSON generator used to write JSON content.
	 * @param provider the serializer provider.
	 * @throws IOException if serialization fails or writing to JSON generator encounters an error.
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