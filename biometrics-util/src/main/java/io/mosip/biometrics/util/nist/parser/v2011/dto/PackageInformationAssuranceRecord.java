package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class PackageInformationAssuranceRecord extends BiometricDataRecord implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssuranceFormatOwnerIdentification")
    private Identification assuranceFormatOwnerId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssuranceOrganization")
    private Organization assuranceOrg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssuranceFormatIdentification")
    private Identification assuranceFormatId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssuranceDateTime")
    private AssuranceDateTime assuranceDateTime;
    
    /**
     *  <!-- 98.900	ALF -->
     *  <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssuranceLogEntry")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<AssuranceLogEntry> assuranceLogEntrieList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssuranceAuditRevisionIdentification")
    private Identification assuranceAuditRevisionId;
}