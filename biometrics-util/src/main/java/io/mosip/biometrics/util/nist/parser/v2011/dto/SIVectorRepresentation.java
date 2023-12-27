package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * SI = SignatureImage
 */
public class SIVectorRepresentation implements Serializable {
	/**
	 *  <!-- 2..* -->
     *  <!-- Each occurrence of this element represents a single vector in the list, as specified in the description of Field 8.008: Signature image data / DATA. -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SignatureImageVector")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SIVector> siVectorList;
}