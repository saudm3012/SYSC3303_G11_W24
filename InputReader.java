import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    /* Text file is stored in program absolute path
     * Each entry is retrieved as strings from input reader
     */
    private static final String ABSOLUTE_PATH = new File("").getAbsolutePath();
    private ArrayList<ArrayList<String>> fileData = new ArrayList<>();
    private static int cursor = 1;
    private final String fileName;

    /**
     *
     * @param fileReadName File InputReader object will be binded to add read from
     */
    public InputReader(String fileReadName){
        this.fileName = fileReadName;
    }

    /**
     * Load data from text file.
     */
    public void loadData() throws IOException {
        //Keep data file in src dir
        File file = new File(ABSOLUTE_PATH + "\\" + fileName);

        // Open file under buffered reader
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        // Read all lines until end and store into array list
        while ((st = br.readLine()) != null) {
            fileData.add(new ArrayList(Arrays.asList(st.split(" "))));
        }
    }

    /**
     *
     * @return DataPacket, returns next data pakcet parsed from file we are reading from
     * @throws IOException
     */
    public DataPacket getNextPacket() throws IOException {

        if(cursor == 1){this.loadData();}

        if(cursor < fileData.size()){
            ArrayList<String> rowData = fileData.get(cursor);

            /* TODO use an enum instead of special numbers */
            String time = rowData.get(0);
            String floor = rowData.get(1);
            String floorButton = rowData.get(2);
            String carButton = rowData.get(3);
            System.out.println("LINE " + cursor);
            System.out.println("SIZE " + fileData.size());
            cursor++;
            return new DataPacket(time, floor, floorButton, carButton);
        }
        //If we are at the end of the file return null
        return null;
    }
}