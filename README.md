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

b. With firefox (geckodriver), an open issue exists which impacts the edit flow of a ToDo (content is capitalized). However, the issue is not seen with Chrome with same code for Chrome. So, it is recommended to use Chrome until I revert back on the issue.

# How to run the tests?
1. Clone the below Git project (using GitBash or any preferred approach):
git clone https://github.com/baligaarun/testToDo.git

2. Apache Maven installed. If no, please follow the instructions at - https://maven.apache.org/install.html

3. Change to the root of project directory (wherein the pom.xml is located) and execute: 

mvn test

# What tests from the potential scenarios are covered? 

For covered cases, the sceanrio code (SCENARIO_X) from the oeprate.feature file is specified for convenience.

**1. Create ToDo flow:**

a. Use variable content - single word, sentence, multiple sentences (atleast 2), duplicate content. *[Covered by SCENARIO_01]*

b. Verify created content is present only in All and Active tab. Not in the Completed tab. *[Covered by SCENARIO_01]*

c. Validate items left in the footer. *[Covered by SCENARIO_01]*


**2. Complete Todo:**

a. Complete ToDo selectively using checkbox - Execute for atleast 2 of the active items. *[Covered by SCENARIO_02]*

b. Verify completed content is present only in All and Completed tab. Not in the Active tab. *[Covered by SCENARIO_02]*

c. Complete All ToDo items using the toggle option. *[Covered by SCENARIO_08]*


**3. Edit Todo:**

a. Edit ToDo by double click. Try from all 3 tabs - All, Active, Completed for different input values. *[Covered by SCENARIO_03]*


**4. Re-Open Todo:**

a. Re-Open ToDo selectively using checkbox - Execute for atleast 2 of the completed items. *[Covered by SCENARIO_04]*

b. Re-Open All ToDo items using the toggle option. *[Covered by SCENARIO_09]*

6. Reopen ToDo
a. Single - Try atleast 2 items. - #DONE
b. All. - #DONE

Future implementation:
Create Flow:
b. Large number of todo items. - Any max limit? Scroll bar or pagination?  {Not 
