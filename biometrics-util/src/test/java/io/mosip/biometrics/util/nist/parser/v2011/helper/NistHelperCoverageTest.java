package io.mosip.biometrics.util.nist.parser.v2011.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Coverage for NIST ITL helper serializers / {@link NamespaceXmlFactory}.
 */
class NistHelperCoverageTest {

    @Test
    void localDateSerializeAndDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        mapper.registerModule(module);

        LocalDate date = LocalDate.of(2024, 6, 15);
        String json = mapper.writeValueAsString(date);
        assertEquals("\"2024-06-15\"", json);
        assertEquals(date, mapper.readValue(json, LocalDate.class));
    }

    @Test
    void localDateTimeSerializeAndDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        mapper.registerModule(module);

        LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        String json = mapper.writeValueAsString(dateTime);
        assertNotNull(json);
        assertEquals(dateTime, mapper.readValue(json, LocalDateTime.class));
    }

    @Test
    void namespaceXmlFactoryCreatesWriterWithNamespaces() throws Exception {
        NamespaceXmlFactory factory = new NamespaceXmlFactory(
                "http://example.com/default",
                Map.of("itl", "http://example.com/itl"));
        XmlMapper xmlMapper = new XmlMapper(factory);
        String xml = xmlMapper.writeValueAsString(Map.of("value", "x"));
        assertNotNull(xml);
        assertThrows(NullPointerException.class, () -> new NamespaceXmlFactory(null, Map.of()));
        assertThrows(NullPointerException.class, () -> new NamespaceXmlFactory("ns", null));
    }

    @Test
    void serializersWriteViaGenerator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        try (JsonGenerator gen = mapper.getFactory().createGenerator(sw)) {
            new LocalDateSerializer().serialize(LocalDate.of(2020, 1, 2), gen, mapper.getSerializerProvider());
            gen.flush();
        }
        assertEquals("\"2020-01-02\"", sw.toString());

        StringWriter sw2 = new StringWriter();
        try (JsonGenerator gen = mapper.getFactory().createGenerator(sw2)) {
            new LocalDateTimeSerializer().serialize(LocalDateTime.of(2020, 1, 2, 3, 4, 5), gen,
                    mapper.getSerializerProvider());
            gen.flush();
        }
        assertNotNull(sw2.toString());
    }

    @Test
    void deserializersReadViaParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (JsonParser parser = mapper.getFactory().createParser("\"2021-03-04\"")) {
            parser.nextToken();
            assertEquals(LocalDate.of(2021, 3, 4),
                    new LocalDateDeserializer().deserialize(parser, mapper.getDeserializationContext()));
        }
        try (JsonParser parser = mapper.getFactory().createParser("\"2021-03-04T05:06:07\"")) {
            parser.nextToken();
            assertEquals(LocalDateTime.of(2021, 3, 4, 5, 6, 7),
                    new LocalDateTimeDeserializer().deserialize(parser, mapper.getDeserializationContext()));
        }
    }
}
