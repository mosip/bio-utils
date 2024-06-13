package io.mosip.biometrics.util.iris;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

public class IrisDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(IrisDecoder.class);
	private static final String ISO_VERSION = "ISO19794_6_2011";

	private IrisDecoder() {
		throw new IllegalStateException("IrisDecoder class");
	}

	@SuppressWarnings({ "java:S100", "java:S112", "unused" })
	private static IrisBDIR getIrisBDIRISO19794_6_2011(byte[] isoData, int onlyImageInformation) throws Exception {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
				DataInputStream inputStream = new DataInputStream(bais);) {
			IrisBDIR irisBDIR = null;
			if (onlyImageInformation == 1)
				irisBDIR = new IrisBDIR(inputStream, true);
			else
				irisBDIR = new IrisBDIR(inputStream);
			return irisBDIR;
		}
	}

	public static IrisBDIR getIrisBDIR(ConvertRequestDto convertRequestDto) throws Exception {
		if (convertRequestDto.getVersion().equals(ISO_VERSION))
			return getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings({ "java:S100", "unused" })
	private static byte[] convertIrisISO19794_6_2011ToImage(byte[] isoData) throws Exception {
		ImageData imageData = getIrisBDIRISO19794_6_2011(isoData, 0).getRepresentation().getRepresentationData()
				.getImageData();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageData.getImage())), "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get iris jpg image", e);
		}
		return imageData.getImage();
	}

	public static BufferedImage convertIrisISOToBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
		if (convertRequestDto.getVersion().equals(ISO_VERSION)) {
			IrisBDIR irisBDIR = getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
			return ImageIO.read(new ByteArrayInputStream(
					irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage()));
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] convertIrisISOToImageBytes(ConvertRequestDto convertRequestDto) throws Exception {
		if (convertRequestDto.getVersion().equals(ISO_VERSION)) {
			IrisBDIR irisBDIR = getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
			int imageFormat = irisBDIR.getRepresentation().getRepresentationHeader().getImageInformation()
					.getImageFormat();
			if (imageFormat == ImageFormat.MONO_JPEG2000 || imageFormat == ImageFormat.RGB_JPEG2000)
				return CommonUtil.convertJP2ToJPEGUsingOpenCV(
						irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage(),
						convertRequestDto.getCompressionRatio());
			else
				return irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage();
		}
		throw new UnsupportedOperationException();
	}
}