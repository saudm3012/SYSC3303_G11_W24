package Tests;

import Floor.FloorRequest;
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
class FloorRequestTest {

    FloorRequest data;
    /**
     * Setup method
     * Creates new data with same value as first entry in provided data.txt file
     */
    @BeforeEach
    void setUp() {
        FloorRequest data = new FloorRequest("14:05:15.0","2" , "Up", "4", false); //makes new packet with values
    }


    /**
     * tests dataPacketToBytes and bytesToDataPackets simulataneously by conveting Datapacket to bytes
     * then converting that same object back to a datapacket and making sure it is the same as the original
     *
     * @throws IOException
     */

    @Test
    void dataPacketToBytes() throws IOException {
        FloorRequest data = new FloorRequest("14:05:15.0","2" , "Up", "4", false); //makes new packet with values
        byte[] packet;
        packet = data.dataPacketToBytes(); //converting data to bytes
        FloorRequest data_2 = new FloorRequest();
        data_2.bytesToDataPacket(packet); // converting back to packet
        assertTrue(data.equals(data_2));
    }
}