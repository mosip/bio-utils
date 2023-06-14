package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * SCI = SegmentContentInfo 
 * SI = segmentIdentifier
 * ST = segmentTranscript
 * STT = segmentTranslation
 * SP = segmentPhonetic
 * TA = transcriptAuthority
 */
public class SegmentContentInfo implements Serializable {
	/*
	* <!--1..600000-->
	*/
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentIdentifierList")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SegmentIdentifier> siList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentTranscriptText")
    private String stText;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentTranscriptLanguageCode")
    private String stLangCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentPhoneticTranscriptText")
    private String spTranscript;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhoneticTranscriptConvention")
    private String spTranscriptConvention;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentTranslationText")
    private String sttText;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentTranslationLanguageCode")
    private String sttLangCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentContentCommentText")
    private String segContCom;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TranscriptAuthorityCommentText")
    private String taCom;
}