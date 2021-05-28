package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;

public class FingerBDIR extends AbstractImageInfo 
{
	private GeneralHeader generalHeader;
    private Representation representation;

    public FingerBDIR 
    	(
			FingerFormatIdentifier formatIdentifier, FingerVersionNumber versionNumber,
			FingerCertificationFlag certificationFlag, Date captureDate, int noOfRepresentations,
			FingerQualityBlock [] qualityBlocks,  FingerCertificationBlock[] certificationBlocks, 
    		FingerPosition fingerPosition, int representationNo, 
    		FingerScaleUnitType scaleUnitType, int noOfFingerPresent,
			byte [] image
		)
    {
    	setRepresentation (new Representation (captureDate, qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType, image));

    	int totalRepresentationLength = getRepresentation().getRecordLength ();
        setGeneralHeader (new GeneralHeader (formatIdentifier, versionNumber, totalRepresentationLength, noOfRepresentations, certificationFlag, noOfFingerPresent));
    }

    public FingerBDIR 
	(
		FingerFormatIdentifier formatIdentifier, FingerVersionNumber versionNumber,
		FingerCertificationFlag certificationFlag, 
		FingerCaptureDeviceTechnology sourceType, FingerCaptureDeviceVendor deviceVendor, FingerCaptureDeviceType deviceType,
		Date captureDate, int noOfRepresentations,
		FingerQualityBlock [] qualityBlocks, FingerCertificationBlock[] certificationBlocks, 
		FingerPosition fingerPosition, int representationNo, 
		FingerScaleUnitType scaleUnitType, 
		int captureDeviceSpatialSamplingRateHorizontal, int captureDeviceSpatialSamplingRateVertical, 
		int imageSpatialSamplingRateHorizontal, int imageSpatialSamplingRateVertical,
		FingerImageBitDepth bitDepth, FingerImageCompressionType compressionType,
		FingerImpressionType impressionType, int lineLengthHorizontal, int lineLengthVertical,
		int noOfFingerPresent, byte [] image, 
		SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock, CommentBlock commentBlock
	)
	{
    	setRepresentation (new Representation (captureDate, 
    			sourceType, deviceVendor, deviceType,
        		qualityBlocks, certificationFlag, certificationBlocks, 
        		fingerPosition, representationNo, scaleUnitType, 
        		captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical, 
        		imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical,
        		bitDepth, compressionType, impressionType, lineLengthHorizontal, lineLengthVertical,
        		image, segmentationBlock, annotationBlock, commentBlock));
	
    	int totalRepresentationLength = getRepresentation().getRecordLength ();
	    setGeneralHeader (new GeneralHeader (formatIdentifier, versionNumber, totalRepresentationLength, noOfRepresentations, certificationFlag, noOfFingerPresent));
	}
    
    public FingerBDIR (DataInputStream inputStream) throws IOException
   	{
       	readObject(inputStream);
   	}

    public int getRecordLength ()
    {
        return getGeneralHeader().getRecordLength() + (1 * getRepresentation().getRecordLength());
    }

	protected void readObject(DataInputStream inputStream) throws IOException {
		setGeneralHeader (new GeneralHeader (inputStream));
		int generalHeaderLength = getGeneralHeader().getRecordLength();
		int noOfRepresentations = getGeneralHeader().getNoOfRepresentations();
		int totalRepresentationLength = getGeneralHeader().getTotalRepresentationLength();
		if (noOfRepresentations == 1) // For Finger 1 Representation
		{
			setRepresentation (new Representation (inputStream, getGeneralHeader().getCertificationFlag()));
		}
	}    
    public void writeObject (DataOutputStream outputStream) throws IOException
    {
    	getGeneralHeader().writeObject (outputStream);
    	getRepresentation().writeObject (outputStream);
        outputStream.flush ();
    }

	public GeneralHeader getGeneralHeader() {
		return generalHeader;
	}

	public void setGeneralHeader(GeneralHeader generalHeader) {
		this.generalHeader = generalHeader;
	}

	public Representation getRepresentation() {
		return representation;
	}

	public void setRepresentation(Representation representation) {
		this.representation = representation;
	}

	@Override
	public String toString() {
		return "\nFingerBDIR [generalHeader=" + generalHeader + ", representation=" + representation + "]\n";
	}	
}
