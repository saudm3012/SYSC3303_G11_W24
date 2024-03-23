
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
    
    // The number of floors in the system
    private int numFloors;

    // The timer thread which will interrupt in the case of faults
    private Timer faultTimer;

    // Flag to indicate floor fault was received
    private boolean floorFault;

    // Flag to indicate door stuck open fault was received 
    private boolean doorOpenFault;

    // Flag to indicate door stuck closed fault was received 
    private boolean doorClosedFault;

    // Flag to indicate elevator to terminate
    private boolean terminate;

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
        this.faultTimer = new Timer(this);
        this.doorOpenFault = false;
        this.doorClosedFault = false;
        this.floorFault = false;
        this.terminate = false;
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
     * returns the current Elevator.
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
        System.out.println(" Direction: " + (direction==Direction.UP ? "UP" : "DOWN"));
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
        System.out.println(name() + "Door Opening");
        // set timer to detect fault
        faultTimer.set(3100);
        if (!doorOpenFault){
            sleep(3000);
            faultTimer.clear();
        } else {
            doorOpenFault = false; // clear fault flag
            try {
                Thread.sleep(6000); // Door fault 
            } catch (InterruptedException e) {
                System.out.println(name()+ "Door Fault Detected! Door stuck open.");
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

        elevatorData.setIsEmpty(isEmpty()&!hasPickUpRequest());
        elevatorData.setDirection(direction);
        elevatorData.setFloor(currFloor);
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
                            break;
                        case DOOR_CLOSED:
                            this.doorClosedFault = true;
                            break;
                        case DOOR_OPEN:
                            this.doorOpenFault = true;
                            break;

                        case NONE:
                            break;
                        default:
                            break;
                    }
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
            state = ElevatorStates.STOP;
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
            state = ElevatorStates.NOTIFY;
        }
        printLatch = true;
    }

    /**
     * Handles the move state of the elevator.
     */
    private void move() {
        // move to next floor
        // set timer to detect fault
        faultTimer.set(2100);
        if (!floorFault){
            sleep(2000); // Traveling between floors is 2 seconds
            faultTimer.clear(); 
        } else {
            try {
                Thread.sleep(Long.MAX_VALUE); // Floor fault
            } catch (InterruptedException e) {
                System.out.println(name()+ "Floor Fault Detected! Terminating...");
                terminate = true;
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
        state = ElevatorStates.NOTIFY;
        printLatch = true;
    }

    /**
     * Handles the stop state of the elevator.
     */
    private void elevatorStop() {
        // set timer to detect fault
        faultTimer.set(100);
        if (!doorClosedFault){
            sleep(10);
            faultTimer.clear();
        } else {
            doorClosedFault = false; // clear fault flag
            try {
                Thread.sleep(1000); // Door stuck closed fault 
            } catch (InterruptedException e) {
                System.out.println(name()+ "Door Fault Detected! Door stuck closed.");
            }
        }
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
