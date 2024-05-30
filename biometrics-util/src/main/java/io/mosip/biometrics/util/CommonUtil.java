package io.mosip.biometrics.util;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import javax.imageio.ImageIO;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Base64.Encoder;

import io.mosip.biometrics.util.constant.BiometricUtilErrorCode;
import io.mosip.biometrics.util.exception.BiometricUtilException;
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
		/**
		 * In Java < 12 use nu.pattern.OpenCV.loadShared() and
		 * System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME)
		 */
		// nu.pattern.OpenCV.loadShared();
		// System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		/**
		 * In Java >= 12 it is no longer possible to use addLibraryPath, which modifies
		 * the ClassLoader's static usr_paths field. There does not seem to be any way
		 * around this so we fall back to loadLocally() and return.
		 */
		nu.pattern.OpenCV.loadLocally();
	}

	private CommonUtil() {
		throw new IllegalStateException("CommonUtil class");
	}

	@SuppressWarnings({ "java:S112" })
	public static BufferedImage getBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
		switch (convertRequestDto.getImageType()) {
		case 0:// JP2000
			return ImageIO.read(new ByteArrayInputStream(convertRequestDto.getInputBytes()));
		case 1:// WSQ
			return convertWSQToBufferedImage(convertRequestDto.getInputBytes());
		default:
			throw new BiometricUtilException(BiometricUtilErrorCode.IMAGE_TYPE_NOT_SUPPORTED_EXCEPTION.getErrorCode(),
					BiometricUtilErrorCode.IMAGE_TYPE_NOT_SUPPORTED_EXCEPTION.getErrorMessage());
		}
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
			LOGGER.error("convertJP2ToJPEGBytes::Failed to get jpg image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertJP2ToJPEGUsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".jpeg", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToJPEGUsingOpenCV::Failed to get jpg image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertBufferedImageToJPEGBytes(BufferedImage image) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("convertBufferedImageToJPEGBytes::Failed to get jpg image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertJP2ToPNGBytes(byte[] jp2000Bytes) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(ImageIO.read(new ByteArrayInputStream(jp2000Bytes)), "png", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("convertJP2ToPNGBytes::Failed to get png image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertBufferedImageToPNGBytes(BufferedImage image) {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "png", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			LOGGER.error("convertBufferedImageToPNGBytes::Failed to get png image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertJP2ToPNGUsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_PNG_COMPRESSION, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".png", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToPNGUsingOpenCV::Failed to get png image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertJP2ToJP2UsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG2000_COMPRESSION_X1000, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".jp2", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToJP2UsingOpenCV::Failed to get jp2 image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertJPEGToJP2UsingOpenCV(byte[] jpegBytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jpegBytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG2000_COMPRESSION_X1000, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".jp2", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJPEGToJP2UsingOpenCV::Failed to get jp2 image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	public static byte[] convertJP2ToWEBPUsingOpenCV(byte[] jp2000Bytes, int compressionRatio) {
		try {
			Mat src = Imgcodecs.imdecode(new MatOfByte(jp2000Bytes), Imgcodecs.IMREAD_UNCHANGED);
			MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_WEBP_QUALITY, compressionRatio);

			MatOfByte mem = new MatOfByte();
			Imgcodecs.imencode(".webp", src, mem, map);
			return mem.toArray();
		} catch (Exception e) {
			LOGGER.error("convertJP2ToWEBPUsingOpenCV::Failed to get webp image", e);
		}
		throw new BiometricUtilException(BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.CONVERT_EXCEPTION.getErrorMessage());
	}

	/*
	 * * @deprecated (since = "1.2.1") This method will not be acceptable in future
	 * versions. <p> Use {@link convertBufferedImageToMat(image)} instead.
	 */
	@SuppressWarnings({ "java:S100" })
	@Deprecated(since = "1.2.1", forRemoval = true)
	public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);
		byteArrayOutputStream.flush();
		return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
	}

	@SuppressWarnings({ "java:S4144" })
	public static Mat convertBufferedImageToMat(BufferedImage image) throws IOException {
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
			throw new BiometricUtilException(BiometricUtilErrorCode.MODALITY_NOT_SUPPORTED_EXCEPTION.getErrorCode(),
					BiometricUtilErrorCode.MODALITY_NOT_SUPPORTED_EXCEPTION.getErrorMessage());
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
		byte[] outImageData = null;
		byte[] outIsoData = null;
		if (inImageDataType == ImageDataType.JPEG2000_LOSS_LESS) {
			if (imageType == ImageType.JPEG)
				outImageData = convertJP2ToJPEGBytes(inImageData);
			else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}

		if (imageType == ImageType.PNG)
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
		byte[] outImageData = null;
		byte[] outIsoData = null;
		if (imageFormat == ImageFormat.MONO_JPEG2000) {
			if (imageType == ImageType.JPEG) {
				outImageData = convertJP2ToJPEGBytes(inImageData);
			} else if (imageType == ImageType.PNG)
				outImageData = convertJP2ToPNGBytes(inImageData);
		}

		if (imageType == ImageType.PNG)
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
		byte[] outImageData = null;
		byte[] outIsoData = null;
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

		if (imageType == ImageType.PNG)
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

	/**
	 * Flips an image horizontally, vertically, or both based on the provided flags.
	 *
	 * @param srcImage       The source image as a BufferedImage object. (Must not
	 *                       be null)
	 * @param dstImage       The destination image to store the flipped image. (Must
	 *                       not be null) Dimensions of dstImage must match
	 *                       srcImage.
	 * @param flipHorizontal A boolean flag indicating whether to flip horizontally
	 *                       (true) or not (false).
	 * @param flipVertical   A boolean flag indicating whether to flip vertically
	 *                       (true) or not (false).
	 * @throws IllegalArgumentException if either source or destination image is
	 *                                  null, or if their dimensions differ.
	 */
	public static void flipImage(BufferedImage srcImage, BufferedImage dstImage, boolean flipHorizontal,
			boolean flipVertical) {
		if (srcImage == null || dstImage == null) {
			throw new IllegalArgumentException("Source or destination image cannot be null");
		}

		if (srcImage.getWidth() != dstImage.getWidth() || srcImage.getHeight() != dstImage.getHeight()) {
			throw new IllegalArgumentException("Source and destination images must have the same dimensions");
		}
		// Create a transformation object
		AffineTransform transform = new AffineTransform();

		// Apply horizontal and vertical flips if requested
		if (flipHorizontal) {
			transform.scale(-1, 1);
			transform.translate(-srcImage.getWidth(), 0);
		}
		if (flipVertical) {
			transform.scale(1, -1);
			transform.translate(0, -srcImage.getHeight());
		}

		// Apply the transformation to the image
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		op.filter(srcImage, dstImage);
	}

	/**
	 * Flips an image horizontally, vertically, or both using OpenCV.
	 *
	 * @param image          The input image as a OpenCV Mat object.
	 * @param flipHorizontal A boolean flag indicating whether to flip horizontally
	 *                       (true) or not (false).
	 * @param flipVertical   A boolean flag indicating whether to flip vertically
	 *                       (true) or not (false).
	 * @return A new Mat object containing the flipped image.
	 * @throws IllegalArgumentException if the input image is null.
	 */
	public static Mat flipImageUsingOpenCV(Mat image, boolean flipHorizontal, boolean flipVertical) {
		if (image == null) {
			throw new IllegalArgumentException("Input image cannot be null");
		}
		// Create an empty Mat object with the same size and type as the original image
		Mat flippedImage = new Mat(image.size(), image.type());

		// Use OpenCV's core.flip method to perform the flipping
		int flipCode = -1;
		if (flipHorizontal && flipVertical) {
			flipCode = 0; // Flip vertically and horizontally
		} else if (flipHorizontal) {
			flipCode = 1; // Flip horizontally
		} else if (flipVertical) {
			flipCode = -1; // Flip vertically
		}

		Core.flip(image, flippedImage, flipCode);

		// Return the flipped image
		return flippedImage;
	}

	/*
	 * requestDto inputBytes base64urldecoded
	 */
	@SuppressWarnings({ "java:S112" })
	public static BiometricInformationExchange nistParser(NistRequestDto requestDto) throws Exception {
		return getXmlMapper(getNamespaceXmlFactory()).readValue(requestDto.getInputBytes(),
				BiometricInformationExchange.class);
	}

	/*
	 * src should be base64urlencoded
	 */
	public static BiometricInformationExchange nistParser(String src) throws Exception {
		byte[] dataBytes = decodeURLSafeBase64(src);
		NistRequestDto requestDto = new NistRequestDto();
		requestDto.setInputBytes(dataBytes);

		return nistParser(requestDto);
	}

	/*
	 * src NISTBiometricInformationExchangePackage output : xml string
	 */
	@SuppressWarnings({ "java:S112" })
	public static String nistXml(BiometricInformationExchange nistRecord) throws Exception {
		return getXmlMapper(getNamespaceXmlFactory()).writeValueAsString(nistRecord);
	}

	@SuppressWarnings({ "java:S1172" })
	public static XmlMapper getXmlMapper(NamespaceXmlFactory xmlFactory) {
		XmlMapper xmlMapper = new XmlMapper(getNamespaceXmlFactory());
		xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY);

		return xmlMapper;
	}

	@SuppressWarnings({ "java:S125" })
	public static NamespaceXmlFactory getNamespaceXmlFactory() {
		String defaultNamespace = XmlnsNameSpaceConstant.NAMESPACE_URL_ITL;
		Map<String, String> otherNamespaces = new HashMap<>();
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
		/*
		 * otherNamespaces.put(XmlnsNameSpaceConstant.NAMESPACE_RENAPO,
		 * XmlnsNameSpaceConstant.NAMESPACE_URL_RENAPO);
		 */

		return new NamespaceXmlFactory(defaultNamespace, otherNamespaces);
	}

	private static Encoder urlSafeEncoder;

	static {
		urlSafeEncoder = Base64.getUrlEncoder().withoutPadding();
	}

	public static String encodeToURLSafeBase64(byte[] data) {
		if (isNullEmpty(data))
			throw new BiometricUtilException(BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorCode(),
					BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorMessage());

		return urlSafeEncoder.encodeToString(data);
	}

	public static String encodeToURLSafeBase64(String data) {
		if (isNullEmpty(data))
			throw new BiometricUtilException(BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorCode(),
					BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorMessage());

		return urlSafeEncoder.encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	public static byte[] decodeURLSafeBase64(String data) {
		if (isNullEmpty(data))
			throw new BiometricUtilException(BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorCode(),
					BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorMessage());

		return Base64.getUrlDecoder().decode(data);
	}

	public static byte[] decodeURLSafeBase64(byte[] data) {
		if (isNullEmpty(data))
			throw new BiometricUtilException(BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorCode(),
					BiometricUtilErrorCode.DATA_NULL_OR_EMPTY_EXCEPTION.getErrorMessage());

		return Base64.getUrlDecoder().decode(data);
	}

	public static boolean isNullEmpty(byte[] array) {
		return Objects.isNull(array) || array.length == 0;
	}

	public static boolean isNullEmpty(String str) {
		return Objects.isNull(str) || str.trim().length() == 0;
	}

	public static byte[] getLastBytes(byte[] xorBytes, int lastBytesNum) {
		if (xorBytes.length >= lastBytesNum)
			return Arrays.copyOfRange(xorBytes, xorBytes.length - lastBytesNum, xorBytes.length);

		throw new BiometricUtilException(BiometricUtilErrorCode.TECHNICAL_ERROR_EXCEPTION.getErrorCode(),
				BiometricUtilErrorCode.TECHNICAL_ERROR_EXCEPTION.getErrorMessage());
	}

	public static byte[] toUtf8ByteArray(String arg) {
		return arg.getBytes(StandardCharsets.UTF_8);
	}

	public static byte[] getXOR(String a, String b) {
		byte[] aBytes = a.getBytes();
		byte[] bBytes = b.getBytes();
		// Lengths of the given strings
		int aLen = aBytes.length;
		int bLen = bBytes.length;

		// Make both the strings of equal lengths
		// by inserting 0s in the beginning
		if (aLen > bLen) {
			bBytes = prependZeros(bBytes, aLen - bLen);
		} else if (bLen > aLen) {
			aBytes = prependZeros(aBytes, bLen - aLen);
		}

		// Updated length
		int len = Math.max(aLen, bLen);
		byte[] xorBytes = new byte[len];

		// To store the resultant XOR
		for (int i = 0; i < len; i++) {
			xorBytes[i] = (byte) (aBytes[i] ^ bBytes[i]);
		}
		return xorBytes;
	}

	public static byte[] prependZeros(byte[] str, int n) {
		byte[] newBytes = new byte[str.length + n];
		int i = 0;
		for (; i < n; i++) {
			newBytes[i] = 0;
		}

		for (int j = 0; i < newBytes.length; i++, j++) {
			newBytes[i] = str[j];
		}

		return newBytes;
	}

	public static byte[] hexStringToByteArray(String thumbprint) {
		int len = thumbprint.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(thumbprint.charAt(i), 16) << 4)
					+ Character.digit(thumbprint.charAt(i + 1), 16));
		}
		return data;
	}
	
	public static byte[] concatByteArrays(byte[] thumbprint, byte[] sessionkey, byte[] keySplitter, byte[] data) {
		ByteBuffer result = ByteBuffer
				.allocate(thumbprint.length + sessionkey.length + keySplitter.length + data.length);
		result.put(thumbprint);
		result.put(sessionkey);
		result.put(keySplitter);
		result.put(data);
		return result.array();
	}
}