package io.mosip.kernel.cbeffutil.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.commons.CbeffValidator;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.spi.CbeffUtil;
import io.mosip.kernel.cbeffutil.container.impl.CbeffContainerImpl;
import jakarta.annotation.PostConstruct;

/**
 * Implementation class for handling Common Biometric Exchange Formats Framework
 * (CBEFF) operations.
 *
 * <p>
 * This class provides methods to create, update, validate, and retrieve
 * biometric data in CBEFF XML format. It initializes the XML schema (XSD) from
 * a specified configuration server location and performs operations using the
 * loaded XSD.
 * </p>
 *
 * <p>
 * This implementation uses {@link CbeffContainerImpl} for creating and updating
 * CBEFF data containers, and {@link CbeffValidator} for XML validation and
 * biometric data extraction operations.
 * </p>
 *
 * <p>
 * Authors: Ramadurai Pandian
 * </p>
 *
 * @since 1.0.0
 * @see BIR
 * @see CbeffUtil
 * @see CbeffContainerImpl
 * @see CbeffValidator
 */
@Component
public class CbeffImpl implements CbeffUtil {
	/** The URL of the configuration server for XSD storage. */
	@Value("${mosip.kernel.xsdstorage-uri}")
	private String configServerFileStorageURL;

	/** The filename of the XSD schema. */
	@Value("${mosip.kernel.xsdfile}")
	private String schemaName;

	/** The byte array representation of the XSD schema. */
	private byte[] xsd;

	/**
	 * Initializes the XSD schema by loading it from the configured server URL.
	 *
	 * @throws IOException        If an I/O error occurs while reading the XSD file.
	 * @throws URISyntaxException If the URI syntax is invalid.
	 */
	@PostConstruct
	public void loadXSD() throws IOException, URISyntaxException {
		try (InputStream xsdBytes = new URI(configServerFileStorageURL + schemaName).toURL().openStream()) {
			xsd = IOUtils.toByteArray(xsdBytes);
		}
	}

	/**
	 * Creates a CBEFF XML representation from the provided list of Biometric
	 * Information Records (BIRs).
	 *
	 * @param birList List of BIRs to create CBEFF XML.
	 * @return Byte array of XML data representing the CBEFF format.
	 * @throws Exception If there is an error during XML creation.
	 */
	@Override
	public byte[] createXML(List<BIR> birList) throws Exception {
		CbeffContainerImpl cbeffContainer = new CbeffContainerImpl();
		BIR bir = cbeffContainer.createBIRType(birList);
		return CbeffValidator.createXMLBytes(bir, xsd);
	}

	/**
	 * Creates a CBEFF XML representation from the provided list of Biometric
	 * Information Records (BIRs) using the specified XSD schema.
	 *
	 * @param birList List of BIRs to create CBEFF XML.
	 * @param xsd     Byte array of XSD data.
	 * @return Byte array of XML data representing the CBEFF format.
	 * @throws Exception If there is an error during XML creation.
	 */
	@Override
	public byte[] createXML(List<BIR> birList, byte[] xsd) throws Exception {
		CbeffContainerImpl cbeffContainer = new CbeffContainerImpl();
		BIR bir = cbeffContainer.createBIRType(birList);
		return CbeffValidator.createXMLBytes(bir, xsd);
	}

	/**
	 * Updates an existing CBEFF XML representation with the provided list of
	 * Biometric Information Records (BIRs).
	 *
	 * @param birList   List of BIRs to update in the existing XML.
	 * @param fileBytes Byte array of the existing XML file.
	 * @return Byte array of XML data representing the updated CBEFF format.
	 * @throws Exception If there is an error during XML update.
	 */
	@Override
	public byte[] updateXML(List<BIR> birList, byte[] fileBytes) throws Exception {
		CbeffContainerImpl cbeffContainer = new CbeffContainerImpl();
		BIR bir = cbeffContainer.updateBIRType(birList, fileBytes);
		return CbeffValidator.createXMLBytes(bir, xsd);
	}

