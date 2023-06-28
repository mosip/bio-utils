package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.ColorSpaces;
import io.mosip.biometrics.util.nist.parser.v2011.constant.IrisEyePositionCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class IrisImage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageBitsPerPixelQuantity")
    private int imgBitsPerPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCaptureDetail")
    private CBEFFImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageColorSpaceCode")
    private ColorSpaces imgColorSpaceCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCommentText")
    private String imgCom;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCompressionAlgorithmText")
    private String imgCompAlg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalLineLengthPixelQuantity")
    private int imgHoriLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalPixelDensityValue")
    private int imgHoriPixelDensity;
    
    /*
     *  <!-- 17.024	IQS -->
     *  <!-- 0..9 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)    
    private List<ImageQuality> imgQualityList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageScaleUnitsCode")
    private int imgScaleUnitsCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalLineLengthPixelQuantity")
    private int imgVertLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalPixelDensityValue")
    private int imgVertPixelDensity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisEyePositionCode")
    private IrisEyePositionCodes irisEyePosCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisEyeRotationAngleText")
    private String irisEyeRotAngle;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisEyeRotationUncertaintyValueText")
    private String irisEyeRotUncertainty;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageCapture")
    private IrisImageCapture irisImgCap;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisEyeColorAttributeCode")
    private String irisEyeColorAttCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageAcquisitionLightingSpectrum")
    private AcquisitionLightingSpectrum irisAcqls;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisDiameterPixelQuantity")
    private int irisDiamPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageMissingReasonCode")
    private String irisImgMissingReason;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageAcquisitionProfileCode")
    private int irisImgAcqProfileCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageStorageFormatCode")
    private int irisImgStorageFormatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageIrisPupilBoundary")
    private IrisImageBoundary irisImgPupilBoundary;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageIrisScleraBoundary")
    private IrisImageBoundary irisImgScleraBoundary;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageIrisUpperEyelidBoundary")
    private IrisImageBoundary irisImgUpperEyelidBoundary;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageIrisLowerEyelidBoundary")
    private IrisImageBoundary irisImgLowerEyelidBoundary;
    
    /*
     *  <!-- 17.037	NEO -->
     *  <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageOcclusion")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<IrisImageOcclusion> irisImgOccList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageRangeMeasure")
    private int irisImgeRange;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageGazeAngleMeasure")
    private int irisImgGazeAngle;
}