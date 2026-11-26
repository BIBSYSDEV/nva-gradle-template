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

These parts are identical in every service:

- `info.contact` and `info.license`
- `x-amazon-apigateway-request-validators`
- `x-amazon-apigateway-gateway-responses`
- the security anchors `x-authenticated-security` and `x-backend-security`
- each entry in `components.responses` that the service uses (leave out the rest, since Spectral warns about unused components)
- the `Problem` and `ValidationError` schemas
- the `CognitoUserPool` security scheme, apart from the extra `ExternalCognitoAuthorizerArn` for APIs that serve external clients

Change a shared part here first, then sync the services.
The `Problem` schema matters most, since every service returns the same error body from nva-commons, and clients should only have to learn it once.

nva-swagger-generator publishes each document verbatim on the service documentation pages, so whatever is in the file, including servers, descriptions and examples, is what readers see.

### Conventions

- Use `openapi: 3.0.3`, since API Gateway does not import OpenAPI 3.1.
- Name the API `NVA <Name> API` in `info.title`.
  The title is the link text on the service documentation index, and also the REST API name in API Gateway, so renaming it is a deploy-visible change.
- Write `info.description` as one or more full sentences.
  It is the first thing readers see on the API's documentation page.
- List the servers in the order production, test, development, sandbox, end-to-end test, each with the service's base path and a description.
  Swagger UI sends "Try it out" requests to the selected server.
- Write tag names in Title Case and give every tag a description.
- Order the keys of an operation as `tags`, `summary`, `description`, `operationId`, `parameters`, `requestBody`, `security`, `x-amazon-apigateway-request-validator`, `x-amazon-apigateway-integration`, `responses`.
- Write `summary` as a short phrase without a trailing period, such as `Get example`.
- Write every `description` as full sentences that end with a period, in operations, parameters, responses and schemas alike.
  Server and tag descriptions are the exception: they are short labels without a period.
- Write `operationId` in lowerCamelCase with the verb first, such as `getExample`.
- Set `security` per operation with one of the anchors, such as `security: *authenticated`, and leave it out for public operations.
  API Gateway [does not use root-level security](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-known-issues.html), so setting it at the root has no effect.
- Name any extra security anchor like the shared ones, `x-<name>-security: &<name>`, such as `x-publication-upsert-security: &publicationUpsert`.
- Set request validation per operation with one of the shared validators.
  Use `validate_body_and_parameters` on every new operation that has a request body or required parameters, so the documentation is enforced.
  Parameter validation only checks that required query and header parameters are present.
  Turning validation on for an existing operation changes its behaviour, so do that one operation at a time.
- Give the integration exactly `uri`, `httpMethod: POST` and `type: AWS_PROXY`.
  The URI targets the `live` alias (`${Handler.Arn}:live/invocations`), which requires `AutoPublishAlias: live` on the function.
- Reference error responses as `#/components/responses/<Name>`, never inline and never keyed by status code.
  Name a response after the reason phrase in UpperCamelCase, such as `NotFound` or `MethodNotAllowed`.
- Use `application/problem+json` for error responses, since that is what `ApiGatewayHandler` in nva-commons returns.
  The shared gateway responses give the errors that API Gateway produces itself (failed request validation, a missing or invalid token, an unknown route) the same body, media type and CORS header.
- Quote status codes (`"200"`) and `$ref` values with double quotes, and quote other strings only when YAML requires it.
- Name schemas in UpperCamelCase with letters and digits only, since they become API Gateway model names, and properties in lowerCamelCase.
- Keep schemas that are used with request body validation within what API Gateway supports.
  It does not support `nullable`, `default`, `readOnly`, `discriminator` or the `int32` and `int64` formats.
