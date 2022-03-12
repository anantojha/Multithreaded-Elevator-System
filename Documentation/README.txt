--------------------------------------------------------------------------------------------------
Iteration 3
Group: L2-G6
Members: Lasitha Amuwala Meesthrige, Bonita Hout, Navaty Khara, Tyler Mak, Anant Ojha
Date: March 9th, 2022
--------------------------------------------------------------------------------------------------
Files in this project:

src/Elevator/ElevatorSubsystem:
Elevator.java: Elevator thread class that retrieves a request from the scheduler class, validates if request can be done, and services the request. Main method in this class creates and starts one elevator thread.
ElevatorState.java: State class for Elevator.java with setter and getter methods for elevator data.

src/Elevator/Enums:
Direction.java: Enum class that declares a type called Direction that can be either UP or DOWN.
ElevatorStatus.java: Enum class that declares Elevator States. (RUNNING, ARRIVED, IDLE, OPEN DOOR, CLOSE DOOR, TERMINATE)
FloorStatus.java: Enum class that declares Floor States. (INITIALIZE, PROCESSING, SENDING, WAITING)
SchedulerStatus.java: Enum class that declares Scheduler States. (INITIALIZE, CONNECTED ELEVATOR, CONNECTED FLOOR, ADDING REQUEST, SEND REQUEST, COMPLETE REQUEST)

src/Elevator/FloorSubsystem:
Floor.java: Floor thread class that creates requests in a csv file stored in FloorCSV folder, reads the requests from created csv files and sends the request to the scheduler class. Main method in this class creates and starts 10 floor threads.
FloorState.java: State class for Floor.java with set and get methods for Floor state.
Request.java: Request data structure that holds information about the time of request, floor of request, direction from source floor to destination floor, and destination floor.

src/Elevator/Global:
SystemConfiguration.java: System data that defines maximum floor and elevators.

src/Elevator/SchedulerSubsystem:
Scheduler.java: Contains a Scheduler class and a ServerThread thread class. The Scheduler class has a queue for all requests, a SchedulerState for it's state and 2 arraylists of ServerThread for floor threads and elevator threads respectively. The Scheduler class when started creates and starts a ServerThread for every connected elevator thread (max 2) and floor thread (unlimited). The ServerThread thread class receives requests from the connected floor threads, prints the requests, then sends the requests to the connected elevator threads.
SchedulerState.java: State class for Scheduler.java that holds queue for all requests, arraylist of ElevatorStates, and state of scheduler. Implements adding and getting elevators, getting queue of requests, checking and setting request avilability, getting and setting number of completed requests, and getting and setting scheduler state.

src/ElevatorTest:
FileTests.java: UnitTest for file creation.
SystemTest.java: UnitTest for entire system.

Documentation:
README.txt: Describes files in the project, setup instructions, how to run the program, and each group member's work contribution. 
Class_Diagram_Iteration2.png: The UML class diagram for Iteration 2 of Elevator Project.
Sequence_Diagram.png: The UML sequence diagram for Iteration 2 of Elevator Project.
StateMachine_Diagram.png: The UML state machine diagram for Iteration 2 of Elevator Project.
--------------------------------------------------------------------------------------------------
Running the program: To run the program, invoke the main function in the following programs in the order: Scheduler.java -> Elevator.java -> Elevator.java -> Floor.java. Each invocation of a program will have a separate output meaning a total of 4 Console Windows will be needed.
Using Eclipse, import the project -> Open Scheduler.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the Console Window, to ensure the Console Window stays focused on Scheduler.java click on 'Pin Console'.
Create a new Console Window by: Select 'Open Console' -> Select '1 New Console View'.
Open Elevator.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application' -> Enter '1' as Elevator Id.
Output will be shown on the 2nd Console Window, to ensure the 2nd Console Window stays focused on Elevator.java click on 'Pin Console'.
Repeat past 3 steps again for a 2nd Elevator.java instance with '2' as the Elevator Id.
Create a new Console Window by: Select 'Open Console' -> Select '1 New Console View'.
Open Floor.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the 4th Console Window, to ensure the 4th Console Window stays focused on Floor.java click on 'Pin Console'.

Running Unit Test:
Import JUnit 4 library from 'configure project build path' settings. 
Right click on test file and run as 'JUnit Test'.
--------------------------------------------------------------------------------------------------
Groupwork Responsibilities:

Lasitha Amuwala Meesthrige: 
	
	
Bonita Hout:
	

Navaty Khara: 
	Added comments to State enums and System configuration
	Updated Sequence Diagram
	
Tyler Mak: 
	Added comments to Elevator.java, Floor.java, Scheduler.java, and SystemTest.java.
	Updated READEME.txt

Anant Ojha: 
	Implemented Socket Communication for Scheduler (Server);
	Implemented Socket Communication for Floor (Client);
	Implemented Socket Communication for Elevator (Client);
	Refactored state machines for each subsystems;
	Refactored System to handle 10 Floors, and 2 Elevator connections concurrently; 
	Updated Unit Tests to work with sockets;
	
	
	
	
	
