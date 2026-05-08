# constant-time-eq-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fconstant--time--eq--kotlin-blue.svg)](https://github.com/KotlinMania/constant-time-eq-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/constant-time-eq-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/constant-time-eq-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/constant-time-eq-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/constant-time-eq-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`cesarb/constant_time_eq`](https://github.com/cesarb/constant_time_eq).

**Original Project:** This port is based on [`cesarb/constant_time_eq`](https://github.com/cesarb/constant_time_eq). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `cesarb/constant_time_eq`

> The text below is reproduced and lightly edited from [`https://github.com/cesarb/constant_time_eq`](https://github.com/cesarb/constant_time_eq). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

Compares two equal-sized byte strings in constant time.

Inspired by the Linux kernel's crypto_memneq.

Licensed under either of

* Apache License, Version 2.0 (LICENSE-APACHE)
* MIT No Attribution License (LICENSE-MIT0)
* CC0 1.0 Universal (LICENSE-CC0)

at your option.

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:constant-time-eq-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same CC0-1.0 license as the upstream [`cesarb/constant_time_eq`](https://github.com/cesarb/constant_time_eq). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the constant_time_eq authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`cesarb/constant_time_eq`](https://github.com/cesarb/constant_time_eq) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
