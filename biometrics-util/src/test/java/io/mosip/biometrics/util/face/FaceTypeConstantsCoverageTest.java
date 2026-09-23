package io.mosip.biometrics.util.face;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Coverage for face ISO constant/helper classes that previously had 0% instruction coverage.
 */
class FaceTypeConstantsCoverageTest {

    @Test
    void imageDataTypeCoversCtorValueFromValueAndToString() {
        ImageDataType type = new ImageDataType(ImageDataType.JPEG2000_LOSS_LESS);
        assertEquals(ImageDataType.JPEG2000_LOSS_LESS, type.value());
        assertEquals(ImageDataType.PNG, ImageDataType.fromValue(ImageDataType.PNG));
        assertTrue(type.toString().contains(Integer.toHexString(ImageDataType.JPEG2000_LOSS_LESS)));
        assertThrows(IllegalArgumentException.class, () -> ImageDataType.fromValue(0x04));
    }

    @Test
    void faceImageTypeCoversCtorValueFromValueAndToString() {
        FaceImageType type = new FaceImageType(FaceImageType.BASIC);
        assertEquals(FaceImageType.BASIC, type.value());
        assertEquals(FaceImageType.BASIC, FaceImageType.fromValue(FaceImageType.BASIC));
        assertNotNull(type.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceImageType.fromValue(-1));
    }

    @Test
    void imageColourSpaceAndExpression() {
        ImageColourSpace colour = new ImageColourSpace(ImageColourSpace.BIT_24_RGB);
        assertEquals(ImageColourSpace.BIT_24_RGB, colour.value());
        assertEquals(ImageColourSpace.UNSPECIFIED, ImageColourSpace.fromValue(ImageColourSpace.UNSPECIFIED));
        assertNotNull(colour.toString());
        assertThrows(IllegalArgumentException.class, () -> ImageColourSpace.fromValue(99));

        Expression expression = new Expression(Expression.RAISED_EYEBROWS);
        assertEquals(Expression.RAISED_EYEBROWS, expression.value());
        assertEquals(Expression.NEUTRAL, Expression.fromValue(Expression.NEUTRAL));
        assertNotNull(expression.toString());
        assertThrows(IllegalArgumentException.class, () -> Expression.fromValue(-1));
    }

    @Test
    void faceVersionFormatAndDeviceConstants() {
        FaceVersionNumber version = new FaceVersionNumber((int) FaceVersionNumber.VERSION_030);
        assertEquals((int) FaceVersionNumber.VERSION_030, version.value());
        assertEquals(FaceVersionNumber.VERSION_030, FaceVersionNumber.fromValue((int) FaceVersionNumber.VERSION_030));
        assertNotNull(version.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceVersionNumber.fromValue(0));

        FaceFormatIdentifier format = new FaceFormatIdentifier((int) FaceFormatIdentifier.FORMAT_FAC);
        assertEquals((int) FaceFormatIdentifier.FORMAT_FAC, format.value());
        assertEquals(FaceFormatIdentifier.FORMAT_FAC, FaceFormatIdentifier.fromValue(FaceFormatIdentifier.FORMAT_FAC));
        assertNotNull(format.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceFormatIdentifier.fromValue(0));

        FaceCaptureDeviceTechnology tech = new FaceCaptureDeviceTechnology(FaceCaptureDeviceTechnology.STATIC_PHOTO_DIGITAL_CAMERA);
        assertEquals(FaceCaptureDeviceTechnology.STATIC_PHOTO_DIGITAL_CAMERA, tech.value());
        assertEquals(FaceCaptureDeviceTechnology.VENDOR_80, FaceCaptureDeviceTechnology.fromValue(FaceCaptureDeviceTechnology.VENDOR_80));
        assertNotNull(tech.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceCaptureDeviceTechnology.fromValue(0x07));

        FaceCaptureDeviceVendor vendor = new FaceCaptureDeviceVendor(FaceCaptureDeviceVendor.UNSPECIFIED);
        assertEquals(FaceCaptureDeviceVendor.UNSPECIFIED, vendor.value());
        assertEquals(FaceCaptureDeviceVendor.VENDOR_FFFF, FaceCaptureDeviceVendor.fromValue(FaceCaptureDeviceVendor.VENDOR_FFFF));
        assertNotNull(vendor.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceCaptureDeviceVendor.fromValue(-1));

        FaceCaptureDeviceType deviceType = new FaceCaptureDeviceType(FaceCaptureDeviceType.UNSPECIFIED);
        assertEquals(FaceCaptureDeviceType.UNSPECIFIED, deviceType.value());
        assertEquals(FaceCaptureDeviceType.VENDOR_FFFF, FaceCaptureDeviceType.fromValue(FaceCaptureDeviceType.VENDOR_FFFF));
        assertNotNull(deviceType.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceCaptureDeviceType.fromValue(-1));
    }

    @Test
    void qualityVendorHeightAndPostAcquisition() {
        FaceQualityAlgorithmVendorIdentifier qualityVendor =
                new FaceQualityAlgorithmVendorIdentifier(FaceQualityAlgorithmVendorIdentifier.UNSPECIFIED);
        assertEquals(FaceQualityAlgorithmVendorIdentifier.UNSPECIFIED, qualityVendor.value());
        assertEquals(FaceQualityAlgorithmVendorIdentifier.VENDOR_FFFF,
                FaceQualityAlgorithmVendorIdentifier.fromValue(FaceQualityAlgorithmVendorIdentifier.VENDOR_FFFF));
        assertNotNull(qualityVendor.toString());
        assertThrows(IllegalArgumentException.class, () -> FaceQualityAlgorithmVendorIdentifier.fromValue(-1));

        HeightCodes height = new HeightCodes(HeightCodes.UNSPECIFIED);
        assertEquals(HeightCodes.UNSPECIFIED, height.value());
        assertEquals(HeightCodes.UNSPECIFIED, HeightCodes.fromValue(HeightCodes.UNSPECIFIED));
        assertNotNull(height.toString());
        assertThrows(IllegalArgumentException.class, () -> HeightCodes.fromValue(-1));

        PostAcquisitionProcessingTypes post = new PostAcquisitionProcessingTypes(0);
        assertEquals(0, post.value());
        assertEquals(0, PostAcquisitionProcessingTypes.fromValue(0));
        assertNotNull(post.toString());
        assertThrows(IllegalArgumentException.class, () -> PostAcquisitionProcessingTypes.fromValue(-1));
    }
}
