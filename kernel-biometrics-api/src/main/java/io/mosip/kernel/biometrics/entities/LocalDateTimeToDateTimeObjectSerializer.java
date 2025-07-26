package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A Jackson serializer that converts a LocalDateTime into a JSON object with nested date and time fields.
 * Output JSON format: {"date": {"year": int, "month": int, "day": int}, "time": {"hour": int, "minute": int, "second": int, "nano": int}}.
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
     * Serializes a LocalDateTime into a JSON object with date and time fields.
     *
     * @param value    the LocalDateTime to serialize
     * @param gen      the JSON generator
     * @param provider the serializer provider
     * @throws IOException if serialization fails
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
       if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeFieldName("date");
        gen.writeStartObject();
        gen.writeNumberField("year", value.getYear());
        gen.writeNumberField("month", value.getMonthValue());
        gen.writeNumberField("day", value.getDayOfMonth());
        gen.writeEndObject();

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