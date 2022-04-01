package io.mosip.biometrics.util.test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.ImageType;
import io.mosip.biometrics.util.Modality;
import io.mosip.biometrics.util.face.*;
import io.mosip.biometrics.util.finger.*;
import io.mosip.biometrics.util.iris.*;

/**
 * BioUtilConvertApplication
 *
 */
public class BioUtilConvertApplication
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BioUtilConvertApplication.class);	

    public static void main(String[] args)
    {
		if (args != null && args.length >= 2)
		{
			// Argument 0 should contain io.mosip.biometrics.util.image.type.jp2000/io.mosip.biometrics.util.image.type.wsq"
			ImageType imageType = ImageType.JPEG;
			String imageTypeFileName = "";
			String conversionImageType = args[0];
			LOGGER.info("main :: imageType :: Argument [0] " + imageType);
			if (conversionImageType.contains(ApplicationConstant.IMAGE_TYPE_JPEG))//2
			{
				conversionImageType = conversionImageType.split("=") [1];
				imageTypeFileName = "JPEG";
				imageType = ImageType.JPEG;
			}
			else if (conversionImageType.contains(ApplicationConstant.IMAGE_TYPE_PNG))//3
			{
				conversionImageType = conversionImageType.split("=") [1];
				imageTypeFileName = "PNG";
				imageType = ImageType.PNG;
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

			// Argument 2 should contain "mosip.mock.sbi.biometric.type.file.image/mosip.mock.sbi.biometric.type.file.iso"
			String converionFile = args[2];
			LOGGER.info("main :: converionFile :: Argument [2] " + converionFile);
			if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_IMAGE))
			{
				converionFile = converionFile.split("=")[1];
			}
			else if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO))
			{
				converionFile = converionFile.split("=")[1];
			}
		
			if (biometricFolderPath.contains("Face"))
			{
				doFaceConversion (imageType, imageTypeFileName, biometricFolderPath, converionFile); 
			}
			else if (biometricFolderPath.contains("Iris"))
			{
				doIrisConversion (imageType, imageTypeFileName, biometricFolderPath, converionFile); 
			}
			else if (biometricFolderPath.contains("Finger"))
			{
				doFingerConversion (imageType, imageTypeFileName, biometricFolderPath, converionFile); 
			}
		}
    }
    
    public static void doFaceConversion (ImageType inputImageType, String imageTypeFileName, String biometricFolderPath, String converionFile)
    {
		LOGGER.info("doFaceConversion :: Started :: inputImageType ::" + inputImageType + "  :: biometricFolderPath :: " + biometricFolderPath + " :: converionFile :: " + converionFile);
    	FileOutputStream tmpOutputStream = null;
		try {
    		String filePath = new File (".").getCanonicalPath ();
    		String fileName = filePath + biometricFolderPath + converionFile;
    		File initialFile = new File(fileName);
    		if (initialFile.exists())
    		{
	    		LOGGER.info("doFaceConversion :: fileName ::" + fileName);

	    		byte[] inIsoData = Files.readAllBytes (Paths.get(fileName));
    			if (inIsoData != null)
    			{
	        		byte [] outIsoData = CommonUtil.decodeURLSafeBase64 (CommonUtil.convertISOImageType(CommonUtil.encodeToURLSafeBase64 (inIsoData), Modality.Face, inputImageType));
	        		if (outIsoData != null)
	        		{
	        			// Write bytes to tmp file.
	        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + "_" + imageTypeFileName + ".iso");
        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
        		        tmpOutputStream.write(outIsoData);
	        		}
	    			else
	    			{
	    				LOGGER.error("doFaceConversion :: Could Not convert the ISO To ISO ");
	    			}
    			}
    			else
    			{
    				LOGGER.error("doFaceConversion :: Could Not convert the ISO To ISO ");
    			}
    		}
		} catch (Exception ex) {
			LOGGER.info("doFaceConversion :: Error ", ex);
		}
		finally
		{
			try
			{
		        if(tmpOutputStream != null)
	                tmpOutputStream.close();
			}
			catch (Exception ex)
			{}
		}
		LOGGER.info("doFaceConversion :: Ended :: ");
    }

    public static void doIrisConversion (ImageType inputImageType, String imageTypeFileName, String biometricFolderPath, String converionFile)
    {
		LOGGER.info("doIrisConversion :: Started :: inputImageType ::" + inputImageType + "  :: biometricFolderPath :: " + biometricFolderPath + " :: converionFile :: " + converionFile);
    	FileOutputStream tmpOutputStream = null;
		try {
    		String filePath = new File (".").getCanonicalPath ();
    		String fileName = filePath + biometricFolderPath + converionFile;
    		File initialFile = new File(fileName);
    		if (initialFile.exists())
    		{
	    		LOGGER.info("doIrisConversion :: fileName ::" + fileName);

	    		byte[] inIsoData = Files.readAllBytes (Paths.get(fileName));
    			if (inIsoData != null)
    			{
	        		byte [] outIsoData = CommonUtil.decodeURLSafeBase64 (CommonUtil.convertISOImageType(CommonUtil.encodeToURLSafeBase64 (inIsoData), Modality.Iris, inputImageType));
	        		if (outIsoData != null)
	        		{
	        			// Write bytes to tmp file.
	        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + "_" + imageTypeFileName + ".iso");
        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
        		        tmpOutputStream.write(outIsoData);
	        		}
	    			else
	    			{
	    				LOGGER.error("doIrisConversion :: Could Not convert the ISO To ISO ");
	    			}
    			}
    			else
    			{
    				LOGGER.error("doIrisConversion :: Could Not convert the ISO To ISO ");
    			}
    		}
		} catch (Exception ex) {
			LOGGER.info("doIrisConversion :: Error ", ex);
		}
		finally
		{
			try
			{
		        if(tmpOutputStream != null)
	                tmpOutputStream.close();
			}
			catch (Exception ex)
			{}
		}
		LOGGER.info("doIrisConversion :: Ended :: ");
    }

    public static void doFingerConversion (ImageType inputImageType, String imageTypeFileName, String biometricFolderPath, String converionFile)
    {
		LOGGER.info("doFingerConversion :: Started :: inputImageType ::" + inputImageType + "  :: biometricFolderPath :: " + biometricFolderPath + " :: converionFile :: " + converionFile);
    	FileOutputStream tmpOutputStream = null;
		try {
    		String filePath = new File (".").getCanonicalPath ();
    		String fileName = filePath + biometricFolderPath + converionFile;
    		File initialFile = new File(fileName);
    		if (initialFile.exists())
    		{
	    		LOGGER.info("doFingerConversion :: fileName ::" + fileName);

	    		byte[] inIsoData = Files.readAllBytes (Paths.get(fileName));
    			if (inIsoData != null)
    			{
	        		byte [] outIsoData = CommonUtil.decodeURLSafeBase64 (CommonUtil.convertISOImageType(CommonUtil.encodeToURLSafeBase64 (inIsoData), Modality.Finger, inputImageType));
	        		if (outIsoData != null)
	        		{
	        			// Write bytes to tmp file.
	        		    File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + "_" + imageTypeFileName + ".iso");
        		        tmpOutputStream = new FileOutputStream(tmpImageFile);
        		        tmpOutputStream.write(outIsoData);
	        		}
	    			else
	    			{
	    				LOGGER.error("doFingerConversion :: Could Not convert the ISO To ISO ");
	    			}
    			}
    			else
    			{
    				LOGGER.error("doFingerConversion :: Could Not convert the ISO To ISO ");
    			}
    		}
		} catch (Exception ex) {
			LOGGER.info("doFingerConversion :: Error ", ex);
		}
		finally
		{
			try
			{
		        if(tmpOutputStream != null)
	                tmpOutputStream.close();
			}
			catch (Exception ex)
			{}
		}
		LOGGER.info("doFingerConversion :: Ended :: ");
    }
}
