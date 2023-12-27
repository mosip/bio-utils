package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * FORINV =  ForensicInvestigatory
 * FOR = Forensic
 */
public class FORINVVoiceDetail implements Serializable {
    /**
    <!-- 11.032 SGEO -->
    <!--0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentGeographicalLocation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SegmentGeographicalLocation> segGeoLocList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AudioObjectDescriptorCode")
    private int audioDescCode;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CaptureOrganization")
    private Organization capOrg;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingSettingDetail")
    private RecordingSettingDetail recSettingDetail;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingContentDescriptor")
    private RecordingContentDescriptor recContentDesc;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AudioSourceRecorder")
    private AudioSourceRecorder audioSrcRec;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SourceAcquisition")
    private SourceAcquisition srcAcq;    
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordCreationDate")
    private NistDate recCreationDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CaptureDate")
    private NistDate capDate;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RecordingDuration")
    private RecordingDuration recDuration;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "PhysicalMediaObject")
    private PhysicalMediaObject phyMedia;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DigitalMediaContainer")
    private DigitalMediaContainer digMediaCont;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "CodecDetails")
    private CodecDetails codecDetails;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RedactionInformation")
    private Information redInfo;
    
    /**
    <!-- 11.022 RDD -->
    <!--0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "RedactionDiaryInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DiaryInformation> redDiaryInfoList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DiscontinuityInformation")
    private Information discontinuityInfo;
    
    /**
    <!-- 11.024 DSD -->
    <!--0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DiscontinuityDiaryInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<DiaryInformation> discontinuityDiaryInfoList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "VocalContentInformation")
    private Information vocalContInfo;
    
    /**
    <!-- 11.026 VCD -->
    <!--0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "VocalContentDiaryInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<VocalContentDiaryInfo> vocalContentDiaryInfoList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "OtherContentInformation")
    private Information otherContentInfo;
    
    /**
    <!-- 11.028 OCD -->
    <!--0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "OtherContentDiaryInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<ContentDiaryInfo> contentDiaryInfoList;
    
    /**
    <!-- 11.033 SQV -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "VocalSegmentQualityValue")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<VocalSegmentQuality> vocalSegQualityList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "VocalCollisionIdentifierList")
    private VocalCollisionIdentifierList vocalCollisionIdList;
    
    /**
    <!-- 11.035 PPY -->
    <!-- 0..9 -->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentProcessingPriorityInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SPPriorityInfo> segProcPriorityInfoList;
    
    /**
    <!-- 11.036 VSCT -->
    <!-- 0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentContentInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SegmentContentInfo> sciList;

    /**
    <!-- 11.037 SCC -->
    <!-- 0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentSpeakerCharacteristicsInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SSCharacteristicsInfo> ssCharInfoList;
    
    /**
    <!-- 11.037 SCC -->
    <!-- 0..600000-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "SegmentChannelInformation")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<SegmentChannelInfo> segChannelInfoList;
    
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "AudioCommentText")
    private String audioCom;
}