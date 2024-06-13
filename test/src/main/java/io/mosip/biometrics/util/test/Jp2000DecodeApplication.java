package io.mosip.biometrics.util.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jp2000DecodeApplication
 *
 */
public class Jp2000DecodeApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(Jp2000DecodeApplication.class);

	public static void main(String[] args) {
		if (args != null && args.length >= 2) {
			// Argument 0 should contain
			// io.mosip.biometrics.util.image.type.jp2000/io.mosip.biometrics.util.image.type.wsq"
			String imageType = args[0];
			LOGGER.info("main :: imageType :: Argument {} ", imageType);
			if (imageType.contains(ApplicationConstant.IMAGE_TYPE_JP2000))// 0
			{
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
			// "mosip.mock.sbi.biometric.type.file.image/mosip.mock.sbi.biometric.type.file.iso"
			String decodeFile = args[2];
			LOGGER.info("main :: converionFile :: Argument {} ", decodeFile);
			if (decodeFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_IMAGE)) {
				decodeFile = decodeFile.split("=")[1];
			} else {
				System.exit(-1);
			}

			// doJP2000Decode (imageType, biometricFolderPath, decodeFile);
			doJP2000DecodeByteWise(imageType, biometricFolderPath, decodeFile);
		}
	}

	public static void doJP2000Decode(String inputImageType, String biometricFolderPath, String decodeFile) {
		LOGGER.info(
				"doJP2000Decode :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: decodeFile :: {}",
				inputImageType, biometricFolderPath, decodeFile);
		InputStream inputStream = null;
		ImageInputStream imageInputStream = null;
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + decodeFile;
			File initialFile = new File(fileName);
			if (initialFile.exists()) {
				LOGGER.info("doJP2000Decode :: fileName :: {}", fileName);

				byte[] imageData = Files.readAllBytes(Paths.get(fileName));
				inputStream = new ByteArrayInputStream(imageData);
				imageInputStream = ImageIO.createImageInputStream(inputStream);
				java.util.Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
				if (!iter.hasNext()) {
					throw new IOException("Unsupported image format!");
				}

				ImageReader reader = iter.next();
				int numImages = reader.getNumImages(true);
				if (numImages == 0) {
					throw new Exception("No of images found is zero");
				} else if (numImages == 1) {
					try {
						// attach source to the reader
						reader.setInput(imageInputStream, true);
						LOGGER.info("-------------------ImageReader :: Data --------------------");
						LOGGER.info("Width  \t = {}", reader.getWidth(0));
						LOGGER.info("Height \t = {}", reader.getHeight(0));
						LOGGER.info("NumImages \t = {}", numImages);
						LOGGER.info("AspectRatio \t = {}", reader.getAspectRatio(0));
						LOGGER.info("getColorMode \t = {}", reader.getRawImageType(0).getColorModel());
						LOGGER.info("getSampleModel \t = {}", reader.getRawImageType(0).getSampleModel());

						IIOMetadata metadata = reader.getImageMetadata(0);
						if (metadata != null && metadata.isStandardMetadataFormatSupported()) {
							IIOMetadataNode metatop = (IIOMetadataNode) metadata
									.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
							final String[] names = metadata.getMetadataFormatNames();
							final int length = names.length;
							for (int i = 0; i < length; i++) {
								showMetadata(metadata.getAsTree(names[i]));
							}
						}
					} catch (Exception e) {
						LOGGER.info("doJP2000Decode :: Error ", e);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doJP2000Decode :: Error ", ex);
		} finally {
			try {
				if (imageInputStream != null)
					imageInputStream.close();
				if (inputStream != null)
					inputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doJP2000Decode :: Error ", ex);
			}
		}
		LOGGER.info("doJP2000Decode :: Ended :: ");
	}

	public static void showMetadata(org.w3c.dom.Node root) {
		showMetadata(root, 0);
	}

	public static void indent(int level) {
		for (int i = 0; i < level; i++) {
			LOGGER.info("    ");
		}
	}

	public static void showMetadata(org.w3c.dom.Node node, int level) {
		// print open tag of element
		indent(level);
		LOGGER.info("<{}", node.getNodeName());
		final org.w3c.dom.NamedNodeMap map = node.getAttributes();
		if (map != null) {
			// print attribute values
			final int length = map.getLength();
			for (int i = 0; i < length; i++) {
				final org.w3c.dom.Node attr = map.item(i);
				LOGGER.info("{}=\"{}\"", attr.getNodeName(), attr.getNodeValue());
			}
		}

		org.w3c.dom.Node child = node.getFirstChild();
		if (child == null) {
			// no children, so close element and return
			LOGGER.info("/>");
			return;
		}

		// children, so close current tag
		LOGGER.info(">");
		while (child != null) {
			// print children recursively
			showMetadata(child, level + 1);
			child = child.getNextSibling();
		}

		// print close tag of element
		indent(level);
		LOGGER.info("</{}>", node.getNodeName());
	}

	/* helper for structure initialization */
	public MarkerInfo initMarker(String mark, String name, String spec) {
		MarkerInfo marker = new MarkerInfo();

		marker.setMark(mark);
		marker.setName(name);
		marker.setSpec(spec);

		return marker;
	}

	public MarkerInfo[] initMarker() {
		int i;
		String name = "";

		/* allocate marker info array */
		MarkerInfo[] marker = new MarkerInfo[256];

		/* set default values for all marker code values */
		for (i = 0; i < 256; i++) {
			name = String.format("reserved %02x", i);
			marker[i] = new MarkerInfo();
			marker[i].setMark("---");
			marker[i].setName(name);
			marker[i].setSpec("JPEG");
		}

		/* set defined marks */
		marker[0x00] = initMarker("nul", "reserved 00", "JPEG");
		marker[0x01] = initMarker("TEM", "reserved 01", "JPEG");

		/* JPEG 1994 - defined in ITU T.81 | ISO IEC 10918-1 */
		/* frame types */
		marker[0xc0] = initMarker("SOF0", "start of frame (baseline jpeg)", "JPEG 1994");
		marker[0xc1] = initMarker("SOF1", "start of frame (extended sequential, huffman)", "JPEG 1994");
		marker[0xc2] = initMarker("SOF2", "start of frame (progressive, huffman)", "JPEG 1994");
		marker[0xc3] = initMarker("SOF3", "start of frame (lossless, huffman)", "JPEG 1994");

		marker[0xc5] = initMarker("SOF5", "start of frame (differential sequential, huffman)", "JPEG 1994");
		marker[0xc6] = initMarker("SOF6", "start of frame (differential progressive, huffman)", "JPEG 1994");
		marker[0xc7] = initMarker("SOF7", "start of frame (differential lossless, huffman)", "JPEG 1994");
		marker[0xc8] = initMarker("JPG", "reserved for JPEG extension", "JPEG 1994");
		marker[0xc9] = initMarker("SOF9", "start of frame (extended sequential, arithmetic)", "JPEG 1994");
		marker[0xca] = initMarker("SOF10", "start of frame (progressive, arithmetic)", "JPEG 1994");
		marker[0xcb] = initMarker("SOF11", "start of frame (lossless, arithmetic)", "JPEG 1994");

		marker[0xcd] = initMarker("SOF13", "start of frame (differential sequential, arithmetic)", "JPEG 1994");
		marker[0xce] = initMarker("SOF14", "start of frame (differential progressive, arithmetic)", "JPEG 1994");
		marker[0xcf] = initMarker("SOF15", "start of frame (differential lossless, arithmetic)", "JPEG 1994");

		marker[0xc4] = initMarker("DHT", "define huffman tables", "JPEG 1994");
		marker[0xcc] = initMarker("DAC", "define arithmetic coding conditioning", "JPEG 1994");

		/* restart markers */
		marker[0xd0] = initMarker("RST0", "restart marker 0", "JPEG 1994");
		marker[0xd1] = initMarker("RST1", "restart marker 1", "JPEG 1994");
		marker[0xd2] = initMarker("RST2", "restart marker 2", "JPEG 1994");
		marker[0xd3] = initMarker("RST3", "restart marker 3", "JPEG 1994");
		marker[0xd4] = initMarker("RST4", "restart marker 4", "JPEG 1994");
		marker[0xd5] = initMarker("RST5", "restart marker 5", "JPEG 1994");
		marker[0xd6] = initMarker("RST6", "restart marker 6", "JPEG 1994");
		marker[0xd7] = initMarker("RST7", "restart marker 7", "JPEG 1994");
		/* delimeters */
		marker[0xd8] = initMarker("SOI", "start of image", "JPEG 1994");
		marker[0xd9] = initMarker("EOI", "end of image", "JPEG 1994");
		marker[0xda] = initMarker("SOS", "start of scan", "JPEG 1994");
		marker[0xdb] = initMarker("DQT", "define quantization tables", "JPEG 1994");
		marker[0xdc] = initMarker("DNL", "define number of lines", "JPEG 1994");
		marker[0xdd] = initMarker("DRI", "define restart interval", "JPEG 1994");
		marker[0xde] = initMarker("DHP", "define hierarchical progression", "JPEG 1994");
		marker[0xdf] = initMarker("EXP", "expand reference components", "JPEG 1994");

		/* JPEG 1997 extensions ITU T.84 | ISO IEC 10918-3 */
		/* application data sections */
		marker[0xe0] = initMarker("APP0", "application data section  0", "JPEG 1997");
		marker[0xe1] = initMarker("APP1", "application data section  1", "JPEG 1997");
		marker[0xe2] = initMarker("APP2", "application data section  2", "JPEG 1997");
		marker[0xe3] = initMarker("APP3", "application data section  3", "JPEG 1997");
		marker[0xe4] = initMarker("APP4", "application data section  4", "JPEG 1997");
		marker[0xe5] = initMarker("APP5", "application data section  5", "JPEG 1997");
		marker[0xe6] = initMarker("APP6", "application data section  6", "JPEG 1997");
		marker[0xe7] = initMarker("APP7", "application data section  7", "JPEG 1997");
		marker[0xe8] = initMarker("APP8", "application data section  8", "JPEG 1997");
		marker[0xe9] = initMarker("APP9", "application data section  9", "JPEG 1997");
		marker[0xea] = initMarker("APP10", "application data section 10", "JPEG 1997");
		marker[0xeb] = initMarker("APP11", "application data section 11", "JPEG 1997");
		marker[0xec] = initMarker("APP12", "application data section 12", "JPEG 1997");
		marker[0xed] = initMarker("APP13", "application data section 13", "JPEG 1997");
		marker[0xee] = initMarker("APP14", "application data section 14", "JPEG 1997");
		marker[0xef] = initMarker("APP15", "application data section 15", "JPEG 1997");
		/* extention data sections */
		marker[0xf0] = initMarker("JPG0", "extension data 00", "JPEG 1997");
		marker[0xf1] = initMarker("JPG1", "extension data 01", "JPEG 1997");
		marker[0xf2] = initMarker("JPG2", "extension data 02", "JPEG 1997");
		marker[0xf3] = initMarker("JPG3", "extension data 03", "JPEG 1997");
		marker[0xf4] = initMarker("JPG4", "extension data 04", "JPEG 1997");
		marker[0xf5] = initMarker("JPG5", "extension data 05", "JPEG 1997");
		marker[0xf6] = initMarker("JPG6", "extension data 06", "JPEG 1997");
		marker[0xf7] = initMarker("SOF48", "start of frame (JPEG-LS)", "JPEG-LS");
		marker[0xf8] = initMarker("LSE", "extension parameters (JPEG-LS)", "JPEG-LS");
		marker[0xf9] = initMarker("JPG9", "extension data 09", "JPEG 1997");
		marker[0xfa] = initMarker("JPG10", "extension data 10", "JPEG 1997");
		marker[0xfb] = initMarker("JPG11", "extension data 11", "JPEG 1997");
		marker[0xfc] = initMarker("JPG12", "extension data 12", "JPEG 1997");
		marker[0xfd] = initMarker("JPG13", "extension data 13", "JPEG 1997");
		marker[0xfe] = initMarker("JCOM", "extension data (comment)", "JPEG 1997");

		/* JPEG 2000 - defined in IEC 15444-1 "JPEG 2000 Core (part 1)" */
		/* delimiters */
		marker[0x4f] = initMarker("SOC", "start of codestream", "JPEG 2000");
		marker[0x90] = initMarker("SOT", "start of tile", "JPEG 2000");
		marker[0xd9] = initMarker("EOC", "end of codestream", "JPEG 2000");
		/* fixed information segment */
		marker[0x51] = initMarker("SIZ", "image and tile size", "JPEG 2000");
		/* functional segments */
		marker[0x52] = initMarker("COD", "coding style default", "JPEG 2000");
		marker[0x53] = initMarker("COC", "coding style component", "JPEG 2000");
		marker[0x5e] = initMarker("RGN", "region of interest", "JPEG 2000");
		marker[0x5c] = initMarker("QCD", "quantization default", "JPEG 2000");
		marker[0x5d] = initMarker("QCC", "quantization component", "JPEG 2000");
		marker[0x5f] = initMarker("POC", "progression order change", "JPEG 2000");
		/* pointer segments */
		marker[0x55] = initMarker("TLM", "tile-part lengths", "JPEG 2000");
		marker[0x57] = initMarker("PLM", "packet length (main header)", "JPEG 2000");
		marker[0x58] = initMarker("PLT", "packet length (tile-part header)", "JPEG 2000");
		marker[0x60] = initMarker("PPM", "packed packet headers (main header)", "JPEG 2000");
		marker[0x61] = initMarker("PPT", "packed packet headers (tile-part header)", "JPEG 2000");
		/* bitstream internal markers and segments */
		marker[0x91] = initMarker("SOP", "start of packet", "JPEG 2000");
		marker[0x92] = initMarker("EPH", "end of packet header", "JPEG 2000");
		marker[0x93] = initMarker("SOD", "Start of data", "JPEG 2000");
		/* informational segments */
		marker[0x63] = initMarker("CRG", "component registration", "JPEG 2000");
		marker[0x64] = initMarker("CME", "comment", "JPEG 2000");

		return marker;
	}

	public static void doJP2000DecodeByteWise(String inputImageType, String biometricFolderPath, String decodeFile) {
		// https://svn.xiph.org/experimental/giles/jpegdump.c
		LOGGER.info(
				"doJP2000Decode :: Started :: inputImageType :: {}  :: biometricFolderPath :: {} :: decodeFile :: {}",
				inputImageType, biometricFolderPath, decodeFile);
		InputStream inputStream = null;
		ImageInputStream imageInputStream = null;
		FileOutputStream oFile = null;
		try {
			String filePath = new File(".").getCanonicalPath();
			String fileName = filePath + biometricFolderPath + decodeFile;
			String outfileName = filePath + biometricFolderPath + decodeFile + ".txt";
			File outFile = new File(outfileName);
			outFile.createNewFile(); // if file already exists will do nothing
			oFile = new FileOutputStream(outFile, false);
			File initialFile = new File(fileName);
			if (initialFile.exists()) {
				LOGGER.info("doJP2000Decode :: fileName ::{}", fileName);
				Jp2000DecodeApplication app = new Jp2000DecodeApplication();
				MarkerInfo[] markers = app.initMarker();
				byte[] imageData = Files.readAllBytes(Paths.get(fileName));
				inputStream = new ByteArrayInputStream(imageData);
				imageInputStream = ImageIO.createImageInputStream(inputStream);

				/*
				 * List<JPEGSegment> segments = JPEGSegmentUtil.readSegments(imageInputStream,
				 * JPEGSegmentUtil.ALL_SEGMENTS);
				 * 
				 * for (JPEGSegment segment : segments) { LOGGER.info("segment: " +
				 * segment.toString()); }
				 */

				int c = 0;
				int offset = 0;
				MarkerRecord[] markerRecords = new MarkerRecord[256];
				while ((c = inputStream.read()) != -1) {
					// Convert byte to character
					char ch = (char) (c & 0xFF);
					// Print the character
					// LOGGER.info("Char : " + ch + " Byte : " + (c & 0xFF) + " Offset : " +
					// offset);
					oFile.write(
							(("Char : " + ch + "  Byte : " + (c & 0xFF) + "  Offset : " + offset + "\n")).getBytes());
					if (c == 0xFF) {
						offset++;
						int code = inputStream.read();
						oFile.write((("code : " + Integer.toHexString(code) + "  Byte : " + (code & 0xFF)
								+ "  Offset : " + offset + "\n")).getBytes());
						if (code > 0) {
							oFile.write(MessageFormat.format("marker 0xff {0} {1} at offset {2} \t({3}) \n", code,
									markers[code].getMark(), offset, markers[code].getName()).getBytes());
						}
						offset++;
					} else
						offset++;
				}

			}
		} catch (Exception ex) {
			LOGGER.info("doJP2000Decode :: Error ", ex);
		} finally {
			try {
				if (imageInputStream != null)
					imageInputStream.close();
				if (inputStream != null)
					inputStream.close();
				if (oFile != null)
					oFile.close();
			} catch (Exception ex) {
				LOGGER.info("doJP2000Decode :: Error ", ex);
			}
		}
		LOGGER.info("doJP2000Decode :: Ended :: ");
	}
}

/*
 * JPEG marker codes - these are the second bytes; a marker consistes of 0xff
 * followed by the type codes given below.
 */
enum Marker {
	/* 0xFF00, 0xFF01, 0xFFFE, 0xFFC0-0xFFDF(?) ITU T.81/IEC 10918-1 */
	/* 0xFFF0 - 0xFFF6 ITU T.84/IEC 10918-3 JPEG extensions */
	/* 0xFFF7 - 0xFFF8 ITU T.87/IEC 14495-1 JPEG LS */
	/* 0xFF4F - 0xFF6f, 0xFF90 - 0xFF93 JPEG 2000 IEC 15444-1 */
	/* 0xFF30 - 0xFF3F reserved JP2 (markers only--no marker segments */

	/* JPEG 1994 - defined in ITU T.81 | ISO IEC 10918-1 */
	SOF0(0xc0), /* start of frame - baseline jpeg */
	SOF1(0xc1), /* extended sequential, huffman */
	SOF2(0xc2), /* progressive, huffman */
	SOF3(0xc3), /* lossless, huffman */

	SOF5(0xc5), /* differential sequential, huffman */
	SOF6(0xc6), /* differential progressive, huffman */
	SOF7(0xc7), /* differential lossless, huffman */
	JPG(0xc8), /* reserved for JPEG extension */
	SOF9(0xc9), /* extended sequential, arithmetic */
	SOF10(0xca), /* progressive, arithmetic */
	SOF11(0xcb), /* lossless, arithmetic */

	SOF13(0xcd), /* differential sequential, arithmetic */
	SOF14(0xce), /* differential progressive, arithmetic */
	SOF15(0xcf), /* differential lossless, arithmetic */

	DHT(0xc4), /* define huffman tables */

	DAC(0xcc), /* define arithmetic-coding conditioning */

	/* restart markers */
	RST0(0xd0), RST1(0xd1), RST2(0xd2), RST3(0xd3), RST4(0xd4), RST5(0xd5), RST6(0xd6), RST7(0xd7),
	/* delimeters */
	SOI(0xd8), /* start of image */
	EOI(0xd9), /* end of image */
	SOS(0xda), /* start of scan */
	DQT(0xdb), /* define quantization tables */
	DNL(0xdc), /* define number of lines */
	DRI(0xdd), /* define restart interval */
	DHP(0xde), /* define hierarchical progression */
	EXP(0xdf), /* expand reference components */

	/* JPEG 1997 extensions ITU T.84 | ISO IEC 10918-3 */
	/* application data sections */
	APP0(0xe0), APP1(0xe1), APP2(0xe2), APP3(0xe3), APP4(0xe4), APP5(0xe5), APP6(0xe6), APP7(0xe7), APP8(0xe8),
	APP9(0xe9), APP10(0xea), APP11(0xeb), APP12(0xec), APP13(0xed), APP14(0xee), APP15(0xef),
	/* extention data sections */
	JPG0(0xf0), JPG1(0xf1), JPG2(0xf2), JPG3(0xf3), JPG4(0xf4), JPG5(0xf5), JPG6(0xf6), SOF48(0xf7), /* JPEG-LS */
	LSE(0xf8), /* JPEG-LS extension parameters */
	JPG9(0xf9), JPG10(0xfa), JPG11(0xfb), JPG12(0xfc), JPG13(0xfd), JCOM(0xfe), /* comment */

	TEM(0x01), /* temporary private use for arithmetic coding */

	/* 0x02 -> 0xbf reserved in JPEG 94/97 */

	/* JPEG 2000 - defined in IEC 15444-1 "JPEG 2000 Core (part 1)" */
	/* delimiters */
	SOC(0x4f), /* start of codestream */
	SOT(0x90), /* start of tile */
	SOD(0x93), /* start of data */
	EOC(0xd9), /* end of codestream */
	/* fixed information segment */
	SIZ(0x51), /* image and tile size */
	/* functional segments */
	COD(0x52), /* coding style default */
	COC(0x53), /* coding style component */
	RGN(0x5e), /* region of interest */
	QCD(0x5c), /* quantization default */
	QCC(0x5d), /* quantization component */
	POC(0x5f), /* progression order change */
	/* pointer segments */
	TLM(0x55), /* tile-part lengths */
	PLM(0x57), /* packet length (main header) */
	PLT(0x58), /* packet length (tile-part header) */
	PPM(0x60), /* packed packet headers (main header) */
	PPT(0x61), /* packet packet headers (tile-part header) */
	/* bitstream internal markers and segments */
	SOP(0x91), /* start of packet */
	EPH(0x92), /* end of packet header */
	/* informational segments */
	CRG(0x63), /* component registration */
	COM(0x64); /* comment */

	private final int value;

	Marker(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value & 0xFF) + ")";
	}
}

