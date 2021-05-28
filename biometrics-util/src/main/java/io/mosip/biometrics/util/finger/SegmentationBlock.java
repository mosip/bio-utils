package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentationBlock extends ExtendedDataBlock
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentationBlock.class);	

	private int segmentationQualityScore;
    private FingerSegmentationAlgorithmVendorIdentifier segmentationAlgorithmVendorIdentifier;
    private FingerSegmentationAlgorithmIdentifier segmentationAlgorithmIdentifier;
    
    private FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier;
    private FingerQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier;

    private int noOfSegmentationData;
    private SegmentationData [] segmentationData;
    
    public SegmentationBlock (int segmentationQualityScore, int noOfSegmentationData, SegmentationData [] segmentationData)
    {
    	setSegmentationAlgorithmVendorIdentifier (FingerSegmentationAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER);
    	setSegmentationAlgorithmIdentifier (FingerSegmentationAlgorithmIdentifier.ALGORITHM_IDENTIFIER);
    	setSegmentationQualityScore (segmentationQualityScore);

    	setQualityAlgorithmIdentifier (FingerQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER);
    	setQualityAlgorithmVendorIdentifier (FingerQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER);
    	
    	setNoOfSegmentationData (noOfSegmentationData);
    	setSegmentationData (segmentationData);
    	setExtendedDataBlockIdentificationCode (ExtendedDataBlockIdentificationCode.SEGMENTATION);
    	setLengthOfExtendedDataBlock (getRecordLength ());
    }
    
    public SegmentationBlock (FingerSegmentationAlgorithmVendorIdentifier segmentationAlgorithmVendorIdentifier, 
		FingerSegmentationAlgorithmIdentifier segmentationAlgorithmIdentifier, int segmentationQualityScore, 
		int noOfSegmentationData, SegmentationData [] segmentationData)
    {
    	setSegmentationAlgorithmVendorIdentifier (segmentationAlgorithmVendorIdentifier);
    	setSegmentationAlgorithmIdentifier (segmentationAlgorithmIdentifier);
    	setSegmentationQualityScore (segmentationQualityScore);

    	setQualityAlgorithmIdentifier (qualityAlgorithmIdentifier);
    	setQualityAlgorithmVendorIdentifier (qualityAlgorithmVendorIdentifier);
    	
    	setNoOfSegmentationData (noOfSegmentationData);
    	setSegmentationData (segmentationData);
    	setExtendedDataBlockIdentificationCode (ExtendedDataBlockIdentificationCode.SEGMENTATION);
    	setLengthOfExtendedDataBlock (getRecordLength ());
    }

    public SegmentationBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException { 
    	setLengthOfExtendedDataBlock (inputStream.readUnsignedShort());

    	int segmentationAlgorithmVendorIdentifier = inputStream.readUnsignedShort();
		try
		{		
	    	setSegmentationAlgorithmVendorIdentifier (FingerSegmentationAlgorithmVendorIdentifier.fromValue(segmentationAlgorithmVendorIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setSegmentationAlgorithmVendorIdentifier :: Not Defined :: segmentationAlgorithmVendorIdentifier :: " + Integer.toHexString (segmentationAlgorithmVendorIdentifier));
		}

    	int segmentationAlgorithmIdentifier = inputStream.readUnsignedShort();
		try
		{		
	    	setSegmentationAlgorithmIdentifier (FingerSegmentationAlgorithmIdentifier.fromValue(segmentationAlgorithmIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setSegmentationAlgorithmIdentifier :: Not Defined :: segmentationAlgorithmIdentifier :: " + Integer.toHexString (segmentationAlgorithmIdentifier));
		}
		
    	setSegmentationQualityScore (inputStream.readUnsignedByte());

    	int qualityAlgorithmIdentifier = inputStream.readUnsignedShort();
		try
		{		
	    	setQualityAlgorithmIdentifier (FingerQualityAlgorithmIdentifier.fromValue(qualityAlgorithmIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setQualityAlgorithmIdentifier :: Not Defined :: qualityAlgorithmIdentifier :: " + Integer.toHexString (qualityAlgorithmIdentifier));
		}

    	int qualityAlgorithmVendorIdentifier = inputStream.readUnsignedShort();
		try
		{		
			setQualityAlgorithmVendorIdentifier (FingerQualityAlgorithmVendorIdentifier.fromValue(qualityAlgorithmVendorIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setQualityAlgorithmVendorIdentifier :: Not Defined :: qualityAlgorithmVendorIdentifier :: " + Integer.toHexString (qualityAlgorithmVendorIdentifier));
		}
    	
    	setNoOfSegmentationData (inputStream.readUnsignedByte());
    	if (getNoOfSegmentationData() > 0)
    	{
    		SegmentationData [] segmentationData = new SegmentationData [getNoOfSegmentationData()];
        	for (int index=0; index < getNoOfSegmentationData(); index++)
        	{
        		segmentationData [index] = new SegmentationData (inputStream);
        	}
        	setSegmentationData (segmentationData);
    	}
    }
    /* 2 + 2 + 1 + 1 (Table 12 � Segmentation data ISO/IEC 19794-4-2011) */
    public int getRecordLength ()
    {
    	int recordLength = 0;
        if (getNoOfSegmentationData() > 0)
        {
            for (int index = 0; index < getNoOfSegmentationData(); index++)
            {
            	recordLength += getSegmentationData()[index].getRecordLength();
            }
        }
        
        return (2 + 2 + 2 + 2 + 1 + 2 + 2 + 1 + recordLength); 
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeShort (getExtendedDataBlockIdentificationCode().value()); 	/* 2 = 2 */
        outputStream.writeShort (getLengthOfExtendedDataBlock());       				/* + 2 = 4 */

        outputStream.writeShort (getSegmentationAlgorithmVendorIdentifier().value()); 	/* + 2 = 6 */
        outputStream.writeShort (getSegmentationAlgorithmIdentifier().value());       	/* + 2 = 8 */
        outputStream.writeByte (getSegmentationQualityScore ());					  	/* + 1  = 9*/
        
        outputStream.writeShort (getQualityAlgorithmIdentifier().value());       		/* + 2 = 11 */
        outputStream.writeShort (getQualityAlgorithmVendorIdentifier().value()); 		/* + 2 = 13 */

        outputStream.writeByte (getNoOfSegmentationData ());					  		/* + 1  = 14*/
        if (getNoOfSegmentationData() > 0)
        {
            for (int index = 0; index < getNoOfSegmentationData(); index++)
            {
            	getSegmentationData()[index].writeObject(outputStream);
            }
        }
        outputStream.flush ();
    }

	public int getSegmentationQualityScore() {
		return segmentationQualityScore;
	}

	public void setSegmentationQualityScore(int segmentationQualityScore) {
		this.segmentationQualityScore = segmentationQualityScore;
	}

	public FingerSegmentationAlgorithmVendorIdentifier getSegmentationAlgorithmVendorIdentifier() {
		return segmentationAlgorithmVendorIdentifier;
	}

	public void setSegmentationAlgorithmVendorIdentifier(
			FingerSegmentationAlgorithmVendorIdentifier segmentationAlgorithmVendorIdentifier) {
		this.segmentationAlgorithmVendorIdentifier = segmentationAlgorithmVendorIdentifier;
	}

	public FingerSegmentationAlgorithmIdentifier getSegmentationAlgorithmIdentifier() {
		return segmentationAlgorithmIdentifier;
	}

	public void setSegmentationAlgorithmIdentifier(FingerSegmentationAlgorithmIdentifier segmentationAlgorithmIdentifier) {
		this.segmentationAlgorithmIdentifier = segmentationAlgorithmIdentifier;
	}

	public FingerQualityAlgorithmVendorIdentifier getQualityAlgorithmVendorIdentifier() {
		return qualityAlgorithmVendorIdentifier;
	}

	public void setQualityAlgorithmVendorIdentifier(
			FingerQualityAlgorithmVendorIdentifier qualityAlgorithmVendorIdentifier) {
		this.qualityAlgorithmVendorIdentifier = qualityAlgorithmVendorIdentifier;
	}

	public FingerQualityAlgorithmIdentifier getQualityAlgorithmIdentifier() {
		return qualityAlgorithmIdentifier;
	}

	public void setQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier) {
		this.qualityAlgorithmIdentifier = qualityAlgorithmIdentifier;
	}

	public int getNoOfSegmentationData() {
		return noOfSegmentationData;
	}

	public void setNoOfSegmentationData(int noOfSegmentationData) {
		this.noOfSegmentationData = noOfSegmentationData;
	}

	public SegmentationData[] getSegmentationData() {
		return segmentationData;
	}

	public void setSegmentationData(SegmentationData[] segmentationData) {
		this.segmentationData = segmentationData;
	}

	@Override
	public String toString() {
		super.toString();
		
		return "\nSegmentationBlock [RecordLength=" + getRecordLength()
				+ ", segmentationAlgorithmVendorIdentifier=" + segmentationAlgorithmVendorIdentifier
				+ ", segmentationAlgorithmIdentifier=" + segmentationAlgorithmIdentifier
				+ ", segmentationQualityScore=" + Integer.toHexString (segmentationQualityScore)
				+ ", qualityAlgorithmIdentifier=" + qualityAlgorithmIdentifier + ", qualityAlgorithmVendorIdentifier="
				+ qualityAlgorithmVendorIdentifier + ", noOfSegmentationData=" + noOfSegmentationData
				+ ", segmentationData=" + Arrays.toString(segmentationData) + "]\n";
	}
}
