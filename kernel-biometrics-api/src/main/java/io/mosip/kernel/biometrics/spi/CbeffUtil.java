package io.mosip.kernel.biometrics.spi;

import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.entities.BIR;

/**
 * Interface for Common Biometric Exchange Formats Framework (CBEFF) utility
 * operations.
 * 
 * <p>
 * This interface defines methods to create, update, validate, and retrieve
 * biometric data in XML format, based on CBEFF standards. It also provides
 * methods to retrieve specific Biometric Data Blocks (BDB) based on type and
 * subtype, and handle XML schema validation.
 * </p>
 * 
 * <p>
 * Implementations of this interface should provide functionality to handle
 * biometric data in compliance with CBEFF specifications, ensuring
 * interoperability and standardization in biometric data exchange and
 * processing.
 * </p>
 * 
 * <p>
 * This interface suppresses warnings related to hardcoding string literals for
 * type safety and clarity.
 * </p>
 * 
 * @see BIR
 */

@SuppressWarnings({ "java:S112" })
public interface CbeffUtil {

	/**
	 * Creates an XML representation from the provided list of Biometric Information
	 * Records (BIRs).
	 * 
	 * @param cbeffPack The list of BIRs to convert to XML.
	 * @return The byte array representation of the generated XML.
	 * @throws Exception If there is an error during XML creation.
	 */
	public byte[] createXML(List<BIR> cbeffPack) throws Exception;

	/**
	 * Updates an existing XML file with new or modified Biometric Information
	 * Records (BIRs).
	 * 
	 * @param cbeffPackList The list of BIRs to update in XML format.
	 * @param fileBytes     The byte array of the existing XML file to update.
	 * @return The byte array representation of the updated XML file.
	 * @throws Exception If there is an error during XML update.
	 */
	public byte[] updateXML(List<BIR> cbeffPackList, byte[] fileBytes) throws Exception;

	/**
	 * Validates the XML content against the provided XML Schema Definition (XSD)
	 * file.
	 * 
	 * @param xmlBytes The byte array representation of the XML content to validate.
	 * @param xsdBytes The byte array representation of the XSD file for validation.
	 * @return {@code true} if the XML is valid according to the XSD, {@code false}
	 *         otherwise.
	 * @throws Exception If there is an error during XML validation.
	 */
	public boolean validateXML(byte[] xmlBytes, byte[] xsdBytes) throws Exception;

	/**
	 * Validates the XML content against an internally defined schema or standard.
	 * 
	 * @param xmlBytes The byte array representation of the XML content to validate.
	 * @return {@code true} if the XML is valid, {@code false} otherwise.
	 * @throws Exception If there is an error during XML validation.
	 */
	public boolean validateXML(byte[] xmlBytes) throws Exception;

	/**
	 * Retrieves Biometric Data Block (BDB) information based on type and subtype
	 * from the provided file bytes.
	 * 
	 * @param fileBytes The byte array of the file containing the biometric data.
	 * @param type      The type of the BDB to retrieve.
	 * @param subType   The subtype of the BDB to retrieve.
	 * @return A map containing key-value pairs of BDB information.
	 * @throws Exception If there is an error while retrieving BDB information.
	 */
	public Map<String, String> getBDBBasedOnType(byte[] fileBytes, String type, String subType) throws Exception;

	/**
	 * Retrieves Biometric Information Records (BIRs) from the XML content in the
	 * provided byte array.
	 * 
	 * @param xmlBytes The byte array representation of the XML content.
	 * @return The list of BIRs extracted from the XML.
	 * @throws Exception If there is an error while extracting BIR data from XML.
	 */
	public List<BIR> getBIRDataFromXML(byte[] xmlBytes) throws Exception;

	/**
	 * Retrieves all Biometric Data Block (BDB) data from the XML content based on
	 * type and subtype.
	 * 
	 * @param xmlBytes The byte array representation of the XML content.
	 * @param type     The type of the BDBs to retrieve.
	 * @param subType  The subtype of the BDBs to retrieve.
	 * @return A map containing all BDB data as key-value pairs.
	 * @throws Exception If there is an error while retrieving BDB data from XML.
	 */
	public Map<String, String> getAllBDBData(byte[] xmlBytes, String type, String subType) throws Exception;

	/**
	 * Creates an XML representation from the provided list of Biometric Information
	 * Records (BIRs) using a specified XSD schema.
	 * 
	 * @param birList The list of BIRs to convert to XML.
	 * @param xsd     The byte array representation of the XSD schema for XML
	 *                validation.
	 * @return The byte array representation of the generated XML.
	 * @throws Exception If there is an error during XML creation.
	 */
	public byte[] createXML(List<BIR> birList, byte[] xsd) throws Exception;

	/**
	 * Retrieves Biometric Information Records (BIRs) from the XML content in the
	 * provided byte array based on a specific type.
	 * 
	 * @param xmlBytes The byte array representation of the XML content.
	 * @param type     The type of the BIRs to retrieve.
	 * @return The list of BIRs extracted from the XML based on the specified type.
	 * @throws Exception If there is an error while extracting BIR data from XML.
	 */
	public List<BIR> getBIRDataFromXMLType(byte[] xmlBytes, String type) throws Exception;
}