package io.mosip.biometrics.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractImageInfo {
	public abstract int getRecordLength();
	
	protected abstract void readObject(DataInputStream in) throws IOException;
	protected abstract void writeObject(DataOutputStream out) throws IOException;
}
