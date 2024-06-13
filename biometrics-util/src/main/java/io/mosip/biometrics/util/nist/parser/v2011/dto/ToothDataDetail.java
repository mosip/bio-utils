package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * TH = tooth
 */
public class ToothDataDetail implements Serializable {
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothDataRecordingDate")
	private NistDate thDataRecordingDate;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothDataRecordingDateEstimateRange")
	private String thDataRecordingDateEstimateRange;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothID")
	private int thID;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "OriginalSystemEncodingText")
	private String originalSysEnc;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothDataADAReferenceCodeList")
	private ThDataAdaReferenceList thDataADARefCodeList;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TransmittedToothEncodingText")
	private String transmittedThEnc;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothIDCertaintyCode")
	private int thIdCertaintyCode;

	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ToothDataAdditionalDescriptiveText")
	private String thDataAddDesc;
}