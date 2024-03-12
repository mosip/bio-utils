package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;

public class ImageInformation extends AbstractImageInfo {
	private int faceImageType;
	private int imageDataType;
	private int width;
	private int height;
	private int spatialSamplingRateLevel;
	private int postAcquistionProcessing;
	private int crossReference;
	private int imageColorSpace;

	public ImageInformation(int width, int height) {
		setFaceImageType(FaceImageType.BASIC);
		setImageDataType(ImageDataType.JPEG2000_LOSS_LESS);
		setWidth(width);
		setHeight(height);
		setSpatialSamplingRateLevel(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180);
		setPostAcquistionProcessing(0);
		setCrossReference(CrossReference.BASIC);
		setImageColorSpace(ImageColourSpace.UNSPECIFIED);
	}

	public ImageInformation(int faceImageType, int imageDataType, int width, int height, int spatialSamplingRateLevel,
			int postAcquistionProcessing, int crossReference, int imageColorSpace) {
		setFaceImageType(faceImageType);
		setImageDataType(imageDataType);
		setWidth(width);
		setHeight(height);
		setSpatialSamplingRateLevel(spatialSamplingRateLevel);
		setPostAcquistionProcessing(postAcquistionProcessing);
		setCrossReference(crossReference);
		setImageColorSpace(imageColorSpace);
	}

	public ImageInformation(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public ImageInformation(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setFaceImageType(inputStream.readUnsignedByte());
		setImageDataType(inputStream.readUnsignedByte());
		setWidth(inputStream.readUnsignedShort());
		setHeight(inputStream.readUnsignedShort());
		setSpatialSamplingRateLevel(inputStream.readUnsignedByte());
		setPostAcquistionProcessing(inputStream.readUnsignedShort());
		setCrossReference(inputStream.readUnsignedByte());
		setImageColorSpace(inputStream.readUnsignedByte());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setFaceImageType(inputStream.readUnsignedByte());
		setImageDataType(inputStream.readUnsignedByte());
		/**
		 *  2(Width) + 2(Height) + 1(SpatialSamplingRateLevel)  + 2(PostAcquistionProcessing) + 1(CrossReference) + 1 (ImageColorSpace)
		 */
		inputStream.skip(9);
	}

	@Override
	public long getRecordLength() {
		return 11; /** 1 + 1 + 2 + 2 + 1 + 2 + 1 + 1 (Figure 2 ISO/IEC 19794-5) */
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeByte(getFaceImageType()); /** 1 */
		outputStream.writeByte(getImageDataType()); /** + 1 = 2 */
		outputStream.writeShort(getWidth()); /** + 2 = 4 */
		outputStream.writeShort(getHeight()); /** + 2 = 6 */
		outputStream.writeByte(getSpatialSamplingRateLevel()); /** + 1 = 7 */
		outputStream.writeShort(getPostAcquistionProcessing()); /** + 2 = 9 */
		outputStream.writeByte(getCrossReference()); /** + 1 = 10 */
		outputStream.writeByte(getImageColorSpace()); /** + 1 = 11 */
		outputStream.flush();
	}

	public int getFaceImageType() {
		return faceImageType;
	}

	public void setFaceImageType(int faceImageType) {
		this.faceImageType = faceImageType;
	}

	public int getImageDataType() {
		return imageDataType;
	}

	public void setImageDataType(int imageDataType) {
		this.imageDataType = imageDataType;
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

	public int getSpatialSamplingRateLevel() {
		return spatialSamplingRateLevel;
	}

	public void setSpatialSamplingRateLevel(int spatialSamplingRateLevel) {
		this.spatialSamplingRateLevel = spatialSamplingRateLevel;
	}

	public int getPostAcquistionProcessing() {
		return postAcquistionProcessing;
	}

	public void setPostAcquistionProcessing(int postAcquistionProcessing) {
		this.postAcquistionProcessing = postAcquistionProcessing;
	}

	public int getCrossReference() {
		return crossReference;
	}

	public void setCrossReference(int crossReference) {
		this.crossReference = crossReference;
	}

	public int getImageColorSpace() {
		return imageColorSpace;
	}

	public void setImageColorSpace(int imageColorSpace) {
		this.imageColorSpace = imageColorSpace;
	}

	@Override
	public String toString() {
		return "\nImageInformation [ImageInformationRecordLength=" + getRecordLength() + ", faceImageType="
				+ Integer.toHexString(faceImageType) + ", imageDataType=" + Integer.toHexString(imageDataType)
				+ ", width=" + Integer.toHexString(width) + ", height=" + Integer.toHexString(height)
				+ ", spatialSamplingRateLevel=" + Integer.toHexString(spatialSamplingRateLevel)
				+ ", postAcquistionProcessing=" + Integer.toHexString(postAcquistionProcessing) + ", crossReference="
				+ Integer.toHexString(crossReference) + ", imageColorSpace=" + Integer.toHexString(imageColorSpace)
				+ "]\n";
	}
}
