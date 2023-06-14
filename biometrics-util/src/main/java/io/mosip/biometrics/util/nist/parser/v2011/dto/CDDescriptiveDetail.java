package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * CD = CustomDefined
 */
public class CDDescriptiveDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "put_whatever_you_like")
    private String put_whatever_you_like;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "this_wont_be_validated")
    private String this_wont_be_validated;
}