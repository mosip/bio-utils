package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralHeader extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralHeader.class);	

    private IrisFormatIdentifier formatIdentifier;
    private IrisVersionNumber versionNumber;
    private int totalRepresentationLength;
    private int noOfRepresentations;
    private IrisCertificationFlag certificationFlag;
    private int noOfEyesPresent;

    public GeneralHeader (int totalRepresentationLength, int noOfRepresentations, int noOfEyesPresent)
    {
    	setFormatIdentifier (IrisFormatIdentifier.FORMAT_IIR);
    	setVersionNumber (IrisVersionNumber.VERSION_020);
    	setTotalRepresentationLength (totalRepresentationLength);
    	setNoOfRepresentations (noOfRepresentations);
    	setCertificationFlag (IrisCertificationFlag.UNSPECIFIED);
    	setNoOfEyesPresent (noOfEyesPresent);
    }
    
    public GeneralHeader 
    	(
			IrisFormatIdentifier formatIdentifier, IrisVersionNumber versionNumber, 
			int totalRepresentationLength, int noOfRepresentations, 
			IrisCertificationFlag certificationFlag, int noOfEyesPresent
		)
    {
    	setFormatIdentifier (formatIdentifier);
    	setVersionNumber (versionNumber);
    	setTotalRepresentationLength (totalRepresentationLength);
    	setNoOfRepresentations (noOfRepresentations);
    	setCertificationFlag (certificationFlag);
    	setNoOfEyesPresent (noOfEyesPresent);
    }

    public GeneralHeader (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {    
    	setFormatIdentifier (IrisFormatIdentifier.fromValue((int)(inputStream.readInt()& 0xFFFFFFFFL)));
    	setVersionNumber (IrisVersionNumber.fromValue((int)(inputStream.readInt()& 0xFFFFFFFFL)));
    	setTotalRepresentationLength ((int)((inputStream.readInt() & 0xFFFFFFFFL) - getRecordLength ()));
    	setNoOfRepresentations (inputStream.readUnsignedShort());
    	setCertificationFlag (IrisCertificationFlag.fromValue(inputStream.readUnsignedByte()));
    	setNoOfEyesPresent (inputStream.readUnsignedByte());
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
        outputStream.writeByte (getNoOfEyesPresent());                       			/* + 1 = 16 */
        outputStream.flush ();
    }
    
	public IrisFormatIdentifier getFormatIdentifier() {
		return formatIdentifier;
	}
	public void setFormatIdentifier(IrisFormatIdentifier formatIdentifier) {
		this.formatIdentifier = formatIdentifier;
	}
	public IrisVersionNumber getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(IrisVersionNumber versionNumber) {
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
	public IrisCertificationFlag getCertificationFlag() {
		return certificationFlag;
	}
	public void setCertificationFlag(IrisCertificationFlag certificationFlag) {
		this.certificationFlag = certificationFlag;
	}
	public int getNoOfEyesPresent() {
		return noOfEyesPresent;
	}
	public void setNoOfEyesPresent(int noOfEyesPresent) {
		this.noOfEyesPresent = noOfEyesPresent;
	}

	@Override
	public String toString() {
		return "\nIrisGeneralHeader [GeneralHeaderRecordLength=" + getRecordLength () + ", formatIdentifier=" + formatIdentifier + ", versionNumber=" + versionNumber
				+ ", totalRepresentationLength=" + totalRepresentationLength + ", noOfRepresentations=" + noOfRepresentations
				+ ", certificationFlag=" + certificationFlag + ", noOfEyesPresent=" + noOfEyesPresent + "]\n";
	}	
}
