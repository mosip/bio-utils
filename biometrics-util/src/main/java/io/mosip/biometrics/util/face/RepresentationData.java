package io.mosip.biometrics.util.face;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.mosip.biometrics.util.AbstractImageInfo;

public class RepresentationData extends AbstractImageInfo {
	private ImageData imageData;
	private byte[] threeDInformationAndData;

	public RepresentationData(ImageData imageData, byte[] threeDInformationAndData) {
		setImageData(imageData);
		setThreeDInformationAndData(threeDInformationAndData);
	}

	public RepresentationData(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public RepresentationData(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	protected void readObject(DataInputStream inputStream) throws IOException {
		setImageData(new ImageData(inputStream));
		try {
			if (inputStream.available() > 0) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int next = inputStream.read();
				while (next > -1) {
					bos.write(next);
					next = inputStream.read();
				}
				bos.flush();
				setThreeDInformationAndData(bos.toByteArray());
				bos.close();
			}
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setImageData(new ImageData(inputStream, onlyImageInformation));
	}

	public long getRecordLength() {
		int threeDInformationAndDataLength = 0;
		if (getThreeDInformationAndData() != null)
			threeDInformationAndDataLength = getThreeDInformationAndData().length;
		return getImageData().getRecordLength() + threeDInformationAndDataLength;
	}

	public void writeObject(DataOutputStream outputStream) throws IOException {
		getImageData().writeObject(outputStream);
		if (getThreeDInformationAndData() != null) {
			outputStream.write(getThreeDInformationAndData(), 0, getThreeDInformationAndData().length);
		}
		outputStream.flush();
	}

	public ImageData getImageData() {
		return imageData;
	}

	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}

	public byte[] getThreeDInformationAndData() {
		return threeDInformationAndData;
	}

	public void setThreeDInformationAndData(byte[] threeDInformationAndData) {
		this.threeDInformationAndData = threeDInformationAndData;
	}

	@Override
	public String toString() {
		return "\nRepresentationData [RepresentationDataRecordLength=" + getRecordLength() + ", imageData=" + imageData
				+ ", threeDInformationAndData=" + Arrays.toString(threeDInformationAndData) + "]\n";
	}
}