--------------------------------------------------------------------------------------------------
Iteration 1
Group: L2-G6
Members: Lasitha Amuwala Meesthrige, Bonita Hout, Navaty Khara, Tyler Mak, Anant Ojha
Date: February 5, 2022
--------------------------------------------------------------------------------------------------
Files in this project:

src/Elevator:
Direction.java: Enum class that declares a type called Direction that can be either Up or Down.
Elevator.java: Elevator thread class that retrieves a request from the request table in the scheduler class, validates if request can be done, and services the request.
Floor.java: Floor thread class that reads requests in csv files in FloorCSV folder and adds them to the request table in the scheduler class. 
Main.java: Creates the csv files in FloorCSV folder, a single floor thread called "Floor 1", a single elevator thread called "Elevator 1", and starts the floor and elevator threads.
Request.java: Request data structure that holds information about the time of request, floor of request, direction from source floor to destination floor, and destination floor.
Scheduler.java: Creates request table that holds all requests, puts requests from Floor thread into request table, gets requests for Elevator thread from request table, and services request when Elevator thread completes a request.

Documentation:
README.txt: Describes files in the project, setup instructions, how to run the program, and each group member's work contribution. 
Class_Diagram.png: The UML class diagram for Iteration 1 of Elevator Project.
Sequence_Diagram.png: The UML sequence diagram for Iteration 1 of Elevator Project.
--------------------------------------------------------------------------------------------------
Running the program:
To run the program, invoke the main function in Main.java which will also run the other classes.
Using Eclipse, import the project -> Open Main.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the Console Window.
--------------------------------------------------------------------------------------------------
Groupwork Responsibilities:

Lasitha Amuwala Meesthrige: 

Bonita Hout:

Navaty Khara: 
	Fixed out-of-bounds issue.
	In-code comments for documentation
Tyler Mak: 

Anant Ojha: 
	Implemented Floor, Elevator & Scheduler
	Implemented JUnit Tests:
					1. inputCsvGeneration()
	
	
	
