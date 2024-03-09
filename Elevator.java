
/**
 * The Elevator class represents an elevator system that communicates with a Scheduler.
 *
 * This class handles the movement, loading, and unloading of the elevator, 
 * receiving instructions from the Scheduler and interacting with the building's floors.
 * 
 * @author Zakariya Khan 101186641
 * @author Jatin Jain 101184197
 */

import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread {

    // communicator helper thread to send and receive data
    private ElevatorSocket socket;

    // The current state of the elevator.
    public ElevatorStates state;

    // Queue for receiving data packets from scheduler.
    private Queue<FloorRequest> receiveQueue;

    // Direction of the Elevator
    private Direction direction;

    // The current floor of the elevator.
    public int currFloor;

    // The elevators current data
    private ElevatorData elevatorData;

    private int numFloors;

    /*
     *  An array representing the elevator floor buttons (true is pressed false otherwise)
     *  Each index represents the floor-1
     */ 
    private boolean elevatorButtons[];

    /*
     *  An array representing the floors in which a passenger is waiting to be picked up
     *  Each index represents the floor-1
     */ 
    private boolean pickUpFloor[];


    // Flag to control printing of state.
    private boolean printLatch;

    /**
     * The constructor for this class.
     *
     * @param elevatorNum The elevator number.
     */
    public Elevator(int elevatorNum, int numFloors) {
        this.socket = new ElevatorSocket(2000 + elevatorNum, this);
        this.receiveQueue = new LinkedList<>();
        this.state = ElevatorStates.NOTIFY;
        this.currFloor = 1;
        this.printLatch = true;
        this.direction = Direction.UP;
        this.numFloors = numFloors;
        this.elevatorButtons = new boolean[numFloors];
        this.pickUpFloor = new boolean[numFloors];
        this.elevatorData = new ElevatorData(currFloor, direction, true, elevatorNum);
    }

    /**
     * The constructor for this class when running on seperate machines.
     *
     * @param elevatorNum The elevator number.
     */
    public Elevator(int elevatorNum, int numFloors, String schedulerAddress ) {
        this.socket = new ElevatorSocket(2000 + elevatorNum, this, schedulerAddress);
        this.receiveQueue = new LinkedList<>();
        this.state = ElevatorStates.NOTIFY;
        this.currFloor = 1;
        this.printLatch = true;
        this.direction = Direction.UP;
        this.numFloors = numFloors;
        this.elevatorButtons = new boolean[numFloors];
        this.pickUpFloor = new boolean[numFloors];
        this.elevatorData = new ElevatorData(currFloor, direction, true, elevatorNum);
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
     * Prints the current floor information.
     */
    private void printCurrentFloor() {
        System.out.print("[ELEVATOR]: Reached Floor: ");
        System.out.print(currFloor);
        System.out.println(" Direction: " + (direction==Direction.UP ? "UP" : "DOWN"));
    }

    /**
     * Prints the current state information.
     */
    private void printState() {
        System.out.println("[ELEVATOR]: " + state);
        printLatch = false;
    }

    /**
     * Returns if the elevator is servicing a pick up request or not
     * @return true if if servicing false otherwise
     */
    private boolean hasPickUpRequest() {
        for (boolean request : pickUpFloor){
            if (request){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the elevator is empty or not
     * @return true if elevator is empty false otherwise
     */
    private boolean isEmpty() {
        for (boolean buttonPressed : elevatorButtons){
            if (buttonPressed){
                return false;
            }
        }
        return true;
    }

    /**
     * Prints door opening and closing while mimicing their timings.
     */
    private void openCloseDoors() {
        System.out.println("[ELEVATOR]: Door Opening");
        sleep(1000); // TODO should be 6 seconds
        System.out.println("[ELEVATOR]: Door Closing");
        sleep(1000); // TODO should be 6 seconds
    }

    /**
     * Handles the NOTIFY state of the elevator.
     */
    private void notifyScheduler() {
        FloorRequest floorRequest;

        elevatorData.setIsEmpty(isEmpty()&!hasPickUpRequest());
        elevatorData.setDirection(direction);
        elevatorData.setFloor(currFloor);
        socket.send(elevatorData);
        while (true){
            sleep(1);
            if (!receiveQueue.isEmpty()) {
                floorRequest = receiveQueue.remove();
                if (!floorRequest.isEnd()){
                    // Add the passangers destination floor to the floor buttons counter
                    elevatorButtons[floorRequest.getCarButton()-1] = true;
                    // update the pickupFloor array
                    pickUpFloor[floorRequest.getFloor()-1] = true;
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
        if (elevatorButtons[currFloor-1] || pickUpFloor[currFloor-1]){
            state = ElevatorStates.IDLE;
        } else if (!isEmpty()) {
            // have passengers to service
            state = ElevatorStates.MOVING;
        } else if (hasPickUpRequest()) {
            // we dont have a passenger but need to pick them up
            // see if we need to change direction to get them
            int destFloor = 0;
            for (int idx = 0; idx < numFloors; idx++){
                if (pickUpFloor[idx]){
                    destFloor = idx + 1;
                    break;
                }
            }
            direction = (destFloor > currFloor) ? Direction.UP: Direction.DOWN;
            state = ElevatorStates.MOVING;
        }
        else {
            sleep(10);
            state = ElevatorStates.NOTIFY;
        }
        printLatch = true;
    }

    /**
     * Handles the move state of the elevator.
     */
    private void move() {
        // move to next floor
        sleep(2000); // Traveling between floors is 2 seconds
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
        state = ElevatorStates.NOTIFY;
        printLatch = true;
    }

    /**
     * Handles the load state of the elevator.
     */
    private void idle() {
        // opening and closing door
        openCloseDoors();
        elevatorButtons[currFloor-1] = false; // update floor button
        pickUpFloor[currFloor-1] = false; // update pickup requests
        state = ElevatorStates.PROCESSING;
        printLatch = true;
    }

    /**
     * The main run method of the elevator thread.
     */
    public void run() {
        socket.start();
        while (true) {
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
                case IDLE:
                    printState();
                    idle();
                    break;
                default:
                    if (printLatch)
                        printState();
                    notifyScheduler();
            }
        }
    }
}