class MarkerInfo {
	private String mark; /* marker mnemonic string */
	private String name; /* longer name */
	private String spec; /* defining specification */

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@Override
	public String toString() {
		return "MarkerInfo [mark=" + mark + ", name=" + name + ", spec=" + spec + "]";
	}

}

class SOCMarker extends MarkerRecord {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCMarker.class);

	public SOCMarker(MarkerInfo[] markers, int marker, String markerCode, int offset, InputStream inputStream) {
		super();
		setMarker(marker);
		setMarkerCode(markerCode);
		setOffset(offset + 1);
		int c;
		try {
			do {
				c = inputStream.read();
				char ch = (char) (c & 0xFF);
				setOffset(getOffset());
				LOGGER.info("SOCMarker>>Char : {}  Byte : {}  Offset : {}", ch, (c & 0xFF), getOffset());
				if (ch == 0xFF) {
					int code = inputStream.read();
					offset++;
					if (code > 0) {
						MarkerInfo info = markers[code];
						LOGGER.info("SOCMarker>>MarkerInfo : {}", info);
						if (info.getMark().equals("SIZ")) {
							c = ((inputStream.read() & 0xff) << 8) | (inputStream.read() & 0xff);
							LOGGER.info("c : {}", c);
							setOffset(getOffset() + 2);
							setDataLength(c - 2 - 1);
							setData(new byte[getDataLength()]);
							setOffset(inputStream.read(getData(), getOffset(), getDataLength()));
						}
					}
				}
			} while (c != 0xFF);
			setDataLength(dataLength);
			setData(new byte[dataLength]);
		} catch (IOException e) {
			LOGGER.error("SOCMarker>>MarkerInfo", e);
		}
	}

	@Override
	public String toString() {
		return "SOCMarker [marker=" + marker + ", markerCode=" + markerCode + ", offset=" + offset + ", dataLength="
				+ dataLength + ", data=" + Arrays.toString(data) + "]";
	}
}

