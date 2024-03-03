SYSC 3303 G11 W24 - Elvator System Project - Iteration 2

The goal of this iteration is to add the state machines for the scheduler and elevator subsystems assuming that there is only one elevator.
--

### Team Members

* Jatin Jain 101184197 

* Riya Arora 101190033 

* Zakariya Khan 101186641 

* Ali Nadim 101192767 

* Mohammad Saud 101195172 

### Included directories/files
* SYSC3303_G11_W24' - IntelliJ project directory
* data.txt - input file to be read with specified header
* FloorRequest.java - Data that contains info from each row of input (time, floor, direction, car)
* Elevator - class represents an elevator in your system that communicates with a Scheduler. It handles the movement, loading, and unloading of the elevator, receiving instructions from the scheduler and interacting with the building's floors.
* ElevatorSocket - Used by Elevator for communication with Scheduler
* FloorSocket - Used by FloorSubsystem for communication with Scheduler
* FloorSubsystem - Acts as floor in the system, is loaded with data from input reader, and sends/receives data to/from scheduler
* InputReader - Gets and imports data from textfile.
* Main - Initializes and runs program
* ElevatorSubsytem - component of the Elevator System project and is responsible for managing a group of elevators. It creates and initializes a specified number of elevators, each represented by an Elevator object.
* Scheduler - managing communication between FloorSocket and ElevatorSocket in an elevator system. Handling the flow of data packets between floors and elevators. It transitions between the IDLE and WAIT_ACK states.
* SchedulerSocket - managing communication between the scheduler and elevators or floor subsystems in an elevator system. It handles the sending and receiving of data packets through UDP using DatagramSockets.
* SchedulerState - defines an enumeration SchedulerState with two possible states: IDLE and WAIT_ACK
* ElevatorStates - defines an enumeration ElevatorStates with four possible states: IDLE, MOVING, LOADING, and UNLOADING.
  
Test files
* InputReaderTest - Tests if input reader receives data packet info correctly from data.txt file
* FloorSocketTest - Tests FloorSubsystem send() functionality and make sure info send is correct
* ElevatorSocketTest - Tests Elevators ProcessData() functionality and make sure info send is correct
* FloorRequestTest - Tests conversion between bytes and FloorRequest type
* SchedulerTest - Tests aims to check the basic functionalities and behavior of the Scheduler class, including socket initialization, sleeping, closing sockets.
* ElevatorStateTest - JUnit test class designed to test the state transitions of an Elevator object in an elevator system. The test focuses on the elevator state machine, simulating the process of receiving a request, moving to the specified floor, loading/unloading passengers, and returning to an idle state.
* SchedulerTest - JUnit test case for the Scheduler to test if scheduler transitions from the IDLE state to the WAIT_ACK state correctly when a data packet is added to its receive queue and then the execute method is called

### Set-up Instructions

Instructions below for macOS Users (assuming no pre-installations have been done):

1. Download Zip File L1_G11_iteration_1.zip
2. Open IntelliJ IDEA CE and select "open"
3. Navigate to the folder under L1_G11_iteration_1 called SYSC3303_G11_W24 and click "open"
4. A pop-up screen will appear, select "Trust Project"
5. Let the project load for a few minutes
6. Under DataPacketTest.java, line 1 is import org.junit.jupiter.api.BeforeEach;
7. The junit of this import will be highlighted in red
8. Hover over the red highlighted junit word and select "More actions"
9. Select the second option that says "Add 'JUnit5.8.1' to classpath"
10. A pop-up screen called Download Library for Maven Repository will come up
11. Ensure you see "org.junit.jupiter:junit-jupiter:5.8.1" and click "OK"
12. Click on the gear icon in top right corner called Settings -> Project Structure -> Project Settings -> Project
13. Under Project look for SDK and ensure you have at least java version 17 installed 
14. Select Main.java in the project directory, and click on the green play button in the top right corner 
15. The project should run, and output should appear in the output console below

Project has been developed and tested using IntelliJ
- Download project and run the Main() class, actions that take place to show data transfer are displayed on console

For testing Junit must be installed
- To run tests run test files above

### Breakdown of Responsibilities
* FloorSubsystem/FloorSocket - Zakariya
* Elevator/ElevatorSocket/ElevatorTest - Zakariya/Jatin
* InputReader - Jatin/Mohammad
* FloorRequest Jatin/Mohammad/Zakariya
* Elevator/ElevatorStates/ElevatorStateTest - Zakariya/Jatin/Saud
* Scheduler/SchedulerSocket/SchedulerState/SchedulerTest/ - Zakariya/Jatin/Saud/Riya
* Test files - Ali/Mohammad/Jatin/Riya
* UML Diagrams - Zakariya/Jatin/Ali
* README - Riya/Jatin

