package io.mosip.biometrics.util.test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.NistRequestDto;
import io.mosip.biometrics.util.nist.parser.v2011.dto.BiometricInformationExchange;
/**
 * SampleNistFileReader
 *
 */
public class SampleNistFileReader{
	private long startTime = System.currentTimeMillis();
	private static Logger LOGGER = LoggerFactory.getLogger(SampleNistFileReader.class);
	
	public static void main(String[] args) {
		if (args != null && args.length >= 1) {
			// Argument 1 should contain
			// "mosip.mock.sbi.biometric.type.nist.folder.path"
			String biometricFolderPath = args[0];
			LOGGER.info("main :: biometricFolderPath :: Argument [0] " + biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_NIST)) {
				biometricFolderPath = biometricFolderPath.split("=")[1];
			}

			if (biometricFolderPath.contains("Nist")) {
				readNistFiles(biometricFolderPath);
			}
		}
	}
	
	public static void readNistFiles(String biometricFolderPath) {
		LOGGER.info("readNistFiles :: Started :: biometricFolderPath :: " + biometricFolderPath );
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileFolder = filePath + biometricFolderPath;
			File[] fileList = null;
			try {
				fileList = (new File(fileFolder)).listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".xml");
				    }
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long startTime = System.currentTimeMillis();
			for(File file:fileList)
			{
				long startTimeOneFile = System.currentTimeMillis();
				LOGGER.info(" **********************************RESULT Started**********************************");
				try {
					NistRequestDto dto = new NistRequestDto();
					dto.setInputBytes(Files.readAllBytes(file.toPath()));
					BiometricInformationExchange nistRecord = CommonUtil.nistParser(dto);
					LOGGER.info(CommonUtil.nistXml(nistRecord));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				long endTimeOneFile = System.currentTimeMillis();
				LOGGER.info(file.getAbsolutePath() + " >>> Time Taken : " + (endTimeOneFile - startTimeOneFile)  + " milliseconds");
				LOGGER.info("                                                                       ");
				LOGGER.info(" **********************************RESULT Ended**********************************");
			}
			long endTime = System.currentTimeMillis();
			LOGGER.info("Time Taken : " + (endTime - startTime)  + " milliseconds");

		} catch (Exception ex) {
			LOGGER.info("readNistFiles :: Error ", ex);
		} finally {
		}
		LOGGER.info("readNistFiles :: Ended :: ");
	}
}
