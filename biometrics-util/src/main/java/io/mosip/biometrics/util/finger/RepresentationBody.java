package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepresentationBody extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RepresentationBody.class);	

	private ImageData imageData;
	private SegmentationBlock segmentationBlock; 
	private AnnotationBlock annotationBlock;
	private CommentBlock commentBlock;

    public RepresentationBody (ImageData imageData, SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock, CommentBlock commentBlock)
    {
    	setImageData (imageData);
    	setSegmentationBlock (segmentationBlock);
    	setAnnotationBlock (annotationBlock);
    	setCommentBlock (commentBlock);
    }

    public RepresentationBody (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {    	
    	setImageData (new ImageData (inputStream));
    	try
    	{
    		if (inputStream.available() > 0)
    		{
        		int typeIdentificationCode = inputStream.readUnsignedShort();
        		if (ExtendedDataBlockIdentificationCode.fromValue(typeIdentificationCode) == ExtendedDataBlockIdentificationCode.SEGMENTATION)
        		{
        	    	setSegmentationBlock (new SegmentationBlock (inputStream));
        		}
    		}
    		if (inputStream.available() > 0)
    		{
        		int typeIdentificationCode = inputStream.readUnsignedShort();
        		if (ExtendedDataBlockIdentificationCode.fromValue(typeIdentificationCode) == ExtendedDataBlockIdentificationCode.ANNOTATION)
        		{
        			setAnnotationBlock (new AnnotationBlock (inputStream));
        		}
    		}
    		if (inputStream.available() > 0)
    		{
        		int typeIdentificationCode = inputStream.readUnsignedShort();
        		if (ExtendedDataBlockIdentificationCode.fromValue(typeIdentificationCode) == ExtendedDataBlockIdentificationCode.COMMENT_03)
        		{
        			setCommentBlock (new CommentBlock (inputStream));
        		}
    		}
    	}
    	catch(Exception ex)
    	{
    		LOGGER.error("readObject :: Error ::", ex);
    	}
    }

    public int getRecordLength ()
    {
        return getImageData().getRecordLength () + 
        		(getSegmentationBlock() != null ? getSegmentationBlock().getRecordLength () : 0) + 
        		(getAnnotationBlock() != null ? getAnnotationBlock().getRecordLength () : 0) + 
        		(getCommentBlock() != null ? getCommentBlock().getRecordLength () : 0) ;
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
    	getImageData().writeObject (outputStream);
    	
    	if (getSegmentationBlock() != null)
    		getSegmentationBlock().writeObject (outputStream);
    	
    	if (getAnnotationBlock() != null)
    		getAnnotationBlock().writeObject (outputStream);
    	
    	if (getCommentBlock() != null)
    		getCommentBlock().writeObject (outputStream);
    	
        outputStream.flush ();
    }

	public ImageData getImageData() {
		return imageData;
	}

	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}

	
	public SegmentationBlock getSegmentationBlock() {
		return segmentationBlock;
	}

	public void setSegmentationBlock(SegmentationBlock segmentationBlock) {
		this.segmentationBlock = segmentationBlock;
	}

	public AnnotationBlock getAnnotationBlock() {
		return annotationBlock;
	}

	public void setAnnotationBlock(AnnotationBlock annotationBlock) {
		this.annotationBlock = annotationBlock;
	}

	public CommentBlock getCommentBlock() {
		return commentBlock;
	}

	public void setCommentBlock(CommentBlock commentBlock) {
		this.commentBlock = commentBlock;
	}

	@Override
	public String toString() {
		return "RepresentationBody [RecordLength=" + getRecordLength() + ", imageData=" + imageData + ", segmentationBlock=" + segmentationBlock
				+ ", annotationBlock=" + annotationBlock + ", commentBlock=" + commentBlock + "]";
	}
}
