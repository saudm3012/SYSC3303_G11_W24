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
    }
    
    public void processData(DataPacket receivedData) {
        // print data
    }

    public void run() {
        while(inputQueue.size() != 0) {
            socket.send(inputQueue.remove());
        }
    }
}