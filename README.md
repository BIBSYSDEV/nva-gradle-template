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
- OpenAPI linting with Spectral

## OpenAPI

[docs/openapi.yaml](docs/openapi.yaml) is the team template for the OpenAPI document of an NVA service behind API Gateway.
Copy it to `docs/openapi.yaml` in the service, replace the example values, and keep the shared parts as they are.
`./gradlew check` lints every YAML file in `docs/` with Spectral, using the ruleset in [.spectral.yaml](.spectral.yaml).

### Shared parts

These parts are identical in every service and marked `# Shared` in the template:

- `info.contact` and `info.license`
- the `external` tag, when the service uses it
- `x-amazon-apigateway-request-validators`
- `x-amazon-apigateway-gateway-responses`
- the security anchors `x-authenticated-security` and `x-backend-security`
- each entry in `components.responses` that the service uses
- the `Problem` and `ValidationError` schemas
- the `CognitoUserPool` security scheme, apart from the extra `ExternalCognitoAuthorizerArn` for APIs that serve external clients

Change a shared part here first, then sync the services.
The `Problem` schema matters most.
nva-swagger-generator renames schemas that share a name but differ, so one deviating copy shows up as a separate `<Title>Problem` schema in the combined documentation.

### Conventions

- Use `openapi: 3.0.3`, since API Gateway does not import OpenAPI 3.1.
- Name the API `NVA <Name> API` in `info.title`.
  The title is also the REST API name in API Gateway, the section name in the combined documentation, and the key nva-swagger-generator uses to match the document to the deployed API.
  Renaming it is a deploy-visible change.
- Write `info.description` as one or more full sentences.
  It becomes the section description in the combined documentation.
- List the servers in the order production, test, development, sandbox, each with the service's base path and a description.
- Write tag names in Title Case and give every tag a description.
  The lowercase `external` tag is reserved: nva-swagger-generator publishes the operations that have it in the external API documentation.
- Order the keys of an operation as `tags`, `summary`, `description`, `operationId`, `parameters`, `requestBody`, `security`, `x-amazon-apigateway-request-validator`, `x-amazon-apigateway-integration`, `responses`.
- Write `summary` as a short phrase without a trailing period, such as `Get example`, and `description` as full sentences.
- Write `operationId` in lowerCamelCase with the verb first, such as `getExample`.
- Set `security` per operation with one of the anchors, such as `security: *authenticated`, and leave it out for public operations.
  Never set it at the root: it also applies to the OPTIONS methods that SAM adds for CORS, and breaks preflight requests (see [nva-nvi#619](https://github.com/BIBSYSDEV/nva-nvi/pull/619), reverted in [nva-nvi#623](https://github.com/BIBSYSDEV/nva-nvi/pull/623)).
- Set request validation per operation with one of the shared validators.
- Give the integration exactly `uri`, `httpMethod: POST` and `type: AWS_PROXY`.
  The URI targets the `live` alias (`${Handler.Arn}:live/invocations`), which requires `AutoPublishAlias: live` on the function.
- Reference error responses as `#/components/responses/<Name>`, never inline and never keyed by status code.
  Name a response after the reason phrase in UpperCamelCase, such as `NotFound` or `MethodNotAllowed`.
- Use `application/problem+json` for error responses, since that is what `ApiGatewayHandler` in nva-commons returns.
- Quote status codes (`"200"`) and `$ref` values with double quotes, and quote other strings only when YAML requires it.
- Name schemas in UpperCamelCase without digits or whitespace (nva-swagger-generator warns about both), and properties in lowerCamelCase.
