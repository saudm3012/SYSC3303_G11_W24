/**
 * The FloorSubsystem thread simulates the arrival of passengers to the elevators and for simulating all button 
 * presses and lamps at the floor.
 *
 * @author  Zakariya Khan 101186641
 */

import java.util.*;

public class FloorSubsystem implements Runnable
{
   private Queue<DataPacket> inputQueue; // data to send from input file
   private FloorSocket socket; // communicator helper thread to send and receive data

    /**
     * The constructor for this class.
     */
    public FloorSubsystem () {
        socket = new FloorSocket(this);
        inputQueue = new LinkedList<>();
        inputQueue.add(new DataPacket("12:13:49:4242","3","up","1")); // TODO REMOVE
    }
    
    public void processData(DataPacket receivedData) {
        // print data
        System.out.println(receivedData.toString());
    }

    public void run() {
        socket.start();
        while(!inputQueue.isEmpty()) {
            socket.send(inputQueue.remove());
        }
    }
}