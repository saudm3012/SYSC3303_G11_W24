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
    private String inputFilename;
    private ArrayList<ArrayList<String>> inputData;
    /**
     * The constructor for this class.
     */
    public FloorSubsystem (String inputFilename) {
        this.inputFilename = inputFilename;
        socket = new FloorSocket(this);
        inputQueue = new LinkedList<>();
        inputData = new ArrayList<>();
    }

    /**
     * Loads data from the input file.
     *
     * @throws IOException Exception, filereader IO exception.
     */
    public void loadData() throws IOException {
        InputReader r = new InputReader();
        r.loadData(this.inputFilename);                 // Open the specified input file.
        this.inputData = r.getFileData();
    }

    /**
     * Creates data packet from specified input row data.
     *
     * @param numLine line number of entry
     * @return DataPacket, the data packet holding the row's data.
     */
    private DataPacket createPacketFromRow(int numLine) {
        ArrayList<String> rowData = inputData.get(numLine);

        /* TODO use an enum instead of special numbers */
        String time = rowData.get(0);
        String floor = rowData.get(1);
        String floorButton = rowData.get(2);
        String carButton = rowData.get(3);

        return new DataPacket(time, floor, floorButton, carButton);
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
        try {                       // Load input requests
            this.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int cursor = 1; cursor < inputData.size(); cursor++) {
            // Create the next packet and update the row cursor.
            DataPacket p = this.createPacketFromRow(cursor);
            //System.out.println("FLOOR READ:" + p.toString()); DEBUG
            inputQueue.add(p);
        }
        socket.start();
        while(!inputQueue.isEmpty()) {
            socket.send(inputQueue.remove());
        }

    }
}