package pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import common.Driver;

/**
 * Page Object Model based class containing business logic for testing of ToDo Application.
 * 
 * @author Arun Baliga (baligaarun@gmail.com)
 * @since 19.05.2022
 */
public class ToDoApp {
	// XPath based locators for WebElements
	// Substrings prefixed and suffixed with '__' (double underscores) are replaced by actual values in methods.
	// Examples: __TODO__  or __TAB__
	
	static final String xpath_todo_create = "//section[@class='todoapp']/header[@class='header']/input";   // Create ToDo box.
	static final String xpath_app_header = "//header/h1";      // Header of the Application.
	static final String xpath_count_text = "//footer//span";   // Items Left section in the footer.
	
	static final String xpath_todo_list = "//ul[@class='todo-list']//label";     // List of ToDos (Text).
	static final String xpath_todo_lineitem_all = "//ul[@class='todo-list']/li"; // List of ToDos (Container).
	
	static final String xpath_todo_label = "//label[text()='__TODO__']"; // Specific ToDo (Text)
	static final String xpath_todo_lineitem_specific = "//ul[@class='todo-list']/li[div/label[text()='__TODO__']]"; // Specific ToDo (Container)	
	
	static final String xpath_todo_checkbox_specific = "//div[label[text()='__TODO__']]//input[@type='checkbox']";  // Check box of a ToDo.
	static final String xpath_todo_checkbox_all = "//input[@type='checkbox' and @class!='toggle-all']";  // All check boxes.
	   
	static final String xpath_tab = "//a[text()='__TAB__']";  // Tab element. Here, __TAB__ can be All, Active or Completed.
	static final String xpath_toggle_all = "//label[@for='toggle-all']";           // Toggle button for All Re-Open/Complete (Text)  
	static final String xpath_toggle_all_input = "//input[@class='toggle-all']";   // Toggle button for All Re-Open/Complete (Container) 
	
	static final String xpath_todo_clear_specific = "//li[div/label[text()='__TODO__']]/div/button";   // Delete button for a ToDo.
	static final String xpath_todo_clear_all = "//footer/button[contains(text(),'Clear completed')]";  // Clear completed button
	
	static final String xpath_todo_edit = "//li[div/label[text()='__TODO__']]/input[@type='text']"; // Edit a ToDo

	WebDriver driver;
	
	/**
	 *  Instantiate ToDoApp class, including navigation to the URL.
	 *  
	 *  @param application_url - URL of the application.
	 *  @param browser - Browser to be used. Supported values - chrome, firefox.
	 */
	public ToDoApp(String application_url, String browser) {
		WebDriver myDriver = Driver.getDriver(browser);
		myDriver.navigate().to(application_url);
		this.driver = myDriver;
	}
	
	/**
	 *  Return the URL navigated in WebDriver
	 * @return
	 */
	public String getApplicationUrl() {
		return driver.getCurrentUrl();
	}

