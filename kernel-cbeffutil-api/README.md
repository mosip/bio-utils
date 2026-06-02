# Kernel CBEFF Util API

## 📌 Overview
**Kernel CBEFF Util API** provides utilities and interfaces to handle CBEFF (Common Biometric Exchange Formats Framework) data structures. It ensures compliance with CBEFF standards for biometric data exchange within the MOSIP ecosystem.

## ✨ Features
- **CBEFF Compliance**: Utilities to create and parse CBEFF-compliant data structures.
- **Data Encapsulation**: Handles BIR (Biometric Information Record) construction.
- **Standardization**: Ensures biometric data is exchanged in a standardized format.

---

## 🧩 Services

This module provides the following key implementation:

- **CbeffImpl**: Implementation of CBEFF utility operations, including the creation and validation of Biometric Information Records (BIR) and handling standard biometric headers.

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

Not applicable. This is a **utility library** and does not require a database.

---

## 🛠️ Configuration

No external configuration is required.

---

## 🐳 Docker Support

Not applicable. This is a **library module**.

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
