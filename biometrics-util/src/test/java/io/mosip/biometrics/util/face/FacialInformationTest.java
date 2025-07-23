package io.mosip.biometrics.util.face;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FacialInformationTest {

    /**
     * Tests FacialInformation constructor with basic parameters
     */
    @Test
    public void constructor_basicParameters_createsFacialInformation() {
        int noOfLandMarkPoints = 5;
        int[] poseAngle = {10, 20, 30};
        int[] poseAngleUncertainty = {5, 5, 5};

        FacialInformation facialInfo = new FacialInformation(noOfLandMarkPoints, poseAngle, poseAngleUncertainty);

        assertNotNull(facialInfo);
        assertEquals(noOfLandMarkPoints, facialInfo.getNoOfLandMarkPoints());
        assertEquals(Gender.UNSPECIFIED, facialInfo.getGender());
        assertEquals(EyeColour.UNSPECIFIED, facialInfo.getEyeColor());
        assertEquals(HairColour.UNSPECIFIED, facialInfo.getHairColor());
        assertEquals(HeightCodes.UNSPECIFIED, facialInfo.getSubjectHeight());
        assertEquals(Features.FEATURES_ARE_SPECIFIED, facialInfo.getFeaturesMask());
        assertEquals(0, facialInfo.getExpressionMask());
//        assertArrayEquals(poseAngleUncertainty, facialInfo.getPoseAngleUncertainty());
    }

    /**
     * Tests FacialInformation constructor with all parameters
     */
    @Test
    public void constructor_allParameters_createsFacialInformation() {
        int noOfLandMarkPoints = 10;
        int gender = Gender.MALE;
        int eyeColor = EyeColour.BLUE;
        int hairColor = HairColour.BROWN;
        int subjectHeight = 180;
        int propertyMask = Features.FEATURES_ARE_SPECIFIED;
        int expressionMask = Expression.NEUTRAL;
        int[] poseAngle = {15, 25, 35};
        int[] poseAngleUncertainty = {3, 3, 3};

        FacialInformation facialInfo = new FacialInformation(noOfLandMarkPoints, gender, eyeColor,
            hairColor, subjectHeight, propertyMask, expressionMask, poseAngle, poseAngleUncertainty);

        assertNotNull(facialInfo);
        assertEquals(noOfLandMarkPoints, facialInfo.getNoOfLandMarkPoints());
        assertEquals(gender, facialInfo.getGender());
        assertEquals(eyeColor, facialInfo.getEyeColor());
        assertEquals(hairColor, facialInfo.getHairColor());
        assertEquals(subjectHeight, facialInfo.getSubjectHeight());
        assertEquals(propertyMask, facialInfo.getFeaturesMask());
        assertEquals(expressionMask, facialInfo.getExpressionMask());
//        assertArrayEquals(poseAngle, facialInfo.getPoseAngle());
    }

    /**
     * Tests FacialInformation constructor with DataInputStream
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStream_createsFacialInformation() throws Exception {
        byte[] testData = createFacialInformationData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FacialInformation facialInfo = new FacialInformation(inputStream);

        assertNotNull(facialInfo);
    }

    /**
     * Tests FacialInformation constructor with DataInputStream and onlyImageInformation flag
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsFacialInformation() throws Exception {
        byte[] testData = createFacialInformationData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FacialInformation facialInfo = new FacialInformation(inputStream, true);

        assertNotNull(facialInfo);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validFacialInformation_returnsCorrectLength() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});

        long recordLength = facialInfo.getRecordLength();

        assertEquals(17, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     * @throws Exception if writing fails
     */
    @Test
    public void writeObject_validFacialInformation_writesDataSuccessfully() throws Exception {
        FacialInformation facialInfo = new FacialInformation(8, Gender.FEMALE, EyeColour.GREEN,
            HairColour.BLACK, 165, Features.FEATURES_ARE_SPECIFIED, Expression.SMILE_OPEN,
            new int[]{5, 10, 15}, new int[]{2, 2, 2});
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        facialInfo.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(17, result.length);
    }

    /**
     * Tests setNoOfLandMarkPoints method sets value correctly
     */
    @Test
    public void setNoOfLandMarkPoints_validCount_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newCount = 12;

        facialInfo.setNoOfLandMarkPoints(newCount);

        assertEquals(newCount, facialInfo.getNoOfLandMarkPoints());
    }

    /**
     * Tests setGender method sets value correctly
     */
    @Test
    public void setGender_validGender_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newGender = Gender.FEMALE;

        facialInfo.setGender(newGender);

        assertEquals(newGender, facialInfo.getGender());
    }

    /**
     * Tests setEyeColor method sets value correctly
     */
    @Test
    public void setEyeColor_validColor_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newColor = EyeColour.BROWN;

        facialInfo.setEyeColor(newColor);

        assertEquals(newColor, facialInfo.getEyeColor());
    }

    /**
     * Tests setHairColor method sets value correctly
     */
    @Test
    public void setHairColor_validColor_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newColor = HairColour.BLONDE;

        facialInfo.setHairColor(newColor);

        assertEquals(newColor, facialInfo.getHairColor());
    }

    /**
     * Tests setSubjectHeight method sets value correctly
     */
    @Test
    public void setSubjectHeight_validHeight_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newHeight = 175;

        facialInfo.setSubjectHeight(newHeight);

        assertEquals(newHeight, facialInfo.getSubjectHeight());
    }

    /**
     * Tests setFeaturesMask method sets value correctly
     */
    @Test
    public void setFeaturesMask_validMask_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newMask = 0x123456;

        facialInfo.setFeaturesMask(newMask);

        assertEquals(newMask, facialInfo.getFeaturesMask());
    }

    /**
     * Tests setExpressionMask method sets value correctly
     */
    @Test
    public void setExpressionMask_validMask_setsValueCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int newMask = Expression.FROWNING;

        facialInfo.setExpressionMask(newMask);

        assertEquals(newMask, facialInfo.getExpressionMask());
    }

    /**
     * Tests setPoseAngle method sets values correctly
     */
    @Test
    public void setPoseAngle_validAngles_setsValuesCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int[] newAngles = {45, 90, 135};

        facialInfo.setPoseAngle(newAngles);

        assertArrayEquals(newAngles, facialInfo.getPoseAngle());
    }

    /**
     * Tests setPoseAngleUncertainty method sets values correctly
     */
    @Test
    public void setPoseAngleUncertainty_validUncertainties_setsValuesCorrectly() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});
        int[] newUncertainties = {1, 2, 3};

        facialInfo.setPoseAngleUncertainty(newUncertainties);

        assertArrayEquals(newUncertainties, facialInfo.getPoseAngleUncertainty());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validFacialInformation_returnsNonNullString() {
        FacialInformation facialInfo = new FacialInformation(5, new int[]{0, 0, 0}, new int[]{0, 0, 0});

        String result = facialInfo.toString();

        assertNotNull(result);
        assertTrue(result.contains("FacialInformation"));
    }

    private byte[] createFacialInformationData() {
        return new byte[]{
            0x00, 0x05,
            0x01, 0x02, 0x03, (byte) 0xB4,
            0x00, 0x00, 0x01,
            0x00, 0x00,
            0x0A, 0x14, 0x1E,
            0x02, 0x02, 0x02
        };
    }
}