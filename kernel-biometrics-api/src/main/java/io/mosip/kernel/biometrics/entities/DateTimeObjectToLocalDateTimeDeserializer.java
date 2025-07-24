package io.mosip.biosdk.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A Jackson deserializer that converts a JSON object with nested date and time fields into a LocalDateTime.
 * Expected JSON format: {"date": {"year": int, "month": int, "day": int}, "time": {"hour": int, "minute": int, "second": int, "nano": int}}.
 *
 */
public class DateTimeObjectToLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    /**
     * Constructs a deserializer for LocalDateTime.
     */
    public DateTimeObjectToLocalDateTimeDeserializer() {
        this(LocalDateTime.class);
    }

    /**
     * Constructs a deserializer for LocalDateTime with the specified class type.
     *
     * @param t the class type (LocalDateTime)
     */
    protected DateTimeObjectToLocalDateTimeDeserializer(Class<LocalDateTime> t) {
        super(t);
    }

    /**
     * Deserializes a JSON object into a LocalDateTime.
     *
     * @param parser  the JSON parser
     * @param context the deserialization context
     * @return the deserialized LocalDateTime
     * @throws IOException if the JSON is invalid, missing required fields, or contains out-of-range values
     */
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
            String errorMsg = "Expected JSON object for LocalDateTime deserialization, found: " + parser.getCurrentToken();
            throw new IOException(errorMsg);
        }

        int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0, nano = 0;
        boolean dateFound = false, timeFound = false;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (parser.getCurrentToken() != JsonToken.FIELD_NAME) {
                String errorMsg = "Expected field name, found: " + parser.getCurrentToken();
                throw new IOException(errorMsg);
            }

            String fieldName = parser.getCurrentName();
            parser.nextToken();

            if ("date".equals(fieldName)) {
                if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
                    String errorMsg = "Expected object for 'date' field, found: " + parser.getCurrentToken();
                    throw new IOException(errorMsg);
                }

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String dateField = parser.getCurrentName();
                    parser.nextToken();
                    if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                        String errorMsg = "Expected integer for 'date." + dateField + "', found: " + parser.getCurrentToken();
                        throw new IOException(errorMsg);
                    }

                    int value = parser.getIntValue();
                    switch (dateField) {
                        case "year":
                            if (value < 1 || value > 9999) {
                                String errorMsg = "Year out of range (1-9999): " + value;
                                throw new IOException(errorMsg);
                            }
                            year = value;
                            break;
                        case "month":
                            if (value < 1 || value > 12) {
                                String errorMsg = "Month out of range (1-12): " + value;
                                throw new IOException(errorMsg);
                            }
                            month = value;
                            break;
                        case "day":
                            if (value < 1 || value > 31) {
                                String errorMsg = "Day out of range (1-31): " + value;
                                throw new IOException(errorMsg);
                            }
                            day = value;
                            break;
                        default:
                            String errorMsg = "Unexpected field in 'date' object: " + dateField;
                            throw new IOException(errorMsg);
                    }
                }
                dateFound = true;
            } else if ("time".equals(fieldName)) {
                if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
                    String errorMsg = "Expected object for 'time' field, found: " + parser.getCurrentToken();
                    throw new IOException(errorMsg);
                }

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String timeField = parser.getCurrentName();
                    parser.nextToken();
                    if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                        String errorMsg = "Expected integer for 'time." + timeField + "', found: " + parser.getCurrentToken();
                        throw new IOException(errorMsg);
                    }

                    int value = parser.getIntValue();
                    switch (timeField) {
                        case "hour":
                            if (value < 0 || value > 23) {
                                String errorMsg = "Hour out of range (0-23): " + value;
                                throw new IOException(errorMsg);
                            }
                            hour = value;
                            break;
                        case "minute":
                            if (value < 0 || value > 59) {
                                String errorMsg = "Minute out of range (0-59): " + value;
                                throw new IOException(errorMsg);
                            }
                            minute = value;
                            break;
                        case "second":
                            if (value < 0 || value > 59) {
                                String errorMsg = "Second out of range (0-59): " + value;
                                throw new IOException(errorMsg);
                            }
                            second = value;
                            break;
                        case "nano":
                            if (value < 0 || value > 999_999_999) {
                                String errorMsg = "Nano out of range (0-999999999): " + value;
                                throw new IOException(errorMsg);
                            }
                            nano = value;
                            break;
                        default:
                            String errorMsg = "Unexpected field in 'time' object: " + timeField;
                            throw new IOException(errorMsg);
                    }
                }
                timeFound = true;
            } else {
                String errorMsg = "Unexpected field in JSON object: " + fieldName;
                throw new IOException(errorMsg);
            }
        }

        if (!dateFound || !timeFound) {
            String errorMsg = "Missing required field(s): " + (dateFound ? "" : "'date' ") + (timeFound ? "" : "'time'");
            throw new IOException(errorMsg);
        }

        try {
            LocalDateTime result = LocalDateTime.of(year, month, day, hour, minute, second, nano);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
}