package Floor;

import Floor.FloorRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Input Reader to read data from text file in format below
 * Time, Floor, Floor Button, Car Button
 * 14:05:15.0, 2, Up, 4
 * @Author Jatin Jain
 * @Author Mohammad Saud 101195172
 */
public class InputReader {
    private ArrayList<ArrayList<String>> fileData = new ArrayList<>();
    private static int cursor = 1;
    private final String fileName;

    /**
     *
     * @param fileReadName File Floor.InputReader object will be bound to read from
     */
    public InputReader(String fileReadName) {
        this.fileName = fileReadName;
    }

    /**
     * Load data from text file.
     */
    public void loadData() throws IOException {
        // Use class loader to load the file
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IOException("File not found: " + fileName);
        }

        // Open file under buffered reader
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String st;
            // Read all lines until end and store into array list
            while ((st = br.readLine()) != null) {
                fileData.add(new ArrayList<>(Arrays.asList(st.split(" "))));
            }
        }
    }

    /**
     *
     * @return DataPacket, returns next data packet parsed from the file we are reading from
     * @throws IOException
     */
    public FloorRequest getNextPacket() throws IOException {
        if (cursor == 1) {
            this.loadData();
        }

        if (cursor < fileData.size()) {
            ArrayList<String> rowData = fileData.get(cursor);

            /* TODO use an enum instead of special numbers */
            String time = rowData.get(0);
            String floor = rowData.get(1);
            String floorButton = rowData.get(2);
            String carButton = rowData.get(3);
            try {
                String fault = rowData.get(4);
                cursor++;
                return new FloorRequest(time, floor, floorButton, carButton, false, fault);
            } catch (IndexOutOfBoundsException e){}
            cursor++;
            return new FloorRequest(time, floor, floorButton, carButton, false);
        }
        // If we are at the end of the file, return null
        return null;
    }
}
