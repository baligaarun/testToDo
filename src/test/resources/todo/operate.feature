Feature: todo

Scenario Outline: Create ToDo
	Given Application is launched
	When ToDo <toDo> is created
	Then Verify <toDo> is "present" in "All" Tab
		And Verify <toDo> is "present" in "Active" Tab
		And Verify <toDo> is "absent" in "Completed" Tab
		And Verify items left is valid
	Examples:
		|toDo      	   |
		|"wake up"      |
		|"Brush my teeth"|
		|"Clean utensils"|
		|"Prepare food :)"|
		|"Eat food!!"|
		|"Clean utensils"|
		|"Separate the dry and wet kitchen waste. Accordingly, put the waste into the respective garbage bins."|
		|"Read books"|

Scenario Outline: Complete ToDo
	Given Application is launched
	When ToDo <toDo> is completed
	Then Verify <toDo> is "present" in "All" Tab
		And Verify <toDo> is "absent" in "Active" Tab
		And Verify <toDo> is "present" in "Completed" Tab
	Examples:
		|toDo      	    |
		|"wake up"      |
		|"Eat food!!"   |
		|"Read books"   |

Scenario Outline: Edit ToDo
	Given Application is launched
	When ToDo <toDo> is edited to <newToDo> in <tab> Tab
	Then Verify <newToDo> is "present" in "All" Tab
		And Verify <newToDo> is "present" in <tab> Tab	
	Examples:
		|toDo      	      |newToDo                    |tab        |
		|"wake up"        |"wake up!!"                |"All"      |
		|"Brush my teeth" |"Brush my teeth and bathe" |"Active"   |
		|"Eat food!!"     |"Eat healthy food!!"       |"Completed"|
		
Scenario Outline: Re-Open ToDo
	Given Application is launched
	When ToDo <toDo> is re-opened
	Then Verify <toDo> is "present" in "All" Tab
		And Verify <toDo> is "present" in "Active" Tab
		And Verify <toDo> is "absent" in "Completed" Tab
	Examples:
		|toDo      	            |
		|"wake up!!"            |
		|"Eat healthy food!!"   |

Scenario Outline: Clear ToDo
	Given Application is launched
	When ToDo <toDo> is cleared from <tab> Tab
	Then Verify <toDo> is "absent" in "All" Tab
		And Verify <toDo> is "absent" in "Active" Tab
		And Verify <toDo> is "absent" in "Completed" Tab
	Examples:
		|toDo      	                |tab        |
		|"Brush my teeth and bathe" |"All"      |
		|"Read books"               |"Completed"|
		|"Eat healthy food!!"       |"Active"   |

Scenario Outline: Complete ToDo - Repeat
	Given Application is launched
	When ToDo <toDo> is completed
	Then Verify <toDo> is "present" in "All" Tab
		And Verify <toDo> is "absent" in "Active" Tab
		And Verify <toDo> is "present" in "Completed" Tab
	Examples:
		|toDo      	      |
		|"Clean utensils" |
		|"wake up!!"      |

Scenario Outline: Clear all completed
	Given Application is launched
	When All Completed ToDo are cleared
	Then Verify "Completed" Tab is empty
	
Scenario Outline: All ToDo are completed
	Given Application is launched
	When All ToDo are completed
	Then Verify "Active" Tab is empty
		
Scenario Outline: All ToDo are re-opened
	Given Application is launched
	When All ToDo are re-opened
	Then Verify "Completed" Tab is empty