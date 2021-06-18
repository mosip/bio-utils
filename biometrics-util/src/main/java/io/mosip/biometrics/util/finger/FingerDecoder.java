package io.mosip.biometrics.util.finger;

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


	public static byte [] convertFingerISOToImageBytes(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_4_2011" :
				FingerBDIR fingerBDIR = getFingerBDIRISO19794_4_2011 (convertRequestDto.getInputBytes());
				FingerImageCompressionType fingerImageCompressionType = fingerBDIR.getRepresentation().getRepresentationHeader().getCompressionType();
				switch (fingerImageCompressionType) {
					case JPEG_2000_LOSSY:
					case JPEG_2000_LOSS_LESS:
						return CommonUtil.convertJP2ToJPEGBytes(fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage());
					default:
						return fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage();
				}
		}
		throw new UnsupportedOperationException();
	}

	public static BufferedImage convertFingerISOToBufferedImage(ConvertRequestDto convertRequestDto) throws Exception
	{
		ImageData imageData = null;
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_4_2011" :
				imageData = getFingerBDIRISO19794_4_2011 (convertRequestDto.getInputBytes()).
						getRepresentation().getRepresentationBody().getImageData();
				return ImageIO.read(new ByteArrayInputStream(imageData.getImage()));
		}
		throw new UnsupportedOperationException();
	}
}
