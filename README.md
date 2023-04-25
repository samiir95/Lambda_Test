# Lambda_Test

This is a Maven project that uses TestNG, Selenium, Allure, and Maven Surefire plugin to automate the test scenario mentioned below on the LambdaTest website. The test scenario has to be executed on different combinations of browsers and platforms using Selenium 4 Grid and the programming language of your choice.

# Test Scenario

1. Navigate to https://www.lambdatest.com.

2. Perform an explicit wait until all the elements in the DOM are available.

3. Scroll to the WebElement ‘SEE ALL INTEGRATIONS’ using the scrollIntoView() method. You are free to use any of the available web locators (e.g., XPath, CssSelector, etc.)

4. Click on the link and ensure that it opens in a new Tab.

5. Save the window handles in a List (or array). Print the window handles of the opened windows (now there are two windows open).

6. Verify whether the URL is the same as the expected URL (if not, throw an Assert).

7. Close the current browser window.

# How to run the project

## Pre-requisites

- JDK 8 or later
- Maven 3.2 or later
- Chrome browser (version 86.0) installed on Windows 10 and Microsoft Edge browser (version 87.0) installed on macOS Sierra

# Steps to run the project

- Clone the repository to your local system

      git clone https://github.com/samiir95/Lambda_Test.git
      
- Navigate to the project directory

      cd Lambda_Test
      
- Execute the below command to run the tests on Chrome + 86.0 + Windows 10 && Microsoft Edge + 87.0 + macOS Sierra in parallel

      mvn clean test -DsuiteXmlFile="parallelSuite.xml" -DltUsername="your_lambda_user_name" -DltAccessKey="you_lambda_access_key"

- Once the tests are completed, execute the below command to generate Allure reports

      allure serve allure-results

The above command will generate a report and open it in a new tab in your default browser.


Note
In case you encounter any issue while running the tests or have any query, please feel free to raise an issue in the repository or reach out to my email: msamiir38@gmail.com
