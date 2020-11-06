package org.mosip.bio.utils.test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.imageio.ImageIO;

import org.mosip.iso.ApplicationConstant;
import org.mosip.iso.face.FaceCaptureDeviceTechnology;
import org.mosip.iso.face.FaceCaptureDeviceType;
import org.mosip.iso.face.FaceCaptureDeviceVendor;
import org.mosip.iso.face.FaceCertificationFlag;
import org.mosip.iso.face.CrossReference;
import org.mosip.iso.face.Expression;
import org.mosip.iso.face.EyeColour;
import org.mosip.iso.face.FaceDecoder;
import org.mosip.iso.face.FaceEncoder;
import org.mosip.iso.face.FaceImageType;
import org.mosip.iso.face.Features;
import org.mosip.iso.face.FaceFormatIdentifier;
import org.mosip.iso.face.Gender;
import org.mosip.iso.face.HairColour;
import org.mosip.iso.face.HeightCodes;
import org.mosip.iso.face.ImageColourSpace;
import org.mosip.iso.face.ImageDataType;
import org.mosip.iso.face.LandmarkPoints;
import org.mosip.iso.face.PostAcquisitionProcessing;
import org.mosip.iso.face.SpatialSamplingRateLevel;
import org.mosip.iso.face.TemporalSequenceFlags;
import org.mosip.iso.finger.AnnotationBlock;
import org.mosip.iso.finger.CommentBlock;
import org.mosip.iso.finger.FingerCaptureDeviceTechnology;
import org.mosip.iso.finger.FingerCaptureDeviceType;
import org.mosip.iso.finger.FingerCaptureDeviceVendor;
import org.mosip.iso.finger.FingerCertificationBlock;
import org.mosip.iso.finger.FingerCertificationFlag;
import org.mosip.iso.finger.FingerDecoder;
import org.mosip.iso.finger.FingerEncoder;
import org.mosip.iso.finger.FingerFormatIdentifier;
import org.mosip.iso.finger.FingerImageBitDepth;
import org.mosip.iso.finger.FingerImageCompressionType;
import org.mosip.iso.finger.FingerImpressionType;
import org.mosip.iso.finger.FingerPosition;
import org.mosip.iso.finger.FingerQualityAlgorithmIdentifier;
import org.mosip.iso.finger.FingerQualityAlgorithmVendorIdentifier;
import org.mosip.iso.finger.FingerQualityBlock;
import org.mosip.iso.finger.FingerScaleUnitType;
import org.mosip.iso.finger.FingerVersionNumber;
import org.mosip.iso.finger.SegmentationBlock;
import org.mosip.iso.face.FaceQualityAlgorithmIdentifier;
import org.mosip.iso.face.FaceQualityAlgorithmVendorIdentifier;
import org.mosip.iso.face.FaceQualityBlock;
import org.mosip.iso.iris.IrisImageBitDepth;
import org.mosip.iso.iris.IrisImageCompressionType;
import org.mosip.iso.iris.EyeLabel;
import org.mosip.iso.iris.ImageFormat;
import org.mosip.iso.iris.ImageType;
import org.mosip.iso.iris.IrisCaptureDeviceTechnology;
import org.mosip.iso.iris.IrisCaptureDeviceType;
import org.mosip.iso.iris.IrisCaptureDeviceVendor;
import org.mosip.iso.iris.IrisCertificationFlag;
import org.mosip.iso.iris.IrisDecoder;
import org.mosip.iso.iris.IrisEncoder;
import org.mosip.iso.iris.IrisFormatIdentifier;
import org.mosip.iso.iris.IrisQualityAlgorithmIdentifier;
import org.mosip.iso.iris.IrisQualityAlgorithmVendorIdentifier;
import org.mosip.iso.iris.IrisQualityBlock;
import org.mosip.iso.iris.IrisVersionNumber;
import org.mosip.iso.iris.Orientation;
import org.mosip.iso.face.FaceVersionNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BioUtilApplication
 *
 */
