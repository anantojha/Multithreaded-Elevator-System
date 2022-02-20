--------------------------------------------------------------------------------------------------
Iteration 2
Group: L2-G6
Members: Lasitha Amuwala Meesthrige, Bonita Hout, Navaty Khara, Tyler Mak, Anant Ojha
Date: February 19, 2022
--------------------------------------------------------------------------------------------------
Files in this project:

src/Elevator:
Main.java: Creates the csv files in FloorCSV folder, a single floor thread called "Floor 1", two elevator threads called "Elevator 1 & Elevator 2", and initializes the state machine and scheduler, and starts the floor and elevator threads.
StateMachineState.java: 

src/Elevator/ElevatorSubsystem:
Elevator.java: Elevator thread class that retrieves a request from the scheduler class, validates if request can be done, and services the request. 
ElevatorState.java: State class for Elevator.java with setter and getter methods for elevator data.

src/Elevator/Enums:
Direction.java: Enum class that declares a type called Direction that can be either Up or Down.
ElevatorStatus.java: Enum class that declares Elevator States. (RUNNING, ARRIVED, IDLE, OPEN DOOR, CLOSED DOOR, TERMINATE)
FloorStatus.java: Enum class that declares Floor States. (INITIALIZE, PROCESSING, SENDING, WAITING)
SchedulerStatus.java: Enum class that declares Scheduler States. (INITIALIZE, ADDINGREQUEST, PRINTREQUEST, COMPLETEREQUEST)
StateMachineStatus.java: Enum class that declares State Machine States. (INITIALIZE, RUNNING, ENDING)

src/Elevator/FloorSubsystem:
Floor.java: Floor thread class that reads requests in csv files in FloorCSV folder and sends the request to the scheduler class. 
FloorState.java: State class for Floor.java with set and get methods for Floor state.
Request.java: Request data structure that holds information about the time of request, floor of request, direction from source floor to destination floor, and destination floor.

src/Elevator/Global:
SystemConfiguration.java: System data that defines maximum floor and elevators.

src/Elevator/SchedulerSubsystem:
Scheduler.java: Creates SchedulerState data structure that holds all requests and state of scheduler, puts requests from Floor thread into SchedulerState, gets requests for Elevator thread from SchedulerState, and services request when Elevator thread completes a request.
SchedulerState.java: State class for Scheduler.java that holds queue for all requests, arraylist of ElevatorStates, and state of scheduler. Implements adding and getting elevators, getting queue of requests, checking and setting request avilability, getting and setting number of completed requests, and getting and setting scheduler state.

src/ElevatorTest:
ElevatorTests.java: UnitTest for Elevator.java that also checks Scheduler.java states.
FileTests.java: UnitTest for file creation.
FloorTest.java: UnitTest for Floor.java that also checks Scheduler.java states.

Documentation:
README.txt: Describes files in the project, setup instructions, how to run the program, and each group member's work contribution. 
Class_Diagram_Iteration2.png: The UML class diagram for Iteration 2 of Elevator Project.
Sequence_Diagram.png: The UML sequence diagram for Iteration 2 of Elevator Project.
StateMachine_Diagram.png: The UML state machine diagram for Iteration 2 of Elevator Project.
--------------------------------------------------------------------------------------------------
Running the program:
To run the program, invoke the main function in Main.java which will also run the other classes.
Using Eclipse, import the project -> Open Main.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the Console Window.

Running Unit Test:
Import JUnit 4 library from 'configure project build path' settings. 
Right click on test file and run as 'JUnit Test'.
--------------------------------------------------------------------------------------------------
Groupwork Responsibilities:

Lasitha Amuwala Meesthrige: 
	
	
Bonita Hout:
	UML class diagram

Navaty Khara: 
	Implemented Floor & Main state machines
	Expanded on Scheduler state machines
	
	
Tyler Mak: 
	Implemented Unit Testing for Floor subsystem
	Updated other unit tests.
	Updated README.txt

Anant Ojha: 
	Implemented Elevator & Scheduler state machines. 
	
	
	
	
