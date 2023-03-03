package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationBlock extends ExtendedDataBlock {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBlock.class);

	private int noOfAnnotationData;
	private AnnotationData[] annotationData;

	public AnnotationBlock(int noOfAnnotationData, AnnotationData[] annotationData) {
		setNoOfAnnotationData(noOfAnnotationData);
		setAnnotationData(annotationData);

		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);
		setLengthOfExtendedDataBlock((int) getRecordLength());
	}

	public AnnotationBlock(DataInputStream inputStream) throws IOException {
		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);
		readObject(inputStream);
	}

	public AnnotationBlock(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setExtendedDataBlockIdentificationCode(ExtendedDataBlockIdentificationCode.ANNOTATION);
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setLengthOfExtendedDataBlock(inputStream.readUnsignedShort());
		setNoOfAnnotationData(inputStream.readUnsignedByte());
		if (getNoOfAnnotationData() > 0) {
			AnnotationData[] annotationData = new AnnotationData[getNoOfAnnotationData()];
			for (int index = 0; index < getNoOfAnnotationData(); index++) {
				annotationData[index] = new AnnotationData(inputStream);
			}
			setAnnotationData(annotationData);
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// Skip at calling class		
	}

	/* 1 + (Table 13 Annotation data ISO/IEC 19794-4-2011) */
	@Override
	public long getRecordLength() {
		int recordLength = 0;
		if (getNoOfAnnotationData() > 0) {
			for (int index = 0; index < getNoOfAnnotationData(); index++) {
				recordLength += getAnnotationData()[index].getRecordLength();
			}
		}

		return (2 + 2 + 1 + recordLength);
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeShort(getExtendedDataBlockIdentificationCode()); /* 2 = 2 */
		outputStream.writeShort(getLengthOfExtendedDataBlock()); /* + 2 = 4 */

		outputStream.writeByte(getNoOfAnnotationData()); /* + 1 = 5 */
		if (getNoOfAnnotationData() > 0) {
			for (int index = 0; index < getNoOfAnnotationData(); index++) {
				getAnnotationData()[index].writeObject(outputStream);
			}
		}
		outputStream.flush();
	}

	public int getNoOfAnnotationData() {
		return noOfAnnotationData;
	}

	public void setNoOfAnnotationData(int noOfAnnotationData) {
		this.noOfAnnotationData = noOfAnnotationData;
	}

	public AnnotationData[] getAnnotationData() {
		return annotationData;
	}

	public void setAnnotationData(AnnotationData[] annotationData) {
		this.annotationData = annotationData;
	}

	@Override
	public String toString() {
		super.toString();

		return "\nAnnotationBlock [RecordLength=" + getRecordLength() + ", noOfAnnotationData=" + noOfAnnotationData
				+ ", annotationData=" + Arrays.toString(annotationData) + "]\n";
	}

}
