# testToDo
Test Code in Java + Cucumber + Selenium to validate a sample application ToDo (URL below) for my learning activities:

https://todomvc.com/examples/vue/


# Unit Test Details
Platform - Windows 10 Enterprise 64 Bit.

Java - JDK 1.8

Maven - 3.8.5

IDE - Eclipse IDE 2022-23

Browsers - Chrome (tested with version 101.0 64-Bit).

Note: 

a. For now, the drivers for Chrome and Firefox were used for tests. 

b. With firefox (geckodriver), an open issue exists which impacts the edit flow of a ToDo (content is capitalized). However, the issue is not seen with Chrome with same code for Chrome. So, it is recommended to use Chrome until I rever back on the issue.

# How to run the tests?
1. Clone the below Git project (using GitBash or any preferred approach):
git clone https://github.com/baligaarun/testToDo.git

2. Apache Maven installed. If no, please follow the instructions at - https://maven.apache.org/install.html

3. Change to the root of project directory (wherein the pom.xml is located) and execute: 

mvn test
