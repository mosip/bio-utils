package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * SPI = SpeakerIdentifier
 */
public class SpeakerIdentifier implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SpeakerIdentifier")
	private int speakerId;
}