public class BioUtilApplication
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BioUtilApplication.class);	

    public static void main(String[] args)
    {
		if (args != null && args.length >= 2)
		{
			// Argument 0 should contain "org.mosip.bio.utils.convert.jp2000.to.iso/org.mosip.bio.utils.convert.iso.to.jp2000"
			String convertTo = args[0];
			LOGGER.info("main :: convertTo :: Argument [0] " + convertTo);
			if (convertTo.contains(ApplicationConstant.MOSIP_CONVERT_JP2000_TO_ISO))//0
			{
				convertTo = convertTo.split("=") [1];
			}
			else if (convertTo.contains(ApplicationConstant.MOSIP_CONVERT_ISO_TO_JP2000))//1
			{
				convertTo = convertTo.split("=") [1];
			}
			
			// Argument 1 should contain "mosip.mock.sbi.biometric.type.finger.folder.path/mosip.mock.sbi.biometric.type.face.folder.path/mosip.mock.sbi.biometric.type.iris.folder.path"
			String biometricFolderPath = args[1];
			LOGGER.info("main :: biometricFolderPath :: Argument [1] " + biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FINGER))
			{
				biometricFolderPath = biometricFolderPath.split("=")[1];
			}
			else if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FACE))
			{
				biometricFolderPath = biometricFolderPath.split("=")[1];
			}
			else if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_IRIS))
			{
				biometricFolderPath = biometricFolderPath.split("=")[1];
			}

			// Argument 2 should contain "org.mosip.bio.utils.biometric.type.file.jp2000/org.mosip.bio.utils.biometric.type.file.iso"
			String converionFile = args[2];
			LOGGER.info("main :: converionFile :: Argument [2] " + converionFile);
			if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_JP2000))
			{
				converionFile = converionFile.split("=")[1];
			}
			else if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO))
			{
				converionFile = converionFile.split("=")[1];
			}
			
			// Argument 3 should contain "mosip.mock.sbi.biometric.subtype=Right" Example#Iris Right eye
			String biometricSubType = args[3];
			LOGGER.info("main :: biometricSubType :: Argument [3] " + biometricSubType);
			if (biometricSubType != null && 
				(
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_UNKNOWN) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_THUMB) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_INDEX) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_MIDDLE) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_RING) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_LITTLE) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_THUMB) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_INDEX) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_MIDDLE) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_RING) ||
					biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_LITTLE)
				))
			{
				biometricSubType = biometricSubType.split("=")[1];
			}
			
			if (biometricFolderPath.contains("Face"))
			{
				doFaceConversion (convertTo, biometricFolderPath, converionFile); 
			}
			else if (biometricFolderPath.contains("Iris"))
			{
				if (biometricSubType != null)
				{
					doIrisConversion (convertTo, biometricFolderPath, converionFile, biometricSubType); 
				}
				else
				{
					LOGGER.info("main :: biometricSubType :: Argument [3] " + biometricSubType + " is empty for Iris");
				}
			}
			else if (biometricFolderPath.contains("Finger"))
			{
				if (biometricSubType != null)
				{
					doFingerConversion (convertTo, biometricFolderPath, converionFile, biometricSubType); 
				}
				else
				{
					LOGGER.info("main :: biometricSubType :: Argument [3] " + biometricSubType + " is empty for Iris");
				}
			}
		}
    }
    
    public static void doFaceConversion (String convertTo, String biometricFolderPath, String converionFile)
    {
		LOGGER.info("doFaceConversion :: Started :: convertTo ::" + convertTo +  " :: biometricFolderPath :: " + biometricFolderPath + " :: converionFile :: " + converionFile);
		InputStream imageInputStream = null;
    	FileOutputStream tmpOutputStream = null;
		try {
	    	if (Integer.parseInt(convertTo) == 0) //Convert JP2000 to Face ISO/IEC 19794-5: 2011 
	    	{
	    		String filePath = new File (".").getCanonicalPath ();
	    		String fileName = filePath + biometricFolderPath + converionFile;
	    		File initialFile = new File(fileName);
	    		if (initialFile.exists())
	    		{
		    		LOGGER.info("doFaceConversion :: fileName ::" + fileName);
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
					imageInputStream = new FileInputStream(initialFile);
	    			BufferedImage buffImg  = ImageIO.read(imageInputStream);
	    			if (buffImg != null)
	    			{
						int imageWidth = buffImg.getWidth();
						int imageHeight = buffImg.getHeight();
						byte[] imageData = Files.readAllBytes(Paths.get(fileName));
						
						LOGGER.info("doFaceConversion :: imageWidth ::" + imageWidth +  " :: imageHeight :: " + imageHeight + " :: Size :: " + imageData.length);

			    		ImageDataType imageDataType = ImageDataType.JPEG2000_LOSS_LESS;
						
		        		byte [] isoData = FaceEncoder.convertFaceImageToISO19794_5_2011 
		        				(
	        						formatIdentifier, versionNumber, 
	        						certificationFlag, temporalSequenceFlags, 
	        						captureDate, noOfRepresentations, noOfLandMarkPoints, 
	        						gender, eyeColour, featureMask, 
	        						hairColour, subjectHeight, expression, 
	        						features, poseAngle, poseAngleUncertainty, 
	        						faceImageType, sourceType, deviceVendor, deviceType, 
	        						qualityBlock, imageData, imageWidth, imageHeight, 
	        						imageDataType, spatialSamplingRateLevel, 
	        						postAcquisitionProcessing, crossReference, 
	        						imageColourSpace, landmarkPoints
        						); 
		        		
		        		if (isoData != null)
		        		{
		        			// Write bytes to tmp file.
		        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".iso");
	        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
	        		        tmpOutputStream.write(isoData);
		        		}
	    			}
	    			else
	    			{
	    				LOGGER.error("doFaceConversion :: Could Not convert the Image To ISO ");
	    			}
	    		}
	    	}
	    	else if (Integer.parseInt(convertTo) == 1) //Convert Face ISO/IEC 19794-5: 2011 to JP2000
	    	{
	    		String filePath = new File (".").getCanonicalPath ();
	    		String fileName = filePath + biometricFolderPath + converionFile;
	    		File initialFile = new File(fileName);
	    		if (initialFile.exists())
	    		{
		    		LOGGER.info("doFaceConversion :: fileName ::" + fileName);
					byte[] isoData = Files.readAllBytes(Paths.get(fileName));
					byte [] imageData = FaceDecoder.convertFaceISO19794_5_2011ToImage (isoData); 
					
	        		if (imageData != null)
	        		{
	        			// Write bytes to tmp file.
	        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".jp2");
        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
        		        tmpOutputStream.write(imageData);
	        		}
	    		}
	    	}	    		 
		} catch (Exception ex) {
			LOGGER.info("doFaceConversion :: Error ", ex);
		}
		finally
		{
			try
			{
				if (imageInputStream != null)
					imageInputStream.close();
		        if(tmpOutputStream != null)
	                tmpOutputStream.close();
			}
			catch (Exception ex)
			{}
		}
		LOGGER.info("doFaceConversion :: Ended :: ");
    }

    public static void doIrisConversion (String convertTo, String biometricFolderPath, String converionFile, String biometricSubType)
    {
		LOGGER.info("doIrisConversion :: Started :: convertTo ::" + convertTo +  " :: biometricFolderPath :: " + biometricFolderPath + " :: converionFile :: " + converionFile+ " :: biometricSubType :: " + biometricSubType);
		InputStream imageInputStream = null;
    	FileOutputStream tmpOutputStream = null;
		try {
	    	if (Integer.parseInt(convertTo) == 0) //Convert JP2000 to IRIS ISO/IEC 19794-6: 2011 
	    	{
	    		String filePath = new File (".").getCanonicalPath ();
	    		String fileName = filePath + biometricFolderPath + converionFile;
	    		File initialFile = new File(fileName);
	    		if (initialFile.exists())
	    		{
		    		LOGGER.info("doIrisConversion :: fileName ::" + fileName);
	    			IrisFormatIdentifier formatIdentifier = IrisFormatIdentifier.FORMAT_IIR;
	    			IrisVersionNumber versionNumber = IrisVersionNumber.VERSION_020;
	    			IrisCertificationFlag certificationFlag = IrisCertificationFlag.UNSPECIFIED;

	    			Date captureDate = new Date ();// the date instance		      		 

	    			IrisQualityAlgorithmVendorIdentifier algorithmVendorIdentifier = IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER;
	    			IrisQualityAlgorithmIdentifier qualityAlgorithmIdentifier = IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER;
	    	    		    	    	
	    			EyeLabel eyeLabel = EyeLabel.UNSPECIFIED;
	    			if (biometricSubType.equals("Left"))
	    				eyeLabel = EyeLabel.LEFT;
	    			else if (biometricSubType.equals("Right"))
	    				eyeLabel = EyeLabel.RIGHT;
	    			
	    			//CROPPED for Registration device and CROPPED_AND_MASKED for Authenication devices
	    			ImageType imageType = ImageType.CROPPED;
	    			ImageFormat imageFormat = ImageFormat.MONO_JPEG_LOSS_LESS;//0A
	    			Orientation horizontalOrientation = Orientation.UNDEFINED;
	    			Orientation verticalOrientation = Orientation.UNDEFINED;
	    			IrisImageCompressionType compressionType = IrisImageCompressionType.UNDEFINED;
	    			IrisImageBitDepth bitDepth = IrisImageBitDepth.BPP_8;
	    			
	    			int range = 0x0000;
	    			int rollAngleOfEye = 0xFFFF;//ANGLE_UNDEFINED
	    			int rollAngleUncertainty = 0xFFFF; //UNCERTAIN_UNDEFINED
	    			int irisCenterSmallestX = 0x0000; 	//COORDINATE_UNDEFINED
	    			int irisCenterLargestX = 0x0000; 	//COORDINATE_UNDEFINED 
	    			int irisCenterSmallestY = 0x0000; 	//COORDINATE_UNDEFINED 
	    			int irisCenterLargestY = 0x0000; 	//COORDINATE_UNDEFINED 
	    			int irisDiameterSmallest = 0x0000; 	//COORDINATE_UNDEFINED 
	    			int irisDiameterLargest = 0x0000; 	//COORDINATE_UNDEFINED
					
					IrisCaptureDeviceTechnology sourceType = IrisCaptureDeviceTechnology.CMOS_OR_CCD;
	    			IrisCaptureDeviceVendor deviceVendor = IrisCaptureDeviceVendor.UNSPECIFIED;
	    			IrisCaptureDeviceType deviceType = IrisCaptureDeviceType.UNSPECIFIED;
					
					int noOfRepresentations = (int)0x0001;
					int representationNo = (int)0x0001;
					int noOfEyesPresent = (int)0x0001;

					int quality = 80; 
					IrisQualityBlock [] qualityBlocks = new IrisQualityBlock [] { new IrisQualityBlock ((byte)quality , algorithmVendorIdentifier, qualityAlgorithmIdentifier)};
					imageInputStream = new FileInputStream(initialFile);
	    			BufferedImage buffImg  = ImageIO.read(imageInputStream);
	    			if (buffImg != null)
	    			{
						int imageWidth = buffImg.getWidth();
						int imageHeight = buffImg.getHeight();
						byte[] imageData = Files.readAllBytes(Paths.get(fileName));
						
						LOGGER.info("doFaceConversion :: imageWidth ::" + imageWidth +  " :: imageHeight :: " + imageHeight + " :: Size :: " + imageData.length);
		        		byte [] isoData = IrisEncoder.convertIrisImageToISO19794_6_2011 
	        				(
        						formatIdentifier, versionNumber,
        						certificationFlag, captureDate, 
        						noOfRepresentations, representationNo, noOfEyesPresent,
        						eyeLabel, imageType, imageFormat,
        						horizontalOrientation, verticalOrientation, compressionType,
        						imageWidth, imageHeight, bitDepth, range, rollAngleOfEye, rollAngleUncertainty, 
        						irisCenterSmallestX, irisCenterLargestX, irisCenterSmallestY, irisCenterLargestY, 
        						irisDiameterSmallest, irisDiameterLargest,
        						sourceType, deviceVendor, deviceType, 
        						qualityBlocks, 
        						imageData, imageWidth, imageHeight
    						); 
		        		
		        		if (isoData != null)
		        		{
		        			// Write bytes to tmp file.
		        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".iso");
	        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
	        		        tmpOutputStream.write(isoData);
		        		}
	    			}
	    			else
	    			{
	    				LOGGER.error("doIrisConversion :: Could Not convert the Image To ISO ");
	    			}
	    		}
	    	}
	    	else if (Integer.parseInt(convertTo) == 1) //Convert IRIS ISO/IEC 19794-6: 2011 to JP2000
	    	{
	    		String filePath = new File (".").getCanonicalPath ();
	    		String fileName = filePath + biometricFolderPath + converionFile;
	    		File initialFile = new File(fileName);
	    		if (initialFile.exists())
	    		{
		    		LOGGER.info("doIrisConversion :: fileName ::" + fileName);
					byte[] isoData = Files.readAllBytes(Paths.get(fileName));
					byte [] imageData = IrisDecoder.convertFaceISO19794_6_2011ToImage (isoData); 
					
	        		if (imageData != null)
	        		{
	        			// Write bytes to tmp file.
	        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".jp2");
        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
        		        tmpOutputStream.write(imageData);
	        		}
	    		}
	    	}	    		 
		} catch (Exception ex) {
			LOGGER.info("doIrisConversion :: Error ", ex);
		}
		finally
		{
			try
			{
				if (imageInputStream != null)
					imageInputStream.close();
		        if(tmpOutputStream != null)
	                tmpOutputStream.close();
			}
			catch (Exception ex)
			{}
		}
		LOGGER.info("doIrisConversion :: Ended :: ");
    }

    public static void doFingerConversion (String convertTo, String biometricFolderPath, String converionFile, String biometricSubType)
    {
		LOGGER.info("doFingerConversion :: Started :: convertTo ::" + convertTo +  " :: biometricFolderPath :: " + biometricFolderPath + " :: converionFile :: " + converionFile+ " :: biometricSubType :: " + biometricSubType);
		InputStream imageInputStream = null;
    	FileOutputStream tmpOutputStream = null;
		try {
	    	if (Integer.parseInt(convertTo) == 0) //Convert JP2000 to Finger ISO/IEC 19794-4: 2011 
	    	{
	    		String filePath = new File (".").getCanonicalPath ();
	    		String fileName = filePath + biometricFolderPath + converionFile;
	    		File initialFile = new File(fileName);
	    		if (initialFile.exists())
	    		{
		    		LOGGER.info("doFingerConversion :: fileName ::" + fileName);
	    			FingerFormatIdentifier formatIdentifier = FingerFormatIdentifier.FORMAT_FIR;
	    			FingerVersionNumber versionNumber = FingerVersionNumber.VERSION_020;
	    			FingerCertificationFlag certificationFlag = FingerCertificationFlag.UNSPECIFIED;

	    			FingerCaptureDeviceTechnology sourceType = FingerCaptureDeviceTechnology.UNSPECIFIED;
	    			FingerCaptureDeviceVendor deviceVendor = FingerCaptureDeviceVendor.UNSPECIFIED;
	    			FingerCaptureDeviceType deviceType = FingerCaptureDeviceType.UNSPECIFIED;
	    			Date captureDate = new Date ();// the date instance		      		 
					int noOfRepresentations= (int)0x0001;

	    			FingerQualityAlgorithmVendorIdentifier algorithmVendorIdentifier = FingerQualityAlgorithmVendorIdentifier.NIST;
	    			FingerQualityAlgorithmIdentifier qualityAlgorithmIdentifier = FingerQualityAlgorithmIdentifier.NIST;

	    			int quality = 80; 
					FingerQualityBlock [] qualityBlocks = new FingerQualityBlock [] { new FingerQualityBlock ((byte)quality , algorithmVendorIdentifier, qualityAlgorithmIdentifier)};
					FingerCertificationBlock[] certificationBlocks = null; 
					
					FingerPosition fingerPosition = FingerPosition.UNKNOWN;
	    			if (biometricSubType.equals("Right Thumb"))
	    				fingerPosition = FingerPosition.RIGHT_THUMB;
	    			else if (biometricSubType.equals("Right IndexFinger"))
	    				fingerPosition = FingerPosition.RIGHT_INDEX_FINGER;
	    			else if (biometricSubType.equals("Right MiddleFinger"))
	    				fingerPosition = FingerPosition.RIGHT_MIDDLE_FINGER;
	    			else if (biometricSubType.equals("Right RingFinger"))
	    				fingerPosition = FingerPosition.RIGHT_RING_FINGER;
	    			else if (biometricSubType.equals("Right LittleFinger"))
	    				fingerPosition = FingerPosition.RIGHT_LITTLE_FINGER;
	    			else if (biometricSubType.equals("Left Thumb"))
	    				fingerPosition = FingerPosition.LEFT_THUMB;
	    			else if (biometricSubType.equals("Left IndexFinger"))
	    				fingerPosition = FingerPosition.LEFT_INDEX_FINGER;
	    			else if (biometricSubType.equals("Left MiddleFinger"))
	    				fingerPosition = FingerPosition.LEFT_MIDDLE_FINGER;
	    			else if (biometricSubType.equals("Left RingFinger"))
	    				fingerPosition = FingerPosition.LEFT_RING_FINGER;
	    			else if (biometricSubType.equals("Left LittleFinger"))
	    				fingerPosition = FingerPosition.LEFT_LITTLE_FINGER;
	    			
					int representationNo = (int)0x0000; 
					FingerScaleUnitType scaleUnitType = FingerScaleUnitType.PIXELS_PER_INCH; 
					int captureDeviceSpatialSamplingRateHorizontal = 500; 
					int captureDeviceSpatialSamplingRateVertical = 500;
					int imageSpatialSamplingRateHorizontal = 500; 
					int imageSpatialSamplingRateVertical = 500;
					FingerImageBitDepth bitDepth = FingerImageBitDepth.BPP_08;
					FingerImageCompressionType compressionType = FingerImageCompressionType.JPEG_2000_LOSS_LESS;
					FingerImpressionType impressionType = FingerImpressionType.UNKNOWN;
					int lineLengthHorizontal = 0x0126;
					int lineLengthVertical = 0x021B;
					
					int noOfFingerPresent = (int)0x0001;
					SegmentationBlock segmentationBlock = null;
					AnnotationBlock annotationBlock = null;
					CommentBlock commentBlock = null;

					
					imageInputStream = new FileInputStream(initialFile);
	    			BufferedImage buffImg  = ImageIO.read(imageInputStream);
	    			if (buffImg != null)
	    			{
						int imageWidth = buffImg.getWidth();
						int imageHeight = buffImg.getHeight();
						byte[] imageData = Files.readAllBytes(Paths.get(fileName));
						
						LOGGER.info("doFaceConversion :: imageWidth ::" + imageWidth +  " :: imageHeight :: " + imageHeight + " :: Size :: " + imageData.length);
		        		byte [] isoData = FingerEncoder.convertFingerImageToISO19794_4_2011 
	        				(
	        						formatIdentifier, versionNumber, certificationFlag, 
	        						sourceType, deviceVendor, deviceType,
	        						captureDate, noOfRepresentations,
	        						qualityBlocks, certificationBlocks, 
	        						fingerPosition, representationNo, scaleUnitType, 
	        						captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical, 
	        						imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical,
	        						bitDepth, compressionType,
	        						impressionType, lineLengthHorizontal, lineLengthVertical,
	        						noOfFingerPresent, imageData, 
	        						segmentationBlock, annotationBlock, commentBlock
    						); 
		        		
		        		if (isoData != null)
		        		{
		        			// Write bytes to tmp file.
		        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".iso");
	        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
	        		        tmpOutputStream.write(isoData);
		        		}
	    			}
	    			else
	    			{
	    				LOGGER.error("doFingerConversion :: Could Not convert the Image To ISO ");
	    			}
	    		}
	    	}
	    	else if (Integer.parseInt(convertTo) == 1) //Convert Finger ISO/IEC 19794-4: 2011 to JP2000
	    	{
	    		String filePath = new File (".").getCanonicalPath ();
	    		String fileName = filePath + biometricFolderPath + converionFile;
	    		File initialFile = new File(fileName);
	    		if (initialFile.exists())
	    		{
		    		LOGGER.info("doFingerConversion :: fileName ::" + fileName);
					byte[] isoData = Files.readAllBytes(Paths.get(fileName));
					byte [] imageData = FingerDecoder.convertFaceISO19794_4_2011ToImage (isoData); 
					
	        		if (imageData != null)
	        		{
	        			// Write bytes to tmp file.
	        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".jp2");
        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
        		        tmpOutputStream.write(imageData);
	        		}
	    		}
	    	}	    		 
		} catch (Exception ex) {
			LOGGER.info("doFingerConversion :: Error ", ex);
		}
		finally
		{
			try
			{
				if (imageInputStream != null)
					imageInputStream.close();
		        if(tmpOutputStream != null)
	                tmpOutputStream.close();
			}
			catch (Exception ex)
			{}
		}
		LOGGER.info("doFingerConversion :: Ended :: ");
    }
}
