package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;

public class FaceBDIR extends AbstractImageInfo 
{
	private GeneralHeader generalHeader;
    private Representation representation;

    public FaceBDIR 
    	(
			FaceFormatIdentifier formatIdentifier, FaceVersionNumber versionNumber,
			FaceCertificationFlag certificationFlag, TemporalSequenceFlags temporalSemantics,
			Date captureDate, int noOfRepresentations,
			FaceQualityBlock [] qualityBlocks, FacialInformation facialInformation, 
			LandmarkPoints [] landmarkPoints, ImageInformation imageInformation, 
			byte [] image
		)
    {
    	setRepresentation (new Representation (captureDate, qualityBlocks, facialInformation, landmarkPoints, imageInformation, image));

    	int totalRepresentationLength = getRepresentation().getRecordLength ();
        setGeneralHeader (new GeneralHeader (formatIdentifier, versionNumber, totalRepresentationLength, noOfRepresentations, certificationFlag, temporalSemantics));
    }

    public FaceBDIR 
	(
		FaceFormatIdentifier formatIdentifier, FaceVersionNumber versionNumber,
		FaceCertificationFlag certificationFlag, TemporalSequenceFlags temporalSemantics,
		FaceCaptureDeviceTechnology sourceType, FaceCaptureDeviceVendor deviceVendor, FaceCaptureDeviceType deviceType,
		Date captureDate, int noOfRepresentations,
		FaceQualityBlock [] qualityBlocks, FacialInformation facialInformation, 
		LandmarkPoints [] landmarkPoints, ImageInformation imageInformation, 
		byte [] image
	)
	{
    	setRepresentation (new Representation (captureDate, sourceType, deviceVendor, deviceType, qualityBlocks, facialInformation, landmarkPoints, imageInformation, image));
	
    	int totalRepresentationLength = getRepresentation().getRecordLength ();
	    setGeneralHeader (new GeneralHeader (formatIdentifier, versionNumber, totalRepresentationLength, noOfRepresentations, certificationFlag, temporalSemantics));
	}
    
    public FaceBDIR (DataInputStream inputStream) throws IOException
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
		if (noOfRepresentations == 1) // For FACE 1 Representation
		{
			setRepresentation (new Representation (inputStream));
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
		return "\nFaceBDIR [generalHeader=" + generalHeader + ", representation=" + representation + "]\n";
	}	
}
