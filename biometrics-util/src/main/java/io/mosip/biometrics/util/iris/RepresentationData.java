package io.mosip.biometrics.util.iris;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepresentationData extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(RepresentationData.class);

	private ImageData imageData;

	public RepresentationData(ImageData imageData) {
		setImageData(imageData);
	}

	public RepresentationData(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public RepresentationData(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		setImageData(new ImageData(inputStream));
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setImageData(new ImageData(inputStream, onlyImageInformation));
	}

	@Override
	public long getRecordLength() {
		return getImageData().getRecordLength();
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		getImageData().writeObject(outputStream);
		outputStream.flush();
	}

	public ImageData getImageData() {
		return imageData;
	}

	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}

	@Override
	public String toString() {
		return "\nRepresentationData [RepresentationDataRecordLength=" + getRecordLength() + ", imageData=" + imageData
				+ "]\n";
	}
}
