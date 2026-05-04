# MyDrinkShop - Lab04

This repository contains the `Lab04` branch for the DrinkShop project.

## What is included

- Maven Java project with JavaFX UI
- JUnit 5 unit and integration tests
- Mockito tests for service-layer isolation

## Homework focus

TestLink is unavailable, so the homework is based on:
- existing local tests in `src/test/java`
- a Jenkins pipeline to run the build and tests

## Jenkins configuration

The `Jenkinsfile` provides a declarative pipeline with the following stages:

1. Checkout the repository
2. Build and run tests using `mvn -B test`
3. Publish JUnit test results from `target/surefire-reports`

## How to run locally

```bash
mvn test
```

## Notes

- The `Jenkinsfile` makes this repository ready for CI
- Jenkins should be configured to run this pipeline from the `Lab04` branch
