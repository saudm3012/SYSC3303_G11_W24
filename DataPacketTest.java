import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
/**
 * DataPacket test
 * will create new packet with same values as first entry in provided data.txt
 *
 * then tests dataPacketToBytes and bytesToDataPackets simulataneously
 * @Author Ali Nadim
 */
class DataPacketTest {

    DataPacket data;
    /**
     * Setup method
     * Creates new data with same value as first entry in provided data.txt file
     */
    @BeforeEach
    void setUp() {
        DataPacket data = new DataPacket("14:05:15.0","2" , "Up", "4"); //makes new packet with values
    }


    /**
     * tests dataPacketToBytes and bytesToDataPackets simulataneously by conveting Datapacket to bytes
     * then converting that same object back to a datapacket and making sure it is the same as the original
     *
     * @throws IOException
     */

    @Test
    void dataPacketToBytes() throws IOException {
        DataPacket data = new DataPacket("14:05:15.0","2" , "Up", "4"); //makes new packet with values
        byte[] packet;
        packet = data.dataPacketToBytes(); //converting data to bytes
        DataPacket data_2 = new DataPacket();
        data_2.bytesToDataPacket(packet); // converting back to packet
        assertTrue(data.equals(data_2));
    }
}