class MarkerRecord {
	protected int marker; /* Marker Value */
	protected String markerCode; /* Marker Code */
	protected int offset; /* offset value */
	protected int dataLength; /* data length */
	protected byte[] data; /* Data */

	public MarkerRecord() {
	}

	public int getMarker() {
		return marker;
	}

	public void setMarker(int marker) {
		this.marker = marker;
	}

	public String getMarkerCode() {
		return markerCode;
	}

	public void setMarkerCode(String markerCode) {
		this.markerCode = markerCode;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MarkerRecord [marker=" + marker + ", markerCode=" + markerCode + ", offset=" + offset + ", dataLength="
				+ dataLength + ", data=" + Arrays.toString(data) + "]";
	}
}

class JPEGSegment implements Serializable {
	final int marker;
	final byte[] data;
	private final int length;

	private transient String id;

	JPEGSegment(int marker, byte[] data, int length) {
		this.marker = marker;
		this.data = data;
		this.length = length;
	}

	public int segmentLength() {
		// This is the length field as read from the stream
		return length;
	}

	public InputStream segmentData() {
		return data != null ? new ByteArrayInputStream(data) : null;
	}

	public int marker() {
		return marker;
	}

	public String identifier() {
		if (id == null) {
			if (isAppSegmentMarker(marker)) {
				// Only for APPn markers
				id = JPEGSegmentUtil.asNullTerminatedAsciiString(data, 0);
			}
		}

		return id;
	}

	static boolean isAppSegmentMarker(final int marker) {
		return marker >= 0xFFE0 && marker <= 0xFFEF;
	}

	// TO DO: Consider returning an ImageInputStream and use
	// ByteArrayImageInputStream directly, for less wrapping and better performance
	// TO DO: BUT: Must find a way to skip padding in/after segment identifier (eg:
	// Exif has null-term + null-pad, ICC_PROFILE has only null-term). Is data
	// always word-aligned?
	public InputStream data() {
		return data != null ? new ByteArrayInputStream(data, offset(), length()) : null;
	}

	public int length() {
		return data != null ? data.length - offset() : 0;
	}

	int offset() {
		String identifier = identifier();

		return identifier == null ? 0 : identifier.length() + 1;
	}

	@Override
	public String toString() {
		String identifier = identifier();

		if (identifier != null) {
			return String.format("JPEGSegment[%04x/%s size: %d]", marker, identifier, segmentLength());
		}

		return String.format("JPEGSegment[%04x size: %d]", marker, segmentLength());
	}

	@Override
	public int hashCode() {
		String identifier = identifier();

		return marker() << 16 | (identifier != null ? identifier.hashCode() : 0) & 0xFFFF;
	}

