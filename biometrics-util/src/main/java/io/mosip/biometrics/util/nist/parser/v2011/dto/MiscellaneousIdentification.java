package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class MiscellaneousIdentification implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "InterpolReferenceNumber")
    private String interpolRefNo;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "NationalPersonIdentifier")
    private NationalPersonIdentification natPersonId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "NationalPersonInquiryIdentifier")
    private NationalPersonIdentification natPersonInquiryId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "NationalCaseIdentifier")
    private NationalPersonIdentification natCaseId;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "LatentCaseEvidenceIdentifier")
    private String latentCaseEvidenceIdentifier;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "LatentIdentifier")
    private String latentIdentifier;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "LatentUniqueIdentifier")
    private String latentUniqueIdentifier;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "NationalBusinessReference")
    private NationalPersonIdentification natBusinessRefId;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "MiscellaneousIdentificationID1")
    private MiscellaneousIdentificationID miscId1;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "MiscellaneousIdentificationID2")
    private MiscellaneousIdentificationID miscId2;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "MiscellaneousIdentificationID3")
    private MiscellaneousIdentificationID miscId3;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "MiscellaneousIdentificationID4")
    private MiscellaneousIdentificationID miscId4;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "MiscellaneousIdentificationID5")
    private MiscellaneousIdentificationID miscId5;
}