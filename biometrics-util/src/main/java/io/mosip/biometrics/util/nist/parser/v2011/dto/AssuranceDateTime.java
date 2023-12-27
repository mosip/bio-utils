package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import io.mosip.biometrics.util.nist.parser.v2011.helper.LocalDateTimeDeserializer;
import io.mosip.biometrics.util.nist.parser.v2011.helper.LocalDateTimeSerializer;
import lombok.Data;

@Data
/**
 * ASNCE = Assurance
 */
public class AssuranceDateTime implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "DateTime")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class)  
    private LocalDateTime dateTime;
}