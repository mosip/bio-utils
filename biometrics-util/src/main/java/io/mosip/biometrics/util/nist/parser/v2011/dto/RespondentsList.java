package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class RespondentsList implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "TransactionCandidate")
    private TransactionCandidate transCandidate;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "TransactionCandidatesTotalQuantity")
    private int transCandidatesTotalQuantity;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "TransactionCategoryCode")
    private String transCatCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "TransactionSearchResultsCode")
    private String transSearchResultsCode;
}