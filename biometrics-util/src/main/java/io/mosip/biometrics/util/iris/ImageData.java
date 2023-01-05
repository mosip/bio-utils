package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageData extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageData.class);

	private long imageLength;
	private byte[] image;

	public ImageData(byte[] image) {
		setImageLength(image.length);
		setImage(image);
	}

	public ImageData(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public ImageData(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setImageLength((inputStream.readInt() & 0xFFFFFFFFL)); /* 4 */

		setImage(new byte[(int) getImageLength()]);
		inputStream.readFully(getImage());
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream);
	}

	@Override
	public long getRecordLength() {
		return 4 + getImageLength();
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt((int) getImageLength()); // 4 bytes
		if (getImage() != null && getImage().length > 0)
			outputStream.write(getImage(), 0, (int) getImageLength());// 4 + ImageLength
		outputStream.flush();
	}

	public long getImageLength() {
		return imageLength;
	}

	public void setImageLength(long imageLength) {
		this.imageLength = imageLength;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "\nIrisImageData [ImageDataRecordLength=" + getRecordLength() + ", imageLength=" + imageLength + "]\n";
	}
}
