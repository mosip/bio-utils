package io.mosip.biometrics.util;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
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
import io.mosip.biometrics.util.nist.parser.v2011.constant.XmlnsNameSpaceConstant;
import io.mosip.biometrics.util.nist.parser.v2011.dto.BiometricInformationExchange;
import io.mosip.biometrics.util.nist.parser.v2011.helper.NamespaceXmlFactory;

public class CommonUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	static {
		// load OpenCV library
		nu.pattern.OpenCV.loadShared();
		/**
         * In Java >= 12 it is no longer possible to use addLibraryPath, which modifies the
         * ClassLoader's static usr_paths field. There does not seem to be any way around this
         * so we fall back to loadLocally() and return.
         */
		//nu.pattern.OpenCV.loadLocally();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
	}

	public static BufferedImage getBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
		BufferedImage bufferedImage = null;
		switch (convertRequestDto.getImageType()) {
		case 0:// JP2000
			bufferedImage = ImageIO.read(new ByteArrayInputStream(convertRequestDto.getInputBytes()));
			break;
		case 1:// WSQ
			bufferedImage = convertWSQToBufferedImage(convertRequestDto.getInputBytes());
			break;
		}
		return bufferedImage;
	}

	public static BufferedImage convertWSQToBufferedImage(byte[] arrImage) {
		WsqDecoder decoder = new WsqDecoder();
		Bitmap bitmap = decoder.decode(arrImage);
		return convert(bitmap);
	}

	public static BufferedImage convert(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		byte[] data = bitmap.getPixels();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		raster.setDataElements(0, 0, width, height, data);
		return image;
	}

	public static byte[] convertJP2ToJPEGBytes(byte[] jp2000Bytes) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(jp2000Bytes)), "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get jpg image", e);
		}
		return null;
	}

	public static byte[] convertJP2ToJPEGUsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".jpeg", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToJPEGUsingOpenCV>>Failed to get jpg image", e);
		}
		return null;
	}

	public static byte[] convertBufferedImageToJPEGBytes(BufferedImage image) {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get jpg image", e);
		}
		return null;
	}

	public static byte[] convertJP2ToPNGBytes(byte[] jp2000Bytes) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(jp2000Bytes)), "png", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get png image", e);
		}
		return null;
	}

	public static byte[] convertBufferedImageToPNGBytes(BufferedImage image) {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "png", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("Failed to get png image", e);
		}
		return null;
	}

	public static byte[] convertJP2ToPNGUsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_PNG_COMPRESSION, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".png", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToPNGUsingOpenCV>>Failed to get png image", e);
		}
		return null;
	}

	public static byte[] convertJP2ToJP2UsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG2000_COMPRESSION_X1000, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".jp2", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToJP2UsingOpenCV>>Failed to get jp2 image", e);
		}
		return null;
	}

	public static byte[] convertJPEGToJP2UsingOpenCV(byte[] jpegBytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jpegBytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG2000_COMPRESSION_X1000, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".jp2", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJPEGToJP2UsingOpenCV>>Failed to get jp2 image", e);
		}
		return null;
	}
	
	public static byte[] convertJP2ToWEBPUsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_WEBP_QUALITY, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".webp", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToWEBPUsingOpenCV>>Failed to get webp image", e);
		}
		return null;
	}

	public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);
		byteArrayOutputStream.flush();
		return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
	}

	/**
	 * It Converts ISO for modality Face, Iris, Finger having JP2000 or WSQ to JPEG
	 * or PNG format ISO
	 * 
	 * @param inIsoData (ISO base64URLEncoded)
	 * @param modality  (Face, Iris, Finger)
	 * @param imageType (JPEG or PNG)
	 * @return outIsoData (ISO base64URLEncoded)
	 */
	public static String convertISOImageType(String inIsoData, Modality modality, ImageType imageType)
			throws Exception {
		String outIsoData = null;
		switch (modality) {
		case Face:
			outIsoData = convertISOFace(inIsoData, modality, imageType);
			break;
		case Iris:
			outIsoData = convertISOIris(inIsoData, modality, imageType);
			break;
		case Finger:
			outIsoData = convertISOFinger(inIsoData, modality, imageType);
			break;
		default:
			throw new Exception("Modality not Supported");
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
		int inImageDataType = bdir.getImageDataType();
		int outImageDataType = ImageDataType.JPEG;
		byte[] inImageData = bdir.getImage();
		byte[] outImageData = null, outIsoData = null;
		if (inImageDataType == ImageDataType.JPEG2000_LOSS_LESS) {
			if (imageType == ImageType.JPEG)
				outImageData = convertJP2ToJPEGBytes(inImageData);
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}

		if (imageType == ImageType.JPEG)
			outImageDataType = ImageDataType.JPEG;
		else if (imageType == ImageType.PNG)
			outImageDataType = ImageDataType.PNG;

		outIsoData = FaceEncoder.convertFaceImageToISO19794_5_2011(bdir.getFormatIdentifier(), bdir.getVersionNumber(),
				bdir.getCertificationFlag(), bdir.getTemporalSemantics(), bdir.getCaptureDateTime(),
				bdir.getNoOfRepresentations(), bdir.getNoOfLandMarkPoints(), bdir.getGender(), bdir.getEyeColor(),
				bdir.getHairColor(), bdir.getSubjectHeight(), bdir.getExpressionMask(), bdir.getFeaturesMask(),
				bdir.getPoseAngle(), bdir.getPoseAngleUncertainty(), bdir.getFaceImageType(),
				bdir.getCaptureDeviceTechnologyIdentifier(), bdir.getCaptureDeviceVendorIdentifier(),
				bdir.getCaptureDeviceTypeIdentifier(), bdir.getQualityBlocks(), outImageData, bdir.getWidth(),
				bdir.getHeight(), outImageDataType, bdir.getSpatialSamplingRateLevel(),
				bdir.getPostAcquistionProcessing(), bdir.getCrossReference(), bdir.getImageColorSpace(),
				bdir.getLandmarkPoints(), bdir.getThreeDInformationAndData());

		return encodeToURLSafeBase64(outIsoData);
	}

	private static String convertISOIris(String isoData, Modality modality, ImageType imageType) throws Exception {
		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality(modality.name());
		requestDto.setVersion("ISO19794_6_2011");
		byte[] isoDataBytes = decodeURLSafeBase64(isoData);
		requestDto.setInputBytes(isoDataBytes);

		IrisBDIR bdir = IrisDecoder.getIrisBDIR(requestDto);
		int imageFormat = bdir.getImageFormat();
		int outImageFormat = ImageFormat.MONO_JPEG;
		byte[] inImageData = bdir.getImage();
		byte[] outImageData = null, outIsoData = null;
		if (imageFormat == ImageFormat.MONO_JPEG2000) {
			if (imageType == ImageType.JPEG) {
				outImageData = convertJP2ToJPEGBytes(inImageData);
				;// throw new Exception ("ISO19794_6_2011 ISO --- Does not Support JPEG IMAGE
					// Format");
			} else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}

		if (imageType == ImageType.JPEG)
			outImageFormat = ImageFormat.MONO_JPEG;
		else if (imageType == ImageType.PNG)
			outImageFormat = ImageFormat.MONO_PNG;

		outIsoData = IrisEncoder.convertIrisImageToISO19794_6_2011(bdir.getFormatIdentifier(), bdir.getVersionNumber(),
				bdir.getCertificationFlag(), bdir.getCaptureDateTime(), bdir.getNoOfRepresentations(),
				bdir.getRepresentationNo(), bdir.getNoOfEyesPresent(), bdir.getEyeLabel(), bdir.getImageType(),
				outImageFormat, bdir.getHorizontalOrientation(), bdir.getVerticalOrientation(),
				bdir.getCompressionType(), bdir.getWidth(), bdir.getHeight(), bdir.getBitDepth(), bdir.getRange(),
				bdir.getRollAngleOfEye(), bdir.getRollAngleUncertainty(), bdir.getIrisCenterSmallestX(),
				bdir.getIrisCenterLargestX(), bdir.getIrisCenterSmallestY(), bdir.getIrisCenterLargestY(),
				bdir.getIrisDiameterSmallest(), bdir.getIrisDiameterLargest(),
				bdir.getCaptureDeviceTechnologyIdentifier(), bdir.getCaptureDeviceVendorIdentifier(),
				bdir.getCaptureDeviceTypeIdentifier(), bdir.getQualityBlocks(), outImageData, bdir.getWidth(),
				bdir.getHeight());

		return encodeToURLSafeBase64(outIsoData);
	}

	private static String convertISOFinger(String isoData, Modality modality, ImageType imageType) throws Exception {
		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality(modality.name());
		requestDto.setVersion("ISO19794_4_2011");
		byte[] isoDataBytes = decodeURLSafeBase64(isoData);
		requestDto.setInputBytes(isoDataBytes);

		FingerBDIR bdir = FingerDecoder.getFingerBDIR(requestDto);
		int inCompressionType = bdir.getCompressionType();
		int outCompressionType = FingerImageCompressionType.JPEG_LOSSY;
		byte[] inImageData = bdir.getImage();
		byte[] outImageData = null, outIsoData = null;
		if (inCompressionType == FingerImageCompressionType.JPEG_2000_LOSS_LESS) {
			if (imageType == ImageType.JPEG)
				outImageData = convertJP2ToJPEGBytes(inImageData);
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		} else if (inCompressionType == FingerImageCompressionType.WSQ) {
			WsqDecoder decoder = new WsqDecoder();
			Bitmap bitmap = decoder.decode(inImageData);
			BufferedImage image = convert(bitmap);
			if (imageType == ImageType.JPEG)
				outImageData = convertBufferedImageToJPEGBytes(image);
			else if (imageType == ImageType.PNG)
				outImageData = convertBufferedImageToPNGBytes(image);
		}
		if (imageType == ImageType.JPEG)
			outCompressionType = FingerImageCompressionType.JPEG_LOSSY;
		else if (imageType == ImageType.PNG)
			outCompressionType = FingerImageCompressionType.PNG;

		outIsoData = FingerEncoder.convertFingerImageToISO19794_4_2011(bdir.getFormatIdentifier(),
				bdir.getVersionNumber(), bdir.getCertificationFlag(), bdir.getCaptureDeviceTechnologyIdentifier(),
				bdir.getCaptureDeviceVendorIdentifier(), bdir.getCaptureDeviceTypeIdentifier(),
				bdir.getCaptureDateTime(), bdir.getNoOfRepresentations(), bdir.getQualityBlocks(),
				bdir.getCertificationBlocks(), bdir.getFingerPosition(), bdir.getRepresentationNo(),
				bdir.getScaleUnits(), bdir.getCaptureDeviceSpatialSamplingRateHorizontal(),
				bdir.getCaptureDeviceSpatialSamplingRateVertical(), bdir.getImageSpatialSamplingRateHorizontal(),
				bdir.getImageSpatialSamplingRateVertical(), bdir.getBitDepth(), outCompressionType,
				bdir.getImpressionType(), bdir.getLineLengthHorizontal(), bdir.getLineLengthVertical(),
				bdir.getNoOfFingerPresent(), outImageData,
				bdir.getSegmentationBlock() != null ? bdir.getSegmentationBlock() : null,
				bdir.getAnnotationBlock() != null ? bdir.getAnnotationBlock() : null,
				bdir.getCommentBlocks() != null ? bdir.getCommentBlocks() : null);

		return encodeToURLSafeBase64(outIsoData);
	}
	
	/*
	 * requestDto inputBytes base64urldecoded
	 */
	public static BiometricInformationExchange nistParser(NistRequestDto requestDto) throws Exception {
		return getXmlMapper (getNamespaceXmlFactory()).readValue(requestDto.getInputBytes(), BiometricInformationExchange.class);
	}
	
	/*
	 * src should be base64urlencoded
	 */
	public static BiometricInformationExchange nistParser(String src) throws Exception  {
		byte[] dataBytes = decodeURLSafeBase64(src);
		NistRequestDto requestDto = new NistRequestDto();
		requestDto.setInputBytes(dataBytes);
		
		return nistParser(requestDto);
	}

	/*
	 * src NISTBiometricInformationExchangePackage
	 * output : xml string
	 */
	public static String nistXml(BiometricInformationExchange nistRecord) throws JsonProcessingException {
		return getXmlMapper(getNamespaceXmlFactory()).writeValueAsString(nistRecord);
	}

	public static XmlMapper getXmlMapper(NamespaceXmlFactory xmlFactory)
	{
		XmlMapper xmlMapper = new XmlMapper(getNamespaceXmlFactory ());
		xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY); 
		
		return xmlMapper;
	}
	
	public static NamespaceXmlFactory getNamespaceXmlFactory()
	{
		String defaultNamespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL;
        Map<String, String> otherNamespaces =  new HashMap<String, String>();
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_S, XmlnsNameSpaceConstant.NAMESPACE_URL_S);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_I, XmlnsNameSpaceConstant.NAMESPACE_URL_I);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_ITL, XmlnsNameSpaceConstant.NAMESPACE_URL_ITL);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_BIOM, XmlnsNameSpaceConstant.NAMESPACE_URL_BIOM);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_NC, XmlnsNameSpaceConstant.NAMESPACE_URL_NC);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_NIEM_XSD, XmlnsNameSpaceConstant.NAMESPACE_URL_NIEM_XSD);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_ISO_3166, XmlnsNameSpaceConstant.NAMESPACE_URL_ISO_3166);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_ISO_639_3, XmlnsNameSpaceConstant.NAMESPACE_URL_ISO_639_3);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_GENC, XmlnsNameSpaceConstant.NAMESPACE_URL_GENC);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_FBI, XmlnsNameSpaceConstant.NAMESPACE_URL_FBI);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_XSI, XmlnsNameSpaceConstant.NAMESPACE_URL_XSI);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_INT_I, XmlnsNameSpaceConstant.NAMESPACE_URL_INT_I);
        otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_RENAPO, XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO);
        
        return new NamespaceXmlFactory(defaultNamespace, otherNamespaces);
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
