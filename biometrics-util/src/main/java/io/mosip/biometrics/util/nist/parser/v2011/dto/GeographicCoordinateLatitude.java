package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GeographicCoordinateLatitude implements Serializable {
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "LatitudeDegreeValue")
    @Min(value = -90)
    @Max(value = 90)
    private int latiDegreeValue;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "LatitudeMinuteValue")
    @Min(value = 0)
    @Max(value = 60)
    private int latiMinute;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "LatitudeSecondValue")
    @Min(value = 0)
    @Max(value = 60)
    private int latiSecond;
}