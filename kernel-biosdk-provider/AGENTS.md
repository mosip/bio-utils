# kernel-biosdk-provider

Reflection bridge to vendor SDKs (`BioProviderImpl_V_0_7`…`_V_0_9`); no compile dep on vendor JAR. Config `mosip.biometric.sdk.providers.*`. Logger from `kernel-core` (not `kernel-logger-logback`). Tests: avoid `@EnableAutoConfiguration` (kernel-core AutoConfiguration.imports need remote/DB). No `kernel-bom`.
