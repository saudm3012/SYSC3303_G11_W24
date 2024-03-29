SYSC 3303 G11 W24 - Elvator System Project - Iteration 4

The goal of this iteration is to expand the elevator system to handle faults that may occcur

### Team Members

* Jatin Jain 101184197 

* Riya Arora 101190033 

* Zakariya Khan 101186641 

* Ali Nadim 101192767 

* Mohammad Saud 101195172 

### Included directories/files

* SYSC3303_G11_W24' - IntelliJ project directory
* data.txt - input file to be read with specified header
* Floor.FloorRequest.java - Data that contains info from each row of input (time, floor, direction, car)
* Elevator.Elevator - class represents an elevator in your system that communicates with a Scheduler.Scheduler. It handles the movement, loading, and unloading of the elevator, receiving instructions from the scheduler and interacting with the building's floors.
* Elevator.ElevatorSocket - Used by Elevator.Elevator for communication with Scheduler.Scheduler
* Floor.FloorSocket - Used by Floor.FloorSubsystem for communication with Scheduler.Scheduler
* Floor.FloorSubsystem - The Floor.FloorSubsystem thread simulates the arrival of passengers to the elevators and for simulating all button presses and lamps at the floor.
* Floor.InputReader - Gets and imports data from textfile.
* Main - Initializes and runs program
* ElevatorSubsytem - component of the Elevator.Elevator System project and is responsible for managing a group of elevators. It creates and initializes a specified number of elevators, each represented by an Elevator.Elevator object.
* Scheduler.Scheduler - managing communication between Floor.FloorSocket and Elevator.ElevatorSocket in an elevator system. Handling the flow of data packets between floors and elevators.
* Scheduler.Scheduler.SchedulerSocket - managing communication between the scheduler and elevators or floor subsystems in an elevator system. It handles the sending and receiving of data packets through UDP using DatagramSockets.
* ScheulerElevatorSocket.java - manages communication between the elevator and the elevators. Handles sending and receieving of data between elevator and scheduler through UDP and Datagram sockets
* Scheduler.SchedulerState - defines an enumeration with possible states the scheduler can be in
* Elevator.ElevatorStates - defines an enumeration with possible states the elevator can be in
* Elevator.Direction.java - Defines enumeration of the direction that the elevator is currently moving
* Elevator.ElevatorData.java - Contains info on the elevators direction
* Floor.Fault.java - Defines enumeration of the different Faults that could occur
* Elevator.Timer.java - simple timer that can be used to manage timeouts in a state machine
  
Test files
* Elevator.ElevatorStateTest - JUnit test class designed to test the state transitions of an Elevator.Elevator object in an elevator system. The test focuses on the elevator state machine, simulating the process of receiving a request, moving to the specified floor, loading/unloading passengers, and returning to an idle state.
For iteration 4, handling of faults was included
* Tests.InputReaderTest - Tests if input reader receives data packet info correctly from data.txt file
* Scheduler.FloorSocketTest - Tests Floor.FloorSubsystem send() functionality and make sure info send is correct
* ElevatorSocketTest - Tests Elevators ProcessData() functionality and make sure info send is correct
* Tests.FloorRequestTest - Tests conversion between bytes and Floor.FloorRequest type
* Scheduler.SchedulerTest - JUnit test case for the Scheduler.Scheduler to test if scheduler transitions from its states on time

### Set-up Instructions (For Iteration 4)

Instructions below for macOS Users (assuming no pre-installations have been done):

1. Download Zip File L1_G11_iteration_1.zip
2. Open IntelliJ IDEA CE and select "open"
3. Navigate to the folder SYSC3303_G11_W24 and click "open"
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

### Running system - intructions for Iteration 4

Project has been developed and tested using IntelliJ
- Download project and run the main() class, actions that take place to initaite a Floor.Fault during normal operation.
- or run Scheduler.Scheduler main(), then Elevatorsubsystem main() then Floorsubsystem Main()

For testing Junit must be installed
- To run tests run test files above

### Breakdown of Responsibilities for Iteration 4
* Floor (all files) - Zakariya/Mohammad/Jatin
* Floor.InputReader - Jatin/Mohammad/Zakariya
* Elevator.Elevator (all files) - Zakariya
* Scheduler.Scheduler (all files)- Mohammad
* Test files - Jatin/Ali
* UML Diagrams - Zakariya/Jatin
* README - Riya/Jatin/Ali

