package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
/*
 * DD = DomainDefined
 */
public class DDDescriptiveDetailRenapo extends DDDescriptiveDetail implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "InformacionSistema")
    private InformacionSistema informacionSistema;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I, localName = "DatosRegistro")
    private DatosRegistro datosRegistro;
}