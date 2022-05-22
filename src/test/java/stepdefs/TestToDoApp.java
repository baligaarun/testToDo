package stepdefs;

import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.ToDoApp;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.List;

/**
 * Cucumber based Step Definitions program containing the tests defined for ToDo Application.
 * 
 * @author Arun Baliga (baligaarun@gmail.com)
 * @since 19.05.2022
 */
public class TestToDoApp {
	// User variables - To be moved as command line arguments in future.
	static String browser = "chrome";
	static String application_url = "https://todomvc.com/examples/vue/";

	// Lists tracking the ToDo items in Active and Completed states at any point of time.
	private static List<String> completed_todo_list = new ArrayList<>();
	private static List<String> active_todo_list = new ArrayList<>();

	// ToDoApp is the Page Object Model based class containing business logic.
	private static ToDoApp objToDoApp = null;

	/**
	 * Launch ToDo Application.
	 */
	@Given("Application is launched")
	public void launch_todo_app() {
		System.out.println("\n\n###############################\n");
		System.out.format("Counts: completed_todo_list --> %d, active_todo_list --> %d\n", completed_todo_list.size(), active_todo_list.size());
		if (objToDoApp != null) {
			System.out.println("Application is already launched. Re-using the session...");
			objToDoApp.tabSwitch("All");
		} else {
			System.out.println("Launching application.");
			objToDoApp = new ToDoApp(application_url, browser);
			objToDoApp.validateToDoForm();
		}
		System.out.println("Application URL --> " + objToDoApp.getApplicationUrl());
	}

	/**
	 * Create ToDo.
	 * 
	 * @param toDo - To be parsed from the scenario outline.
	 */
	@When("ToDo {string} is created")
	public void create_a_todo(String toDo) {	
		if (objToDoApp.createToDo(toDo)) {
			active_todo_list.add(toDo);
		} else {
			fail("ToDo creation failed.");
		}
	}

	/**
	 * Complete a ToDo.
	 * 
	 * @param toDo - To be parsed from scenario outline.
	 */
	@When("ToDo {string} is completed")
	public void complete_a_todo(String toDo) {
		if (objToDoApp.completeToDo(toDo)) {
			active_todo_list.remove(toDo);
			completed_todo_list.add(toDo);
		} else {
			fail("complete_a_todo - Failed");
		}
	}

	/**
	 * Complete all ToDo (bulk operation)
	 */
	@When("All ToDo are completed")
	public void complete_all_todo() {
		if (objToDoApp.completeAllToDo()) {
			completed_todo_list.addAll(active_todo_list);
			active_todo_list.clear();
		} else {
			fail("complete_all_todo - Failed");
		}
	}

	/**
	 * Re-Open a completed ToDo
	 * 
	 * @param toDo - To be parsed from scenario outline.
	 */
	@When("ToDo {string} is re-opened")
	public void reopen_a_todo(String toDo) {
		if (objToDoApp.reOpenToDo(toDo)) {
			System.out.println("Prior-" + completed_todo_list.size());
			completed_todo_list.remove(toDo);
			System.out.println("Post-" + completed_todo_list.size());
			active_todo_list.add(toDo);
		} else {
			fail("reopen_a_todo - Failed");
		}
	}
	
	/**
	 * Re-Open all ToDo (bulk operation)
	 */
	@When("All ToDo are re-opened")
	public void reopen_all_todo() {
		if (objToDoApp.reOpenAllToDo()) {
			active_todo_list.addAll(completed_todo_list);
		} else {
			fail("reopen_todo_all - Failed");
		}
	}

	/**
	 * Edit a ToDo (Double Click). Parameters are parsed from scenario outline.
	 * 
	 * @param toDo - Existing ToDo.
	 * @param newToDo - New ToDo
	 * @param tab - Tab in which the Edit should be done.
	 */
	@When("ToDo {string} is edited to {string} in {string} Tab")
	public void edit_todo(String toDo, String newToDo, String tab) {
		String result = objToDoApp.editToDo(toDo, newToDo, tab);
		int index = 0;
		if (result.equals("active")) {
			index = active_todo_list.indexOf(toDo);
			active_todo_list.set(index, newToDo);
		} else if (result.equals("completed")) {
			index = completed_todo_list.indexOf(toDo);
			completed_todo_list.set(index, newToDo);
		} else {
			fail("edit_todo - Failed");
		}
	}

	/**
	 * Clear a ToDo (i.e. Delete). Parameters are parsed from scenario outline.
	 * 
	 * @param toDo - Existing ToDo.
	 * @param tab - Tab in which the clear should be done.
	 */
	@When("ToDo {string} is cleared from {string} Tab")
	public void clear_a_todo(String toDo, String tab) {
		String result = objToDoApp.clearToDo(toDo, tab);
		if (result.equals("active")) {
			active_todo_list.remove(toDo);
		} else if (result.equals("completed")) {
			completed_todo_list.remove(toDo);
		} else {
			fail("clear_a_todo - Failed");
		}
	}

	/**
	 * Clear all completed ToDo (bulk operation)
	 */
	@When("All Completed ToDo are cleared")
	public void clear_all_completed_todo() {
		if (objToDoApp.clearAllCompletedToDo()) {
			completed_todo_list.clear();
		} else {
			fail("clear_all_completed_todo - Failed");
		}
	}

	/**
	 * Verify specified tab has no ToDos. Parameters are parsed from scenario outline.
	 * 
	 * @param tab - Tab to be verified.
	 */
	@Then("Verify {string} Tab is empty")
	public void verify_tab_is_empty(String tab) {
		if (!objToDoApp.verifyTabIsEmpty(tab)) {
			fail("verify_tab_is_empty(" + tab + ") - Failed");
		}
	}

	/**
	 * Verify status of a ToDo in the specified tab. Parameters are parsed from scenario outline.
	 * 
	 * @param toDo - ToDo to be verified.
	 * @param verify - Type of verification. Possible values - present, absent.
	 * @param tab - Tab to be used for verification.
	 */
	@Then("Verify {string} is {string} in {string} Tab")
	public void verify_status_of_todo_in_tab(String toDo, String verify, String tab) {
		if (!objToDoApp.verifyStatusOfToDoInTab(toDo, tab, verify, active_todo_list, completed_todo_list)) {
			fail("verify_status_of_todo_in_tab - Check failed");
		}
	}
	
	/**
	 * Verify Items Left count in the footer section is valid.
	 */
	@Then("Verify items left is valid")
	public void verifyItemsLeftIsValid() {
		if (!objToDoApp.validateItemsLeft()) {
			fail("verifyItemsLeftIsValid - Failed");
		}
	}
	
	/**
	 * Tear Down to be executed after all tests.
	 */
	@AfterAll
    public static void tearDown(){
		System.out.println("Closing the application");
		objToDoApp.closeApplication();
	}
}