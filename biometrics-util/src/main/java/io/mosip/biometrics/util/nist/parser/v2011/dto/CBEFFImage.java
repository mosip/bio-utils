package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class CBEFFImage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCaptureDetail")
    private CBEFFImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> imgQualList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFFormatOwnerIdentification")
    private Identification cbeffFormatOwnerId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFFormatCategoryIdentification")
    private Identification cbeffFormatCatId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFVersionIdentification")
    private Identification cbeffVersionId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFCategoryCode")
    private int cbeffCatCode;
}