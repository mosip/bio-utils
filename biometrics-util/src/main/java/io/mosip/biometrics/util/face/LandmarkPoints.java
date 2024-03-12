package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;

public class LandmarkPoints extends AbstractImageInfo {
	private int landmarkPointType;
	private int landmarkPointCode;
	private int xCoordinate;
	private int yCoordinate;
	private int zCoordinate;

	public LandmarkPoints(int landmarkPointType, int landmarkPointCode, int xCoordinate, int yCoordinate,
			int zCoordinate) {
		setLandmarkPointType(landmarkPointType);
		setLandmarkPointCode(landmarkPointCode);
		setXCoordinate(xCoordinate);
		setYCoordinate(yCoordinate);
		setZCoordinate(zCoordinate);
	}

	public LandmarkPoints(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public LandmarkPoints(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setLandmarkPointType(inputStream.readUnsignedByte());
		setLandmarkPointCode(inputStream.readUnsignedByte());
		setXCoordinate(inputStream.readUnsignedShort());
		setYCoordinate(inputStream.readUnsignedShort());
		setZCoordinate(inputStream.readUnsignedShort());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		inputStream.skip(8);
	}

	@Override
	public long getRecordLength() {
		return 1 + 1 + 2 + 2 + 2;/** 8 */
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeByte(getLandmarkPointType()); /** 1 bytes*/
		outputStream.writeByte(getLandmarkPointCode()); /** 1 bytes*/
		outputStream.writeShort(getXCoordinate());/** 2 bytes*/
		outputStream.writeShort(getYCoordinate());/** 2 bytes*/
		outputStream.writeShort(getZCoordinate());/** 2 bytes*/
		outputStream.flush();
	}

	public int getLandmarkPointType() {
		return landmarkPointType;
	}

	public void setLandmarkPointType(int landmarkPointType) {
		this.landmarkPointType = landmarkPointType;
	}

	public int getLandmarkPointCode() {
		return landmarkPointCode;
	}

	public void setLandmarkPointCode(int landmarkPointCode) {
		this.landmarkPointCode = landmarkPointCode;
	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public void setYCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getZCoordinate() {
		return zCoordinate;
	}

	public void setZCoordinate(int zCoordinate) {
		this.zCoordinate = zCoordinate;
	}

	@Override
	public String toString() {
		return "\nLandmarkPoints [LandmarkPointRecordLength=" + getRecordLength() + ", landmarkPointType="
				+ Integer.toHexString(landmarkPointType) + ", landmarkPointCode="
				+ Integer.toHexString(landmarkPointCode) + ", xCoordinate=" + Integer.toHexString(xCoordinate)
				+ ", yCoordinate=" + Integer.toHexString(yCoordinate) + ", zCoordinate="
				+ Integer.toHexString(zCoordinate) + "]\n";
	}
}
