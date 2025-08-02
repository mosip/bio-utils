package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom Jackson deserializer that supports multiple JSON representations
 * of date-time values and converts them into a {@link LocalDateTime}.
 *
 * <p>Supported input formats:
 * <ul>
 *   <li>ISO date-time strings with or without timezone (e.g., "2025-07-30T05:52:00.000Z")</li>
 *   <li>Epoch timestamps in milliseconds (numeric)</li>
 *   <li>Array format: {@code [year, month, day, hour, minute, second, nano]}</li>
 *   <li>Object format:
 *     <pre>{@code
 *     {
 *        "date": {"year":2025,"month":7,"day":30},
 *        "time": {"hour":5,"minute":52,"second":0,"nano":123000000}
 *     }
 *     }</pre>
 *   </li>
 * </ul>
 *
 * <p>This deserializer provides robust error handling and detailed logging
 * for invalid or unexpected input values.
 *
 * <p>Usage:
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * SimpleModule module = new SimpleModule();
 * module.addDeserializer(LocalDateTime.class, new DateTimeObjectToLocalDateTimeDeserializer());
 * mapper.registerModule(module);
 * }</pre>
 *
 * @author
 * @since 1.0
 */
public class DateTimeObjectToLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeObjectToLocalDateTimeDeserializer.class);
	private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
	private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	/**
	 * Default constructor for Jackson deserializer.
	 */
	public DateTimeObjectToLocalDateTimeDeserializer() {
		this(null);
	}

	/**
	 * Parameterized constructor for Jackson.
	 * @param vc Class type handled by this deserializer
	 */
	protected DateTimeObjectToLocalDateTimeDeserializer(Class<?> vc) {
		super(vc);
	}

	/**
	 * Deserialize multiple date-time representations into {@link LocalDateTime}.
	 *
	 * @param parser  the JSON parser
	 * @param context deserialization context
	 * @return a {@link LocalDateTime} parsed from input
	 * @throws IOException if parsing fails or input is invalid
	 */
	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		JsonToken currentToken = parser.getCurrentToken();

		// Handle null values
		if (currentToken == JsonToken.VALUE_NULL) {
			LOGGER.debug("Deserialization: Null value encountered for LocalDateTime field.");
			return null;
		}

		// Handle ISO date-time strings
		if (currentToken == JsonToken.VALUE_STRING) {
			String dateTimeString = parser.getText();
			try {
				LocalDateTime result = LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
				LOGGER.debug("Parsed ISO_DATE_TIME format: {}", result);
				return result;
			} catch (DateTimeException e1) {
				try {
					LocalDateTime result = LocalDateTime.parse(dateTimeString, ISO_LOCAL_DATE_TIME_FORMATTER);
					LOGGER.debug("Parsed ISO_LOCAL_DATE_TIME format: {}", result);
					return result;
				} catch (DateTimeException e2) {
					LOGGER.error("Failed parsing string input: {}", dateTimeString, e2);
					throw new IOException("Invalid date/time string: " + dateTimeString, e2);
				}
			}
		}

		// Handle numeric timestamp (milliseconds)
		if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
			long millis = parser.getLongValue();
			try {
				LocalDateTime result = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"));
				LOGGER.debug("Parsed timestamp in milliseconds: {}", result);
				return result;
			} catch (DateTimeException e) {
				LOGGER.error("Failed parsing numeric timestamp: {}", millis, e);
				throw new IOException("Invalid timestamp: " + millis, e);
			}
		}

		// Handle array format [year, month, day, hour, minute, second, nano]
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

		// Handle object format {"date":{...}, "time":{...}}
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
						parser.nextToken();
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
						parser.nextToken();
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