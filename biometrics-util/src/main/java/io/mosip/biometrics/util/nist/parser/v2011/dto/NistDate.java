package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import io.mosip.biometrics.util.nist.parser.v2011.helper.LocalDateDeserializer;
import io.mosip.biometrics.util.nist.parser.v2011.helper.LocalDateSerializer;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class NistDate implements Serializable {
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "Date")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
    private LocalDate date;
}