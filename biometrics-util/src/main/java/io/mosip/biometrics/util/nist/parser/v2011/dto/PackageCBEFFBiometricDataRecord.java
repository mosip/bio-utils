package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class PackageCBEFFBiometricDataRecord extends BiometricDataRecord implements Serializable {
	/**
	 * <!-- 11.902	ANN -->
     * <!-- 0..* -->
	 */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ProcessAnnotation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ProcessAnnotation> prosannList;
    
    /**
     * <!-- 11.995	ASC -->
     * <!-- 0..255 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AssociatedContext")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<AssociatedContext> asscxtList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "ImageHashValue")
    private String imgHash;
    
    /**
     * <!-- 11.997 SOR -->
     * <!-- 0..255 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SourceRepresentation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SourceRepresentation> srcRepList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CBEFFImage")
    private CBEFFImage cbeffImage;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FingerImpressionImage")
    private FingerImpressionImage fingerImpressionImage;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "IrisImage")
    private IrisImage irisImage;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImage")
    private FrictionRidgeImage frictionRidgeImage;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PalmprintImage")
    private PalmprintImage palmprintImage;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PlantarImage")
    private PlantarImage plantarImage;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "TestImage")
    private TestImage testImage;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "FingerprintImage")
    private FingerImpressionImage fingerprintImage;

    /**
     * <!-- 15.021 SEG -->
     * <!-- 0..17 -->
     */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "FrictionRidgeImageSegmentPositionSquare")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<FREISegmentPositionSquare> freiSegPosSquareList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SubjectExistentialDetails")
    private SubjectExistentialDetails subed;    
}