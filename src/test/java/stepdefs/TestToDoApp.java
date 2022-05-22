package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.ToDoApp;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.List;

public class TestToDoApp {
	//User defined variables. To be eventually moved as arguments in execution environment.
	static String browser = "chrome";
	static String application_url = "https://todomvc.com/examples/vue/";	

	private static List<String> completed_todo_list = new ArrayList<>();
	private static List<String> active_todo_list = new ArrayList<>();
	private static ToDoApp objToDoApp = null;

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

	@When("ToDo {string} is created")
	public void create_a_todo(String toDo) {	
		if (objToDoApp.createToDo(toDo)) {
			active_todo_list.add(toDo);
		} else {
			fail("ToDo creation failed.");
		}
	}

	@When("ToDo {string} is completed")
	public void complete_a_todo(String toDo) {
		if (objToDoApp.completeToDo(toDo)) {
			active_todo_list.remove(toDo);
			completed_todo_list.add(toDo);
		} else {
			fail("complete_a_todo - Failed");
		}
	}

	@When("All ToDo are completed")
	public void complete_all_todo() {
		if (objToDoApp.completeAllToDo()) {
			completed_todo_list.addAll(active_todo_list);
			active_todo_list.clear();
		} else {
			fail("complete_all_todo - Failed");
		}
	}

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
	
	@When("All ToDo are re-opened")
	public void reopen_all_todo() {
		if (objToDoApp.reOpenAllToDo()) {
			active_todo_list.addAll(completed_todo_list);
		} else {
			fail("reopen_todo_all - Failed");
		}
	}

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

	@When("All Completed ToDo are cleared")
	public void clear_all_completed_todo() {
		if (objToDoApp.clearAllCompletedToDo()) {
			completed_todo_list.clear();
		} else {
			fail("clear_all_completed_todo - Failed");
		}
	}

	@Then("Verify {string} Tab is empty")
	public void verify_tab_is_empty(String tab) {
		if (!objToDoApp.verifyTabIsEmpty(tab)) {
			fail("verify_tab_is_empty(" + tab + ") - Failed");
		}
	}

	@Then("Verify {string} is {string} in {string} Tab")
	public void verify_status_of_todo_in_tab(String toDo, String verify, String tab) {
		if (!objToDoApp.verifyStatusOfToDoInTab(toDo, tab, verify, active_todo_list, completed_todo_list)) {
			fail("verify_status_of_todo_in_tab - Check failed");
		}
	}
	
	@Then("Verify items left is valid")
	public void verifyItemsLeftIsValid() {
		if (!objToDoApp.validateItemsLeft()) {
			fail("verifyItemsLeftIsValid - Failed");
		}
	}
}