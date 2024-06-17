package io.mosip.biometrics.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * Abstract class for validating ISO standards related to biometric data.
 */
public abstract class ISOStandardsValidator {
	/**
	 * Validates the capture date and time based on ISO standards.
	 *
	 * @param year        the year component of the date/time
	 * @param month       the month component of the date/time
	 * @param day         the day component of the date/time
	 * @param hour        the hour component of the time
	 * @param minute      the minute component of the time
	 * @param second      the second component of the time
	 * @param milliSecond the millisecond component of the time
	 * @return true if the date/time components are valid according to ISO
	 *         standards, false otherwise
	 */
	@SuppressWarnings({ "java:S1066", "java:S3776" })
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

	/**
	 * Validates the image data based on ISO standards for a given purpose and
	 * modality.
	 *
	 * @param purpose           the purpose of the biometric data (e.g., AUTH or
	 *                          REGISTRATION)
	 * @param modality          the modality of the biometric data (e.g., Finger,
	 *                          Face)
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image data is valid according to ISO standards, false
	 *         otherwise
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageData(String purpose, Modality modality, ImageDecoderRequestDto decoderRequestDto) {
		switch (Purposes.fromCode(purpose)) {
		case AUTH:
			if (isJP2000(true, decoderRequestDto) || isWSQ(true, decoderRequestDto)) {
				return true;
			}
			break;
		case REGISTRATION:
			if (isJP2000(false, decoderRequestDto)) {
				return true;
			}
			break;
		default:
			return false;
		}

		return false;
	}

	/**
	 * Retrieves the biometric data type (e.g., JPEG2000, WSQ) based on ISO
	 * standards.
	 *
	 * @param purpose     the purpose of the biometric data (e.g., AUTH or
	 *                    REGISTRATION)
	 * @param modality    the modality of the biometric data (e.g., Finger, Iris)
	 * @param inImageData the byte array of the image data
	 * @return the biometric data type as an integer value, or -1 if not recognized
	 * @throws Exception if there is an error processing the image data
	 */
	public int getBioDataType(String purpose, Modality modality, byte[] inImageData) throws Exception {
		switch (modality) {
		case Finger, Iris, Face:
			switch (Purposes.fromCode(purpose)) {
			case AUTH:
				if (isJP2000(inImageData)) {
					return ImageType.JPEG2000.value();
				}
				if (isWSQ(inImageData)) {
					return ImageType.WSQ.value();
				}
				break;
			case REGISTRATION:
				if (isJP2000(inImageData)) {
					return ImageType.JPEG2000.value();
				}
				break;
			default:
				return -1;
			}
			break;
		case UnSpecified:
			break;
		default:
			return -1;
		}

		return -1;
	}

	/**
	 * Checks if the image type in the decoder request DTO is JP2000 format.
	 *
	 * @param isAuth            indicates if authentication mode is enabled
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image type is JP2000, false otherwise
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isJP2000(boolean isAuth, ImageDecoderRequestDto decoderRequestDto) {
		return (decoderRequestDto.getImageType().equals("JP2000"));
	}

	/**
	 * Checks if the image type in the decoder request DTO is WSQ format and is used
	 * in authentication mode.
	 *
	 * @param isAuth            indicates if authentication mode is enabled
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image type is WSQ and used in authentication mode, false
	 *         otherwise
	 */
	public boolean isWSQ(boolean isAuth, ImageDecoderRequestDto decoderRequestDto) {
		return (isAuth && decoderRequestDto.getImageType().equals("WSQ"));
	}

