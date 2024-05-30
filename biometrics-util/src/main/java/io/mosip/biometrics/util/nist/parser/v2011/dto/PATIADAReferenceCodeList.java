package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * PATI = PatternedInjury
 */
public class PATIADAReferenceCodeList implements Serializable {
	/**
	 * <!-- 0..* -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PatternedInjuryADAReferenceCodeText")
    @JacksonXmlElementWrapper(useWrapping=false)
	@SuppressWarnings({ "java:S1700" })
    private List<String> patiADAReferenceCodeList;
}