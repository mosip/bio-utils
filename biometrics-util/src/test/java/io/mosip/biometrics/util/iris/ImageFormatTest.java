package io.mosip.biometrics.util.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ImageFormatTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructorCreatesImageFormatCorrectly() {
        ImageFormat imageFormat = new ImageFormat(ImageFormat.MONO_RAW);

        assertEquals(ImageFormat.MONO_RAW, imageFormat.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void valueReturnsCorrectValue() {
        ImageFormat imageFormat = new ImageFormat(ImageFormat.RGB_JPEG);

        int result = imageFormat.value();

        assertEquals(ImageFormat.RGB_JPEG, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        int result = ImageFormat.fromValue(ImageFormat.MONO_RAW);

        assertEquals(ImageFormat.MONO_RAW, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        int result = ImageFormat.fromValue(ImageFormat.RGB_PNG);

        assertEquals(ImageFormat.RGB_PNG, result);
    }

    /**
     * Tests fromValue method with valid middle value
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        int result = ImageFormat.fromValue(ImageFormat.MONO_JPEG2000);

        assertEquals(ImageFormat.MONO_JPEG2000, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        ImageFormat.fromValue(0x01);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        ImageFormat.fromValue(0x11);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toStringReturnsFormattedString() {
        ImageFormat imageFormat = new ImageFormat(ImageFormat.RGB_JPEG2000);

        String result = imageFormat.toString();

        assertNotNull(result);
        assertTrue(result.contains("c"));
    }
}