	@Override
	public boolean equals(final Object other) {
		return other instanceof JPEGSegment && ((JPEGSegment) other).marker == marker
				&& Arrays.equals(((JPEGSegment) other).data, data);
	}
}

class JPEGSegmentUtil {
	public static final List<String> ALL_IDS = Collections.unmodifiableList(new AllIdsList());
	public static final Map<Integer, List<String>> ALL_SEGMENTS = Collections.unmodifiableMap(new AllSegmentsMap());
	public static final Map<Integer, List<String>> APP_SEGMENTS = Collections.unmodifiableMap(new AllAppSegmentsMap());

	private JPEGSegmentUtil() {
	}

	/**
	 * Reads the requested JPEG segments from the stream. The stream position must
	 * be directly before the SOI marker, and only segments for the current image is
	 * read.
	 *
	 * @param stream     the stream to read from.
	 * @param marker     the segment marker to read
	 * @param identifier the identifier to read, or {@code null} to match any
	 *                   segment
	 * @return a list of segments with the given app marker and optional identifier.
	 *         If no segments are found, an empty list is returned.
	 * @throws IIOException if a JPEG format exception occurs during reading
	 * @throws IOException  if an I/O exception occurs during reading
	 */
	public static List<JPEGSegment> readSegments(final ImageInputStream stream, final int marker,
			final String identifier) throws IOException {
		return readSegments(stream,
				Collections.singletonMap(marker, identifier != null ? Collections.singletonList(identifier) : ALL_IDS));
	}

	/**
	 * Reads the requested JPEG segments from the stream. The stream position must
	 * be directly before the SOI marker, and only segments for the current image is
	 * read.
	 *
	 * @param stream             the stream to read from.
	 * @param segmentIdentifiers the segment identifiers
	 * @return a list of segments with the given app markers and optional
	 *         identifiers. If no segments are found, an empty list is returned.
	 * @throws IIOException if a JPEG format exception occurs during reading
	 * @throws IOException  if an I/O exception occurs during reading
	 *
	 * @see #ALL_SEGMENTS
	 * @see #APP_SEGMENTS
	 * @see #ALL_IDS
	 */
	public static List<JPEGSegment> readSegments(final ImageInputStream stream,
			final Map<Integer, List<String>> segmentIdentifiers) throws IOException {
		readSOI(Validate.notNull(stream, "stream"));

		List<JPEGSegment> segments = Collections.emptyList();

		JPEGSegment segment;
		try {
			do {
				segment = readSegment(stream, segmentIdentifiers);

				if (isRequested(segment, segmentIdentifiers)) {
					if (segments.isEmpty()) {
						segments = new ArrayList<>();
					}

					segments.add(segment);
				}
			} while (!isImageDone(segment));
		} catch (EOFException ignore) {
			// Just end here, in case of malformed stream
		}

		// TO DO: Should probably skip until EOI, so that multiple invocations succeeds
		// for multiple image streams.

		return segments;
	}

	private static boolean isRequested(JPEGSegment segment, Map<Integer, List<String>> segmentIdentifiers) {
		return (segmentIdentifiers.containsKey(segment.marker)
				&& (segment.identifier() == null && segmentIdentifiers.get(segment.marker) == null
						|| containsSafe(segment, segmentIdentifiers)));
	}

	private static boolean containsSafe(JPEGSegment segment, Map<Integer, List<String>> segmentIdentifiers) {
		List<String> identifiers = segmentIdentifiers.get(segment.marker);
		return identifiers != null && identifiers.contains(segment.identifier());
	}

	private static boolean isImageDone(final JPEGSegment segment) {
		// We're done with this image if we encounter a SOS, EOI (or a new SOI, but that
		// should never happen)
		return segment.marker == JPEG.SOS || segment.marker == JPEG.EOI || segment.marker == JPEG.SOI;
	}

	static String asNullTerminatedAsciiString(final byte[] data, final int offset) {
		for (int i = 0; i < data.length - offset; i++) {
			if (data[offset + i] == 0 || i > 255) {
				return asAsciiString(data, offset, offset + i);
			}
		}

		return null;
	}

	static String asAsciiString(final byte[] data, final int offset, final int length) {
		return new String(data, offset, length, StandardCharsets.US_ASCII);
	}

	static void readSOI(final ImageInputStream stream) throws IOException {
		if (stream.readUnsignedShort() != JPEG.SOI) {
			throw new IIOException("Not a JPEG stream");
		}
	}

	static JPEGSegment readSegment(final ImageInputStream stream, final Map<Integer, List<String>> segmentIdentifiers)
			throws IOException {
//        int trash = 0;
		int marker = stream.readUnsignedByte();

		while (!isKnownJPEGMarker(marker)) {
			// Skip trash padding before the marker
			while (marker != 0xff) {
				marker = stream.readUnsignedByte();
//            trash++;
			}

//        if (trash != 0) {
			// TO DO: Issue warning?
//            System.err.println("trash: " + trash);
//        }

			marker = 0xff00 | stream.readUnsignedByte();

			// Skip over 0xff padding between markers
			while (marker == 0xffff) {
				marker = 0xff00 | stream.readUnsignedByte();
			}
		}

		if ((marker >> 8 & 0xff) != 0xff) {
			throw new IIOException(String.format("Bad marker: %04x", marker));
		}

		int length = stream.readUnsignedShort(); // Length including length field itself

		byte[] data;

		if (segmentIdentifiers.containsKey(marker)) {
			data = new byte[Math.max(0, length - 2)];
			stream.readFully(data);
		} else {
			if (JPEGSegment.isAppSegmentMarker(marker)) {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(32);
				int read;

				// NOTE: Read until null-termination (0) or EOF
				while ((read = stream.read()) > 0) {
					buffer.write(read);
				}

				data = buffer.toByteArray();

				stream.skipBytes(length - 3 - data.length);
			} else {
				data = null;
				stream.skipBytes(length - 2);
			}
		}

		return new JPEGSegment(marker, data, length);
	}

	@SuppressWarnings({ "java:S6208" })
	public static boolean isKnownJPEGMarker(final int marker) {
		switch (marker) {
		case JPEG.SOI:
		case JPEG.EOI:
		case JPEG.DHT:
		case JPEG.SOS:
		case JPEG.DQT:
		case JPEG.COM:
		case JPEG.SOF0:
		case JPEG.SOF1:
		case JPEG.SOF2:
		case JPEG.SOF3:
		case JPEG.SOF5:
		case JPEG.SOF6:
		case JPEG.SOF7:
		case JPEG.SOF9:
		case JPEG.SOF10:
		case JPEG.SOF11:
		case JPEG.SOF13:
		case JPEG.SOF14:
		case JPEG.SOF15:
		case JPEG.SOF55:
		case JPEG.APP0:
		case JPEG.APP1:
		case JPEG.APP2:
		case JPEG.APP3:
		case JPEG.APP4:
		case JPEG.APP5:
		case JPEG.APP6:
		case JPEG.APP7:
		case JPEG.APP8:
		case JPEG.APP9:
		case JPEG.APP10:
		case JPEG.APP11:
		case JPEG.APP12:
		case JPEG.APP13:
		case JPEG.APP14:
		case JPEG.APP15:
		case JPEG.DRI:
		case JPEG.TEM:
		case JPEG.DAC:
		case JPEG.DHP:
		case JPEG.DNL:
		case JPEG.EXP:
		case JPEG.LSE:
			return true;
		default:
			return false;
		}
	}

	private static class AllIdsList extends ArrayList<String> {
		@Override
		public String toString() {
			return "[All ids]";
		}

		@Override
		public boolean contains(final Object o) {
			return true;
		}
	}

	private static class AllSegmentsMap extends HashMap<Integer, List<String>> {
		@Override
		public String toString() {
			return "{All segments}";
		}

		@Override
		public List<String> get(final Object key) {
			return key instanceof Integer && JPEGSegment.isAppSegmentMarker((Integer) key) ? ALL_IDS : null;
		}

		@Override
		public boolean containsKey(final Object key) {
			return true;
		}
	}

	private static class AllAppSegmentsMap extends HashMap<Integer, List<String>> {
		@Override
		public String toString() {
			return "{All APPn segments}";
		}

		@Override
		public List<String> get(final Object key) {
			return containsKey(key) ? ALL_IDS : null;

		}

		@Override
		public boolean containsKey(Object key) {
			return key instanceof Integer && JPEGSegment.isAppSegmentMarker((Integer) key);
		}
	}
}

interface JPEG {
	/** Start of Image segment marker (SOI). */
	int SOI = 0xFFD8;
	/** End of Image segment marker (EOI). */
	int EOI = 0xFFD9;

	/** Start of Scan segment marker (SOS). */
	int SOS = 0xFFDA;

	/** Define Quantization Tables segment marker (DQT). */
	int DQT = 0xFFDB;
	/** Define Huffman Tables segment marker (DHT). */
	int DHT = 0xFFC4;

	/** Comment (COM) */
	int COM = 0xFFFE;

