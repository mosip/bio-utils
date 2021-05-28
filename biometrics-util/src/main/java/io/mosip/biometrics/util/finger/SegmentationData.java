package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentationData extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentationData.class);	

    private FingerPosition fingerPosition;
    private int qualityScore;
    private int noOfCoordinates;
    private int []XCoordinates;
    private int []YCoordinates;
    private int fingerOrientation;
    
    public SegmentationData (int qualityScore)
    {
    	setFingerPosition (FingerPosition.UNKNOWN);
    	setQualityScore (qualityScore);
    	setNoOfCoordinates (2);// Default 2 or More
    	setXCoordinates (new int[2]);
    	setYCoordinates (new int[2]);
    	for (int index=0; index < getNoOfCoordinates(); index++)
    	{
    		this.getXCoordinates() [index] = 0;
    		this.getYCoordinates() [index] = 0;
    	}
    	setFingerOrientation (0);
    }
    
    public SegmentationData (FingerPosition fingerPosition, int qualityScore, 
    	int noOfCoordinates, int [] xCoordinates, int [] yCoordinates, int fingerOrientation)
    {
    	setFingerPosition (fingerPosition);
    	setQualityScore (qualityScore);
    	setNoOfCoordinates (noOfCoordinates);// Default 2 or More
    	setXCoordinates (xCoordinates);
    	setYCoordinates (yCoordinates);
    	setFingerOrientation (fingerOrientation);
    }

    public SegmentationData (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException { 
    	setFingerPosition (FingerPosition.fromValue(inputStream.readUnsignedByte()));
    	setQualityScore (inputStream.readUnsignedByte());
    	setNoOfCoordinates (inputStream.readUnsignedByte());// Default 2 or More
    	setXCoordinates (new int[getNoOfCoordinates()]);
    	setYCoordinates (new int[getNoOfCoordinates()]);
    	for (int index=0; index < getNoOfCoordinates(); index++)
    	{
    		this.getXCoordinates() [index] = inputStream.readUnsignedShort();
    		this.getYCoordinates() [index] = inputStream.readUnsignedShort();
    	}
    	setFingerOrientation (inputStream.readUnsignedByte());
    }
    
    /*  (1 + 1 + 1 + (NoOfCoordinates * 4) + 1 ) (Table 12 � Segmentation data  ISO/IEC 19794-4-2011) */
    public int getRecordLength ()
    {
    	int recordLength = 0;
        if (getNoOfCoordinates() > 0)
        {
            for (int index = 0; index < getNoOfCoordinates(); index++)
            	recordLength += (2 + 2);
        }
        
        return (1 + 1 + 1 + recordLength + 1); 
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeByte (getFingerPosition().value()); 	/* 1 = 1 */
        outputStream.writeByte (getQualityScore());       		/* + 1 = 2 */
    	outputStream.writeByte (getNoOfCoordinates ());			/* + 1 = 3*/
    	for (int index=0; index < getNoOfCoordinates(); index++)
    	{
    		outputStream.writeShort (this.getXCoordinates () [index]);	/* + 2 = 5 */
    		outputStream.writeShort (this.getYCoordinates () [index]);  /* + 2 = 7 */
    	}
    	outputStream.writeByte (getFingerOrientation ());			/* + 1 = 8*/
        outputStream.flush ();
    }

	public FingerPosition getFingerPosition() {
		return fingerPosition;
	}

	public void setFingerPosition(FingerPosition fingerPosition) {
		this.fingerPosition = fingerPosition;
	}

	public int getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(int qualityScore) {
		this.qualityScore = qualityScore;
	}

	public int getNoOfCoordinates() {
		return noOfCoordinates;
	}

	public void setNoOfCoordinates(int noOfCoordinates) {
		this.noOfCoordinates = noOfCoordinates;
	}

	public int[] getXCoordinates() {
		return XCoordinates;
	}

	public void setXCoordinates(int[] xCoordinates) {
		XCoordinates = xCoordinates;
	}

	public int[] getYCoordinates() {
		return YCoordinates;
	}

	public void setYCoordinates(int[] yCoordinates) {
		YCoordinates = yCoordinates;
	}

	public int getFingerOrientation() {
		return fingerOrientation;
	}

	public void setFingerOrientation(int fingerOrientation) {
		this.fingerOrientation = fingerOrientation;
	}

	@Override
	public String toString() {
		return "\nSegmentationData [RecordLength=" + getRecordLength() + ", fingerPosition=" + fingerPosition + ", qualityScore=" + qualityScore
				+ ", noOfCoordinates=" + noOfCoordinates + ", XCoordinates=" + Arrays.toString(XCoordinates)
				+ ", YCoordinates=" + Arrays.toString(YCoordinates) + ", fingerOrientation=" + fingerOrientation + "]\n";
	}    
}
