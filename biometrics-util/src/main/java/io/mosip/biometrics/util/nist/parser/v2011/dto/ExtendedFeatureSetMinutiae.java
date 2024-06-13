package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
@SuppressWarnings({ "java:S6539" })
public class ExtendedFeatureSetMinutiae implements Serializable {
    /**
    <!-- 9.303 FSP -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ExtendedFeatureSetProfileIdentification")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Identification> extFeaProfIdList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageRegionOfInterest")
    private FREIRegionOfInterest freiRegOfInt;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintImageFingerprintOrientation")
    private FPIFingerprintOrientation fpImgFpOrientation;    
    
    /**
    <!-- 9.302 FPP -->
    <!--1..20-->
    <!-- Although each individual friction ridge element allowed here is optional, at least one of these MUST appear: 
    biom:MinutiaeFrictionRidgeLocation
    biom:MinutiaePalmLocation
    biom:MinutiaePlantarLocation
    biom:MinutiaeFingerLocation
    (Samples below)
    -->
    <!-- This element may be used for any friction ridge impression.  -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFrictionRidgeLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MINFRLocation> mfrLocList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaePalmLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaePalmLocation> minutiaePalmLocList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaePlantarLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaePlantarLocation> minutiaePlantLocList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFingerLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeFingerLocation> minutiaeFingLocList;

    /**
    <!-- 9.307 PAT -->
    <!--0..7-->
    <!-- This field shall only be used for fingerprints, and shall be omitted for other friction ridge impressions. -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintPatternClassification")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FPPClassification> fpPatClassList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeQualityMap")
    private MRQualityMap mrQualityMap;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeFlowMap")
    private MRFlowMap mrFlowMap;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeWavelengthMap")
    private MRWavelengthMap mrWavelengthMap;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageTonalReversalCode")
    private String fricRidgeImgTonalRevCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageLateralReversalCode")
    private String fricRidgeImgLatRevCode;
    
    /**
    <!-- 9.316 FQM -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ImageQuality> imageQualityList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeGrowthOrShrinkage")
    private GrowthOrShrinkage minutiaeGrowthOrShrinkage;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoCoresPresentIndicator")
    private Boolean minutiaeNoCoresPresInd;
    
    /**
    <!-- 9.320 COR -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeCore")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeCore> minutiaeCoreList;   
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoDeltasPresentIndicator")
    private Boolean minutiaeNoDeltasPresInd;
    
    /**
    <!-- 9.321 DEL -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeDelta")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeDelta> minutiaeDeltaList;    
    
    /**
    <!-- 9.322 CDR -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeCountCoreToDelta")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MINRCountCoreToDelta> minrCountCoreToDeltaList;

    /**
    <!-- 9.323 CPR -->
    <!--0..3-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageCenter")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FRICenter> friCenterList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoDistinctiveFeaturesPresentIndicator")
    private Boolean minutiaeNoDistFeatPresInd;
    
    /**
    <!-- 9.324 DIS -->
    <!-- 0..99 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeDistinctiveFeature")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeDistinctiveFeature> minutiaeDistFeatList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoMinutiaePresentIndicator")
    private Boolean minutiaeNoMinutiaePresentInd;
    
    /**
    <!-- 9.331 MIN -->
    <!-- 0..999 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "EFSMinutia")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<EFSMinutia> efsMinutiaList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "EFSRidgeCountAlgorithmCode")
    private String efsRidgeCountAlgCode;
    
    /**
    <!-- 9.333 MRC -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "EFSRidgeCountItem")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<EFSRidgeCountItem> eFSRidgeCountItemList;

    /**
    <!-- 9.335 RCC -->
    <!-- 0..7992 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeCountConfidence")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MINRCountConfidence> minrCountConfList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoDotsPresentIndicator")
    private Boolean minutiaeNoDotsPresentIndicator;
    
    /**
    <!-- 9.346 NDOT -->
    <!-- 0..1 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeDot")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeDot> minutiaeDotList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoIncipientRidgesPresentIndicator")
    private Boolean minutiaeNoIncipientRidgesPresInd;
    
    /**
    <!-- 9.345 POR -->
    <!-- 0..9999-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaePore")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaLocationPoint> minutiaePoreList;
    
    /**
    <!-- 9.350 MFD -->
    <!-- 0..99 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFeatureDetection")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MFDetection> minutiaeFeatDetList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeCommentText")
    private String minutiaeCommentText;
    
    /**
    <!-- 9.352 LPM -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "LatentProcessingCategoryCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> latProcCatCodeList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeValueAssessment")
    private MinutiaeValueAssessment minutiaeValueAssessment;
    
    /**
    <!-- 9.354 EOF -->
    <!--0..4-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFraudEvidence")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeFraudEvidence> minfreList;
    
    /**
    <!-- 9.355 LSB -->
    <!--0..3-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeLatentSubstrate")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeLatentSubstrate> minlsList;
    
    /**
    <!-- 9.356 LMT -->
    <!--0..3-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeLatentMatrix")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeLatentMatrix> minlmList;
    
    /**
    <!-- 9.357 LQI -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeLocalQualityIssues")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeLocalQualityIssues> minlqIssueList;
    
    /**
    <!-- 9.360 AOC -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageAreaOfCorrespondence")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FRIAreaOfCorrespondence> friAOCList;
    
    /**
    <!-- 9.341 INR -->
    <!-- 0..999 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeIncipientRidge")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeIncipientRidge> mininrList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoCreasesPresentIndicator")
    private Boolean minutiaeNoCreasesPresentInd;
    
    /**
    <!-- 9.342 CLD -->
    <!-- 0..999 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFlexionCrease")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeIncipientRidge> mininrFlexionCreaseList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoRidgeEdgeFeaturesPresentIndicator")
    private Boolean minutiaeNoRidgeEdgeFeatPresInd;
    
    /**
    <!-- 9.343 REF -->
    <!-- 0..999-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeEdgeOrDiscontinuity")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MREdgeOrDiscontinuity> mrEdgeOrDiscontinuitieList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeNoPoresPresentIndicator")
    private Boolean minutiaeNoPoresPresentInd;
    
    /**
    <!-- 9.361 CPF -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFeatureCorrespondence")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MINFCorrespondence> minutiaeFeatCorrList;
    
    /**
    <!-- 9.362 ECD -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeExaminerComparisonDetermination")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MINEXComparisonDetermination> minutiaeExamComparDeterList;
    
    /**
    <!-- 9.363 RRC -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageRelativeRotation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FRIRelativeRotation> friRelRotList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeSkeletonizedImageBinaryObject")
    private String frSkeletonizedImageBinaryObject;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeImageRidgePathRepresentation")
    private MIRPathRepresentation mirPathRep;
    
    /**
    <!-- 9.380 TPL -->
    <!-- 0..999 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "EFSTemporaryLine")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<EFSTemporaryLine> efsTempLineList;
    
    /**
    <!-- 9.381 FCC -->
    <!-- 0..999 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "EFSFeatureColor")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<EFSFeatureColor> efsFeaColorList;
}