SYSC 3303 G11 W24 - Elvator System Project - Iteration 1
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
* DataPacket.java - Data that contains info from each row of input (time, floor, direction, car)
* Elevator - Acts as elevator in the system, sends/receives data to/from scheduler
* ElevatorSocket - Used by Elevator for communication with Scheduler
* FloorSocket - Used by FloorSubsystem for communication with Scheduler
* FloorSubsystem - Acts as floor in the system, is loaded with data from input reader, and sends/receives data to/from scheduler
* InputReader - Gets and imports data from textfile.
* Main - Initializes and runs program
* Scheduler - The scheduler communicated event data by transferring DataPackets between FloorSubsystem and Elevator

Test files
* InputReaderTest - Tests if input reader receives data packet info correctly from data.txt file
* FloorSocketTest - Tests FloorSubsystem send() functionality and make sure info send is correct
* ElevatorSocketTest - Tests Elevators ProcessData() functionality and make sure info send is correct
* DataPacketTest - Tests conversion between bytes and DataPacket type
* 

### Set-up Instructions
Project has been developed and tested using IntelliJ
- Download project and run the Main() class, actions that take place to show data transfer are displayed on console

### Breakdown of Responsibilities
* FloorSubsystem/FloorSocket - Zakariya
* Scheduler - Zakariya/Mohammad/Riya
* Elevator/ElevatorSocket - Zakariya/Jatin
* InputReader - Jatin/Mohammad
* DataPacket Jatin/Mohammad/Zakariya

* Test files - Ali/Mohammad

* UML Diagrams - Zakariya/Jatin
* README - Riya/Jatin

