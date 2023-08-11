package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/**
 * Documento = doc
 */
public class Documento implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "DocumentoPoS")
    private int docPo5;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "TipoPersonaDocto")
    private int tipoPerDoc;
}