	/** Define Number of Lines (DNL). */
	int DNL = 0xFFDC;
	/** Define Restart Interval (DRI). */
	int DRI = 0xFFDD;
	/** Define Hierarchical Progression (DHP). */
	int DHP = 0xFFDE;
	/** Expand reference components (EXP). */
	int EXP = 0xFFDF;
	/** Temporary use in arithmetic coding (TEM). */
	int TEM = 0xFF01;
	/** Define Define Arithmetic Coding conditioning (DAC). */
	int DAC = 0xFFCC;

	// App segment markers (APPn).
	int APP0 = 0xFFE0;
	int APP1 = 0xFFE1;
	int APP2 = 0xFFE2;
	int APP3 = 0xFFE3;
	int APP4 = 0xFFE4;
	int APP5 = 0xFFE5;
	int APP6 = 0xFFE6;
	int APP7 = 0xFFE7;
	int APP8 = 0xFFE8;
	int APP9 = 0xFFE9;
	int APP10 = 0xFFEA;
	int APP11 = 0xFFEB;
	int APP12 = 0xFFEC;
	int APP13 = 0xFFED;
	int APP14 = 0xFFEE;
	int APP15 = 0xFFEF;

	// Start of Frame segment markers (SOFn).
	/** SOF0: Baseline DCT, Huffman coding. */
	int SOF0 = 0xFFC0;
	/** SOF0: Extended DCT, Huffman coding. */
	int SOF1 = 0xFFC1;
	/** SOF2: Progressive DCT, Huffman coding. */
	int SOF2 = 0xFFC2;
	/** SOF3: Lossless sequential, Huffman coding. */
	int SOF3 = 0xFFC3;
	/** SOF5: Sequential DCT, differential Huffman coding. */
	int SOF5 = 0xFFC5;
	/** SOF6: Progressive DCT, differential Huffman coding. */
	int SOF6 = 0xFFC6;
	/** SOF7: Lossless, Differential Huffman coding. */
	int SOF7 = 0xFFC7;
	/** SOF9: Extended sequential DCT, arithmetic coding. */
	int SOF9 = 0xFFC9;
	/** SOF10: Progressive DCT, arithmetic coding. */
	int SOF10 = 0xFFCA;
	/** SOF11: Lossless sequential, arithmetic coding. */
	int SOF11 = 0xFFCB;
	/** SOF13: Sequential DCT, differential arithmetic coding. */
	int SOF13 = 0xFFCD;
	/** SOF14: Progressive DCT, differential arithmetic coding. */
	int SOF14 = 0xFFCE;
	/** SOF15: Lossless, differential arithmetic coding. */
	int SOF15 = 0xFFCF;

	// JPEG-LS markers
	/** SOF55: JPEG-LS. */
	int SOF55 = 0xFFF7; // NOTE: Equal to a normal SOF segment
	int LSE = 0xFFF8; // JPEG-LS Preset Parameter marker

	// TO DO: Known/Important APPn marker identifiers
	// "JFIF" APP0
	// "JFXX" APP0
	// "Exif" APP1
	// "ICC_PROFILE" APP2
	// "Adobe" APP14

	// Possibly
	// "http://ns.adobe.com/xap/1.0/" (XMP) APP1
	// "Photoshop 3.0" (may contain IPTC) APP13
}

class Segment {
	final int marker;

	protected Segment(final int marker) {
		this.marker = Validate.isTrue(marker >> 8 == 0xFF, marker, "Unknown JPEG marker: 0x%04x");
	}

	@SuppressWarnings({ "java:S6208" })
	static Segment read(int marker, String identifier, int length, DataInput data) throws IOException {
		switch (marker) {
		case JPEG.DHT:
			return HuffmanTable.read(data, length);
		case JPEG.DQT:
			return QuantizationTable.read(data, length);
		case JPEG.SOF0:
		case JPEG.SOF1:
		case JPEG.SOF2:
		case JPEG.SOF3:
		case JPEG.SOF5:
		case JPEG.SOF6:
		case JPEG.SOF7:
		case JPEG.SOF9:
		case JPEG.SOF10:
		case JPEG.SOF11:
		case JPEG.SOF13:
		case JPEG.SOF14:
		case JPEG.SOF15:
			return Frame.read(marker, data, length);
		case JPEG.SOS:
			return Scan.read(data, length);
		case JPEG.COM:
			return Comment.read(data, length);
		// TO DO: JPEG.DAC
		case JPEG.DRI:
			return RestartInterval.read(data, length);
		case JPEG.APP0:
		case JPEG.APP1:
		case JPEG.APP2:
		case JPEG.APP3:
		case JPEG.APP4:
		case JPEG.APP5:
		case JPEG.APP6:
		case JPEG.APP7:
		case JPEG.APP8:
		case JPEG.APP9:
		case JPEG.APP10:
		case JPEG.APP11:
		case JPEG.APP12:
		case JPEG.APP13:
		case JPEG.APP14:
		case JPEG.APP15:
			return Application.read(marker, identifier, data, length);
		default:
			return Unknown.read(marker, length, data);
		}
	}
}

class Frame extends Segment {
	final int samplePrecision; // Sample precision
	final int lines; // Height
	final int samplesPerLine; // Width

	final Component[] components; // Components specifications

	private Frame(final int marker, final int samplePrecision, final int lines, final int samplesPerLine,
			final Component[] components) {
		super(marker);

		this.samplePrecision = samplePrecision;
		this.lines = lines;
		this.samplesPerLine = samplesPerLine;
		this.components = components;
	}

	int process() {
		return marker & 0xff - 0xc0;
	}

	int componentsInFrame() {
		return components.length;
	}

	Component getComponent(final int id) {
		for (Component component : components) {
			if (component.id == id) {
				return component;
			}
		}

		throw new IllegalArgumentException(String.format("No such component id: %d", id));
	}

	@Override
	public String toString() {
		return String.format("SOF%d[%04x, precision: %d, lines: %d, samples/line: %d, components: %s]", process(),
				marker, samplePrecision, lines, samplesPerLine, Arrays.toString(components));
	}

	static Frame read(final int marker, final DataInput data, final int length) throws IOException {
		int samplePrecision = data.readUnsignedByte();
		int lines = data.readUnsignedShort();
		int samplesPerLine = data.readUnsignedShort();
		int componentsInFrame = data.readUnsignedByte();

		int expected = 8 + componentsInFrame * 3;
		if (length != expected) {
			throw new IIOException(String.format("Unexpected SOF length: %d != %d", length, expected));
		}

		Component[] components = new Component[componentsInFrame];

		for (int i = 0; i < componentsInFrame; i++) {
			int id = data.readUnsignedByte();
			int sub = data.readUnsignedByte();
			int qtSel = data.readUnsignedByte();

			components[i] = new Component(id, ((sub & 0xF0) >> 4), (sub & 0xF), qtSel);
		}

		return new Frame(marker, samplePrecision, lines, samplesPerLine, components);
	}

	static Frame read(final int marker, final ImageInputStream data) throws IOException {
		int length = data.readUnsignedShort();

		return read(marker, new SubImageInputStream(data, length), length);
	}

	public static final class Component {
		final int id;
		final int hSub; // Horizontal sampling factor
		final int vSub; // Vertical sampling factor
		final int qtSel; // Quantization table destination selector

		Component(int id, int hSub, int vSub, int qtSel) {
			this.id = id;
			this.hSub = hSub;
			this.vSub = vSub;
			this.qtSel = qtSel;
		}

		@Override
		public String toString() {
			// Use id either as component number or component name, based on value
			Serializable idStr = (id >= 'a' && id <= 'z' || id >= 'A' && id <= 'Z') ? "'" + (char) id + "'" : id;
			return String.format("id: %s, sub: %d/%d, sel: %d", idStr, hSub, vSub, qtSel);
		}
	}
}

class QuantizationTable extends Segment {

	private static final int[] ZIGZAG = { 0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30,
			41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21,
			34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63 };

	private final int[] precision = new int[4]; // Quantization precision 8 or 16
	private final boolean[] tq = new boolean[4]; // 1: this table is present

	private final int[][] quantTables = new int[4][64]; // Tables

	QuantizationTable() {
		super(JPEG.DQT);
	}

	// TO DO: Consider creating a copy for the decoder here, as we need to keep the
	// original values for the metadata
	void enhanceTables() {
		for (int t = 0; t < 4; t++) {
			if (tq[t]) {
				enhanceQuantizationTable(quantTables[t], ZIGZAG);
			}
		}
	}

