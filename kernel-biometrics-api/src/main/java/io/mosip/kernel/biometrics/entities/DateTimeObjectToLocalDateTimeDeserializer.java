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
        // Handle null
        if (currentToken == JsonToken.VALUE_NULL) {
            LOGGER.debug("Deserialization", "Null Value", "creationDate is null");
            return null;
        }

        // Handle string format: Try ISO_DATE_TIME first, then ISO_LOCAL_DATE_TIME
        if (currentToken == JsonToken.VALUE_STRING) {
            String dateTimeString = parser.getText();
            try {
                // Try ISO_DATE_TIME (handles with/without timezone, e.g., "2025-07-30T05:52:00.000Z", "2025-07-29T11:30:40.628")
                LocalDateTime result = LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
                LOGGER.debug("Deserialization", "ISO_DATE_TIME Success", "Deserialized LocalDateTime: " + result);
                return result;
            } catch (DateTimeException e) {
                try {
                    // Fallback to ISO_LOCAL_DATE_TIME (strictly no timezone, e.g., "2025-07-29T11:30:40.628")
                    LocalDateTime result = LocalDateTime.parse(dateTimeString, ISO_LOCAL_DATE_TIME_FORMATTER);
                    LOGGER.debug("Deserialization", "ISO_LOCAL_DATE_TIME Success", "Deserialized LocalDateTime: " + result);
                    return result;
                } catch (DateTimeException e2) {
                    String errorMsg = "Invalid date/time string: " + dateTimeString + ", tried ISO_DATE_TIME, ISO_LOCAL_DATE_TIME, and custom formats, error: " + e2.getMessage();
                    LOGGER.error("Deserialization", "Invalid String", errorMsg, e2);
                    throw new IOException(errorMsg, e2);
                }
            }
        }
        // Handle numeric timestamp (milliseconds since epoch)
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            long millis = parser.getLongValue();
            try {
                LocalDateTime result = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"));
                LOGGER.debug("Deserialization", "Timestamp Success", "Deserialized LocalDateTime: " + result);
                return result;
            } catch (DateTimeException e) {
                String errorMsg = "Invalid timestamp: " + millis + ", error: " + e.getMessage();
                LOGGER.error("Deserialization", "Invalid Timestamp", errorMsg, e);
                throw new IOException(errorMsg, e);
            }
        }

        // Handle array format: [year, month, day, hour, minute, second, nano]
        if (currentToken == JsonToken.START_ARRAY) {
            int[] values = new int[7];  // Allocate for max 7 elements
            int index = 0;
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                    String errorMsg = "Expected integer in array at index " + index + ", found: " + parser.getCurrentToken();
                    LOGGER.error("Deserialization", "Invalid Value", errorMsg);
                    throw new IOException(errorMsg);
                }
                int value = parser.getIntValue();
                if (index < values.length) {
                    values[index] = value;
                } else {
                    String errorMsg = "Too many values in array, expected 7, found more at index: " + index;
                    LOGGER.error("Deserialization", "Invalid Array Length", errorMsg);
                    throw new IOException(errorMsg);
                }
                index++;
            }
            // Accept 6 or 7 elements
            if (index != 6 && index != 7) {
                String errorMsg = "Expected 7 values in array for LocalDateTime, found: " + index;
                LOGGER.error("Deserialization", "Invalid Array Length", errorMsg);
                throw new IOException(errorMsg);
            }
            // Extract values
            int year = values[0];
            int month = values[1];
            int day = values[2];
            int hour = values[3];
            int minute = values[4];
            int second = values[5];
            int nano = (index == 7) ? values[6] : 0; // Default nano to 0 if only 6 elements
            // Validate values
            if (year < 1 || year > 9999) {
                String errorMsg = "Year out of range (1-9999): " + year;
                LOGGER.error("Deserialization", "Invalid Year", errorMsg);
                throw new IOException(errorMsg);
            }
            if (month < 1 || month > 12) {
                String errorMsg = "Month out of range (1-12): " + month;
                LOGGER.error("Deserialization", "Invalid Month", errorMsg);
                throw new IOException(errorMsg);
            }
            if (day < 1 || day > 31) {
                String errorMsg = "Day out of range (1-31): " + day;
                LOGGER.error("Deserialization", "Invalid Day", errorMsg);
                throw new IOException(errorMsg);
            }
            if (hour < 0 || hour > 23) {
                String errorMsg = "Hour out of range (0-23): " + hour;
                LOGGER.error("Deserialization", "Invalid Hour", errorMsg);
                throw new IOException(errorMsg);
            }
            if (minute < 0 || minute > 59) {
                String errorMsg = "Minute out of range (0-59): " + minute;
                LOGGER.error("Deserialization", "Invalid Minute", errorMsg);
                throw new IOException(errorMsg);
            }
            if (second < 0 || second > 59) {
                String errorMsg = "Second out of range (0-59): " + second;
                LOGGER.error("Deserialization", "Invalid Second", errorMsg);
                throw new IOException(errorMsg);
            }
            if (nano < 0 || nano > 999_999_999) {
                String errorMsg = "Nano out of range (0-999999999): " + nano;
                LOGGER.error("Deserialization", "Invalid Nano", errorMsg);
                throw new IOException(errorMsg);
            }
            try {
                LocalDateTime result = LocalDateTime.of(year, month, day, hour, minute, second, nano);
                LOGGER.debug("Deserialization", "Array Success", "Deserialized LocalDateTime: " + result);
                return result;
            } catch (DateTimeException e) {
                String errorMsg = "Invalid date/time values from array: " + e.getMessage();
                LOGGER.error("Deserialization", "Invalid DateTime", errorMsg, e);
                throw new IOException(errorMsg, e);
            }
        }

        if (currentToken != JsonToken.START_OBJECT) {
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