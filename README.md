# nva-gradle-template

[![ci](https://github.com/BIBSYSDEV/nva-gradle-template/actions/workflows/build.yml/badge.svg)](https://github.com/BIBSYSDEV/nva-gradle-template/actions/workflows/build.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/8405a7d7b690490f8690949d207d9cdf)](https://app.codacy.com/gh/BIBSYSDEV/nva-gradle-template/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/8405a7d7b690490f8690949d207d9cdf)](https://app.codacy.com/gh/BIBSYSDEV/nva-gradle-template/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

This project is a minimal example and template for a Java project using Gradle as build tool.
It is intended to demonstrate a multi-module project structure and to provide a starting point for new projects.

## Quick Start

```bash
# Build and test
./gradlew build

# Run tests
./gradlew test

# Check for dependency updates
./gradlew dependencyUpdates
```

The `dependencyUpdates` task lists dependencies that have newer versions available for upgrade.

## Project Structure

- **common**: Shared library with model classes
- **calculator**: Example app depending on common module

## Key Features

- Java 23 with Amazon Corretto toolchain
- 100% code coverage enforced
- Auto-formatting with Google Java Format
- Static analysis with PMD and ErrorProne
