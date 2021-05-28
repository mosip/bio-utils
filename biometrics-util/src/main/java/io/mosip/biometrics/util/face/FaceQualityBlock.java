package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaceQualityBlock extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceQualityBlock.class);	

    private int qualityScore;
    private FaceQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier;
    private FaceQualityAlgorithmIdentifier qualityAlgorithmIdentifier;

    public FaceQualityBlock (int qualityScore)
    {
    	setQualityScore (qualityScore);
    	setQualityAlgorithmVendorIdentifier (FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER);
    	setQualityAlgorithmIdentifier (FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER);
    }
    
    public FaceQualityBlock (int qualityScore, FaceQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier, FaceQualityAlgorithmIdentifier qualityAlgorithmIdentifier)
    {
    	setQualityScore (qualityScore);
    	setQualityAlgorithmVendorIdentifier (qualityAlgorithmVendorIdentifier);
    	setQualityAlgorithmIdentifier (qualityAlgorithmIdentifier);
    }

    public FaceQualityBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException { 
    	setQualityScore (inputStream.readUnsignedByte());
    	setQualityAlgorithmVendorIdentifier (FaceQualityAlgorithmVendorIdentifier.fromValue(inputStream.readUnsignedShort()));
    	setQualityAlgorithmIdentifier (FaceQualityAlgorithmIdentifier.fromValue(inputStream.readUnsignedShort()));
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
	public FaceQualityAlgorithmVendorIdentifier getQualityAlgorithmVendorIdentifier() {
		return qualityAlgorithmVendorIdentifier;
	}
	public void setQualityAlgorithmVendorIdentifier(FaceQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier) {
		this.qualityAlgorithmVendorIdentifier = qualityAlgorithmVendorIdentifier;
	}
	public FaceQualityAlgorithmIdentifier getQualityAlgorithmIdentifier() {
		return qualityAlgorithmIdentifier;
	}
	public void setQualityAlgorithmIdentifier(FaceQualityAlgorithmIdentifier qualityAlgorithmIdentifier) {
		this.qualityAlgorithmIdentifier = qualityAlgorithmIdentifier;
	}

	@Override
	public String toString() {
		return "\nQualityBlock [QualityBlockRecordLength=" + getRecordLength () + ", qualityScore=" + qualityScore + ", qualityAlgorithmVendorIdentifier="
				+ qualityAlgorithmVendorIdentifier + ", qualityAlgorithmIdentifier=" + qualityAlgorithmIdentifier + "]\n";
	}	
}
