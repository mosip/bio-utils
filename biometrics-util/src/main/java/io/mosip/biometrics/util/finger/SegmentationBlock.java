package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentationBlock extends ExtendedDataBlock {
	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentationBlock.class);

	private int segmentationQualityScore;
	private int segmentationAlgorithmVendorIdentifier;
	private int segmentationAlgorithmIdentifier;

	private int qualityAlgorithmIdentifier;
	private int qualityAlgorithmVendorIdentifier;

	private int noOfSegmentationData;
	private SegmentationData[] segmentationData;

	public SegmentationBlock(int segmentationQualityScore, int noOfSegmentationData,
			SegmentationData[] segmentationData) {
		setSegmentationAlgorithmVendorIdentifier(FingerSegmentationAlgorithmVendorIdentifier.UNSPECIFIED);
		setSegmentationAlgorithmIdentifier(FingerSegmentationAlgorithmIdentifier.UNSPECIFIED);
		setSegmentationQualityScore(segmentationQualityScore);

		setQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.UNSPECIFIED);
		setQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED);

		setNoOfSegmentationData(noOfSegmentationData);
		setSegmentationData(segmentationData);
		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.SEGMENTATION);
		setLengthOfExtendedDataBlock((int) getRecordLength());
	}

	public SegmentationBlock(int segmentationAlgorithmVendorIdentifier, int segmentationAlgorithmIdentifier,
			int segmentationQualityScore, int noOfSegmentationData, SegmentationData[] segmentationData) {
		setSegmentationAlgorithmVendorIdentifier(segmentationAlgorithmVendorIdentifier);
		setSegmentationAlgorithmIdentifier(segmentationAlgorithmIdentifier);
		setSegmentationQualityScore(segmentationQualityScore);

		setQualityAlgorithmIdentifier(qualityAlgorithmIdentifier);
		setQualityAlgorithmVendorIdentifier(qualityAlgorithmVendorIdentifier);

		setNoOfSegmentationData(noOfSegmentationData);
		setSegmentationData(segmentationData);
		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.SEGMENTATION);
		setLengthOfExtendedDataBlock((int) getRecordLength());
	}

	public SegmentationBlock(DataInputStream inputStream) throws IOException {
		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.SEGMENTATION);
		readObject(inputStream);
	}

	public SegmentationBlock(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.SEGMENTATION);
		readObject(inputStream, onlyImageInformation);
	}

	protected void readObject(DataInputStream inputStream) throws IOException {
		setLengthOfExtendedDataBlock(inputStream.readUnsignedShort());

		int segmentationAlgorithmVendorIdentifierInfo = inputStream.readUnsignedShort();
		try {
			setSegmentationAlgorithmVendorIdentifier(segmentationAlgorithmVendorIdentifierInfo);
		} catch (Exception ex) {
			LOGGER.error(
					"setSegmentationAlgorithmVendorIdentifier :: Not Defined :: segmentationAlgorithmVendorIdentifier :: {}",
					Integer.toHexString(segmentationAlgorithmVendorIdentifierInfo));
		}

		int segmentationAlgorithmIdentifierInfo = inputStream.readUnsignedShort();
		try {
			setSegmentationAlgorithmIdentifier(segmentationAlgorithmIdentifierInfo);
		} catch (Exception ex) {
			LOGGER.error("setSegmentationAlgorithmIdentifier :: Not Defined :: segmentationAlgorithmIdentifier :: {}",
					Integer.toHexString(segmentationAlgorithmIdentifierInfo));
		}

		setSegmentationQualityScore(inputStream.readUnsignedByte());

		int qualityAlgorithmIdentifierInfo = inputStream.readUnsignedShort();
		try {
			setQualityAlgorithmIdentifier(qualityAlgorithmIdentifierInfo);
		} catch (Exception ex) {
			LOGGER.error("setQualityAlgorithmIdentifier :: Not Defined :: qualityAlgorithmIdentifier :: {}",
					Integer.toHexString(qualityAlgorithmIdentifierInfo));
		}

		int qualityAlgorithmVendorIdentifierInfo = inputStream.readUnsignedShort();
		try {
			setQualityAlgorithmVendorIdentifier(qualityAlgorithmVendorIdentifierInfo);
		} catch (Exception ex) {
			LOGGER.error("setQualityAlgorithmVendorIdentifier :: Not Defined :: qualityAlgorithmVendorIdentifier :: {}",
					Integer.toHexString(qualityAlgorithmVendorIdentifierInfo));
		}

		setNoOfSegmentationData(inputStream.readUnsignedByte());
		if (getNoOfSegmentationData() > 0) {
			SegmentationData[] segmentationDataInfo = new SegmentationData[getNoOfSegmentationData()];
			for (int index = 0; index < getNoOfSegmentationData(); index++) {
				segmentationDataInfo[index] = new SegmentationData(inputStream);
			}
			setSegmentationData(segmentationDataInfo);
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// Skip as base calling class
	}

	/* 2 + 2 + 1 + 1 (Table 12 Segmentation data ISO/IEC 19794-4-2011) */
	public long getRecordLength() {
		long recordLength = 0;
		if (getNoOfSegmentationData() > 0) {
			for (int index = 0; index < getNoOfSegmentationData(); index++) {
				recordLength += getSegmentationData()[index].getRecordLength();
			}
		}

		return (2 + 2 + 2 + 2 + 1 + 2 + 2 + 1 + recordLength);
	}

	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeShort(getExtendedDataBlockIdentificationCode()); /* 2 = 2 */
		outputStream.writeShort(getLengthOfExtendedDataBlock()); /* + 2 = 4 */

		outputStream.writeShort(getSegmentationAlgorithmVendorIdentifier()); /* + 2 = 6 */
		outputStream.writeShort(getSegmentationAlgorithmIdentifier()); /* + 2 = 8 */
		outputStream.writeByte(getSegmentationQualityScore()); /* + 1 = 9 */

		outputStream.writeShort(getQualityAlgorithmIdentifier()); /* + 2 = 11 */
		outputStream.writeShort(getQualityAlgorithmVendorIdentifier()); /* + 2 = 13 */

		outputStream.writeByte(getNoOfSegmentationData()); /* + 1 = 14 */
		if (getNoOfSegmentationData() > 0) {
			for (int index = 0; index < getNoOfSegmentationData(); index++) {
				getSegmentationData()[index].writeObject(outputStream);
			}
		}
		outputStream.flush();
	}

	public int getSegmentationQualityScore() {
		return segmentationQualityScore;
	}

	public void setSegmentationQualityScore(int segmentationQualityScore) {
		this.segmentationQualityScore = segmentationQualityScore;
	}

	public int getSegmentationAlgorithmVendorIdentifier() {
		return segmentationAlgorithmVendorIdentifier;
	}

	public void setSegmentationAlgorithmVendorIdentifier(int segmentationAlgorithmVendorIdentifier) {
		this.segmentationAlgorithmVendorIdentifier = segmentationAlgorithmVendorIdentifier;
	}

	public int getSegmentationAlgorithmIdentifier() {
		return segmentationAlgorithmIdentifier;
	}

	public void setSegmentationAlgorithmIdentifier(int segmentationAlgorithmIdentifier) {
		this.segmentationAlgorithmIdentifier = segmentationAlgorithmIdentifier;
	}

	public int getQualityAlgorithmVendorIdentifier() {
		return qualityAlgorithmVendorIdentifier;
	}

	public void setQualityAlgorithmVendorIdentifier(int qualityAlgorithmVendorIdentifier) {
		this.qualityAlgorithmVendorIdentifier = qualityAlgorithmVendorIdentifier;
	}

	public int getQualityAlgorithmIdentifier() {
		return qualityAlgorithmIdentifier;
	}

	public void setQualityAlgorithmIdentifier(int qualityAlgorithmIdentifier) {
		this.qualityAlgorithmIdentifier = qualityAlgorithmIdentifier;
	}

	public int getNoOfSegmentationData() {
		return noOfSegmentationData;
	}

	public void setNoOfSegmentationData(int noOfSegmentationData) {
		this.noOfSegmentationData = noOfSegmentationData;
	}

	public SegmentationData[] getSegmentationData() {
		return segmentationData;
	}

	public void setSegmentationData(SegmentationData[] segmentationData) {
		this.segmentationData = segmentationData;
	}

	@Override
	public String toString() {
		super.toString();

		return "\nSegmentationBlock [RecordLength=" + getRecordLength() + ", segmentationAlgorithmVendorIdentifier="
				+ Integer.toHexString(segmentationAlgorithmVendorIdentifier) + ", segmentationAlgorithmIdentifier="
				+ Integer.toHexString(segmentationAlgorithmIdentifier) + ", segmentationQualityScore="
				+ Integer.toHexString(segmentationQualityScore) + ", qualityAlgorithmIdentifier="
				+ Integer.toHexString(qualityAlgorithmIdentifier) + ", qualityAlgorithmVendorIdentifier="
				+ Integer.toHexString(qualityAlgorithmVendorIdentifier) + ", noOfSegmentationData="
				+ Integer.toHexString(noOfSegmentationData) + ", segmentationData=" + Arrays.toString(segmentationData)
				+ "]\n";
	}
}