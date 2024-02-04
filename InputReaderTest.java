import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * InputReader Test
 * will create new packet with same values as first entry in provided data.txt and check if it is the same as
 *  * when getNextPacket() function is initialised
 * @Author Ali Nadim
 */

class InputReaderTest {
    private ArrayList<ArrayList<String>> inputData;
    DataPacket data;

    /**
     * Creates new data with same value as first entry in provided data.txt file
     */
    @BeforeEach
    void setUp() {
        data = new DataPacket("14:05:15.0","2" , "Up", "4");

    }
    /**
     * check if it is the same as when getNextPacket() function is initialised
     */
    @Test
    void testloadData() throws IOException {
        InputReader datafile = new InputReader("data.txt");
        DataPacket data2 = datafile.getNextPacket();
        assertTrue(data.equals(data2));

    }
}