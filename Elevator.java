
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

    // Queue for receiving data packets.
    private Queue<DataPacket> receiveQueue;

    // Flag indicating whether the elevator is ascending or not.
    private boolean ascending;

    // The current floor of the elevator.
    public int currFloor;

    // The floor request data packet.
    private DataPacket floorRequest;

    // Queue for storing the next floors to visit
    private Queue<Integer> nextFloorQueue;

    // Flag to control printing of state.
    private boolean printLatch;

    /**
     * The constructor for this class.
     *
     * @param elevatorNum The elevator number.
     */
    public Elevator(int elevatorNum) {
        this.socket = new ElevatorSocket(2000 + elevatorNum, this);
        this.receiveQueue = new LinkedList<>();
        this.nextFloorQueue = new LinkedList<>();
        this.state = ElevatorStates.IDLE;
        this.currFloor = 1;
        this.printLatch = true;
    }

    /**
     * Processes the received data packet.
     *
     * @param receivedData The data packet received.
     */
    public void processData(DataPacket receivedData) {
        this.receiveQueue.add(receivedData);
        // send data back to scheduler
        // socket.send(receivedData);
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
        System.out.println(" Direction: " + (ascending ? "UP" : "DOWN"));
    }

    /**
     * Prints the current state information.
     */
    private void printState() {
        System.out.println("[ELEVATOR]: " + state);
        printLatch = false;
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
     * Handles the idle state of the elevator.
     */
    private void idle() {
        // read and parse the next instruction from scheduler in the receive queue
        sleep(100);
        if (!receiveQueue.isEmpty()) {
            floorRequest = receiveQueue.remove();
            // Add the passangers floor to the destination queue
            nextFloorQueue.add((Integer) floorRequest.getFloor());
            // Add the passengers destination to the destination queue
            nextFloorQueue.add((Integer) floorRequest.getCarButton());
            // the instruction is to move to a floor change to moving state
            state = ElevatorStates.MOVING;
            printLatch = true;
        } else if (!nextFloorQueue.isEmpty()) {
            // if theres no instruction but theres a destination keep moving
            state = ElevatorStates.MOVING;
            printLatch = true;
        }
        // stay idle otherwise
    }

    /**
     * Handles the move state of the elevator.
     */
    private void move() {
        // move to next floor
        sleep(1000); // TODO Traveling between floors should be 2 seconds
        if (currFloor < nextFloorQueue.peek()) {
            ascending = true;
            currFloor++;
            printCurrentFloor();
        } else if (currFloor > nextFloorQueue.peek()) {
            ascending = false;
            currFloor--;
            printCurrentFloor();
        } else {
            // we have reached the destination
            nextFloorQueue.remove();
            // change the state to load/unload
            state = (nextFloorQueue.isEmpty()) ? ElevatorStates.UNLOADING : ElevatorStates.LOADING;
            printLatch = true;
        }
    }

    /**
     * Handles the load state of the elevator.
     */
    private void load() {
        // opening and closing door
        openCloseDoors();
        state = ElevatorStates.MOVING;
        printLatch = true;
    }

    /**
     * Handles the unload state of the elevator.
     */
    private void unload() {
        // opening and closing door
        openCloseDoors();
        // let scheduler know we have fulfilled a floor request
        socket.send(floorRequest);
        state = ElevatorStates.IDLE;
        printLatch = true;
    }

    /**
     * The main run method of the elevator thread.
     */
    public void run() {
        socket.start();
        while (true) {
            switch (state) {
                case IDLE:
                    if (printLatch)
                        printState();
                    idle();
                    break;
                case MOVING:
                    if (printLatch)
                        printState();
                    move();
                    break;
                case LOADING:
                    if (printLatch)
                        printState();
                    load();
                    break;
                case UNLOADING:
                    if (printLatch)
                        printState();
                    unload();
                    break;
                default:
                    if (printLatch)
                        printState();
                    idle();
            }
        }
    }
}
