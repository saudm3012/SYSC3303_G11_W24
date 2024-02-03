/**
 * The FloorSubsystem thread simulates the arrival of passengers to the elevators and for simulating all button 
 * presses and lamps at the floor.
 *
 * @author  Zakariya Khan 101186641
 */

import java.util.*;
import java.io.IOException;

public class FloorSubsystem implements Runnable
{
    private Queue<DataPacket> inputQueue; // data to send from input file
    private FloorSocket socket; // communicator helper thread to send and receive data
    private ArrayList<ArrayList<String>> inputData;
    /**
     * The constructor for this class.
     */
    public FloorSubsystem () {
        socket = new FloorSocket(this);
        inputQueue = new LinkedList<>();
        inputData = new ArrayList<>();
    }

    public void addPacketToQueue(DataPacket p){
        inputQueue.add(p);
    }

    public void processData(DataPacket receivedData) {
        // do nothing for now
    }

    /**
     * Gets input data.
     *
     * @return ArrayList<ArrayList<String>>, the parsed input data.
     */
    public ArrayList<ArrayList<String>> getInputData() {
        return this.inputData;
    }

    public void run() {
        socket.start();
        while(!inputQueue.isEmpty()) {
            socket.send(inputQueue.remove());
        }

    }
}