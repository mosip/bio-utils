package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LandmarkPoints extends AbstractImageInfo 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LandmarkPoints.class);	

    private byte landmarkPointType;
    private byte landmarkPointCode;
    private short xCoordinate;
    private short yCoordinate;
    private short zCoordinate;

    public LandmarkPoints (byte landmarkPointType, byte landmarkPointCode, short xCoordinate, short yCoordinate, short zCoordinate)
    {
        setLandmarkPointType (landmarkPointType);
        setLandmarkPointCode (landmarkPointCode);
        setxCoordinate (xCoordinate);
        setyCoordinate (yCoordinate);
        setzCoordinate (zCoordinate);
    }

    public LandmarkPoints (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {   
    	setLandmarkPointType ((byte)inputStream.readUnsignedByte());
        setLandmarkPointCode ((byte)inputStream.readUnsignedByte());
        setxCoordinate ((short)inputStream.readUnsignedShort());
        setyCoordinate ((short)inputStream.readUnsignedShort());
        setzCoordinate ((short)inputStream.readUnsignedShort());
    }

    public int getRecordLength ()
    {
        return 1 + 1 + 2 + 2 + 2;//8
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeByte (getLandmarkPointType()); // 1 bytes
        outputStream.writeByte (getLandmarkPointCode()); // 1 bytes
        outputStream.writeShort (getxCoordinate());// 2 bytes
        outputStream.writeShort (getyCoordinate());// 2 bytes
        outputStream.writeShort (getzCoordinate());// 2 bytes
        outputStream.flush ();
    }

	public byte getLandmarkPointType() {
		return landmarkPointType;
	}

	public void setLandmarkPointType(byte landmarkPointType) {
		this.landmarkPointType = landmarkPointType;
	}

	public byte getLandmarkPointCode() {
		return landmarkPointCode;
	}

	public void setLandmarkPointCode(byte landmarkPointCode) {
		this.landmarkPointCode = landmarkPointCode;
	}

	public short getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(short xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public short getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(short yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public short getzCoordinate() {
		return zCoordinate;
	}

	public void setzCoordinate(short zCoordinate) {
		this.zCoordinate = zCoordinate;
	}

	@Override
	public String toString() {
		return "\nLandmarkPoints [LandmarkPointRecordLength=" + getRecordLength () + ", landmarkPointType=" + landmarkPointType + ", landmarkPointCode=" + landmarkPointCode
				+ ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + ", zCoordinate=" + zCoordinate
				+ "]\n";
	}    	
}
