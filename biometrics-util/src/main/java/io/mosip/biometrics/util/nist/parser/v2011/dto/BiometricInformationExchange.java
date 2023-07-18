package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "itl:NISTBiometricInformationExchangePackage")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiometricInformationExchange implements Serializable {    
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_S, localName = XmlnsNameSpaceConstant.NAMESPACE_S)
    private String xmlnsS = XmlnsNameSpaceConstant.NAMESPACE_URL_S;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_I, localName = XmlnsNameSpaceConstant.NAMESPACE_I)
    private String xmlnsI = XmlnsNameSpaceConstant.NAMESPACE_URL_I;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = XmlnsNameSpaceConstant.NAMESPACE_ITL)
    private String xmlnsITL = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = XmlnsNameSpaceConstant.NAMESPACE_BIOM)
    private String xmlnsBiom = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NC, localName = XmlnsNameSpaceConstant.NAMESPACE_NC)
    private String xmlnsNc = XmlnsNameSpaceConstant.NAMESPACE_URL_NC;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_NIEM_XSD, localName = XmlnsNameSpaceConstant.NAMESPACE_NIEM_XSD)
    private String xmlnsNiemXSD = XmlnsNameSpaceConstant.NAMESPACE_URL_NIEM_XSD;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ISO_3166, localName = XmlnsNameSpaceConstant.NAMESPACE_ISO_3166)
    private String xmlnsISO3166 = XmlnsNameSpaceConstant.NAMESPACE_URL_ISO_3166;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ISO_639_3, localName = XmlnsNameSpaceConstant.NAMESPACE_ISO_639_3)
    private String xmlnsISO6393 = XmlnsNameSpaceConstant.NAMESPACE_URL_ISO_639_3;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_GENC, localName = XmlnsNameSpaceConstant.NAMESPACE_GENC)
    private String xmlnsGenc = XmlnsNameSpaceConstant.NAMESPACE_URL_GENC;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_FBI, localName = XmlnsNameSpaceConstant.NAMESPACE_FBI)
    private String xmlnsFbi = XmlnsNameSpaceConstant.NAMESPACE_URL_FBI;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = XmlnsNameSpaceConstant.NAMESPACE_INT_I)
    private String xmlnsInti = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = XmlnsNameSpaceConstant.NAMESPACE_RENAPO)
    private String xmlnsRenapo = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO;
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_XSI, localName = XmlnsNameSpaceConstant.NAMESPACE_XSI)
    private String xmlnsXsi = XmlnsNameSpaceConstant.NAMESPACE_URL_XSI;    
    @JacksonXmlProperty(isAttribute = true, namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_XSI, localName = XmlnsNameSpaceConstant.NAMESPACE_XSI_SCHEMA_LOCATION)
    private String xsiSchemaLocation = XmlnsNameSpaceConstant.NAMESPACE_URL_XSI_SCHEMA_LOCATION;
	
    /**
    <!--******************************************************************-->
    <!--Type-1 Record (Header Record)                                     -->
    <!--******************************************************************-->
    <!-- 1..1 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageInformationRecord")
    private PackageInformationRecord informationRecord;

    /**
    <!--******************************************************************-->
    <!--Type-2 Record (User Defined Descriptive Text Record)              -->
    <!--******************************************************************-->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageDescriptiveTextRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageDescriptiveTextRecord> descriptiveTextRecordList;

    /**
    <!--******************************************************************-->
    <!--Type-4 Record (High Resolution Grayscale Fingerprint Image Record)-->
    <!--******************************************************************-->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageHighResolutionGrayscaleImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageHighResolutionGrayscaleImageRecord> highResolutionGrayscaleImageRecordList;

    /**
    <!--******************************************************************-->
    <!--Type-7 Record (User Defined Image Record)						  -->
    <!--******************************************************************-->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageUserDefinedImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<BiometricDataRecord> userDefinedImageRecordList;
    
    /*
    <!--******************************************************************-->
    <!--Type-8 Record (Signature Record)								  -->
    <!--******************************************************************-->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageSignatureImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageSignatureImageRecord> signatureImageRecordList;
    
    /**
    <!--******************************************************************-->
    <!--Type-9 Record (Legacy Fingerprint Minutiae Record)			      -->
    <!--******************************************************************-->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageMinutiaeRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageMinutiaeRecord> minutiaeRecordList;
    
    /**
    <!-- ***************************************************************************************** -->
    <!--    RECORD TYPE 10     Facial Image Record                                                 -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageFacialAndSMTImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageNonPhotographicImageryRecord> facialAndSMTImageRecordList;
    
    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 11     Voice Record                                                           -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageVoiceDataRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageVoiceDataRecord> voiceDataRecordList;
    
    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 12     Dental Record                                                          -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageForensicDentalDataRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageForensicDentalDataRecord> forensicDentalDataRecordList;
    
    /**
 	<!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 13     Latent Friction Ridge Record                                           -->
    <!-- ***************************************************************************************** -->
 	<!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageLatentImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> latentImageRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 14     Friction Ridge Record                                                  -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageFingerprintImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> fingerprintImageRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 15     Palm Record                                                            -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackagePalmprintImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> palmprintImageRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 16     Test Record                                                            -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageUserDefinedTestingImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> userDefinedTestingImageRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 17     Iris Record                                                            -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageIrisImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> irisImageRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 18      DNA Record                                                            -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageDNARecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageDNARecord> dNARecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 19      Plantar Record                                                        -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackagePlantarImageRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> plantarImageRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 20      Source Representation Record with Image                               -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageSourceRepresentationRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageSourceRepresentationRecord> sourceRepresentationRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 21      Associated Context Record with Video                                  -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageAssociatedContextRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageAssociatedContextRecord> associatedContextRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 22      Non Photographic Imagery Record                                       -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageNonPhotographicImageryRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageNonPhotographicImageryRecord> nonPhotographicImageryRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 98      Information Assurance Record                                          -->
    <!-- ***************************************************************************************** -->
    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageInformationAssuranceRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageInformationAssuranceRecord> informationAssuranceRecordList;

    /**
    <!-- ***************************************************************************************** -->
    <!-- RECORD TYPE 99      CBEFF Biometric Data Record                                           -->
    <!-- ***************************************************************************************** -->

    <!-- 0..* -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL, localName = "PackageCBEFFBiometricDataRecord")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<PackageCBEFFBiometricDataRecord> cbeffBiometricDataRecordList;
}