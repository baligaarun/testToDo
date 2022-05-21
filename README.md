# testToDo
Test Code in Java + Cucumber + Selenium to validate a sample application ToDo for my learning activities.


# Unit Test Details
Platform - Windows 10 Enterprise 64 Bit.

Java - JDK 1.8

Maven - 3.8.5

IDE - Eclipse IDE 2022-23

Browsers - Chrome (tested with version 101.0 64-Bit).

Note: For now, the drivers for Chrome and Firefox were used for tests. With firefox (geckodriver), an open issue exists which impacts the edit flow of a ToDo (content is capitalized). However, no such issue is not with same code for Chrome. So, it is recommended to use Chrome.

# Pre-Requisites
1. Clone the below Git project (using GitBash or any preferred approach):
git clone https://github.com/baligaarun/testToDo.git

2. Apache Maven installed. If no, please follow the instructions at - https://maven.apache.org/install.html

3. Change to the root of project directory (wherein the pom.xml is located)

Example:
C:\Users\baligaarun\eclipse-workspace\testToDo>dir
21-05-2022  11:43    <DIR>          .
21-05-2022  11:43    <DIR>          ..
17-05-2022  21:27             1,315 .classpath
20-05-2022  18:05               560 .project
20-04-2022  00:43        11,704,320 chromedriver.exe
06-04-2022  15:53         3,738,640 geckodriver.exe
20-05-2022  18:10            11,558 LICENSE
21-05-2022  11:39             2,877 pom.xml
20-05-2022  18:28               286 README.md
20-05-2022  18:11    <DIR>          src
21-05-2022  11:45    <DIR>          target

And execute the tests using build command:
mvn test

