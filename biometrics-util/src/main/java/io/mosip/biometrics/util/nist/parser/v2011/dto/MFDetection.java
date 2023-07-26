package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * MF = MinutiaeFeature
 */
public class MFDetection implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFeatureDetectionFieldListText")
    private String mfDetFieldList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFeatureDetectionMethodCode")
    private String mfDetMethod;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeAlgorithmVendorIdentification")
    private Identification minutiaeAlgVendorId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeAlgorithmIdentification")
    private Identification minutiaeAlgId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeExaminer")
    private MinutiaeExaminer minutiaeExaminer;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeExaminerAffiliation")
    private MINEXAffiliation meAffiliation;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFeatureDetectionDateTime")
    private AssuranceDateTime mfDetDateTime;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeCommentText")
    private String minutiaeCom;
}