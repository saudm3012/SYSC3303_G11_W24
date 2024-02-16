/**
 * The Elevator class that sends/receives packets to/from Scheduler
 * @author Zakariya Khan 101186641
 * @author Jatin Jain 101184197
 */
 
 public class Elevator implements Runnable
 {
     private ElevatorSocket socket; // communicator helper thread to send and receive data
     private ElevatorStates state;
     /**
      * The constructor for this class.
      */
     public Elevator () {
        socket = new ElevatorSocket(this);
        this.state = ElevatorStates.IDLE;
     }
 
     public void processData(DataPacket receivedData) {
         // send data back to scheduler
         socket.send(receivedData);
     }
     
     private void idle(){
        // read and parse the next instruction from scheduler in the receive queue
        // if theres no instruction stay idle 
        state = ElevatorStates.IDLE;
        // update the state: if the instruction is to move to a floor change to moving state
        state = ElevatorStates.MOVING;
        // else stop and unload/load
        state = ElevatorStates.LOADING_UNLOADING;
     }

     private void move(){
        // move to next floor in queue
        // if we reach a DESTINATION floor change the state to load/unload
        state = ElevatorStates.LOADING_UNLOADING;
        // otherwise we reached a floor between our destination and need to ask scheduler if we should stop or continue moving
        state = ElevatorStates.MOVING;
     }
     
     private void loadAndUnload(){
        //sleep
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
                    break;
            }
         }
     }
 }
