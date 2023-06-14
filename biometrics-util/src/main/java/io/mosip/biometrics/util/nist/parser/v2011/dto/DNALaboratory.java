package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.LaboratoryCategoryCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.LaboratoryUnitCategoryCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class DNALaboratory implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "OrganizationName")
    private String orgName;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "OrganizationPrimaryContactInformation")
    private PrimaryContactInfo orgPrimContInfo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryUnitCategoryCode")
    private LaboratoryUnitCategoryCodes dnaLabUnitCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryCategoryCode")
    private LaboratoryCategoryCodes dnaLabCatCode;
    
    /*
    <!-- ACC -->
    <!--0..6-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryAccreditation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DNALabAccreditation> dnaLabAccredList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryProcessingCountryISO3166Alpha2Code")
    private String dnaLabProcCounISO3166Alpha2Code;
        
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryProcessingCountryISO3166Alpha3Code")
    private String dnaLabProcCounISO3166Alpha3Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryProcessingCountryISO3166NumericCode")
    private String dnaLabProcCounISO3166NumericCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryProcessingCountryGENCAlpha2Code")
    private String dnaLabProcCounGENCAlpha2Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryProcessingCountryGENCAlpha3Code")
    private String dnaLabProcCounGENCAlpha3Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryProcessingCountryGENCNumeric")
    private String dnaLabProcCounGENCNumeric;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryInternationalOrganizationName")
    private String dnaLabIntOrgName;
}