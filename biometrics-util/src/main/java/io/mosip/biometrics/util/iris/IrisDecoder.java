package io.mosip.biometrics.util.iris;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.ConvertRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrisDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(IrisDecoder.class);	
	
	 private static byte [] convertIrisISO19794_6_2011ToImage(byte [] isoData) throws IOException
	 {
		 try(ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
		 DataInputStream  inputStream = new DataInputStream  (bais);) {
			 IrisBDIR irisBDIR = new IrisBDIR(inputStream);
			 LOGGER.info("convertIrisISO19794_6_2011ToImage :: {}", irisBDIR);
			 return irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage();
		 }
	 }

	public static byte [] convertIrisISOToImage(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_6_2011" :
				return convertIrisISO19794_6_2011ToImage(convertRequestDto.getInputBytes());
		}
		throw new UnsupportedOperationException();
	}
}
