package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;

public class IrisBDIR extends AbstractImageInfo 
{
	private GeneralHeader generalHeader;
    private Representation representation;

    public IrisBDIR 
    	(
			IrisFormatIdentifier formatIdentifier, IrisVersionNumber versionNumber,
			IrisCertificationFlag certificationFlag, Date captureDate, int noOfRepresentations,
			IrisQualityBlock [] qualityBlocks, ImageInformation imageInformation, int representationNo, int noOfEyesPresent,
			byte [] image
		)
    {
    	setRepresentation (new Representation (captureDate, qualityBlocks, imageInformation, representationNo, image));

    	int totalRepresentationLength = getRepresentation().getRecordLength ();
        setGeneralHeader (new GeneralHeader (formatIdentifier, versionNumber, totalRepresentationLength, noOfRepresentations, certificationFlag, noOfEyesPresent));
    }

    public IrisBDIR 
	(
		IrisFormatIdentifier formatIdentifier, IrisVersionNumber versionNumber,
		IrisCertificationFlag certificationFlag, 
		IrisCaptureDeviceTechnology sourceType, IrisCaptureDeviceVendor deviceVendor, IrisCaptureDeviceType deviceType,
		Date captureDate, int noOfRepresentations,
		IrisQualityBlock [] qualityBlocks, ImageInformation imageInformation, 
		int representationNo, int noOfEyesPresent,
		byte [] image
	)
	{
    	setRepresentation (new Representation (captureDate, sourceType, deviceVendor, deviceType, qualityBlocks, imageInformation, representationNo, image));
	
    	int totalRepresentationLength = getRepresentation().getRecordLength ();
	    setGeneralHeader (new GeneralHeader (formatIdentifier, versionNumber, totalRepresentationLength, noOfRepresentations, certificationFlag, noOfEyesPresent));
	}
    
    public IrisBDIR (DataInputStream inputStream) throws IOException
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
		if (noOfRepresentations == 1) // For IRIS 1 Representation
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
		return "\nIrisBDIR [generalHeader=" + generalHeader + ", representation=" + representation + "]\n";
	}	
}
