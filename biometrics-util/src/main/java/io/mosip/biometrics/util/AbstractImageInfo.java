package io.mosip.biometrics.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractImageInfo {
	public abstract long getRecordLength();

	protected abstract void readObject(DataInputStream inputStream) throws IOException;

	protected abstract void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException;

	protected abstract void writeObject(DataOutputStream outputStream) throws IOException;
}
