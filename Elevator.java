/**
 * TODO add class description
 * @author  Zakariya Khan 101186641
 * @author Jatin Jain 101184197
 */
 
 public class Elevator implements Runnable
 {
     private ElevatorSocket socket; // communicator helper thread to send and receive data
         
     /**
      * The constructor for this class.
      */
     public Elevator () {
        socket = new ElevatorSocket(this);
     }
 
     public void processData(DataPacket receivedData) {
         // send data back to scheduler
         socket.send(receivedData);
     }
  
     public void run() {
         socket.start();
         while (true){
            // do nothing but listen for packets using socket thread
         }
     }
 }
