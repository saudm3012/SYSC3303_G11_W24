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
* SchedulerTest - Tests aims to check the basic functionalities and behavior of the Scheduler class, including socket initialization, sleeping, closing sockets.

### Set-up Instructions
Project has been developed and tested using IntelliJ
- Download project and run the Main() class, actions that take place to show data transfer are displayed on console

### JUnit Download for Testing

# Project Title

Brief description of your project.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Adding JUnit Dependency](#adding-junit-dependency)
- [Creating a Simple Test](#creating-a-simple-test)
- [Running Tests in IntelliJ](#running-tests-in-intellij)

## Prerequisites

- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) installed on your machine.

## Adding JUnit Dependency

1. Open your IntelliJ IDEA project.

2. If you're using Maven:

   - Open the `pom.xml` file in your project.

   - Inside the `<dependencies>` section, add the following dependency:

     ```xml
     <dependencies>
         <!-- Other dependencies -->
         <dependency>
             <groupId>junit</groupId>
             <artifactId>junit</artifactId>
             <version>5.8.1</version> <!-- Use the latest version available -->
             <scope>test</scope>
         </dependency>
     </dependencies>
     ```

   - Save the file.

3. If you're using Gradle:

   - Open the `build.gradle` file in your project.

   - Inside the `dependencies` block, add the following dependencies:

     ```gradle
     dependencies {
         // Other dependencies
         testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1' // Use the latest version available
         testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'
     }
     ```

   - Save the file.

4. IntelliJ IDEA should automatically detect the changes and prompt you to sync the project. Click on the prompt to sync the dependencies.

## Creating a Simple Test

1. Create a test class in your test directory, for example, `src/test/java`. Here's a simple example:

   ```java
   import org.junit.jupiter.api.Test;
   import static org.junit.jupiter.api.Assertions.*;

   public class MyTestClass {

       @Test
       void myTestMethod() {
           assertEquals(2, 1 + 1);
       }
   }
## Running Tests in IntelliJ

1. Open the test class file in IntelliJ.

2. Locate the test method (e.g., myTestMethod).

3. Right-click on the method name or class name.

4. Select "Run 'MyTestMethod'" or "Run 'MyTestClass'" from the context menu.

5. Alternatively, you can run all tests in a package or module.

6. View the test results in the "Run" tab at the bottom of the IntelliJ window.

7. Green checkmarks indicate successful tests, and red icons indicate failures.

You've added JUnit testing to your project and executed a simple test using IntelliJ IDEA


### Breakdown of Responsibilities
* FloorSubsystem/FloorSocket - Zakariya
* Scheduler - Zakariya/Mohammad/Riya
* Elevator/ElevatorSocket - Zakariya/Jatin
* InputReader - Jatin/Mohammad
* DataPacket Jatin/Mohammad/Zakariya

* Test files - Ali/Mohammad

* UML Diagrams - Zakariya/Jatin
* README - Riya/Jatin

