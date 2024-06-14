package io.mosip.biometrics.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Abstract base class for representing image information. Provides methods for
 * reading from and writing to data streams.
 */
public abstract class AbstractImageInfo {
	/**
	 * Retrieves the length of the record.
	 *
	 * @return The length of the record in bytes.
	 */
	public abstract long getRecordLength();

	/**
	 * Reads object data from the input stream.
	 *
	 * @param inputStream The data input stream from which to read.
	 * @throws IOException If an I/O error occurs while reading from the stream.
	 */
	protected abstract void readObject(DataInputStream inputStream) throws IOException;

	/**
	 * Reads object data from the input stream, with an option to read only image
	 * information.
	 *
	 * @param inputStream          The data input stream from which to read.
	 * @param onlyImageInformation Flag indicating whether to read only image
	 *                             information.
	 * @throws IOException If an I/O error occurs while reading from the stream.
	 */
	protected abstract void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException;

	/**
	 * Writes object data to the output stream.
	 *
	 * @param outputStream The data output stream to which to write.
	 * @throws IOException If an I/O error occurs while writing to the stream.
	 */
	protected abstract void writeObject(DataOutputStream outputStream) throws IOException;
}