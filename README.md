# testToDo
Test Code in Java + Cucumber + Selenium to validate a sample application ToDo (URL below) for my learning activities:

https://todomvc.com/examples/vue/


# Unit Test Details
Platform - Windows 10 Enterprise 64 Bit.

Java - JDK 1.8

Maven - 3.8.5

IDE - Eclipse IDE 2022-23 (IDE not mandatory for execution)

Browsers - Chrome (tested with version 101.0 64-Bit).

*Note:*

*a. For now, the drivers for Chrome and Firefox were used for tests.*

*b. With firefox (geckodriver), an open issue exists which impacts the edit flow of a ToDo (content is capitalized). However, the issue is not seen with Chrome with same code for Chrome. So, it is recommended to use Chrome until I revert back on the issue.*

# How to run the tests?

1. Clone the below Git project (using GitBash or any preferred approach):

```git clone https://github.com/baligaarun/testToDo.git```

2. Apache Maven installed. If no, please follow the instructions at - https://maven.apache.org/install.html

3. Update browser driver exe in the root of the project directory. Currently, for testing ease, the following drivers are added in the project folder. Eventually, they should be removed from git project so that end users can add a compatible dirver.

chromedriver.exe --> ChromeDriver 101.0.4951.41 for Chrome version 101. To fetch an alternate version, please check: https://chromedriver.chromium.org/downloads

geckodriver.exe --> geckodriver-v0.31.0-win64. To fetch an alternate version, please check: https://github.com/mozilla/geckodriver/releases

4. Change to the root of project directory (wherein the pom.xml is located) and execute: 

```mvn test```

# Which tests from the potential scenarios are covered? 

For covered cases, the scenario code (SCENARIO_X) from the operate.feature file is specified for convenience.

**1. Create ToDo flow:**

a. Use variable content - single word, sentence, multiple sentences (atleast 2), duplicate content. *[Covered by SCENARIO_01]*

b. Verify created content is present only in All and Active tab. Not in the Completed tab. *[Covered by SCENARIO_01]*

c. Validate items left in the footer. *[Covered by SCENARIO_01]*

------------

**2. Complete Todo:**

a. Complete ToDo selectively using checkbox - Execute for atleast 2 of the active items. *[Covered by SCENARIO_02, SCENARIO_06]*

b. Verify completed content is present only in All and Completed tab. Not in the Active tab. *[Covered by SCENARIO_02]*

c. Complete All ToDo items using the toggle option. Also, verify clear completed button is shown.  *[Covered by SCENARIO_08]*

-----------

**3. Edit Todo:**

a. Edit ToDo by double click. Try from all 3 tabs - All, Active, Completed for different input values. *[Covered by SCENARIO_03]*

-----------

**4. Re-Open Todo:**

a. Re-Open ToDo selectively using checkbox - Execute for atleast 2 of the completed items. *[Covered by SCENARIO_04]*

b. Re-Open All ToDo items using the toggle option. Also, verify clear completed button is not shown. *[Covered by SCENARIO_09]*

------------

**5. Clear Todo:**

a. Clear a ToDo selectively using checkbox - Cover both active and completed ToDo items. *[Covered by SCENARIO_05]*

b. Clear all Completed ToDo items using the Clear Completed option. *[Covered by SCENARIO_07]*

-------------

**6. User Interface Validation:**

a. Presence of tabs and selective highlighting of a tab upon switching to it. *[Covered by tabSwitch method invoked by most scenarios implicitly]*

b. Default placeholder text for adding ToDo. *[Covered by validateToDoForm method invoked by all scenarios implicitly]*

c. Completed ToDo is crossed out (strikethrough). *[Covered by verifyStatusOfToDoInTab method invoked by most scenarios implicitly]*

------------

# Which potential scenarios should be considered in the future?

*Subject to the answers for the following queries:*

1. Does the application have any limitation on the total number of ToDo items? If so, what is the total threshold? 

2. To complement the total threshold above, is there pagination support? If so, what is the page level threshold?

3. Concurrent user sessions to the application.

4. How does the application store the data? E.g. Browser cache, file system etc.

5. Test by different user accounts (or roles) on the same system.

6. Full list of supported browsers.
