package org.mosip.iso.face;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaceDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceDecoder.class);	
	
	 public static byte [] convertFaceISO19794_5_2011ToImage(byte [] isoData) throws IOException
	 {
		 ByteArrayInputStream bais = new ByteArrayInputStream(isoData);
		 DataInputStream  inputStream = new DataInputStream  (bais);
		 FaceBDIR faceBDIR = new FaceBDIR (inputStream);
		 LOGGER.info("convertFaceISO19794_5_2011ToImage :: " + faceBDIR.toString());	
		 byte [] data = faceBDIR.getRepresentation().getRepresentationData().getImageData().getImage();
	
		 inputStream.close();
		 return data;
	 }	 
}
