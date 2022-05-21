package pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import common.Driver;


public class ToDoApp {
	WebDriver driver;
	static final String xpath_todo_create = "//section[@class='todoapp']/header[@class='header']/input";
	static final String xpath_app_header = "//header/h1";
	static final String xpath_todo_list = "//ul[@class='todo-list']//label";
	static final String xpath_count_text = "//footer//span";
	static final String xpath_todo_lineitem_all = "//ul[@class='todo-list']/li";
	static final String xpath_todo_lineitem_specific = "//ul[@class='todo-list']/li[div/label[text()='__TODO__']]";
	
	static final String xpath_todo_checkbox_specific = "//div[label[text()='__TODO__']]//input[@type='checkbox']";
	static final String xpath_todo_checkbox_all = "//input[@type='checkbox' and @class!='toggle-all']";
	   
	//Tab XPath, wherein __TAB__ can be: All, Active or Completed.
	static final String xpath_tab = "//a[text()='__TAB__']";
	static final String xpath_toggle_all = "//label[@for='toggle-all']";
	static final String xpath_toggle_all_input = "//input[@class='toggle-all']";
	
	static final String xpath_todo_clear_specific = "//li[div/label[text()='__TODO__']]/div/button";  //Clear a ToDo.
	static final String xpath_todo_clear_all = "//footer/button[contains(text(),'Clear completed')]";  //Clear all ToDo.
	
	static final String xpath_todo_edit = "//li[div/label[text()='__TODO__']]/input[@type='text']"; //Edit a ToDo
	static final String xpath_todo_label = "//label[text()='__TODO__']"; //Edit a ToDo

