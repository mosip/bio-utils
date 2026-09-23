# bio-utils

JDK21 · Maven3.9+ · Boot **4.1.1**. `mvn clean install "-Dgpg.skip=true"`. No `kernel-bom`. Parent artifact `biometrics`.

Prerequisite: commons `kernel-core` **1.4.1-SNAPSHOT** (`cd ../commons/kernel && mvn clean install "-Dgpg.skip=true"`). Pin via `kernel.core.version` — do **not** parent `kernel-parent` (its `kernel.core.version=${project.version}` would miss commons).

Order: `kernel-biometrics-api` → `cbeffutil` / `biosdk-provider`; `biometrics-util` ISO↔image (standalone). Sibling deps omit `<version>`. `test/` = sample CLI only (not in reactor).

Ban: `kernel-bom`; separate `kernel-logger-logback` artifact (use `kernel-core`); Boot `repackage` on library modules; Jakarta JAXB for CBEFF (stay `javax.xml.bind` 2.3.x); Jackson 3 without `spring-boot-jackson2`.
