package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * TRANS = Transaction
 */
public class Transaction implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionDate")
    private NistDate transDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionDestinationOrganization")
    private Organization transDestOrg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionOriginatingOrganization")
    private Organization transOriginatingOrg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionUTCDate")
    private AssuranceDateTime transUTCDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionControlIdentification")
    private Identification transControlId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionControlReferenceIdentification")
    private Identification transControlRefId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionDomain")
    private TransactionDomain transDomain;
    
    /*
     *  <!--APS 1.016, Optional -->
     *  <!-- 0..99 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionApplicationProfile")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<TransactionApplicationProfile> tapList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionImageResolutionDetails")
    private TIResolutionDetails tiResolutionDetails;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionMajorVersionValue")
    private String transMajorVersion;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionMinorVersionValue")
    private String transMinorVersion;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionPriorityValue")
    private int transPriority;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionCategoryCode")
    private String transCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionContentSummary")
    private TransactionContentSummary tcs;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionCharacterSetDirectory")
    private TransactionCharacterSetDirectory tcsd;
   
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransactionGeographicNameSetCode")
    private String transGeoNameSetCode;
}