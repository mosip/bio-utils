package io.mosip.biometrics.util.finger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

public class FingerDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerDecoder.class);	
	
	private static FingerBDIR getFingerBDIRISO19794_4_2011(byte [] isoData) throws Exception
	{
		 try(ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
			DataInputStream inputStream = new DataInputStream(bais);) {
			FingerBDIR fingerBDIR = new FingerBDIR(inputStream);
			LOGGER.info("fingerBDIR :: ", fingerBDIR);
			return fingerBDIR;
		}
	}

	public static FingerBDIR getFingerBDIR(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_4_2011" :
				return getFingerBDIRISO19794_4_2011(convertRequestDto.getInputBytes());
		}
		throw new UnsupportedOperationException();
	}

	private static byte [] convertFingerISO19794_4_2011ToImage(byte [] isoData) throws Exception
	{
		ImageData imageData = getFingerBDIRISO19794_4_2011 (isoData).getRepresentation().getRepresentationBody().getImageData();
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageData.getImage())), "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get finger jpg image", e);
		}
		return imageData.getImage();
	}

	public static byte [] convertFingerISOToImage(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_4_2011" :
				return convertFingerISO19794_4_2011ToImage(convertRequestDto.getInputBytes());
		}
		throw new UnsupportedOperationException();
	}
}
