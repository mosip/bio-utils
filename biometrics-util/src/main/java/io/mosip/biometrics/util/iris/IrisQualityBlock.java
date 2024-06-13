package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;

public class IrisQualityBlock extends AbstractImageInfo {
	private int qualityScore = 255;
	private int qualityAlgorithmVendorIdentifier = IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED;
	private int qualityAlgorithmIdentifier = IrisQualityAlgorithmIdentifier.UNSPECIFIED;

	public IrisQualityBlock(int qualityScore) {
		setQualityScore(qualityScore);
		setQualityAlgorithmVendorIdentifier(IrisQualityAlgorithmVendorIdentifier.UNSPECIFIED);
		setQualityAlgorithmIdentifier(IrisQualityAlgorithmIdentifier.UNSPECIFIED);
	}

	public IrisQualityBlock(int qualityScore, int qualityAlgorithmVendorIdentifier,
			int qualityAlgorithmIdentifier) {
		setQualityScore(qualityScore);
		setQualityAlgorithmVendorIdentifier(qualityAlgorithmVendorIdentifier);
		setQualityAlgorithmIdentifier(qualityAlgorithmIdentifier);
	}

	public IrisQualityBlock(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public IrisQualityBlock(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setQualityScore(inputStream.readUnsignedByte());
		setQualityAlgorithmVendorIdentifier(inputStream.readUnsignedShort());
		setQualityAlgorithmIdentifier(inputStream.readUnsignedShort());
	}

	@Override
	@SuppressWarnings({ "java:S2674" })
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		inputStream.skip(5);
	}

	@Override
	public long getRecordLength() {
		return 5; /* 1 + 2 + 2 (table 2 ISO/IEC 19794-5) */
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeByte(getQualityScore()); /* 1 */
		outputStream.writeShort(getQualityAlgorithmVendorIdentifier()); /* + 2 = 3 */
		outputStream.writeShort(getQualityAlgorithmIdentifier()); /* + 2 = 5 */
		outputStream.flush();
	}

	public int getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(int qualityScore) {
		this.qualityScore = qualityScore;
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

	@Override
	public String toString() {
		return "\nIrisQualityBlock [QualityBlockRecordLength=" + getRecordLength() + ", qualityScore="
				+ Integer.toHexString(qualityScore) + ", qualityAlgorithmVendorIdentifier="
				+ Integer.toHexString(qualityAlgorithmVendorIdentifier) + ", qualityAlgorithmIdentifier="
				+ Integer.toHexString(qualityAlgorithmIdentifier) + "]\n";
	}
}