	/**
	 * Validates the provided XML data against the specified XSD schema.
	 *
	 * @param xmlBytes Byte array of XML data to validate.
	 * @param xsdBytes Byte array of XSD data for validation.
	 * @return {@code true} if the XML is valid according to the XSD schema,
	 *         {@code false} otherwise.
	 * @throws Exception If there is an error during XML validation.
	 */
	@Override
	public boolean validateXML(byte[] xmlBytes, byte[] xsdBytes) throws Exception {
		CbeffContainerImpl cbeffContainer = new CbeffContainerImpl();
		return cbeffContainer.validateXML(xmlBytes, xsdBytes);
	}

	/*
	 * (non-Javadoc) Validates the provided XML data against the internally loaded
	 * XSD schema.
	 * 
	 * @param xmlBytes Byte array of XML data to validate.
	 * 
	 * @return {@code true} if the XML is valid according to the loaded XSD schema,
	 * {@code false} otherwise.
	 * 
	 * @throws Exception If there is an error during XML validation.
	 * 
	 * @see io.mosip.kernel.core.cbeffutil.spi.CbeffUtil#validateXML(byte[])
	 */
	@Override
	public boolean validateXML(byte[] xmlBytes) throws Exception {
		return validateXML(xmlBytes, xsd);
	}

	/**
	 * Retrieves Biometric Data Block (BDB) information based on the specified type
	 * and subtype from the provided XML file bytes.
	 *
	 * @param fileBytes Byte array of the XML file containing biometric data.
	 * @param type      Type of the BDB to retrieve.
	 * @param subType   Subtype of the BDB to retrieve.
	 * @return Map containing key-value pairs of BDB information.
	 * @throws Exception If there is an error while retrieving BDB information.
	 */
	@Override
	public Map<String, String> getBDBBasedOnType(byte[] fileBytes, String type, String subType) throws Exception {
		BIR bir = CbeffValidator.getBIRFromXML(fileBytes);
		return CbeffValidator.getBDBBasedOnTypeAndSubType(bir, type, subType);
	}

	/**
	 * Retrieves Biometric Information Records (BIRs) from the provided XML byte
	 * array.
	 *
	 * @param xmlBytes Byte array of the XML content.
	 * @return List of BIRs extracted from the XML.
	 * @throws Exception If there is an error while extracting BIR data from XML.
	 */
	@Override
	public List<BIR> getBIRDataFromXML(byte[] xmlBytes) throws Exception {
		BIR bir = CbeffValidator.getBIRFromXML(xmlBytes);
		return bir.getBirs();
	}

	/**
	 * Retrieves all Biometric Data Block (BDB) data based on the specified type and
	 * subtype from the provided XML byte array.
	 *
	 * @param xmlBytes Byte array of the XML content.
	 * @param type     Type of the BDBs to retrieve.
	 * @param subType  Subtype of the BDBs to retrieve.
	 * @return Map containing all BDB data as key-value pairs.
	 * @throws Exception If there is an error while retrieving BDB data from XML.
	 */
	@Override
	public Map<String, String> getAllBDBData(byte[] xmlBytes, String type, String subType) throws Exception {
		BIR bir = CbeffValidator.getBIRFromXML(xmlBytes);
		return CbeffValidator.getAllBDBData(bir, type, subType);
	}

	/**
	 * (non-Javadoc) Retrieves Biometric Information Records (BIRs) of a specific
	 * type from the provided XML byte array.
	 *
	 * @param xmlBytes Byte array of the XML content.
	 * @param type     Type of the BIRs to retrieve.
	 * @return List of BIRs extracted from the XML based on the specified type.
	 * @throws Exception If there is an error while extracting BIR data from XML.
	 *                   io.mosip.kernel.core.cbeffutil.spi.CbeffUtil#getBIRDataFromXMLType(byte[],
	 *                   java.lang.String)
	 */

	@Override
	public List<BIR> getBIRDataFromXMLType(byte[] xmlBytes, String type) throws Exception {
		return CbeffValidator.getBIRDataFromXMLType(xmlBytes, type);
	}
}