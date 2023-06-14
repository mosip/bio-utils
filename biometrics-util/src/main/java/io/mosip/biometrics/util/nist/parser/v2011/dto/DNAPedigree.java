package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class DNAPedigree implements Serializable {
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeIdentification")
    private Identification dnaPedId;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeMemberIdentification")
    private Identification dnaPedMemId;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeMemberStatusCode")
    private String dnaPedMemStatusCode;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeSampleIdentification")
    private Identification dnaPedSamId;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeFatherIdentification")
    private Identification dnaPedFatherId;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeMotherIdentification")
    private Identification dnaPedMotherId;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAPedigreeCommentText")
    private String dnaPedCom;
}