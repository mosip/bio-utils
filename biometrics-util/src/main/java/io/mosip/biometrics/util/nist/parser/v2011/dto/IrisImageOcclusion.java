package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.OcclusionCategoryCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.OcclusionOpacityCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class IrisImageOcclusion implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageOcclusionOpacityCode")
    private OcclusionOpacityCodes irisImgOccOpacity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImageOcclusionCategoryCode")
    private OcclusionCategoryCodes irisImgOccCat;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageFeatureVertexQuantity")
    private int imgFeatVertexQuantity;
    
    /*
     * <!--3..99-->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageFeatureVertex")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<CoreLocation> imgFeatVertexList;
}