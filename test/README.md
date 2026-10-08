# Biometrics Util Samples

## Overview

Sample CLI applications demonstrating `biometrics-util`. This module is **not** in the Maven reactor and is **not** published to Maven Central.

Local coordinates: `io.mosip.bio.utils:bioutils`

Sample data lives under `BiometricInfo/` (Face, Finger, Iris, NistXmlData, NistDataQualityAnalyser). Paths are resolved as:

```text
{cwd} + folderPath + fileName
```

Always run from the **`test/`** directory so `/BiometricInfo/...` resolves correctly.

---

## How to run all samples

### 1. Prerequisites

- JDK 21
- Maven 3.9+
- Commons `kernel-core` **1.4.1-SNAPSHOT** installed (or available from your snapshot repo)

### 2. Build dependencies + this module

From repository root:

```text
mvn clean install -Dgpg.skip=true -pl biometrics-util -am
cd test
mvn clean package -Dgpg.skip=true
```

### 3. Prepare classpath (`target/` + `lib/`)

Still in `test/`:

```text
mvn -q dependency:copy-dependencies -DoutputDirectory=target/lib -DincludeScope=runtime
```

Optional: override jar version (default `1.4.1-SNAPSHOT`):

```text
# Windows
set BIOUTILS_VERSION=1.4.1-SNAPSHOT

# Linux / macOS
export BIOUTILS_VERSION=1.4.1-SNAPSHOT
```

### 4. Run samples via `.bat` (Windows) or `.sh` (Linux / macOS)

Working directory: **`test/`**. Each sample has a matching pair (`run_*.bat` / `run_*.sh`). Classpath prefers `target/bioutils-$VER.jar` + `target/lib/*`, and falls back to flat `bioutils-$VER.jar` + `lib/*`.

On Unix: `chmod +x *.sh` once.

#### Windows

```bat
REM Face / Iris / Finger — ISO → image
run_decoder_face.bat
run_decoder_iris.bat
run_decoder_finger_jp2000.bat
run_decoder_finger_wsq.bat

REM Face / Iris / Finger — image → ISO
run_encoder_face_registration.bat
run_encoder_face_auth.bat
run_encoder_iris_registration.bat
run_encoder_iris_auth.bat
run_encoder_finger_jp2000_registration.bat
run_encoder_finger_jp2000_auth.bat
run_encoder_finger_wsq_auth.bat

REM Convert ISO image codec (JP2000/WSQ → JPEG/PNG)
run_convert_face_JP2000_JPEG.bat
run_convert_face_JP2000_PNG.bat
run_convert_iris_JP2000_JPEG.bat
run_convert_iris_JP2000_PNG.bat
run_convert_finger_JP2000_JPEG.bat
run_convert_finger_JP2000_PNG.bat
run_convert_finger_WSQ_JPEG.bat
run_convert_finger_WSQ_PNG.bat
run_convert_finger_JP2000_WSQ.bat
run_convert_finger_JP2000_ISO_WSQ.bat
run_encoder_finger_jp2000_wsq_auth.bat

REM NIST / JP2000 / rotate
run_nist_file_reader.bat
run_decoder_jp2000.bat
run_rotate_face.bat
run_rotate_finger.bat
run_rotate_iris.bat

REM Legacy sample (face JP2000 → ISO)
run.bat
```

Optional (need extra inputs / services):

```bat
run_bio_auth_decoder.bat
run_nist_data_quality_analyser.bat
```

#### Linux / macOS

Same names with `.sh`:

```sh
./run_decoder_face.sh
./run_encoder_face_registration.sh
./run_convert_face_JP2000_JPEG.sh
./run_nist_file_reader.sh
./run_decoder_jp2000.sh
./run_rotate_face.sh
./run.sh
# optional:
./run_bio_auth_decoder.sh
./run_nist_data_quality_analyser.sh
```

Run a batch of samples:

```sh
for s in run_decoder_*.sh run_encoder_*.sh run_convert_*.sh run_nist_file_reader.sh run_decoder_jp2000.sh; do
  echo "=== $s ==="
  ./"$s" || exit 1
done
```

### 5. Run via Maven `exec:java` (alternative)

From `test/` after `mvn package`:

```text
mvn -q exec:java -Dexec.classpathScope=runtime ^
  -Dexec.mainClass=io.mosip.biometrics.util.test.SampleNistFileReader ^
  -Dexec.args="mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistXmlData/"
```

Repeat with each `mainClass` / args from the tables below.

---

## Unit tests (reactor libraries — not this module)

This `test/` folder has **no Surefire unit tests**. To run library unit tests (JaCoCo ≥85%):

```text
cd ..
mvn clean test -Dgpg.skip=true
# or one module:
mvn test -Dgpg.skip=true -pl biometrics-util
mvn test -Dgpg.skip=true -pl kernel-biometrics-api
mvn test -Dgpg.skip=true -pl kernel-cbeffutil-api
mvn test -Dgpg.skip=true -pl kernel-biosdk-provider
```

---

## Applications

Prefer the `.bat` / `.sh` runners above. Manual `java -cp` examples below use Unix classpath (`:`); on Windows use `;` instead (`target\bioutils-<version>.jar;target\lib\*`).

