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

public class DateTimeObjectToLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeObjectToLocalDateTimeDeserializer.class);
    public DateTimeObjectToLocalDateTimeDeserializer() {
        this(null);
    }

    protected DateTimeObjectToLocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
            String errorMsg = "Expected JSON object for LocalDateTime deserialization, found: " + parser.getCurrentToken();
            LOGGER.error("Deserialization", "Invalid Token", errorMsg);
            throw new IOException(errorMsg);
        }

        int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0, nano = 0;
        boolean dateFound = false, timeFound = false;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (parser.getCurrentToken() != JsonToken.FIELD_NAME) {
                String errorMsg = "Expected field name, found: " + parser.getCurrentToken();
                LOGGER.error("Deserialization", "Invalid Token", errorMsg);
                throw new IOException(errorMsg);
            }

            String fieldName = parser.getCurrentName();
            parser.nextToken();

            if ("date".equalsIgnoreCase(fieldName)) {
                if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
                    String errorMsg = "Expected object for 'date' field, found: " + parser.getCurrentToken();
                    LOGGER.error("Deserialization", "Invalid Field", errorMsg);
                    throw new IOException(errorMsg);
                }

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String dateField = parser.getCurrentName();
                    parser.nextToken();
                    if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                        String errorMsg = "Expected integer for 'date." + dateField + "', found: " + parser.getCurrentToken();
                        LOGGER.error("Deserialization", "Invalid Value", errorMsg);
                        throw new IOException(errorMsg);
                    }

                    int value = parser.getIntValue();
                    switch (dateField) {
                        case "year":
                            if (value < 1 || value > 9999) {
                                String errorMsg = "Year out of range (1-9999): " + value;
                                LOGGER.error("Deserialization", "Invalid Year", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            year = value;
                            break;
                        case "month":
                            if (value < 1 || value > 12) {
                                String errorMsg = "Month out of range (1-12): " + value;
                                LOGGER.error("Deserialization", "Invalid Month", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            month = value;
                            break;
                        case "day":
                            if (value < 1 || value > 31) {
                                String errorMsg = "Day out of range (1-31): " + value;
                                LOGGER.error("Deserialization", "Invalid Day", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            day = value;
                            break;
                        default:
                            String errorMsg = "Unexpected field in 'date' object: " + dateField;
                            LOGGER.error("Deserialization", "Unexpected Field", errorMsg);
                            throw new IOException(errorMsg);
                    }
                }
                dateFound = true;
            } else if ("time".equalsIgnoreCase(fieldName)) {
                if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
                    String errorMsg = "Expected object for 'time' field, found: " + parser.getCurrentToken();
                    LOGGER.error("Deserialization", "Invalid Field", errorMsg);
                    throw new IOException(errorMsg);
                }

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String timeField = parser.getCurrentName();
                    parser.nextToken();
                    if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                        String errorMsg = "Expected integer for 'time." + timeField + "', found: " + parser.getCurrentToken();
                        LOGGER.error("Deserialization", "Invalid Value", errorMsg);
                        throw new IOException(errorMsg);
                    }

                    int value = parser.getIntValue();
                    switch (timeField) {
                        case "hour":
                            if (value < 0 || value > 23) {
                                String errorMsg = "Hour out of range (0-23): " + value;
                                LOGGER.error("Deserialization", "Invalid Hour", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            hour = value;
                            break;
                        case "minute":
                            if (value < 0 || value > 59) {
                                String errorMsg = "Minute out of range (0-59): " + value;
                                LOGGER.error("Deserialization", "Invalid Minute", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            minute = value;
                            break;
                        case "second":
                            if (value < 0 || value > 59) {
                                String errorMsg = "Second out of range (0-59): " + value;
                                LOGGER.error("Deserialization", "Invalid Second", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            second = value;
                            break;
                        case "nano":
                            if (value < 0 || value > 999_999_999) {
                                String errorMsg = "Nano out of range (0-999999999): " + value;
                                LOGGER.error("Deserialization", "Invalid Nano", errorMsg);
                                throw new IOException(errorMsg);
                            }
                            nano = value;
                            break;
                        default:
                            String errorMsg = "Unexpected field in 'time' object: " + timeField;
                            LOGGER.error("Deserialization", "Unexpected Field", errorMsg);
                            throw new IOException(errorMsg);
                    }
                }
                timeFound = true;
            } else {
                String errorMsg = "Unexpected field in JSON object: " + fieldName;
                LOGGER.error("Deserialization", "Unexpected Field", errorMsg);
                throw new IOException(errorMsg);
            }
        }

        if (!dateFound || !timeFound) {
            String errorMsg = "Missing required field(s): " + (dateFound ? "" : "'date' ") + (timeFound ? "" : "'time'");
            LOGGER.error("Deserialization", "Unexpected Field", errorMsg);
            throw new IOException(errorMsg);
        }

        try {
            LocalDateTime result = LocalDateTime.of(year, month, day, hour, minute, second, nano);
            LOGGER.debug("Deserialization", "Success", "Deserialized LocalDateTime: " + result);
            return result;
        } catch (DateTimeException e) {
            String errorMsg = "Invalid date/time values: " + e.getMessage();
            LOGGER.error("Deserialization", "Invalid DateTime", errorMsg, e);
            throw new IOException(errorMsg, e);
        }
    }
}