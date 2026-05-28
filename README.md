[![Maven Package upon a push](https://github.com/mosip/bio-utils/actions/workflows/push-trigger.yml/badge.svg?branch=master)](https://github.com/mosip/bio-utils/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=master)](https://sonarcloud.io/dashboard?branch=master)

# Bio Utils

A shared collection of **biometric utility libraries** for the MOSIP ecosystem. This repository centralizes reusable logic for biometric data handling, ISO format conversions, and biometric SDK integration.

---

## 📌 Overview

The **bio-utils** repository provides common utilities and APIs used across MOSIP components for:

- Processing biometric data (Finger, Iris, Face)
- Converting between ISO biometric formats and image representations
- Defining standard biometric APIs
- Integrating external biometric SDKs
- Handling CBEFF (Common Biometric Exchange Formats Framework) structures

This helps ensure **consistency**, **reusability**, and **standardization** across MOSIP biometric implementations.

---

## 📦 Modules

The repository is organized into the following modules:

### 🔹 biometrics-util

Utilities for converting ISO biometric formats to images and vice versa.

**Key features:**
- ISO Finger, Iris, and Face format handling
- Image extraction and generation
- Helper utilities for biometric processing

📖 Refer to the [README](biometrics-util/README.md) for detailed usage and sample code.

---

### 🔹 kernel-biometrics-api

Core API definitions and interfaces for biometric operations within MOSIP.

**Key features:**
- Standard biometric operation contracts
- Interfaces used by kernel and SDK providers

📖 Refer to the [README](kernel-biometrics-api/README.md) for detailed usage and sample code.

---

### 🔹 kernel-biosdk-provider

Implementation of the Biometric SDK provider that bridges the MOSIP kernel with external biometric SDKs.

**Key features:**
- SDK provider implementation
- Kernel-to-SDK integration layer
- Pluggable biometric SDK support

📖 Refer to the [README](kernel-biosdk-provider/README.md) for detailed usage and sample code.

---

### 🔹 kernel-cbeffutil-api

Utilities and APIs for handling CBEFF (Common Biometric Exchange Formats Framework) data structures.

**Key features:**
- CBEFF structure creation and parsing
- Standard-compliant biometric data exchange

📖 Refer to the [README](kernel-cbeffutil-api/README.md) for detailed usage and sample code.

---

## 📄 License

![License: MPL 2.0](https://img.shields.io/badge/License-MPL_2.0-brightgreen.svg)

This project is licensed under the **Mozilla Public License 2.0 (MPL 2.0)**.

See the [LICENSE](LICENSE) file for full license details.
