# Kernel Biometrics API

## Overview

Core **SPI contracts**, **entities**, and **constants** for biometric operations in MOSIP. Downstream providers (`kernel-biosdk-provider`, vendor SDKs) and CBEFF utilities implement or consume these types.

Maven coordinates: `io.mosip.kernel:kernel-biometrics-api`

## Standards

| Concept | Mapping |
| ------- | ------- |
| BIR / biometric record | Aligns with ISO/IEC 19785 CBEFF structures |
| Modalities | `BiometricType`: `FINGER`, `IRIS`, `FACE` |
| SPI functions | `BiometricFunction` (match, extract, quality check, …) |

## Features

- Service Provider Interfaces for quality check, match, extract, segment, and format conversion
- Shared data model: `BIR`, `BiometricRecord`, `BDBInfo`, `Response<T>`, …
- CBEFF validation helpers (`CbeffValidator`)
- Jackson 2 deserializers for biometric payloads (via `spring-boot-jackson2` under Boot 4)

## API surface

| Type | Purpose |
| ---- | ------- |
| `IBioApi` | Legacy SPI (`convertFormat` deprecated since 1.2.1) |
| `IBioApiV2` | Preferred SPI — use `convertFormatV2` |
| `CbeffUtil` | SPI for CBEFF XML create/update/validate/extract |
| `BiometricType` / `BiometricFunction` | Modality and function enums |
| `Response<T>` | Status + typed payload wrapper |

## Prerequisites

- JDK 21
- Maven 3.9+
- `io.mosip.kernel:kernel-core:1.4.1-SNAPSHOT`

## Build

```text
mvn clean install -Dgpg.skip=true -pl kernel-biometrics-api -am
```

## Configuration / Database / Docker

Not applicable — API definition library only.

## Contribution & Community

- [Code contributions](https://docs.mosip.io/1.2.0/community/code-contributions)
- [MOSIP Community](https://community.mosip.io/)
- [Issues](https://github.com/mosip/bio-utils/issues)

## License

Licensed under the [Mozilla Public License 2.0](../LICENSE).
