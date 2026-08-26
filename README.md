# automationexercise — Skeleton

This is the **skeleton-only** version of the `automationexercise`
framework.

The purpose of this project is to provide the initial structure in which the
complete automation framework will be created and implemented. The framework
will be developed using **Java, Selenium WebDriver, TestNG, Maven, and the Page Object Model (POM).**

It preserves the project structure, Maven/TestNG/Surefire configuration,
Jenkinsfile, resources, packages, classes, annotations, PageFactory locators,
fields, method signatures, and the four requested test classes.

The existing structure serves as the foundation for implementing the complete
framework, including page objects, reusable components, test scenarios,
validations, configuration, test data, reporting, and CI/CD execution.


The existing structure serves as the foundation for implementing the complete
framework. It is intended to help the team consistently construct and extend
the automation framework based on the defined architecture and project
structure.

The framework will be implemented with page objects, reusable components,
test scenarios, validations, configuration, test data, reporting, and CI/CD
execution.

Java method and constructor implementations have been intentionally replaced
with:
```java
// TODO: implement.
throw new UnsupportedOperationException("TODO");
```

This keeps the architecture, class structure, method signatures, and intended
responsibilities visible while leaving the actual implementation for the
automation team to build on top of the provided framework structure.

## Test cases

- Test Case 1 — Register User
- Test Case 9 — Search Product
- Test Case 16 — Place Order: Login before Checkout
- Test Case 25 — Verify Scroll Up using Arrow button

## Project structure

```text
automationexercise/
├── pom.xml
├── testng.xml
├── Jenkinsfile
├── .run/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── config/
│   │   │   ├── factory/
│   │   │   ├── features/
│   │   │   ├── listeners/
│   │   │   ├── models/
│   │   │   ├── pages/
│   │   │   └── utils/
│   │   └── resources/
│   └── test/
│       └── java/
│           └── tests/
└── ...
```

Use this version as the starting point for constructing and implementing the automation framework. It provides the defined architecture and project structure that the team should follow while developing the complete framework.
