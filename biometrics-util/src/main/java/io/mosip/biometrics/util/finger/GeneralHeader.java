package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralHeader extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralHeader.class);	

    private FingerFormatIdentifier formatIdentifier;
    private FingerVersionNumber versionNumber;
    private int totalRepresentationLength;
    private int noOfRepresentations;
    private FingerCertificationFlag certificationFlag;
    private int noOfFingerPresent;

    public GeneralHeader (int totalRepresentationLength, int noOfRepresentations, int noOfFingerPresent)
    {
    	setFormatIdentifier (FingerFormatIdentifier.FORMAT_FIR);
    	setVersionNumber (FingerVersionNumber.VERSION_020);
    	setTotalRepresentationLength (totalRepresentationLength);
    	setNoOfRepresentations (noOfRepresentations);
    	setCertificationFlag (FingerCertificationFlag.UNSPECIFIED);
    	setNoOfFingerPresent (noOfFingerPresent);
    }
    
    public GeneralHeader 
    	(
			FingerFormatIdentifier formatIdentifier, FingerVersionNumber versionNumber, 
			int totalRepresentationLength, int noOfRepresentations, 
			FingerCertificationFlag certificationFlag, int noOfFingerPresent
		)
    {
    	setFormatIdentifier (formatIdentifier);
    	setVersionNumber (versionNumber);
    	setTotalRepresentationLength (totalRepresentationLength);
    	setNoOfRepresentations (noOfRepresentations);
    	setCertificationFlag (certificationFlag);
    	setNoOfFingerPresent (noOfFingerPresent);
    }

    public GeneralHeader (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {    
    	setFormatIdentifier (FingerFormatIdentifier.fromValue((int)(inputStream.readInt()& 0xFFFFFFFFL)));
    	setVersionNumber (FingerVersionNumber.fromValue((int)(inputStream.readInt()& 0xFFFFFFFFL)));
    	setTotalRepresentationLength ((int)((inputStream.readInt() & 0xFFFFFFFFL) - getRecordLength ()));
    	setNoOfRepresentations (inputStream.readUnsignedShort());
    	setCertificationFlag (FingerCertificationFlag.fromValue(inputStream.readUnsignedByte()));
    	setNoOfFingerPresent (inputStream.readUnsignedByte());
    }
    
    public int getRecordLength ()
    {
        return 16; /* 4 + 4 + 4 + 2 + 1 + 1 (table 3 ISO/IEC 19794-6-2011) */
    }

    protected void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeInt (getFormatIdentifier().value());							/* 4 */
        outputStream.writeInt (getVersionNumber().value());                             /* + 4 = 8 */
        outputStream.writeInt ((int)(getRecordLength () + getTotalRepresentationLength ()));  	/* + 4 = 12 */
        outputStream.writeShort (getNoOfRepresentations() );                           	/* + 2 = 14 */
        outputStream.writeByte (getCertificationFlag().value());                        /* + 1 = 15 */
        outputStream.writeByte (getNoOfFingerPresent());                       			/* + 1 = 16 */
        outputStream.flush ();
    }
    
	public FingerFormatIdentifier getFormatIdentifier() {
		return formatIdentifier;
	}
	public void setFormatIdentifier(FingerFormatIdentifier formatIdentifier) {
		this.formatIdentifier = formatIdentifier;
	}
	public FingerVersionNumber getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(FingerVersionNumber versionNumber) {
		this.versionNumber = versionNumber;
	}
	public int getTotalRepresentationLength() {
		return totalRepresentationLength;
	}
	public void setTotalRepresentationLength(int totalRepresentationLength) {
		this.totalRepresentationLength = totalRepresentationLength;
	}
	public int getNoOfRepresentations() {
		return noOfRepresentations;
	}
	public void setNoOfRepresentations(int noOfRepresentations) {
		this.noOfRepresentations = noOfRepresentations;
	}
	public FingerCertificationFlag getCertificationFlag() {
		return certificationFlag;
	}
	public void setCertificationFlag(FingerCertificationFlag certificationFlag) {
		this.certificationFlag = certificationFlag;
	}
	public int getNoOfFingerPresent() {
		return noOfFingerPresent;
	}
	public void setNoOfFingerPresent(int noOfFingerPresent) {
		this.noOfFingerPresent = noOfFingerPresent;
	}

	@Override
	public String toString() {
		return "\nIrisGeneralHeader [GeneralHeaderRecordLength=" + getRecordLength () + ", formatIdentifier=" + formatIdentifier + ", versionNumber=" + versionNumber
				+ ", totalRepresentationLength=" + totalRepresentationLength + ", noOfRepresentations=" + noOfRepresentations
				+ ", certificationFlag=" + certificationFlag + ", noOfFingerPresent=" + noOfFingerPresent + "]\n";
	}	
}
