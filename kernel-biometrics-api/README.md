# Kernel Biometrics API

## 📌 Overview
**Kernel Biometrics API** provides the core API definitions, interfaces, and data models for biometric operations within the MOSIP ecosystem. It defines the contract that biometric providers and SDKs must adhere to.

## ✨ Features
- **Biometric SPI**: Standard Service Provider Interfaces for biometric operations.
- **Data Models**: Common DTOs and entities for biometric data exchange.
- **Constants**: Standardized error codes and constants.

---

## 🧩 Services

This module defines the following key interfaces (SPI):

- **IBioApi** – Core interface for biometric operations such as quality check, matching, and extraction.
- **IBioApiV2** – Enhanced version of the biometric API interface.
- **CbeffUtil** – SPI/Contract for CBEFF utility operations.

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

Not applicable. This is an **API definition library** and does not require a database.

---

## 🛠️ Configuration

No external configuration is required.

---

## 🐳 Docker Support

Not applicable. This is a **library module**, not a deployable service.

---

## 🚀 Deployment

This module is published as a **Maven artifact**.

---

## ⬆️ Upgrade

Standard Maven dependency upgrade process applies.

---

## 🤝 Contribution & Community

We welcome contributions from everyone!

[Check here](https://docs.mosip.io/1.2.0/community/code-contributions) to learn how you can contribute code to this application.

If you have questions or encounter issues, feel free to raise them in the [MOSIP Community](https://docs.mosip.io/1.2.0/community/code-contributions).

---

## 📄 License

![License: MPL 2.0](https://img.shields.io/badge/License-MPL_2.0-brightgreen.svg)

This project is licensed under the **Mozilla Public License 2.0 (MPL 2.0)**.  
See the [LICENSE](../LICENSE) for full license details.
