package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.mosip.biometrics.util.AbstractImageInfo;

public class FingerBDIR extends AbstractImageInfo {
	private GeneralHeader generalHeader;
	private int representationIndex = 0;
	private List<Representation> representation;

	public FingerBDIR(long formatIdentifier, long versionNumber, int certificationFlag, Date captureDate,
			int noOfRepresentations, FingerQualityBlock[] qualityBlocks, FingerCertificationBlock[] certificationBlocks,
			int fingerPosition, int representationNo, int scaleUnitType, int noOfFingerPresent, byte[] image) {
		setRepresentation(new Representation(captureDate, qualityBlocks, certificationFlag, certificationBlocks,
				fingerPosition, representationNo, scaleUnitType, image));

		long totalRepresentationLength = getRepresentation().getRecordLength();
		setGeneralHeader(new GeneralHeader(formatIdentifier, versionNumber, totalRepresentationLength,
				noOfRepresentations, certificationFlag, noOfFingerPresent));
	}

	public FingerBDIR(long formatIdentifier, long versionNumber, int certificationFlag, int sourceType,
			int deviceVendor, int deviceType, Date captureDate, int noOfRepresentations,
			FingerQualityBlock[] qualityBlocks, FingerCertificationBlock[] certificationBlocks, int fingerPosition,
			int representationNo, int scaleUnitType, int captureDeviceSpatialSamplingRateHorizontal,
			int captureDeviceSpatialSamplingRateVertical, int imageSpatialSamplingRateHorizontal,
			int imageSpatialSamplingRateVertical, int bitDepth, int compressionType, int impressionType,
			int lineLengthHorizontal, int lineLengthVertical, int noOfFingerPresent, byte[] image,
			SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock, CommentBlock[] commentBlocks) {
		setRepresentation(new Representation(captureDate, sourceType, deviceVendor, deviceType, qualityBlocks,
				certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType,
				captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical,
				imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical, bitDepth, compressionType,
				impressionType, lineLengthHorizontal, lineLengthVertical, image, segmentationBlock, annotationBlock,
				commentBlocks));

		long totalRepresentationLength = getRepresentation().getRecordLength();
		setGeneralHeader(new GeneralHeader(formatIdentifier, versionNumber, totalRepresentationLength,
				noOfRepresentations, certificationFlag, noOfFingerPresent));
	}

	public FingerBDIR(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public FingerBDIR(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	public long getRecordLength() {
		return getGeneralHeader().getRecordLength() + (1 * getRepresentation().getRecordLength());
	}

	protected void readObject(DataInputStream inputStream) throws IOException {
		setGeneralHeader(new GeneralHeader(inputStream));
		int noOfRepresentations = getGeneralHeader().getNoOfRepresentations();
		if (noOfRepresentations == 1) // For Finger 1 Representation
		{
			setRepresentation(new Representation(inputStream, getGeneralHeader().getCertificationFlag()));
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setGeneralHeader(new GeneralHeader(inputStream, onlyImageInformation));
		int noOfRepresentations = getGeneralHeader().getNoOfRepresentations();
		if (noOfRepresentations == 1) // For Finger 1 Representation
		{
			setRepresentation(new Representation(inputStream, getGeneralHeader().getCertificationFlag(), onlyImageInformation));
		}
	}

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

	public long getRepresentationsLength() {
		return getRepresentation().getRecordLength();
	}

	public int getNoOfQualityBlocks() {
		return getRepresentation().getRepresentationHeader().getNoOfQualityBlocks();
	}

	public FingerQualityBlock[] getQualityBlocks() {
		return getRepresentation().getRepresentationHeader().getQualityBlocks();
	}

	public int getNoOfCertificationBlocks() {
		return getRepresentation().getRepresentationHeader().getNoOfCertificationBlocks();
	}

	public FingerCertificationBlock[] getCertificationBlocks() {
		return getRepresentation().getRepresentationHeader().getCertificationBlocks();
	}

	public int getFingerPosition() {
		return getRepresentation().getRepresentationHeader().getFingerPosition();
	}

	public int getRepresentationNo() {
		return getRepresentation().getRepresentationHeader().getRepresentationNo();
	}

	public int getScaleUnits() {
		return getRepresentation().getRepresentationHeader().getScaleUnits();
	}

	public int getCaptureDeviceSpatialSamplingRateHorizontal() {
		return getRepresentation().getRepresentationHeader().getCaptureDeviceSpatialSamplingRateHorizontal();
	}

	public int getCaptureDeviceSpatialSamplingRateVertical() {
		return getRepresentation().getRepresentationHeader().getCaptureDeviceSpatialSamplingRateVertical();
	}

	public int getImageSpatialSamplingRateHorizontal() {
		return getRepresentation().getRepresentationHeader().getImageSpatialSamplingRateHorizontal();
	}

	public int getImageSpatialSamplingRateVertical() {
		return getRepresentation().getRepresentationHeader().getImageSpatialSamplingRateVertical();
	}

	public int getBitDepth() {
		return getRepresentation().getRepresentationHeader().getBitDepth();
	}

	public int getCompressionType() {
		return getRepresentation().getRepresentationHeader().getCompressionType();
	}

	public int getImpressionType() {
		return getRepresentation().getRepresentationHeader().getImpressionType();
	}

	public int getLineLengthHorizontal() {
		return getRepresentation().getRepresentationHeader().getLineLengthHorizontal();
	}

	public int getLineLengthVertical() {
		return getRepresentation().getRepresentationHeader().getLineLengthVertical();
	}

	public int getNoOfFingerPresent() {
		return getGeneralHeader().getNoOfFingerPresent();
	}

	public SegmentationBlock getSegmentationBlock() {
		return getRepresentation().getRepresentationBody().getSegmentationBlock();
	}

	public AnnotationBlock getAnnotationBlock() {
		return getRepresentation().getRepresentationBody().getAnnotationBlock();
	}

	public CommentBlock[] getCommentBlocks() {
		return getRepresentation().getRepresentationBody().getCommentBlocks();
	}

	public long getImageLength() {
		return getRepresentation().getRepresentationBody().getImageData().getImageLength();
	}

	public byte[] getImage() {
		return getRepresentation().getRepresentationBody().getImageData().getImage();
	}

	@Override
	public String toString() {
		return "\nFingerBDIR [generalHeader=" + generalHeader + ", representation=" + representation + "]\n";
	}
}