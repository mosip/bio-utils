package io.mosip.kernel.cbeffutil.container;

import java.util.List;

/**
 * Interface for creating and updating Common Biometric Exchange Formats
 * Framework (CBEFF) data containers.
 * 
 * <p>
 * This interface defines methods for creating and updating Biometric
 * Information Record (BIR) types and validating XML against XSD schema.
 * </p>
 * 
 * <p>
 * Implementations of this interface should provide concrete implementations for
 * creating and updating BIR types, as well as validating XML data against an
 * XSD schema.
 * </p>
 * 
 * <p>
 * Authors: Ramadurai Pandian
 * </p>
 * 
 * @param <T> Type of data used for creating or updating BIR types.
 * @param <U> Type representing the result of creating or updating BIR types.
 * 
 * @since 1.0.0
 */

@SuppressWarnings({ "java:S112" })
public abstract class CbeffContainerI<T, U> {
	/**
	 * Creates a new Biometric Information Record (BIR) type based on the provided
	 * list of data.
	 *
	 * @param bir List of data to create the BIR type.
	 * @return The created BIR type.
	 * @throws Exception If an error occurs during creation.
	 */
	public abstract U createBIRType(List<T> bir) throws Exception;

	/**
	 * Updates an existing Biometric Information Record (BIR) type using the
	 * provided list of data and the existing file bytes.
	 *
	 * @param bir       List of data to update the BIR type.
	 * @param fileBytes Byte array of the existing file.
	 * @return The updated BIR type.
	 * @throws Exception If an error occurs during update.
	 */
	public abstract U updateBIRType(List<T> bir, byte[] fileBytes) throws Exception;

	/**
	 * Validates the XML data against the XSD schema represented by the provided
	 * byte arrays.
	 *
	 * @param fileBytes Byte array of the XML data to validate.
	 * @param xsdBytes  Byte array of the XSD schema data for validation.
	 * @return {@code true} if the XML is valid according to the XSD schema,
	 *         {@code false} otherwise.
	 * @throws Exception If an error occurs during validation.
	 */
	public abstract boolean validateXML(byte[] fileBytes, byte[] xsdBytes) throws Exception;
}