--------------------------------------------------------------------------------------------------
Iteration 5
Group: L2-G6
Members: Lasitha Amuwala Meesthrige, Bonita Hout, Navaty Khara, Tyler Mak, Anant Ojha
Date: April 11th, 2022
--------------------------------------------------------------------------------------------------
Demo Link:	 https://youtu.be/w3qq8DSZHgA 
Github Link:	 https://github.com/anantojha/SYSC3303-Elevator-Project 

Files in this project:
elevator_close.jpeg: Image of closed doors elevator for GUI
elevator_open.jpeg: Image of open doors elevator for GUI

src/Elevator/ElevatorSubsystem:
Elevator.java: Elevator thread class that connects to the GUI and services the requests in it's queue. 
ElevatorContext.java: Class that holds elevator's data such as the floor and direction.
ElevatorController.java: Thread class that sends and receives DatagramPackets to scheduler, adds requests to queue, initializes GUI, and starts Elevator thread.
ElevatorState.java: Statemachine class for Elevator.java which updates and gets elevator states. Also holds the Elevator's id number.

src/Elevator/Enums:
Direction.java: Enum class that declares a type called Direction that can be either UP or DOWN.
ElevatorStatus.java: Enum class that declares Elevator States. (INITIALIZE, IDLE, RUNNING, ARRIVED, OPEN_DOOR, CLOSE_DOOR, FAULT_DETECTED, RESETTING, RESUMING, TERMINATE)
FloorStatus.java: Enum class that declares Floor States. (INITIALIZE, PROCESSING, SENDING, RECEIVING, WAITING, ERROR)
SchedulerStatus.java: Enum class that declares Scheduler States. (INITIALIZE, CONNECTED ELEVATOR, CONNECTED FLOOR, ADDING REQUEST, SEND REQUEST, COMPLETE REQUEST)
SubsystemMapping.java: Enum class that declares each subsystem. 

src/Elevator/FloorSubsystem:
Floor.java: Floor thread class that creates requests in a single csv file stored in CSV/FloorCSV folder, reads the requests from created csv file and sends the request to the scheduler class. Main method in this class creates and starts 22 floor threads.
FloorState.java: State class for Floor.java with update and get methods for Floor state.
Request.java: Request data structure that holds information about the time of request, floor of request, direction from source floor to destination floor, destination floor, and whether fault occurs or not.

src/Elevator/Global:
PacketHelper.java: Helper class for data serialization for UDP communication between subsystems.
StateMachine.java: Superclass of statemachine classes.
SystemConfiguration.java: System data that defines maximum floor, max requests, max elevators, acknowledgement signal, floor request time, scheduler elevator port, scheduler floor port, elevator port, and floor port.

src/Elevator/SchedulerSubsystem:
Scheduler.java: Scheduler thread class that receives requests from the floor threads then adds the requests to scheduler task queue and sends acknowledgement back to floor thread. Also receives requests for task from elevator threads and sends tasks to elevator threads.
SchedulerState.java: State class for Scheduler.java that holds number of requests completed, arraylist of ElevatorStates, and state of scheduler. Implements adding and getting elevators, checking and setting request avilability, getting and setting number of completed requests, and getting and setting scheduler state.

src/ElevatorTest:
ElevatorStatusTest.java: UnitTest for elevator state machine.
ElevatorUDPTest.java: UnitTest for elevator UDP connection with scheduler.
FileTests.java: UnitTest for file creation.
FloorStatusTest.java: UnitTest for floor state machine.
FloorUDPTest.java: UnitTest for floor UDP connection with scheduler.
HardFaultTest.java: UnitTest for system when hard fault occurs.
PackageHelperTest.java: UnitTest for PackageHelper data serialization.
SchedulerUDPTest.java: UnitTest for scheduler UDP connections with elevator and floor threads.
SystemTest.java: UnitTest for entire system for 4 elevators and 22 floors.
TransientFaultTest.java: UnitTest for system when transient fault occurs.

Documentation: 
README.txt: Describes files in the project, setup instructions, how to run the program, and each group member's work contribution. 

Documentation/Class-Diagrams:
Elevator_Class_Diagram.pdf: The UML class diagram for elevator subsystem.
Floor_Class_Diagram.pdf: The UML class diagram for floor subsystem.
Scheduler_Class_Diagram.pdf: The UML class diagram for scheduler subsystem.
UML_Class_Diagram.pdf: The UML class diagram for entire system.

Documentation/Sequence-Diagrams:
HardFaultSequenceDiagram.pdf: The UML sequence diagram of system when hard fault occurs.
Sequence_Diagram.pdf: The UML sequence diagram of system for entire system.
TransientFaultSequenceDiagram.pdf: The UML sequence diagram of system when transient fault occurs.

Documentation/State-Diagrams:
Elevator-StateMachine-Diagram.pdf: The UML statemachine diagram of elevator subsystem.
Floor-StateMachine-Diagram.pdf: The UML statemachine diagram of floor subsystem.
Scheduler-StateMachine-Diagram.pdf: The UML statemachine diagram of scheduler subsystem.

Documentation/Timing-Diagrams:
HardFaultTimingDiagram.pdf: The UML timing diagram of system when hard fault occurs.
TimingDiagram.pdf: The UML timing diagram of system for 1 regular request.
TransientFaultTimingDiagram.pdf: The UML timing diagram of system when transient fault occurs.

--------------------------------------------------------------------------------------------------
Running the program: To run the program, invoke the main function in the following programs in the order: Scheduler.java -> ElevatorController.java -> Floor.java. Each invocation of a program will have a separate output meaning a total of 3 Console Windows will be needed.
Using Eclipse, import the project -> Open Scheduler.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the Console Window, to ensure the Console Window stays focused on Scheduler.java click on 'Pin Console'.
Create a new Console Window by: Select 'Open Console' -> Select '1 New Console View'.
Open ElevatorController.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the 2nd Console Window and the GUI (make full screen), to ensure the 2nd Console Window stays focused on ElevatorController.java click on 'Pin Console'.
Create a new Console Window by: Select 'Open Console' -> Select '1 New Console View'.
Open Floor.java -> Right Click in Code Window -> Select 'Run as' -> Select 'Java Application'.
Output will be shown on the 3rd Console Window, to ensure the 3rd Console Window stays focused on Floor.java click on 'Pin Console'.

Running Unit Test:
Import JUnit 4 library from 'configure project build path' settings. 
Right click on test file and run as 'JUnit Test'.
--------------------------------------------------------------------------------------------------
Groupwork Responsibilities:
	
Lasitha Amuwala Meesthrige:
	Implemented and designed main GUI
	Updated and fixed State Machines
	Created state machine tests
	Some code clean up
	Updates state machine diagrams

Bonita Hout:
	UML Diagram
	Final Report

Navaty Khara: 
	Bug fixes
	Documentation
	Implemented table GUI 
	Implemented fault detection and handling
	GUI Integration between main and table

Tyler Mak: 
	Created PackageHelper, UDP, & fault tests
	Created fault sequence diagrams
	Created timing diagrams for regular request and fault requests
	Updated README.txt
	
Anant Ojha: 
	Implemented some GUI components 
	Helped with GUI integration 
	Configured System to work with 4 Elevators and 22 Floors

	
	
	
	
	
	
