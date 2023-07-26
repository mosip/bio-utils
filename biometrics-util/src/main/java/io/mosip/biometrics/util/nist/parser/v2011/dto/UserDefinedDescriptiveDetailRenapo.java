package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
@JsonIgnoreProperties({ "DomainDefinedDescriptiveDetail"})
public class UserDefinedDescriptiveDetailRenapo extends UserDefinedDescriptiveDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "DomainDefinedDescriptiveDetail")
    private DDDescriptiveDetailRenapo ddDescDetailRenapo;
}