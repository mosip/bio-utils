# bio-utils

```
JDK21 · Maven3.9+ · Boot 4.1.1 · parent biometrics · NO kernel-bom
└─ mvn clean install "-Dgpg.skip=true"
```

```
prereq
└─ ../commons/kernel → kernel-core 1.4.1-SNAPSHOT
   └─ pin kernel.core.version (never parent kernel-parent alone)
```

```
reactor
├─ kernel-biometrics-api     # SPI IBioApi/V2, BIR, CbeffUtil
│  ├─ kernel-cbeffutil-api   # CbeffImpl + XSD config
│  └─ kernel-biosdk-provider # reflect vendor SDK V0.7–0.9
├─ biometrics-util           # ISO19794 ↔ image (standalone)
└─ test/                     # sample CLI — NOT in reactor
```

```
rules
├─ siblings omit <version>
├─ libs: no Boot repackage
├─ CBEFF: javax.xml.bind 2.3.x (not Jakarta 4)
├─ Jackson2 via spring-boot-jackson2 (not Jackson3 default)
├─ ban: kernel-bom · kernel-logger-logback (use kernel-core)
└─ jacoco ≥85%
```
