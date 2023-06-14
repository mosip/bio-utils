package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * FOR = Forensic
 * DENT = Dental
 */
public class FORDentalOralDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DentalCaptureDetail")
    private DentalCaptureDetail dentCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SourceExternalDigitalImageReferenceText")
    private String srcExtDigImgRef;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicDentalSetting")
    private FORDentalSetting forDentSetting;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DentalSubjectInformation")
    private DentalSubjectInformation dentSubInfo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "OriginalDentalEncodingSystemInformation")
    private DentalEncodingSystemInfo oriDentEncSysInfo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransmittalDentalEncodingSystemInformation")
    private DentalEncodingSystemInfo transmittalDentEncSysInfo;
    
    /*
    <!-- 12.009 HDD -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DentalHistoryDataDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DentalHistoryDataDetail> dentHistDataDetailList;
    
    /*
    <!-- 12.010 TDD -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothDataDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ToothDataDetail> toothDataDetailList;
    
    /*
    <!-- 12.011 -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MouthDataDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MouthDataDetail> mouthDataDetailList;
    
    /*
    <!-- 12.012 -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DentalStudyToothImprintDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DentalStudyToothImprintDetail> dentalStudyToothImprintDetailList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicDentalCommentText")
    private String forensicDentCom;
}