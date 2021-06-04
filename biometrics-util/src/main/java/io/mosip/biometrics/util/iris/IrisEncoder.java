package io.mosip.biometrics.util.iris;

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

public class IrisEncoder {	
	 private static byte [] convertIrisImageToISO19794_6_2011
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

	public static byte [] convertIrisImageToISO(ConvertRequestDto convertRequestDto) throws Exception
	{
		switch (convertRequestDto.getVersion()) {
			case "ISO19794_6_2011" :
				IrisFormatIdentifier formatIdentifier = IrisFormatIdentifier.FORMAT_IIR;
				IrisVersionNumber versionNumber = IrisVersionNumber.VERSION_020;
				IrisCertificationFlag certificationFlag = IrisCertificationFlag.UNSPECIFIED;

				Date captureDate = new Date ();// the date instance

				IrisQualityAlgorithmVendorIdentifier algorithmVendorIdentifier = IrisQualityAlgorithmVendorIdentifier.ALGORITHM_VENDOR_IDENTIFIER;
				IrisQualityAlgorithmIdentifier qualityAlgorithmIdentifier = IrisQualityAlgorithmIdentifier.ALGORITHM_IDENTIFIER;
				EyeLabel eyeLabel = getEyeLabel(convertRequestDto.getBiometricSubType());

				ImageType imageType = convertRequestDto.getPurpose().equalsIgnoreCase("AUTH") ? ImageType.CROPPED_AND_MASKED :
						ImageType.CROPPED;
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
				BufferedImage bufferedImage = CommonUtil.getBufferedImage(convertRequestDto);
				int imageWidth = bufferedImage.getWidth();
				int imageHeight = bufferedImage.getHeight();

				return convertIrisImageToISO19794_6_2011
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
								convertRequestDto.getInputBytes(), imageWidth, imageHeight
						);
		}
		throw new UnsupportedOperationException();
	}

	private static EyeLabel getEyeLabel(String biometricSubType) {
		if(biometricSubType == null)
			return EyeLabel.UNSPECIFIED;

		switch (biometricSubType) {
			case "Right":
				return EyeLabel.RIGHT;
			case "Left":
				return EyeLabel.LEFT;
			default:
				return EyeLabel.UNSPECIFIED;
		}
	}
}
