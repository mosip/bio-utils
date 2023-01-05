package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageInformation extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageInformation.class);

	private int eyeLabel;
	private int imageType;
	private int imageFormat;
	private int horizontalOrientation;
	private int verticalOrientation;
	private int compressionType;
	private int width;
	private int height;
	private int bitDepth;
	private int range;
	private int rollAngleOfEye;
	private int rollAngleUncertainty;
	private int irisCenterSmallestX;
	private int irisCenterLargestX;
	private int irisCenterSmallestY;
	private int irisCenterLargestY;
	private int irisDiameterSmallest;
	private int irisDiameterLargest;

	public ImageInformation(int eyeLabel, int imageType, int imageFormat, int horizontalOrientation,
			int verticalOrientation, int compressionType, int width, int height, int bitDepth, int range,
			int rollAngleOfEye, int rollAngleUncertainty, int irisCenterSmallestX, int irisCenterLargestX,
			int irisCenterSmallestY, int irisCenterLargestY, int irisDiameterSmallest, int irisDiameterLargest) {
		super();
		setEyeLabel(eyeLabel);
		setImageType(imageType);
		setImageFormat(imageFormat);
		setHorizontalOrientation(horizontalOrientation);
		setVerticalOrientation(verticalOrientation);
		setCompressionType(compressionType);
		setWidth(width);
		setHeight(height);
		setBitDepth(bitDepth);
		setRange(range);
		setRollAngleOfEye(rollAngleOfEye);
		setRollAngleUncertainty(rollAngleUncertainty);
		setIrisCenterSmallestX(irisCenterSmallestX);
		setIrisCenterLargestX(irisCenterLargestX);
		setIrisCenterSmallestY(irisCenterSmallestY);
		setIrisCenterLargestY(irisCenterLargestY);
		setIrisDiameterSmallest(irisDiameterSmallest);
		setIrisDiameterLargest(irisDiameterLargest);
	}

	public ImageInformation(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public ImageInformation(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		int eyeLabel = inputStream.readUnsignedByte();
		try {
			setEyeLabel(eyeLabel);
		} catch (Exception ex) {
			LOGGER.error("setEyeLabel :: Not Defined :: eyeLabel :: " + eyeLabel);
		}

		int imageType = inputStream.readUnsignedByte();
		try {
			setImageType(imageType);
		} catch (Exception ex) {
			LOGGER.error("setImageType :: Not Defined :: imageType :: " + imageType);
		}

		int imageFormat = inputStream.readUnsignedByte();
		try {
			setImageFormat(imageFormat);
		} catch (Exception ex) {
			LOGGER.error("setImageFormat :: Not Defined :: imageFormat :: " + imageFormat);
		}
		/*
		 * 8 7 6 5 4 3 2 1 [ | | | | | | | | ] 1 1 = 0x0003 horizontalOrientation (>> 0)
		 * 1 1 0 0 = 0x000C verticalOrientation (>> 2) 1 1 0 0 0 0 = 0x0060 empty (>> 4)
		 * 1 1 0 0 0 0 0 0 = 0x0080 compressionType (>> 6)
		 */
		int imagePropertiesBits = inputStream.readUnsignedByte();
		int horizontalOrientation = imagePropertiesBits & 0x0003;
		try {
			setHorizontalOrientation(horizontalOrientation);
		} catch (Exception ex) {
			LOGGER.error(
					"setHorizontalOrientation :: Not Defined :: horizontalOrientation :: " + horizontalOrientation);
		}
		int verticalOrientation = (imagePropertiesBits & 0x000C) >> 2;
		try {
			setVerticalOrientation(verticalOrientation);
		} catch (Exception ex) {
			LOGGER.error("setVerticalOrientation :: Not Defined :: verticalOrientation :: " + verticalOrientation);
		}

		int futureType = (imagePropertiesBits & 0x0030) >> 4; // NO USED NOW
		int compressionType = (imagePropertiesBits & 0x00C0) >> 6;

		try {
			setCompressionType(compressionType);
		} catch (Exception ex) {
			LOGGER.error("setCompressionType :: Not Defined :: compressionType :: " + compressionType);
		}
		//System.out.println(imagePropertiesBits + " >> " + horizontalOrientation + " " + verticalOrientation + " " + compressionType);
		setWidth(inputStream.readUnsignedShort());
		setHeight(inputStream.readUnsignedShort());

		int bitDepth = inputStream.readUnsignedByte();
		try {
			setBitDepth((byte) bitDepth);
		} catch (Exception ex) {
			LOGGER.error("setBitDepth :: Not Defined :: bitDepth :: " + bitDepth);
		}
		setRange(inputStream.readUnsignedShort());
		setRollAngleOfEye(inputStream.readUnsignedShort());
		setRollAngleUncertainty(inputStream.readUnsignedShort());
		setIrisCenterSmallestX(inputStream.readUnsignedShort());
		setIrisCenterLargestX(inputStream.readUnsignedShort());
		setIrisCenterSmallestY(inputStream.readUnsignedShort());
		setIrisCenterLargestY(inputStream.readUnsignedShort());
		setIrisDiameterSmallest(inputStream.readUnsignedShort());
		setIrisDiameterLargest(inputStream.readUnsignedShort());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		int eyeLabel = inputStream.readUnsignedByte();
		try {
			setEyeLabel(eyeLabel);
		} catch (Exception ex) {
			LOGGER.error("setEyeLabel :: Not Defined :: eyeLabel :: " + eyeLabel);
		}

		int imageType = inputStream.readUnsignedByte();
		try {
			setImageType(imageType);
		} catch (Exception ex) {
			LOGGER.error("setImageType :: Not Defined :: imageType :: " + imageType);
		}

		int imageFormat = inputStream.readUnsignedByte();
		try {
			setImageFormat(imageFormat);
		} catch (Exception ex) {
			LOGGER.error("setImageFormat :: Not Defined :: imageFormat :: " + imageFormat);
		}
		/*
		 * 8 7 6 5 4 3 2 1 [ | | | | | | | | ] 1 1 = 0x0003 horizontalOrientation (>> 0)
		 * 1 1 0 0 = 0x000C verticalOrientation (>> 2) 1 1 0 0 0 0 = 0x0060 empty (>> 4)
		 * 1 1 0 0 0 0 0 0 = 0x0080 compressionType (>> 6)
		 */
		int imagePropertiesBits = inputStream.readUnsignedByte();
		int horizontalOrientation = imagePropertiesBits & 0x0003;
		try {
			setHorizontalOrientation(horizontalOrientation);
		} catch (Exception ex) {
			LOGGER.error(
					"setHorizontalOrientation :: Not Defined :: horizontalOrientation :: " + horizontalOrientation);
		}
		int verticalOrientation = (imagePropertiesBits & 0x000C) >> 2;
		try {
			setVerticalOrientation(verticalOrientation);
		} catch (Exception ex) {
			LOGGER.error("setVerticalOrientation :: Not Defined :: verticalOrientation :: " + verticalOrientation);
		}

		int futureType = (imagePropertiesBits & 0x0030) >> 4; // NO USED NOW
		int compressionType = (imagePropertiesBits & 0x00C0) >> 6;

		try {
			setCompressionType(compressionType);
		} catch (Exception ex) {
			LOGGER.error("setCompressionType :: Not Defined :: compressionType :: " + compressionType);
		}
		// 2(Width) + 2(Height) + 1(bitDepth) + 2(Range) + 2(RollAngleOfEye) 
		// + 2(RollAngleUncertainty) + 2(IrisCenterSmallestX) + 2(IrisCenterLargestX) 
		// + 2(IrisCenterSmallestY) + 2(IrisCenterLargestY) 
		// + 2(IrisDiameterSmallest) + 2(IrisDiameterLargest) 
		inputStream.skip(23);
	}

	@Override
	public long getRecordLength() {
		return 27; /*
					 * 1 + 1 + 1 + 1 + 2 + 2 + 1 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 (Table 4 �
					 * Iris representation header ISO/IEC 19794-6-2011)
					 */
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeByte(getEyeLabel()); /* 1 */
		outputStream.writeByte(getImageType()); /* + 1 = 2 */
		outputStream.writeByte(getImageFormat()); /* + 1 = 3 */

		byte imagePropertiesBits = 0;
		imagePropertiesBits |= (getHorizontalOrientation() & 0x0003); // Bit 1-2
		imagePropertiesBits |= ((getVerticalOrientation() << 2) & 0x000C); // Bit 3-4
		imagePropertiesBits |= ((0 << 4) & 0x0030); // Bit 5-6
		imagePropertiesBits |= ((getCompressionType() << 6) & 0x00C0); // Bit 7-8
		outputStream.writeByte(imagePropertiesBits); /* + 1 = 4 */

		outputStream.writeShort(getWidth()); /* + 2 = 6 */
		outputStream.writeShort(getHeight()); /* + 2 = 8 */
		outputStream.writeByte(getBitDepth()); /* + 1 = 9 */
		outputStream.writeShort(getRange()); /* + 2 = 11 */
		outputStream.writeShort(getRollAngleOfEye()); /* + 2 = 13 */
		outputStream.writeShort(getRollAngleUncertainty()); /* + 2 = 15 */
		outputStream.writeShort(getIrisCenterSmallestX()); /* + 2 = 17 */
		outputStream.writeShort(getIrisCenterLargestX()); /* + 2 = 19 */
		outputStream.writeShort(getIrisCenterSmallestY()); /* + 2 = 21 */
		outputStream.writeShort(getIrisCenterLargestY()); /* + 2 = 23 */
		outputStream.writeShort(getIrisDiameterSmallest()); /* + 2 = 25 */
		outputStream.writeShort(getIrisDiameterLargest()); /* + 2 = 27 */
		outputStream.flush();
	}

	public int getEyeLabel() {
		return eyeLabel;
	}

	public void setEyeLabel(int eyeLabel) {
		this.eyeLabel = eyeLabel;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public int getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(int imageFormat) {
		this.imageFormat = imageFormat;
	}

	public int getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(int horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	public int getVerticalOrientation() {
		return verticalOrientation;
	}

	public void setVerticalOrientation(int verticalOrientation) {
		this.verticalOrientation = verticalOrientation;
	}

	public int getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(int compressionType) {
		this.compressionType = compressionType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBitDepth() {
		return bitDepth;
	}

	public void setBitDepth(int bitDepth) {
		this.bitDepth = bitDepth;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRollAngleOfEye() {
		return rollAngleOfEye;
	}

	public void setRollAngleOfEye(int rollAngleOfEye) {
		this.rollAngleOfEye = rollAngleOfEye;
	}

	public int getRollAngleUncertainty() {
		return rollAngleUncertainty;
	}

	public void setRollAngleUncertainty(int rollAngleUncertainty) {
		this.rollAngleUncertainty = rollAngleUncertainty;
	}

	public int getIrisCenterSmallestX() {
		return irisCenterSmallestX;
	}

	public void setIrisCenterSmallestX(int irisCenterSmallestX) {
		this.irisCenterSmallestX = irisCenterSmallestX;
	}

	public int getIrisCenterLargestX() {
		return irisCenterLargestX;
	}

	public void setIrisCenterLargestX(int irisCenterLargestX) {
		this.irisCenterLargestX = irisCenterLargestX;
	}

	public int getIrisCenterSmallestY() {
		return irisCenterSmallestY;
	}

	public void setIrisCenterSmallestY(int irisCenterSmallestY) {
		this.irisCenterSmallestY = irisCenterSmallestY;
	}

	public int getIrisCenterLargestY() {
		return irisCenterLargestY;
	}

	public void setIrisCenterLargestY(int irisCenterLargestY) {
		this.irisCenterLargestY = irisCenterLargestY;
	}

	public int getIrisDiameterSmallest() {
		return irisDiameterSmallest;
	}

	public void setIrisDiameterSmallest(int irisDiameterSmallest) {
		this.irisDiameterSmallest = irisDiameterSmallest;
	}

	public int getIrisDiameterLargest() {
		return irisDiameterLargest;
	}

	public void setIrisDiameterLargest(int irisDiameterLargest) {
		this.irisDiameterLargest = irisDiameterLargest;
	}

	@Override
	public String toString() {
		return "\nImageInformation [RecordLength=" + getRecordLength() + ", eyeLabel=" + Integer.toHexString(eyeLabel)
				+ ", imageType=" + Integer.toHexString(imageType) + ", imageFormat=" + Integer.toHexString(imageFormat)
				+ ", horizontalOrientation=" + Integer.toHexString(horizontalOrientation) + ", verticalOrientation="
				+ Integer.toHexString(verticalOrientation) + ", compressionType=" + Integer.toHexString(compressionType)
				+ ", width=" + Integer.toHexString(width) + ", height=" + Integer.toHexString(height) + ", bitDepth="
				+ Integer.toHexString(bitDepth) + ", range=" + Integer.toHexString(range) + ", rollAngleOfEye="
				+ Integer.toHexString(rollAngleOfEye) + ", rollAngleUncertainty="
				+ Integer.toHexString(rollAngleUncertainty) + ", irisCenterSmallestX="
				+ Integer.toHexString(irisCenterSmallestX) + ", irisCenterLargestX="
				+ Integer.toHexString(irisCenterLargestX) + ", irisCenterSmallestY="
				+ Integer.toHexString(irisCenterSmallestY) + ", irisCenterLargestY="
				+ Integer.toHexString(irisCenterLargestY) + ", irisDiameterSmallest="
				+ Integer.toHexString(irisDiameterSmallest) + ", irisDiameterLargest="
				+ Integer.toHexString(irisDiameterLargest) + "]\n";
	}
}
