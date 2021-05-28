package io.mosip.biometrics.util;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;

public class CommonUtil {

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
}
