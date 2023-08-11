package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.mosip.biometrics.util.AbstractImageInfo;

public class FaceBDIR extends AbstractImageInfo {
	private GeneralHeader generalHeader;
	private int representationIndex = 0;
	private List<Representation> representation;

	public FaceBDIR(long formatIdentifier, long versionNumber, int certificationFlag, int temporalSemantics,
			Date captureDate, int noOfRepresentations, FaceQualityBlock[] qualityBlocks,
			FacialInformation facialInformation, LandmarkPoints[] landmarkPoints, ImageInformation imageInformation,
			byte[] image, byte[] threeDInformationAndData) {
		setRepresentation(new Representation(captureDate, qualityBlocks, facialInformation, landmarkPoints,
				imageInformation, image, threeDInformationAndData));

		long totalRepresentationLength = getRepresentation().getRecordLength();
		setGeneralHeader(new GeneralHeader(formatIdentifier, versionNumber, totalRepresentationLength,
				noOfRepresentations, certificationFlag, temporalSemantics));
	}

	public FaceBDIR(long formatIdentifier, long versionNumber, int certificationFlag, int temporalSemantics,
			int sourceType, int deviceVendor, int deviceType, Date captureDate, int noOfRepresentations,
			FaceQualityBlock[] qualityBlocks, FacialInformation facialInformation, LandmarkPoints[] landmarkPoints,
			ImageInformation imageInformation, byte[] image, byte[] threeDInformationAndData) {
		setRepresentation(new Representation(captureDate, sourceType, deviceVendor, deviceType, qualityBlocks,
				facialInformation, landmarkPoints, imageInformation, image, threeDInformationAndData));

		long totalRepresentationLength = getRepresentation().getRecordLength();
		setGeneralHeader(new GeneralHeader(formatIdentifier, versionNumber, totalRepresentationLength,
				noOfRepresentations, certificationFlag, temporalSemantics));
	}

	public FaceBDIR(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public FaceBDIR(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setGeneralHeader(new GeneralHeader(inputStream));
		if (getGeneralHeader().getNoOfRepresentations() == 1) // For FACE 1 Representation
		{
			setRepresentation(new Representation(inputStream));
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setGeneralHeader(new GeneralHeader(inputStream, onlyImageInformation));
		if (getGeneralHeader().getNoOfRepresentations() == 1) // For FACE 1 Representation
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

	@Override
	public long getRecordLength() {
		return getGeneralHeader().getRecordLength() + (1 * getRepresentation().getRecordLength());
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
			this.representation = new ArrayList<>();

		this.representation.add(representationIndex++, representation);
	}

	public void setRepresentation(Representation representation, int representationIndex) {
		if (this.representation == null)
			this.representation = new ArrayList<>();

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

	public int getTemporalSemantics() {
		return getGeneralHeader().getTemporalSemantics();
	}

	public long getRepresentationsLength() {
		return getRepresentation().getRecordLength();
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

	public int getNoOfLandMarkPoints() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getNoOfLandMarkPoints();
	}

	public int getGender() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getGender();
	}

	public int getEyeColor() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getEyeColor();
	}

	public int getHairColor() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getHairColor();
	}

	public int getSubjectHeight() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getSubjectHeight();
	}

	public int getExpressionMask() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getExpressionMask();
	}

	public int getFeaturesMask() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getFeaturesMask();
	}

	public int[] getPoseAngle() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getPoseAngle();
	}

	public int[] getPoseAngleUncertainty() {
		return getRepresentation().getRepresentationHeader().getFacialInformation().getPoseAngleUncertainty();
	}

	public int getFaceImageType() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getFaceImageType();
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

	public int getNoOfQualityBlocks() {
		return getRepresentation().getRepresentationHeader().getNoOfQualityBlocks();
	}

	public FaceQualityBlock[] getQualityBlocks() {
		return getRepresentation().getRepresentationHeader().getQualityBlocks();
	}

	public int getImageDataType() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getImageDataType();
	}

	public int getWidth() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getWidth();
	}

	public int getHeight() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getHeight();
	}

	public int getSpatialSamplingRateLevel() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getSpatialSamplingRateLevel();
	}

	public int getPostAcquistionProcessing() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getPostAcquistionProcessing();
	}

	public int getCrossReference() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getCrossReference();
	}

	public int getImageColorSpace() {
		return getRepresentation().getRepresentationHeader().getImageInformation().getImageColorSpace();
	}

	public LandmarkPoints[] getLandmarkPoints() {
		return getRepresentation().getRepresentationHeader().getLandmarkPoints();
	}

	public long getImageLength() {
		return getRepresentation().getRepresentationData().getImageData().getImageLength();
	}

	public byte[] getImage() {
		return getRepresentation().getRepresentationData().getImageData().getImage();
	}

	public byte[] getThreeDInformationAndData() {
		return getRepresentation().getRepresentationData().getThreeDInformationAndData();
	}

	@Override
	public String toString() {
		return "\nFaceBDIR [generalHeader=" + generalHeader + ", representation=" + representation + "]\n";
	}
}
