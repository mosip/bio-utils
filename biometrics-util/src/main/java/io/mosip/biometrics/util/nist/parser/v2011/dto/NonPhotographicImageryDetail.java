package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class NonPhotographicImageryDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "BiometricCaptureDetail")
    private CBEFFImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCategoryCode")
    private String imgCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "NonPhotographicImageryCommentText")
    private String nonPhotographicImgCom;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectExistentialDetails")
    private SubjectExistentialDetails subed;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "NonPhotographicImageTypeCode")
    private String nonPhotographicimgTypeCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "NonPhotographicImageFormatCode")
    private String nonPhotographicimgFormatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DentalRadiographImageInformation")
    private DentalRadiographImageInfo denRadiographIMgInfo;
}