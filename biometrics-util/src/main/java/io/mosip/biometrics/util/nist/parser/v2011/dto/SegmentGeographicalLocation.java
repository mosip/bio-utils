package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class SegmentGeographicalLocation implements Serializable {
    /**
    <!-- 1..600000 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentIdentifierList")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SegmentIdentifier> segIdList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "LocationDescriptionText")
    private String locDesc;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "LocationGeographicElevation")
    private GeographicElevation locGeoElevation;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "LocationTwoDimensionalGeographicCoordinate")
    private TwoDimGeographicCoordinate locTwoDimGeoCoord;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "LocationUTMCoordinate")
    private LocationUTMCoordinate locUTMCoord;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "LocationAlternateGeographicSystemValue")
    private AlternateGeographicSystem locAlternateGeographicSys;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CellPhoneTowerCodeText")
    private String cellPhoneTowerCode;
}