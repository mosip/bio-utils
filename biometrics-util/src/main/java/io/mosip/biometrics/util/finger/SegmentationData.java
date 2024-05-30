package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.mosip.biometrics.util.AbstractImageInfo;

public class SegmentationData extends AbstractImageInfo {
	private int fingerPosition;
	private int qualityScore;
	private int noOfCoordinates;
	private int[] xCoordinates;
	private int[] yCoordinates;
	private int fingerOrientation;

	public SegmentationData(int qualityScore) {
		setFingerPosition(FingerPosition.UNKNOWN);
		setQualityScore(qualityScore);
		setNoOfCoordinates(2);// Default 2 or More
		setXCoordinates(new int[2]);
		setYCoordinates(new int[2]);
		for (int index = 0; index < getNoOfCoordinates(); index++) {
			this.getXCoordinates()[index] = 0;
			this.getYCoordinates()[index] = 0;
		}
		setFingerOrientation(0);
	}

	public SegmentationData(int fingerPosition, int qualityScore, int noOfCoordinates, int[] xCoordinates,
			int[] yCoordinates, int fingerOrientation) {
		setFingerPosition(fingerPosition);
		setQualityScore(qualityScore);
		setNoOfCoordinates(noOfCoordinates);// Default 2 or More
		setXCoordinates(xCoordinates);
		setYCoordinates(yCoordinates);
		setFingerOrientation(fingerOrientation);
	}

	public SegmentationData(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public SegmentationData(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setFingerPosition(inputStream.readUnsignedByte());
		setQualityScore(inputStream.readUnsignedByte());
		setNoOfCoordinates(inputStream.readUnsignedByte());// Default 2 or More
		setXCoordinates(new int[getNoOfCoordinates()]);
		setYCoordinates(new int[getNoOfCoordinates()]);
		for (int index = 0; index < getNoOfCoordinates(); index++) {
			this.getXCoordinates()[index] = inputStream.readUnsignedShort();
			this.getYCoordinates()[index] = inputStream.readUnsignedShort();
		}
		setFingerOrientation(inputStream.readUnsignedByte());
	}

	@Override
	@SuppressWarnings({ "java:S2674" })
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// 1(FingerPosition) + 1(QualityScore)
		inputStream.skip(2);
		setNoOfCoordinates(inputStream.readUnsignedByte());// Default 2 or More
		for (int index = 0; index < getNoOfCoordinates(); index++) {
			inputStream.skip(4);
		}
		// 1(FingerOrientation)
		inputStream.skip(1);
	}
	/*
	 * (1 + 1 + 1 + (NoOfCoordinates * 4) + 1 ) (Table 12 Segmentation data
	 * ISO/IEC 19794-4-2011)
	 */
	@Override
	public long getRecordLength() {
		long recordLength = 0;
		if (getNoOfCoordinates() > 0) {
			for (int index = 0; index < getNoOfCoordinates(); index++)
				recordLength += (2 + 2);
		}

		return (1 + 1 + 1 + recordLength + 1);
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeByte(getFingerPosition()); /* 1 = 1 */
		outputStream.writeByte(getQualityScore()); /* + 1 = 2 */
		outputStream.writeByte(getNoOfCoordinates()); /* + 1 = 3 */
		for (int index = 0; index < getNoOfCoordinates(); index++) {
			outputStream.writeShort(this.getXCoordinates()[index]); /* + 2 = 5 */
			outputStream.writeShort(this.getYCoordinates()[index]); /* + 2 = 7 */
		}
		outputStream.writeByte(getFingerOrientation()); /* + 1 = 8 */
		outputStream.flush();
	}

	public int getFingerPosition() {
		return fingerPosition;
	}

	public void setFingerPosition(int fingerPosition) {
		this.fingerPosition = fingerPosition;
	}

	public int getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(int qualityScore) {
		this.qualityScore = qualityScore;
	}

	public int getNoOfCoordinates() {
		return noOfCoordinates;
	}

	public void setNoOfCoordinates(int noOfCoordinates) {
		this.noOfCoordinates = noOfCoordinates;
	}

	public int[] getXCoordinates() {
		return xCoordinates;
	}

	public void setXCoordinates(int[] xCoordinates) {
		this.xCoordinates = xCoordinates;
	}

	public int[] getYCoordinates() {
		return yCoordinates;
	}

	public void setYCoordinates(int[] yCoordinates) {
		this.yCoordinates = yCoordinates;
	}

	public int getFingerOrientation() {
		return fingerOrientation;
	}

	public void setFingerOrientation(int fingerOrientation) {
		this.fingerOrientation = fingerOrientation;
	}

	@Override
	public String toString() {
		return "\nSegmentationData [RecordLength=" + getRecordLength() + ", fingerPosition="
				+ Integer.toHexString(fingerPosition) + ", qualityScore="
				+ Integer.toHexString(qualityScore) + ", noOfCoordinates="
				+ Integer.toHexString(noOfCoordinates) + ", XCoordinates=" + Arrays.toString(xCoordinates)
				+ ", YCoordinates=" + Arrays.toString(yCoordinates) + ", fingerOrientation="
				+ Integer.toHexString(fingerOrientation) + "]\n";
	}
}