	public ToDoApp(String application_url, String browser) {
		WebDriver myDriver = Driver.getDriver(browser);
		myDriver.navigate().to(application_url);
		this.driver = myDriver;
	}
	
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
				System.out.println("Switch to tab 'All' failed.");
				return status;
			}
			
			//Find the initial ToDo count.
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
	 * Validate Items Left
	 * 
	 * @return status - true if success. Else, false.
	 */
	public boolean validateItemsLeft() {
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
	 * Edit a ToDo.
	 * 
	 * @param todo ToDo to be edited.
	 * @param newToDo New ToDo value.
	 * @param tab Tab to be used.
	 * @return True if success. Else, False.
	 */
	public String editToDo(String todo, String newToDo, String tab) {
		String result = "FAILURE";
		try {
			Actions actions = new Actions(driver);			
		    String effective_path_label = xpath_todo_label.replace("__TODO__", todo);
			List<WebElement> toDoElements = driver.findElements(By.xpath(effective_path_label));
			if (toDoElements.size() == 0) {
				System.out.println("ERROR: Todo not found");
				return result;
			} 
			
			WebElement toDoElement = toDoElements.get(0);
			actions.doubleClick(toDoElement).perform();
			sleep(1);
			
			int todo_count_initial = driver.findElements(By.xpath(xpath_todo_label.replace("__TODO__", newToDo))).size();
			String effective_path_edit = xpath_todo_edit.replace("__TODO__", todo);
			toDoElements = driver.findElements(By.xpath(effective_path_edit));

			if (toDoElements.size() == 0) {
				System.out.println("ERROR: Todo input box not found");
				return result;
			} 
			toDoElement = toDoElements.get(0);
			toDoElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), newToDo, Keys.ENTER);
			sleep(1);
			
			List<WebElement> we_list = driver.findElements(By.xpath(xpath_todo_lineitem_specific.replace("__TODO__", newToDo)));
			String text_status = we_list.get(0).getAttribute("class");
			System.out.println("Text_Status --> " + text_status);
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
	 * Complete a ToDo.
	 * @param todo ToDo to be completed.
	 * @return True if success. Else, False.
	 */
	public boolean completeToDo(String todo) {
		boolean status = false;
		try {
			if (!tabSwitch("All")) {
				return status;
			}
			
			String effective_xpath = xpath_todo_checkbox_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			if (matched_todo_list.size() == 0) {
				System.out.format("No ToDo found - %s", todo);
				return status;
			}
			WebElement first_match = matched_todo_list.get(0);
			if (first_match.isSelected()) {
				System.out.println("WARN: ToDo is already completed. Nothing to do.");
			} else {
				first_match.click();
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
	 * @return True if success. Else, False.
	 */
	public boolean reOpenToDo(String todo) {
		boolean status = false;
		try {
			String effective_xpath = xpath_todo_checkbox_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			if (matched_todo_list.size() == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}
			WebElement first_match = matched_todo_list.get(0);
			if (!first_match.isSelected()) {
				System.out.println("Already active.");
			} else {
				first_match.click();
				sleep(1);
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
	 * @param todo ToDo to be cleared.
	 * @return True if success. Else, False.
	 */
	public String clearToDo(String todo, String tab) {
		String status = "FAILURE";
		try {
			if (tabSwitch(tab)) {
				System.out.println("Switch to tab " + tab + ". Proceeding further..");
			} else {
				System.out.println("Switch to tab " + tab + " failed.");
				return status;
			}
			
			String effective_xpath = xpath_todo_clear_specific.replace("__TODO__", todo);
			System.out.println("Clear XPATH " + effective_xpath);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			int matched_todo_count_initial = matched_todo_list.size();
			if (matched_todo_count_initial == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}
			
			effective_xpath = xpath_todo_checkbox_specific.replace("__TODO__", todo);
			matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			//Execute clear on the ToDo.
			WebElement first_match = matched_todo_list.get(0);
			boolean isCompleted = first_match.isSelected();
			Actions action = new Actions(driver);
			action.moveToElement(first_match).perform();			
			sleep(1);
			effective_xpath = xpath_todo_clear_specific.replace("__TODO__", todo);

			matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			first_match = matched_todo_list.get(0);
			first_match.click();		
			
			//Verify ToDo is no longer shown
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
	 * Clear all Completed ToDo from the specified tab.
	 * @return True if success. Else, False.
	 */
	public boolean clearAllCompletedToDo() {
		boolean status = false;
		try {
			
			if (tabSwitch("Active")) {
				System.out.println("Switch to tab 'Active'. Proceeding further..");
			} else {
				System.out.println("Switch to tab 'Active' failed.");
				return status;
			}
			int active_todo_count_initial = driver.findElements(By.xpath(xpath_todo_lineitem_all)).size();

			driver.findElement(By.xpath(xpath_todo_clear_all)).click();
			sleep(1);

			
			//Confirm the Active ToDos list is not impacted by the operation.
			if (tabSwitch("Active")) {
				System.out.println("Switch to tab 'Active'. Proceeding further..");
			} else {
				System.out.println("Switch to tab 'Active' failed.");
				return status;
			}
			int active_todo_count_final = driver.findElements(By.xpath(xpath_todo_lineitem_all)).size();
			
			if (active_todo_count_final == active_todo_count_initial) {
				System.out.println("Active ToDo list is unimpacted by the Clear All Completed operation.");
				status = true;
			} else {
				System.out.println("ERROR: Active ToDo list is impacted by the Clear All Completed operation.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	
	/**
	 * Verify Tab is empty.
	 * @param tab Tab
	 * @return True if success. Else, False.
	 */
	public boolean verifyTabIsEmpty(String tab) {
		boolean status = false;
		try {			
			if (!tabSwitch(tab)) {
				return status;
			} 
			//Confirm the completed ToDos list is empty.
			List<WebElement> completed_todo = driver.findElements(By.xpath(xpath_todo_lineitem_all));			
			if (completed_todo.size() == 0) {
				System.out.println(tab + " ToDo list is empty as expected.");
				status = true;
			} else {
				System.out.println("ERROR: " + tab + " ToDo list is not empty while we expected it to be empty.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Complete all ToDo items.
	 * @return True if success. Else, False.
	 */
	public boolean completeAllToDo() {
		boolean status = false;
		try {
			if (driver.findElement(By.xpath(xpath_toggle_all_input)).isSelected()) {
				System.out.println("Toggle element is already set to Completed. Skipping...");
			} else {
				driver.findElement(By.xpath(xpath_toggle_all)).click();
				sleep(1);			
				List<WebElement> todo_list = driver.findElements(By.xpath(xpath_todo_lineitem_all));
				int todo_list_count = todo_list.size();
				int todo_completed_count = 0;
				for (WebElement todo_elem : todo_list) {
					if (todo_elem.getAttribute("class").contains("completed")) {
						todo_completed_count++;
					}
				}
				if (todo_completed_count == todo_list_count) {
					System.out.println("All itmes in todo are completed.");
					status = true;
				} else {
					System.out.println("ERROR: Total todo count is " + todo_list_count + " while completed count is "
							+ todo_completed_count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Open all ToDo items.
	 * @return True if success. Else, False.
	 */
	public boolean reOpenAllToDo() {
		boolean status = false;
		try {
			if (!driver.findElement(By.xpath(xpath_toggle_all_input)).isSelected()) {
				System.out.println("SKIP: Toggle element is not set to All Completed. Open All cannot be executed.");
			} else {
				driver.findElement(By.xpath(xpath_toggle_all)).click();
				sleep(1);			
				List<WebElement> todo_list = driver.findElements(By.xpath(xpath_todo_lineitem_all));
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
					System.out.println("ERROR: 1 or more ToDo are in Completed. Completed count " + todo_completed_count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}



	/**
	 * Validate basic form.
	 * @return True if success. Else, False.
	 */
	public boolean validateToDoForm() {
		boolean status = false;
		try {
			String app_header = driver.findElement(By.xpath(xpath_app_header)).getText();
			String app_default_text = driver.findElement(By.xpath(xpath_todo_create)).getAttribute("placeholder");
			if (app_header.equals("todos")) {
				System.out.println("Header is correct - " + app_default_text);
			} else {
				System.out.println("Header is wrong. Expected todos, found " + app_header);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Tab - Switch
	 * @param tab Tab to be switched to. Possible options: All, Completed, Active.
	 * @return True if success. Else, False.
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
			if (tabSwitch(tab)) {
				System.out.println("Switch to tab " + tab + ". Proceeding further..");
			} else {
				System.out.println("Switch to tab " + tab + " failed.");
				return status;
			}

			String effective_xpath = xpath_todo_lineitem_specific.replace("__TODO__", toDo);
			List<WebElement> toDoElements = driver.findElements(By.xpath(effective_xpath));

			int expectedCountOfTodo = 0;
			if (tab.equals("Active")) {
				expectedCountOfTodo = countOfToDo(activeToDoList, toDo);
			} else if (tab.equals("Completed")) {
				expectedCountOfTodo = countOfToDo(completedToDoList, toDo);
			} else {
				expectedCountOfTodo = countOfToDo(activeToDoList, toDo) + countOfToDo(completedToDoList, toDo);
			}
			System.out.println("Expected count " + expectedCountOfTodo + ", Observed count " + toDoElements.size());
			if (toDoElements.size() == expectedCountOfTodo) {
				if (verify.equals("absent")) {
					status = true;
				} else {
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
				System.out.format("ERROR: Count of ToDo observed %d doesnt match with expected", expectedCountOfTodo);
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
	public void sleep(Integer time_in_sec) {
		try {
			TimeUnit.SECONDS.sleep(time_in_sec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Count of ToDo in list
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
		System.out.println("Returned list count " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}