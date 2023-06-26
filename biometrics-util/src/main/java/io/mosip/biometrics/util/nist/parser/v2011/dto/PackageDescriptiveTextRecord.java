package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class PackageDescriptiveTextRecord extends BiometricDataRecord implements Serializable {
	/*
	 *  <!-- User Defined Fields 2.003 999 -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "UserDefinedDescriptiveDetail")
    private UserDefinedDescriptiveDetailRenapo userDefinedDescDetail;
}