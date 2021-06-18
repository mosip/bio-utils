package io.mosip.biometrics.util;

import io.mosip.biometrics.util.face.FaceDecoder;
import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    public static BufferedImage getBufferedImage(ConvertRequestDto convertRequestDto) throws Exception {
        BufferedImage bufferedImage = null;
        switch (convertRequestDto.getImageType()) {
            case 0://JP2000
                bufferedImage = ImageIO.read(new ByteArrayInputStream(convertRequestDto.getInputBytes()));
                break;
            case 1://WSQ
                WsqDecoder decoder = new WsqDecoder ();
                Bitmap bitmap = decoder.decode(convertRequestDto.getInputBytes());
                bufferedImage = convert(bitmap);
                break;
        }
        return bufferedImage;
    }

    private static BufferedImage convert(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] data = bitmap.getPixels();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, width, height, data);
        return image;
    }

    public static byte[] convertJP2ToJPEGBytes(byte[] jp2000Bytes) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(jp2000Bytes)), "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Failed to get jpg image", e);
        }
        return null;
    }
}