	private void enhanceQuantizationTable(final int[] qtab, final int[] table) {
		for (int i = 0; i < 8; i++) {
			qtab[table[i]] *= 90;
			qtab[table[(4 * 8) + i]] *= 90;
			qtab[table[(2 * 8) + i]] *= 118;
			qtab[table[(6 * 8) + i]] *= 49;
			qtab[table[(5 * 8) + i]] *= 71;
			qtab[table[(8) + i]] *= 126;
			qtab[table[(7 * 8) + i]] *= 25;
			qtab[table[(3 * 8) + i]] *= 106;
		}

		for (int i = 0; i < 8; i++) {
			qtab[table[(8 * i)]] *= 90;
			qtab[table[4 + (8 * i)]] *= 90;
			qtab[table[2 + (8 * i)]] *= 118;
			qtab[table[6 + (8 * i)]] *= 49;
			qtab[table[5 + (8 * i)]] *= 71;
			qtab[table[1 + (8 * i)]] *= 126;
			qtab[table[7 + (8 * i)]] *= 25;
			qtab[table[3 + (8 * i)]] *= 106;
		}

		for (int i = 0; i < 64; i++) {
			qtab[i] >>= 6;
		}
	}

	@Override
	public String toString() {
		// TO DO: Tables...
		return "DQT[]";
	}

	@SuppressWarnings({ "java:S3776" })
	public static QuantizationTable read(final DataInput data, final int length) throws IOException {
		int count = 2;

		QuantizationTable table = new QuantizationTable();
		while (count < length) {
			final int temp = data.readUnsignedByte();
			count++;
			final int t = temp & 0x0F;

			if (t > 3) {
				throw new IIOException("Unexpected JPEG Quantization Table Id (> 3): " + t);
			}

			table.precision[t] = temp >> 4;

			if (table.precision[t] == 0) {
				table.precision[t] = 8;
			} else if (table.precision[t] == 1) {
				table.precision[t] = 16;
			} else {
				throw new IIOException("Unexpected JPEG Quantization Table precision: " + table.precision[t]);
			}

			table.tq[t] = true;

			if (table.precision[t] == 8) {
				for (int i = 0; i < 64; i++) {
					if (count > length) {
						throw new IIOException("JPEG Quantization Table format error");
					}

					table.quantTables[t][i] = data.readUnsignedByte();
					count++;
				}
			} else {
				for (int i = 0; i < 64; i++) {
					if (count > length) {
						throw new IIOException("JPEG Quantization Table format error");
					}

					table.quantTables[t][i] = data.readUnsignedShort();
					count += 2;
				}
			}
		}

		if (count != length) {
			throw new IIOException("JPEG Quantization Table error, bad segment length: " + length);
		}

		return table;
	}

	public boolean isPresent(int tabelId) {
		return tq[tabelId];
	}

	int precision(int tableId) {
		return precision[tableId];
	}

	int[] qTable(int tabelId) {
		return quantTables[tabelId];
	}

	javax.imageio.plugins.jpeg.JPEGQTable toNativeTable(int tableId) {
		// TO DO: Should de-zigzag (ie. "natural order") while reading
		// TO DO: ...and make sure the table isn't "enhanced"...
		int[] qTable = new int[quantTables[tableId].length];

		for (int i = 0; i < qTable.length; i++) {
			qTable[i] = quantTables[tableId][ZIGZAG[i]];
		}

		return new javax.imageio.plugins.jpeg.JPEGQTable(qTable);
	}
}

class HuffmanTable extends Segment {

	private final short[][][] l = new short[4][2][16];
	private final short[][][][] v = new short[4][2][16][200]; // tables
	private final boolean[][] tc = new boolean[4][2]; // 1: this table is present

	private static final int MSB = 0x80000000;

	private HuffmanTable() {
		super(JPEG.DHT);
	}

	void buildHuffTables(final int[][][] huffTab) throws IOException {
		for (int t = 0; t < 4; t++) {
			for (int c = 0; c < 2; c++) {
				if (tc[t][c]) {
					buildHuffTable(huffTab[t][c], l[t][c], v[t][c]);
				}
			}
		}
	}

	// Build_HuffTab()
	// Parameter: t table ID
	// c table class ( 0 for DC, 1 for AC )
	// L[i] # of codewords which length is i
	// V[i][j] Huffman Value (length=i)
	// Effect:
	// build up HuffTab[t][c] using L and V.
	@SuppressWarnings({ "java:S117", "java:S3776" })
	private void buildHuffTable(final int[] tab, final short[] L, final short[][] V) throws IOException {
		int temp = 256;
		int k = 0;

		for (int i = 0; i < 8; i++) { // i+1 is Code length
			for (int j = 0; j < L[i]; j++) {
				for (int n = 0; n < (temp >> (i + 1)); n++) {
					tab[k] = V[i][j] | ((i + 1) << 8);
					k++;
				}
			}
		}

		for (int i = 1; k < 256; i++, k++) {
			tab[k] = i | MSB;
		}

		int currentTable = 1;
		k = 0;

		for (int i = 8; i < 16; i++) { // i+1 is Code length
			for (int j = 0; j < L[i]; j++) {
				for (int n = 0; n < (temp >> (i - 7)); n++) {
					tab[(currentTable * 256) + k] = V[i][j] | ((i + 1) << 8);
					k++;
				}
				if (k >= 256) {
					if (k > 256) {
						throw new IIOException("JPEG Huffman Table error");
					}

					k = 0;
					currentTable++;
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("DHT[");

		for (int t = 0; t < tc.length; t++) {
			for (int c = 0; c < tc[t].length; c++) {
				if (tc[t][c]) {
					if (builder.length() > 4) {
						builder.append(", ");
					}

					builder.append("id: ");
					builder.append(t);

					builder.append(", class: ");
					builder.append(c);
				}
			}
		}

		builder.append(']');

		return builder.toString();
	}

	public static Segment read(final DataInput data, final int length) throws IOException {
		int count = 2;

		HuffmanTable table = new HuffmanTable();

		while (count < length) {
			int temp = data.readUnsignedByte();
			count++;
			int t = temp & 0x0F;
			if (t > 3) {
				throw new IIOException("Unexpected JPEG Huffman Table Id (> 3):" + t);
			}

			int c = temp >> 4;
			if (c > 2) {
				throw new IIOException("Unexpected JPEG Huffman Table class (> 2): " + c);
			}

			table.tc[t][c] = true;

			for (int i = 0; i < 16; i++) {
				table.l[t][c][i] = (short) data.readUnsignedByte();
				count++;
			}

			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < table.l[t][c][i]; j++) {
					if (count > length) {
						throw new IIOException("JPEG Huffman Table format error");
					}
					table.v[t][c][i][j] = (short) data.readUnsignedByte();
					count++;
				}
			}
		}

		if (count != length) {
			throw new IIOException("JPEG Huffman Table format error, bad segment length: " + length);
		}

		return table;
	}

	public boolean isPresent(int tableId, int tableClass) {
		return tc[tableId][tableClass];
	}

	private short[] lengths(int tableId, int tableClass) {
		return l[tableId][tableClass];
	}

	private short[] tables(int tableId, int tableClass) {
		// Find sum of lengths
		short[] lengths = lengths(tableId, tableClass);

		int sumOfLengths = 0;
		for (int length : lengths) {
			sumOfLengths += length;
		}

		// Flatten the tables
		short[] tables = new short[sumOfLengths];

		int pos = 0;
		for (int i = 0; i < 16; i++) {
			short[] table = v[tableId][tableClass][i];
			short length = lengths[i];

			System.arraycopy(table, 0, tables, pos, length);
			pos += length;
		}

		return tables;
	}

	JPEGHuffmanTable toNativeTable(int tableId, int tableClass) {
		return new JPEGHuffmanTable(lengths(tableId, tableClass), tables(tableId, tableClass));
	}
}

class Scan extends Segment {
	final int spectralSelStart; // Start of spectral or predictor selection
	final int spectralSelEnd; // End of spectral selection
	final int approxHigh;
	final int approxLow;

	final Component[] components;

	Scan(final Component[] components, final int spectralStart, final int spectralSelEnd, final int approxHigh,
			final int approxLow) {
		super(JPEG.SOS);

		this.components = components;
		this.spectralSelStart = spectralStart;
		this.spectralSelEnd = spectralSelEnd;
		this.approxHigh = approxHigh;
		this.approxLow = approxLow;
	}

	@Override
	public String toString() {
		return String.format(
				"SOS[spectralSelStart: %d, spectralSelEnd: %d, approxHigh: %d, approxLow: %d, components: %s]",
				spectralSelStart, spectralSelEnd, approxHigh, approxLow, Arrays.toString(components));
	}

	public static Scan read(final ImageInputStream data) throws IOException {
		int length = data.readUnsignedShort();

		return read(new SubImageInputStream(data, length), length);
	}

	public static Scan read(final DataInput data, final int length) throws IOException {
		int numComp = data.readUnsignedByte();

		int expected = 6 + numComp * 2;
		if (expected != length) {
			throw new IIOException(String.format("Unexpected SOS length: %d != %d", length, expected));
		}

		Component[] components = new Component[numComp];

		for (int i = 0; i < numComp; i++) {
			int scanCompSel = data.readUnsignedByte();
			final int temp = data.readUnsignedByte();

			components[i] = new Component(scanCompSel, temp & 0x0F, temp >> 4);
		}

		int selection = data.readUnsignedByte();
		int spectralEnd = data.readUnsignedByte();
		int temp = data.readUnsignedByte();

		return new Scan(components, selection, spectralEnd, temp >> 4, temp & 0x0F);
	}

