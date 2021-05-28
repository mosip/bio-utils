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

    private FingerCertificationAuthorityID certificationAuthorityID;
    private FingerCertificationSchemeIdentifier certificationSchemeIdentifier;

    public FingerCertificationBlock ()
    {
    	setCertificationAuthorityID (FingerCertificationAuthorityID.UNSPECIFIED);
    	setCertificationSchemeIdentifier (FingerCertificationSchemeIdentifier.UNSPECIFIED);
    }
    
    public FingerCertificationBlock (FingerCertificationAuthorityID certificationAuthorityID, FingerCertificationSchemeIdentifier certificationSchemeIdentifier)
    {
    	setCertificationAuthorityID (certificationAuthorityID);
    	setCertificationSchemeIdentifier (certificationSchemeIdentifier);
    }

    public FingerCertificationBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {     	
		int certificationAuthorityID = inputStream.readUnsignedShort();
		try
		{		
        	setCertificationAuthorityID (FingerCertificationAuthorityID.fromValue (certificationAuthorityID));
		}
		catch(Exception ex)
		{
			LOGGER.error("setCertificationAuthorityID :: Not Defined :: certificationAuthorityID :: " + Integer.toHexString (certificationAuthorityID));
		}
		
		int certificationSchemeIdentifier = inputStream.readUnsignedByte();
		try
		{		
    		setCertificationSchemeIdentifier (FingerCertificationSchemeIdentifier.fromValue (certificationSchemeIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setCertificationSchemeIdentifier :: Not Defined :: certificationSchemeIdentifier :: " + Integer.toHexString (certificationSchemeIdentifier));
		}
    }
    
    /*  ((255 * 3)) (Table 2 � Finger image representation header record  ISO/IEC 19794-4-2011) */
    public int getRecordLength ()
    {
        return (2 + 1); 
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
		outputStream.writeShort (getCertificationAuthorityID().value());     /* + 2 = 2 */
		outputStream.writeByte (getCertificationSchemeIdentifier().value()); /* + 1 = 3 */
        outputStream.flush ();
    }
    	
	public FingerCertificationAuthorityID getCertificationAuthorityID() {
		return certificationAuthorityID;
	}

	public void setCertificationAuthorityID(FingerCertificationAuthorityID certificationAuthorityID) {
		this.certificationAuthorityID = certificationAuthorityID;
	}

	public FingerCertificationSchemeIdentifier getCertificationSchemeIdentifier() {
		return certificationSchemeIdentifier;
	}

	public void setCertificationSchemeIdentifier(FingerCertificationSchemeIdentifier certificationSchemeIdentifier) {
		this.certificationSchemeIdentifier = certificationSchemeIdentifier;
	}

	@Override
	public String toString() {
		return "\nFingerCertificationBlock [RecordLength=" + getRecordLength () 
				+ ", certificationAuthorityID=" + certificationAuthorityID
				+ ", certificationSchemeIdentifier=" + certificationSchemeIdentifier + "]\n";
	}
}
