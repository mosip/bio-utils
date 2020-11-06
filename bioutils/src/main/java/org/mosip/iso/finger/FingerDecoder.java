package org.mosip.iso.finger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FingerDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FingerDecoder.class);	
	
	 public static byte [] convertFaceISO19794_4_2011ToImage(byte [] isoData) throws IOException
	 {
		 ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
		 DataInputStream  inputStream = new DataInputStream  (bais);
		 FingerBDIR fingerBDIR = new FingerBDIR (inputStream);
		 LOGGER.info("convertFingerISO19794_4_2011ToImage :: " + fingerBDIR.toString());	

		 byte [] data = fingerBDIR.getRepresentation().getRepresentationBody().getImageData().getImage();
	
		 inputStream.close();
		 return data;
	 }	 
}
