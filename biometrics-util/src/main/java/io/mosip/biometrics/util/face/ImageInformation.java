package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageInformation extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageInformation.class);	

    private FaceImageType faceImageType;
    private ImageDataType imageDataType;
    private int width;
    private int height;
    private SpatialSamplingRateLevel spatialSamplingRateLevel;
    private PostAcquisitionProcessing postAcquistionProcessing;
    private CrossReference crossReference;
    private ImageColourSpace imageColorSpace;

    public ImageInformation (int width, int height)
    {
        setFaceImageType (FaceImageType.BASIC);
        setImageDataType (ImageDataType.JPEG2000_LOSS_LESS);
        setWidth (width);
        setHeight (height);
        setSpatialSamplingRateLevel (SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180);
        setPostAcquistionProcessing (PostAcquisitionProcessing.CROPPED);
        setCrossReference (CrossReference.BASIC);
        setImageColorSpace (ImageColourSpace.UNSPECIFIED);
    }

    public ImageInformation (FaceImageType faceImageType, ImageDataType imageDataType, int width, int height, 
		SpatialSamplingRateLevel spatialSamplingRateLevel, PostAcquisitionProcessing postAcquistionProcessing, 
		CrossReference crossReference, ImageColourSpace imageColorSpace)
    {
        setFaceImageType (faceImageType);
        setImageDataType (imageDataType);
        setWidth (width);
        setHeight (height);
        setSpatialSamplingRateLevel (spatialSamplingRateLevel);
        setPostAcquistionProcessing (postAcquistionProcessing);
        setCrossReference (crossReference);
        setImageColorSpace (imageColorSpace);
    }

    public ImageInformation (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {  
        setFaceImageType (FaceImageType.fromValue(inputStream.readUnsignedByte()));
        setImageDataType (ImageDataType.fromValue(inputStream.readUnsignedByte()));
        setWidth (inputStream.readUnsignedShort());
        setHeight (inputStream.readUnsignedShort());
        setSpatialSamplingRateLevel (SpatialSamplingRateLevel.fromValue(inputStream.readUnsignedByte()));
        setPostAcquistionProcessing (PostAcquisitionProcessing.fromValue(inputStream.readUnsignedShort()));
        setCrossReference (CrossReference.fromValue(inputStream.readUnsignedByte()));
        setImageColorSpace (ImageColourSpace.fromValue(inputStream.readUnsignedByte()));
    }
    
    public int getRecordLength ()
    {
        return 11; /* 1 + 1 + 2 + 2 + 1 + 2 + 1 + 1 (Figure 2 ISO/IEC 19794-5) */
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeByte (getFaceImageType().value());                /* 1 */
        outputStream.writeByte (getImageDataType().value());                /* + 1 = 2 */
        outputStream.writeShort (getWidth());								/* + 2 = 4 */
        outputStream.writeShort (getHeight());								/* + 2 = 6 */
        outputStream.writeByte (getSpatialSamplingRateLevel().value());     /* + 1 = 7 */
        outputStream.writeShort (getPostAcquistionProcessing().value());    /* + 2 = 9 */
        outputStream.writeByte (getCrossReference().value());               /* + 1 = 10 */
        outputStream.writeByte (getImageColorSpace().value());				/* + 1 = 11 */
        outputStream.flush ();
    }

	public FaceImageType getFaceImageType() {
		return faceImageType;
	}

	public void setFaceImageType(FaceImageType faceImageType) {
		this.faceImageType = faceImageType;
	}

	public ImageDataType getImageDataType() {
		return imageDataType;
	}

	public void setImageDataType(ImageDataType imageDataType) {
		this.imageDataType = imageDataType;
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

	public SpatialSamplingRateLevel getSpatialSamplingRateLevel() {
		return spatialSamplingRateLevel;
	}

	public void setSpatialSamplingRateLevel(SpatialSamplingRateLevel spatialSamplingRateLevel) {
		this.spatialSamplingRateLevel = spatialSamplingRateLevel;
	}

	public PostAcquisitionProcessing getPostAcquistionProcessing() {
		return postAcquistionProcessing;
	}

	public void setPostAcquistionProcessing(PostAcquisitionProcessing postAcquistionProcessing) {
		this.postAcquistionProcessing = postAcquistionProcessing;
	}

	public CrossReference getCrossReference() {
		return crossReference;
	}

	public void setCrossReference(CrossReference crossReference) {
		this.crossReference = crossReference;
	}

	public ImageColourSpace getImageColorSpace() {
		return imageColorSpace;
	}

	public void setImageColorSpace(ImageColourSpace imageColorSpace) {
		this.imageColorSpace = imageColorSpace;
	}

	@Override
	public String toString() {
		return "\nImageInformation [ImageInformationRecordLength=" + getRecordLength () + ", faceImageType=" + faceImageType + ", imageDataType=" + imageDataType + ", width="
				+ width + ", height=" + height + ", spatialSamplingRateLevel=" + spatialSamplingRateLevel
				+ ", postAcquistionProcessing=" + postAcquistionProcessing + ", crossReference=" + crossReference
				+ ", imageColorSpace=" + imageColorSpace + "]\n";
	}    	
}
