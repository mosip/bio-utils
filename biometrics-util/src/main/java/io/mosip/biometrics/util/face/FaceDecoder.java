package io.mosip.biometrics.util.face;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaceDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceDecoder.class);	
	
	 public static byte [] convertFaceISO19794_5_2011ToImage(byte [] isoData) throws IOException
	 {
		 try(ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
		 DataInputStream  inputStream = new DataInputStream  (bais);) {
			 FaceBDIR faceBDIR = new FaceBDIR(inputStream);
			 LOGGER.info("convertFaceISO19794_5_2011ToImage :: {}", faceBDIR);
			 return faceBDIR.getRepresentation().getRepresentationData().getImageData().getImage();
		 }
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
