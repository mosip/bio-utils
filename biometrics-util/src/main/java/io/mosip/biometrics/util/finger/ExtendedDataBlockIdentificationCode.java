package io.mosip.biometrics.util.finger;

/** Finger Device Technology Identifier, 0x00. Table 4 � Capture device technology  of ISO/IEC 19794-4-2011. */
public class ExtendedDataBlockIdentificationCode {

	public static int RESERVED = (0x0000); 
	public static int SEGMENTATION = (0x0001); 
	public static int ANNOTATION = (0x0002); 
	public static int COMMENT_03 = (0x0003);// Minumum 
	public static int COMMENT_FF = (0x00FF);// Maximum

	private int value;
	public ExtendedDataBlockIdentificationCode(int value) {
		this.value = value;
	}	

	public void setvalue(int value) {
		this.value = value;
	}
	
	public int getvalue() {
		return value;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
