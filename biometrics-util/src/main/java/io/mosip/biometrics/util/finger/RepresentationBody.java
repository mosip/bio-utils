package io.mosip.biometrics.util.finger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepresentationBody extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(RepresentationBody.class);

	private ImageData imageData;
	private SegmentationBlock segmentationBlock;
	private AnnotationBlock annotationBlock;
	private CommentBlock[] commentBlocks;

	public RepresentationBody(ImageData imageData, SegmentationBlock segmentationBlock, AnnotationBlock annotationBlock,
			CommentBlock[] commentBlocks) {
		setImageData(imageData);
		setSegmentationBlock(segmentationBlock);
		setAnnotationBlock(annotationBlock);
		setCommentBlocks(commentBlocks);
	}

	public RepresentationBody(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public RepresentationBody(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	@SuppressWarnings({ "java:S3776" })
	protected void readObject(DataInputStream inputStream) throws IOException {
		setImageData(new ImageData(inputStream));
		try {
			if (inputStream.available() > 0) {
				int typeIdentificationCode = inputStream.readUnsignedShort();
				if (typeIdentificationCode == ExtendedDataBlockIdentificationCode.SEGMENTATION) {
					setSegmentationBlock(new SegmentationBlock(inputStream));
				} else if (typeIdentificationCode == ExtendedDataBlockIdentificationCode.ANNOTATION) {
					setAnnotationBlock(new AnnotationBlock(inputStream));
				} else if (typeIdentificationCode >= ExtendedDataBlockIdentificationCode.COMMENT_03
						&& typeIdentificationCode <= ExtendedDataBlockIdentificationCode.COMMENT_FF) {
					readCommentBlocks(inputStream);
				}
			}
			if (inputStream.available() > 0) {
				int typeIdentificationCode = inputStream.readUnsignedShort();
				if (typeIdentificationCode == ExtendedDataBlockIdentificationCode.ANNOTATION) {
					setAnnotationBlock(new AnnotationBlock(inputStream));
				} else if (typeIdentificationCode >= ExtendedDataBlockIdentificationCode.COMMENT_03
						&& typeIdentificationCode <= ExtendedDataBlockIdentificationCode.COMMENT_FF) {
					readCommentBlocks(inputStream);
				}
			}
			if (inputStream.available() > 0) {
				int typeIdentificationCode = inputStream.readUnsignedShort();
				if (typeIdentificationCode >= ExtendedDataBlockIdentificationCode.COMMENT_03
						&& typeIdentificationCode <= ExtendedDataBlockIdentificationCode.COMMENT_FF) {
					readCommentBlocks(inputStream);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("readObject :: Error ::", ex);
		}
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setImageData(new ImageData(inputStream, onlyImageInformation));
		// Rest of data not required so not reading
	}

	private void readCommentBlocks(DataInputStream inputStream) throws IOException {
		try {
			List<CommentBlock> commentBlockList = new ArrayList<>();
			int extendedDataBlockIdentificationCodeValue = ExtendedDataBlockIdentificationCode.COMMENT_03;
			while (inputStream.available() > 0) {
				commentBlockList.add(new CommentBlock(inputStream, extendedDataBlockIdentificationCodeValue));
				extendedDataBlockIdentificationCodeValue++;
			}
			if (!commentBlockList.isEmpty())
				setCommentBlocks(commentBlockList.toArray(new CommentBlock[commentBlockList.size()]));
		} catch (Exception ex) {
			LOGGER.error("readCommentBlocks :: Error ::", ex);
		}
	}

	@Override
	public long getRecordLength() {
		long nCommentBlockLength = 0;
		if (getCommentBlocks() != null) {
			for (int index = 0; index < getCommentBlocks().length; index++)
				nCommentBlockLength += getCommentBlocks()[index].getRecordLength();
		}
		return getImageData().getRecordLength()
				+ (getSegmentationBlock() != null ? getSegmentationBlock().getRecordLength() : 0)
				+ (getAnnotationBlock() != null ? getAnnotationBlock().getRecordLength() : 0) + (nCommentBlockLength);
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		getImageData().writeObject(outputStream);

		if (getSegmentationBlock() != null)
			getSegmentationBlock().writeObject(outputStream);

		if (getAnnotationBlock() != null)
			getAnnotationBlock().writeObject(outputStream);

		if (getCommentBlocks() != null) {
			for (int index = 0; index < getCommentBlocks().length; index++)
				getCommentBlocks()[index].writeObject(outputStream);
		}

		outputStream.flush();
	}

	public ImageData getImageData() {
		return imageData;
	}

	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}

	public SegmentationBlock getSegmentationBlock() {
		return segmentationBlock;
	}

	public void setSegmentationBlock(SegmentationBlock segmentationBlock) {
		this.segmentationBlock = segmentationBlock;
	}

	public AnnotationBlock getAnnotationBlock() {
		return annotationBlock;
	}

	public void setAnnotationBlock(AnnotationBlock annotationBlock) {
		this.annotationBlock = annotationBlock;
	}

	public CommentBlock[] getCommentBlocks() {
		return commentBlocks;
	}

	public void setCommentBlocks(CommentBlock[] commentBlocks) {
		this.commentBlocks = commentBlocks;
	}

	@Override
	public String toString() {
		return "RepresentationBody [imageData=" + imageData + ", segmentationBlock=" + segmentationBlock
				+ ", annotationBlock=" + annotationBlock + ", commentBlock=" + Arrays.toString(commentBlocks) + "]";
	}
}