package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * FPI = FingerprintImage
 */
public class FPIPosition implements Serializable {
	/**
	<!-- 1..6 -->
	*/ 
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerPositionCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Integer> fingerPosCodeList;
}