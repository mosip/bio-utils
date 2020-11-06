package org.mosip.iso.finger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class FingerEncoder {
	
	 public static byte [] convertFingerImageToISO19794_4_2011 
 		(
			FingerFormatIdentifier formatIdentifier, FingerVersionNumber versionNumber,
			FingerCertificationFlag certificationFlag, 
			FingerCaptureDeviceTechnology sourceType, FingerCaptureDeviceVendor deviceVendor, FingerCaptureDeviceType deviceType,
			Date captureDate, int noOfRepresentations,
			FingerQualityBlock [] qualityBlocks, FingerCertificationBlock[] certificationBlocks, 
			FingerPosition fingerPosition, int representationNo, 
			FingerScaleUnitType scaleUnitType, 
			int captureDeviceSpatialSamplingRateHorizontal, int captureDeviceSpatialSamplingRateVertical, 
			int imageSpatialSamplingRateHorizontal, int imageSpatialSamplingRateVertical,
			FingerImageBitDepth bitDepth, FingerImageCompressionType compressionType,
			FingerImpressionType impressionType, int lineLengthHorizontal, int lineLengthVertical,
			int noOfFingerPresent, byte [] image, 
			SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock, CommentBlock commentBlock
		) throws IOException
	{	
		 FingerBDIR irisBDIR = new FingerBDIR 
			 (
				 formatIdentifier, versionNumber,
				 certificationFlag, sourceType, deviceVendor, deviceType,
				 captureDate, noOfRepresentations,
				 qualityBlocks, certificationBlocks, 
				 fingerPosition, representationNo, 
				 scaleUnitType, captureDeviceSpatialSamplingRateHorizontal, captureDeviceSpatialSamplingRateVertical, 
				 imageSpatialSamplingRateHorizontal, imageSpatialSamplingRateVertical,
				 bitDepth, compressionType,
				 impressionType, lineLengthHorizontal, lineLengthVertical,
				 noOfFingerPresent, image, 
				 segmentationBlock, annotationBlock, commentBlock
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
