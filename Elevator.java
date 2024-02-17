import java.util.LinkedList;
import java.util.Queue;

import javax.xml.crypto.Data;

/**
 * The Elevator class that sends/receives packets to/from Scheduler
 * @author Zakariya Khan 101186641
 * @author Jatin Jain 101184197
 */
 
 public class Elevator extends Thread
 {
     private ElevatorSocket socket; // communicator helper thread to send and receive data
     private ElevatorStates state;
     private Queue<DataPacket> receiveQueue;
     private boolean ascending;
     private int currFloor;
     private DataPacket floorRequest;
     private Queue<Integer> nextFloorQueue;
     /**
      * The constructor for this class.
      */
     public Elevator (int elevatorNum) {
        this.socket = new ElevatorSocket(2000+elevatorNum,this);
        this.receiveQueue = new LinkedList<>();
        this.nextFloorQueue = new LinkedList<>();
        this.state = ElevatorStates.IDLE;
        this.currFloor = 1;
     }
     
     public void processData(DataPacket receivedData) {
        this.receiveQueue.add(receivedData);
        // send data back to scheduler
         //socket.send(receivedData);
     }
     
     /**
      * Close the socket of elevators
      */
      public void closeSockets() {
        // We're finished, so close the sockets.
        this.socket.closeSockets();
    }

    /**
	 * Wait for the specified amount of time
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
    private void printCurrentFloor(){
        System.out.print("[ELEVATOR]: MOVING Reached Floor: ");
        System.out.print(currFloor);
        System.out.println(" Direction: "+ (ascending ? "UP" : "DOWN"));
    }
     private void idle(){
        //DataPacket floorRequest;
        // read and parse the next instruction from scheduler in the receive queue
        sleep(100);
        if (!receiveQueue.isEmpty()){
            System.out.println("[ELEVATOR]: IDLE");
            floorRequest = receiveQueue.remove();
            // Add the passangers floor to the destination queue
            nextFloorQueue.add((Integer)floorRequest.getFloor());
            // Add the passengers destination to the destination queue
            nextFloorQueue.add((Integer)floorRequest.getCarButton());
            // the instruction is to move to a floor change to moving state
            state = ElevatorStates.MOVING;
        }
        else if (!nextFloorQueue.isEmpty()) {
            // if theres no instruction but theres a destination keep moving 
            state = ElevatorStates.MOVING;
        }
        // stay idle otherwise
     }
    
     private void move(){
        // move to next floor 
        sleep(1000); // TODO Traveling between floors should be 2 seconds
        if (currFloor<nextFloorQueue.peek()){
            ascending = true;
            currFloor++;
            printCurrentFloor();
        } else if (currFloor>nextFloorQueue.peek()){
            ascending = false;
            currFloor--;
            printCurrentFloor();
        } else{
            // we have reached the destination
            nextFloorQueue.remove();
            // change the state to load/unload
            state = ElevatorStates.LOADING_UNLOADING;
        }
     }
     
     private void loadAndUnload(){
        // opening and closing door
        System.out.println("[ELEVATOR]: Door Opening");
        sleep(1000); // TODO should be 6 seconds
        System.out.println("[ELEVATOR]: Door Closing");
        sleep(1000); // TODO should be 6 seconds
        if (nextFloorQueue.isEmpty()){
            // let scheduler know we have fulfilled a floor request
            socket.send(floorRequest);
        }
        state = ElevatorStates.IDLE;
     }

     public void run() {
         socket.start();
         while (true){
            switch (state) {
                case IDLE:
                    idle();
                    break;
                case MOVING:
                    move();
                    break;
                case LOADING_UNLOADING:
                    loadAndUnload();
                    break;
                default:
                    idle();
            }
         }
     }
 }
