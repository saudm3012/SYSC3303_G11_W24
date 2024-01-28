import java.io.Serializable;

/**
 * Represents data for communication between elevator components.
 * The class defines constants for request modes and provides
 * constructors and methods to manage elevator state and requests.
 */
public class SchedulerData implements Serializable {
    // CONSTANTS FOR REQUEST MODES
    public static final int CONTINUE_REQUEST = 0;
    public static final int FLOOR_REQUEST = 1;
    public static final int MOVE_REQUEST = 2;
    public static final int STOP_REQUEST = 3;
    public static final int DOOR_REQUEST = 4;

    // Number of floors
    public static final int NUM_FLOORS = 7;
    // Number of elevators
    public static final int NUM_ELEVATORS = 3; // Change this value based on your requirement

    // Instance variables
    private final int requestMode;
    private final int elevatorNumber;
    private boolean[] floorLamps;
    private int currentFloor;
    private int destinationFloor;
    private boolean movingUp, movingDown, doorOpen;

    /**
     * Constructor for FLOOR requests.
     *
     * @param elevatorNumber     Elevator number
     * @param requestMode        Request mode
     * @param floorLamps         FloorSubsystem lamp states
     * @param currentFloor       Current floor
     * @param destinationFloor   Destination floor
     */
    public SchedulerData(int elevatorNumber, int requestMode, boolean[] floorLamps, int currentFloor, int destinationFloor) {
        this.elevatorNumber = elevatorNumber;
        this.requestMode = requestMode;
        this.floorLamps = floorLamps;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public void updateElevatorState(int currentPosition, boolean[] currentFloorLamps, boolean isMovingUp, boolean isMovingDown, boolean isDoorOpen) {
        this.currentFloor = currentPosition;
        this.floorLamps = currentFloorLamps;
        this.movingUp = isMovingUp;
        this.movingDown = isMovingDown;
        this.doorOpen = isDoorOpen;
    }

    /**
     * Constructor for MOVE/STOP requests.
     *
     * @param elevatorNumber Elevator number
     * @param requestMode    Request mode
     * @param movingUp       Flag indicating if the elevator is moving up
     * @param movingDown     Flag indicating if the elevator is moving down
     * @param doorOpen       Flag indicating if the door is open
     */
    public SchedulerData(int elevatorNumber, int requestMode, boolean movingUp, boolean movingDown, boolean doorOpen) {
        this.elevatorNumber = elevatorNumber;
        this.requestMode = requestMode;
        this.movingUp = movingUp;
        this.movingDown = movingDown;
        this.doorOpen = doorOpen;
    }

    /**
     * Constructor for CONTINUE/DOOR requests.
     *
     * @param elevatorNumber Elevator number
     * @param requestMode    Request mode
     */
    public SchedulerData(int elevatorNumber, int requestMode) {
        this.elevatorNumber = elevatorNumber;
        this.requestMode = requestMode;
    }

    // Getter methods

    /**
     * Gets the elevator number.
     *
     * @return The elevator number
     */
    public int getElevatorNumber() {
        return elevatorNumber;
    }

    /**
     * Gets the array of requested floors.
     *
     * @return The array of requested floors
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Gets the destination floor.
     *
     * @return The destination floor
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     * Gets the array of FloorSubsystem lamp states.
     *
     * @return The array of FloorSubsystem lamp states
     */
    public boolean[] getFloorLamps() {
        return floorLamps;
    }

    /**
     * Checks if the elevator is moving up.
     *
     * @return True if the elevator is moving up, false otherwise
     */
    public boolean isMovingUp() {
        return movingUp;
    }

    /**
     * Checks if the elevator is moving down.
     *
     * @return True if the elevator is moving down, false otherwise
     */
    public boolean isMovingDown() {
        return movingDown;
    }

    /**
     * Checks if the elevator is idle.
     *
     * @return True if the elevator is idle, false otherwise
     */
    public boolean isIdle() {
        return !movingUp && !movingDown;
    }

    /**
     * Checks if the door is open.
     *
     * @return True if the door is open, false if closed
     */
    public boolean isDoorOpen() {
        return doorOpen;
    }

    /**
     * Gets the mode of the request.
     *
     * @return The mode of the request (FLOOR, MOVE, CONTINUE, STOP)
     */
    public int getRequestMode() {
        return requestMode;
    }
}
