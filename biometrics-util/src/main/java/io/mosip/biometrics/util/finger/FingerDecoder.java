package io.mosip.biometrics.util.finger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FingerDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerDecoder.class);	
	
	 private static byte [] convertFingerISO19794_4_2011ToImage(byte [] isoData) throws Exception
	 {
		 try(ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
			 DataInputStream inputStream = new DataInputStream(bais);) {

			 FingerBDIR fingerBDIR = new FingerBDIR(inputStream);
			 LOGGER.info("convertFingerISO19794_4_2011ToImage :: {}", fingerBDIR);
			 return fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage();
		 }
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
