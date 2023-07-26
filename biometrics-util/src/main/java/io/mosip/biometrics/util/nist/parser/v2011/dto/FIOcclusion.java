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
/**
 * FI = FaceImage
 */
public class FIOcclusion implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageOcclusionOpacityCode")
    private OcclusionOpacityCodes fiOcclOpacityCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FaceImageOcclusionCategoryCode")
    private OcclusionCategoryCodes fiOcclCatCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PositionPolygonVertexQuantity")
    private int posPolyVertexQuantity;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PositionPolygonVertex")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<CoreLocation> posPolyVertexList;
}