package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacialInformation extends AbstractImageInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacialInformation.class);

	/** Indexes into poseAngle array. */
	private static final int YAW = 0, PITCH = 1, ROLL = 2;

	private int noOfLandMarkPoints;
	private int gender;
	private int eyeColor;
	private int hairColor;
	private int subjectHeight;
	private int featuresMask;
	private int expressionMask;
	private int[] poseAngle = new int[3];
	private int[] poseAngleUncertainty = new int[3];

	public FacialInformation(int noOfLandMarkPoints, int[] poseAngle, int[] poseAngleUncertainty) {
		setNoOfLandMarkPoints(noOfLandMarkPoints);
		setGender(Gender.UNSPECIFIED);
		setEyeColor(EyeColour.UNSPECIFIED);
		setHairColor(HairColour.UNSPECIFIED);
		setSubjectHeight(HeightCodes.UNSPECIFIED);
		setFeaturesMask(Features.FEATURES_ARE_SPECIFIED);
		setExpressionMask(0);

		setPoseAngle(new int[3]);
		System.arraycopy(poseAngle, 0, poseAngle, 0, 3);

		setPoseAngleUncertainty(new int[3]);
		System.arraycopy(poseAngleUncertainty, 0, poseAngleUncertainty, 0, 3);
	}

	public FacialInformation(int noOflandMarkPoints, int gender, int eyeColor, int hairColor, int subjectHeight,
			int propertyMask, int expressionMask, int[] poseAngle, int[] poseAngleUncertainty) {
		setNoOfLandMarkPoints(noOflandMarkPoints);
		setGender(gender);
		setEyeColor(eyeColor);
		setHairColor(hairColor);
		setSubjectHeight(subjectHeight);
		setFeaturesMask(propertyMask);
		setExpressionMask(expressionMask);

		setPoseAngle(new int[3]);
		System.arraycopy(poseAngle, 0, poseAngle, 0, 3);

		setPoseAngleUncertainty(new int[3]);
		System.arraycopy(poseAngleUncertainty, 0, poseAngleUncertainty, 0, 3);
	}

	public FacialInformation(DataInputStream inputStream) throws IOException {
		readObject(inputStream);
	}

	public FacialInformation(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		readObject(inputStream, onlyImageInformation);
	}

	@Override
	protected void readObject(DataInputStream inputStream) throws IOException {
		/* Facial Information Block (20), see ISO 19794-5-2011 5.5 */
		setNoOfLandMarkPoints(inputStream.readUnsignedShort());
		setGender(inputStream.readUnsignedByte());
		setEyeColor(inputStream.readUnsignedByte());
		setHairColor(inputStream.readUnsignedByte());
		setSubjectHeight(inputStream.readUnsignedByte());
		int featureMask = inputStream.readUnsignedByte();
		featureMask = (featureMask << 16) | inputStream.readUnsignedShort();

		setFeaturesMask(Features.fromValue(featureMask));
		setExpressionMask(inputStream.readUnsignedShort());

		poseAngle = new int[3];
		int by = inputStream.readUnsignedByte();
		poseAngle[YAW] = by;
		int bp = inputStream.readUnsignedByte();
		poseAngle[PITCH] = bp;
		int br = inputStream.readUnsignedByte();
		poseAngle[ROLL] = br;
		setPoseAngle(poseAngle);

		poseAngleUncertainty = new int[3];
		poseAngleUncertainty[YAW] = inputStream.readUnsignedByte();
		poseAngleUncertainty[PITCH] = inputStream.readUnsignedByte();
		poseAngleUncertainty[ROLL] = inputStream.readUnsignedByte();
		setPoseAngleUncertainty(poseAngleUncertainty);
	}

	@Override
	protected void readObject(DataInputStream inputStream, boolean onlyImageInformation) throws IOException {
		setNoOfLandMarkPoints(inputStream.readUnsignedShort());
		// 1(gender) + 1(EyeColor) + 1(HairColor) + 1(SubjectHeight) 
		// + 3(featureMask) + 2(ExpressionMask) + 3(poseAngle) + 3(poseAngleUncertainty)
		inputStream.skip(15);
	}

	@Override
	public long getRecordLength() {
		return 17; /* 2 + 1 + 1 + 1 + 1 + 3 + 2 + 3 + 3 (table 2 ISO/IEC 19794-5) */
	}

	@Override
	public void writeObject(DataOutputStream outputStream) throws IOException {
		outputStream.writeShort(getNoOfLandMarkPoints()); /* 2 */
		outputStream.writeByte(getGender()); /* + 1 = 3 */
		outputStream.writeByte(getEyeColor()); /* + 1 = 4 */
		outputStream.writeByte(getHairColor()); /* + 1 = 5 */
		outputStream.writeByte(getSubjectHeight()); /* + 1 = 6 */

		outputStream.writeByte((byte) ((getFeaturesMask() & 0xFF0000L) >> 16)); /* + 1 = 7 */
		outputStream.writeByte((byte) ((getFeaturesMask() & 0x00FF00L) >> 8)); /* + 1 = 8 */
		outputStream.writeByte((byte) (getFeaturesMask() & 0x0000FFL)); /* + 1 = 9 */

		outputStream.writeShort(getExpressionMask()); /* + 2 = 11 */

		for (int i = 0; i < 3; i++) { /* + 3 = 14 */
			int b = poseAngle[i];
			outputStream.writeByte(b);
		}

		for (int i = 0; i < 3; i++) /* + 3 = 17 */
		{
			outputStream.writeByte(poseAngleUncertainty[i]);
		}

		outputStream.flush();
	}

	public int getNoOfLandMarkPoints() {
		return noOfLandMarkPoints;
	}

	public void setNoOfLandMarkPoints(int noOfLandMarkPoints) {
		this.noOfLandMarkPoints = noOfLandMarkPoints;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(int eyeColor) {
		this.eyeColor = eyeColor;
	}

	public int getHairColor() {
		return hairColor;
	}

	public void setHairColor(int hairColor) {
		this.hairColor = hairColor;
	}

	public int getSubjectHeight() {
		return subjectHeight;
	}

	public void setSubjectHeight(int subjectHeight) {
		this.subjectHeight = subjectHeight;
	}

	public int getFeaturesMask() {
		return featuresMask;
	}

	public void setFeaturesMask(int featuresMask) {
		this.featuresMask = featuresMask;
	}

	public int getExpressionMask() {
		return expressionMask;
	}

	public void setExpressionMask(int expressionMask) {
		this.expressionMask = expressionMask;
	}

	public int[] getPoseAngle() {
		return poseAngle;
	}

	public void setPoseAngle(int[] poseAngle) {
		this.poseAngle = poseAngle;
	}

	public int[] getPoseAngleUncertainty() {
		return poseAngleUncertainty;
	}

	public void setPoseAngleUncertainty(int[] poseAngleUncertainty) {
		this.poseAngleUncertainty = poseAngleUncertainty;
	}

	@Override
	public String toString() {
		return "\nFacialInformation [FacialInformationRecordLength=" + getRecordLength() + ", noOfLandMarkPoints="
				+ noOfLandMarkPoints + ", gender=" + Integer.toHexString(gender) + ", eyeColor="
				+ Integer.toHexString(eyeColor) + ", hairColor=" + Integer.toHexString(hairColor) + ", subjectHeight="
				+ Integer.toHexString(subjectHeight) + ", featuresMask=" + Integer.toHexString(featuresMask)
				+ ", expressionMask=" + Integer.toHexString(expressionMask) + ", poseAngle="
				+ Arrays.toString(poseAngle) + ", poseAngleUncertainty=" + Arrays.toString(poseAngleUncertainty)
				+ "]\n";
	}

}
