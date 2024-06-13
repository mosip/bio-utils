package io.mosip.biometrics.util.test;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImageRotateApplication
 *
 */
public class ImageRotateApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageRotateApplication.class);

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
		// nu.pattern.OpenCV.loadLocally();
	}

	@SuppressWarnings({ "java:S3776" })
	public static void main(String[] args) {
		if (args != null && args.length >= 2) {
			// Argument 0 should contain
			// io.mosip.biometrics.util.image.type.jp2000/io.mosip.biometrics.util.image.type.wsq"
			String imageType = args[0];
			LOGGER.info("main :: imageType :: Argument {} ", imageType);
			// 0 or 1
			if (imageType.contains(ApplicationConstant.IMAGE_TYPE_JP2000)
					|| imageType.contains(ApplicationConstant.IMAGE_TYPE_WSQ)) {
				imageType = imageType.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 1 should contain
			// "mosip.mock.sbi.biometric.type.finger.folder.path/mosip.mock.sbi.biometric.type.face.folder.path/mosip.mock.sbi.biometric.type.iris.folder.path"
			String biometricFolderPath = args[1];
			LOGGER.info("main :: biometricFolderPath :: Argument {} ", biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FINGER)
					|| biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FACE)
					|| biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_IRIS)) {
				biometricFolderPath = biometricFolderPath.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 2 should contain
			// "mosip.mock.sbi.biometric.type.file.image"
			String converionFile = args[2];
			LOGGER.info("main :: converionFile :: Argument {} ", converionFile);
			if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_IMAGE)) {
				converionFile = converionFile.split("=")[1];
			}

			// Argument 3 should contain
			// "io.mosip.biometrics.image.rotation=90"
			String rotationAngle = args[3];
			LOGGER.info("main :: rotationAngle :: Argument {} ", rotationAngle);
			if (rotationAngle.contains(ApplicationConstant.MOSIP_IMAGE_ROTATION)) {
				rotationAngle = rotationAngle.split("=")[1];
			}

			if (biometricFolderPath.contains("Face") || biometricFolderPath.contains("Iris")
					|| biometricFolderPath.contains("Finger")) {
				doImageRotation1(imageType, biometricFolderPath, converionFile, rotationAngle);
			} else {
				System.exit(-1);
			}
		}
	}

	public static void doImageRotation(String inputImageType, String biometricFolderPath, String converionFile,
			String rotationAngle) {
		LOGGER.info(
				"doImageRotation :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: converionFile :: {} :: rotationAngle :: {} ",
				inputImageType, biometricFolderPath, converionFile, rotationAngle);
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + converionFile;
			Mat src = Imgcodecs.imread(fileName, Imgcodecs.IMREAD_UNCHANGED);

			// Create empty Mat object to store output image
			Mat dst = new Mat();

			// Define Rotation Angle
			double angle = Double.parseDouble(rotationAngle);

			// Image rotation according to the angle provided
			if (angle == 90 || angle == -270)
				Core.rotate(src, dst, Core.ROTATE_90_CLOCKWISE);
			else if (angle == 180 || angle == -180)
				Core.rotate(src, dst, Core.ROTATE_180);
			else if (angle == 270 || angle == -90)
				Core.rotate(src, dst, Core.ROTATE_90_COUNTERCLOCKWISE);
			else {
				// Center of the rotation is given by
				// midpoint of source image :
				// (width/2.0,height/2.0)
				Point rotPoint = new Point(src.cols() / 2.0, src.rows() / 2.0);

				// Create Rotation Matrix
				Mat rotMat = Imgproc.getRotationMatrix2D(rotPoint, angle, 1);

				// Apply Affine Transformation
				Imgproc.warpAffine(src, dst, rotMat, src.size(), Imgproc.WARP_INVERSE_MAP);

				// If counterclockwise rotation is required use
				// following: Imgproc.warpAffine(src, dst, rotMat, src.size());
			}
			// Save rotated image

			// Parameters for image compression (JPEG quality)
			// JP2000 quality set to 1000
			MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_JPEG2000_COMPRESSION_X1000, 1000);
			// Destination where rotated image is saved
			// on local directory
			Imgcodecs.imwrite(filePath + biometricFolderPath + converionFile + "rotated_image.jp2", dst, params);
		} catch (Exception ex) {
			LOGGER.error("doImageRotation :: Error ", ex);
		}
		LOGGER.info("doImageRotation :: Ended :: ");
	}

	@SuppressWarnings({ "java:S125" })
	public static void doImageRotation1(String inputImageType, String biometricFolderPath, String converionFile,
			String rotationAngle) {
		LOGGER.info(
				"doImageRotation1 :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: converionFile :: {} :: rotationAngle :: {} ",
				inputImageType, biometricFolderPath, converionFile, rotationAngle);
		ImageInputStream inputStream = null;
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + converionFile;
			File inputFile = new File(fileName);
			inputStream = ImageIO.createImageInputStream(inputFile);
			// Get the appropriate ImageReader for the image format
			ImageReader reader = ImageIO.getImageReadersByFormatName("JPEG2000").next();
			reader.setInput(inputStream);

			// Get the metadata of the image
			IIOMetadata metadata = reader.getImageMetadata(0);

			// Retrieve DPI information from the metadata
			IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
			double horizontalDpi = getDpi(root, "HorizontalPixelSize");
			double verticalDpi = getDpi(root, "VerticalPixelSize");

			LOGGER.info("Horizontal DPI: {}", horizontalDpi);
			LOGGER.info("Vertical DPI: {}", verticalDpi);

			// Read the input JP2000 image using JAI
			BufferedImage origImage = ImageIO.read(new File(fileName));
			BufferedImage destImage = null;
			if (origImage.getType() != 0) {
				destImage = new BufferedImage(origImage.getWidth(), origImage.getHeight(), origImage.getType());
				flipImage(origImage, destImage, true, true);
			} else {
				BufferedImage convertedImg = new BufferedImage(origImage.getWidth(), origImage.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
				convertedImg.getGraphics().drawImage(origImage, 0, 0, null);

				destImage = new BufferedImage(origImage.getWidth(), origImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
				flipImage(convertedImg, destImage, true, true);
			}
			/*
			 * // Rotate the image (e.g., rotate by 90 degrees clockwise) AffineTransform af
			 * = new AffineTransform(); int centerX = origImage.getWidth() / 2; int centerY
			 * = origImage.getHeight() / 2; // Rotate 180 degrees af.quadrantRotate(2,
			 * centerX, centerY);
			 * 
			 * AffineTransformOp affineTransformOp = new AffineTransformOp(af, null);
			 * LOGGER.info("Image size: " + origImage.getWidth() + " x " +
			 * origImage.getHeight()); LOGGER.info("Image type: " +
			 * origImage.getType()); BufferedImage destImage = null; if (origImage.getType()
			 * != 0) { destImage = new BufferedImage(origImage.getWidth(),
			 * origImage.getHeight(), origImage.getType());
			 * affineTransformOp.filter(origImage, destImage); } else { BufferedImage
			 * convertedImg = new BufferedImage(origImage.getWidth(), origImage.getHeight(),
			 * BufferedImage.TYPE_INT_ARGB); convertedImg.getGraphics().drawImage(origImage,
			 * 0, 0, null);
			 * 
			 * destImage = new BufferedImage(origImage.getWidth(), origImage.getHeight(),
			 * BufferedImage.TYPE_INT_ARGB); affineTransformOp.filter(convertedImg,
			 * destImage); }
			 */
			// Verify that destImage has indeed been rotated by point
			// checking a random
			// pixel.
			int origX = 90;
			int origY = 32;
			// Where we expect this pixel to have been translated to.
			int expX = destImage.getWidth() - origX;
			int expY = destImage.getHeight() - origY;

			LOGGER.info("Origin pixel: x {} : y {}", origX, origY);
			LOGGER.info("Destination pixel: x {} : y {}", expX,  expY);

			int origPixel = origImage.getRGB(origX, origY);
			int expPixel = destImage.getRGB(expX, expY);
			LOGGER.info("Pixel values: {} : {}", Integer.toHexString(origPixel), Integer.toHexString(expPixel));

			// Always prints false. Why????
			LOGGER.info("Pixels are equal: {}", (origPixel == expPixel));

			// Write the rotated image to a JP2000 file using ImageIO
			File outputImageFile = new File(filePath + biometricFolderPath + converionFile + "rotated_image.jp2");
			ImageIO.write(destImage, "JPEG2000", outputImageFile);

		} catch (Exception ex) {
			LOGGER.error("doImageRotation :: Error ", ex);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error("doImageRotation :: Error ", 1);
				}
		}
		LOGGER.info("doImageRotation :: Ended :: ");
	}

	private static double getDpi(IIOMetadataNode root, String propertyName) {
		IIOMetadataNode dimension = getChildNode(root, "Dimension");
		if (dimension != null) {
			IIOMetadataNode childNode = getChildNode(dimension, propertyName);
			if (childNode != null) {
				String dpi = childNode.getAttribute("value");
				return Double.parseDouble(dpi);
			}
		}
		return -1.0; // DPI not found or not supported
	}

	private static IIOMetadataNode getChildNode(IIOMetadataNode parent, String name) {
		for (int i = 0; i < parent.getLength(); i++) {
			if (parent.item(i).getNodeName().equalsIgnoreCase(name)) {
				return (IIOMetadataNode) parent.item(i);
			}
		}
		return null; // Child node not found
	}

	private static void flipImage(BufferedImage image, BufferedImage destImage, boolean flipHorizontal,
			boolean flipVertical) {
		// Create a transformation object
		AffineTransform transform = new AffineTransform();

		// Apply horizontal and vertical flips if requested
		if (flipHorizontal) {
			transform.scale(-1, 1);
			transform.translate(-image.getWidth(), 0);
		}
		if (flipVertical) {
			transform.scale(1, -1);
			transform.translate(0, -image.getHeight());
		}

		// Apply the transformation to the image
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		op.filter(image, destImage);
	}
}
