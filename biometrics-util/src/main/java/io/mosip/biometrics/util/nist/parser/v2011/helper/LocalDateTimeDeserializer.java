package io.mosip.biometrics.util.nist.parser.v2011.helper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
	public LocalDateTimeDeserializer(){
        super(LocalDateTime.class);
    }

	@Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        return LocalDateTime.parse(jp.readValueAs(String.class).trim(), DateTimeFormatter.ISO_DATE_TIME);
    }
}