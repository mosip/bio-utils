# Biometrics Util

## Overview

Standalone utility library to convert biometric data between **ISO/IEC 19794** binary formats and **image** representations (`byte[]` JPEG/PNG or `BufferedImage`). No Spring dependency — pure static helpers for use from any MOSIP service or CLI.

Maven coordinates: `io.mosip.biometric.util:biometrics-util`

## Standards

| Modality | Standard | Version string in API |
| -------- | -------- | --------------------- |
| Finger | ISO/IEC 19794-4:2011 | `ISO19794_4_2011` |
| Iris | ISO/IEC 19794-6:2011 | `ISO19794_6_2011` |
| Face | ISO/IEC 19794-5:2011 | `ISO19794_5_2011` |

Supporting libraries:

- JPEG2000 — `jai-imageio-jpeg2000`
- WSQ — `jnbis`
- Image ops — `org.openpnp:opencv`
- NIST ITL 1-2011 XML — parser under `nist/parser/v2011/`

JPEG2000 payloads are typically converted to JPEG for interoperability.

## Features

- Encode image → ISO biometric binary
- Decode ISO binary → image bytes or `BufferedImage`
- Convert image codec inside an ISO container (JP2000/WSQ → JPEG/PNG) via `CommonUtil`
- Header / standards validation helpers (`ISOStandardsValidator`)

## API surface

| Class | Purpose |
| ----- | ------- |
| `FingerDecoder` / `FingerEncoder` | Finger ISO ↔ image |
| `IrisDecoder` / `IrisEncoder` | Iris ISO ↔ image |
| `FaceDecoder` / `FaceEncoder` | Face ISO ↔ image |
| `CommonUtil` | Base64URL ISO image-type conversion; NIST helpers |
| `ConvertRequestDto` | Shared input DTO (`version`, `inputBytes`, `compressionRatio`, …) |

## Sample code

### Finger ISO → image

```java
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_4_2011");
convertRequestDto.setInputBytes(/* ISO bytes */);
convertRequestDto.setCompressionRatio(95); // JPEG quality 0–100; default 95

byte[] imageBytes = FingerDecoder.convertFingerISOToImageBytes(convertRequestDto);
BufferedImage image = FingerDecoder.convertFingerISOToBufferedImage(convertRequestDto);
```

### Iris ISO → image

```java
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_6_2011");
convertRequestDto.setInputBytes(/* ISO bytes */);
convertRequestDto.setCompressionRatio(95);

byte[] imageBytes = IrisDecoder.convertIrisISOToImageBytes(convertRequestDto);
BufferedImage image = IrisDecoder.convertIrisISOToBufferedImage(convertRequestDto);
```

### Face ISO → image

```java
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_5_2011");
convertRequestDto.setInputBytes(/* ISO bytes */);
convertRequestDto.setCompressionRatio(95);

byte[] imageBytes = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
BufferedImage image = FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto);
```

### Convert ISO image type (Base64URL)

```java
CommonUtil.convertISOImageType(
    String inIsoData,
    Modality modality,
    ImageType imageType
) throws Exception;
```

## Prerequisites

- JDK 21
- Maven 3.9+
- Parent reactor built (or `kernel-core` 1.4.1-SNAPSHOT available)

## Build

From repository root:

```text
mvn clean install -Dgpg.skip=true -pl biometrics-util -am
```

## Configuration / Database / Docker

Not applicable — library module only. No external config, database, or container image.

## Samples

CLI demos live in the [test](../test/README.md) module (not published to Maven Central).

## Contribution & Community

- [Code contributions](https://docs.mosip.io/1.2.0/community/code-contributions)
- [MOSIP Community](https://community.mosip.io/)
- [Issues](https://github.com/mosip/bio-utils/issues)

## License

Licensed under the [Mozilla Public License 2.0](../LICENSE).
