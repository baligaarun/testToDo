package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.ToDoApp;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestToDoApp { 
	   private static WebDriver driver = new FirefoxDriver(); 
	   private static List<String> todo_list = new ArrayList<>();
	   ToDoApp objToDoApp = new ToDoApp(driver);
	   String application_url = "https://todomvc.com/examples/vue/";

	   @Given("Application is launched") 
	   public void launchApplication() {
		   System.out.println("\n\n\n" + driver.getCurrentUrl());
		   if (driver.getCurrentUrl().equals(application_url)) {
			   System.out.println("\n\n**************Application is already launched***************");
		   } else {
			   System.setProperty("webdriver.gecko.driver", "geckodriver.exe");	   
			   driver.navigate().to(application_url); 
			   objToDoApp.validateToDoForm();
		   }
	   } 
		
	   @When("ToDo {string} is entered")
	   public void enter_todo(String toDo) { 
		   todo_list.add(toDo);
		   System.out.println("There are " + todo_list.size() + " items in the list");
		   objToDoApp.createToDo(toDo);
		   objToDoApp.sleep(1);
	   } 
	   
	   @Then("ToDo {string} is created")
	   public void validate_todo_is_created(String toDo) { 
		   System.out.println("Validate ToDo");
		   boolean todo_status = objToDoApp.validateToDo(toDo, todo_list.size()-1);
		   boolean count_status = objToDoApp.validateToDoCount(todo_list.size());
		   System.out.println(todo_status + "," + count_status);
		   if (todo_status && count_status) {
			   System.out.println("Check passed");
		   } else {
			   fail("validate_todo_is_created - Failed");
		   }
		   objToDoApp.validateToDoCount(todo_list.size());
	   } 
	   
	   @When("ToDo {string} is marked complete")
	   public void move_todo_to_complete(String toDo) {
		   boolean status;
		   if (toDo.equals("__ALL__")) {
			   //Complete all ToDo items in bulk using the toggle option.
			   status = objToDoApp.completeAllToDo();
		   } else {
			   //Complete the specific ToDo item using the linked check-box.
			   status = objToDoApp.completeToDo(toDo);
		   }
		   if (!status) {
			   fail("move_todo_to_complete - Failed");
		   }
		   objToDoApp.sleep(1);
	   }
	   
	   @When("ToDo {string} is edited to {string} in {string} Tab")
	   public void edit_todo(String toDo, String newToDo, String tab) {
		   if (!objToDoApp.editToDo(toDo, newToDo, tab)) {
			   fail("edit_todo - Failed");
		   }
		   objToDoApp.sleep(1);
	   } 
	   
	   @When("ToDo {string} is cleared from {string} Tab")
	   public void clear_todo(String toDo, String tab) {
		   if (!objToDoApp.clearToDo(toDo, tab)) {
			   fail("clear_todo - Failed");
		   }
		   objToDoApp.sleep(1);
	   } 
	   
	   @When("All Completed ToDo are cleared")
	   public void clear_todo_completed() {
		   if (!objToDoApp.clearAllCompletedToDo()) {
			   fail("clear_todo_completed - Failed");
		   }
		   objToDoApp.sleep(1);
	   } 
	   
	   @Then("Verify Completed Tab is empty")
	   public void verify_completed_tab_is_empty() { 
		   if(!objToDoApp.verifyCompletedTabIsEmpty()) {
			   fail("verify_completed_tab_is_empty - Failed");
		   } else {
			   System.out.println("verify_completed_tab_is_empty - Success");
		   }
	   } 
	   
	   @Then("ToDo {string} is completed")
	   public void validate_todo_is_complete(String toDo) { 
		   objToDoApp.sleep(1);
	   } 
	   
	   @Then("Verify {string} is {string} in {string} Tab")
	   public void verifyToDoStatusInTab(String toDo, String verify, String tab) {
		   List<Boolean> status = objToDoApp.verifyStatusOfToDoInTab(Arrays.asList(toDo), tab, verify);
		   if (status.contains(false)) {
			   fail("Check failed");
		   }			   
	   }
	}