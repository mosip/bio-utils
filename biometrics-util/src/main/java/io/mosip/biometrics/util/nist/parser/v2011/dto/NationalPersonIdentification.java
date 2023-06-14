package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
/*
 * NAT = National
 * NATPI = NationalPersonIdentification
 */
public class NationalPersonIdentification extends Identification implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "LocationCountryISO3166Alpha3Code")
    private String locCountryISO3166Alpha3Code;
}