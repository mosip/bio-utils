package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class PackageMinutiaeRecord extends BiometricDataRecord implements Serializable {
	/*
	 * <!-- 9.902 ANN -->
     * <!-- 0..* -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ProcessAnnotation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ProcessAnnotation> prosannList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeImpressionCaptureCategoryCode")
    private int minutiaeImpressionCaptCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFormatNISTStandardIndicator")
    private Boolean minutiaeFormatNISTStandardInd;
    
    /*
     *  <!-- 9.901 ULA -->
     *  <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeUniversalLatentWorkstationAnnotationText")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> minutiaeUniLatentWorkstationAnnList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "BiometricCaptureDetail")
    private AudioSourceRecorder bioCapDetail;

    /*
     * <!-- 9.013- 9.125, 9.151 - 9.175, 9.180- 9.225 -->
     * <!-- 0..* -->
     * <!-- This element is abstract and must be substituted with a user-defined element. -->
     * <!-- <biom:RecordMinutiae>-->
     * <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "Minutiae")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Minutiae> minutiaeList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "OtherMinutiae")
    private OtherMinutiae otherMinutiae;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "INCITSMinutiae")
    private INCITSMinutiae incitsMinutiae;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ExtendedFeatureSetMinutiae")
    private ExtendedFeatureSetMinutiae extendedFeatureSetMinutiae;
}