# Kernel BioSDK Provider

## 📌 Overview
**Kernel BioSDK Provider** is the implementation module that acts as a bridge between the MOSIP Kernel and various Biometric SDKs. It provides concrete implementations of the biometric interfaces defined in `kernel-biometrics-api`.

## ✨ Features
- **SDK Integration**: Adapts various biometric SDKs to the MOSIP Kernel API.
- **Version Support**: Provides implementations for multiple SDK versions.
- **Spring Integration**: Built with Spring Boot starters for seamless integration.

---

## 🧩 Services

This module provides the following key implementations:

- **BioProviderImpl**: Implementation of the [IBioApiV2](https://github.com/mosip/bio-utils/blob/master/kernel-biometrics-api/src/main/java/io/mosip/kernel/biometrics/spi/IBioApiV2.java) interface, handling biometric operations like extraction, matching, and quality checks using the underlying SDKs.
  - Supports multiple versions (e.g., 0.9).

---

## ⚙️ Local Setup

### 📄 Build Locally

To build the project locally, run:

```bash
mvn clean install -Dgpg.skip=true
```

---

## 📦 Prerequisites

- Java 21
- Maven 3.11.0 or higher
- Git

---

## 🗄️ Database Setup

Not applicable. This is a **provider library**. It may use a database (H2/JPA) internally or rely on the host application's datasource, but no standalone setup is required for this module itself.

---

## 🛠️ Configuration

This module acts as a library. Configuration is typically handled by the consuming application (e.g., proper SDK paths or provider properties in `application.properties`).

---

## 🐳 Docker Support

Not applicable. This is a **library module**.

---

## 🚀 Deployment

This module is published as a **Maven artifact**.

---

## ⬆️ Upgrade

Standard Maven dependency upgrade process applies.


## 🤝 Contribution & Community

We welcome contributions from everyone!

[Check here](https://docs.mosip.io/1.2.0/community/code-contributions) to learn how you can contribute code to this application.

If you have questions or encounter issues, feel free to raise them in the [MOSIP Community](https://docs.mosip.io/1.2.0/community/code-contributions).

---

## 📄 License

![License: MPL 2.0](https://img.shields.io/badge/License-MPL_2.0-brightgreen.svg)

This project is licensed under the **Mozilla Public License 2.0 (MPL 2.0)**.  
See the [LICENSE](../LICENSE) for full license details.
