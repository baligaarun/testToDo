package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ToDoApp {
	WebDriver driver;
	static final String xpath_todo_create = "//section[@class='todoapp']/header[@class='header']/input";
	static final String xpath_app_header = "//header/h1";
	static final String xpath_todo_list = "//ul[@class='todo-list']//label";
	static final String xpath_count_text = "//footer//span";
	static final String xpath_todo_lineitem_all = "//ul[@class='todo-list']/li";
	static final String xpath_todo_lineitem_specific = "//ul[@class='todo-list']/li[div/label[text()='__TODO__']]";
	static final String xpath_todo_complete = "//div[label[text()='__TODO__']]//input[@type='checkbox']";
	static final String xpath_todo_checkboxes = "//input[@type='checkbox' and @class!='toggle-all']";
	   
	//Tab XPath, wherein __TAB__ can be: All, Active or Completed.
	static final String xpath_tab = "//a[text()='__TAB__']";
	static final String xpath_toggle_all = "//label[@for='toggle-all']";
	
	static final String xpath_todo_clear_specific = "//li[div/label[text()='__TODO__']]/div/button";  //Clear a ToDo.
	static final String xpath_todo_clear_all = "//footer/button[contains(text(),'Clear completed')]";  //Clear all ToDo.
	
	static final String xpath_todo_edit = "//li[div/label[text()='__TODO__']]/input[@type='text']"; //Edit a ToDo
	static final String xpath_todo_label = "//label[text()='__TODO__']"; //Edit a ToDo

	public ToDoApp(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Create a ToDo.
	 * 
	 * @param todo ToDo to be created.
	 * @return True if success. Else, False.
	 */
	public boolean createToDo(String todo) {
		boolean status = false;
		try {
			driver.findElement(By.xpath(xpath_todo_create)).sendKeys(todo, Keys.ENTER);
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
	public boolean editToDo(String todo, String newToDo, String tab) {
		boolean status = false;
		try {
			Actions actions = new Actions(driver);			
		    String effective_path_label = xpath_todo_label.replace("__TODO__", todo);
			List<WebElement> toDoElements = driver.findElements(By.xpath(effective_path_label));
			if (toDoElements.size() == 0) {
				System.out.println("ERROR: Todo not found");
				return status;
			} 
			
			WebElement toDoElement = toDoElements.get(0);
			actions.doubleClick(toDoElement).perform();
			sleep(1);
			
			int todo_count_initial = driver.findElements(By.xpath(xpath_todo_label.replace("__TODO__", newToDo))).size();
			String effective_path_edit = xpath_todo_edit.replace("__TODO__", todo);
			toDoElements = driver.findElements(By.xpath(effective_path_edit));
			if (toDoElements.size() == 0) {
				System.out.println("ERROR: Todo input box not found");
				return status;
			} 
			toDoElement = toDoElements.get(0);
			System.out.println(newToDo);
			toDoElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.BACK_SPACE);
			toDoElement.sendKeys(newToDo, Keys.ENTER);
			sleep(1);
			int todo_count_final = driver.findElements(By.xpath(xpath_todo_label.replace("__TODO__", newToDo))).size();
			if (todo_count_final == todo_count_initial + 1) {
				System.out.println("ToDo is successfully edited.");
				status = true;
			} else {
				System.out.println("ToDo edit failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Validate a ToDo.
	 * @param todo     ToDo to be validated.
	 * @param position Position of the ToDo
	 * @return True if success. Else, False.
	 */
	public boolean validateToDo(String todo, int position) {
		boolean status = false;
		try {
			List<WebElement> todoList = driver.findElements(By.xpath(xpath_todo_list));
			if (position <= todoList.size()) {
				System.out.println("Position is valid.");
				WebElement todo_element = todoList.get(position);
				if (todo_element.getText().equals(todo)) {
					System.out.println("Text matches");
					status = true;
				} else {
					System.out
							.println("Text does not match. Expected " + todo + " but found " + todo_element.getText());
				}
			} else {
				System.out.println("Position is invalid.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Validate a ToDo list count.
	 * 
	 * @param todo     ToDo to be validated.
	 * @param position Position of the ToDo
	 * @return True if success. Else, False.
	 */
	public boolean validateToDoCount(int countOfToDoAdded) {
		boolean status = false;
		try {
			List<WebElement> todoList = driver.findElements(By.xpath(xpath_todo_list));
			String footer_text_observed = driver.findElement(By.xpath(xpath_count_text)).getText();
			String footer_text_expected = countOfToDoAdded + " items left";

			if (countOfToDoAdded == 1) {
				footer_text_expected = countOfToDoAdded + " item left";
			}
			System.out.println(footer_text_observed);
			if ((todoList.size() == countOfToDoAdded) && (footer_text_observed.equals(footer_text_expected))) {
				System.out.println("Footer text matches");
				status = true;
			} else {
				System.out.println("ERROR: Footer text does not match-[" + footer_text_observed + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Complete a ToDo.
	 * @param todo ToDo to be completed.
	 * @return True if success. Else, False.
	 */
	public boolean completeToDo(String todo) {
		boolean status = false;
		try {
			String effective_xpath = xpath_todo_complete.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			if (matched_todo_list.size() == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}
			WebElement first_match = matched_todo_list.get(0);
			if (first_match.isSelected()) {
				System.out.println("Already completed");
			} else {
				first_match.click();
				sleep(1);
				first_match = matched_todo_list.get(0);
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
	
	
	/**
	 * Clear a ToDo from the specified tab.
	 * @param todo ToDo to be cleared.
	 * @return True if success. Else, False.
	 */
	public boolean clearToDo(String todo, String tab) {
		boolean status = false;
		try {
			if (tabSwitch(tab)) {
				System.out.println("Switch to tab " + tab + ". Proceeding further..");
			} else {
				System.out.println("Switch to tab " + tab + " failed.");
				return status;
			}
			
			String effective_xpath = xpath_todo_clear_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			int matched_todo_count_initial = matched_todo_list.size();
			if (matched_todo_count_initial == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}
			
			effective_xpath = xpath_todo_complete.replace("__TODO__", todo);
			matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			
			//Execute clear on the ToDo.
			WebElement first_match = matched_todo_list.get(0);
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
				status = true;
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
	 * Verify Completed Tab is empty.
	 * @return True if success. Else, False.
	 */
	public boolean verifyCompletedTabIsEmpty() {
		boolean status = false;
		try {			
			if (!tabSwitch("Completed")) {
				return status;
			} 
			//Confirm the completed ToDos list is empty.
			List<WebElement> completed_todo = driver.findElements(By.xpath(xpath_todo_lineitem_all));			
			if (completed_todo.size() == 0) {
				System.out.println("Completed ToDo list is empty as expected.");
				status = true;
			} else {
				System.out.println("ERROR: Completed ToDo list is not empty while we expected it to be empty.");
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
			driver.findElement(By.xpath(xpath_toggle_all)).click();
			List<WebElement> todo_list = driver.findElements(By.xpath(xpath_todo_lineitem_all));
			int todo_list_count = todo_list.size();
			int todo_completed_count = 0;
			for (WebElement we : todo_list) {
				if (we.getAttribute("class").contains("completed")) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Validate a ToDo is completed.
	 * @param todo ToDo which was completed.
	 * @return True if success. Else, False.
	 */
	public boolean validateCompletionOfToDo(String todo) {
		boolean status = false;
		try {
			String effective_xpath = xpath_todo_lineitem_specific.replace("__TODO__", todo);
			List<WebElement> matched_todo_list = driver.findElements(By.xpath(effective_xpath));
			if (matched_todo_list.size() == 0) {
				System.out.println("No ToDo found for the string:" + todo);
				return status;
			}

			WebElement first_match = matched_todo_list.get(0);
			if (first_match.getAttribute("class").contains("completed")) {
				System.out.println("Text is striked through");
				status = true;
			} else {
				System.out.println("ERROR: Text is not striked through");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Validate presence/absence of ToDo items in specified tab.
	 * 
	 * @param toDoList List of ToDo to verify.
	 * @param tab      Tab to be verified. Possible options: All, Completed, Active.
	 * @param verify   Type of verification. Possible options: presence, absence.
	 * @return True if success. Else, False.
	 */
	public List<Boolean> verifyStatusOfToDoInTab(List<String> ToDoList, String tab, String verify) {
		List<Boolean> status = new ArrayList<>();
		try {
			// Switch to the required tab
			if (tabSwitch(tab)) {
				System.out.println("Switch to tab " + tab + ". Proceeding further..");
			} else {
				System.out.println("Switch to tab " + tab + " failed.");
			}

			for (String toDo : ToDoList) {
				boolean toDo_status = false;
				String effective_xpath = xpath_todo_lineitem_specific.replace("__TODO__", toDo);
				List<WebElement> toDoElements = driver.findElements(By.xpath(effective_xpath));

				if (toDoElements.size() == 0) {
					if (verify.equals("present")) {
						System.out.println("ERROR: No ToDo item found while it's presence was expected");
					} else if (verify.equals("absent")) {
						System.out.println("No ToDo item found as expected");
						toDo_status = true;
					}
				} else {
					if (verify.equals("absent")) {
						System.out.println("ERROR: ToDo item found while it's absence was expected");
					} else {
						for (WebElement we : toDoElements) {
							if (we.findElement(By.xpath("./div/label")).getText().equals(toDo)) {
								System.out.println("Found the ToDo item.");

								// Validate if it is stroked out if tab is completed. Else, it is not striked
								// out.
								if (tab.equals("Completed") && we.getAttribute("class").contains("completed")) {
									System.out.println("ToDo is crossed out");
									toDo_status = true;
									break;
								} else if (tab.equals("Active") && !we.getAttribute("class").contains("completed")) {
									System.out.println("ToDo is not crossed out");
									toDo_status = true;
									break;
								} else if (tab.equals("All")) {
									toDo_status = true;
									break;
								}
							}
						}
					}
				}
				status.add(toDo_status);
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

}