# Bio Utils

[![Maven Package upon a push](https://github.com/mosip/bio-utils/actions/workflows/push-trigger.yml/badge.svg?branch=develop)](https://github.com/mosip/bio-utils/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=develop&project=mosip_biometrics-util&metric=alert_status)](https://sonarcloud.io/dashboard?branch=develop&id=mosip_biometrics-util)
[![License: MPL 2.0](https://img.shields.io/badge/License-MPL_2.0-brightgreen.svg)](LICENSE)

## Overview

**Bio Utils** is the shared biometric library set for the [MOSIP](https://mosip.io) platform. It provides reusable Maven artifacts for ISO biometric encode/decode, CBEFF XML handling, biometric SPI contracts, and a reflection-based bridge to vendor biometric SDKs.

This repository publishes **library JARs only** (no deployable REST services). Consuming MOSIP modules (registration, ID repository, authentication, and others) depend on these artifacts for consistent biometric processing.

Parent Maven coordinates: `io.mosip.biometrics:biometrics` (`1.4.1-SNAPSHOT`).

## Features

- **ISO biometric conversion:** Finger, Iris, and Face ISO/IEC 19794 ↔ JPEG/PNG/`BufferedImage`
- **CBEFF interchange:** Create, update, validate, and extract BIR XML (ISO/IEC 19785)
- **Biometric SPI:** `IBioApi` / `IBioApiV2`, `CbeffUtil`, shared entities and constants
- **BioSDK provider:** Config-driven, reflection-based integration with vendor SDKs (no compile-time vendor JAR)
- **NIST support:** NIST ITL 1-2011 XML parsing utilities (used by sample CLI apps)

## Standards

| Standard | Scope in this repo |
| -------- | ------------------ |
| [ISO/IEC 19794-4:2011](https://www.iso.org/standard/50867.html) | Finger image data interchange |
| [ISO/IEC 19794-5:2011](https://www.iso.org/standard/50866.html) | Face image data interchange |
| [ISO/IEC 19794-6:2011](https://www.iso.org/standard/51228.html) | Iris image data interchange |
| [ISO/IEC 19785](https://www.iso.org/standard/41047.html) (CBEFF) | Common Biometric Exchange Formats Framework / BIR XML |
| NIST ITL 1-2011 | XML biometric transaction parsing (utilities / samples) |

**Engineering standards (this reactor):**

| Item | Value |
| ---- | ----- |
| Java | 21 |
| Maven | 3.9+ |
| Spring Boot | **4.1.1** (via `spring-boot-starter-parent`) |
| BOM policy | **No `kernel-bom`** — versions pinned in the parent POM |
| Commons dependency | `io.mosip.kernel:kernel-core:1.4.1-SNAPSHOT` |
| JAXB for CBEFF | `javax.xml.bind` **2.3.x** (not Jakarta 4.x) |
| Jackson | Jackson **2** via `spring-boot-jackson2` (Boot 4 defaults to Jackson 3) |
| License | [Mozilla Public License 2.0](LICENSE) |

## Modules

Build order: API contracts first, then implementations; `biometrics-util` is independent of the CBEFF/BioSDK modules.

| Module | Artifact | Description |
| ------ | -------- | ----------- |
| [kernel-biometrics-api](kernel-biometrics-api/README.md) | `io.mosip.kernel:kernel-biometrics-api` | SPI, entities, constants (`IBioApi`, `IBioApiV2`, `CbeffUtil`, `BIR`) |
| [kernel-cbeffutil-api](kernel-cbeffutil-api/README.md) | `io.mosip.kernel:kernel-cbeffutil-api` | `CbeffImpl` — CBEFF XML create/update/validate |
| [kernel-biosdk-provider](kernel-biosdk-provider/README.md) | `io.mosip.kernel:kernel-biosdk-provider` | Reflection bridge to vendor BioSDKs (`BioProviderImpl` V0.7–V0.9) |
| [biometrics-util](biometrics-util/README.md) | `io.mosip.biometric.util:biometrics-util` | ISO ↔ image converters (no Spring) |
| [test](test/README.md) | `io.mosip.bio.utils:bioutils` | Sample CLI apps (**not** in the Maven reactor / not published) |

```text
kernel-biometrics-api
        ↑
        ├── kernel-cbeffutil-api
        └── kernel-biosdk-provider

biometrics-util   (standalone converters; depends on kernel-core only)
```

Prefer **`IBioApiV2.convertFormatV2`** over the deprecated `IBioApi.convertFormat`.

## Prerequisites

- **JDK:** 21
- **Maven:** 3.9.6 or higher
- **Git**
- **commons `kernel-core` 1.4.1-SNAPSHOT** installed locally (or available from your snapshot repo):

  ```text
  cd ../commons/kernel
  mvn clean install -Dgpg.skip=true
  ```

Do **not** introduce `kernel-bom`. Do **not** parent `kernel-parent` without overriding `kernel.core.version` (that parent ties it to `${project.version}`).

## Installation

### Clone

```text
git clone https://github.com/mosip/bio-utils.git
cd bio-utils
```

### Build all modules

```text
mvn clean install -Dgpg.skip=true
```

### Build a single module

```text
mvn clean install -Dgpg.skip=true -pl biometrics-util
mvn clean install -Dgpg.skip=true -pl kernel-biometrics-api
mvn clean install -Dgpg.skip=true -pl kernel-cbeffutil-api
mvn clean install -Dgpg.skip=true -pl kernel-biosdk-provider
```

### Run tests

```text
mvn test
mvn test -pl biometrics-util -Dtest=FingerDecoderTest
```

### Skip tests / GPG (local)

```text
mvn clean install -Dgpg.skip=true -DskipTests
```

### Consume as a dependency

Always declare `<version>` (pin via your own property — it will change across releases):

```xml
<properties>
  <mosip.biometrics.util.version>…</mosip.biometrics.util.version>
  <kernel.biometrics.api.version>…</kernel.biometrics.api.version>
  <kernel.cbeffutil.api.version>…</kernel.cbeffutil.api.version>
  <kernel.biosdk.provider.version>…</kernel.biosdk.provider.version>
</properties>

<dependency>
  <groupId>io.mosip.biometric.util</groupId>
  <artifactId>biometrics-util</artifactId>
  <version>${mosip.biometrics.util.version}</version>
</dependency>

<dependency>
  <groupId>io.mosip.kernel</groupId>
  <artifactId>kernel-biometrics-api</artifactId>
  <version>${kernel.biometrics.api.version}</version>
</dependency>

<dependency>
  <groupId>io.mosip.kernel</groupId>
  <artifactId>kernel-cbeffutil-api</artifactId>
  <version>${kernel.cbeffutil.api.version}</version>
</dependency>

<dependency>
  <groupId>io.mosip.kernel</groupId>
  <artifactId>kernel-biosdk-provider</artifactId>
  <version>${kernel.biosdk.provider.version}</version>
</dependency>
```

Inside this multi-module reactor only, sibling modules may omit `<version>` (inherited from the parent). External consumers must always keep `<version>`.

## Configuration

These are **libraries**, not standalone services.

| Module | Configuration notes |
| ------ | ------------------- |
| `biometrics-util` | None (pure static utilities) |
| `kernel-biometrics-api` | None (SPI / models only) |
| `kernel-cbeffutil-api` | Host app: `mosip.kernel.xsdstorage-uri`, `mosip.kernel.xsdfile` for CBEFF XSD |
| `kernel-biosdk-provider` | Host app: `mosip.biometric.sdk.providers.finger|iris|face.<vendorId>.*` |

## Database / Docker / Kubernetes

Not applicable for this repository — no deployable services, Helm charts, or DB scripts. Integration and deployment live in consuming MOSIP services.

## Testing

- Unit tests: Maven Surefire (`mvn test`)
- Sample CLI usage: [test/README.md](test/README.md)
- Functional / platform tests: [MOSIP Functional Tests](https://github.com/mosip/mosip-functional-tests)

## Notices & licensing matrix

Third-party attributions and the MOSIP open-source license compatibility matrix (MPL 2.0 Compatible / Use with MOSIP) are documented in:

- [NOTICE](NOTICE)
- [licenses/NOTICE](licenses/NOTICE)

## Contribution & Community

- Code contributions: [MOSIP Code Contributions](https://docs.mosip.io/1.2.0/community/code-contributions)
- Community support: [MOSIP Community](https://community.mosip.io/)
- Issues: [github.com/mosip/bio-utils/issues](https://github.com/mosip/bio-utils/issues)

## License

This project is licensed under the [Mozilla Public License 2.0](LICENSE).
