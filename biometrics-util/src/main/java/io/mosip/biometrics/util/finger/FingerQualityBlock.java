package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FingerQualityBlock extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerQualityBlock.class);

	private int qualityScore = 255;
	private int qualityAlgorithmVendorIdentifier = FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED;
	private int qualityAlgorithmIdentifier = FingerQualityAlgorithmIdentifier.UNSPECIFIED;

	public FingerQualityBlock(int qualityScore) {
		setQualityScore(qualityScore);
		setQualityAlgorithmVendorIdentifier(FingerQualityAlgorithmVendorIdentifier.UNSPECIFIED);
		setQualityAlgorithmIdentifier(FingerQualityAlgorithmIdentifier.UNSPECIFIED);
	}

	public FingerQualityBlock(int qualityScore, int qualityAlgorithmVendorIdentifier, int qualityAlgorithmIdentifier) {
		setQualityScore(qualityScore);
		setQualityAlgorithmVendorIdentifier(qualityAlgorithmVendorIdentifier);
		setQualityAlgorithmIdentifier(qualityAlgorithmIdentifier);
	}

	public FingerQualityBlock(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public FingerQualityBlock(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setQualityScore(inputStream.readUnsignedByte());

		int qualityAlgorithmVendorIdentifier = inputStream.readUnsignedShort();
		try {
			setQualityAlgorithmVendorIdentifier(qualityAlgorithmVendorIdentifier);
		} catch (Exception ex) {
			LOGGER.error("setQualityAlgorithmVendorIdentifier :: Not Defined :: qualityAlgorithmVendorIdentifier :: "
					+ Integer.toHexString(qualityAlgorithmVendorIdentifier));
		}

		int qualityAlgorithmIdentifier = inputStream.readUnsignedShort();
		try {
			setQualityAlgorithmIdentifier(qualityAlgorithmIdentifier);
		} catch (Exception ex) {
			LOGGER.error("setQualityAlgorithmIdentifier :: Not Defined :: qualityAlgorithmIdentifier :: "
					+ Integer.toHexString(qualityAlgorithmIdentifier));
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// skip		
		inputStream.skip(5);
	}

	@Override
	public long getRecordLength() {
		return 5; /* 1 + 2 + 2 (table 2 ISO/IEC 19794-4-2011) */
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
		return "\nQualityBlock [QualityBlockRecordLength=" + getRecordLength() + ", qualityScore="
				+ Integer.toHexString(qualityScore) + ", qualityAlgorithmVendorIdentifier="
				+ Integer.toHexString(qualityAlgorithmVendorIdentifier) + ", qualityAlgorithmIdentifier="
				+ Integer.toHexString(qualityAlgorithmIdentifier) + "]\n";
	}

}
