package io.mosip.biometrics.util.finger;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExtendedDataBlock extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedDataBlock.class);

	private int extendedDataBlockIdentificationCode;
	private int lengthOfExtendedDataBlock;

	public ExtendedDataBlock() {
	}

	public ExtendedDataBlock(int extendedDataBlockIdentificationCode, int lengthOfExtendedDataBlock) {
		setExtendedDataBlockIdentificationCode(extendedDataBlockIdentificationCode);
		setLengthOfExtendedDataBlock(lengthOfExtendedDataBlock);
	}

	public int getExtendedDataBlockIdentificationCode() {
		return extendedDataBlockIdentificationCode;
	}

	public void setExtendedDataBlockIdentificationCode(int extendedDataBlockIdentificationCode) {
		this.extendedDataBlockIdentificationCode = extendedDataBlockIdentificationCode;
	}

	public int getLengthOfExtendedDataBlock() {
		return lengthOfExtendedDataBlock;
	}

	public void setLengthOfExtendedDataBlock(int lengthOfExtendedDataBlock) {
		this.lengthOfExtendedDataBlock = lengthOfExtendedDataBlock;
	}

	@Override
	public String toString() {
		return "\nExtendedDataBlock [extendedDataBlockIdentificationCode="
				+ Integer.toHexString(extendedDataBlockIdentificationCode) + ", lengthOfExtendedDataBlock="
				+ lengthOfExtendedDataBlock + "]\n";
	}
}
