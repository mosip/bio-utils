package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneId;

public class DateTimeObjectToLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeObjectToLocalDateTimeDeserializer.class);
    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public DateTimeObjectToLocalDateTimeDeserializer() {
        this(null);
    }

    protected DateTimeObjectToLocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        // Handle null values
        if (currentToken == JsonToken.VALUE_NULL) {
            LOGGER.debug("Deserialization: Null Value encountered for LocalDateTime field.");
            return null;
        }

        // Handle string input with multiple format attempts
        if (currentToken == JsonToken.VALUE_STRING) {
            String dateTimeString = parser.getText();
            try {
                LocalDateTime result = LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
                LOGGER.debug("Deserialization: Successfully parsed ISO_DATE_TIME format: {}", result);
                return result;
            } catch (DateTimeException e) {
                try {
                    LocalDateTime result = LocalDateTime.parse(dateTimeString, ISO_LOCAL_DATE_TIME_FORMATTER);
                    LOGGER.debug("Deserialization: Successfully parsed ISO_LOCAL_DATE_TIME format: {}", result);
                    return result;
                } catch (DateTimeException e2) {
                    String errorMsg = "Invalid date/time string: " + dateTimeString;
                    LOGGER.error("Deserialization: Failed parsing string input.", e2);
                    throw new IOException(errorMsg, e2);
                }
            }
        }
        // Handle numeric timestamp (milliseconds since epoch)
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            long millis = parser.getLongValue();
            try {
                LocalDateTime result = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"));
                LOGGER.debug("Deserialization: Successfully parsed timestamp: {}", result);
                return result;
            } catch (DateTimeException e) {
                String errorMsg = "Invalid timestamp: " + millis;
                LOGGER.error("Deserialization: Failed parsing numeric timestamp.", e);
                throw new IOException(errorMsg, e);
            }
        }

        // Handle array format: [year, month, day, hour, minute, second, nano]
        if (currentToken == JsonToken.START_ARRAY) {
            int[] values = new int[7];
            int index = 0;
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                    throw new IOException("Expected integer in array at index " + index);
                }
                values[index++] = parser.getIntValue();
            }
            if (index < 6 || index > 7) {
                throw new IOException("Expected 6 or 7 values in array for LocalDateTime, found: " + index);
            }
            return LocalDateTime.of(values[0], values[1], values[2], values[3], values[4], values[5], index == 7 ? values[6] : 0);
        }

        if (currentToken == JsonToken.START_OBJECT) {
            int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0, nano = 0;
            boolean dateFound = false, timeFound = false;

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                if (fieldName == null) continue;

                parser.nextToken(); // move to value or START_OBJECT

                if ("date".equalsIgnoreCase(fieldName) && parser.currentToken() == JsonToken.START_OBJECT) {
                    dateFound = true;
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String subField = parser.getCurrentName();
                        if (subField == null) continue;

                        parser.nextToken(); // move to value
                        switch (subField) {
                            case "year":  year = parser.getIntValue(); break;
                            case "month": month = parser.getIntValue(); break;
                            case "day":   day = parser.getIntValue(); break;
                        }
                    }
                } else if ("time".equalsIgnoreCase(fieldName) && parser.currentToken() == JsonToken.START_OBJECT) {
                    timeFound = true;
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String subField = parser.getCurrentName();
                        if (subField == null) continue;

                        parser.nextToken(); // move to value
                        switch (subField) {
                            case "hour":   hour = parser.getIntValue(); break;
                            case "minute": minute = parser.getIntValue(); break;
                            case "second": second = parser.getIntValue(); break;
                            case "nano":   nano = parser.getIntValue(); break;
                        }
                    }
                } else {
                    parser.skipChildren(); // skip any unknown objects
                }
            }

            if (!dateFound || !timeFound) {
                throw new IOException("Missing required 'date' or 'time' fields for LocalDateTime deserialization");
            }

            return LocalDateTime.of(year, month, day, hour, minute, second, nano);
        }

        throw new IOException("Unsupported token for LocalDateTime deserialization: " + currentToken);
    }
}