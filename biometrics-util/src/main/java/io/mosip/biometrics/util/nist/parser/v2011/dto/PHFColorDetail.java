package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * PHF = PhysicalFeature
 */
public class PHFColorDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeaturePrimaryColorCode")
    private String phfPrimaryColorCode;
    
    /*
     * <!-- TC2, TC3, TC4, TC5, TC6 -->
     * <!--0..5-->
     * <!--Repeat this element as necessary to represent TC2, TC3, TC4, TC5 and TC6.-->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeatureSecondaryColorCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> phfSecondaryColorCodeList;
}