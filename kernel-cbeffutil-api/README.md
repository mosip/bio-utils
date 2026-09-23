# Kernel CBEFF Util API

## Overview

Implementation of the **CBEFF** (Common Biometric Exchange Formats Framework) utility SPI for MOSIP. Builds and validates Biometric Information Record (BIR) XML used as the canonical interchange format between MOSIP components.

Maven coordinates: `io.mosip.kernel:kernel-cbeffutil-api`

## Standards

| Standard | Role |
| -------- | ---- |
| ISO/IEC 19785 (CBEFF) | BIR XML structure and semantics |
| Host-provided XSD | Schema validation at create/update time |

XSD is loaded at runtime from configuration (`mosip.kernel.xsdstorage-uri` + `mosip.kernel.xsdfile`), typically from the MOSIP config server.

## Features

- Create / update / validate CBEFF XML
- Extract BIR and BDB data from XML
- Spring `@Component` (`CbeffImpl`) for drop-in use in MOSIP services
- Depends on `kernel-biometrics-api` entities and `CbeffUtil` SPI

## API surface

| Type | Purpose |
| ---- | ------- |
| `CbeffImpl` | `CbeffUtil` implementation |
| `CbeffContainerImpl` | BIR list XML serialize/deserialize |
| `CbeffContainerI` | Container interface |

## Prerequisites

- JDK 21
- Maven 3.9+
- `kernel-biometrics-api` and `kernel-core` on the classpath
- Configured CBEFF XSD URI when running inside a MOSIP service

## Build

```text
mvn clean install -Dgpg.skip=true -pl kernel-cbeffutil-api -am
```

## Configuration

| Property | Purpose |
| -------- | ------- |
| `mosip.kernel.xsdstorage-uri` | Base URI for XSD storage (e.g. config server) |
| `mosip.kernel.xsdfile` | XSD file name |

## Database / Docker

Not applicable — library module only.

## Contribution & Community

- [Code contributions](https://docs.mosip.io/1.2.0/community/code-contributions)
- [MOSIP Community](https://community.mosip.io/)
- [Issues](https://github.com/mosip/bio-utils/issues)

## License

Licensed under the [Mozilla Public License 2.0](../LICENSE).
