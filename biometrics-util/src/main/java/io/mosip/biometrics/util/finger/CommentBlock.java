package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommentBlock extends ExtendedDataBlock {
	private byte[] comment;

	public CommentBlock(byte[] comment, short extendedDataBlockIdentificationCode) {
		setComment(comment);
		setExtendedDataBlockIdentificationCode(extendedDataBlockIdentificationCode);
		setLengthOfExtendedDataBlock((int) getRecordLength());
	}

	public CommentBlock(DataInputStream inputStream, int extendedDataBlockIdentificationCode) throws IOException {
		setExtendedDataBlockIdentificationCode(extendedDataBlockIdentificationCode);
		readObject(inputStream);
	}

	public CommentBlock(DataInputStream inputStream, int extendedDataBlockIdentificationCode, boolean onlyImageInformation) throws IOException {
		setExtendedDataBlockIdentificationCode(extendedDataBlockIdentificationCode);
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setLengthOfExtendedDataBlock(inputStream.readUnsignedShort()); /* 2 */
		setComment(new byte[getLengthOfExtendedDataBlock() - 4]);
		inputStream.readFully(getComment());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// SKIP as calling class		
	}

	@Override
	public long getRecordLength() {
		return (2 + 2 + (getComment() != null && getComment().length > 0 ? getComment().length : 0));
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeShort(getExtendedDataBlockIdentificationCode()); /* 2 = 2 */
		outputStream.writeShort(getLengthOfExtendedDataBlock()); /* + 2 = 4 */

		if (getComment() != null && getComment().length > 0)
			outputStream.write(getComment(), 0, getComment().length); // 4 + Comment Length

		outputStream.flush();
	}

	public byte[] getComment() {
		return comment;
	}

	public void setComment(byte[] comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		super.toString();

		return "\nCommentBlock [RecordLength=" + getRecordLength() + ", CommentLength=" + getComment().length
				+ ", Comment=" + (getComment() != null && getComment().length > 0 ? new String(getComment()) : "")
				+ "]\n";
	}
}
