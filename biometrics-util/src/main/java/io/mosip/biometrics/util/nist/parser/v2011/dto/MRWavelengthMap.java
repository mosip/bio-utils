package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * MR = MinutiaeRidge
 */
public class MRWavelengthMap implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeWavelengthMapSamplingFrequencyValue")
    private int mrWavelengthMapSamplingFrequency;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeWavelengthMapFormatCode")
    private String mrWavelengthMapFormatCode;
    
    /*
    <!-- 9.310 RFM -->
    <!--1..*-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeWavelengthMapRowText")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> mrWavelengthMapRowTextList;
}