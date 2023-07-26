package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import io.mosip.biometrics.util.nist.parser.v2011.helper.LocalDateTimeDeserializer;
import io.mosip.biometrics.util.nist.parser.v2011.helper.LocalDateTimeSerializer;
import lombok.Data;

@Data
public class DentalSubjectInformation implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectStatusCode")
    private String subStatus;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectLastContactDate")
    private NistDate subLastContDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectLastContactDateEstimateRange")
    private String subLastContDateEstiRange;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "PersonBirthDate")
    private NistDate perBirDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectBirthDateEstimateRange")
    private String subBirDateEstiRange;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PersonEthnicityText")
    private String perEthText;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectDNARecordsAvailableCode")
    private int subDNARecAvailCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectCollectionLocationDescriptionText")
    private String subColLocDesc;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectEstimatedDeathDate")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class)  
    private LocalDateTime subEstiDeathDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectDeathDateRange")
    private String subDeathDateRange;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectDeathTimeEstimateRationaleText")
    private String subDeathTimeEstiRationale;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectDeathAgeEstimateText")
    private String subDeathAgeEsti;
}