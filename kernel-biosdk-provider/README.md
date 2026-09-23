# Kernel BioSDK Provider

## Overview

Reflection-based **bridge** between MOSIP biometric SPIs and external vendor biometric SDKs. Provider class names and modality settings come from configuration — there is **no compile-time dependency** on vendor SDK JARs.

Maven coordinates: `io.mosip.kernel:kernel-biosdk-provider`

## Standards & contracts

| Layer | Detail |
| ----- | ------ |
| SPI | Implements internal `iBioProviderApi`; consumes `kernel-biometrics-api` types |
| Protocol versions | `BioProviderImpl_V_0_7`, `_V_0_8`, `_V_0_9` (V0.9 is current) |
| Modalities | Finger, Iris, Face via `BiometricType` / `BiometricFunction` |

## Features

- Pluggable vendor SDKs via `Class.forName` and configured class names
- Registry of providers per modality and biometric function
- Spring Boot integration (`BioAPIFactory` with `@ConfigurationProperties`)
- Logging via APIs packaged in commons `kernel-core` (not a separate logger artifact)

## API surface

| Type | Purpose |
| ---- | ------- |
| `BioAPIFactory` | Loads `mosip.biometric.sdk.providers.*` and builds the provider registry |
| `BioProviderImpl_V_0_7` / `_V_0_8` / `_V_0_9` | Versioned SDK protocol adapters |
| `iBioProviderApi` | Internal provider SPI |

## Prerequisites

- JDK 21
- Maven 3.9+
- Vendor SDK JAR(s) available on the **runtime** classpath of the host service
- `kernel-biometrics-api` and `kernel-core`

## Build

```text
mvn clean install -Dgpg.skip=true -pl kernel-biosdk-provider -am
```

## Configuration

Prefix: `mosip.biometric.sdk.providers`

Typical pattern:

```text
mosip.biometric.sdk.providers.finger.<vendorId>.*
mosip.biometric.sdk.providers.iris.<vendorId>.*
mosip.biometric.sdk.providers.face.<vendorId>.*
```

Exact keys (classname, version, etc.) are defined by the host MOSIP service and vendor adapter documentation.

## Database / Docker

Optional H2 / JPA is declared for the provider framework in library form. No standalone DB setup or Docker image for this module.

## Contribution & Community

- [Code contributions](https://docs.mosip.io/1.2.0/community/code-contributions)
- [MOSIP Community](https://community.mosip.io/)
- [Issues](https://github.com/mosip/bio-utils/issues)

## License

Licensed under the [Mozilla Public License 2.0](../LICENSE).
