package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepresentationHeader extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(RepresentationHeader.class);

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
	private FingerQualityBlock[] qualityBlocks;
	private int certificationFlag;
	private int noOfCertificationBlocks;
	private FingerCertificationBlock[] certificationBlocks;

	private int fingerPosition;
	private int representationNo;
	private int scaleUnits;
	private int captureDeviceSpatialSamplingRateHorizontal;
	private int captureDeviceSpatialSamplingRateVertical;
	private int imageSpatialSamplingRateHorizontal;
	private int imageSpatialSamplingRateVertical;
	private int bitDepth;
	private int compressionType;
	private int impressionType;
	private int lineLengthHorizontal;
	private int lineLengthVertical;

	public RepresentationHeader(long representationDataLength, Date captureDate, FingerQualityBlock[] qualityBlocks,
			int certificationFlag, FingerCertificationBlock[] certificationBlocks, int fingerPosition,
			int representationNo, int scaleUnitType) {
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

		setCaptureDeviceTechnologyIdentifier(FingerCaptureDeviceTechnology.UNSPECIFIED);
		setCaptureDeviceVendorIdentifier(FingerCaptureDeviceVendor.UNSPECIFIED);
		setCaptureDeviceTypeIdentifier(FingerCaptureDeviceType.UNSPECIFIED);

		setNoOfQualityBlocks(qualityBlocks.length);
		setQualityBlocks(qualityBlocks);
		setCertificationFlag(certificationFlag);
		setNoOfCertificationBlocks(certificationBlocks.length);
		setCertificationBlocks(certificationBlocks);

		setFingerPosition(fingerPosition);
		setRepresentationNo(representationNo);
		setScaleUnits(scaleUnitType);
		setCaptureDeviceSpatialSamplingRateHorizontal(0);
		setCaptureDeviceSpatialSamplingRateVertical(0);
		setImageSpatialSamplingRateHorizontal(0);
		setImageSpatialSamplingRateVertical(0);
		setBitDepth(FingerImageBitDepth.BPP_08);
		setCompressionType(FingerImageCompressionType.JPEG_2000_LOSS_LESS);
		setImpressionType(FingerImpressionType.UNKNOWN);
		setLineLengthHorizontal(0);
		setLineLengthVertical(0);
	}

	public RepresentationHeader(long representationDataLength, Date captureDate, int captureDeviceTechnologyIdentifier,
			int captureDeviceVendorIdentifier, int captureDeviceTypeIdentifier, FingerQualityBlock[] qualityBlocks,
			int certificationFlag, FingerCertificationBlock[] certificationBlocks, int fingerPosition,
			int representationNo, int scaleUnitType, int captureDeviceSpatialSamplingRateHorizontal,
			int captureDeviceSpatialSamplingRateVertical, int imageSpatialSamplingRateHorizontal,
			int imageSpatialSamplingRateVertical, int bitDepth, int compressionType, int impressionType,
			int lineLengthHorizontal, int lineLengthVertical) {
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

		setNoOfQualityBlocks(((qualityBlocks != null && qualityBlocks.length > 0) ? qualityBlocks.length : 0));
		setQualityBlocks(qualityBlocks);
		setCertificationFlag(certificationFlag);
		setNoOfCertificationBlocks(
				((certificationBlocks != null && certificationBlocks.length > 0) ? certificationBlocks.length : 0));
		setCertificationBlocks(certificationBlocks);

		setFingerPosition(fingerPosition);
		setRepresentationNo(representationNo);
		setScaleUnits(scaleUnitType);
		setCaptureDeviceSpatialSamplingRateHorizontal(captureDeviceSpatialSamplingRateHorizontal);
		setCaptureDeviceSpatialSamplingRateVertical(captureDeviceSpatialSamplingRateVertical);
		setImageSpatialSamplingRateHorizontal(imageSpatialSamplingRateHorizontal);
		setImageSpatialSamplingRateVertical(imageSpatialSamplingRateVertical);
		setBitDepth(bitDepth);
		setCompressionType(compressionType);
		setImpressionType(impressionType);
		setLineLengthHorizontal(lineLengthHorizontal);
		setLineLengthVertical(lineLengthVertical);
	}

	public RepresentationHeader(DataInputStream inputStream, int certificationFlag) throws IOException {
		setCertificationFlag(certificationFlag);
		readObject(inputStream);
	}

	public RepresentationHeader(DataInputStream inputStream, int certificationFlag, boolean onlyImageInformation) throws IOException {
		setCertificationFlag(certificationFlag);
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setRepresentationDataLength((inputStream.readInt() & 0xFFFFFFFFL));

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
		int captureDeviceVendorIdentifier = inputStream.readUnsignedShort();
		try {
			setCaptureDeviceVendorIdentifier(captureDeviceVendorIdentifier);
		} catch (Exception ex) {
			LOGGER.error("setCaptureDeviceVendorIdentifier :: Not Defined :: captureDeviceVendorIdentifier :: "
					+ Integer.toHexString(captureDeviceVendorIdentifier));
		}

		int captureDeviceTypeIdentifier = inputStream.readUnsignedShort();
		try {
			setCaptureDeviceTypeIdentifier(captureDeviceTypeIdentifier);
		} catch (Exception ex) {
			LOGGER.error("setCaptureDeviceTypeIdentifier :: Not Defined :: captureDeviceTypeIdentifier :: "
					+ Integer.toHexString(captureDeviceTypeIdentifier));
		}

		setNoOfQualityBlocks(inputStream.readUnsignedByte());
		FingerQualityBlock[] qualityBlock = new FingerQualityBlock[getNoOfQualityBlocks()];
		if (getNoOfQualityBlocks() > 0) {
			for (int index = 0; index < getNoOfQualityBlocks(); index++) {
				qualityBlock[index] = new FingerQualityBlock(inputStream);
			}
		}
		setQualityBlocks(qualityBlock);

		if (getCertificationFlag() == FingerCertificationFlag.ONE) {
			setNoOfCertificationBlocks(inputStream.readUnsignedByte());
			FingerCertificationBlock[] certificationBlocks = new FingerCertificationBlock[getNoOfCertificationBlocks()];
			if (getNoOfCertificationBlocks() > 0) {
				for (int index = 0; index < getNoOfCertificationBlocks(); index++) {
					certificationBlocks[index] = new FingerCertificationBlock(inputStream);
				}
			}
			setCertificationBlocks(certificationBlocks);
		}

		int fingerPosition = inputStream.readUnsignedByte();
		try {
			setFingerPosition(fingerPosition);
		} catch (Exception ex) {
			LOGGER.error(
					"setFingerPosition :: Not Defined :: fingerPosition :: " + Integer.toHexString(fingerPosition));
		}

		setRepresentationNo(inputStream.readUnsignedByte());

		int scaleUnits = inputStream.readUnsignedByte();
		try {
			setScaleUnits(scaleUnits);
		} catch (Exception ex) {
			LOGGER.error("setScaleUnits :: Not Defined :: scaleUnits :: " + Integer.toHexString(scaleUnits));
		}
		setCaptureDeviceSpatialSamplingRateHorizontal(inputStream.readUnsignedShort());
		setCaptureDeviceSpatialSamplingRateVertical(inputStream.readUnsignedShort());
		setImageSpatialSamplingRateHorizontal(inputStream.readUnsignedShort());
		setImageSpatialSamplingRateVertical(inputStream.readUnsignedShort());

		int bitDepth = inputStream.readUnsignedByte();
		try {
			setBitDepth(bitDepth);
		} catch (Exception ex) {
			LOGGER.error("setBitDepth :: Not Defined :: bitDepth :: " + Integer.toHexString(bitDepth));
		}

		int compressionType = inputStream.readUnsignedByte();
		try {
			setCompressionType(compressionType);
		} catch (Exception ex) {
			LOGGER.error(
					"setCompressionType :: Not Defined :: compressionType :: " + Integer.toHexString(compressionType));
		}

		int impressionType = inputStream.readUnsignedByte();
		try {
			setImpressionType(impressionType);
		} catch (Exception ex) {
			LOGGER.error(
					"setImpressionType :: Not Defined :: impressionType :: " + Integer.toHexString(impressionType));
		}

		setLineLengthHorizontal(inputStream.readUnsignedShort());
		setLineLengthVertical(inputStream.readUnsignedShort());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		// 4(RepresentationDataLength) + 9(Datetime) + 1(DeviceTechnologyIdentifier) + 2(DeviceVendorIdentifier) + 2 (DeviceTypeIdentifier)
		inputStream.skipBytes(18);

		setNoOfQualityBlocks(inputStream.readUnsignedByte());
		inputStream.skipBytes(getNoOfQualityBlocks() * 5);

		if (getCertificationFlag() == FingerCertificationFlag.ONE) {
			setNoOfCertificationBlocks(inputStream.readUnsignedByte());
			inputStream.skipBytes(getNoOfCertificationBlocks() * 3);
		}
		
		int fingerPosition = inputStream.readUnsignedByte();
		try {
			setFingerPosition(fingerPosition);
		} catch (Exception ex) {
			LOGGER.error(
					"setFingerPosition :: Not Defined :: fingerPosition :: " + Integer.toHexString(fingerPosition));
		}

		// 1(RepresentationNo) + 1(scaleUnits) + 2(DeviceSpatialSamplingRateHorizontal) + 2(CaptureDeviceSpatialSamplingRateVertical)
		// + 2(ImageSpatialSamplingRateHorizontal) + 2(ImageSpatialSamplingRateVertical)		
		inputStream.skipBytes(10);
		
		int bitDepth = inputStream.readUnsignedByte();
		try {
			setBitDepth(bitDepth);
		} catch (Exception ex) {
			LOGGER.error("setBitDepth :: Not Defined :: bitDepth :: " + Integer.toHexString(bitDepth));
		}

		int compressionType = inputStream.readUnsignedByte();
		try {
			setCompressionType(compressionType);
		} catch (Exception ex) {
			LOGGER.error(
					"setCompressionType :: Not Defined :: compressionType :: " + Integer.toHexString(compressionType));
		}

		// 1(impressionType) + 2(LineLengthHorizontal) + 2(LineLengthVertical)
		inputStream.skipBytes(5);
	}

	/*
	 * 4 + 9 + 1 + 2 + 2 + 1 + 5x + (1 + 3x) + 1 + 1 + 2 + 2 + 2 + 2 + 1 + 1 + 1 + 2
	 * + 2 (Table 4 Finger representation header ISO/IEC 19794-4-2011)
	 */
	@Override
	public long getRecordLength() {
		long qualityBlockRecordLength = 0;
		if (getQualityBlocks() != null && getQualityBlocks().length > 0) {
			for (int index = 0; index < getQualityBlocks().length; index++)
				qualityBlockRecordLength += getQualityBlocks()[index].getRecordLength();
		}

		long certificationBlockRecordLength = 0;
		if (getCertificationFlag() == FingerCertificationFlag.ONE) {
			certificationBlockRecordLength = 1;
			if (getCertificationBlocks() != null && getCertificationBlocks().length > 0) {
				for (int index = 0; index < getCertificationBlocks().length; index++)
					certificationBlockRecordLength += getCertificationBlocks()[index].getRecordLength();
			}
		}

		return (4 + 9 + 1 + 2 + 2 + 1 + qualityBlockRecordLength // (5x)
				+ certificationBlockRecordLength // (1 + 3x)
				+ 1 + 1 + 1 + 2 + 2 + 2 + 2 + 1 + 1 + 1 + 2 + 2);
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt((int) (getRecordLength() + getRepresentationDataLength())); // 4
		outputStream.writeShort(getCaptureYear()); // 4 + 2 = 6
		outputStream.writeByte(getCaptureMonth() + 1); // 6 + 1 = 7
		outputStream.writeByte(getCaptureDay()); // 7 + 1 = 8
		outputStream.writeByte(getCaptureHour()); // 8 + 1 = 9
		outputStream.writeByte(getCaptureMinute()); // 9 + 1 = 10
		outputStream.writeByte(getCaptureSecond()); // 10 + 1 = 11
		outputStream.writeShort(getCaptureMilliSecond()); // 11 + 2 = 13

		outputStream.writeByte(getCaptureDeviceTechnologyIdentifier()); // 13 + 1 = 14
		outputStream.writeShort(getCaptureDeviceVendorIdentifier()); // 14 + 2 = 16
		outputStream.writeShort(getCaptureDeviceTypeIdentifier()); // 16 + 2 = 18

		outputStream.writeByte(getNoOfQualityBlocks()); // 18 + 1 = 19
		if (getQualityBlocks() != null) // 19 + 5XBlocks = 24
		{
			for (int index = 0; index < getQualityBlocks().length; index++) {
				getQualityBlocks()[index].writeObject(outputStream);
			}
		}
		if (getCertificationFlag() == FingerCertificationFlag.ONE) {
			if (getCertificationBlocks() != null && getCertificationBlocks().length > 0) {
				outputStream.writeByte(getNoOfCertificationBlocks()); // 24 + 1 = 25
				for (int index = 0; index < getCertificationBlocks().length; index++) // 25 + 3X = 28
				{
					getCertificationBlocks()[index].writeObject(outputStream);
				}
			} else {
				LOGGER.error("getCertificationBlocks :: Not Defined ::  If FingerCertificationFlag.ONE :: ");
			}
		}
		outputStream.writeByte(getFingerPosition()); // 28 + 1 = 29
		outputStream.writeByte(getRepresentationNo()); // 29 + 1 = 30
		outputStream.writeByte(getScaleUnits()); // 30 + 1 = 31
		outputStream.writeShort(getCaptureDeviceSpatialSamplingRateHorizontal()); // 31 + 2 = 33
		outputStream.writeShort(getCaptureDeviceSpatialSamplingRateVertical()); // 33 + 2 = 35
		outputStream.writeShort(getImageSpatialSamplingRateHorizontal()); // 35 + 2 = 37
		outputStream.writeShort(getImageSpatialSamplingRateVertical()); // 37 + 2 = 39
		outputStream.writeByte(getBitDepth()); // 39 + 1 = 40
		outputStream.writeByte(getCompressionType()); // 40 + 1 = 41
		outputStream.writeByte(getImpressionType()); // 41 + 1 = 42
		outputStream.writeShort(getLineLengthHorizontal()); // 42 + 2 = 44
		outputStream.writeShort(getLineLengthVertical()); // 44 + 2 = 46
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

	public FingerQualityBlock[] getQualityBlocks() {
		return qualityBlocks;
	}

	public void setQualityBlocks(FingerQualityBlock[] qualityBlocks) {
		this.qualityBlocks = qualityBlocks;
	}

	public int getCertificationFlag() {
		return certificationFlag;
	}

	public void setCertificationFlag(int certificationFlag) {
		this.certificationFlag = certificationFlag;
	}

	public int getNoOfCertificationBlocks() {
		return noOfCertificationBlocks;
	}

	public void setNoOfCertificationBlocks(int noOfCertificationBlocks) {
		this.noOfCertificationBlocks = noOfCertificationBlocks;
	}

	public FingerCertificationBlock[] getCertificationBlocks() {
		return certificationBlocks;
	}

	public void setCertificationBlocks(FingerCertificationBlock[] certificationBlocks) {
		this.certificationBlocks = certificationBlocks;
	}

	public int getFingerPosition() {
		return fingerPosition;
	}

	public void setFingerPosition(int fingerPosition) {
		this.fingerPosition = fingerPosition;
	}

	public int getRepresentationNo() {
		return representationNo;
	}

	public void setRepresentationNo(int representationNo) {
		this.representationNo = representationNo;
	}

	public int getScaleUnits() {
		return scaleUnits;
	}

	public void setScaleUnits(int scaleUnits) {
		this.scaleUnits = scaleUnits;
	}

	public int getCaptureDeviceSpatialSamplingRateHorizontal() {
		return captureDeviceSpatialSamplingRateHorizontal;
	}

	public void setCaptureDeviceSpatialSamplingRateHorizontal(int captureDeviceSpatialSamplingRateHorizontal) {
		this.captureDeviceSpatialSamplingRateHorizontal = captureDeviceSpatialSamplingRateHorizontal;
	}

	public int getCaptureDeviceSpatialSamplingRateVertical() {
		return captureDeviceSpatialSamplingRateVertical;
	}

	public void setCaptureDeviceSpatialSamplingRateVertical(int captureDeviceSpatialSamplingRateVertical) {
		this.captureDeviceSpatialSamplingRateVertical = captureDeviceSpatialSamplingRateVertical;
	}

	public int getImageSpatialSamplingRateHorizontal() {
		return imageSpatialSamplingRateHorizontal;
	}

	public void setImageSpatialSamplingRateHorizontal(int imageSpatialSamplingRateHorizontal) {
		this.imageSpatialSamplingRateHorizontal = imageSpatialSamplingRateHorizontal;
	}

	public int getImageSpatialSamplingRateVertical() {
		return imageSpatialSamplingRateVertical;
	}

	public void setImageSpatialSamplingRateVertical(int imageSpatialSamplingRateVertical) {
		this.imageSpatialSamplingRateVertical = imageSpatialSamplingRateVertical;
	}

	public int getBitDepth() {
		return bitDepth;
	}

	public void setBitDepth(int bitDepth) {
		this.bitDepth = bitDepth;
	}

	public int getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(int compressionType) {
		this.compressionType = compressionType;
	}

	public int getImpressionType() {
		return impressionType;
	}

	public void setImpressionType(int impressionType) {
		this.impressionType = impressionType;
	}

	public int getLineLengthHorizontal() {
		return lineLengthHorizontal;
	}

	public void setLineLengthHorizontal(int lineLengthHorizontal) {
		this.lineLengthHorizontal = lineLengthHorizontal;
	}

	public int getLineLengthVertical() {
		return lineLengthVertical;
	}

	public void setLineLengthVertical(int lineLengthVertical) {
		this.lineLengthVertical = lineLengthVertical;
	}

	@Override
	public String toString() {
		return "\nRepresentationHeader [RecordLength=" + getRecordLength() + ", representationDataLength="
				+ representationDataLength + ", representationLength=" + representationLength + ", captureDateTime="
				+ captureDateTime + ", captureYear=" + captureYear + ", captureMonth=" + captureMonth + ", captureDay="
				+ captureDay + ", captureHour=" + captureHour + ", captureMinute=" + captureMinute + ", captureSecond="
				+ captureSecond + ", captureMilliSecond=" + captureMilliSecond + ", captureDeviceTechnologyIdentifier="
				+ Integer.toHexString(captureDeviceTechnologyIdentifier) + ", captureDeviceVendorIdentifier="
				+ Integer.toHexString(captureDeviceVendorIdentifier) + ", captureDeviceTypeIdentifier="
				+ Integer.toHexString(captureDeviceTypeIdentifier) + ", noOfQualityBlocks="
				+ Integer.toHexString(noOfQualityBlocks) + ", qualityBlocks=" + Arrays.toString(qualityBlocks)
				+ ", certificationFlag=" + Integer.toHexString(certificationFlag) + ", noOfCertificationBlocks="
				+ Integer.toHexString(noOfCertificationBlocks) + ", certificationBlocks="
				+ Arrays.toString(certificationBlocks) + ", fingerPosition=" + fingerPosition + ", representationNo="
				+ Integer.toHexString(representationNo) + ", scaleUnits=" + scaleUnits
				+ ", captureDeviceSpatialSamplingRateHorizontal="
				+ Integer.toHexString(captureDeviceSpatialSamplingRateHorizontal)
				+ ", captureDeviceSpatialSamplingRateVertical="
				+ Integer.toHexString(captureDeviceSpatialSamplingRateVertical)
				+ ", imageSpatialSamplingRateHorizontal=" + Integer.toHexString(imageSpatialSamplingRateHorizontal)
				+ ", imageSpatialSamplingRateVertical=" + Integer.toHexString(imageSpatialSamplingRateVertical)
				+ ", bitDepth=" + Integer.toHexString(bitDepth) + ", compressionType="
				+ Integer.toHexString(compressionType) + ", impressionType=" + Integer.toHexString(impressionType)
				+ ", lineLengthHorizontal=" + Integer.toHexString(lineLengthHorizontal) + ", lineLengthVertical="
				+ Integer.toHexString(lineLengthVertical) + "]\n";
	}
}
