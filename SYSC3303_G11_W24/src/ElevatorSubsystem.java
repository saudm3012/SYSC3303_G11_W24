import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ElevatorSubsystem extends Thread {

    // List of elevators
    private Elevator elevatorList[];

    /**
     * Create a new elevator subsystem with numElevators and numFloors
     * 
     * @param numElevators the number of elevators in the system
     */
    public ElevatorSubsystem(int numFloors, int numElevators) {
        elevatorList = new Elevator[numElevators];

        // Initialize the elevators
        for (int i = 0; i < numElevators; i++) {
            elevatorList[i] = ((new Elevator(i, numFloors)));
        }

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
            elevator.start();
        }
    }

    public static void main(String args[]) throws IOException {
        Thread elevatorSubystem = new ElevatorSubsystem(5, 3);
        elevatorSubystem.start();
    }
}