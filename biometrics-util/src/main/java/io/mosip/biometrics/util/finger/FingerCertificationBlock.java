package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FingerCertificationBlock extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerCertificationBlock.class);	

    private int certificationAuthorityID;
    private int certificationSchemeIdentifier;

    public FingerCertificationBlock ()
    {
    	setCertificationAuthorityID (FingerCertificationAuthorityID.UNSPECIFIED);
    	setCertificationSchemeIdentifier (FingerCertificationSchemeIdentifier.UNSPECIFIED);
    }
    
    public FingerCertificationBlock (int certificationAuthorityID, int certificationSchemeIdentifier)
    {
    	setCertificationAuthorityID (certificationAuthorityID);
    	setCertificationSchemeIdentifier (certificationSchemeIdentifier);
    }

    public FingerCertificationBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    public FingerCertificationBlock (DataInputStream inputStream, boolean onlyImageInformation) throws IOException
	{
    	readObject(inputStream, onlyImageInformation);
	}
    
	@Override
    protected void readObject(DataInputStream inputStream) throws IOException {     	
		int certificationAuthorityID = inputStream.readUnsignedShort();
		try
		{		
        	setCertificationAuthorityID (certificationAuthorityID);
		}
		catch(Exception ex)
		{
			LOGGER.error("setCertificationAuthorityID :: Not Defined :: certificationAuthorityID :: " + Integer.toHexString (certificationAuthorityID));
		}
		
		int certificationSchemeIdentifier = inputStream.readUnsignedByte();
		try
		{		
    		setCertificationSchemeIdentifier (certificationSchemeIdentifier);
		}
		catch(Exception ex)
		{
			LOGGER.error("setCertificationSchemeIdentifier :: Not Defined :: certificationSchemeIdentifier :: " + Integer.toHexString (certificationSchemeIdentifier));
		}
    }
	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// SKIP	
		inputStream.skip(3);
	}
    
    /*  ((255 * 3)) (Table 2 Finger image representation header record  ISO/IEC 19794-4-2011) */
	@Override
    public long getRecordLength ()
    {
        return (2 + 1); 
    }

	@Override
    public void writeObject (DataOutputStream outputStream) throws IOException
    {
		outputStream.writeShort (getCertificationAuthorityID());     /* + 2 = 2 */
		outputStream.writeByte (getCertificationSchemeIdentifier()); /* + 1 = 3 */
        outputStream.flush ();
    }
    	
	public int getCertificationAuthorityID() {
		return certificationAuthorityID;
	}

	public void setCertificationAuthorityID(int certificationAuthorityID) {
		this.certificationAuthorityID = certificationAuthorityID;
	}

	public int getCertificationSchemeIdentifier() {
		return certificationSchemeIdentifier;
	}

	public void setCertificationSchemeIdentifier(int certificationSchemeIdentifier) {
		this.certificationSchemeIdentifier = certificationSchemeIdentifier;
	}

	@Override
	public String toString() {
		return "\nFingerCertificationBlock [RecordLength=" + getRecordLength () 
				+ ", certificationAuthorityID=" + Integer.toHexString (certificationAuthorityID)
				+ ", certificationSchemeIdentifier=" + Integer.toHexString (certificationSchemeIdentifier) + "]\n";
	}

}