	public final static class Component {
		final int scanCompSel; // Scan component selector
		final int acTabSel; // AC table selector
		final int dcTabSel; // DC table selector

		Component(final int scanCompSel, final int acTabSel, final int dcTabSel) {
			this.scanCompSel = scanCompSel;
			this.acTabSel = acTabSel;
			this.dcTabSel = dcTabSel;
		}

		@Override
		public String toString() {
			return String.format("scanCompSel: %d, acTabSel: %d, dcTabSel: %d", scanCompSel, acTabSel, dcTabSel);
		}
	}
}

class Comment extends Segment {
	final String comment;

	private Comment(final String comment) {
		super(JPEG.COM);
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "COM[" + comment + "]";
	}

	public static Segment read(final DataInput data, final int length) throws IOException {
		byte[] ascii = new byte[length - 2];
		data.readFully(ascii);

		return new Comment(new String(ascii, StandardCharsets.UTF_8)); // Strictly, it is ASCII, but UTF-8 is compatible
	}
}

class RestartInterval extends Segment {
	final int interval;

	private RestartInterval(int interval) {
		super(JPEG.DRI);
		this.interval = interval;
	}

	@Override
	public String toString() {
		return "DRI[" + interval + "]";
	}

	public static RestartInterval read(final DataInput data, final int length) throws IOException {
		if (length != 4) {
			throw new IIOException("Unexpected length of DRI segment: " + length);
		}

		return new RestartInterval(data.readUnsignedShort());
	}
}

class Unknown extends Segment {
	final byte[] data;

	private Unknown(final int marker, final byte[] data) {
		super(marker);

		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("Unknown[%04x, length: %d]", marker, data.length);
	}

	public static Segment read(int marker, int length, DataInput data) throws IOException {
		byte[] bytes = new byte[length - 2];
		data.readFully(bytes);

		return new Unknown(marker, bytes);
	}
}

class Application extends Segment {

	final String identifier;
	final byte[] data;

	Application(final int marker, final String identifier, final byte[] data) {
		super(marker);

		this.identifier = identifier; // NOTE: Some JPEGs contain APP segments without NULL-terminated identifier
		this.data = data;
	}

	InputStream data() {
		int offset = identifier.length() + 1;
		return new ByteArrayInputStream(data, offset, data.length - offset);
	}

	@Override
	public String toString() {
		return "APP" + (marker & 0x0f) + "/" + identifier + "[length: " + data.length + "]";
	}

	@SuppressWarnings({ "java:S128" })
	public static Application read(final int marker, final String identifier, final DataInput data, final int length)
			throws IOException {
		switch (marker) {
		case JPEG.APP0:
			// JFIF
			if ("JFIF".equals(identifier)) {
				return JFIF.read(data, length);
			}
		case JPEG.APP1:
			// JFXX
			if ("JFXX".equals(identifier)) {
				return JFXX.read(data, length);
			}
			if ("Exif".equals(identifier)) {
				return EXIF.read(data, length);
			}
		case JPEG.APP2:
			// ICC_PROFILE
			if ("ICC_PROFILE".equals(identifier)) {
				return ICCProfile.read(data, length);
			}
		case JPEG.APP14:
			// Adobe
			if ("Adobe".equals(identifier)) {
				return AdobeDCT.read(data, length);
			}

		default:
			// Generic APPn segment
			byte[] bytes = new byte[Math.max(0, length - 2)];
			data.readFully(bytes);
			return new Application(marker, identifier, bytes);
		}
	}
}

class AdobeDCT extends Application {
	static final int Unknown = 0;
	static final int YCC = 1;
	static final int YCCK = 2;

	final int version;
	final int flags0;
	final int flags1;
	final int transform;

	private AdobeDCT(int version, int flags0, int flags1, int transform) {
		super(JPEG.APP14, "Adobe", new byte[] { 'A', 'd', 'o', 'b', 'e', 0, (byte) version, (byte) (flags0 >> 8),
				(byte) (flags0 & 0xff), (byte) (flags1 >> 8), (byte) (flags1 & 0xff), (byte) transform });

		this.version = version; // 100 or 101
		this.flags0 = flags0;
		this.flags1 = flags1;
		this.transform = transform;
	}

	@Override
	public String toString() {
		return String.format("AdobeDCT[ver: %d.%02d, flags: %s %s, transform: %d]", version / 100, version % 100,
				Integer.toBinaryString(flags0), Integer.toBinaryString(flags1), transform);
	}

	public static AdobeDCT read(final DataInput data, final int length) throws IOException {
		// Investigate http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6355567: 33/35
		// byte Adobe APP14 markers

		data.skipBytes(6); // A, d, o, b, e, \0

		// version (byte), flags (4bytes), color transform (byte: 0=unknown, 1=YCC,
		// 2=YCCK)
		return new AdobeDCT(data.readUnsignedByte(), data.readUnsignedShort(), data.readUnsignedShort(),
				data.readUnsignedByte());
	}
}

class ICCProfile extends Application {
	private ICCProfile(final byte[] data) {
		super(JPEG.APP2, "ICC_PROFILE", data);
	}

	// TO DO: Create util method to concat all ICC segments to one and return
	// ICC_Profile (move from JPEGImageReader)
	// If so, how to deal with warnings from the original code? Throw exceptions
	// instead?

	@Override
	public String toString() {
		return "ICC_PROFILE[" + data[12] + "/" + data[13] + " length: " + data.length + "]";
	}

	public static ICCProfile read(DataInput data, int length) throws IOException {
		byte[] bytes = new byte[length - 2];
		data.readFully(bytes);

		return new ICCProfile(bytes);
	}
}

class EXIF extends Application {
	EXIF(byte[] data) {
		super(JPEG.APP1, "Exif", data);
	}

	@Override
	public String toString() {
		return String.format("APP1/Exif, length: %d", data.length);
	}

	ImageInputStream exifData() {
		// Identifier is "Exif\0" + 1 byte pad
		int offset = identifier.length() + 2;
		return new ByteArrayImageInputStream(data, offset, data.length - offset);
	}

	public static EXIF read(final DataInput data, int length) throws IOException {
		if (length < 2 + 6) {
			throw new EOFException();
		}

		byte[] bytes = new byte[length - 2];
		data.readFully(bytes);

		return new EXIF(bytes);
	}
}

class JFIF extends Application {
	final int majorVersion;
	final int minorVersion;
	final int units;
	final int xDensity;
	final int yDensity;
	final int xThumbnail;
	final int yThumbnail;
	final byte[] thumbnail;

	@SuppressWarnings({ "java:S107" })
	JFIF(int majorVersion, int minorVersion, int units, int xDensity, int yDensity, int xThumbnail, int yThumbnail,
			byte[] thumbnail) {
		super(JPEG.APP0, "JFIF", new byte[5 + 9 + (thumbnail != null ? thumbnail.length : 0)]);

		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.units = units;
		this.xDensity = xDensity;
		this.yDensity = yDensity;
		this.xThumbnail = xThumbnail;
		this.yThumbnail = yThumbnail;
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return String.format("APP0/JFIF v%d.%02d %dx%d %s (%s)", majorVersion, minorVersion, xDensity, yDensity,
				unitsAsString(), thumbnailToString());
	}

	private String unitsAsString() {
		switch (units) {
		case 0:
			return "(aspect only)";
		case 1:
			return "dpi";
		case 2:
			return "dpcm";
		default:
			return "(unknown unit)";
		}
	}

	private String thumbnailToString() {
		if (xThumbnail == 0 || yThumbnail == 0) {
			return "no thumbnail";
		}

		return String.format("thumbnail: %dx%d", xThumbnail, yThumbnail);
	}

	public static JFIF read(final DataInput data, int length) throws IOException {
		if (length < 2 + 5 + 9) {
			throw new EOFException();
		}

		data.readFully(new byte[5]); // Skip "JFIF\0"

		byte[] bytes = new byte[length - 2 - 5];
		data.readFully(bytes);

		int x;
		int y;

		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		return new JFIF(buffer.get() & 0xff, buffer.get() & 0xff, buffer.get() & 0xff, buffer.getShort() & 0xffff,
				buffer.getShort() & 0xffff, x = buffer.get() & 0xff, y = buffer.get() & 0xff,
				getBytes(buffer, Math.min(buffer.remaining(), x * y * 3)));
	}

	private static byte[] getBytes(ByteBuffer buffer, int len) {
		if (len == 0) {
			return null;
		}

		byte[] dst = new byte[len];
		buffer.get(dst);

		return dst;
	}
}

class JFXX extends Application {
	public static final int JPEG = 0x10;
	public static final int INDEXED = 0x11;
	public static final int RGB = 0x13;

	final int extensionCode;
	final byte[] thumbnail;