	/**
	 * Create a ToDo.
	 * 
	 * @param todo ToDo to be created.
	 * @return status true if success. Else, false.
	 */
	public boolean createToDo(String todo) {
		boolean status = false;
		try {	
			if (!tabSwitch("All")) {
				return status;
			}
			
			//Find initial count of ToDo.
			int toDoCountInitial = driver.findElements(By.xpath(xpath_todo_list)).size();
			
			//Enter ToDo.
			driver.findElement(By.xpath(xpath_todo_create)).sendKeys(todo, Keys.ENTER);			
			
			//Confirm creation by ensuring ToDo list is appended with new ToDo.
			List<WebElement> toDoList = driver.findElements(By.xpath(xpath_todo_list));
			int toDoCountFinal = toDoList.size();

			if ((toDoCountFinal == toDoCountInitial + 1) && (toDoList.get(toDoCountFinal - 1).getText().equals(todo))) {
				System.out.println("ToDo is created.");				
				status = true;
			} else {
				System.out.println("ERROR: ToDo creation failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	

	/**
	 * Complete a ToDo.
	 * @param todo ToDo to be completed.
	 * @return status True if success. Else, False.
	 */
	public boolean completeToDo(String todo) {
		boolean status = false;
		try {
			if (!tabSwitch("All")) {
				return status;
			}
			
			// Get check box of ToDo and select it.
			String effective_xpath = xpath_todo_checkbox_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			if (matched_todo_list.size() == 0) {
				System.out.format("No ToDo found - %s\n", todo);
				return status;
			}
			WebElement first_match = matched_todo_list.get(0);
			if (first_match.isSelected()) {
				System.out.println("WARN: ToDo is already completed. Nothing to do.");
			} else {
				first_match.click();
				
				// Double check if element is now completed.
				first_match = driver.findElements(By.xpath(effective_xpath)).get(0);
				if (first_match.isSelected()) {
					System.out.println("ToDo is now completed");
					status = true;
				} else {
					System.out.println("ToDo failed to complete");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/** Re-Open a ToDo.
	 * @param todo ToDo to be completed.
	 * @return status True if success. Else, False.
	 */
	public boolean reOpenToDo(String todo) {
		boolean status = false;
		try {
			if (!tabSwitch("All")) {
				return status;
			}
			
			// Get check box of ToDo and select it.
			String effective_xpath = xpath_todo_checkbox_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			if (matched_todo_list.size() == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}
			WebElement first_match = matched_todo_list.get(0);
			if (!first_match.isSelected()) {
				System.out.println("WARN: ToDo is already active. Nothing to do.");
			} else {
				first_match.click();
				sleep(1);
				
				// Double check if element is now active.
				first_match =  driver.findElements(By.xpath(effective_xpath)).get(0);
				if (!first_match.isSelected()) {
					System.out.println("ToDo is now Active.");
					status = true;
				} else {
					System.out.println("ToDo failed to move to Active.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Clear a ToDo from the specified tab.
	 * 
	 * @param todo ToDo to be cleared.
	 * @param tab Tab to be used.
	 * @return result Status of ToDo ("active" or "completed") if success. Else, "FAILURE".
	 */
	public String clearToDo(String todo, String tab) {
		String status = "FAILURE";
		try {
			if (!tabSwitch(tab)) {
				return status;
			}
			
			// Find ToDo list matching existing ToDo string.
			String effective_xpath = xpath_todo_clear_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			int matched_todo_count_initial = matched_todo_list.size();
			if (matched_todo_count_initial == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}
			
			// Find the 1st match of the check box for the ToDo.
			effective_xpath = xpath_todo_checkbox_specific.replace("__TODO__", todo);
			matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			WebElement first_match = matched_todo_list.get(0);
			boolean isCompleted = first_match.isSelected();
			
			// Hover on the check box to get the delete button.
			Actions action = new Actions(driver);
			action.moveToElement(first_match).perform();			
			sleep(1);
			
			// Find the delete button and click on it.
			effective_xpath = xpath_todo_clear_specific.replace("__TODO__", todo);
			matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			first_match = matched_todo_list.get(0);
			first_match.click();		
			
			// Verify ToDo is no longer shown (i.e. in case of duplicates in view, matched todo count is reduced by 1)
			// If success, return text status of ToDo cleared ("active" or "completed"). Else, "FAILURE"
			matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			int matched_todo_count_final = matched_todo_list.size();
			
			if (matched_todo_count_final == matched_todo_count_initial - 1) {
				System.out.println("ToDo " + todo + " is cleared successfully.");
				if (isCompleted) {
					status = "completed";
				} else {
					status = "active";
				}
			} else {
				System.out.println("ERROR: For ToDo " + todo + ", initial count is " + matched_todo_count_final + " after delete, it is " + matched_todo_count_initial);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Edit a ToDo.
	 * 
	 * @param todo ToDo to be edited.
	 * @param newToDo New ToDo value.
	 * @param tab Tab to be used.
	 * @return result Status of ToDo ("active" or "completed") if success. Else, "FAILURE".
	 */
	public String editToDo(String todo, String newToDo, String tab) {
		String result = "FAILURE";
		try {
			// Find ToDo list matching existing ToDo string.
		    String effective_path_label = xpath_todo_label.replace("__TODO__", todo);
			List<WebElement> toDoElements = driver.findElements(By.xpath(effective_path_label));
			if (toDoElements.size() == 0) {
				System.out.println("ERROR: Todo not found. Not proceeding further.");
				return result;
			} 
			
			// Double click on the 1st match to enable edit.
			WebElement toDoElement = toDoElements.get(0);
			Actions actions = new Actions(driver);		
			actions.doubleClick(toDoElement).perform();
			sleep(1);
			
			// Find initial count of ToDos matching the new ToDo string (useful if duplicates exist).
			int todo_count_initial = driver.findElements(By.xpath(xpath_todo_label.replace("__TODO__", newToDo))).size();
			
			//Find 1st match of editable box matching the ToDo string.
			String effective_path_edit = xpath_todo_edit.replace("__TODO__", todo);
			toDoElements = driver.findElements(By.xpath(effective_path_edit));
			if (toDoElements.size() == 0) {
				System.out.println("ERROR: Todo input box not shown. Not proceeding further.");
				return result;
			} 
			toDoElement = toDoElements.get(0);
			
			// Update by selecting existing text and overwriting it with new value.
			toDoElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), newToDo, Keys.ENTER);
			sleep(1);
			
			// Find ToDo list matching new ToDo string. Also, get text status of the 1st match (active or completed).
			List<WebElement> we_list = driver.findElements(By.xpath(xpath_todo_lineitem_specific.replace("__TODO__", newToDo)));
			String text_status = we_list.get(0).getAttribute("class");
			System.out.println("Text_Status --> " + text_status);
			
			// Verify count of ToDos matching the new ToDo string is incremented by 1. If success, return text status.
			int todo_count_final = driver.findElements(By.xpath(xpath_todo_label.replace("__TODO__", newToDo))).size();
			if (todo_count_final == todo_count_initial + 1) {
				System.out.println("ToDo is successfully edited.");
				if (text_status.contains("completed")) {
					result = "completed";
				} else {
					result = "active";
				}
			} else {
				System.out.println("ToDo edit failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * Clear all Completed ToDo from the specified tab.
	 * 
	 * @return True if success. Else, False.
	 */
	public boolean clearAllCompletedToDo() {
		boolean status = false;
		try {		
			if (!tabSwitch("Active")) {
				return status;
			}
			
			// Get the initial count of ToDos in Active tab.
			int active_todo_count_initial = driver.findElements(By.xpath(xpath_todo_lineitem_all)).size();
			
			// Execute the clear completed operation.
			driver.findElement(By.xpath(xpath_todo_clear_all)).click();
			sleep(1);
			
			//Confirm the Active ToDos list is not impacted by the operation.
			if (!tabSwitch("Active")) {
				return status;
			}
			int active_todo_count_final = driver.findElements(By.xpath(xpath_todo_lineitem_all)).size();
			
			if (active_todo_count_final == active_todo_count_initial) {
				System.out.println("Active ToDo list is NOT impacted by Clear All Completed operation.");
				status = true;
			} else {
				System.out.println("ERROR: Active ToDo list is impacted by Clear All Completed operation.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Get presence of clear completed button
	 * 
	 * @return presence true if present. Else, false.
	 */
	public boolean verifyPresenceOfClearCompleted() {
		boolean status = false;
		try {		
			if (driver.findElement(By.xpath(xpath_todo_clear_all)).isDisplayed()) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	
	/**
	 * Verify Tab is empty.
	 * 
	 * @param tab Tab
	 * @return status True if success. Else, False.
	 */
	public boolean verifyTabIsEmpty(String tab) {
		boolean status = false;
		try {			
			if (!tabSwitch(tab)) {
				return status;
			} 
			
			// Confirm the completed ToDos list is empty.		
			if (driver.findElements(By.xpath(xpath_todo_lineitem_all)).size() == 0) {
				System.out.format("%s ToDo list is empty as expected\n.", tab);
				status = true;
			} else {
				System.out.format("ERROR: %s ToDo list is not empty unlike expectation\n", tab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Complete all ToDo items.
	 * 
	 * @return status True if success. Else, False.
	 */
	public boolean completeAllToDo() {
		boolean status = false;
		try {
			// Proceed only if Toggle All element is not selected
			if (driver.findElement(By.xpath(xpath_toggle_all_input)).isSelected()) {
				System.out.println("WARN: Toggle element is already set to Completed. Skipping...");
			} else {
				
				// Click on Toggle All.
				driver.findElement(By.xpath(xpath_toggle_all)).click();
				sleep(1);	
				
				// Confirm by matching count of items in completed state with count of all items.
				List<WebElement> todo_list = driver.findElements(By.xpath(xpath_todo_lineitem_all));
				int todo_completed_count = 0;
				for (WebElement todo_elem : todo_list) {
					if (todo_elem.getAttribute("class").contains("completed")) {
						todo_completed_count++;
					}
				}
				if (todo_completed_count == todo_list.size()) {
					System.out.println("All items in todo are completed.");
					status = true;
				} else {
					System.out.format("ERROR: Total todo count is %d while completed count is %d\n", todo_list.size(), todo_completed_count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Re-Open all completed ToDo items.
	 * 
	 * @return status true if success. Else, false.
	 */
	public boolean reOpenAllToDo() {
		boolean status = false;
		try {
			// Proceed only if Toggle All element is selected
			if (!driver.findElement(By.xpath(xpath_toggle_all_input)).isSelected()) {
				System.out.println("WARN: Toggle element is not set to All Completed. Open All cannot be executed.");
			} else {
				
				// Click on Toggle All.
				driver.findElement(By.xpath(xpath_toggle_all)).click();
				sleep(1);			
				List<WebElement> todo_list = driver.findElements(By.xpath(xpath_todo_lineitem_all));
				
				// Confirm count of items in completed state is now 0.
				int todo_completed_count = 0;
				for (WebElement todo_elem : todo_list) {
					if (todo_elem.getAttribute("class").contains("completed")) {
						todo_completed_count++;
					}
				}
				if (todo_completed_count == 0) {
					System.out.println("All ToDo items are open.");
					status = true;
				} else {
					System.out.format("ERROR: Few ToDo are still in Completed. Completed count %d\n", todo_completed_count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}


	/**
	 * Validate basic form - Header and Placeholder text
	 * 
	 * @return status true if success. Else, false.
	 */
	public boolean validateToDoForm() {
		boolean status = false;
		try {
			// Get the header and placeholder to compare with expectations.
			String app_header = driver.findElement(By.xpath(xpath_app_header)).getText();
			String app_default_text = driver.findElement(By.xpath(xpath_todo_create)).getAttribute("placeholder");
			if (app_header.equals("todos") && app_default_text.equals("What needs to be done?")) {
				System.out.println("Header as well as Placeholder text are valid");
				status = true;
			} else {
				System.out.format("ERROR: Found header - %s, Placeholder - %s\n", app_header, app_default_text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Tab - Switch
	 * 
	 * @param tab Tab to be switched to. Possible options: All, Completed, Active.
	 * @return status true if success. Else, false.
	 */
	public boolean tabSwitch(String tab) {
		boolean status = false;
		try {
			String effective_xpath = xpath_tab.replace("__TAB__", tab);
			WebElement tabElement = driver.findElement(By.xpath(effective_xpath));

			if (!tabElement.isDisplayed()) {
				System.out.println("Tab is not created as apparently no items exist yet.");
				status = true;
			} else {
				// Click on the tab
				tabElement.click();
				sleep(1);

				// Verify we switched to correct tab by checking if it is highlighted.
				tabElement = driver.findElement(By.xpath(effective_xpath));
				if (tabElement.getAttribute("class").equals("selected")) {
					System.out.println("Switched to tab " + tab);
					status = true;
				} else {
					System.out.println("ERROR: Failed to switch to tab " + tab);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Verify Items Left
	 * 
	 * @return status true if success. Else, false.
	 */
	public boolean verifyItemsLeft() {
		boolean status = false;
		try {
			if (!tabSwitch("Active")) {
				System.out.println("Switch to tab 'Active' failed.");
				return status;
			}
			
			String footer_text_observed = driver.findElement(By.xpath(xpath_count_text)).getText();
			int expected_count = driver.findElements(By.xpath(xpath_todo_list)).size();
			String footer_text_expected = expected_count + " items left";
			
			if (expected_count == 1) {
				footer_text_expected = expected_count + " item left";
			}

			System.out.format("Observed Footer - [%s], Expected Footer -[%s]", footer_text_observed, footer_text_expected);
			if (footer_text_observed.equals(footer_text_expected)) {
				status = true;
			} else {
				System.out.println("ERROR: Footer text does not match expectation.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Validate presence/absence of ToDo items in specified tab.
	 * 
	 * @param toDo List of ToDo to verify.
	 * @param tab      Tab to be verified. Possible options: All, Completed, Active.
	 * @param verify   Type of verification. Possible options: presence, absence.
	 * @return True if success. Else, False.
	 */
	public boolean verifyStatusOfToDoInTab(String toDo, String tab, String verify, List<String> activeToDoList, List<String> completedToDoList) {
		boolean status = false;
		try {
			// Switch to the required tab
			if (!tabSwitch(tab)) {
				return status;
			}
            
			// Find the ToDos in the tab matching the ToDo string.
			String effective_xpath = xpath_todo_lineitem_specific.replace("__TODO__", toDo);
			List<WebElement> toDoElements = driver.findElements(By.xpath(effective_xpath));
			
			// Get the expected count based on the global lists maintained.
			int expectedCountOfTodo = 0;
			if (tab.equals("Active")) {
				expectedCountOfTodo = countOfToDo(activeToDoList, toDo);
			} else if (tab.equals("Completed")) {
				expectedCountOfTodo = countOfToDo(completedToDoList, toDo);
			} else {
				expectedCountOfTodo = countOfToDo(activeToDoList, toDo) + countOfToDo(completedToDoList, toDo);
			}
			System.out.println("Expected count " + expectedCountOfTodo + ", Observed count " + toDoElements.size());
			
			// Compared the expected count with observed count. It should be equal for all cases and for 0, it is expected only if verify = "absent".
			// Additionally, if verify = present, check if ToDo is crossed out when in Completed Tab. 
			if (toDoElements.size() == expectedCountOfTodo) {
				if (verify.equals("absent") && expectedCountOfTodo == 0) {
					status = true;
				} else if (verify.equals("absent") && expectedCountOfTodo != 0) {
					System.out.format("ERROR: %d items matched for ToDo while expectation was absence of it.\n", expectedCountOfTodo);
				} else if (verify.equals("present") && expectedCountOfTodo == 0) {
					System.out.format("ERROR: 0 items matched for ToDo while expectation was presence of it.\n", expectedCountOfTodo);
				} else if (verify.equals("present") && expectedCountOfTodo != 0) {
					for (WebElement we : toDoElements) {
						if (we.findElement(By.xpath("./div/label")).getText().equals(toDo)) {
							System.out.println("Found the ToDo item.");
							// Validate if it is stroked out if tab is completed.
							if (tab.equals("Completed") && we.getAttribute("class").contains("completed")) {
								System.out.println("ToDo is crossed out as expected.");
								status = true;
								break;
							} else if (tab.equals("Active") && !we.getAttribute("class").contains("completed")) {
								System.out.println("ToDo is not crossed out as expected");
								status = true;
								break;
							} else if (tab.equals("All")) {
								status = true;
								break;
							}
						}
					}
				}
			} else {
				System.out.format("ERROR: Count of ToDo observed %d doesnt match with expected %d\n", toDoElements.size(), expectedCountOfTodo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Sleep
	 * @param time_in_sec Time in seconds.
	 * @return Void
	 */
	public void sleep(int time_in_sec) {
		try {
			TimeUnit.SECONDS.sleep(time_in_sec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Number of occurrences of a specific ToDo in a list of ToDos.
	 * @param todoList ToDo List
	 * @param toDo ToDo to check
	 * @return count - Count of ToDo in the list
	 */
	public int countOfToDo(List<String> todoList, String toDo) {
		int count = 0;
		try {
			for (String s : todoList) {
				if (s.equals(toDo)) {
					count++;
				}
			}
		System.out.println("countOfToDo: Returned list count " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * Close the application
	 * 
	 * @return
	 */
	public void closeApplication() {
		Driver.closeDriver();
	}
}