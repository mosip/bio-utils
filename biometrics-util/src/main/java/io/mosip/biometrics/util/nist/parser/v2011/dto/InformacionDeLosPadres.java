package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class InformacionDeLosPadres implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "CURPPadre")
    private String curpPadre;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "CURPMadre")
    private String curpMadre;
}