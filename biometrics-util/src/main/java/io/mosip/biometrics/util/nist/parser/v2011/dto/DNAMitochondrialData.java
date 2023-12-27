package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class DNAMitochondrialData implements Serializable {
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoControlRegion1Text")
    private String dnaMitoControlReg1;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoControlRegion2Text")
    private String dnaMitoControlReg2;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoBaseStartNumeric")
    private int dnaMitoBaseStartNum;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoBaseEndNumeric")
    private int dnaMitoBaseEndNum;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoBaseAdenineQuantity")
    private int dnaMitoBaseAdenineQuantity;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoBaseGuanineQuantity")
    private int dnaMitoBaseGuanineQuantity;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoBaseCytosineQuantity")
    private int dnaMitoBaseCytosineQuantity;
	
	@JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNAMitoBaseThymineQuantity")
    private int dnaMitoBaseThymineQuantity;
}