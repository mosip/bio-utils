package io.mosip.biometrics.util.face;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.iris.IrisBDIR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

public class FaceDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceDecoder.class);	
	
	private static FaceBDIR getFaceBDIRISO19794_5_2011(byte [] isoData) throws Exception
	{
		try(ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
			DataInputStream inputStream = new DataInputStream(bais);) {
			FaceBDIR faceBDIR = new FaceBDIR(inputStream);
			LOGGER.info("faceBDIR :: ", faceBDIR);
			return faceBDIR;
		}
	}

	/**
	 *
	 * @param convertRequestDto
	 * @return
	 * @throws Exception
	 */
	public static FaceBDIR getFaceBDIR(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_5_2011" :
				return getFaceBDIRISO19794_5_2011(convertRequestDto.getInputBytes());
		}
		throw new UnsupportedOperationException();
	}
	
	private static byte[] convertFaceISO19794_5_2011ToImage(byte [] isoData) throws Exception
	{
		ImageData imageData = getFaceBDIRISO19794_5_2011 (isoData).getRepresentation()
				.getRepresentationData().getImageData();
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageData.getImage())), "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get jpg image", e);
		}
		return imageData.getImage();
	}

	/**
	 * Convert Face ISO to Buffered image
	 * supported versions: ISO19794_5_2011
	 * Note: JPEG_2000 will be returned as JPEG image
	 * @param convertRequestDto
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage convertFaceISOToBufferedImage(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_5_2011" :
				FaceBDIR faceBDIR =  getFaceBDIRISO19794_5_2011 (convertRequestDto.getInputBytes());
				return ImageIO.read(new ByteArrayInputStream(faceBDIR.getRepresentation()
						.getRepresentationData().getImageData().getImage()));
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Convert Face ISO to Image bytes
	 * supported versions: ISO19794_5_2011
	 * Note: JPEG_2000 will be returned as JPEG image
	 * @param convertRequestDto
	 * @return
	 * @throws Exception
	 */
	public static byte[] convertFaceISOToImageBytes(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_5_2011" :
				FaceBDIR faceBDIR =  getFaceBDIRISO19794_5_2011 (convertRequestDto.getInputBytes());
				ImageDataType imageDataType = faceBDIR.getRepresentation().getRepresentationHeader().getImageInformation().getImageDataType();
				switch (imageDataType) {
					case JPEG2000_LOSSY:
					case JPEG2000_LOSS_LESS:
						return CommonUtil.convertJP2ToJPEGBytes(faceBDIR.getRepresentation()
								.getRepresentationData().getImageData().getImage());
					default:
						return faceBDIR.getRepresentation()
								.getRepresentationData().getImageData().getImage();
				}
		}
		throw new UnsupportedOperationException();
	}
}
