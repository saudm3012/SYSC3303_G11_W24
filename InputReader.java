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
 */
public class InputReader {
    /* Text file is stored in program absolute path
     * Each entry is retrieved as strings from input reader
     */
    private static final String ABSOLUTE_PATH = new File("").getAbsolutePath();
    private ArrayList<ArrayList<String>> fileData = new ArrayList<>();

    /**
     * Load data from text file.
     *
     * @param fileName String, the name of the file.
     */
    public void loadData(String fileName) throws IOException {
        //Keep data file in src dir
        File file = new File(ABSOLUTE_PATH + "\\src\\" + fileName);

        // Open file under buffered reader
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        // Read all lines until end and store into array list
        while ((st = br.readLine()) != null) {
            fileData.add(new ArrayList(Arrays.asList(st.split(", "))));
        }
    }

    /**
     * Gets file data.
     *
     * @return ArrayList<ArrayList<String>>, the parsed data.
     */
    public ArrayList<ArrayList<String>> getFileData() {
        return fileData;
    }
}