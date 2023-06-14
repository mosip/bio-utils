package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.LaboratoryAccreditationLevelCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.LaboratoryAccreditationScopeCodes;
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class DNALabAccreditation implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryAccreditationLevelCode")
    private LaboratoryAccreditationLevelCodes dnaLabAccredLevelCode;
    
    /*
    <!-- ACC -->
    <!--0..4-->
    */
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM, localName = "DNALaboratoryAccreditationScopeCode")
    @JacksonXmlElementWrapper(useWrapping=false)
    private List<LaboratoryAccreditationScopeCodes> dnaLabAccredScopeCodeList;
}