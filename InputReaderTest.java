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
 * @Author Riya Arora 101190033
 */

class InputReaderTest {
    private ArrayList<ArrayList<String>> inputData;
    FloorRequest data;

    /**
     * Setup method
     * Creates new data with same value as first entry in provided data.txt file
     */
    @BeforeEach
    void setUp() {
        data = new FloorRequest("14:05:15.0","2" , "Up", "4");

    }
    /**
     * Test function method
     * check if it is the same as when getNextPacket() function is initialised
     * @throws IOException
     */
    @Test
    void testloadData() throws IOException {
        InputReader datafile = new InputReader("data.txt");
        FloorRequest data2 = datafile.getNextPacket();
        assertTrue(data.equals(data2));

    }
}