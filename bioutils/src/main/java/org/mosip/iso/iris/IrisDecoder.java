package org.mosip.iso.iris;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrisDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(IrisDecoder.class);	
	
	 public static byte [] convertFaceISO19794_6_2011ToImage(byte [] isoData) throws IOException
	 {
		 ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
		 DataInputStream  inputStream = new DataInputStream  (bais);
		 IrisBDIR irisBDIR = new IrisBDIR (inputStream);
		 LOGGER.info("convertIrisISO19794_6_2011ToImage :: " + irisBDIR.toString());	
		 byte [] data = irisBDIR.getRepresentation().getRepresentationData().getImageData().getImage();
	
		 inputStream.close();
		 return data;
	 }	 
}
