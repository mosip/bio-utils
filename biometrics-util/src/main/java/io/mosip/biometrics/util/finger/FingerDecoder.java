package io.mosip.biometrics.util.finger;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;

import javax.imageio.ImageIO;

public class FingerDecoder {
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
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_4_2011":
			return getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] convertFingerISOToImageBytes(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_4_2011":
			FingerBDIR fingerBDIR = getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
			int fingerImageCompressionType = fingerBDIR.getRepresentation().getRepresentationHeader()
					.getCompressionType();
			switch (fingerImageCompressionType) {
			case FingerImageCompressionType.JPEG_2000_LOSSY:
			case FingerImageCompressionType.JPEG_2000_LOSS_LESS:
				return CommonUtil.convertJP2ToJPEGUsingOpenCV(fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage(), convertRequestDto.getCompressionRatio());
			default:
				return fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static BufferedImage convertFingerISOToBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
		ImageData imageData = null;
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_4_2011":
			imageData = getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation()).getRepresentation().getRepresentationBody()
					.getImageData();
			return ImageIO.read(new ByteArrayInputStream(imageData.getImage()));
		}
		throw new UnsupportedOperationException();
	}
}
