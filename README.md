# Bio Utils

## Overview
Utility to convert ISO format to image and vice-a-versa.

## Supported ISO versions

| Modality    | ISO version     |
| ----------- | ----------------|
| Finger      | ISO19794_4_2011 |
| Iris        | ISO19794_6_2011 |
| Face        | ISO19794_5_2011 |

Note: JPEG2000 will be converted to JPEG.

## Sample code

To convert from ISO format to image:

```
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_4_2011");
convertRequestDto.setInputBytes(<ISO bytes>);  
byte[] image = FingerDecoder.convertFingerISOToImageBytes(convertRequestDto);
BufferedImage image = FingerDecoder.convertFingerISOToBufferedImage(convertRequestDto);
```

```
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_6_2011");
convertRequestDto.setInputBytes(<ISO bytes>);
byte[] image = IrisDecoder.convertIrisISOToImageBytes(convertRequestDto);
BufferedImage image = IrisDecoder.convertIrisISOToBufferedImage(convertRequestDto);
```

```
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_5_2011");
convertRequestDto.setInputBytes(<ISO bytes>);
byte[] image = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
BufferedImage image = FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto);
```
  
