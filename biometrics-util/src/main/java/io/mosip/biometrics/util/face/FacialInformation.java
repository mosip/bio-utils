package io.mosip.biometrics.util.face;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.mosip.biometrics.util.AbstractImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacialInformation extends AbstractImageInfo
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FacialInformation.class);	

	/** Indexes into poseAngle array. */
	private static final int YAW = 0, PITCH = 1, ROLL = 2;
	
    private int noOfLandMarkPoints;
    private Gender gender;
    private EyeColour eyeColor;
    private HairColour hairColor;
    private HeightCodes subjectHeight;
    private Features featuresMask;
    private Expression expressionMask;
    private int [] poseAngle = new int [3];
    private int [] poseAngleUncertainty = new int [3];

    public FacialInformation (int noOfLandMarkPoints, int [] poseAngle, int [] poseAngleUncertainty)
    {
    	setNoOfLandMarkPoints (noOfLandMarkPoints);
        setGender (Gender.UNSPECIFIED);
        setEyeColor (EyeColour.UNSPECIFIED);
        setHairColor (HairColour.UNSPECIFIED);
        setSubjectHeight (HeightCodes.UNSPECIFIED);
        setFeaturesMask (Features.FEATURES_ARE_SPECIFIED);
        setExpressionMask (Expression.UNSPECIFIED);

        setPoseAngle (new int [3]);
        System.arraycopy(poseAngle, 0, poseAngle, 0, 3);

        setPoseAngleUncertainty (new int [3]);
        System.arraycopy(poseAngleUncertainty, 0, poseAngleUncertainty, 0, 3);
    }

    public FacialInformation (int noOflandMarkPoints, Gender gender, EyeColour eyeColor, 
    		HairColour hairColor, HeightCodes subjectHeight, Features propertyMask, Expression expressionMask, 
    		int [] poseAngle, int [] poseAngleUncertainty)
    {
    	setNoOfLandMarkPoints (noOflandMarkPoints);
        setGender (gender);
        setEyeColor (eyeColor);
        setHairColor (hairColor);
        setSubjectHeight (subjectHeight);
        setFeaturesMask (propertyMask);
        setExpressionMask (expressionMask);

        setPoseAngle (new int [3]);
        System.arraycopy(poseAngle, 0, poseAngle, 0, 3);

        setPoseAngleUncertainty (new int [3]);
        System.arraycopy(poseAngleUncertainty, 0, poseAngleUncertainty, 0, 3);
    }
    
    public FacialInformation (DataInputStream inputStream) throws IOException
	{
    	readObject(inputStream);
	}
    
    protected void readObject(DataInputStream inputStream) throws IOException {
    	/* Facial Information Block (20), see ISO 19794-5-2011 5.5 */
    	setNoOfLandMarkPoints (inputStream.readUnsignedShort());
        setGender (Gender.fromValue(inputStream.readUnsignedByte()));
        setEyeColor (EyeColour.fromValue(inputStream.readUnsignedByte()));
        setHairColor (HairColour.fromValue(inputStream.readUnsignedByte()));
        setSubjectHeight (HeightCodes.fromValue(inputStream.readUnsignedByte()));
        int featureMask = inputStream.readUnsignedByte(); 
		featureMask = (featureMask << 16) | inputStream.readUnsignedShort(); 
		
        setFeaturesMask (Features.fromValue(featureMask));
        setExpressionMask (Expression.fromValue(inputStream.readUnsignedShort()));
        
        poseAngle = new int[3];
		int by = inputStream.readUnsignedByte(); 
		poseAngle[YAW] = by; 
		int bp = inputStream.readUnsignedByte(); 
		poseAngle[PITCH] = bp; 
		int br = inputStream.readUnsignedByte(); 
		poseAngle[ROLL] = br; 
		setPoseAngle (poseAngle);
        
		poseAngleUncertainty = new int[3];
		poseAngleUncertainty[YAW] = inputStream.readUnsignedByte(); 
		poseAngleUncertainty[PITCH] = inputStream.readUnsignedByte(); 
		poseAngleUncertainty[ROLL] = inputStream.readUnsignedByte(); 
		setPoseAngleUncertainty (poseAngleUncertainty);
    }
    
    public int getRecordLength ()
    {
        return 17; /* 2 + 1 + 1 + 1 + 1 + 3 + 2 + 3 + 3 (table 2 ISO/IEC 19794-5) */
    }

    public void writeObject (DataOutputStream outputStream) throws IOException
    {
        outputStream.writeShort (getNoOfLandMarkPoints());								/* 2 */
        outputStream.writeByte (getGender().value());									/* + 1 = 3 */
        outputStream.writeByte (getEyeColor().value());                                 /* + 1 = 4 */
        outputStream.writeByte (getHairColor().value());                                /* + 1 = 5 */
        outputStream.writeByte (getSubjectHeight().value());                            /* + 1 = 6 */

        outputStream.writeByte ((byte)((getFeaturesMask().value() & 0xFF0000L) >> 16));	/* + 1 = 7 */
        outputStream.writeByte ((byte)((getFeaturesMask().value() & 0x00FF00L) >> 8));	/* + 1 = 8 */
        outputStream.writeByte ((byte)(getFeaturesMask().value() & 0x0000FFL));			/* + 1 = 9 */

        outputStream.writeShort (getExpressionMask().value());							/* + 2 = 11 */

        for (int i = 0; i < 3; i++)
        {                                   /* + 3 = 14 */
            int b = poseAngle [i];
            outputStream.writeByte (b);
        }

        for (int i = 0; i < 3; i++)  /* + 3 = 17 */
        {                                 
            outputStream.writeByte (poseAngleUncertainty [i]);
        }

        outputStream.flush ();
    }

	public int getNoOfLandMarkPoints() {
		return noOfLandMarkPoints;
	}

	public void setNoOfLandMarkPoints(int noOfLandMarkPoints) {
		this.noOfLandMarkPoints = noOfLandMarkPoints;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public EyeColour getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(EyeColour eyeColor) {
		this.eyeColor = eyeColor;
	}

	public HairColour getHairColor() {
		return hairColor;
	}

	public void setHairColor(HairColour hairColor) {
		this.hairColor = hairColor;
	}

	public HeightCodes getSubjectHeight() {
		return subjectHeight;
	}

	public void setSubjectHeight(HeightCodes subjectHeight) {
		this.subjectHeight = subjectHeight;
	}

	public Features getFeaturesMask() {
		return featuresMask;
	}

	public void setFeaturesMask(Features featuresMask) {
		this.featuresMask = featuresMask;
	}

	public Expression getExpressionMask() {
		return expressionMask;
	}

	public void setExpressionMask(Expression expressionMask) {
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
		return "\nFacialInformation [FacialInformationRecordLength=" + getRecordLength () + ", noOfLandMarkPoints=" + noOfLandMarkPoints + ", gender=" + gender + ", eyeColor="
				+ eyeColor + ", hairColor=" + hairColor + ", subjectHeight=" + subjectHeight + ", featuresMask="
				+ featuresMask + ", expressionMask=" + expressionMask + ", poseAngle=" + Arrays.toString(poseAngle)
				+ ", poseAngleUncertainty=" + Arrays.toString(poseAngleUncertainty) + "]\n";
	}
}
