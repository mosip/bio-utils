package io.mosip.biometrics.util.finger;

import io.mosip.biometrics.util.AbstractImageInfo;

public abstract class ExtendedDataBlock extends AbstractImageInfo {
	private int extendedDataBlockIdentificationCode;
	private int lengthOfExtendedDataBlock;

	protected ExtendedDataBlock() {
		super();
	}

	protected ExtendedDataBlock(int extendedDataBlockIdentificationCode, int lengthOfExtendedDataBlock) {
		this();
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