| Application | Purpose |
| ----------- | ------- |
| `BioUtilApplication` | Decode ISO → image or encode image → ISO (Finger / Iris / Face) |
| `BioUtilConvertApplication` | Convert image codec inside ISO (JP2000/WSQ → JPEG/PNG) |
| `Jp2ToWsqApplication` | Convert finger JP2000 (raw or ISO payload) → WSQ image; decode with jnbis |
| `BioAuthDecoderValueCreaterApplication` | Decode auth biometric payload (Salt, AAD, encoded data) |
| `SampleNistFileReader` | Parse NIST ITL XML sample files |
| `NistDataQualityAnalyser` | NIST quality analysis (HTTP BQAT) → CSV |
| `Jp2000DecodeApplication` | Dump JPEG2000 metadata / byte-wise decode |
| `ImageRotateApplication` | Rotate JP2 samples |

### Argument key reference

| Key | Values |
| --- | ------ |
| `io.mosip.biometrics.util.image.type.jp2000` | `0` |
| `io.mosip.biometrics.util.image.type.wsq` | `1` |
| `io.mosip.biometrics.util.image.type.jpeg` | `2` |
| `io.mosip.biometrics.util.image.type.png` | `3` |
| `io.mosip.biometrics.util.convert.iso.to.image` | `1` = decode ISO → image |
| `io.mosip.biometrics.util.convert.image.to.iso` | `0` = encode image → ISO |
| `io.mosip.biometrics.util.purpose.auth` | `AUTH` |
| `io.mosip.biometrics.util.purpose.registration` | `REGISTRATION` |

Folder path values must contain `Face`, `Iris`, `Finger`, or `Nist` (substring check in code).

---

## BioAuthDecoderValueCreaterApplication

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioAuthDecoderValueCreaterApplication
"mosip.mock.sbi.biometric.transaction.id=SBI1069-208"
"mosip.mock.sbi.biometric.time.stamp=2023-01-03T05:56:45Z"
"mosip.mock.sbi.biometric.thumb.print=2F6FB5590B21E9526F8E4A23B5CC961021614C236D517794F0A7F29E5BA32C2C"
"mosip.mock.sbi.biometric.session.key=<session-key>"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_encyrpted.iso"
```

## BioUtilApplication

Decode ISO → JPEG, or encode JP2000/WSQ → ISO.

### Face — Decoder

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

### Face — Encoder (Auth)

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_auth.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.auth=AUTH"
```

### Face — Encoder (Registration)

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_registration.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

### Iris — Decoder

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.iso=info_right_registration.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

Encoder variants: same pattern with iris folder, `info_left_auth.jp2` / `info_right_registration.jp2`, subtypes `Left` / `Right`, purpose `AUTH` or `REGISTRATION`.

### Finger — Decoder (JP2000 / WSQ)

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_index_registration_jp2000.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilApplication
"io.mosip.biometrics.util.image.type.wsq=1"
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_thumb_auth_wsq.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

## BioUtilConvertApplication

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.jpeg=2"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso"
```

Use `image.type.png=3` for PNG. Same pattern for Iris / Finger.

## SampleNistFileReader

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.SampleNistFileReader
"mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistXmlData/"
```

## NistDataQualityAnalyser

Requires a reachable BQAT HTTP service.

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.NistDataQualityAnalyser
"mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistDataQualityAnalyser/"
"bqat.server.ipaddress=<host>"
"bqat.server.port=:<port>"
"bqat.server.path=/base64?urlsafe=false"
"bqat.content.type=application/json"
"bqat.content.charset=utf-8"
"bqat.json.results=results"
```

## Jp2ToWsqApplication

Converts a finger JP2000 image to both **lossy** WSQ (`CommonUtil.convertJP2ToWSQ`, FBI 0.75 bpp) and **8-bit lossless** WSQ (`CommonUtil.convertJP2ToWSQLossless`). Writes `*.wsq` and `*.lossless.wsq` next to the source, and round-trips both with jnbis.

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.Jp2ToWsqApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.image=info_left_index_auth.jp2"
```

ISO payload (extracts JP2000 from `info_left_index_auth_jp2000.iso`):

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.Jp2ToWsqApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_index_auth_jp2000.iso"
```

After `run_convert_finger_JP2000_WSQ`, wrap the WSQ as AUTH ISO:

```bat
run_encoder_finger_jp2000_wsq_auth.bat
```

## Jp2000DecodeApplication / ImageRotateApplication

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.Jp2000DecodeApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_registration.jp2"
```

```text
java -cp target/bioutils-<version>.jar:target/lib/* io.mosip.biometrics.util.test.ImageRotateApplication
"io.mosip.biometrics.util.image.type.jp2000=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_registration.jp2"
"io.mosip.biometrics.image.rotation=90"
```

---

## Standards exercised

| Standard | Used by |
| -------- | ------- |
| ISO/IEC 19794-4/5/6:2011 | `BioUtilApplication`, `BioUtilConvertApplication` |
| NIST ITL 1-2011 | `SampleNistFileReader`, `NistDataQualityAnalyser` |

## License

[Mozilla Public License 2.0](../LICENSE).
