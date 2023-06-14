package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class Source implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "BiometricCaptureDetail")
    private CBEFFImageCaptureDetail imgCapDetail;    
    
    /*
    <!-- 20.019 TIX -->
    <!--0..99 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TimeSegment")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TimeSegment> tsist;

    /*
    <!-- 20.016 SEG -->
    <!-- 0..99 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageSegment")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ContextImageSegment> imgSegList;
}