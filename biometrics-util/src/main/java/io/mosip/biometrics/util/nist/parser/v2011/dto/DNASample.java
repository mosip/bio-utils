package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class DNASample implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "BiometricCaptureDetail")
    private BiometricCaptureDetail captureDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratory")
    private DNALaboratory dnaLab;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAAnalysisQuantityCode")
    private int dnaAnaQuantCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNADonor")
    private DNADonor dnaDonor;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAClaimedRelationshipCode")
    private int dnaClaimRelCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAValidatedRelationshipCode")
    private int dnaValidatedRelCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigree")
    private DNAPedigree dnaPed;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNASampleOrigin")
    private DNASampleOrigin dnaSamOrigin;    
    
    /*
    <!-- 18.011 STI -->
    <!--1..5-->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNATypingTechnologyCategoryCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Integer> dnaTypTecCatCodeList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNASampleCollectionMethodText")
    private String dnaSameColMethod;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAProfileStorageDate")
    private NistDate dnaProStorageDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAProfile")
    private DNAProfile dnaPro;    
    
    /*
    <!-- 18.016 STR -->
    <!-- 0..* -->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNASTRProfile")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DNASTRProfile> dnaStrProList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitochondrialData")
    private DNAMitochondrialData dnaMitochondrialData;
    
    /*
    <!-- 18.019 EPD -->
    <!-- 0..* -->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAElectropherogram")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DNAElectropherogram> dnaElectropherogramList;
    
    /*
    <!-- 18.023 EPL -->
    <!-- 0..* -->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAElectropherogramLadder")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DNAElectropherogram> dnaElectropherogramLadderList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAGenotypeDistributionCode")
    private int dnaGenotypeDistCode;
    
    /*
    <!-- 18.021 GAP -->
    <!-- 0..* -->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAGenotypeAllelePair")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DNAGenotypeAllelePair> dnaGenotypeAllelePairList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNACommentText")
    private String dnaCom;
}