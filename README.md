# HamroBazaar UI Automation

## Overview

This project automates the search functionality on HamroBazaar using Java, Selenium, and the Page Object Model (POM) design pattern. The main objectives are to perform a search for "Monitor", filter the results by location and radius, sort the results by price, and extract the details of the top 50 items into a CSV file.

## Prerequisites

- Java JDK
- Maven
- TestNG

## Setup

1. Clone the repository:

   ```sh
   git clone https://github.com/pradipbudhathoki/HamroBazarSelenium.git
   cd HamroBazarSelenium

   ```

2. Build the project using Maven:
   ```sh
   mvn clean install
   ```

## Running the Tests

To execute the test and generate the CSV report, use the following command:

    mvn test

You can run the tests using your IDE or from the command line.

### Using IDE

- Open the project in your preferred IDE (such as IntelliJ or Eclipse).
- Right-click on the testng.xml file and select "Run".

### Using Command Line

To execute the test and generate the CSV report, use the following command:

```sh
mvn test
```

## Project Structure

- **src/test/java/pages**: Contains the Page Object Model classes.
  - HomePage.java : Responsible for interacting with the home page elements such as the search box and search button.
  - SearchResultsPage.java: Sets the location filter, location radius, filter and the sorting of the items.
  - FinalResultPage.java: Handles the extraction of product details and writing them to a CSV file.
- **src/test/java/tests**: Contains the test class.
  - Tests.java: Contains the TestNG test classes.
- **src/main/resources**: Contains the log4j2.xml file
- **logs**: Contains the logs of the test run
- **extentReport**: HTML report of the test run
- **pom.xml**: Maven dependencies.
- **testng.xml**: TestNG configuration file to define and run test suites.

## Test Description

1. Opens HamroBazaar.
2. Searches for 'Monitor'.
3. Filters results by location and radius.
4. Sorts results by price (low to high).
5. Collects details of top 50 items and saves them to a CSV file.
6. Displays the CSV file content in a tabular format.

## CSV Output

After running the test, a CSV file named Search_Result.csv will be generated in the project root directory. This file contains the details of the top 50 items, including:

- Title of the product
- Description of the product
- Price of the product
- Condition of the product
- Ad posted date
- Name of the seller

## Dependencies

The project uses the following dependencies:

- **Selenium**: For browser automation.
- **TestNG**: For structuring and running the tests.
- **WebDriverManager**: For managing WebDriver binaries.
- **Apache Commons CSV**: For handling CSV operations.
- **OpenCSV**: For reading CSV files.
- **ExtentReports**: For reporting the test results.
- **Log4j**: For logging the test in the logs.

These dependencies are managed via Maven.
