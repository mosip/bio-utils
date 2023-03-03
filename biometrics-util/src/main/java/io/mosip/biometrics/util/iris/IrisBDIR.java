package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.mosip.biometrics.util.AbstractImageInfo;

public class IrisBDIR extends AbstractImageInfo {
	private GeneralHeader generalHeader;
	private int representationIndex = 0;
	private List<Representation> representation;

	public IrisBDIR(long formatIdentifier, long versionNumber, int certificationFlag, Date captureDate,
			int noOfRepresentations, IrisQualityBlock[] qualityBlocks, ImageInformation imageInformation,
			int representationNo, int noOfEyesPresent, byte[] image) {
		setRepresentation(new Representation(captureDate, qualityBlocks, imageInformation, representationNo, image));

		long totalRepresentationLength = getRepresentation().getRecordLength();
		setGeneralHeader(new GeneralHeader(formatIdentifier, versionNumber, totalRepresentationLength,
				noOfRepresentations, certificationFlag, noOfEyesPresent));
	}

	public IrisBDIR(long formatIdentifier, long versionNumber, int certificationFlag, int sourceType, int deviceVendor,
			int deviceType, Date captureDate, int noOfRepresentations, IrisQualityBlock[] qualityBlocks,
			ImageInformation imageInformation, int representationNo, int noOfEyesPresent, byte[] image) {
		setRepresentation(new Representation(captureDate, sourceType, deviceVendor, deviceType, qualityBlocks,
				imageInformation, representationNo, image));

		long totalRepresentationLength = getRepresentation().getRecordLength();
		setGeneralHeader(new GeneralHeader(formatIdentifier, versionNumber, totalRepresentationLength,
				noOfRepresentations, certificationFlag, noOfEyesPresent));
	}

	public IrisBDIR(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public IrisBDIR(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	public long getRecordLength() {
		return getGeneralHeader().getRecordLength() + (1 * getRepresentation().getRecordLength());
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setGeneralHeader(new GeneralHeader(inputStream));
		long generalHeaderLength = getGeneralHeader().getRecordLength();
		int noOfRepresentations = getGeneralHeader().getNoOfRepresentations();
		long totalRepresentationLength = getGeneralHeader().getTotalRepresentationLength();
		if (noOfRepresentations == 1) // For IRIS 1 Representation
		{
			setRepresentation(new Representation(inputStream));
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setGeneralHeader(new GeneralHeader(inputStream, onlyImageInformation));
		int noOfRepresentations = getGeneralHeader().getNoOfRepresentations();
		long totalRepresentationLength = getGeneralHeader().getTotalRepresentationLength();
		if (noOfRepresentations == 1) // For IRIS 1 Representation
		{
			setRepresentation(new Representation(inputStream, onlyImageInformation));
		}
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		getGeneralHeader().writeObject(outputStream);
		getRepresentation().writeObject(outputStream);
		outputStream.flush();
	}

	public GeneralHeader getGeneralHeader() {
		return generalHeader;
	}

	public void setGeneralHeader(GeneralHeader generalHeader) {
		this.generalHeader = generalHeader;
	}

	public Representation getRepresentation() {
		if (this.representation == null)
			return null;
		return representation.get(0);
	}

	public Representation getRepresentation(int representationIndex) {
		if (this.representation == null)
			return null;
		return representation.get(representationIndex);
	}

	public void setRepresentation(Representation representation) {
		if (this.representation == null)
			this.representation = new ArrayList<Representation>();

		this.representation.add(representationIndex++, representation);
	}

	public void setRepresentation(Representation representation, int representationIndex) {
		if (this.representation == null)
			this.representation = new ArrayList<Representation>();

		this.representation.add(representationIndex, representation);
	}

	public long getFormatIdentifier() {
		return getGeneralHeader().getFormatIdentifier();
	}

	public long getVersionNumber() {
		return getGeneralHeader().getVersionNumber();
	}

	public int getCertificationFlag() {
		return getGeneralHeader().getCertificationFlag();
	}

	public long getRepresentationsLength() {
		return getRepresentation().getRecordLength();
	}

	public int getCaptureDeviceTechnologyIdentifier() {
		return getRepresentation().getRepresentationHeader().getCaptureDeviceTechnologyIdentifier();
	}

	public int getCaptureDeviceVendorIdentifier() {
		return getRepresentation().getRepresentationHeader().getCaptureDeviceVendorIdentifier();
	}

	public int getCaptureDeviceTypeIdentifier() {
		return getRepresentation().getRepresentationHeader().getCaptureDeviceTypeIdentifier();
	}

	public Date getCaptureDateTime() {
		return getRepresentation().getRepresentationHeader().getCaptureDateTime();
	}

	public int getCaptureYear() {
		return getRepresentation().getRepresentationHeader().getCaptureYear();
	}

	public int getCaptureMonth() {
		return getRepresentation().getRepresentationHeader().getCaptureMonth();
	}

	public int getCaptureDay() {
		return getRepresentation().getRepresentationHeader().getCaptureDay();
	}

	public int getCaptureHour() {
		return getRepresentation().getRepresentationHeader().getCaptureHour();
	}

	public int getCaptureMinute() {
		return getRepresentation().getRepresentationHeader().getCaptureMinute();
	}

	public int getCaptureSecond() {
		return getRepresentation().getRepresentationHeader().getCaptureSecond();
	}

	public int getCaptureMilliSecond() {
		return getRepresentation().getRepresentationHeader().getCaptureMilliSecond();
	}

	public int getNoOfRepresentations() {
		return getGeneralHeader().getNoOfRepresentations();
	}

	public int getRepresentationNo() {
		return getRepresentation().getRepresentationHeader().getRepresentationNo();
	}

	public int getNoOfEyesPresent() {
		return getGeneralHeader().getNoOfEyesPresent();
	}

	public int getEyeLabel() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getEyeLabel();
	}

	public int getImageType() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getImageType();
	}

	public int getImageFormat() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getImageFormat();
	}

	public int getHorizontalOrientation() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getHorizontalOrientation();
	}

	public int getVerticalOrientation() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getVerticalOrientation();
	}

	public int getCompressionType() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getCompressionType();
	}

	public int getWidth() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getWidth();
	}

	public int getHeight() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getHeight();
	}

	public int getBitDepth() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getBitDepth();
	}

	public int getRange() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getRange();
	}

	public int getRollAngleOfEye() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getRollAngleOfEye();
	}

	public int getRollAngleUncertainty() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getRollAngleUncertainty();
	}

	public int getIrisCenterSmallestX() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterSmallestX();
	}

	public int getIrisCenterLargestX() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterLargestX();
	}

	public int getIrisCenterSmallestY() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterSmallestY();
	}

	public int getIrisCenterLargestY() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterLargestY();
	}

	public int getIrisDiameterSmallest() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getIrisDiameterSmallest();
	}

	public int getIrisDiameterLargest() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getIrisDiameterLargest();
	}

	public int getNoOfQualityBlocks() {
		return getRepresentation().getRepresentationHeader().getNoOfQualityBlocks();
	}

	public IrisQualityBlock[] getQualityBlocks() {
		return getRepresentation().getRepresentationHeader().getQualityBlocks();
	}

	public long getImageLength() {
		return getRepresentation().getRepresentationData().getImageData().getImageLength();
	}

	public byte[] getImage() {
		return getRepresentation().getRepresentationData().getImageData().getImage();
	}

	@Override
	public String toString() {
		return "\nIrisBDIR [generalHeader=" + generalHeader + ", representation=" + representation + "]\n";
	}
}
