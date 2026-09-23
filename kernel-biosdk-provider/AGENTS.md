# kernel-biosdk-provider

```
reflect vendor SDK (no compile vendor JAR)
├─ BioProviderImpl_V_0_7 | _0_8 | _0_9
├─ config: mosip.biometric.sdk.providers.*
└─ log ← kernel-core (ban kernel-logger-logback)
```

```
deps: boot-web · jackson2 · jpa · h2 · kernel-core · biometrics-api
tests: no @EnableAutoConfiguration (kernel-core AutoConfiguration.imports)
```
