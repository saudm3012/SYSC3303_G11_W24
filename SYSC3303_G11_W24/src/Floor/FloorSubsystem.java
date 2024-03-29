package Floor; /**
 * The Floor.FloorSubsystem thread simulates the arrival of passengers to the elevators and for simulating all button
 * presses and lamps at the floor.
 *
 * @author  Zakariya Khan 101186641
 * @author Mohammad Saud 101195172
 */

import java.io.IOException;
import java.util.*;

public class FloorSubsystem implements Runnable
{
    private Queue<FloorRequest> inputQueue; // data to send from input file
    public FloorSocket socket; // communicator helper thread to send and receive data
    private ArrayList<ArrayList<String>> inputData;

    public int numRequests;


    /**
     * The constructor for this class.
     */
    public FloorSubsystem () {
        socket = new FloorSocket(this);
        inputQueue = new LinkedList<>();
        inputData = new ArrayList<>();
    }

    /**
     *
     * @param p DataPacket to be added to input queue
     * @return
     */
    public boolean addPacketToQueue(FloorRequest p){
        if(p == null) {
            return false;
        }
        inputQueue.add(p);
        return true;
    }

    /**
     * placeholder for future
     * @param receivedData
     */
    public void processData(FloorRequest receivedData) {
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
        numRequests = 0; //Counts number of requests sent by floor
        socket.start();
        while(!inputQueue.isEmpty()) {
            socket.send(inputQueue.remove());
            numRequests += 1;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Number of requests sent by Floor.FloorSubsystem: " + numRequests);

    }
    public static void main (String args[]) throws IOException {
        InputReader datafile = new InputReader("data.txt");
        FloorSubsystem floorSubsystem = new FloorSubsystem();
        Thread floorThread = new Thread(floorSubsystem); 
        while(floorSubsystem.addPacketToQueue(datafile.getNextPacket())){};
        floorSubsystem.addPacketToQueue(datafile.getNextPacket());
        floorThread.start();
    }
}