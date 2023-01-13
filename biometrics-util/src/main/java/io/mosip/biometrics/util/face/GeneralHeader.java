package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralHeader extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralHeader.class);

	private long formatIdentifier;
	private long versionNumber;
	private long totalRepresentationLength;
	private int noOfRepresentations;
	private int certificationFlag;
	private int temporalSemantics;

	public GeneralHeader(long totalRepresentationLength, int noOfRepresentations) {
		setFormatIdentifier(FaceFormatIdentifier.FORMAT_FAC);
		setVersionNumber(FaceVersionNumber.VERSION_030);
		setTotalRepresentationLength(totalRepresentationLength);
		setNoOfRepresentations(noOfRepresentations);
		setCertificationFlag(FaceCertificationFlag.UNSPECIFIED);
		setTemporalSemantics(TemporalSequenceFlags.ONE_REPRESENTATION);
	}

	public GeneralHeader(long formatIdentifier, long versionNumber,
			long totalRepresentationLength, int noOfRepresentations, int certificationFlag, int temporalSemantics) {
		setFormatIdentifier(formatIdentifier);
		setVersionNumber(versionNumber);
		setTotalRepresentationLength(totalRepresentationLength);
		setNoOfRepresentations(noOfRepresentations);
		setCertificationFlag(certificationFlag);
		setTemporalSemantics(temporalSemantics);
	}

	public GeneralHeader(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public GeneralHeader(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setFormatIdentifier(inputStream.readInt() & 0xFFFFFFFFL);
		setVersionNumber(inputStream.readInt() & 0xFFFFFFFFL);
		setTotalRepresentationLength(((inputStream.readInt() & 0xFFFFFFFFL) - getRecordLength()));
		setNoOfRepresentations(inputStream.readUnsignedShort());
		setCertificationFlag(inputStream.readUnsignedByte());
		setTemporalSemantics(inputStream.readUnsignedShort());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// 4(FormatIdentifier) + 4(VersionNumber) + 4(TotalRepresentationLength)
		inputStream.skip(12);
		setNoOfRepresentations(inputStream.readUnsignedShort());
		// 1(CertificationFlag) + 2(TemporalSemantics)
		inputStream.skip(3);
	}

	@Override
	public long getRecordLength() {
		return 17; /* 4 + 4 + 4 + 2 + 1 + 2 (table 2 ISO/IEC 19794-5) */
	}

	@Override
	protected void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt((int) getFormatIdentifier()); /* 4 */
		outputStream.writeInt((int) getVersionNumber()); /* + 4 = 8 */
		outputStream.writeInt((int) (getRecordLength() + getTotalRepresentationLength())); /* + 4 = 12 */
		outputStream.writeShort(getNoOfRepresentations()); /* + 2 = 14 */
		outputStream.writeByte(getCertificationFlag()); /* + 1 = 15 */
		outputStream.writeShort(getTemporalSemantics()); /* + 2 = 17 */
		outputStream.flush();
	}

	public long getFormatIdentifier() {
		return formatIdentifier;
	}

	public void setFormatIdentifier(long formatIdentifier) {
		this.formatIdentifier = formatIdentifier;
	}

	public long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public long getTotalRepresentationLength() {
		return totalRepresentationLength;
	}

	public void setTotalRepresentationLength(long totalRepresentationLength) {
		this.totalRepresentationLength = totalRepresentationLength;
	}

	public int getNoOfRepresentations() {
		return noOfRepresentations;
	}

	public void setNoOfRepresentations(int noOfRepresentations) {
		this.noOfRepresentations = noOfRepresentations;
	}

	public int getCertificationFlag() {
		return certificationFlag;
	}

	public void setCertificationFlag(int certificationFlag) {
		this.certificationFlag = certificationFlag;
	}

	public int getTemporalSemantics() {
		return temporalSemantics;
	}

	public void setTemporalSemantics(int temporalSemantics) {
		this.temporalSemantics = temporalSemantics;
	}

	@Override
	public String toString() {
		return "\nFaceGeneralHeader [GeneralHeaderRecordLength=" + getRecordLength() + ", formatIdentifier="
				+ formatIdentifier + ", versionNumber=" + versionNumber + ", totalRepresentationLength="
				+ totalRepresentationLength + ", noOfRepresentations=" + noOfRepresentations + ", certificationFlag="
				+ Integer.toHexString(certificationFlag) + ", temporalSemantics="
				+ Integer.toHexString(temporalSemantics) + "]\n";
	}
}
