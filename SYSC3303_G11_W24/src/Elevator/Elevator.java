package Elevator;
/**
 * The Elevator.Elevator class represents an elevator system that communicates with a Scheduler.Scheduler.
 *
 * This class handles the movement, loading, and unloading of the elevator,
 * receiving instructions from the Scheduler.Scheduler and interacting with the building's floors.
 *
 * @author Zakariya Khan 101186641
 * @author Jatin Jain 101184197
 * @author Riya Arora 101190033
 */

import Floor.FloorRequest;
import Gui.ElevatorGUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class Elevator extends Thread {

    // communicator helper thread to send and receive data
    private ElevatorSocket socket;

    // The current state of the elevator.
    public ElevatorStates state;

    // Queue for receiving data packets from scheduler.
    private Queue<FloorRequest> receiveQueue;

    // Elevator.Direction of the Elevator.Elevator
    private Direction direction;

    // The current floor of the elevator.
    public int currFloor;

    // The elevators current data
    private ElevatorData elevatorData;

    // The number of floors in the system
    private int numFloors;

    // The timer thread which will interrupt in the case of faults
    private Timer faultTimer;

    // Flag to indicate floor fault was received
    private boolean floorFault;

    // Flag to indicate door stuck open fault was received
    private boolean doorFault;

    // Flag to indicate elevator to terminate
    private boolean terminate;

    //UI instance
    private ElevatorGUI gui;

    /*
     *  An array representing the elevator floor buttons (true is pressed false otherwise)
     *  Each index represents the floor-1
     */
    private int elevatorButtons[];

    /*
     *  An array representing the floors in which a passenger is waiting to be picked up
     *  Each index represents the floor-1
     */
    private ArrayList<ArrayList<FloorRequest>> pickUpRequests;

    // counter to keep track of the number of completed floor requests
    public int completedRequestsCount;

    // Flag to control printing of state.
    private boolean printLatch;

    /**
     * The constructor for this class.
     *
     * @param elevatorNum The elevator number.
     */
    public Elevator(int elevatorNum, int numFloors, ElevatorGUI ui) {
        this.socket = new ElevatorSocket(2000 + elevatorNum, this);
        this.receiveQueue = new LinkedList<>();
        this.state = ElevatorStates.NOTIFY;
        this.currFloor = (elevatorNum % 2 == 0) ? 1 : numFloors;
        this.printLatch = true;
        this.direction = (elevatorNum % 2 == 0) ? Direction.UP : Direction.DOWN;
        this.numFloors = numFloors;
        this.elevatorButtons = new int[numFloors];
        this.pickUpRequests = new ArrayList<ArrayList<FloorRequest>>(numFloors);
        for (int floor = 0; floor < numFloors; floor++){
            pickUpRequests.add(floor, new ArrayList<FloorRequest>());
        }
        this.elevatorData = new ElevatorData(currFloor, direction, true, elevatorNum, 0);
        this.faultTimer = new Timer(this);
        this.doorFault = false;
        this.floorFault = false;
        this.terminate = false;
        this.completedRequestsCount = 0;
        this.gui = ui;
        // update GUI with initital position
        // Update GUI status 
        if (gui != null) {
            gui.updateStatus(this.elevatorData.getElevatorNum(), this.currFloor, this.state.toString(), getNumPassengers(),"NONE");
        }
    }

    /**
     * Processes the received data packet.
     *
     * @param receivedData The data packet received.
     */
    public void processData(FloorRequest receivedData) {
        this.receiveQueue.add(receivedData);
    }

    /**
     * Close the socket of elevators
     */
    public void closeSockets() {
        // We're finished, so close the sockets.
        this.socket.closeSockets();
    }

    /**
     * Waits for the specified amount of time.
     *
     * @param ms The time to sleep in milliseconds.
     */
    private void sleep(int ms) {
        // Slow things down (wait 5 seconds)
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * returns the current Elevator.Elevator.
     */
    private String name() {
        return "[ELEVATOR-"+ elevatorData.getElevatorNum()+"]: ";
    }

    /**
     * Prints the current floor information.
     */
    private void printCurrentFloor() {
        System.out.print(name() + "Reached Floor: ");
        System.out.print(currFloor);
        System.out.print(" | Elevator.Direction: " + (direction== Direction.UP ? "UP" : "DOWN"));
        System.out.println(" | Elevator.Elevator Buttons: " + elevatorButtonsToString());
    }

    /**
     * returns a string of the current elevator buttons
     */
    private String elevatorButtonsToString() {
        String requestString = "[";
        for (int i = 0; i<elevatorButtons.length; i++ ){
            if (i<elevatorButtons.length-1) requestString += (elevatorButtons[i] == 0) ? ("F"+(i+1)+": "+"0, ") : ("F"+(i+1)+": "+"1, ");
            else requestString += (elevatorButtons[i] == 0) ? ("F"+(i+1)+": "+"0]") : ("F"+(i+1)+": "+"1]");
        }
        return requestString;
    }

    /**
     * Prints the current state information.
     */
    private void printState() {
        System.out.println(name() + state);
        printLatch = false;
    }

    /**
     * Returns if the elevator is servicing a pick up request or not
     * @return true if if servicing false otherwise
     */
    private boolean hasPickUpRequest() {
        for (int floor = 0; floor < numFloors; floor++){
            if (!pickUpRequests.get(floor).isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the elevator is empty or not by checking the elevator buttons
     * @return true if elevator is empty false otherwise
     */
    private boolean isEmpty() {
        for (int buttonPressed : elevatorButtons){
            if (buttonPressed!=0){
                return false;
            }
        }
        return true;
    }

    /**
     * returns a number of passengers in the elevator
     */
    private int getNumPassengers() {
        int total = 0;
        for (int count : elevatorButtons){
            total += count;
        }
        return total;
    }

    /**
     * Prints door opening and closing while mimicing their timings.
     */
    private void openCloseDoors() {
        System.out.println(name() + "Door Opening");
        // set timer to detect fault
        faultTimer.set(3100);
        if (!doorFault){
            sleep(3000);
            faultTimer.interrupt();
        } else {
            doorFault = false; // clear fault flag
            try {
                Thread.sleep(Long.MAX_VALUE); // Door fault
            } catch (InterruptedException e) {
                gui.updateStatus(elevatorData.getElevatorNum(), currFloor, state.toString(), getNumPassengers(),"DOOR");
                System.out.println(name()+ "Door Floor.Fault Detected! Door stuck open.");
                sleep(2900); // 6 seconds total to handle door fault
            }
        }
        System.out.println(name() + "Door Closing");
        sleep(3000);
    }

    /**
     * Handles the NOTIFY state of the elevator.
     */
    private void notifyScheduler() {
        FloorRequest floorRequest;
        if (gui != null) {
            gui.updateStatus(this.elevatorData.getElevatorNum(), this.currFloor, this.state.toString(), getNumPassengers(),"NONE");
        }
        elevatorData.setIsEmpty(isEmpty()&!hasPickUpRequest());
        elevatorData.setDirection(direction);
        elevatorData.setFloor(currFloor);
        elevatorData.setNumPassengers(getNumPassengers());
        socket.send(elevatorData);
        while (true){
            sleep(1);
            if (!receiveQueue.isEmpty()) {
                floorRequest = receiveQueue.remove();
                if (!floorRequest.isEnd()){
                    // enable fault flags if any
                    switch (floorRequest.getFault()) {
                        case FLOOR:
                            this.floorFault = true;
                            this.state = ElevatorStates.STOP;
                            break;
                        case DOOR:
                            this.doorFault = true;
                            break;
                        case NONE:
                            break;
                        default:
                            break;
                    }
                    // (passenger is being picked up right away at this floor) Add the passengers destination floor to the elevator buttons
                    if (floorRequest.getFloor() == currFloor) {
                        elevatorButtons[floorRequest.getCarButton()-1]++;
                    } else{
                        // (passenger needs to be picked up) update the pickUpRequests array
                        pickUpRequests.get(floorRequest.getFloor()-1).add(new FloorRequest(floorRequest));
                    }
                } else {
                    break;
                }
            }
        }
        printLatch = true;
        state = ElevatorStates.PROCESSING;
    }

    /**
     * Handles the PROCESSING state of the elevator.
     */
    private void processRequests() {
        if (gui != null) {
            gui.updateStatus(this.elevatorData.getElevatorNum(), this.currFloor, this.state.toString(), getNumPassengers(),"NONE");
        }
        if ((elevatorButtons[currFloor-1] != 0 ) || !pickUpRequests.get(currFloor-1).isEmpty()){
            // stop to pick/drop off passenger(s)
            state = ElevatorStates.STOP;
        } else if (!isEmpty()) {
            // have passengers to service
            // check if we need to change direction
            boolean changeDirection = true;
            if (direction == Direction.UP){
                // if we are currently going up check if there is a person to drop off/pickup at a floor above
                for (int nextFloor = currFloor; nextFloor < numFloors; nextFloor++){
                    if (elevatorButtons[nextFloor] >= 1 || !pickUpRequests.get(nextFloor).isEmpty()){
                        changeDirection = false;
                        break;
                    }
                }
            } else {
                // if we are currently going down check if there is a person to drop off/pickup at a floor below
                for (int nextFloor = 0; nextFloor < currFloor; nextFloor++){
                    if (elevatorButtons[nextFloor] >= 1 || !pickUpRequests.get(nextFloor).isEmpty()){
                        changeDirection = false;
                        break;
                    }
                }
            }
            if (changeDirection){
                direction = (direction == Direction.DOWN) ? Direction.UP : Direction.DOWN;
            }
            state = ElevatorStates.MOVING;
        } else if (hasPickUpRequest()) {
            // we dont have a passenger but need to pick them up
            // see if we need to change direction to get them
            int destFloor = 0;
            for (int idx = 0; idx < numFloors; idx++){
                if (!pickUpRequests.get(idx).isEmpty()){
                    destFloor = idx + 1;
                    break;
                }
            }
            direction = (destFloor > currFloor) ? Direction.UP: Direction.DOWN;
            state = ElevatorStates.MOVING;
        }
        else {
            // no requests to handle and empty elevator
            state = ElevatorStates.NOTIFY;
            System.out.println(name() + "Completed Requests: " + completedRequestsCount);
        }
        printLatch = true;
    }

    /**
     * Handles the move state of the elevator.
     */
    private void move() {
        // move to next floor
        // set timer to detect fault
        if (gui != null) {
            gui.updateStatus(elevatorData.getElevatorNum(), currFloor, state.toString(), getNumPassengers(),"NONE");
        }
        faultTimer.set(1100);
        if (!floorFault){
            sleep(1000); // Traveling between floors is 1 second
            faultTimer.interrupt();
        } else {
            try {
                Thread.sleep(Long.MAX_VALUE); // Floor fault
            } catch (InterruptedException e) {
                System.out.println(name()+ "Floor Floor.Fault Detected! Terminating...");
                terminate = true;
                if (gui != null) {
                    gui.updateStatus(elevatorData.getElevatorNum(), currFloor, state.toString(), getNumPassengers(),"FLOOR");
                }
                return;
            }
        }
        if (direction == Direction.UP) {
            currFloor++;
            printCurrentFloor();
        } else {
            currFloor--;
            printCurrentFloor();
        }
        if (currFloor == numFloors) {
            // reached the top floor, change direction
            direction = Direction.DOWN;
        } else if (currFloor == 1) {
            // reached the bottom floor, change direction
            direction = Direction.UP;
        }

        if (gui != null) {
            gui.updateStatus(elevatorData.getElevatorNum(), currFloor, state.toString(), getNumPassengers(),"NONE");
        }

        state = ElevatorStates.NOTIFY;
        printLatch = true;
    }

    /**
     * Handles the stop state of the elevator.
     */
    private void elevatorStop() {
        if (gui != null) {
            gui.updateStatus(elevatorData.getElevatorNum(), currFloor, state.toString(), getNumPassengers(),"NONE");
        }
        // opening and closing door
        openCloseDoors();
        // update pickup requests and elevator buttons
        for (int reqIdx = pickUpRequests.get(currFloor-1).size()-1; reqIdx >= 0; reqIdx--) {
            elevatorButtons[pickUpRequests.get(currFloor-1).remove(reqIdx).getCarButton()-1]++;
        }
        completedRequestsCount += elevatorButtons[currFloor-1];
        elevatorButtons[currFloor-1] = 0; // update floor button
        state = ElevatorStates.PROCESSING;

        if (gui != null) {
            gui.updateStatus(elevatorData.getElevatorNum(), currFloor, state.toString(), getNumPassengers(),"NONE");
        }
    }

    /**
     * The main run method of the elevator thread.
     */
    public void run() {
        socket.start();
        faultTimer.start();
        while (!terminate) {
            switch (state) {
                case NOTIFY:
                    if (printLatch)
                        printState();
                    notifyScheduler();
                    break;
                case PROCESSING:
                    if (printLatch)
                        printState();
                    processRequests();
                    break;
                case MOVING:
                    if (printLatch)
                        printState();
                    move();
                    break;
                case STOP:
                    printState();
                    elevatorStop();
                    break;
                default:
                    if (printLatch)
                        printState();
                    notifyScheduler();
            }
        }
    }
}
