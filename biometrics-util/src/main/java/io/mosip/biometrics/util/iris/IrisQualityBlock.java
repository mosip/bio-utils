package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrisQualityBlock extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(IrisQualityBlock.class);	

    private int qualityScore;
    private IrisQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier;
    private IrisQualityAlgorithmIdentifier qualityAlgorithmIdentifier;

    public IrisQualityBlock (int qualityScore)
    {
    	setQualityScore (qualityScore);
    	setQualityAlgorithmVendorIdentifier (IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER);
    	setQualityAlgorithmIdentifier (IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER);
    }
    
    public IrisQualityBlock (int qualityScore, IrisQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier, IrisQualityAlgorithmIdentifier qualityAlgorithmIdentifier)
    {
    	setQualityScore (qualityScore);
    	setQualityAlgorithmVendorIdentifier (qualityAlgorithmVendorIdentifier);
    	setQualityAlgorithmIdentifier (qualityAlgorithmIdentifier);
    }

    public IrisQualityBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException { 
    	setQualityScore (inputStream.readUnsignedByte());
    	setQualityAlgorithmVendorIdentifier (IrisQualityAlgorithmVendorIdentifier.fromValue(inputStream.readUnsignedShort()));
    	setQualityAlgorithmIdentifier (IrisQualityAlgorithmIdentifier.fromValue(inputStream.readUnsignedShort()));
    }
    
    public int getRecordLength ()
    {
        return 5; /* 1 + 2 + 2 (table 2 ISO/IEC 19794-5) */
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
	public IrisQualityAlgorithmVendorIdentifier getQualityAlgorithmVendorIdentifier() {
		return qualityAlgorithmVendorIdentifier;
	}
	public void setQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier) {
		this.qualityAlgorithmVendorIdentifier = qualityAlgorithmVendorIdentifier;
	}
	public IrisQualityAlgorithmIdentifier getQualityAlgorithmIdentifier() {
		return qualityAlgorithmIdentifier;
	}
	public void setQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier qualityAlgorithmIdentifier) {
		this.qualityAlgorithmIdentifier = qualityAlgorithmIdentifier;
	}

	@Override
	public String toString() {
		return "\nIrisQualityBlock [QualityBlockRecordLength=" + getRecordLength () + ", qualityScore=" + qualityScore + ", qualityAlgorithmVendorIdentifier="
				+ qualityAlgorithmVendorIdentifier + ", qualityAlgorithmIdentifier=" + qualityAlgorithmIdentifier + "]\n";
	}	
}