	/**
	 * Checks if the image data is in JPEG2000 format. Some Extra info about other
	 * file format with jpeg: initial of file contains these bytes BMP : 42 4D JPG :
	 * FF D8 FF EO ( Starting 2 Byte will always be same) PNG : 89 50 4E 47 GIF : 47
	 * 49 46 38 When a JPG file uses JFIF or EXIF, The signature is different : Raw
	 * : FF D8 FF DB JFIF : FF D8 FF E0 EXIF : FF D8 FF E1 WSQ : check with marker
	 * from WsqDecoder JP2000: 6a 70 32 68 // JP2 Header
	 *
	 * @param imageData the byte array of the image data
	 * @return true if the image data is in JPEG2000 format, false otherwise
	 * @throws Exception if there is an error processing the image data
	 */
	@SuppressWarnings({ "java:S112", "java:S135" })
	public boolean isJP2000(byte[] imageData) throws Exception {
		boolean isValid = false;
		try (DataInputStream ins = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(imageData)))) {
			// Make sure that the first 12 bytes is the JP2_SIGNATURE_BOX
			// or if not that the first 2 bytes is the SOC marker
			while (true) {
				// a JP2 file
				if (ins.readInt() == 0x0000000c || ins.readInt() == 0x6a703268 || ins.readInt() == 0x0d0a870a) {
					isValid = true;
					break;
				}
				break;
			}
		}
		return isValid;
	}

	/**
	 * Checks if the image data is in WSQ format.
	 *
	 * @param imageData the byte array of the image data
	 * @return true if the image data is in WSQ format, false otherwise
	 * @throws Exception if there is an error processing the image data
	 */
	@SuppressWarnings({ "java:S112", "java:S125", "java:S135" })
	public boolean isWSQ(byte[] imageData) throws Exception {
		boolean isValid = false;

		try (DataInputStream ins = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(imageData)))) {
			while (true) {
				// a wsq file WSQ Marker Definitions
				/*
				 * SOI_WSQ value 0xffa0;
				 */
				if (ins.readUnsignedShort() == 0xffa0) {
					isValid = true;
					break;
				}
				break;
			}
		}
		return isValid;
	}

	/**
	 * Validates the length of the image data.
	 *
	 * @param imageData       the byte array of the image data
	 * @param imageDataLength the expected length of the image data
	 * @return true if the image data length matches the expected length, false
	 *         otherwise
	 */
	public boolean isValidImageDataLength(byte[] imageData, long imageDataLength) {
		return (imageData != null && imageData.length == (int) imageDataLength);
	}

	/**
	 * Checks if the image compression ratio is valid based on ISO standards.
	 *
	 * @param purpose           the purpose of the biometric data (e.g., AUTH or
	 *                          REGISTRATION)
	 * @param modality          the modality of the biometric data (e.g., Finger,
	 *                          Face)
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image compression ratio is valid, false otherwise
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageCompressionRatio(String purpose, Modality modality,
			ImageDecoderRequestDto decoderRequestDto) {
		switch (Purposes.fromCode(purpose)) {
		case AUTH:
			String ratio = decoderRequestDto.getImageCompressionRatio();
			if (ratio != null && !ratio.isBlank() && !ratio.isEmpty()) {
				String[] arrRatio = ratio.split(":");
				// Up to 15:1
				if ((arrRatio != null && arrRatio.length > 0) && (Float.parseFloat(arrRatio[0].trim()) > 0.0f
						&& Float.parseFloat(arrRatio[0].trim()) <= 15.0f))
					return true;
			}
			break;
		case REGISTRATION:
			return true;
		default:
			return false;
		}

		return false;
	}

	/**
	 * Checks if the image aspect ratio is valid based on ISO standards.
	 *
	 * @param purpose           the purpose of the biometric data (e.g., AUTH or
	 *                          REGISTRATION)
	 * @param modality          the modality of the biometric data (e.g., Finger,
	 *                          Face)
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image aspect ratio is valid, false otherwise
	 */
	@SuppressWarnings({ "java:S1172" })
	public boolean isValidImageAspectRatio(String purpose, Modality modality,
			ImageDecoderRequestDto decoderRequestDto) {
		switch (Purposes.fromCode(purpose)) {
		case AUTH:
			String ratio = decoderRequestDto.getImageAspectRatio();
			if (ratio != null && !ratio.isBlank() && !ratio.isEmpty()) {
				String[] arrRatio = ratio.split(":");
				// Up to 1:1
				if (arrRatio != null && arrRatio.length > 0 && Float.parseFloat(arrRatio[0].trim()) == 1.0f)
					return true;
			}
			break;
		case REGISTRATION:
			return true;
		default:
			return false;
		}

		return false;
	}

	/**
	 * Checks if the image color space is valid based on ISO standards. GRAY[8 bit]
	 * and RGB[24 bit]
	 * 
	 * @param purpose           the purpose of the biometric data (e.g., AUTH or
	 *                          REGISTRATION)
	 * @param modality          the modality of the biometric data (e.g., Finger,
	 *                          Face)
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image color space is valid, false otherwise
	 */
	@SuppressWarnings({ "java:S3776" })
	public boolean isValidImageColorSpace(String purpose, Modality modality, ImageDecoderRequestDto decoderRequestDto) {
		String colorSpace = decoderRequestDto.getImageColorSpace();
		switch (Purposes.fromCode(purpose)) {
		case AUTH:
			switch (modality) {
			case Finger:
				if (!colorSpace.equalsIgnoreCase("GRAY"))
					return false;
				break;
			case Iris:
				if (!colorSpace.equalsIgnoreCase("GRAY"))
					return false;
				break;
			case Face:
				if (!colorSpace.equalsIgnoreCase("RGB"))
					return false;
				break;
			default:
				break;
			}
			break;
		case REGISTRATION:
			switch (modality) {
			case Finger:
				if (!colorSpace.equalsIgnoreCase("GRAY"))
					return false;
				break;
			case Iris:
				if (!colorSpace.equalsIgnoreCase("GRAY"))
					return false;
				break;
			case Face:
				if (!colorSpace.equalsIgnoreCase("RGB"))
					return false;
				break;
			default:
				break;
			}
			break;
		default:
			return false;
		}

		return true;
	}

	/**
	 * Checks if the image DPI (dots per inch) is valid based on ISO standards.
	 * HorizontalDPI [490 to 1010 DPI] and VerticalDPI [490 to 1010 DPI]
	 * 
	 * @param purpose           the purpose of the biometric data (e.g., AUTH or
	 *                          REGISTRATION)
	 * @param modality          the modality of the biometric data (e.g., Finger,
	 *                          Face)
	 * @param decoderRequestDto the DTO containing image decoding request
	 *                          information
	 * @return true if the image DPI is valid, false otherwise
	 */
	@SuppressWarnings({ "java:S3776" })
	public boolean isValidImageDPI(String purpose, Modality modality, ImageDecoderRequestDto decoderRequestDto) {
		int horizontalDPI = decoderRequestDto.getHorizontalDPI();
		int verticalDPI = decoderRequestDto.getVerticalDPI();
		switch (Purposes.fromCode(purpose)) {
		case AUTH:
			switch (modality) {
			case Finger:
				if (!(horizontalDPI >= 490 && horizontalDPI <= 1010) || !(verticalDPI >= 490 && verticalDPI <= 1010))
					return false;
				break;
			case Iris:
				return true;
			case Face:
				return true;
			default:
				break;
			}
			break;
		case REGISTRATION:
			switch (modality) {
			case Finger:
				if (!(horizontalDPI >= 490 && horizontalDPI <= 1010) || !(verticalDPI >= 490 && verticalDPI <= 1010))
					return false;
				break;
			case Iris:
				return true;
			case Face:
				return true;
			default:
				break;
			}
			break;
		default:
			return false;
		}

		return true;
	}
}