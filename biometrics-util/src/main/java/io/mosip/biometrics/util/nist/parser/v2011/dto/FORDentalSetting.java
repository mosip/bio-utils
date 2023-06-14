package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * FOR = Forensic
 */
public class FORDentalSetting implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicAnalystCategoryCode")
    private String forAnalystCatCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "OrganizationPrimaryContactInformation")
    private PrimaryContactInfo orgPriContInfo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicSourceCountryISO3166Alpha2Code")
    private String forSrcCountryISO3166Alpha2Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicSourceCountryISO3166Alpha3Code")
    private String forSrcCountryISO3166Alpha3Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicSourceCountryISO3166NumericCode")
    private String forSrcCountryISO3166NumericCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicSourceCountryGENCAlpha2Code")
    private String forSrcCountryGENCAlpha2Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicSourceCountryGENCAlpha3Code")
    private String forSrcCountryGENCAlpha3Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ForensicSourceCountryGENCNumericCode")
    private String forSrcCountryGENCNumericCode;
}