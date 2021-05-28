package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentBlock extends ExtendedDataBlock
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentBlock.class);	

    private byte [] comment;

    public CommentBlock (byte [] comment)
    {
    	setComment (comment);
    	setExtendedDataBlockIdentificationCode (ExtendedDataBlockIdentificationCode.COMMENT_03);
    	setLengthOfExtendedDataBlock (getRecordLength ());
    }

    public CommentBlock (byte [] comment, ExtendedDataBlockIdentificationCode extendedDataBlockIdentificationCode)
    {
    	setComment (comment);
    	setExtendedDataBlockIdentificationCode (extendedDataBlockIdentificationCode);
    	setLengthOfExtendedDataBlock (getRecordLength ());
    }

    public CommentBlock (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {
    	setLengthOfExtendedDataBlock (inputStream.readUnsignedShort()); /* 2 */
    	setComment (new byte[getLengthOfExtendedDataBlock() - 4]);
    	inputStream.readFully(getComment());    	
    }

	public int getRecordLength ()
    {
        return (2 + 2 +  (getComment() != null && getComment().length > 0 ? getComment().length : 0));
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeShort (getExtendedDataBlockIdentificationCode().value()); 	/* 2 = 2 */
        outputStream.writeShort (getLengthOfExtendedDataBlock());       				/* + 2 = 4 */

        if (getComment() != null && getComment().length > 0)
            outputStream.write (getComment(), 0, getComment().length); // 4 + Comment Length
        outputStream.flush ();
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
		
		return "\nCommentBlock [RecordLength=" + getRecordLength () 
			+ ", CommentLength=" + getComment ().length 
			+ ", Comment=" + (getComment () != null && getComment ().length > 0 ? new String (getComment ()) : "") 
			+ "]\n";
	}    	
}
