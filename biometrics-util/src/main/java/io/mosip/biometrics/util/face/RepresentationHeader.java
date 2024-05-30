package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;

public class RepresentationHeader extends AbstractImageInfo {
	private long representationDataLength;
	private long representationLength;

	private Date captureDateTime;
	private int captureYear;
	private int captureMonth;
	private int captureDay;
	private int captureHour;
	private int captureMinute;
	private int captureSecond;
	private int captureMilliSecond;

	private int captureDeviceTechnologyIdentifier;
	private int captureDeviceVendorIdentifier;
	private int captureDeviceTypeIdentifier;

	private int noOfQualityBlocks;
	private FaceQualityBlock[] qualityBlocks;
	private FacialInformation facialInformation;
	private LandmarkPoints[] landmarkPoints;
	private ImageInformation imageInformation;

	public RepresentationHeader(long representationDataLength, Date captureDate, FaceQualityBlock[] qualityBlocks,
			FacialInformation facialInformation, LandmarkPoints[] landmarkPoints, ImageInformation imageInformation) {
		setRepresentationDataLength(representationDataLength);
		setCaptureDateTime(captureDate);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(captureDate);
		setCaptureYear(calendar.get(Calendar.YEAR));
		setCaptureMonth(calendar.get(Calendar.MONTH));
		setCaptureDay(calendar.get(Calendar.DAY_OF_MONTH));
		setCaptureHour(calendar.get(Calendar.HOUR_OF_DAY));
		setCaptureMinute(calendar.get(Calendar.MINUTE));
		setCaptureSecond(calendar.get(Calendar.SECOND));
		setCaptureMilliSecond(calendar.get(Calendar.MILLISECOND));

		setCaptureDeviceTechnologyIdentifier(FaceCaptureDeviceTechnology.UNSPECIFIED);
		setCaptureDeviceVendorIdentifier(FaceCaptureDeviceVendor.UNSPECIFIED);
		setCaptureDeviceTypeIdentifier(FaceCaptureDeviceType.UNSPECIFIED);

		setNoOfQualityBlocks(qualityBlocks.length);
		setQualityBlocks(qualityBlocks);
		setFacialInformation(facialInformation);
		setLandmarkPoints(landmarkPoints);
		setImageInformation(imageInformation);
		setRepresentationLength(getRecordLength() + getRepresentationDataLength());
	}

	@SuppressWarnings({ "java:S107" })
	public RepresentationHeader(long representationDataLength, Date captureDate, int captureDeviceTechnologyIdentifier,
			int captureDeviceVendorIdentifier, int captureDeviceTypeIdentifier, FaceQualityBlock[] qualityBlocks,
			FacialInformation facialInformation, LandmarkPoints[] landmarkPoints, ImageInformation imageInformation) {
		setRepresentationDataLength(representationDataLength);
		setCaptureDateTime(captureDate);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(captureDate);
		setCaptureYear(calendar.get(Calendar.YEAR));
		setCaptureMonth(calendar.get(Calendar.MONTH));
		setCaptureDay(calendar.get(Calendar.DAY_OF_MONTH));
		setCaptureHour(calendar.get(Calendar.HOUR_OF_DAY));
		setCaptureMinute(calendar.get(Calendar.MINUTE));
		setCaptureSecond(calendar.get(Calendar.SECOND));
		setCaptureMilliSecond(calendar.get(Calendar.MILLISECOND));

		setCaptureDeviceTechnologyIdentifier(captureDeviceTechnologyIdentifier);
		setCaptureDeviceVendorIdentifier(captureDeviceVendorIdentifier);
		setCaptureDeviceTypeIdentifier(captureDeviceTypeIdentifier);

		setNoOfQualityBlocks(qualityBlocks.length);
		setQualityBlocks(qualityBlocks);
		setFacialInformation(facialInformation);
		setLandmarkPoints(landmarkPoints);
		setImageInformation(imageInformation);
		setRepresentationLength(getRecordLength() + getRepresentationDataLength());
	}

