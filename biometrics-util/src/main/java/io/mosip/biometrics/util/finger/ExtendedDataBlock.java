package io.mosip.biometrics.util.finger;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExtendedDataBlock extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedDataBlock.class);	

    private ExtendedDataBlockIdentificationCode extendedDataBlockIdentificationCode;
	private int lengthOfExtendedDataBlock;
    
    public ExtendedDataBlock ()
    {
    }

    public ExtendedDataBlock (ExtendedDataBlockIdentificationCode extendedDataBlockIdentificationCode, int lengthOfExtendedDataBlock)
    {
    	setExtendedDataBlockIdentificationCode (extendedDataBlockIdentificationCode);
    	setLengthOfExtendedDataBlock (lengthOfExtendedDataBlock);
    }
    
	public ExtendedDataBlockIdentificationCode getExtendedDataBlockIdentificationCode() {
		return extendedDataBlockIdentificationCode;
	}

	public void setExtendedDataBlockIdentificationCode(
			ExtendedDataBlockIdentificationCode extendedDataBlockIdentificationCode) {
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
		return "\nExtendedDataBlock [extendedDataBlockIdentificationCode=" + extendedDataBlockIdentificationCode
				+ ", lengthOfExtendedDataBlock=" + lengthOfExtendedDataBlock + "]\n";
	}	
}
