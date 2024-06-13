package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;

public class GeneralHeader extends AbstractImageInfo {
	private long formatIdentifier;
	private long versionNumber;
	private long totalRepresentationLength;
	private int noOfRepresentations;
	private int certificationFlag;
	private int noOfEyesPresent;

	public GeneralHeader(long totalRepresentationLength, int noOfRepresentations, int noOfEyesPresent) {
		setFormatIdentifier(IrisFormatIdentifier.FORMAT_IIR);
		setVersionNumber(IrisVersionNumber.VERSION_020);
		setTotalRepresentationLength(totalRepresentationLength);
		setNoOfRepresentations(noOfRepresentations);
		setCertificationFlag(IrisCertificationFlag.UNSPECIFIED);
		setNoOfEyesPresent(noOfEyesPresent);
	}

	public GeneralHeader(long formatIdentifier, long versionNumber,
			long totalRepresentationLength, int noOfRepresentations, int certificationFlag, int noOfEyesPresent) {
		setFormatIdentifier(formatIdentifier);
		setVersionNumber(versionNumber);
		setTotalRepresentationLength(totalRepresentationLength);
		setNoOfRepresentations(noOfRepresentations);
		setCertificationFlag(certificationFlag);
		setNoOfEyesPresent(noOfEyesPresent);
	}

	public GeneralHeader(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public GeneralHeader(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setFormatIdentifier((inputStream.readInt() & 0xFFFFFFFFL));
		setVersionNumber((inputStream.readInt() & 0xFFFFFFFFL));
		setTotalRepresentationLength(((inputStream.readInt() & 0xFFFFFFFFL) - getRecordLength()));
		setNoOfRepresentations(inputStream.readUnsignedShort());
		setCertificationFlag(inputStream.readUnsignedByte());
		setNoOfEyesPresent(inputStream.readUnsignedByte());
	}

	@Override
	@SuppressWarnings({ "java:S2674" })
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// 4(FormatIdentifier) + 4(VersionNumber)
		inputStream.skip(8);
		setTotalRepresentationLength(((inputStream.readInt() & 0xFFFFFFFFL) - getRecordLength()));
		setNoOfRepresentations(inputStream.readUnsignedShort());
		// 1(CertificationFlag) + 1(NoOfEyesPresent)
		inputStream.skip(2);
	}

	@Override
	public long getRecordLength() {
		return 16; /* 4 + 4 + 4 + 2 + 1 + 1 (table 3 ISO/IEC 19794-6-2011) */
	}

	@Override
	protected void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt((int) getFormatIdentifier()); /* 4 */
		outputStream.writeInt((int) getVersionNumber()); /* + 4 = 8 */
		outputStream.writeInt((int) (getRecordLength() + getTotalRepresentationLength()));/* + 4 = 12 */
		outputStream.writeShort(getNoOfRepresentations()); /* + 2 = 14 */
		outputStream.writeByte(getCertificationFlag()); /* + 1 = 15 */
		outputStream.writeByte(getNoOfEyesPresent()); /* + 1 = 16 */
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

	public int getNoOfEyesPresent() {
		return noOfEyesPresent;
	}

	public void setNoOfEyesPresent(int noOfEyesPresent) {
		this.noOfEyesPresent = noOfEyesPresent;
	}

	@Override
	public String toString() {
		return "\nIrisGeneralHeader [GeneralHeaderRecordLength=" + getRecordLength() + ", formatIdentifier="
				+ (formatIdentifier) + ", versionNumber=" + (versionNumber) + ", totalRepresentationLength="
				+ totalRepresentationLength + ", noOfRepresentations=" + Integer.toHexString(noOfRepresentations)
				+ ", certificationFlag=" + Integer.toHexString(certificationFlag) + ", noOfEyesPresent="
				+ Integer.toHexString(noOfEyesPresent) + "]\n";
	}
}
