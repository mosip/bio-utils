package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationData extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationData.class);

	private int fingerPosition;
	private int annotationCode;

	public AnnotationData() {
		setFingerPosition(FingerPosition.UNKNOWN);
		setAnnotationCode(AnnotationCode.AMPUTATED_FINGER);
	}

	public AnnotationData(int fingerPosition, int annotationCode) {
		setFingerPosition(fingerPosition);
		setAnnotationCode(annotationCode);
	}

	public AnnotationData(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public AnnotationData(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setFingerPosition(inputStream.readUnsignedByte());
		setAnnotationCode(inputStream.readUnsignedByte());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// skip at the base calling class

	}

	/* (1 + 1 ) (Table 13 Annotation data ISO/IEC 19794-4-2011) */
	@Override
	public long getRecordLength() {
		return (1 + 1);
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeByte(getFingerPosition()); /* 1 = 1 */
		outputStream.writeByte(getAnnotationCode()); /* + 1 = 2 */
		outputStream.flush();
	}

	public int getFingerPosition() {
		return fingerPosition;
	}

	public void setFingerPosition(int fingerPosition) {
		this.fingerPosition = fingerPosition;
	}

	public int getAnnotationCode() {
		return annotationCode;
	}

	public void setAnnotationCode(int annotationCode) {
		this.annotationCode = annotationCode;
	}

	@Override
	public String toString() {
		return "\nAnnotationData [RecordLength=" + getRecordLength() + ", fingerPosition="
				+ Integer.toHexString(fingerPosition) + ", annotationCode=" + Integer.toHexString(annotationCode)
				+ "]\n";
	}
}
