package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * RecordImage = recImage
 */
public class RecordImage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCaptureDetail")
    private ImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalLineLengthPixelQuantity")
    private int imgHoriLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalLineLengthPixelQuantity")
    private int imgVertLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "ImageRepresentationCode")
    private int imgRepCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "ColorSpaceCode")
    private String colorSpaceCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "Documento")
    private Documento doc;
}
