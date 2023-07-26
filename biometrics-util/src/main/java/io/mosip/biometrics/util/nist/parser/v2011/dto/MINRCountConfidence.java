package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * MINR = MinutiaeRidge
 */
public class MINRCountConfidence implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaLocationPoint")
    private MinutiaLocationPoint minutiaLocPoint;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaLocationReferencePoint")
    private MinutiaLocationPoint minutiaLocRefPoint;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeCountMethodCode")
    private String mrCountMethodCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeCountConfidenceValue")
    private int mrCountConfidence;
}