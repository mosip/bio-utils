package io.mosip.biometrics.util.iris;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.finger.FingerBDIR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrisDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(IrisDecoder.class);	
	
	private static IrisBDIR getIrisBDIRISO19794_6_2011(byte [] isoData) throws Exception
	{
		try(ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
			DataInputStream inputStream = new DataInputStream(bais);) {
			IrisBDIR irisBDIR = new IrisBDIR(inputStream);
			LOGGER.info("irisBDIR :: ", irisBDIR);
			return irisBDIR;
		}
	}

	public static IrisBDIR getIrisBDIR(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_6_2011" :
				return getIrisBDIRISO19794_6_2011(convertRequestDto.getInputBytes());
		}
		throw new UnsupportedOperationException();
	}

	private static byte [] convertIrisISO19794_6_2011ToImage(byte [] isoData) throws Exception
	{
		return getIrisBDIRISO19794_6_2011 (isoData).getRepresentation().getRepresentationData().getImageData().getImage();
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
