package io.mosip.biometrics.util.face;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.finger.*;
import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class FaceEncoder {	
	 private static byte [] convertFaceImageToISO19794_5_2011 
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

	public static byte [] convertFaceImageToISO(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_5_2011" :
				FaceFormatIdentifier formatIdentifier = FaceFormatIdentifier.FORMAT_FAC;
				FaceVersionNumber versionNumber = FaceVersionNumber.VERSION_030;
				FaceCertificationFlag certificationFlag = FaceCertificationFlag.UNSPECIFIED;
				TemporalSequenceFlags temporalSequenceFlags = TemporalSequenceFlags.ONE_REPRESENTATION;

				Date captureDate = new Date ();// the date instance

				short noOfLandMarkPoints = 0x00;
				FaceQualityAlgorithmVendorIdentifier algorithmVendorIdentifier = FaceQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER;
				FaceQualityAlgorithmIdentifier qualityAlgorithmIdentifier = FaceQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER;

				Gender gender = Gender.UNKNOWN;
				EyeColour eyeColour = EyeColour.UNSPECIFIED;
				int featureMask = 0;
				HeightCodes subjectHeight = HeightCodes.UNSPECIFIED;
				HairColour hairColour = HairColour.UNSPECIFIED;
				Expression expression = Expression.UNSPECIFIED;
				Features features = Features.FEATURES_ARE_SPECIFIED;
				int [] poseAngle = { 0, 0, 0 };
				int [] poseAngleUncertainty = { 0, 0, 0 };
				FaceImageType faceImageType = FaceImageType.FULL_FRONTAL;
				ImageColourSpace imageColourSpace = ImageColourSpace.BIT_24_RGB;
				FaceCaptureDeviceTechnology sourceType = FaceCaptureDeviceTechnology.VIDEO_FRAME_ANALOG_CAMERA;
				FaceCaptureDeviceVendor deviceVendor = FaceCaptureDeviceVendor.UNSPECIFIED;
				FaceCaptureDeviceType deviceType = FaceCaptureDeviceType.UNSPECIFIED;

				SpatialSamplingRateLevel spatialSamplingRateLevel = SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180;
				PostAcquisitionProcessing postAcquisitionProcessing = PostAcquisitionProcessing.CROPPED;
				CrossReference crossReference = CrossReference.BASIC;
				LandmarkPoints [] landmarkPoints = null;
				int noOfRepresentations = (int)0x0001;

				int quality = 80;
				FaceQualityBlock [] qualityBlock = new FaceQualityBlock [] { new FaceQualityBlock ((byte)quality , algorithmVendorIdentifier, qualityAlgorithmIdentifier)};
				ImageDataType imageDataType = ImageDataType.JPEG2000_LOSS_LESS;
				BufferedImage bufferedImage = CommonUtil.getBufferedImage(convertRequestDto);
				int imageWidth = bufferedImage.getWidth();
				int imageHeight = bufferedImage.getHeight();

				return convertFaceImageToISO19794_5_2011
						(
								formatIdentifier, versionNumber,
								certificationFlag, temporalSequenceFlags,
								captureDate, noOfRepresentations, noOfLandMarkPoints,
								gender, eyeColour, featureMask,
								hairColour, subjectHeight, expression,
								features, poseAngle, poseAngleUncertainty,
								faceImageType, sourceType, deviceVendor, deviceType,
								qualityBlock, convertRequestDto.getInputBytes(), imageWidth, imageHeight,
								imageDataType, spatialSamplingRateLevel,
								postAcquisitionProcessing, crossReference,
								imageColourSpace, landmarkPoints
						);
		}
		throw new UnsupportedOperationException();
	}
}
