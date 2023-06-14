package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * FRE = FrictionRidge
 * FREI = FrictionRidgeImage
 */
public class FrictionRidgeImage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageBitsPerPixelQuantity")
    private int imgBitsPerPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCaptureDetail")
    private CBEFFImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCommentText")
    private String imgCom;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCompressionAlgorithmText")
    private String imgCompAlg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalLineLengthPixelQuantity")
    private int imgHoriLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalPixelDensityValue")
    private int imgHoriPixelDensity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageScaleUnitsCode")
    private int imgScaleUnitsCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalLineLengthPixelQuantity")
    private int imgVertLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageVerticalPixelDensityValue")
    private int imgVertPixelDensity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintImageImpressionCaptureCategoryCode")
    private int fpImgImprCapCatCode;
    
    /*
    <!-- 13.013 FGP -->
    <!--1..6-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgePositionCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Integer> frePositionCodeList;
    
    /*
    <!--<!-\- FGP -\-> <!-\-1..6-\->
    Note: This element has been DEPRECATED
    <biom:PalmPositionCode>	
    <!-\- FGP -\-> <!-\-1..6-\->
    Note: This element has been DEPRECATED
    <biom:PlantarPositionCode>	
    <!-\- FGP -\-> <!-\-1..6-\->	
    Note: This element has been DEPRECATED
   <biom:FingerPositionCode>-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PalmPositionCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    @Deprecated
    private List<Integer> palmPositionCodeList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PlantarPositionCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    @Deprecated
    private List<Integer> plantarPositionCodeList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerPositionCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    @Deprecated
    private List<Integer> fingerPositionCodeList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintImageMajorCasePrint")
    private FPIMajorCasePrint fpiMajorCasePrint;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> freiQualityList;
        
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> fpiQualityList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PalmprintImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> ppiQualityList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PlantarImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> pliQualityList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "StandardFingerprintFormNumberText")
    private String standardFpFormNo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ResolutionMethodInformation")
    private ResolutionMethodInformation resmi;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectExistentialDetails")
    private SubjectExistentialDetails subed;
}