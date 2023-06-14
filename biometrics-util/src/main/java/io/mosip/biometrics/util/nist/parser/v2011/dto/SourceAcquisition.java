package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * SRCA = SourceAcquisition
 * ACQ = Acquisition
 */
public class SourceAcquisition implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AcquisitionSourceCode")
    private int acqSrcCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AcquisitionDigitalConversionDescriptionText")
    private String acqDigitalConversionDesc;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AcquisitionFormatDescriptionText")
    private String acqFormatDesc;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AcquisitionSpecialCharacteristicsText")
    private String acqSpecialCharacteristics;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AcquisitionRadioTransmissionFormatDescriptionText")
    private String acqRadioTransmissionFormatDesc;
}