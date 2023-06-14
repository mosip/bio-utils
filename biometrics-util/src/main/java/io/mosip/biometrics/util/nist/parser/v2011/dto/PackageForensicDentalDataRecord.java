package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class PackageForensicDentalDataRecord extends BiometricDataRecord implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "Type2CrossReferenceIdentification")
    private Identification t2crId;
   
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "Type10CrossReferenceIdentification")
    private Identification t10crId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "Type22CrossReferenceIdentification")
    private Identification t22crId;
    
    /*
     *  <!-- 12.902	ANN -->
     *  <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ProcessAnnotation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ProcessAnnotation> prosannList;
    
    /*
     * <!-- 12.995	ASC -->
     * <!-- 0..255 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssociatedContext")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<AssociatedContext> asscxtList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHashValue")
    private String imgHash;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicDentalOralDetail")
    private FORDentalOralDetail forDentalOralDetail;
}