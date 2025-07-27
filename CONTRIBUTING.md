# Contributing

Refer to our [contribution guidelines](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md).

## Code Style

Code style is enforced through standard Jenkins spotless rules.

```shell
# fix formatting violations
mvn spotless:apply
```

[Format as you build](https://github.com/jenkinsci/plugin-pom/blob/master/README.md#format-as-you-build)

## Testing

* Prefer JUnit 5 tests for unit tests

```shell
# run unit tests
mvn test
```

```shell
# start jenkins server with plugin installed
mvn hpi:run
```