	public RepresentationHeader(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public RepresentationHeader(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setRepresentationLength((inputStream.readInt() & 0xFFFFFFFFL));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		setCaptureYear(inputStream.readUnsignedShort());
		setCaptureMonth(inputStream.readUnsignedByte());
		setCaptureDay(inputStream.readUnsignedByte());
		setCaptureHour(inputStream.readUnsignedByte());
		setCaptureMinute(inputStream.readUnsignedByte());
		setCaptureSecond(inputStream.readUnsignedByte());
		setCaptureMilliSecond(inputStream.readUnsignedShort());

		calendar.set(Calendar.YEAR, getCaptureYear());
		calendar.set(Calendar.MONTH, getCaptureMonth() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, getCaptureDay());
		calendar.set(Calendar.HOUR_OF_DAY, getCaptureHour());
		calendar.set(Calendar.MINUTE, getCaptureMinute());
		calendar.set(Calendar.SECOND, getCaptureSecond());
		calendar.set(Calendar.MILLISECOND, getCaptureMilliSecond());
		setCaptureDateTime(calendar.getTime());

		setCaptureDeviceTechnologyIdentifier(inputStream.readUnsignedByte());
		setCaptureDeviceVendorIdentifier(inputStream.readUnsignedShort());
		setCaptureDeviceTypeIdentifier(inputStream.readUnsignedShort());

		setNoOfQualityBlocks(inputStream.readUnsignedByte());
		FaceQualityBlock[] qualityBlock = new FaceQualityBlock[getNoOfQualityBlocks()];
		for (int index = 0; index < getNoOfQualityBlocks(); index++) {
			qualityBlock[index] = new FaceQualityBlock(inputStream);
		}
		setQualityBlocks(qualityBlock);

		setFacialInformation(new FacialInformation(inputStream));
		int noOfLandMarkPoints = getFacialInformation().getNoOfLandMarkPoints();
		if (noOfLandMarkPoints > 0) {
			LandmarkPoints[] landmarkPointsInfo = new LandmarkPoints[noOfLandMarkPoints];
			for (int index = 0; index < noOfLandMarkPoints; index++) {
				landmarkPointsInfo[index] = new LandmarkPoints(inputStream);
			}
			setLandmarkPoints(landmarkPointsInfo);
		}

		setImageInformation(new ImageInformation(inputStream));
		setRepresentationDataLength(getRepresentationLength() - getRecordLength());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// 4(RepresentationLength) + 4(RepresentationLength) + 9(Datetime) + 1(DeviceTechnologyIdentifier) + 2(DeviceVendorIdentifier) + 2 (DeviceTypeIdentifier)
		inputStream.skipBytes(18);

		setNoOfQualityBlocks(inputStream.readUnsignedByte());
		inputStream.skipBytes(getNoOfQualityBlocks() * 5);

		setFacialInformation(new FacialInformation(inputStream, onlyImageInformation));

		inputStream.skipBytes(getFacialInformation().getNoOfLandMarkPoints() * 8);

		setImageInformation(new ImageInformation(inputStream, onlyImageInformation));
	}

	@Override
	public long getRecordLength() {
		int qualityBlockRecordLength = 0;
		if (getQualityBlocks() != null && getQualityBlocks().length > 0) {
			for (int index = 0; index < getQualityBlocks().length; index++)
				qualityBlockRecordLength += getQualityBlocks()[index].getRecordLength();
		}

		int landMarkRecordLength = 0;
		if (getLandmarkPoints() != null && getLandmarkPoints().length > 0) {
			for (int index = 0; index < getLandmarkPoints().length; index++)
				landMarkRecordLength += getLandmarkPoints()[index].getRecordLength();
		}
		return (4 + 9 + 1 + 2 + 2 + 1 + qualityBlockRecordLength + 17 + landMarkRecordLength
				+ 11); /* 4 + 9 + 1 + 2 + 2 + 1 + 5x + 17 + 8x + 11 (table 2 ISO/IEC 19794-5) */
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt((int) getRepresentationLength());
		outputStream.writeShort(getCaptureYear()); // 2
		outputStream.writeByte(getCaptureMonth() + 1); // 1
		outputStream.writeByte(getCaptureDay()); // 1
		outputStream.writeByte(getCaptureHour()); // 1
		outputStream.writeByte(getCaptureMinute());// 1
		outputStream.writeByte(getCaptureSecond());// 1
		outputStream.writeShort(getCaptureMilliSecond());// 2

		outputStream.writeByte(getCaptureDeviceTechnologyIdentifier());
		outputStream.writeShort(getCaptureDeviceVendorIdentifier());
		outputStream.writeShort(getCaptureDeviceTypeIdentifier());

		outputStream.writeByte(getNoOfQualityBlocks());
		if (getQualityBlocks() != null) {
			for (int index = 0; index < getQualityBlocks().length; index++) {
				getQualityBlocks()[index].writeObject(outputStream);
			}
		}

		getFacialInformation().writeObject(outputStream);

		if (getLandmarkPoints() != null) {
			for (int index = 0; index < getLandmarkPoints().length; index++) {
				getLandmarkPoints()[index].writeObject(outputStream);
			}
		}

		getImageInformation().writeObject(outputStream);

		outputStream.flush();
	}

	public long getRepresentationDataLength() {
		return representationDataLength;
	}

	public void setRepresentationDataLength(long representationDataLength) {
		this.representationDataLength = representationDataLength;
	}

	public long getRepresentationLength() {
		return representationLength;
	}

	public void setRepresentationLength(long representationLength) {
		this.representationLength = representationLength;
	}

	public Date getCaptureDateTime() {
		return captureDateTime;
	}

	public void setCaptureDateTime(Date captureDateTime) {
		this.captureDateTime = captureDateTime;
	}

	public int getCaptureYear() {
		return captureYear;
	}

	public void setCaptureYear(int captureYear) {
		this.captureYear = captureYear;
	}

	public int getCaptureMonth() {
		return captureMonth;
	}

	public void setCaptureMonth(int captureMonth) {
		this.captureMonth = captureMonth;
	}

	public int getCaptureDay() {
		return captureDay;
	}

	public void setCaptureDay(int captureDay) {
		this.captureDay = captureDay;
	}

	public int getCaptureHour() {
		return captureHour;
	}

	public void setCaptureHour(int captureHour) {
		this.captureHour = captureHour;
	}

	public int getCaptureMinute() {
		return captureMinute;
	}

	public void setCaptureMinute(int captureMinute) {
		this.captureMinute = captureMinute;
	}

	public int getCaptureSecond() {
		return captureSecond;
	}

	public void setCaptureSecond(int captureSecond) {
		this.captureSecond = captureSecond;
	}

	public int getCaptureMilliSecond() {
		return captureMilliSecond;
	}

	public void setCaptureMilliSecond(int captureMilliSecond) {
		this.captureMilliSecond = captureMilliSecond;
	}

	public int getCaptureDeviceTechnologyIdentifier() {
		return captureDeviceTechnologyIdentifier;
	}

	public void setCaptureDeviceTechnologyIdentifier(int captureDeviceTechnologyIdentifier) {
		this.captureDeviceTechnologyIdentifier = captureDeviceTechnologyIdentifier;
	}

	public int getCaptureDeviceVendorIdentifier() {
		return captureDeviceVendorIdentifier;
	}

	public void setCaptureDeviceVendorIdentifier(int captureDeviceVendorIdentifier) {
		this.captureDeviceVendorIdentifier = captureDeviceVendorIdentifier;
	}

	public int getCaptureDeviceTypeIdentifier() {
		return captureDeviceTypeIdentifier;
	}

	public void setCaptureDeviceTypeIdentifier(int captureDeviceTypeIdentifier) {
		this.captureDeviceTypeIdentifier = captureDeviceTypeIdentifier;
	}

	public int getNoOfQualityBlocks() {
		return noOfQualityBlocks;
	}

	public void setNoOfQualityBlocks(int noOfQualityBlocks) {
		this.noOfQualityBlocks = noOfQualityBlocks;
	}

	public FaceQualityBlock[] getQualityBlocks() {
		return qualityBlocks;
	}

	public void setQualityBlocks(FaceQualityBlock[] qualityBlocks) {
		this.qualityBlocks = qualityBlocks;
	}

	public FacialInformation getFacialInformation() {
		return facialInformation;
	}

	public void setFacialInformation(FacialInformation facialInformation) {
		this.facialInformation = facialInformation;
	}

	public LandmarkPoints[] getLandmarkPoints() {
		return landmarkPoints;
	}

	public void setLandmarkPoints(LandmarkPoints[] landmarkPoints) {
		this.landmarkPoints = landmarkPoints;
	}

	public ImageInformation getImageInformation() {
		return imageInformation;
	}

	public void setImageInformation(ImageInformation imageInformation) {
		this.imageInformation = imageInformation;
	}

	@Override
	public String toString() {
		return "\nRepresentationHeader [RepresentationHeaderLength=" + getRecordLength() + ", representationDataLength="
				+ Long.toHexString(representationDataLength) + ", representationLength="
				+ Long.toHexString(representationLength) + ", captureDateTime=" + captureDateTime + ", captureYear="
				+ Integer.toHexString(captureYear) + ", captureMonth=" + Integer.toHexString(captureMonth)
				+ ", captureDay=" + Integer.toHexString(captureDay) + ", captureHour="
				+ Integer.toHexString(captureHour) + ", captureMinute=" + Integer.toHexString(captureMinute)
				+ ", captureSecond=" + Integer.toHexString(captureSecond) + ", captureMilliSecond="
				+ Integer.toHexString(captureMilliSecond) + ", captureDeviceTechnologyIdentifier="
				+ Integer.toHexString(captureDeviceTechnologyIdentifier) + ", captureDeviceVendorIdentifier="
				+ Integer.toHexString(captureDeviceVendorIdentifier) + ", captureDeviceTypeIdentifier="
				+ Integer.toHexString(captureDeviceTypeIdentifier) + ", noOfQualityBlocks="
				+ Integer.toHexString(noOfQualityBlocks) + ", qualityBlocks=" + Arrays.toString(qualityBlocks)
				+ ", facialInformation=" + facialInformation + ", landmarkPoints=" + Arrays.toString(landmarkPoints)
				+ ", imageInformation=" + imageInformation + "]\n";
	}
}
