package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * T2CR = Type2CrossReference
 */
public class T2CRIdentificationList implements Serializable {
	/**
	 * <!-- 0..98 -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "Type2CrossReferenceIdentification")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Identification> t2crIdList;
}