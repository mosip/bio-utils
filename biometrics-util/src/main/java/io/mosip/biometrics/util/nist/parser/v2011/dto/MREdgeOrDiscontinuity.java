package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * MR = MinutiaeRidge
 */
public class MREdgeOrDiscontinuity extends MinutiaLocationPoint implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeEdgeOrDiscontinuityCategoryCode")
    private String mrEdgeOrDiscontinuityCatCode;
}