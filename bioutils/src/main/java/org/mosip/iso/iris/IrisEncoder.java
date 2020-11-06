package org.mosip.iso.iris;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class IrisEncoder {
	
	 public static byte [] convertIrisImageToISO19794_6_2011 
 		(
			IrisFormatIdentifier formatIdentifier, IrisVersionNumber versionNumber,
			IrisCertificationFlag certificationFlag, Date captureDate, 
			int noOfRepresentations, int representationNo, int noOfEyesPresent,
			EyeLabel eyeLabel, ImageType imageType, ImageFormat imageFormat,
			Orientation horizontalOrientation, Orientation verticalOrientation, IrisImageCompressionType compressionType,
			int width, int height, IrisImageBitDepth bitDepth, int range, int rollAngleOfEye, int rollAngleUncertainty, 
			int irisCenterSmallestX, int irisCenterLargestX, int irisCenterSmallestY, int irisCenterLargestY, 
			int irisDiameterSmallest, int irisDiameterLargest,
			IrisCaptureDeviceTechnology sourceType, IrisCaptureDeviceVendor deviceVendor, IrisCaptureDeviceType deviceType, 
			IrisQualityBlock [] qualityBlocks, 
			byte [] imageData, int imageWidth, int imageHeight
		) throws IOException
	 {
	      ImageInformation imageInformation = new ImageInformation 
				  (
					  	eyeLabel, imageType, imageFormat,
						horizontalOrientation, verticalOrientation, compressionType,
						width, height, bitDepth, range, rollAngleOfEye, rollAngleUncertainty, 
						irisCenterSmallestX, irisCenterLargestX, irisCenterSmallestY, irisCenterLargestY, 
						irisDiameterSmallest, irisDiameterLargest
				  );
	
	      IrisBDIR irisBDIR = new IrisBDIR 
			  (
					formatIdentifier, versionNumber,
					certificationFlag, captureDate, noOfRepresentations,
					qualityBlocks, imageInformation, representationNo, noOfEyesPresent,
					imageData
			  );
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream outputStream = new DataOutputStream (baos);
	      irisBDIR.writeObject (outputStream);
	      outputStream.flush();
	      byte [] data = baos.toByteArray();
	
	      outputStream.close();
	      return data;
	 }	 
}
