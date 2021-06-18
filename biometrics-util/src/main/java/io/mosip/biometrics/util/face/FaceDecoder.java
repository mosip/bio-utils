package io.mosip.biometrics.util.face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

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

	public static byte [] convertFaceISOToImage(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_5_2011" :
				return convertFaceISO19794_5_2011ToImage(convertRequestDto.getInputBytes());
		}
		throw new UnsupportedOperationException();
	}
}
