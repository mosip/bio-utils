package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageInformation extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageInformation.class);	

    private EyeLabel eyeLabel;
    private ImageType imageType;
    private ImageFormat imageFormat;
    private Orientation horizontalOrientation; 
    private Orientation verticalOrientation;
    private IrisImageCompressionType compressionType;
    private int width;
    private int height;
    private IrisImageBitDepth bitDepth;
    private int range;
    private int rollAngleOfEye;
    private int rollAngleUncertainty;
    private int irisCenterSmallestX;
    private int irisCenterLargestX;
    private int irisCenterSmallestY;
    private int irisCenterLargestY;
    private int irisDiameterSmallest;
    private int irisDiameterLargest;
    
    public ImageInformation(EyeLabel eyeLabel, ImageType imageType, ImageFormat imageFormat,
			Orientation horizontalOrientation, Orientation verticalOrientation, IrisImageCompressionType compressionType,
			int width, int height, IrisImageBitDepth bitDepth, int range, int rollAngleOfEye, int rollAngleUncertainty, 
			int irisCenterSmallestX, int irisCenterLargestX, int irisCenterSmallestY, int irisCenterLargestY, 
			int irisDiameterSmallest, int irisDiameterLargest) {
		super();
		setEyeLabel (eyeLabel);
		setImageType (imageType);
		setImageFormat (imageFormat);
		setHorizontalOrientation (horizontalOrientation);
		setVerticalOrientation (verticalOrientation);
		setCompressionType (compressionType);
		setWidth (width);
		setHeight (height);
		setBitDepth (bitDepth);
		setRange (range);
		setRollAngleOfEye (rollAngleOfEye);
		setRollAngleUncertainty (rollAngleUncertainty);
		setIrisCenterSmallestX (irisCenterSmallestX);
		setIrisCenterLargestX (irisCenterLargestX);
		setIrisCenterSmallestY (irisCenterSmallestY);
		setIrisCenterLargestY (irisCenterLargestY);
		setIrisDiameterSmallest (irisDiameterSmallest);
		setIrisDiameterLargest (irisDiameterLargest);
	}

	public ImageInformation (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {  
    	int eyeLabel = inputStream.readUnsignedByte();
		try
		{			
			setEyeLabel (EyeLabel.fromValue(eyeLabel));
		}
		catch(Exception ex)
		{
			LOGGER.error("setEyeLabel :: Not Defined :: eyeLabel :: " + eyeLabel);
		}

    	int imageType = inputStream.readUnsignedByte();
		try
		{			
			setImageType (ImageType.fromValue(imageType));
		}
		catch(Exception ex)
		{
			LOGGER.error("setImageType :: Not Defined :: imageType :: " + imageType);
		}

    	int imageFormat = inputStream.readUnsignedByte();
		try
		{			
			setImageFormat (ImageFormat.fromValue(imageFormat));
		}
		catch(Exception ex)
		{
			LOGGER.error("setImageFormat :: Not Defined :: imageFormat :: " + imageFormat);
		}
		/*
		 *    8  7   6  5  4  3  2  1
		 * [  |  |   |  |  |  |  |  |  ]
		 *                      1  1   = 0x0003 horizontalOrientation (>> 0)
		 *                 1  1  0  0  = 0x000C verticalOrientation (>> 2)
		 *           1  1  0  0  0  0  = 0x0060 empty (>> 2)
		 *    1  1   0  0  0  0  0  0  = 0x0080 compressionType (>> 2)
		 */
		int imagePropertiesBits = inputStream.readUnsignedByte();
		int horizontalOrientation = imagePropertiesBits & 0x0003;
		try
		{			
			setHorizontalOrientation (Orientation.fromValue(horizontalOrientation));
		}
		catch(Exception ex)
		{
			LOGGER.error("setHorizontalOrientation :: Not Defined :: horizontalOrientation :: " + horizontalOrientation);
		}
		int verticalOrientation = (imagePropertiesBits & 0x00C) >> 2;
		try
		{
			setVerticalOrientation (Orientation.fromValue(verticalOrientation));
		}
		catch(Exception ex)
		{
			LOGGER.error("setVerticalOrientation :: Not Defined :: verticalOrientation :: " + verticalOrientation);
		}

		int futureType = (imagePropertiesBits & 0x0060) >> 2;	//NO USED NOW			
		int compressionType = (imagePropertiesBits & 0x0080) >> 2;
		try
		{
			setCompressionType (IrisImageCompressionType.fromValue(compressionType));
		}
		catch(Exception ex)
		{
			LOGGER.error("setCompressionType :: Not Defined :: compressionType :: " + compressionType);
		}
		setWidth (inputStream.readUnsignedShort());
		setHeight (inputStream.readUnsignedShort());

		int bitDepth = inputStream.readUnsignedByte();
		try
		{
			setBitDepth (IrisImageBitDepth.fromValue(bitDepth));
		}
		catch(Exception ex)
		{
			LOGGER.error("setBitDepth :: Not Defined :: bitDepth :: " + bitDepth);
		}
		setRange (inputStream.readUnsignedShort());
		setRollAngleOfEye (inputStream.readUnsignedShort());
		setRollAngleUncertainty (inputStream.readUnsignedShort());
		setIrisCenterSmallestX (inputStream.readUnsignedShort());
		setIrisCenterLargestX (inputStream.readUnsignedShort());
		setIrisCenterSmallestY (inputStream.readUnsignedShort());
		setIrisCenterLargestY (inputStream.readUnsignedShort());
		setIrisDiameterSmallest (inputStream.readUnsignedShort());
		setIrisDiameterLargest (inputStream.readUnsignedShort());
    }
    
    public int getRecordLength ()
    {
        return 27; /* 1 + 1 + 1 + 1 + 2 + 2 + 1 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 (Table 4 � Iris representation header ISO/IEC 19794-6-2011) */
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeByte (getEyeLabel().value());                		/* 1 */
        outputStream.writeByte (getImageType().value());                	/* + 1 = 2 */
        outputStream.writeByte (getImageFormat().value());                	/* + 1 = 3 */

        byte imagePropertiesBits = 0;
        imagePropertiesBits |= (getHorizontalOrientation().value() & 0x0003); 		//Bit 1-2
		imagePropertiesBits |= ((getVerticalOrientation().value() << 2) & 0x00C); 	//Bit 3-4
		imagePropertiesBits |= ((0 << 2) & 0x0060); 								//Bit 5-6
		imagePropertiesBits |= ((getCompressionType().value() << 2) & 0x0080); 		//Bit 7-8
		outputStream.writeByte (imagePropertiesBits);                		/* + 1 = 4 */
		
        outputStream.writeShort (getWidth());								/* + 2 = 6 */
        outputStream.writeShort (getHeight());								/* + 2 = 8 */
        outputStream.writeByte (getBitDepth().value());     				/* + 1 = 9 */
        outputStream.writeShort (getRange());    							/* + 2 = 11 */
        outputStream.writeShort (getRollAngleOfEye());    					/* + 2 = 13 */
        outputStream.writeShort (getRollAngleUncertainty());    			/* + 2 = 15 */
        outputStream.writeShort (getIrisCenterSmallestX());    				/* + 2 = 17 */
        outputStream.writeShort (getIrisCenterLargestX());    				/* + 2 = 19 */
        outputStream.writeShort (getIrisCenterSmallestY());    				/* + 2 = 21 */
        outputStream.writeShort (getIrisCenterLargestY());    				/* + 2 = 23 */
        outputStream.writeShort (getIrisDiameterSmallest());    			/* + 2 = 25 */
        outputStream.writeShort (getIrisDiameterLargest());    				/* + 2 = 27 */
        outputStream.flush ();
    }

	public EyeLabel getEyeLabel() {
		return eyeLabel;
	}

	public void setEyeLabel(EyeLabel eyeLabel) {
		this.eyeLabel = eyeLabel;
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	public ImageFormat getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(ImageFormat imageFormat) {
		this.imageFormat = imageFormat;
	}

	public Orientation getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(Orientation horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	public Orientation getVerticalOrientation() {
		return verticalOrientation;
	}

	public void setVerticalOrientation(Orientation verticalOrientation) {
		this.verticalOrientation = verticalOrientation;
	}

	public IrisImageCompressionType getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(IrisImageCompressionType compressionType) {
		this.compressionType = compressionType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public IrisImageBitDepth getBitDepth() {
		return bitDepth;
	}

	public void setBitDepth(IrisImageBitDepth bitDepth) {
		this.bitDepth = bitDepth;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRollAngleOfEye() {
		return rollAngleOfEye;
	}

	public void setRollAngleOfEye(int rollAngleOfEye) {
		this.rollAngleOfEye = rollAngleOfEye;
	}
	
	public int getRollAngleUncertainty() {
		return rollAngleUncertainty;
	}

	public void setRollAngleUncertainty(int rollAngleUncertainty) {
		this.rollAngleUncertainty = rollAngleUncertainty;
	}

	public int getIrisCenterSmallestX() {
		return irisCenterSmallestX;
	}

	public void setIrisCenterSmallestX(int irisCenterSmallestX) {
		this.irisCenterSmallestX = irisCenterSmallestX;
	}

	public int getIrisCenterLargestX() {
		return irisCenterLargestX;
	}

	public void setIrisCenterLargestX(int irisCenterLargestX) {
		this.irisCenterLargestX = irisCenterLargestX;
	}

	public int getIrisCenterSmallestY() {
		return irisCenterSmallestY;
	}

	public void setIrisCenterSmallestY(int irisCenterSmallestY) {
		this.irisCenterSmallestY = irisCenterSmallestY;
	}

	public int getIrisCenterLargestY() {
		return irisCenterLargestY;
	}

	public void setIrisCenterLargestY(int irisCenterLargestY) {
		this.irisCenterLargestY = irisCenterLargestY;
	}

	public int getIrisDiameterSmallest() {
		return irisDiameterSmallest;
	}

	public void setIrisDiameterSmallest(int irisDiameterSmallest) {
		this.irisDiameterSmallest = irisDiameterSmallest;
	}

	public int getIrisDiameterLargest() {
		return irisDiameterLargest;
	}

	public void setIrisDiameterLargest(int irisDiameterLargest) {
		this.irisDiameterLargest = irisDiameterLargest;
	}

	@Override
	public String toString() {
		return "\nImageInformation [RecordLength=" + getRecordLength () + ", eyeLabel=" + eyeLabel + ", imageType=" + imageType + ", imageFormat=" + imageFormat
				+ ", horizontalOrientation=" + horizontalOrientation + ", verticalOrientation=" + verticalOrientation
				+ ", compressionType=" + compressionType + ", width=" + width + ", height=" + height + ", bitDepth="
				+ bitDepth + ", range=" + range + ", rollAngleOfEye=" + rollAngleOfEye + ", rollAngleUncertainty="
				+ rollAngleUncertainty + ", irisCenterSmallestX=" + irisCenterSmallestX + ", irisCenterLargestX="
				+ irisCenterLargestX + ", irisCenterSmallestY=" + irisCenterSmallestY + ", irisCenterLargestY="
				+ irisCenterLargestY + ", irisDiameterSmallest=" + irisDiameterSmallest + ", irisDiameterLargest="
				+ irisDiameterLargest + "]\n";
	}	
}
