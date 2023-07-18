package io.mosip.biometrics.util.test;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BioAuthDecoderValueCreaterApplication
 *
 */
public class BioAuthDecoderValueCreaterApplication
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BioAuthDecoderValueCreaterApplication.class);	

    public static void main(String[] args)
    {
		if (args != null && args.length >= 6)
		{
			// Argument 0 should contain "mosip.mock.sbi.biometric.transaction.id"
			String transactionId = args[0];
			LOGGER.info("main :: MOSIP_TRANSACTION_ID :: Argument [0] " + transactionId);
			if (transactionId.contains(ApplicationConstant.MOSIP_TRANSACTION_ID))
			{
				transactionId = transactionId.split("=")[1];
			}
			else 
			{
				System.exit(-1);
			}

			// Argument 1 should contain "mosip.mock.sbi.biometric.time.stamp"
			String timeStamp = args[1];
			LOGGER.info("main :: MOSIP_TIME_STAMP :: Argument [1] " + timeStamp);
			if (timeStamp.contains(ApplicationConstant.MOSIP_TIME_STAMP))
			{
				timeStamp = timeStamp.split("=")[1];
			}
			else 
			{
				System.exit(-1);
			}

			// Argument 2 should contain "mosip.mock.sbi.biometric.thumb.print"
			String thumbPrint = args[2];
			LOGGER.info("main :: MOSIP_THUMB_PRINT :: Argument [2] " + thumbPrint);
			if (thumbPrint.contains(ApplicationConstant.MOSIP_THUMB_PRINT))
			{
				thumbPrint = thumbPrint.split("=")[1];
			}
			else 
			{
				System.exit(-1);
			}

			// Argument 3 should contain "mosip.mock.sbi.biometric.session.key"
			String sessionKey = args[3];
			LOGGER.info("main :: MOSIP_SESSION_KEY :: Argument [3] " + sessionKey);
			if (sessionKey.contains(ApplicationConstant.MOSIP_SESSION_KEY))
			{
				sessionKey = sessionKey.split("=")[1];
			}
			else 
			{
				System.exit(-1);
			}

			String keySplitter = ApplicationConstant.MOSIP_KEY_SPLITTER;
			LOGGER.info("main :: MOSIP_KEY_SPLITTER " + keySplitter);

			// Argument 4 should contain "mosip.mock.sbi.biometric.type.finger.folder.path/mosip.mock.sbi.biometric.type.face.folder.path/mosip.mock.sbi.biometric.type.iris.folder.path"
			String biometricFolderPath = args[4];
			LOGGER.info("main :: biometricFolderPath :: Argument [4] " + biometricFolderPath);
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

			// Argument 5 should contain "mosip.mock.sbi.biometric.type.file.iso"
			String decodeFile = args[5];
			LOGGER.info("main :: converionFile :: Argument [5] " + decodeFile);
			if (decodeFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO))
			{
				decodeFile = decodeFile.split("=")[1];
			}
			
			doBioAuthDecodeValueCreator(biometricFolderPath, decodeFile, timeStamp, transactionId, thumbPrint, sessionKey, keySplitter); 
		}
    }
    
    public static void doBioAuthDecodeValueCreator (String biometricFolderPath, String decodeFile, 
    		String timestamp, String transactionId, String thumbprint, String sessionKey, String keySplitter)
    {
		LOGGER.info("doBioAuthDecodeValueCreator :: Started :: biometricFolderPath :: " + biometricFolderPath + " :: decodeFile :: " + decodeFile);
		try {
    		String filePath = new File (".").getCanonicalPath ();
    		String fileName = filePath + biometricFolderPath + decodeFile;
    		File initialFile = new File(fileName);
    		if (initialFile.exists())
    		{
	    		LOGGER.info("doBioAuthDecodeValueCreator :: fileName ::" + fileName);

				byte[] encryptedData = Files.readAllBytes(Paths.get(fileName));
				
				byte[] xorResult = getXOR(timestamp, transactionId);
				byte[] aadBytes = getLastBytes(xorResult, 16);
				byte[] ivBytes = getLastBytes(xorResult, 12);

				byte[] thumbprintArr = hexStringToByteArray(thumbprint);
				byte[] sessionKeyArr = decodeURLSafeBase64(sessionKey);
				byte[] keySplitterArr = toUtf8ByteArray(keySplitter);
				byte[] dataArr = decodeURLSafeBase64(encryptedData);

				String encodedData = encodeToURLSafeBase64(concatByteArrays(thumbprintArr, sessionKeyArr, keySplitterArr, dataArr));
				
	    		LOGGER.info("doBioAuthDecodeValueCreator :: Data :: Starts");
	    		LOGGER.info("Salt ::\t" + encodeToURLSafeBase64(ivBytes));
	    		LOGGER.info("aad ::\t" + encodeToURLSafeBase64(aadBytes));
	    		LOGGER.info("EncodedData ::\t" + encodedData);

	    		LOGGER.info("doBioAuthDecodeValueCreator :: Data :: Ends");
	    	}	    		 
		} catch (Exception ex) {
			LOGGER.info("doBioAuthDecodeValueCreator :: Error ", ex);
		}
		LOGGER.info("doBioAuthDecodeValueCreator :: Ended :: ");
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
    
    public static byte[] decodeURLSafeBase64(byte[] data) {
		if (isNullEmpty(data)) {
			return null;
		}
		return Base64.getUrlDecoder().decode(data);
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
    
    public static byte[] getLastBytes(byte[] xorBytes, int lastBytesNum) {
		assert (xorBytes.length >= lastBytesNum);
		return Arrays.copyOfRange(xorBytes, xorBytes.length - lastBytesNum, xorBytes.length);
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
}