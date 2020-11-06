package org.mosip.iso.face;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class FaceEncoder {
	
	 public static byte [] convertFaceImageToISO19794_5_2011 
 		(
			FaceFormatIdentifier formatIdentifier, FaceVersionNumber versionNumber,
			FaceCertificationFlag certificationFlag, TemporalSequenceFlags temporalSemantics,
			Date captureDate, int noOfRepresentations, short noOfLandMarkPoints, 
			Gender gender, EyeColour eyeColour, 
			int featureMask, HairColour hairColour, HeightCodes subjectHeight, 
			Expression expression, Features features, 
			int [] poseAngle, int [] poseAngleUncertainty, FaceImageType faceImageType, 
			FaceCaptureDeviceTechnology sourceType, FaceCaptureDeviceVendor deviceVendor, FaceCaptureDeviceType deviceType, 
			FaceQualityBlock [] qualityBlock,
			byte [] imageData, int imageWidth, int imageHeight, 
			ImageDataType imageDataType, SpatialSamplingRateLevel spatialSamplingRateLevel, 
			PostAcquisitionProcessing postAcquisitionProcessing, CrossReference crossReference, 
			ImageColourSpace imageColourSpace, LandmarkPoints [] landmarkPoints
		) throws IOException
	 {
	      FacialInformation facialInformation = new FacialInformation 
	    		  (
					  noOfLandMarkPoints, gender, eyeColour, 
					  hairColour, subjectHeight, 
					  features, expression, 
					  poseAngle, poseAngleUncertainty
				  );
	      
	      ImageInformation imageInformation = new ImageInformation 
				  (
					  faceImageType, imageDataType, imageWidth, imageHeight, 
					  spatialSamplingRateLevel, postAcquisitionProcessing, 
					  crossReference, imageColourSpace
				  );
	
	      FaceBDIR faceBDIR = new FaceBDIR 
			  (
				  formatIdentifier, versionNumber, 
				  certificationFlag, temporalSemantics, 
				  sourceType, deviceVendor, deviceType,
				  captureDate, noOfRepresentations, qualityBlock, 
				  facialInformation, landmarkPoints, imageInformation, 
				  imageData
			  );
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream outputStream = new DataOutputStream (baos);
	      faceBDIR.writeObject (outputStream);
	      outputStream.flush();
	      byte [] data = baos.toByteArray();
	
	      outputStream.close();
	      return data;
	 }	 
}
