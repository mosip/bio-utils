# Biometrics Util Samples

## Overview

Sample CLI applications demonstrating `biometrics-util` usage. This module is **not** part of the Maven reactor and is **not** published to Maven Central.

Maven coordinates (local only): `io.mosip.bio.utils:bioutils`

## Prerequisites

- JDK 21
- Build `biometrics-util` (and parent reactor) first:

  ```text
  cd ..
  mvn clean install -Dgpg.skip=true -pl biometrics-util -am
  ```

- Build this module:

  ```text
  mvn clean package -Dgpg.skip=true
  ```

## Applications

| Application | Purpose |
| ----------- | ------- |
| `BioUtilApplication` | Decode ISO → image or encode image → ISO (Finger / Iris / Face) |
| `BioUtilConvertApplication` | Convert image codec inside ISO (JP2000/WSQ → JPEG/PNG) |
| `BioAuthDecoderValueCreaterApplication` | Decode auth biometric payload (Salt, AAD, encoded data) |
| `SampleNistFileReader` | Parse NIST ITL XML sample files |
| `NistDataQualityAnalyser` | NIST quality analysis helper (calls external BQAT service) |

## BioAuthDecoderValueCreaterApplication

Decodes auth biometric value fields (Salt, AAD, Encoded Data).

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioAuthDecoderValueCreaterApplication
"mosip.mock.sbi.biometric.transaction.id=SBI1069-208"
"mosip.mock.sbi.biometric.time.stamp=2023-01-03T05:56:45Z"
"mosip.mock.sbi.biometric.thumb.print=2F6FB5590B21E9526F8E4A23B5CC961021614C236D517794F0A7F29E5BA32C2C"
"mosip.mock.sbi.biometric.session.key=<session-key>"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_encyrpted.iso"
```

## BioUtilApplication

Decode an ISO file to JPEG, or encode JP2000/WSQ images into ISO.

### Face

#### Decoder

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

#### Encoder (Auth)

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_auth.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.auth=AUTH"
```

#### Encoder (Registration)

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_registration.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

### Iris

#### Decoder

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.iso=info_right_registration.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

#### Encoder (Auth / Registration)

Use the same pattern as Face, with iris folder paths and `info_left_auth.jp2` / `info_right_registration.jp2`, subtypes `Left` / `Right`, and purpose `AUTH` or `REGISTRATION`.

### Finger

Supports JP2000 and WSQ. Examples:

#### Decoder (JP2000)

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_index_registration_jp2000.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

#### Decoder (WSQ)

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.wsq=1"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_thumb_auth_wsq.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

Encoder variants (Auth/Registration × JP2000/WSQ) follow the same property pattern as Face/Iris.

## BioUtilConvertApplication

Convert ISO containing JP2000 or WSQ to ISO containing JPEG or PNG.

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.jpeg=2"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso"
```

Use `image.type.png=3` for PNG. Apply the same pattern for Iris and Finger (including WSQ sources).

## SampleNistFileReader

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.SampleNistFileReader
"mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistXmlData/"
```

## NistDataQualityAnalyser

Parses NIST files, runs quality analysis (via BQAT HTTP service), writes CSV results.

```text
java -cp bioutils-1.4.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.NistDataQualityAnalyser
"mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistDataQualityAnalyser/"
"bqat.server.ipaddress=<host>"
"bqat.server.port=:<port>"
"bqat.server.path=/base64?urlsafe=false"
"bqat.content.type=application/json"
"bqat.content.charset=utf-8"
"bqat.json.results=results"
```

## Standards exercised

| Standard | Used by |
| -------- | ------- |
| ISO/IEC 19794-4/5/6:2011 | `BioUtilApplication` / `BioUtilConvertApplication` |
| NIST ITL 1-2011 | `SampleNistFileReader`, `NistDataQualityAnalyser` |

## License

Same as the repository: [Mozilla Public License 2.0](../LICENSE).
