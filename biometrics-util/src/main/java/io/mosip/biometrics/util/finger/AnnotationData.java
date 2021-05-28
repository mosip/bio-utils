package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationData extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationData.class);	

    private FingerPosition fingerPosition;
    private AnnotationCode annotationCode;
    
    public AnnotationData ()
    {
    	setFingerPosition (FingerPosition.UNKNOWN);
    	setAnnotationCode (AnnotationCode.AMPUTATED_FINGER);
    }
    
    public AnnotationData (FingerPosition fingerPosition, AnnotationCode annotationCode)
    {
    	setFingerPosition (fingerPosition);
    	setAnnotationCode (annotationCode);
    }

    public AnnotationData (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException { 
    	setFingerPosition (FingerPosition.fromValue(inputStream.readUnsignedByte()));
    	setAnnotationCode (AnnotationCode.fromValue(inputStream.readUnsignedByte()));
    }
    
    /*  (1 + 1  ) (Table 13 � Annotation data   ISO/IEC 19794-4-2011) */
    public int getRecordLength ()
    {
        return (1 + 1); 
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeByte (getFingerPosition().value()); 	/* 1 = 1 */
        outputStream.writeByte (getAnnotationCode().value());	/* + 1 = 2 */
        outputStream.flush ();
    }

	public FingerPosition getFingerPosition() {
		return fingerPosition;
	}

	public void setFingerPosition(FingerPosition fingerPosition) {
		this.fingerPosition = fingerPosition;
	}

	public AnnotationCode getAnnotationCode() {
		return annotationCode;
	}

	public void setAnnotationCode(AnnotationCode annotationCode) {
		this.annotationCode = annotationCode;
	}

	@Override
	public String toString() {
		return "\nAnnotationData [RecordLength=" + getRecordLength() + ", fingerPosition=" + fingerPosition + ", annotationCode=" + annotationCode + "]\n";
	}
}
