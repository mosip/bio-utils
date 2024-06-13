package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class ErrorMessage implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "ErrorCode")
    private int errorCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "ErrorFieldMnemonicCode")
    private String errorMnemonicCode;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "ErrorMessageText")
	@SuppressWarnings({ "java:S1700" })
    private String errorMessage;
}