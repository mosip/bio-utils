package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * CD = CustomDefined
 */
public class CDDescriptiveDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "putWhateverYouLike")
    private String putWhateverYouLike;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "thisWontBeValidated")
    private String thisWontBeValidated;
}