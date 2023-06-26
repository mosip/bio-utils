package io.mosip.biometrics.util.nist.parser.v2011.dto;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import lombok.Data;

@Data
public class DatosRegistro implements Serializable {
    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "NuTramite")
    private String nuTramite;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "TipoSolicitante")
    private String tipoSolicitante;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "TipoRegistro")
    private String tipoRegistro;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "TipoDocumento")
    private Integer tipoDocumento;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "CURP")
    private String curp;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "InformacionDeLosPadres")
    private InformacionDeLosPadres informacionDeLosPadres;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "CodigoVAN")
    private String codigoVAN;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "NumeroIdentificadorER")
    private String numeroIdentificadorER;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "SolDoctoIdencion")
    private String solDoctoIdencion;

    @JacksonXmlProperty(namespace = XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO, localName = "AceptoCDMSolicitante")
    private String aceptoCDMSolicitante;
}