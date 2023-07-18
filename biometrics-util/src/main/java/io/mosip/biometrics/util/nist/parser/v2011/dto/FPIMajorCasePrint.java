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
public class FPIMajorCasePrint implements Serializable {
    /**
    <!-- 13.014	SPD - PDF -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerPositionCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<Integer> fingerPosCodeList;

    /**
    <!-- FIC -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MajorCasePrintCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<String> majorCasePrintCodeList;
    
    /**
    <!-- 13.015	PPC -->
    <!-- 0..12 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MajorCasePrintSegmentOffset")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MajorCasePrintSegmentOffset> majorCasePrintSegOffsetList;
}