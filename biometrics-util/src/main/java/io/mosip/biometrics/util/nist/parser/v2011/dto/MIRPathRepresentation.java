package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * MIR = MinutiaeImageRidge
 */
public class MIRPathRepresentation implements Serializable {
	/**
	<!--1..*-->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgePathSegment")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MRPathSegment> mrPathSegmentList;
}