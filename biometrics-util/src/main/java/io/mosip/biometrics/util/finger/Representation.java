package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;

public class Representation extends AbstractImageInfo {
	private RepresentationHeader representationHeader;
	private RepresentationBody representationBody;
	private int certificationFlag;

	@SuppressWarnings({ "java:S107" })
	public Representation(Date captureDate, FingerQualityBlock[] qualityBlocks, int certificationFlag,
			FingerCertificationBlock[] certificationBlocks, int fingerPosition, int representationNo, int scaleUnitType,
			byte[] image) {
		setCertificationFlag(certificationFlag);
		setRepresentationBody(new RepresentationBody(new ImageData(image), null, null, null));
		setRepresentationHeader(
				new RepresentationHeader(getRepresentationBody().getRecordLength(), captureDate, qualityBlocks,
						certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType));
	}

	@SuppressWarnings({ "java:S107" })
	public Representation(Date captureDate, int captureDeviceTechnologyIdentifier, int captureDeviceVendorIdentifier,
			int captureDeviceTypeIdentifier, FingerQualityBlock[] qualityBlocks, int certificationFlag,
			FingerCertificationBlock[] certificationBlocks, int fingerPosition, int representationNo, int scaleUnitType,
			int captureDeviceSpatialSamplingRateHorizontal, int captureDeviceSpatialSamplingRateVertical,
			int imageSpatialSamplingRateHorizontal, int imageSpatialSamplingRateVertical, int bitDepth,
			int compressionType, int impressionType, int lineLengthHorizontal, int lineLengthVertical, byte[] image,
			SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock, CommentBlock[] commentBlocks) {
		setCertificationFlag(certificationFlag);
		setRepresentationBody(
				new RepresentationBody(new ImageData(image), segmentationBlock, annotationBlock, commentBlocks));
		setRepresentationHeader(new RepresentationHeader(getRepresentationBody().getRecordLength(), captureDate,
				captureDeviceTechnologyIdentifier, captureDeviceVendorIdentifier, captureDeviceTypeIdentifier,
				qualityBlocks, certificationFlag, certificationBlocks, fingerPosition, representationNo, scaleUnitType,
				captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical,
				imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical, bitDepth, compressionType,
				impressionType, lineLengthHorizontal, lineLengthVertical));
	}

	public Representation(DataInputStream inputStream, int certificationFlag) throws IOException {
		setCertificationFlag(certificationFlag);
		readObject(inputStream);
	}

	public Representation(DataInputStream inputStream, int certificationFlag, boolean onlyImageInformation) throws IOException {
		setCertificationFlag(certificationFlag);
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setRepresentationHeader(new RepresentationHeader(inputStream, getCertificationFlag()));
		setRepresentationBody(new RepresentationBody(inputStream));
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setRepresentationHeader(new RepresentationHeader(inputStream, getCertificationFlag(), onlyImageInformation));
		setRepresentationBody(new RepresentationBody(inputStream, onlyImageInformation));
	}

	@Override
	public long getRecordLength() {
		return getRepresentationHeader().getRecordLength() + getRepresentationBody().getRecordLength();
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		getRepresentationHeader().writeObject(outputStream);
		getRepresentationBody().writeObject(outputStream);
		outputStream.flush();
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

	public int getCertificationFlag() {
		return certificationFlag;
	}

	public void setCertificationFlag(int certificationFlag) {
		this.certificationFlag = certificationFlag;
	}

	@Override
	public String toString() {
		return "\nRepresentation [RepresentationRecordLength=" + getRecordLength() + ", representationHeader="
				+ representationHeader + ", representationData=" + representationBody + "]\n";
	}
}