	JFXX(final int extensionCode, final byte[] thumbnail) {
		super(io.mosip.biometrics.util.test.JPEG.APP0, "JFXX",
				new byte[1 + (thumbnail != null ? thumbnail.length : 0)]);

		this.extensionCode = extensionCode;
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return String.format("APP0/JFXX extension (%s thumb size: %d)", extensionAsString(), thumbnail.length);
	}

	private String extensionAsString() {
		switch (extensionCode) {
		case JPEG:
			return "JPEG";
		case INDEXED:
			return "Indexed";
		case RGB:
			return "RGB";
		default:
			return String.valueOf(extensionCode);
		}
	}

	public static JFXX read(final DataInput data, final int length) throws IOException {
		data.readFully(new byte[5]);

		byte[] bytes = new byte[length - 2 - 5];
		data.readFully(bytes);

		return new JFXX(bytes[0] & 0xff, bytes.length - 1 > 0 ? Arrays.copyOfRange(bytes, 1, bytes.length - 1) : null);
	}
}

class ByteArrayImageInputStream extends ImageInputStreamImpl {
	private final byte[] data;
	private final int dataOffset;
	private final int dataLength;

	public ByteArrayImageInputStream(final byte[] data) {
		this(data, 0, data != null ? data.length : -1);
	}

	public ByteArrayImageInputStream(final byte[] data, int offset, int length) {
		this.data = Validate.notNull(data, "data");
		dataOffset = isMax(data.length, offset, "offset");
		dataLength = isMax(data.length - offset, length, "length");
	}

	private static int isMax(final int high, final int value, final String name) {
		return Validate.isTrue(value >= 0 && value <= high, value,
				String.format("%s out of range [0, %d]: %d", name, high, value));
	}

	public int read() throws IOException {
		if (streamPos >= dataLength) {
			return -1;
		}

		bitOffset = 0;

		return data[((int) streamPos++) + dataOffset] & 0xff;
	}

	public int read(byte[] buffer, int offset, int len) throws IOException {
		if (streamPos >= dataLength) {
			return -1;
		}

		int length = (int) Math.min(dataLength - streamPos, len);
		bitOffset = 0;
		System.arraycopy(data, (int) streamPos + dataOffset, buffer, offset, length);
		streamPos += length;

		return length;
	}

	@Override
	public long length() {
		return dataLength;
	}

	@Override
	public boolean isCached() {
		return true;
	}

	@Override
	public boolean isCachedMemory() {
		return true;
	}
}

class Validate {
	// TO DO: Make it possible to throw IllegalStateException instead of
	// IllegalArgumentException?

	private static final String UNSPECIFIED_PARAM_NAME = "method parameter";

	private Validate() {
	}

	// Not null...

	public static <T> T notNull(final T pParameter) {
		return notNull(pParameter, null);
	}

	public static <T> T notNull(final T pParameter, final String pParamName) {
		if (pParameter == null) {
			throw new IllegalArgumentException(
					String.format("%s may not be null", pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
		}

		return pParameter;
	}

	// Not empty

	public static <T extends CharSequence> T notEmpty(final T pParameter) {
		return notEmpty(pParameter, null);
	}

	public static <T extends CharSequence> T notEmpty(final T pParameter, final String pParamName) {
		if (pParameter == null || pParameter.length() == 0 || isOnlyWhiteSpace(pParameter)) {
			throw new IllegalArgumentException(
					String.format("%s may not be blank", pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
		}

		return pParameter;
	}

	private static <T extends CharSequence> boolean isOnlyWhiteSpace(T pParameter) {
		for (int i = 0; i < pParameter.length(); i++) {
			if (!Character.isWhitespace(pParameter.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	public static <T> T[] notEmpty(final T[] pParameter) {
		return notEmpty(pParameter, null);
	}

	public static <T> T[] notEmpty(final T[] pParameter, final String pParamName) {
		if (pParameter == null || pParameter.length == 0) {
			throw new IllegalArgumentException(
					String.format("%s may not be empty", pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
		}

		return pParameter;
	}

	public static <T> Collection<T> notEmpty(final Collection<T> pParameter) {
		return notEmpty(pParameter, null);
	}

	public static <T> Collection<T> notEmpty(final Collection<T> pParameter, final String pParamName) {
		if (pParameter == null || pParameter.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("%s may not be empty", pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
		}

		return pParameter;
	}

	public static <K, V> Map<K, V> notEmpty(final Map<K, V> pParameter) {
		return notEmpty(pParameter, null);
	}

	public static <K, V> Map<K, V> notEmpty(final Map<K, V> pParameter, final String pParamName) {
		if (pParameter == null || pParameter.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("%s may not be empty", pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
		}

		return pParameter;
	}

	// No null elements

	public static <T> T[] noNullElements(final T[] pParameter) {
		return noNullElements(pParameter, null);
	}

	public static <T> T[] noNullElements(final T[] pParameter, final String pParamName) {
		noNullElements(pParameter == null ? null : Arrays.asList(pParameter), pParamName);
		return pParameter;
	}

	public static <T> Collection<T> noNullElements(final Collection<T> pParameter) {
		return noNullElements(pParameter, null);
	}

	public static <T> Collection<T> noNullElements(final Collection<T> pParameter, final String pParamName) {
		notNull(pParameter, pParamName);

		for (T element : pParameter) {
			if (element == null) {
				throw new IllegalArgumentException(String.format("%s may not contain null elements",
						pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
			}
		}

		return pParameter;
	}

	public static <K, V> Map<K, V> noNullValues(final Map<K, V> pParameter) {
		return noNullValues(pParameter, null);
	}

	public static <K, V> Map<K, V> noNullValues(final Map<K, V> pParameter, final String pParamName) {
		notNull(pParameter, pParamName);

		for (V value : pParameter.values()) {
			if (value == null) {
				throw new IllegalArgumentException(String.format("%s may not contain null values",
						pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
			}
		}

		return pParameter;
	}

	public static <K, V> Map<K, V> noNullKeys(final Map<K, V> pParameter) {
		return noNullKeys(pParameter, null);
	}

	public static <K, V> Map<K, V> noNullKeys(final Map<K, V> pParameter, final String pParamName) {
		notNull(pParameter, pParamName);

		for (K key : pParameter.keySet()) {
			if (key == null) {
				throw new IllegalArgumentException(String.format("%s may not contain null keys",
						pParamName == null ? UNSPECIFIED_PARAM_NAME : pParamName));
			}
		}

		return pParameter;
	}

	// Is true

	public static boolean isTrue(final boolean pExpression, final String pMessage) {
		return isTrue(pExpression, pExpression, pMessage);
	}

	public static <T> T isTrue(final boolean condition, final T value, final String message) {
		if (!condition) {
			throw new IllegalArgumentException(
					String.format(message == null ? "expression may not be %s" : message, value));
		}

		return value;
	}
}

class SubImageInputStream extends ImageInputStreamImpl {
	// NOTE: This class is based on
	// com.sun.imageio.plugins.common.SubImageInputStream, but fixes some of its
	// bugs.

	private final ImageInputStream stream;
	private final long startPos;
	private final long length;

	/**
	 * Creates a {@link ImageInputStream}, reading up to a maximum number of bytes
	 * from the underlying stream.
	 *
	 * @param stream the underlying stream
	 * @param length the maximum length to read from the stream. Note that
	 *               {@code stream} may contain less than this maximum number of
	 *               bytes.
	 *
	 * @throws IOException              if {@code stream}'s position can't be
	 *                                  determined.
	 * @throws IllegalArgumentException if {@code stream == null} or
	 *                                  {@code length < 0}
	 */
	public SubImageInputStream(final ImageInputStream stream, final long length) throws IOException {
		Validate.notNull(stream, "stream");
		Validate.isTrue(length >= 0, length, "length < 0: %d");

		this.stream = stream;
		this.startPos = stream.getStreamPosition();
		this.length = length;
	}

	public int read() throws IOException {
		if (streamPos >= length) { // Local EOF
			return -1;
		} else {
			int read = stream.read();

			if (read >= 0) {
				streamPos++;
			}

			return read;
		}
	}

	public int read(final byte[] bytes, final int off, final int len) throws IOException {
		if (streamPos >= length) { // Local EOF
			return -1;
		}

		// Safe cast, as len can never cause int overflow
		int length = (int) Math.min(len, this.length - streamPos);
		int count = stream.read(bytes, off, length);

		if (count >= 0) {
			streamPos += count;
		}

		return count;
	}

	@Override
	public long length() {
		try {
			long length = stream.length();
			return length < 0 ? -1 : Math.min(length - startPos, this.length);
		} catch (IOException ignore) {
		}

		return -1;
	}

	@Override
	public void seek(final long position) throws IOException {
		if (position < getFlushedPosition()) {
			throw new IndexOutOfBoundsException("pos < flushedPosition");
		}

		stream.seek(startPos + position);
		streamPos = position;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	protected void finalize() {
	}
}
