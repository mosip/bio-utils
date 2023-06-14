package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class Minutiae implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "MinutiaeNISTStandard")
    private MinutiaeNISTStandard minutiaeNISTStandard;

    /*
     * <!-- 9.008 CRP -->
     * <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFingerCorePosition")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<CoreLocation> minutiaeFingerCorePosList;

    /*
     *  <!-- 9.009 DLT -->
     *  <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFingerDeltaPosition")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<CoreLocation> minutiaeFingerDeltaPosList;

    /*
     *  <!-- 9.007FPC -->
     *  <!-- 0..* -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "MinutiaeFingerPatternDetail")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeFingerPatternDetail> minutiaeFingerPatDetailList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeFingerPositionCode")
    private int minutiaeFingerPosCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaePalmPositionCode")
    private int minutiaePalmPosCode;
}