package io.mosip.biometrics.util;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public abstract class ISOStandardsValidator {
	public boolean isValidCaptureDateTime(int year, int month, int day, int hour, int minute, int second,
			int milliSecond) {
		/*
		 * If date/time is not provided, all bytes should be set to 0xff. Individual
		 * date/time fields can be also set to 0xff or 0xffff, indicating that this
		 * component of the date/time is not provided. In that case, all fields
		 * describing higher resolution date/time components should be also set to
		 * 0xff/0xffff. This allows implementations to omit milliseconds or to provide
		 * only date without time.
		 */
		if (year == 0xFFFF && month == 0xFF && day == 0xFF && hour == 0xFF && minute == 0xFF && second == 0xFF
				&& milliSecond == 0xFFFF)
			return true;

		if (year == 0xFFFF && month == 0xFF && day == 0xFF)
			if (hour >= 0x00 && hour <= 0x17)
				if (minute >= 0x00 && minute <= 0x3B)
					if (second >= 0x00 && second <= 0x3B)
						if (milliSecond >= 0x00 && milliSecond <= 0x03E7)
							return true;

		if (year >= 0x01 && year <= 0xFFFF)
			if (month >= 0x01 && month <= 0x0C)
				if (day >= 0x01 && day <= 0x1F)
					if (hour == 0xFF && minute == 0xFF && second == 0xFF && milliSecond == 0xFFFF)
						return true;

		if (year >= 0x01 && year <= 0xFFFF)
			if (month >= 0x01 && month <= 0x0C)
				if (day >= 0x01 && day <= 0x1F)
					if (hour >= 0x00 && hour <= 0x17)
						if (minute >= 0x00 && minute <= 0x3B)
							if (second >= 0x00 && second <= 0x3B)
								if (milliSecond >= 0x00 && milliSecond <= 0x03E7)
									return true;
		return false;
	}

	public boolean isValidImageData(String purpose, Modality modality, byte[] inImageData) throws Exception {
		switch (Purposes.fromCode(purpose)) {
		case AUTH:
			if (isJP2000(inImageData) || isWSQ(inImageData)) {
				return true;
			}
			break;
		case REGISTRATION:
			if (isJP2000(inImageData)) {
				return true;
			}
			break;
		}

		return false;
	}

	/**
	 * Some Extra info about other file format with jpeg: initial of file contains
	 * these bytes BMP : 42 4D JPG : FF D8 FF EO ( Starting 2 Byte will always be
	 * same) PNG : 89 50 4E 47 GIF : 47 49 46 38 When a JPG file uses JFIF or EXIF,
	 * The signature is different : Raw : FF D8 FF DB JFIF : FF D8 FF E0 EXIF : FF
	 * D8 FF E1 WSQ : check with marker from WsqDecoder JP2000: 6a 70 32 68 // JP2
	 * Header
	 */
	public boolean isJP2000(byte[] imageData) throws Exception {
		boolean isValid = false;
		DataInputStream ins = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(imageData)));
		try {
			// Make sure that the first 12 bytes is the JP2_SIGNATURE_BOX
			// or if not that the first 2 bytes is the SOC marker
			while (true) {
				if (ins.readInt() == 0x0000000c || ins.readInt() == 0x6a703268 || ins.readInt() == 0x0d0a870a) { // a
																													// JP2
																													// file
					isValid = true;
					break;
				}
				break;
			}
		} finally {
			ins.close();
		}
		return isValid;
	}

	public boolean isWSQ(byte[] imageData) throws Exception {

		try {
			WsqDecoder decoder = new WsqDecoder();
			Bitmap bitmap = decoder.decode(imageData);
			if (bitmap != null && bitmap.getPixels() != null && bitmap.getPixels().length > 0) {
				return true;
			} else {
				return false;

			}
		} finally {
		}
	}

	public boolean isValidImageDataLength(byte[] imageData, long imageDataLength) {
		if (imageData != null && imageData.length == (int) imageDataLength)
			return true;
		return false;
	}
}
