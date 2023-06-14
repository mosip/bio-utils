package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class INCITSMinutiae implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFFormatOwnerIdentification")
    private Identification cbeffFormatOwnerId;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFFormatCategoryIdentification")
    private Identification cbeffFormatCatId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFProductIdentification")
    private Identification cbeffProductId;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageCaptureDetail")
    private INCITSMinutiaeImageCaptureDetail imgCapDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerImpressionImage")
    private INCITSMinutiaeFingerImpressionImage fingerImprImg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerViewNumeric")
    private int fingerViewNum;

    /*
    <!-- 9.135 FQD -->
    <!--1..9-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeQuality")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<MinutiaeQuality> minutiaeQualityList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeQuantity")
    private int minutiaeQuantity;

    /*
    <!-- 9.137 FMD -->
    <!--1..9999-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "INCITSMinutia")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<INCITSMinutia> incitsMinutiaList;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "MinutiaeRidgeCountDetail")
    private MINRCountDetail minrCountDetail;
    
    /*
    <!-- 9.139 CIN -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintPatternCoreLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<CoreLocation> fppCoreLocList;

    /*
    <!-- 9.140 DIN -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerprintPatternDeltaLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<CoreLocation> fppDeltaLocList;
}