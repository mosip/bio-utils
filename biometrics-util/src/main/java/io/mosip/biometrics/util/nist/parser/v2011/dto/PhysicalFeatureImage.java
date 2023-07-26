package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.ColorSpaces;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * PHFI = PhysicalFeatureImage
 * PHF = PhysicalFeature
 */
public class PhysicalFeatureImage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
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
    
    /**
     * <!-- 10.024 SQS -->
     * <!-- 0..9 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> imgQualityList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageScaleUnitsCode")
    private int imgScaleUnitsCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCategoryCode")
    private String imgCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalLineLengthPixelQuantity")
    private int imgVertLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalPixelDensityValue")
    private int imgVertPixelDensity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageDistortion")
    private ImageDistortion imgd;        
    
    /**
     * <!-- 0..9 -->
     * <!--Used for Physical Features Only-->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeatureDescriptionDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PHFDescriptionDetail> phfDescDetailList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeatureNCICCode")
    private String phfNCICCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeatureSize")
    private PHFSize phfSize;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeatureReferenceIdentification")
    private Identification phfRefId;

    /**
    * <!-- 10.032 3DF -->
    * <!--0..88-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImage3DFeaturePoint")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FIFeaturePoint> fi3DFeaturePointList;

    /**
     * <!-- 10.044 ITX -->
     * <!-- 0..18 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageTransformationCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> imgTransCodeList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectExistentialDetails")
    private SubjectExistentialDetails subed;

    /**
     * <!-- 10.048 PID -->
     * <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PatternedInjuryDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PatternedInjuryDetail> piDetailList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CheiloscopicImageInformation")
    private CheiloscopicImageInfo ciInfo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DentalVisualImageDataInformation")
    private DentalVisualImageDataInfo dviDataInfo;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RulerScalePresenceInformation")
    private RulerScalePresenceInfo rspInfo;
}