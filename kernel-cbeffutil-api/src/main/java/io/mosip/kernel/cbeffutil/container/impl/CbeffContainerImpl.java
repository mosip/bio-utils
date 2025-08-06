package io.mosip.kernel.cbeffutil.container.impl;

import java.util.ArrayList;
import java.util.List;

import io.mosip.kernel.biometrics.commons.CbeffValidator;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BIRInfo;
import io.mosip.kernel.biometrics.entities.BIRInfo.BIRInfoBuilder;
import io.mosip.kernel.cbeffutil.container.CbeffContainerI;
import io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator;

/**
 * Implementation class for creating and updating Biometric Information Record
 * (BIR) using CBEFF format.
 * 
 * <p>
 * This class provides methods to create a new BIR type, update an existing BIR
 * type, and validate CBEFF XML data against an XSD schema.
 * </p>
 * 
 * <p>
 * Authors: Ramadurai Pandian
 * </p>
 */
public class CbeffContainerImpl extends CbeffContainerI<BIR, BIR> {

	/** Shared reusable BIRInfo to avoid recreating metadata every time. */
	private static final BIRInfo DEFAULT_BIR_INFO = new BIRInfo(new BIRInfoBuilder().withIntegrity(false));


	/**
	 * Initializes the BIR instance with the provided list of BIR data.
	 * 
	 * @param birList List of BIR data to create the BIR type.
	 * @return BIR instance with initialized data.
	 */
	@Override
	public BIR createBIRType(List<BIR> birList) {
		BIR bir = new BIR();
		bir.setBirInfo(DEFAULT_BIR_INFO);
		bir.setBirs(birList != null ? birList : new ArrayList<>());
		return bir;
	}

	/**
	 * Updates an existing BIR instance with the provided list of BIR data and CBEFF
	 * XML bytes.
	 * 
	 * @param birList   List of BIR data to update the BIR type.
	 * @param fileBytes CBEFF XML data as bytes.
	 * @return Updated BIR instance.
	 * @throws Exception If an error occurs during the update process.
	 */
	@Override
	public BIR updateBIRType(List<BIR> birList, byte[] fileBytes) throws Exception {
		BIR biometricRecord = CbeffValidator.getBIRFromXML(fileBytes);
		for (BIR birInfo : birList) {
			biometricRecord.getBirs().add(birInfo);
		}
		return biometricRecord;
	}

	/**
	 * Validates the provided CBEFF XML data against the specified XSD schema.
	 * 
	 * @param xmlBytes Byte array of CBEFF XML data to validate.
	 * @param xsdBytes Byte array of XSD schema data for validation.
	 * @return {@code true} if the XML data is valid according to the XSD schema,
	 *         {@code false} otherwise.
	 * @throws Exception If an error occurs during the validation process.
	 */
	@Override
	public boolean validateXML(byte[] xmlBytes, byte[] xsdBytes) throws Exception {
		return CbeffXSDValidator.validateXML(xsdBytes, xmlBytes);
	}
}