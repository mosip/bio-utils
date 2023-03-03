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

	private static IrisBDIR getIrisBDIRISO19794_6_2011(byte[] isoData, int onlyImageInformation) throws Exception {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
				DataInputStream inputStream = new DataInputStream(bais);) {
			IrisBDIR irisBDIR = null;
			if (onlyImageInformation == 1)
				irisBDIR = new IrisBDIR(inputStream, true);
			else
				irisBDIR = new IrisBDIR(inputStream);
			// LOGGER.info("irisBDIR :: ", irisBDIR);
			return irisBDIR;
		}
	}

	public static IrisBDIR getIrisBDIR(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_6_2011":
			return getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
		}
		throw new UnsupportedOperationException();
	}

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
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_6_2011":
			IrisBDIR irisBDIR = getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
			return ImageIO.read(new ByteArrayInputStream(
					irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage()));
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] convertIrisISOToImageBytes(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getVersion()) {
		case "ISO19794_6_2011":
			IrisBDIR irisBDIR = getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes(),
					convertRequestDto.getOnlyImageInformation());
			int imageFormat = irisBDIR.getRepresentation().getRepresentationHeader().getImageInformation()
					.getImageFormat();
			switch (imageFormat) {
			case ImageFormat.MONO_JPEG2000:
			case ImageFormat.RGB_JPEG2000:
				//return CommonUtil.convertJP2ToJPEGBytes(irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage());
				return CommonUtil.convertJP2ToJPEGUsingOpenCV(irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage(), convertRequestDto.getCompressionRatio());
			default:
				return irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage();
			}
		}
		throw new UnsupportedOperationException();
	}
}
