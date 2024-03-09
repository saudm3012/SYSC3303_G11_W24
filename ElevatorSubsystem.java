import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class ElevatorSubsystem extends Thread{
     
     //List of elevators
     private Elevator elevatorList[];
     
     
     /**
      * Create a new elevator subsystem with numElevators and numFloors
      * @param numElevators the number of elevators in the system
      */
     public ElevatorSubsystem(int numFloors, int numElevators) {
         elevatorList = new Elevator[numElevators];
         
         //Initialize the elevators
         for (int i = 0; i < numElevators; i ++) {
             elevatorList[i] = ((new Elevator(i,numFloors)));
         }

     }

     /**
      * Create a new elevator subsystem with numElevators and numFloors
      * @param numFloors the number of floors in the system
      * @param numElevators the number of elevators in the system
      * @param schedulerAddress the address of the scheduler
      */
      public ElevatorSubsystem(int numFloors, int numElevators, String schedulerAddress) {
        elevatorList = new Elevator[numElevators];
        
        //Initialize the elevators
        for (int i = 0; i < numElevators; i ++) {
            elevatorList[i] = ((new Elevator(i,numFloors,schedulerAddress)));
        }

    }
          
     /**
      * Close the sockets of all elevators
      */
     public void closeSockets() {
         // We're finished, so close the sockets.
         for (Elevator e: elevatorList) {
             e.closeSockets();
         }
     }
     
     /**
      * Print a status message in the console
      * @param message the message to be printed
      */
     public void print(String message) {
         System.out.println("ELEVATOR SUBSYSTEM: " + message);
     }
     public void run() {
        for (Elevator e: elevatorList) {
			e.start();
		}
    }
    public static void main (String args[]) throws IOException {
        String schedulerAddress = "";
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        InetAddress elevatorAddress;
        try {
            elevatorAddress = InetAddress.getLocalHost();
            System.out.println("ElevatorSubsystem's Address is " + elevatorAddress.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Enter the Schedulers IP address: ");
        schedulerAddress = inputReader.readLine();
        inputReader.close();
        Thread elevatorSubystem = new ElevatorSubsystem(5,1, schedulerAddress);
        elevatorSubystem.start();
    }
 }