package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.ColorSpaces;
import io.mosip.biometrics.util.nist.parser.v2011.constant.CompressionCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.PixelDensityValues;
import io.mosip.biometrics.util.nist.parser.v2011.constant.SubjectAcquisitionProfileCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class FaceImage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "BinaryBase64Object")
    private String binaryBase64;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCaptureDetail")
    private CBEFFImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageColorSpaceCode")
    private ColorSpaces imgColorSpaceCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCommentText")
    private String imgCom;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCompressionAlgorithmText")
    private CompressionCodes imgAlg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalLineLengthPixelQuantity")
    private int imgHoriLineLengthPixelQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHorizontalPixelDensityValue")
    private PixelDensityValues imgHoriPixelDensity;    
    
    /*
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
    private PixelDensityValues imgVertPixelDensity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageDistortion")
    private ImageDistortion imgDist;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImage3DPoseAngle")
    private FI3DPoseAngle fi3DPoseAngle;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageAcquisitionProfileCode")
    private SubjectAcquisitionProfileCodes fiAcqProfCode;
    
    /*
     * <!-- 10.022 PXS -->
     * <!-- 0..9 -->
     * <!-- For legacy use only. -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageAttribute")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FIAttribute> fiAttrList;

    /*
     * <!-- 10.026 SXS -->
     * <!--0..50-->
     * <!--Use this element when the value is explicitly specified in Table 63 of ANSI/NIST-ITL 1-2011 Update:2013, Subject facial description codes, or is a physical characteristic from Annex D-->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageDescriptionCode")
    @JacksonXmlElementWrapper(useWrapping=false)    
    private List<String> fiDescrCodeList;
    
    /*
     * <!-- 10.026 SXS -->
     * <!--0..50-->
     * <!--  Use this element when the value is unformatted text (identified as "Other characteristics" in Table 63 of ANSI/NIST- ITL 1-2011 Update:2013, Subject facial description codes). -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageDescriptionText")
    @JacksonXmlElementWrapper(useWrapping=false)    
    private List<String> faceImageDescTextList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageEyeColorAttributeCode")
    private String faceImgEyeColorAttributeCode;
    
    /*
    <!-- 10.029 FFP -->
    <!--0..88-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageFeaturePoint")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FIFeaturePoint> fiFeaPointList;
    
    /*
     * <!-- 10.028 SHC -->
     * <!--0..2-->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageHairColorAttributeCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> fiHairColorAttributeCodeList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImagePoseOffsetAngleMeasure")
    private int fiPoseOffsetAngle;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageSubjectPoseCode")
    private String fiSubjectPoseCode;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageAcquisitionSource")
    private FIAcquisitionSource fiAcquisitionSrc;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageBoundingSquare")
    private FIBoundingSquare fiBoundingSquare;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageBoundary")
    private FPISegmentPositionPolygon fiBoundary;
    
    /*
     * <!-- 10.019 LAF -->
     * <!--0..3-->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageLightingArtifactsCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> fiLightingArtifactsCodeList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageFeaturePointTierCode")
    private int fiFeaturePointTierCode;    
    
    /*
    <!-- 10.032 3DF -->
    <!--0..88-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImage3DFeaturePoint")
    @JacksonXmlElementWrapper(useWrapping=false)    
    private List<FIFeaturePoint> fi3DFeaPointList;
    
    /*
    <!-- 10.033 FEC -->
    <!-- 0..12 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageContour")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FPISegmentPositionPolygon> fiContourList;
    
    /*
    <!-- 10.045 OCC -->
    <!-- 0..16 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageOcclusion")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FIOcclusion> fiOcclusionList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalFeatureReferenceIdentification")
    private Identification physFeaRefId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageTransformationCode")
    private String imgTransCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectExistentialDetails")
    private SubjectExistentialDetails subed;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PatternedInjuryDetail")
    private PatternedInjuryDetail patid;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RulerScalePresenceInformation")
    private RulerScalePresenceInfo rspInfo;
}