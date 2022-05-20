Feature: todo

Scenario Outline: Create ToDo
	Given Application is launched
	When ToDo <toDo> is entered
	Then ToDo <toDo> is created
		And Verify <toDo> is "present" in "Active" Tab
		And Verify <toDo> is "absent" in "Completed" Tab
	Examples:
		|toDo      	   |
		|"wake up"      |
		|"Brush my teeth"|
		|"Clean utensils"|
		|"Prepare food :)"|
		|"Eat food!!"|
		|"Clean utensils"|
		|"Separate the dry and wet kitchen waste. Accordingly, put the waste into the respective garbage bins."|

Scenario Outline: Edit ToDo
	Given Application is launched
	When ToDo <toDo> is edited to <newToDo> in <tab> Tab
	Then Verify <newToDo> is "present" in "All" Tab
		And Verify <newToDo> is "present" in <tab> Tab		
	Examples:
		|toDo      	      |newToDo                    |tab     |
		|"wake up"        |"wake up!!"                |"All"   |
		|"Brush my teeth" |"Brush my teeth and bathe" |"Active"|