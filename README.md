[![Maven Package upon a push](https://github.com/mosip/bio-utils/actions/workflows/push_trigger.yml/badge.svg?branch=release-1.2.0)](https://github.com/mosip/bio-utils/actions/workflows/push_trigger.yml)
 [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=release-1.2.0&project=mosip_biometrics-util&id=mosip_biometrics-util&metric=alert_status)](https://sonarcloud.io/dashboard?id=mosip_biometrics-util)
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
