package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ImageTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsImageType() {
        ImageType imageType = new ImageType(ImageType.UNCROPPED);

        assertEquals(ImageType.UNCROPPED, imageType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validImageType_returnsCorrectValue() {
        ImageType imageType = new ImageType(ImageType.VGA);

        int result = imageType.value();

        assertEquals(ImageType.VGA, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = ImageType.fromValue(ImageType.UNCROPPED);

        assertEquals(ImageType.UNCROPPED, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = ImageType.fromValue(ImageType.CROPPED_AND_MASKED);

        assertEquals(ImageType.CROPPED_AND_MASKED, result);
    }

    /**
     * Tests fromValue method with valid cropped value
     */
    @Test
    public void fromValue_validCroppedValue_returnsValue() {
        int result = ImageType.fromValue(ImageType.CROPPED);

        assertEquals(ImageType.CROPPED, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        ImageType.fromValue(0x00);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        ImageType.fromValue(0x08);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validImageType_returnsFormattedString() {
        ImageType imageType = new ImageType(ImageType.CROPPED_AND_MASKED);

        String result = imageType.toString();

        assertNotNull(result);
        assertTrue(result.contains("7"));
    }
}