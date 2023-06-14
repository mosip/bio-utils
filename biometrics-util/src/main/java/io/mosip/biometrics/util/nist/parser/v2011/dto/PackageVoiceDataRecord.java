package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class PackageVoiceDataRecord extends BiometricDataRecord implements Serializable {
	/*
	 * <!-- 11.902	ANN -->
     * <!-- 0..* -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ProcessAnnotation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ProcessAnnotation> prosannList;
    
    /*
     * <!-- 11.995	ASC -->
     * <!-- 0..255 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssociatedContext")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<AssociatedContext> asscxtList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DigitalAudioRecordHashValue")
    private String dataHash;
    
    /*
     * <!--10.997 SOR -->
     * <!-- 0..255 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SourceRepresentation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SourceRepresentation> srcRepList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "ForensicInvestigatoryVoiceDetail")
    private FORINVVoiceDetail forinvVoiceDetail;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SourceExternalFileReferenceText")
    private String srcExternalFileRef;
}