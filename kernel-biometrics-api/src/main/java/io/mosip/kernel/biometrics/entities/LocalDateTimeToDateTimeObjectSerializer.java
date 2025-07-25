package io.mosip.biosdk.client.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A Jackson serializer that converts a LocalDateTime into a JSON object with nested date and time fields.
 * Output JSON format: {"date": {"year": int, "month": int, "day": int}, "time": {"hour": int, "minute": int, "second": int, "nano": int}}.
 *
 */
public class LocalDateTimeToDateTimeObjectSerializer extends StdSerializer<LocalDateTime> {

    /**
     * Constructs a serializer for LocalDateTime.
     */
    public LocalDateTimeToDateTimeObjectSerializer() {
        this(LocalDateTime.class);
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
     * @throws IOException if the input is null or serialization fails
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        try {
            gen.writeStartObject();

            // Write date object
            gen.writeFieldName("date");
            gen.writeStartObject();
            gen.writeFieldName("year");
            gen.writeNumber(value.getYear());
            gen.writeFieldName("month");
            gen.writeNumber(value.getMonthValue());
            gen.writeFieldName("day");
            gen.writeNumber(value.getDayOfMonth());
            gen.writeEndObject();

            // Write time object
            gen.writeFieldName("time");
            gen.writeStartObject();
            gen.writeFieldName("hour");
            gen.writeNumber(value.getHour());
            gen.writeFieldName("minute");
            gen.writeNumber(value.getMinute());
            gen.writeFieldName("second");
            gen.writeNumber(value.getSecond());
            gen.writeFieldName("nano");
            gen.writeNumber(value.getNano());
            gen.writeEndObject();

            gen.writeEndObject();

        } catch (IOException e) {
            throw e;
        }
    }
}