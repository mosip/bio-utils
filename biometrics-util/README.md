# Biometrics Util

## 📌 Overview
**Biometrics Util** is a utility library to convert biometric data between **ISO formats** and **image formats**, and vice versa.

## ✨ Features

### 📄 Supported ISO Versions

| Modality | ISO Version |
| -------- | ----------- |
| Finger   | ISO 19794-4:2011 |
| Iris    | ISO 19794-6:2011 |
| Face    | ISO 19794-5:2011 |

> **Note:** JPEG2000 images are automatically converted to JPEG.

---

## 🧩 Services

This module provides the following utility classes:

- **FingerDecoder** – Utilities for fingerprint ISO conversion
- **IrisDecoder** – Utilities for iris ISO conversion
- **FaceDecoder** – Utilities for face ISO conversion
- **CommonUtil** – Common utilities for ISO image type conversion

---

## 💻 Sample Code

### 🖐️ Convert Finger ISO to Image

```java
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_4_2011");
convertRequestDto.setInputBytes(<ISO bytes>);
convertRequestDto.setCompressionRatio(95); // JPEG quality (0–100), default is 95

byte[] imageBytes = FingerDecoder.convertFingerISOToImageBytes(convertRequestDto);
BufferedImage image = FingerDecoder.convertFingerISOToBufferedImage(convertRequestDto);
```

### 👁️ Convert Iris ISO to Image

```java
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_6_2011");
convertRequestDto.setInputBytes(<ISO bytes>);
convertRequestDto.setCompressionRatio(95);

byte[] imageBytes = IrisDecoder.convertIrisISOToImageBytes(convertRequestDto);
BufferedImage image = IrisDecoder.convertIrisISOToBufferedImage(convertRequestDto);
```

### 🙂 Convert Face ISO to Image

```java
ConvertRequestDto convertRequestDto = new ConvertRequestDto();
convertRequestDto.setVersion("ISO19794_5_2011");
convertRequestDto.setInputBytes(<ISO bytes>);
convertRequestDto.setCompressionRatio(95);

byte[] imageBytes = FaceDecoder.convertFaceISOToImageBytes(convertRequestDto);
BufferedImage image = FaceDecoder.convertFaceISOToBufferedImage(convertRequestDto);
```

---

## 🔄 Convert ISO Image Type (Base64URL)

To convert a Base64URL-encoded ISO biometric (Face, Iris, Finger) to a specified image type (JPEG or PNG) and generate a new Base64URL-encoded ISO:

```java
CommonUtil.convertISOImageType(
    String inIsoData,
    Modality modality,
    ImageType imageType
) throws Exception;
```

---

## ⚙️ Local Setup

### 📄 Build Locally

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
See the [LICENSE](LICENSE) file for full license details.

