package io.mosip.biometrics.util.finger;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import javax.imageio.ImageIO;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;

public class FingerDecoder {
	private static final String ISO_VERSION = "ISO19794_4_2011";

	private FingerDecoder() {
		throw new IllegalStateException("FingerDecoder class");
	}

	@SuppressWarnings({ "java:S100", "java:S112" })
	private static FingerBDIR getFingerBDIRISO19794_4_2011(byte[] isoData, int onlyImageInformation) throws Exception {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
				DataInputStream inputStream = new DataInputStream(bais);) {
			FingerBDIR fingerBDIR = null;
			if (onlyImageInformation == 1)
				fingerBDIR = new FingerBDIR(inputStream, true);
			else
				fingerBDIR = new FingerBDIR(inputStream);
			return fingerBDIR;
		}
	}

	public static FingerBDIR getFingerBDIR(ConvertRequestDto convertRequestDto) throws Exception {
		if (convertRequestDto.getVersion().equals(ISO_VERSION))
			return getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());

		throw new UnsupportedOperationException();
	}

	public static byte[] convertFingerISOToImageBytes(ConvertRequestDto convertRequestDto) throws Exception {
		if (convertRequestDto.getVersion().equals(ISO_VERSION)) {
			FingerBDIR fingerBDIR = getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
			int fingerImageCompressionType = fingerBDIR.getRepresentation().getRepresentationHeader()
					.getCompressionType();
			if (fingerImageCompressionType == FingerImageCompressionType.JPEG_2000_LOSSY
					|| fingerImageCompressionType == FingerImageCompressionType.JPEG_2000_LOSS_LESS)
				return CommonUtil.convertJP2ToJPEGUsingOpenCV(
						fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage(),
						convertRequestDto.getCompressionRatio());
			else
				return fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage();
		}
		throw new UnsupportedOperationException();
	}

	public static BufferedImage convertFingerISOToBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
		ImageData imageData = null;
		if (convertRequestDto.getVersion().equals(ISO_VERSION)) {
			imageData = getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation()).getRepresentation().getRepresentationBody()
					.getImageData();
			return ImageIO.read(new ByteArrayInputStream(imageData.getImage()));
		}
		throw new UnsupportedOperationException();
	}
}