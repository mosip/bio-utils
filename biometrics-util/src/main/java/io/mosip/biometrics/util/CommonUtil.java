package io.mosip.biometrics.util;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;

import io.mosip.biometrics.util.face.FaceBDIR;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.biometrics.util.face.FaceEncoder;
import io.mosip.biometrics.util.face.ImageDataType;
import io.mosip.biometrics.util.finger.FingerBDIR;
import io.mosip.biometrics.util.finger.FingerDecoder;
import io.mosip.biometrics.util.finger.FingerEncoder;
import io.mosip.biometrics.util.finger.FingerImageCompressionType;
import io.mosip.biometrics.util.iris.ImageFormat;
import io.mosip.biometrics.util.iris.IrisBDIR;
import io.mosip.biometrics.util.iris.IrisDecoder;
import io.mosip.biometrics.util.iris.IrisEncoder;

public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    public static BufferedImage getBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
        BufferedImage bufferedImage = null;
        switch (convertRequestDto.getImageType()) {
            case 0://JP2000
                bufferedImage = ImageIO.read(new ByteArrayInputStream(convertRequestDto.getInputBytes()));
                break;
            case 1://WSQ
                WsqDecoder decoder = new WsqDecoder ();
                Bitmap bitmap = decoder.decode(convertRequestDto.getInputBytes());
                bufferedImage = convert(bitmap);
                break;
        }
        return bufferedImage;
    }

    private static BufferedImage convert(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] data = bitmap.getPixels();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, width, height, data);
        return image;
    }

    public static byte[] convertJP2ToJPEGBytes(byte[] jp2000Bytes) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(jp2000Bytes)), "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Failed to get jpg image", e);
        }
        return null;
    }

    public static byte[] convertJP2ToJPEGBytes(BufferedImage image) {
    	
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        	ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Failed to get jpg image", e);
        }
        return null;
    }

    public static byte[] convertJP2ToPNGBytes(byte[] jp2000Bytes) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(jp2000Bytes)), "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Failed to get png image", e);
        }
        return null;
    }
    
    public static byte[] convertJP2ToPNGBytes(BufferedImage image) {
    	
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        	ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Failed to get png image", e);
        }
        return null;
    }

    /**
	 * It Converts ISO for modality Face, Iris, Finger having JP2000 or WSQ to JPEG or PNG format ISO
	 * 
	 * @param inIsoData (ISO base64URLEncoded)
	 * @param modality (Face, Iris, Finger)
	 * @param imageType (JPEG or PNG)
	 * @return outIsoData (ISO base64URLEncoded)
	 */
    public static String convertISOImageType(String inIsoData, Modality modality, ImageType imageType) throws Exception
    {
    	String outIsoData = null;
    	switch(modality)
    	{
    		case Face:
    			outIsoData = convertISOFace(inIsoData, modality, imageType);
    			break;
    		case Iris:
    			outIsoData = convertISOIris(inIsoData, modality, imageType);
    			break;
    		case Finger:
    			outIsoData = convertISOFinger(inIsoData, modality, imageType);
    			break;
    	}    		
    	return outIsoData;
    }

    private static String convertISOFace(String isoData, Modality modality, ImageType imageType) throws Exception {
    	ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality(modality.name());
		requestDto.setVersion("ISO19794_5_2011");
		byte[] isoDataBytes = decodeURLSafeBase64(isoData);
		requestDto.setInputBytes(isoDataBytes);
		
		FaceBDIR bdir = FaceDecoder.getFaceBDIR(requestDto);
		ImageDataType inCompressionType = bdir.getRepresentation().getRepresentationHeader().getImageInformation().getImageDataType();
		ImageDataType outCompressionType = ImageDataType.JPEG;
		byte [] inImageData = bdir.getRepresentation().getRepresentationData().getImageData().getImage();
		byte [] outImageData = null, outIsoData = null;
		if (inCompressionType == ImageDataType.JPEG2000_LOSS_LESS)
		{
			if (imageType == ImageType.JPEG)
				outImageData = convertJP2ToJPEGBytes(inImageData);
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}

		if (imageType == ImageType.JPEG)
			outCompressionType = ImageDataType.JPEG;
		else if (imageType == ImageType.PNG)
			outCompressionType = ImageDataType.PNG;
			
		outIsoData = FaceEncoder.convertFaceImageToISO19794_5_2011
			(
				bdir.getGeneralHeader().getFormatIdentifier(), bdir.getGeneralHeader().getVersionNumber(), 
				bdir.getGeneralHeader().getCertificationFlag(), bdir.getGeneralHeader().getTemporalSemantics(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDateTime(), 
				bdir.getGeneralHeader().getNoOfRepresentations(), 
				(short)bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getNoOfLandMarkPoints(), 
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getGender(),
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getEyeColor(),
				0, 
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getHairColor(),
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getSubjectHeight(),
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getExpressionMask(),
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getFeaturesMask(),
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getPoseAngle(),
				bdir.getRepresentation().getRepresentationHeader().getFacialInformation().getPoseAngleUncertainty(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getFaceImageType(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceTechnologyIdentifier(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceVendorIdentifier(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceTypeIdentifier(),
				bdir.getRepresentation().getRepresentationHeader().getQualityBlocks(),
				outImageData,
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getWidth(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getHeight(),
				outCompressionType,
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getSpatialSamplingRateLevel(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getPostAcquistionProcessing(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getCrossReference(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getImageColorSpace(),
				bdir.getRepresentation().getRepresentationHeader().getLandmarkPoints()
			);
		
        return encodeToURLSafeBase64(outIsoData);
    }

    private static String convertISOIris(String isoData, Modality modality, ImageType imageType) throws Exception {
    	ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality(modality.name());
		requestDto.setVersion("ISO19794_6_2011");
		byte[] isoDataBytes = decodeURLSafeBase64(isoData);
		requestDto.setInputBytes(isoDataBytes);
		
		IrisBDIR bdir = IrisDecoder.getIrisBDIR(requestDto);
		ImageFormat inCompressionType = bdir.getRepresentation().getRepresentationHeader().getImageInformation().getImageFormat();
		ImageFormat outCompressionType = ImageFormat.MONO_JPEG;
		byte [] inImageData = bdir.getRepresentation().getRepresentationData().getImageData().getImage();
		byte [] outImageData = null, outIsoData = null;
		if (inCompressionType == ImageFormat.MONO_JPEG2000_LOSS_LESS)
		{
			if (imageType == ImageType.JPEG)
			{
				outImageData = convertJP2ToJPEGBytes(inImageData);
				;//throw new Exception ("ISO19794_6_2011 ISO --- Does not Support JPEG IMAGE Format");
			}
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}

		if (imageType == ImageType.JPEG)
			outCompressionType = ImageFormat.MONO_JPEG;
		else if (imageType == ImageType.PNG)
			outCompressionType = ImageFormat.MONO_PNG;
			
		outIsoData = IrisEncoder.convertIrisImageToISO19794_6_2011
			(
				bdir.getGeneralHeader().getFormatIdentifier(), bdir.getGeneralHeader().getVersionNumber(), 
				bdir.getGeneralHeader().getCertificationFlag(), 
				bdir.getRepresentation().getRepresentationHeader().getCaptureDateTime(),
				bdir.getGeneralHeader().getNoOfRepresentations(),
				bdir.getRepresentation().getRepresentationHeader().getRepresentationNo(),
				bdir.getGeneralHeader().getNoOfEyesPresent(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getEyeLabel(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getImageType(),
				outCompressionType,
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getHorizontalOrientation(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getVerticalOrientation(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getCompressionType(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getWidth(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getHeight(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getBitDepth(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getRange(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getRollAngleOfEye(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getRollAngleUncertainty(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterSmallestX(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterLargestX(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterSmallestY(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getIrisCenterLargestY(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getIrisDiameterSmallest(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getIrisDiameterLargest(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceTechnologyIdentifier(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceVendorIdentifier(), 
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceTypeIdentifier(), 
				bdir.getRepresentation().getRepresentationHeader().getQualityBlocks(), 
				outImageData,
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getWidth(),
				bdir.getRepresentation().getRepresentationHeader().getImageInformation().getHeight()
			);
		
        return encodeToURLSafeBase64(outIsoData);
    }
    
    private static String convertISOFinger(String isoData, Modality modality, ImageType imageType) throws Exception {
    	ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality(modality.name());
		requestDto.setVersion("ISO19794_4_2011");
		byte[] isoDataBytes = decodeURLSafeBase64(isoData);
		requestDto.setInputBytes(isoDataBytes);
		
		FingerBDIR bdir = FingerDecoder.getFingerBDIR(requestDto);
		FingerImageCompressionType inCompressionType = bdir.getRepresentation().getRepresentationHeader().getCompressionType();
		FingerImageCompressionType outCompressionType = FingerImageCompressionType.JPEG_LOSSY;
		byte [] inImageData = bdir.getRepresentation().getRepresentationBody().getImageData().getImage();
		byte [] outImageData = null, outIsoData = null;
		if (inCompressionType == FingerImageCompressionType.JPEG_2000_LOSS_LESS)
		{
			if (imageType == ImageType.JPEG)
				outImageData = convertJP2ToJPEGBytes(inImageData);
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}
		else if (inCompressionType == FingerImageCompressionType.WSQ)
		{
			WsqDecoder decoder = new WsqDecoder ();
			Bitmap bitmap = decoder.decode(inImageData);
			BufferedImage image = convert(bitmap);
			if (imageType == ImageType.JPEG)
				outImageData = convertJP2ToJPEGBytes(image);
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(image);
		}
		if (imageType == ImageType.JPEG)
			outCompressionType = FingerImageCompressionType.JPEG_LOSSY;
		else if (imageType == ImageType.PNG)
			outCompressionType = FingerImageCompressionType.PNG;
			
		outIsoData = FingerEncoder.convertFingerImageToISO19794_4_2011
			(
				bdir.getGeneralHeader().getFormatIdentifier(), bdir.getGeneralHeader().getVersionNumber(), 
				bdir.getRepresentation().getCertificationFlag(), bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceTechnologyIdentifier(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceVendorIdentifier(), bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceTypeIdentifier(), 
				bdir.getRepresentation().getRepresentationHeader().getCaptureDateTime(), 
				bdir.getGeneralHeader().getNoOfRepresentations(), 
				bdir.getRepresentation().getRepresentationHeader().getQualityBlocks(), 
				bdir.getRepresentation().getRepresentationHeader().getCertificationBlocks(), 
				bdir.getRepresentation().getRepresentationHeader().getFingerPosition(),
				bdir.getRepresentation().getRepresentationHeader().getRepresentationNo(),
				bdir.getRepresentation().getRepresentationHeader().getScaleUnits(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceSpatialSamplingRateHorizontal(),
				bdir.getRepresentation().getRepresentationHeader().getCaptureDeviceSpatialSamplingRateVertical(),
				bdir.getRepresentation().getRepresentationHeader().getImageSpatialSamplingRateHorizontal(),
				bdir.getRepresentation().getRepresentationHeader().getImageSpatialSamplingRateVertical(),
				bdir.getRepresentation().getRepresentationHeader().getBitDepth(),
				outCompressionType,
				bdir.getRepresentation().getRepresentationHeader().getImpressionType(),
				bdir.getRepresentation().getRepresentationHeader().getLineLengthHorizontal(),
				bdir.getRepresentation().getRepresentationHeader().getLineLengthVertical(),
				bdir.getGeneralHeader().getNoOfFingerPresent(),
				outImageData,
				bdir.getRepresentation().getRepresentationBody().getSegmentationBlock() != null ? bdir.getRepresentation().getRepresentationBody().getSegmentationBlock() : null,
				bdir.getRepresentation().getRepresentationBody().getAnnotationBlock() != null ? bdir.getRepresentation().getRepresentationBody().getAnnotationBlock() : null,
				bdir.getRepresentation().getRepresentationBody().getCommentBlocks() != null ? bdir.getRepresentation().getRepresentationBody().getCommentBlocks() : null
			);
		
        return encodeToURLSafeBase64(outIsoData);
    }
    
    private static Encoder urlSafeEncoder;
		
	static {
		urlSafeEncoder = Base64.getUrlEncoder().withoutPadding();
	}	
    public static String encodeToURLSafeBase64(byte[] data) {
		if (isNullEmpty(data)) {
			return null;
		}
		return urlSafeEncoder.encodeToString(data);
	}

    public static String encodeToURLSafeBase64(String data) {
		if (isNullEmpty(data)) {
			return null;
		}
		return urlSafeEncoder.encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

    public static byte[] decodeURLSafeBase64(String data) {
		if (isNullEmpty(data)) {
			return null;
		}
		return Base64.getUrlDecoder().decode(data);
	}
	
	public static boolean isNullEmpty(byte[] array) {
		return array == null || array.length == 0;
	}
	public static boolean isNullEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
}

