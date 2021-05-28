package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepresentationHeader extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RepresentationHeader.class);	
   
    private int representationDataLength;
    private int representationLength;

    private Date captureDateTime;
    private int captureYear;
    private int captureMonth;
    private int captureDay;
    private int captureHour;
    private int captureMinute;
    private int captureSecond;
    private int captureMilliSecond;

    private IrisCaptureDeviceTechnology captureDeviceTechnologyIdentifier;
    private IrisCaptureDeviceVendor captureDeviceVendorIdentifier;
    private IrisCaptureDeviceType captureDeviceTypeIdentifier;

    private int noOfQualityBlocks;
    private IrisQualityBlock[] qualityBlocks;
    private ImageInformation imageInformation;
    private int representationNo;

    public RepresentationHeader (int representationDataLength, Date captureDate,
        IrisQualityBlock [] qualityBlocks, ImageInformation imageInformation, int representationNo)
    {
        setRepresentationDataLength (representationDataLength);
        setCaptureDateTime (captureDate);
        
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(captureDate);
		setCaptureYear (calendar.get(Calendar.YEAR));
		setCaptureMonth (calendar.get(Calendar.MONTH));
		setCaptureDay (calendar.get(Calendar.DAY_OF_MONTH));
		setCaptureHour (calendar.get(Calendar.HOUR_OF_DAY));
		setCaptureMinute (calendar.get(Calendar.MINUTE));
		setCaptureSecond (calendar.get(Calendar.SECOND));
		setCaptureMilliSecond (calendar.get(Calendar.MILLISECOND));

        setCaptureDeviceTechnologyIdentifier (IrisCaptureDeviceTechnology.UNSPECIFIED);
        setCaptureDeviceVendorIdentifier (IrisCaptureDeviceVendor.UNSPECIFIED);
        setCaptureDeviceTypeIdentifier (IrisCaptureDeviceType.UNSPECIFIED);

        setNoOfQualityBlocks (qualityBlocks.length);
        setQualityBlocks (qualityBlocks);
        setImageInformation (imageInformation);
        setRepresentationNo (representationNo);
    }

    public RepresentationHeader (int representationDataLength, Date captureDate, 
    		IrisCaptureDeviceTechnology captureDeviceTechnologyIdentifier, 
    		IrisCaptureDeviceVendor captureDeviceVendorIdentifier, 
    		IrisCaptureDeviceType captureDeviceTypeIdentifier,
    		IrisQualityBlock [] qualityBlocks, ImageInformation imageInformation, 
    		int representationNo)
    {
        setRepresentationDataLength (representationDataLength);
        setCaptureDateTime (captureDate);

        Calendar calendar = Calendar.getInstance();
		calendar.setTime(captureDate);
		setCaptureYear (calendar.get(Calendar.YEAR));
		setCaptureMonth (calendar.get(Calendar.MONTH));
		setCaptureDay (calendar.get(Calendar.DAY_OF_MONTH));
		setCaptureHour (calendar.get(Calendar.HOUR_OF_DAY));
		setCaptureMinute (calendar.get(Calendar.MINUTE));
		setCaptureSecond (calendar.get(Calendar.SECOND));
		setCaptureMilliSecond (calendar.get(Calendar.MILLISECOND));

        setCaptureDeviceTechnologyIdentifier (captureDeviceTechnologyIdentifier);
        setCaptureDeviceVendorIdentifier (captureDeviceVendorIdentifier);
        setCaptureDeviceTypeIdentifier (captureDeviceTypeIdentifier);

        setNoOfQualityBlocks (qualityBlocks.length);
        setQualityBlocks (qualityBlocks);
        setImageInformation (imageInformation);
        setRepresentationNo (representationNo);
    }

    public RepresentationHeader (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {    
    	setRepresentationDataLength ((int)(inputStream.readInt() & 0xFFFFFFFFL));

        Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date ());
		setCaptureYear (inputStream.readUnsignedShort());
		setCaptureMonth (inputStream.readUnsignedByte());
		setCaptureDay (inputStream.readUnsignedByte());
		setCaptureHour (inputStream.readUnsignedByte());
		setCaptureMinute (inputStream.readUnsignedByte());
		setCaptureSecond (inputStream.readUnsignedByte());
		setCaptureMilliSecond (inputStream.readUnsignedShort());

		calendar.set(Calendar.YEAR, getCaptureYear());
		calendar.set(Calendar.MONTH, getCaptureMonth() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, getCaptureDay());
		calendar.set(Calendar.HOUR_OF_DAY, getCaptureHour());
		calendar.set(Calendar.MINUTE, getCaptureMinute());
		calendar.set(Calendar.SECOND, getCaptureSecond());
		calendar.set(Calendar.MILLISECOND, getCaptureMilliSecond());
		setCaptureDateTime (calendar.getTime());

		setCaptureDeviceTechnologyIdentifier (IrisCaptureDeviceTechnology.fromValue(inputStream.readUnsignedByte()));
		int captureDeviceVendorIdentifier = inputStream.readUnsignedShort();
		try
		{			
	        setCaptureDeviceVendorIdentifier (IrisCaptureDeviceVendor.fromValue(captureDeviceVendorIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setCaptureDeviceVendorIdentifier :: Not Defined :: captureDeviceVendorIdentifier :: " + captureDeviceVendorIdentifier);
		}

		int captureDeviceTypeIdentifier = inputStream.readUnsignedShort();
		try
		{			
			setCaptureDeviceTypeIdentifier (IrisCaptureDeviceType.fromValue(captureDeviceTypeIdentifier));
		}
		catch(Exception ex)
		{
			LOGGER.error("setCaptureDeviceTypeIdentifier :: Not Defined :: captureDeviceTypeIdentifier :: " + captureDeviceTypeIdentifier);
		}

        setNoOfQualityBlocks (inputStream.readUnsignedByte());
        IrisQualityBlock [] qualityBlock = new IrisQualityBlock[getNoOfQualityBlocks ()];
        if (getNoOfQualityBlocks () > 0)
        {
            for (int index=0;index < getNoOfQualityBlocks (); index++)
            {        	
            	qualityBlock [index] = new IrisQualityBlock (inputStream);        	
            }
        }
        setQualityBlocks (qualityBlock);
        setRepresentationNo (inputStream.readUnsignedShort());
        
        setImageInformation (new ImageInformation (inputStream));
    }
    
    public int getRecordLength ()
    {
        int qualityBlockRecordLength = 0;
        if (getQualityBlocks() != null && getQualityBlocks().length > 0)
        {
            for (int index = 0; index < getQualityBlocks().length; index++)
                qualityBlockRecordLength += getQualityBlocks() [index].getRecordLength ();
        }

        return (4 + 9 + 1 + 2 + 2 + 1 + qualityBlockRecordLength + 2 + 27); /* 4 + 9 + 1 + 2 + 2 + 1 + 5x + 2 + 27 (Table 4 � Iris representation header ISO/IEC 19794-5-2011) */
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeInt (getRecordLength () + getRepresentationDataLength());                                             
        outputStream.writeShort (getCaptureYear());                                                            
        outputStream.writeByte (getCaptureMonth() + 1);  
        outputStream.writeByte (getCaptureDay());                                                         
        outputStream.writeByte (getCaptureHour());
        outputStream.writeByte (getCaptureMinute());
        outputStream.writeByte (getCaptureSecond());
        outputStream.writeShort (getCaptureMilliSecond());

        outputStream.writeByte (getCaptureDeviceTechnologyIdentifier().value());	                        
        outputStream.writeShort (getCaptureDeviceVendorIdentifier().value());		                
        outputStream.writeShort (getCaptureDeviceTypeIdentifier().value());

        outputStream.writeByte (getNoOfQualityBlocks());
        if (getQualityBlocks() != null)
        {
            for (int index = 0; index < getQualityBlocks().length; index++)
            {
            	getQualityBlocks() [index].writeObject (outputStream);
            }
        }

        outputStream.writeShort (getRepresentationNo());
        getImageInformation().writeObject (outputStream);
        outputStream.flush ();
    }

	public int getRepresentationDataLength() {
		return representationDataLength;
	}

	public void setRepresentationDataLength(int representationDataLength) {
		this.representationDataLength = representationDataLength;
	}

	public int getRepresentationLength() {
		return representationLength;
	}

	public void setRepresentationLength(int representationLength) {
		this.representationLength = representationLength;
	}
	
	public Date getCaptureDateTime() {
		return captureDateTime;
	}

	public void setCaptureDateTime(Date captureDateTime) {
		this.captureDateTime = captureDateTime;
	}

	public int getCaptureYear() {
		return captureYear;
	}

	public void setCaptureYear(int captureYear) {
		this.captureYear = captureYear;
	}

	public int getCaptureMonth() {
		return captureMonth;
	}

	public void setCaptureMonth(int captureMonth) {
		this.captureMonth = captureMonth;
	}

	public int getCaptureDay() {
		return captureDay;
	}

	public void setCaptureDay(int captureDay) {
		this.captureDay = captureDay;
	}

	public int getCaptureHour() {
		return captureHour;
	}

	public void setCaptureHour(int captureHour) {
		this.captureHour = captureHour;
	}

	public int getCaptureMinute() {
		return captureMinute;
	}

	public void setCaptureMinute(int captureMinute) {
		this.captureMinute = captureMinute;
	}

	public int getCaptureSecond() {
		return captureSecond;
	}

	public void setCaptureSecond(int captureSecond) {
		this.captureSecond = captureSecond;
	}

	public int getCaptureMilliSecond() {
		return captureMilliSecond;
	}

	public void setCaptureMilliSecond(int captureMilliSecond) {
		this.captureMilliSecond = captureMilliSecond;
	}

	public IrisCaptureDeviceTechnology getCaptureDeviceTechnologyIdentifier() {
		return captureDeviceTechnologyIdentifier;
	}

	public void setCaptureDeviceTechnologyIdentifier(IrisCaptureDeviceTechnology captureDeviceTechnologyIdentifier) {
		this.captureDeviceTechnologyIdentifier = captureDeviceTechnologyIdentifier;
	}

	public IrisCaptureDeviceVendor getCaptureDeviceVendorIdentifier() {
		return captureDeviceVendorIdentifier;
	}

	public void setCaptureDeviceVendorIdentifier(IrisCaptureDeviceVendor captureDeviceVendorIdentifier) {
		this.captureDeviceVendorIdentifier = captureDeviceVendorIdentifier;
	}

	public IrisCaptureDeviceType getCaptureDeviceTypeIdentifier() {
		return captureDeviceTypeIdentifier;
	}

	public void setCaptureDeviceTypeIdentifier(IrisCaptureDeviceType captureDeviceTypeIdentifier) {
		this.captureDeviceTypeIdentifier = captureDeviceTypeIdentifier;
	}

	public int getNoOfQualityBlocks() {
		return noOfQualityBlocks;
	}

	public void setNoOfQualityBlocks(int noOfQualityBlocks) {
		this.noOfQualityBlocks = noOfQualityBlocks;
	}

	public IrisQualityBlock[] getQualityBlocks() {
		return qualityBlocks;
	}

	public void setQualityBlocks(IrisQualityBlock[] qualityBlocks) {
		this.qualityBlocks = qualityBlocks;
	}

	public ImageInformation getImageInformation() {
		return imageInformation;
	}

	public void setImageInformation(ImageInformation imageInformation) {
		this.imageInformation = imageInformation;
	}

	public int getRepresentationNo() {
		return representationNo;
	}

	public void setRepresentationNo(int representationNo) {
		this.representationNo = representationNo;
	}

	@Override
	public String toString() {
		return "RepresentationHeader [RecordLength=" + getRecordLength () 
				+ ", representationDataLength=" + representationDataLength + ", representationLength="
				+ representationLength + ", captureDateTime=" + captureDateTime + ", captureYear=" + captureYear
				+ ", captureMonth=" + captureMonth + ", captureDay=" + captureDay + ", captureHour=" + captureHour
				+ ", captureMinute=" + captureMinute + ", captureSecond=" + captureSecond + ", captureMilliSecond="
				+ captureMilliSecond + ", captureDeviceTechnologyIdentifier=" + captureDeviceTechnologyIdentifier
				+ ", captureDeviceVendorIdentifier=" + captureDeviceVendorIdentifier + ", captureDeviceTypeIdentifier="
				+ captureDeviceTypeIdentifier + ", noOfQualityBlocks=" + noOfQualityBlocks + ", qualityBlocks="
				+ Arrays.toString(qualityBlocks) + ", imageInformation=" + imageInformation + ", representationNo="
				+ representationNo + "]";
	}
}
