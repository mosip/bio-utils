package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Representation extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(Representation.class);	

	private RepresentationHeader representationHeader;
    private RepresentationBody representationBody;
    private FingerCertificationFlag certificationFlag;

    public Representation (Date captureDate, FingerQualityBlock [] qualityBlocks, 
    		FingerCertificationFlag certificationFlag, FingerCertificationBlock[] certificationBlocks, 
    		FingerPosition fingerPosition, int representationNo, 
    		FingerScaleUnitType scaleUnitType,  byte [] image)
    {
    	setCertificationFlag (certificationFlag);
    	setRepresentationBody (new RepresentationBody (new ImageData (image), null, null, null));
    	setRepresentationHeader (new RepresentationHeader (getRepresentationBody().getRecordLength (), captureDate, qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType));
    }

    public Representation (Date captureDate, 
    		FingerCaptureDeviceTechnology captureDeviceTechnologyIdentifier, 
    		FingerCaptureDeviceVendor captureDeviceVendorIdentifier, 
    		FingerCaptureDeviceType captureDeviceTypeIdentifier,
    		FingerQualityBlock [] qualityBlocks, 
    		FingerCertificationFlag certificationFlag, FingerCertificationBlock[] certificationBlocks, 
    		FingerPosition fingerPosition, int representationNo, 
    		FingerScaleUnitType scaleUnitType, 
    		int captureDeviceSpatialSamplingRateHorizontal, int captureDeviceSpatialSamplingRateVertical, 
    		int imageSpatialSamplingRateHorizontal, int imageSpatialSamplingRateVertical,
    		FingerImageBitDepth bitDepth, FingerImageCompressionType compressionType,
    		FingerImpressionType impressionType, int lineLengthHorizontal, int lineLengthVertical,
    		byte [] image, 
    		SegmentationBlock segmentationBlock, 
    		AnnotationBlock annotationBlock, 
    		CommentBlock commentBlock)
    {
    	setCertificationFlag (certificationFlag);
    	setRepresentationBody (new RepresentationBody (new ImageData (image), segmentationBlock, annotationBlock, commentBlock));
    	setRepresentationHeader (new RepresentationHeader (getRepresentationBody().getRecordLength (), captureDate, captureDeviceTechnologyIdentifier, 
    			captureDeviceVendorIdentifier, captureDeviceTypeIdentifier, qualityBlocks, certificationFlag, certificationBlocks, 
    			fingerPosition, representationNo, 
    			scaleUnitType, captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical, 
    			imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical,
    			bitDepth, compressionType, impressionType, lineLengthHorizontal, lineLengthVertical));
    	
        LOGGER.info (this.toString());
    }
    
    public Representation (DataInputStream inputStream, FingerCertificationFlag certificationFlag) throws IOException
	{
    	setCertificationFlag (certificationFlag);
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {    	
    	setRepresentationHeader (new RepresentationHeader (inputStream, getCertificationFlag ()));
    	setRepresentationBody (new RepresentationBody (inputStream));
    }
    
    public int getRecordLength ()
    {
        return getRepresentationHeader().getRecordLength () + getRepresentationBody().getRecordLength ();
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
    	getRepresentationHeader().writeObject (outputStream);
    	getRepresentationBody().writeObject (outputStream);
        outputStream.flush ();
    }

	public RepresentationHeader getRepresentationHeader() {
		return representationHeader;
	}

	public void setRepresentationHeader(RepresentationHeader representationHeader) {
		this.representationHeader = representationHeader;
	}

	public RepresentationBody getRepresentationBody() {
		return representationBody;
	}

	public void setRepresentationBody(RepresentationBody representationBody) {
		this.representationBody = representationBody;
	}

	public FingerCertificationFlag getCertificationFlag() {
		return certificationFlag;
	}

	public void setCertificationFlag(FingerCertificationFlag certificationFlag) {
		this.certificationFlag = certificationFlag;
	}

	@Override
	public String toString() {
		return "\nRepresentation [RepresentationRecordLength=" + getRecordLength () + ", representationHeader=" + representationHeader + ", representationData="
				+ representationBody + "]\n";
	}
}
