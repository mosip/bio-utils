package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class RecordingSettingDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCategoryCode")
	private String recSrcCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SourceOrganizationName")
	private String srcOrgName;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "OrganizationPrimaryContactInformation")
	private PrimaryContactInfo priContInfo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCountryISO3166Alpha2Code")
	private String recSrcCountryISO3166Alpha2Code;

    /**
    <!--Used for 3-character representations of ISO 3166-1.-->
    <!--<biom:RecordingSourceCountryISO3166Alpha3Code/>
         <!-\- CSC -\-> <!-\- 0..1 -\-><!-\-Used for numeric representations of ISO 3166-1.-\->
         <biom:RecordingSourceCountryISO3166NumericCode>
         <!-\- CSC -\-> <!-\- 0..1 -\-><!-\-Used for 2-character representations of GENC. -\->
         <biom:RecordingSourceCountryGENCAlpha2Code/>
         <!-\- CSC -\-><!-\- 0..1 -\-> <!-\-Used for 3-character representations of GENC.-\->
         <biom:RecordingSourceCountryGENCAlpha3Code/>	
         <!-\- CSC -\-> <!-\- 0..1 -\-><!-\-Used for numeric representations of GENC-\->
         <biom:RecordingSourceCountryGENCNumericCode/>	-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCountryISO3166Alpha3Code")
	private String recSrcCountryISO3166Alpha3Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCountryISO3166NumericCode")
	private String recSrcCountryISO3166NumericCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCountryGENCAlpha2Code")
	private String recSrcCountryGENCAlpha2Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCountryGENCAlpha3Code")
	private String recSrcCountryGENCAlpha3Code;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSourceCountryGENCNumericCode")
	private String recSrcCountryGENCNumericCode;
}