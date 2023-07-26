package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.GeodeticDatumCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * TwoDim =TwoDimensional
 */
public class TwoDimGeographicCoordinate implements Serializable {    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "GeographicCoordinateLatitude")
	private GeographicCoordinateLatitude geoCoordLat;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = "GeographicCoordinateLongitude")
    private GeographicCoordinateLongitude geoCoordLong;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "GeodeticDatumCoordinateSystemCode")
    private GeodeticDatumCodes geodeticDatumCoordSysCode;
}