package Elevator;

import Gui.ElevatorGUI;

import java.io.IOException;

// @author editted by Riya Arora for GUI integration (101190033)

public class ElevatorSubsystem extends Thread {

    // List of elevators
    private Elevator elevatorList[];
    private static ElevatorGUI gui;

    private int totalRequestsCompleted;

    private int time;

    private float throughput;
    /**
     * Create a new elevator subsystem with numElevators and numFloors
     *
     * @param numElevators the number of elevators in the system
     */
    public ElevatorSubsystem(int numFloors, int numElevators, ElevatorGUI ui) {
        elevatorList = new Elevator[numElevators];
        this.gui = ui;
        time = 0;
        // Initialize the elevators
        for (int i = 0; i < numElevators; i++) {
            elevatorList[i] = ((new Elevator(i, numFloors, ui)));
        }

    }

    /**
     * Calculate throughput
     */
    public void calculateThroughput() {
        time += 1;
        totalRequestsCompleted = 0; // Reset totalRequestsCompleted each time to recalculate
        for (Elevator e : elevatorList) {
            totalRequestsCompleted += e.completedRequestsCount;
        }
        throughput = (float) totalRequestsCompleted / time; // Cast as float to keep decimal
        gui.updateThroughput(throughput);
    }


    /**
     * Close the sockets of all elevators
     */
    public void closeSockets() {
        // We're finished, so close the sockets.
        for (Elevator e : elevatorList) {
            e.closeSockets();
        }
    }

    /**
     * Print a status message in the console
     *
     * @param message the message to be printed
     */
    public void print(String message) {
        System.out.println("ELEVATOR SUBSYSTEM: " + message);
    }

    public void run() {
        for (Elevator elevator : elevatorList) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
            elevator.start();
        }

        // Start the throughput calculation 
            while (true) {
                calculateThroughput();
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
    }

    public static void main(String args[]) throws IOException {
        ElevatorGUI gui = new ElevatorGUI();
        Thread elevatorSubystem = new ElevatorSubsystem(22, 4, gui);
        elevatorSubystem.start();
    }
}
