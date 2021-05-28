package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FingerQualityBlock extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerQualityBlock.class);	

    private int qualityScore;
    private FingerQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier;
    private FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier;

    public FingerQualityBlock (int qualityScore)
    {
    	setQualityScore (qualityScore);
    	setQualityAlgorithmVendorIdentifier (FingerQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER);
    	setQualityAlgorithmIdentifier (FingerQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER);
    }
    
    public FingerQualityBlock (int qualityScore, FingerQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier, FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier)
    {
    	setQualityScore (qualityScore);
    	setQualityAlgorithmVendorIdentifier (qualityAlgorithmVendorIdentifier);
    	setQualityAlgorithmIdentifier (qualityAlgorithmIdentifier);
    }

    public FingerQualityBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException { 
    	setQualityScore (inputStream.readUnsignedByte());

    	int qualityAlgorithmVendorIdentifier = inputStream.readUnsignedShort();
		try
		{		
	    	setQualityAlgorithmVendorIdentifier (FingerQualityAlgorithmVendorIdentifier.fromValue(qualityAlgorithmVendorIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setQualityAlgorithmVendorIdentifier :: Not Defined :: qualityAlgorithmVendorIdentifier :: " + Integer.toHexString (qualityAlgorithmVendorIdentifier));
		}

    	int qualityAlgorithmIdentifier = inputStream.readUnsignedShort();
		try
		{		
	    	setQualityAlgorithmIdentifier (FingerQualityAlgorithmIdentifier.fromValue(qualityAlgorithmIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setQualityAlgorithmIdentifier :: Not Defined :: qualityAlgorithmIdentifier :: " + Integer.toHexString (qualityAlgorithmIdentifier));
		}
    }
    
    public int getRecordLength ()
    {
        return 5; /* 1 + 2 + 2 (table 2 ISO/IEC 19794-4-2011) */
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeByte (getQualityScore ());                                              /* 1 */
        outputStream.writeShort (getQualityAlgorithmVendorIdentifier().value());                  /* + 2 = 3 */
        outputStream.writeShort (getQualityAlgorithmIdentifier().value());                        /* + 2 = 5 */
        outputStream.flush ();
    }
    
	public int getQualityScore() {
		return qualityScore;
	}
	public void setQualityScore(int qualityScore) {
		this.qualityScore = qualityScore;
	}
	public FingerQualityAlgorithmVendorIdentifier getQualityAlgorithmVendorIdentifier() {
		return qualityAlgorithmVendorIdentifier;
	}
	public void setQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier) {
		this.qualityAlgorithmVendorIdentifier = qualityAlgorithmVendorIdentifier;
	}
	public FingerQualityAlgorithmIdentifier getQualityAlgorithmIdentifier() {
		return qualityAlgorithmIdentifier;
	}
	public void setQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier) {
		this.qualityAlgorithmIdentifier = qualityAlgorithmIdentifier;
	}

	@Override
	public String toString() {
		return "\nQualityBlock [QualityBlockRecordLength=" + getRecordLength () + ", qualityScore=" + qualityScore + ", qualityAlgorithmVendorIdentifier="
				+ qualityAlgorithmVendorIdentifier + ", qualityAlgorithmIdentifier=" + qualityAlgorithmIdentifier + "]\n";
	}	
}
