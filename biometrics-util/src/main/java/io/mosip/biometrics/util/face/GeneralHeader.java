package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralHeader extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralHeader.class);	

    private FaceFormatIdentifier formatIdentifier;
    private FaceVersionNumber versionNumber;
    private int totalRepresentationLength;
    private int noOfRepresentations;
    private FaceCertificationFlag certificationFlag;
    private TemporalSequenceFlags temporalSemantics;

    public GeneralHeader (int totalRepresentationLength, int noOfRepresentations)
    {
    	setFormatIdentifier (FaceFormatIdentifier.FORMAT_FAC);
    	setVersionNumber (FaceVersionNumber.VERSION_030);
    	setTotalRepresentationLength (totalRepresentationLength);
    	setNoOfRepresentations (noOfRepresentations);
    	setCertificationFlag (FaceCertificationFlag.UNSPECIFIED);
    	setTemporalSemantics (TemporalSequenceFlags.ONE_REPRESENTATION);
    }
    
    public GeneralHeader 
    	(
			FaceFormatIdentifier formatIdentifier, FaceVersionNumber versionNumber, 
			int totalRepresentationLength, int noOfRepresentations, 
			FaceCertificationFlag certificationFlag, TemporalSequenceFlags temporalSemantics
		)
    {
    	setFormatIdentifier (formatIdentifier);
    	setVersionNumber (versionNumber);
    	setTotalRepresentationLength (totalRepresentationLength);
    	setNoOfRepresentations (noOfRepresentations);
    	setCertificationFlag (certificationFlag);
    	setTemporalSemantics (temporalSemantics);
    }

    public GeneralHeader (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {    
    	setFormatIdentifier (FaceFormatIdentifier.fromValue((int)(inputStream.readInt()& 0xFFFFFFFFL)));
    	setVersionNumber (FaceVersionNumber.fromValue((int)(inputStream.readInt()& 0xFFFFFFFFL)));
    	setTotalRepresentationLength ((int)((inputStream.readInt() & 0xFFFFFFFFL) - getRecordLength ()));
    	setNoOfRepresentations (inputStream.readUnsignedShort());
    	setCertificationFlag (FaceCertificationFlag.fromValue(inputStream.readUnsignedByte()));
    	setTemporalSemantics (TemporalSequenceFlags.fromValue(inputStream.readUnsignedShort()));
    }
    
    public int getRecordLength ()
    {
        return 17; /* 4 + 4 + 4 + 2 + 1 + 2 (table 2 ISO/IEC 19794-5) */
    }

    protected void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeInt (getFormatIdentifier().value());							/* 4 */
        outputStream.writeInt (getVersionNumber().value());                             /* + 4 = 8 */
        outputStream.writeInt ((int)(getRecordLength () + getTotalRepresentationLength ()));  	/* + 4 = 12 */
        outputStream.writeShort (getNoOfRepresentations() );                           	/* + 2 = 14 */
        outputStream.writeByte (getCertificationFlag().value());                        /* + 1 = 15 */
        outputStream.writeShort (getTemporalSemantics().value());                       /* + 2 = 17 */
        outputStream.flush ();
    }
    
	public FaceFormatIdentifier getFormatIdentifier() {
		return formatIdentifier;
	}
	public void setFormatIdentifier(FaceFormatIdentifier formatIdentifier) {
		this.formatIdentifier = formatIdentifier;
	}
	public FaceVersionNumber getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(FaceVersionNumber versionNumber) {
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
	public FaceCertificationFlag getCertificationFlag() {
		return certificationFlag;
	}
	public void setCertificationFlag(FaceCertificationFlag certificationFlag) {
		this.certificationFlag = certificationFlag;
	}
	public TemporalSequenceFlags getTemporalSemantics() {
		return temporalSemantics;
	}
	public void setTemporalSemantics(TemporalSequenceFlags temporalSemantics) {
		this.temporalSemantics = temporalSemantics;
	}

	@Override
	public String toString() {
		return "\nFaceGeneralHeader [GeneralHeaderRecordLength=" + getRecordLength () + ", formatIdentifier=" + formatIdentifier + ", versionNumber=" + versionNumber
				+ ", totalRepresentationLength=" + totalRepresentationLength + ", noOfRepresentations=" + noOfRepresentations
				+ ", certificationFlag=" + certificationFlag + ", temporalSemantics=" + temporalSemantics + "]\n";
	}	
}
