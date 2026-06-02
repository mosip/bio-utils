# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build all modules (skip GPG signing for local dev)
mvn clean install -Dgpg.skip=true

# Build a single module
mvn clean install -Dgpg.skip=true -pl biometrics-util
mvn clean install -Dgpg.skip=true -pl kernel-biometrics-api
mvn clean install -Dgpg.skip=true -pl kernel-cbeffutil-api
mvn clean install -Dgpg.skip=true -pl kernel-biosdk-provider

# Run all tests
mvn test

# Run tests for a single module
mvn test -pl biometrics-util

# Run a specific test class
mvn test -pl biometrics-util -Dtest=FingerDecoderTest

# Run with Sonar analysis
mvn verify sonar:sonar -Psonar

# Skip tests during build
mvn clean install -Dgpg.skip=true -DskipTests
```

Java 21 is required. All modules compile with `--enable-preview` enabled. The surefire plugin passes several `--add-opens` JVM flags for tests; these are already configured in each module's pom.xml.

## Architecture Overview

This is a Maven multi-module project (`groupId: io.mosip.biometrics`, version `1.4.0-SNAPSHOT`) providing biometric utility libraries for the [MOSIP](https://mosip.io) ecosystem. All modules are pure library JARs published to Maven Central (Sonatype OSS).

### Module Dependency Chain

```
kernel-biometrics-api         ← defines all SPIs, entities, constants, models
       ↑
kernel-cbeffutil-api          ← implements CbeffUtil SPI; depends on kernel-biometrics-api
kernel-biosdk-provider        ← implements iBioProviderApi; depends on kernel-biometrics-api
biometrics-util               ← standalone ISO↔image converters; no dependency on other modules here
test/                         ← sample CLI apps showing library usage (not a deployed artifact)
```

### kernel-biometrics-api

Defines the contracts everything else depends on:

- **`IBioApi`** (`spi/`) — core SPI: `init`, `checkQuality`, `match`, `extractTemplate`, `segment`, `convertFormat` (deprecated since 1.2.1)
- **`IBioApiV2`** extends `IBioApi` — adds `convertFormatV2` (use this instead of deprecated `convertFormat`)
- **`CbeffUtil`** (`spi/`) — SPI for CBEFF XML operations: `createXML`, `updateXML`, `validateXML`, `getBIRDataFromXML`, `getAllBDBData`, etc.
- **`BIR`** / **`BiometricRecord`** (`entities/`) — core data model; `BIR` = Biometric Information Record, `BiometricRecord` wraps a list of BIRs
- **`BiometricType`** (`constant/`) — `FINGER`, `IRIS`, `FACE`
- **`BiometricFunction`** (`constant/`) — enum of functions the SDK can perform
- **`Response<T>`** (`model/`) — generic wrapper returned by all `IBioApi` methods; contains status code and typed payload
- **`CbeffValidator`** (`commons/`) — static utility for XML schema validation and BIR extraction

### kernel-cbeffutil-api

Single implementation of the CBEFF standard:

- **`CbeffImpl`** — Spring `@Component` implementing `CbeffUtil`; loads its XSD schema at startup from a config server URL (`mosip.kernel.xsdstorage-uri` + `mosip.kernel.xsdfile`)
- **`CbeffContainerImpl`** — handles XML serialization/deserialization of BIR lists; wraps the JAXB-based container
- **`CbeffContainerI`** — interface for the container

### kernel-biosdk-provider

Bridges MOSIP kernel to external biometric SDKs via reflection. Does not call SDK directly — it uses reflection to invoke SDK methods by configured class names.

- **`iBioProviderApi`** (`spi/`) — internal SPI; each `BioProviderImpl_V_X_X` implements this per SDK protocol version
- **`BioProviderImpl_V_0_7`**, **`_V_0_8`**, **`_V_0_9`** — three concrete implementations; each handles a different SDK API version. The V_0_9 variant is the most current
- **`BioAPIFactory`** — Spring `@Component` with `@ConfigurationProperties(prefix = "mosip.biometric.sdk.providers")`; reads per-vendor per-modality config at startup, initializes providers, and builds a registry `Map<BiometricType, Map<BiometricFunction, iBioProviderApi>>`
- Configuration keys follow the pattern: `mosip.biometric.sdk.providers.finger.<vendorId>.*`, `.iris.<vendorId>.*`, `.face.<vendorId>.*`
- Uses H2 in-memory database and Spring Boot JPA (declared as dependencies but used by the SDK provider framework, not this library itself)

### biometrics-util

Standalone utility for ISO biometric format ↔ image conversion. No Spring dependency; purely functional static methods.

- **ISO standards supported**: ISO 19794-4:2011 (Finger), ISO 19794-6:2011 (Iris), ISO 19794-5:2011 (Face)
- **Decoders**: `FingerDecoder`, `IrisDecoder`, `FaceDecoder` — convert ISO binary (`byte[]`) → JPEG/PNG `byte[]` or `BufferedImage`
- **Encoders**: `FingerEncoder`, `IrisEncoder`, `FaceEncoder` — convert image → ISO binary
- **`CommonUtil`** — converts a Base64URL-encoded ISO biometric from one image type (JP2000, WSQ) to another (JPEG, PNG)
- **`ConvertRequestDto`** — DTO used as input to all decoders/encoders; holds `version`, `inputBytes`, `compressionRatio` (default 95 for JPEG quality)
- **`ISOStandardsValidator`** — validates ISO header fields across all modalities
- **NIST parser** (`nist/parser/v2011/`) — parses NIST ITL 1-2011 XML biometric files into a rich DTO hierarchy; used by the `test/` sample apps
- JPEG2000 support via `jai-imageio-jpeg2000`; WSQ support via `jnbis`; OpenCV via `openpnp.opencv`

### test/ module

Not a deployed artifact — contains standalone CLI sample applications demonstrating library usage: `BioUtilApplication` (decode/encode ISO↔image), `BioUtilConvertApplication` (convert image type within ISO), `SampleNistFileReader`, `NistDataQualityAnalyser`.

## Key Design Patterns

**SPI + reflection for SDK integration**: `kernel-biosdk-provider` never has a compile-time dependency on the actual biometric SDK JAR. SDK class names are passed as config properties; the provider uses reflection to call `init`, `verify`, `identify`, `extractTemplate` etc. This allows swapping SDKs without recompiling.

**CBEFF XML as the canonical biometric interchange format**: All biometric data exchanged between MOSIP components flows as CBEFF XML. The `BIR` entity maps directly to ISO/IEC 19785 CBEFF structures. The XSD for validation is loaded at runtime from a config server.

**`convertFormat` is deprecated**: Prefer `IBioApiV2.convertFormatV2` (returns `Response<BiometricRecord>`) over the old `IBioApi.convertFormat` (returns `BiometricRecord` directly with no error wrapping). The old method is marked `@Deprecated(since = "1.2.1", forRemoval = true)`.

## Sonar Coverage Exclusions

`biometrics-util` excludes from coverage: `constant/`, `exception/`, `nist/parser/v2011/`, and various ISO data model classes (e.g., `FaceBDIR`, `IrisBDIR`, `FingerBDIR`, `FeaturePoint`, `LandmarkPointType`). These are excluded because they are generated/data-only classes with no logic to test.