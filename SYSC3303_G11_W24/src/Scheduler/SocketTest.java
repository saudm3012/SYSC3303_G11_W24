package Scheduler;

import Floor.FloorRequest;
import Floor.FloorSubsystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Gui.ElevatorGUI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static org.junit.jupiter.api.Assertions.*;

// @author editted by Riya Arora for GUI integration (101190033)

/**
 * Floor Socket test
 * Will open a socket and send data through it using floorSubsystem Send() method
 * Then will receive the packet and make sure the values are equal
 * @Author Ali Nadim
 */

class FloorSocketTest{
    private DatagramPacket receivePacket;
    private DatagramSocket sendReceiveSocket,receiveSocket;
    private FloorSubsystem sub;

    private Scheduler sch;

    private FloorRequest data;


    /**
     * Setup method
     * Creates new data with same value as first entry in provided data.txt file
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        receiveSocket = new DatagramSocket();
        sendReceiveSocket = new DatagramSocket();
        sch = new Scheduler();

        sub = new FloorSubsystem();
        data = new FloorRequest("14:05:15.0","2" , "Up", "4", false); //makes new packet with values

    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    protected void tearDown()
    {
        receiveSocket.close();
        sendReceiveSocket.close();
    }
    /**
     * Test function method
     * open a socket and send data through it using floorSubsystem Send() method
     * then checks if it got added to scheduler queue
     * @throws IOException
     */
    @Test
    void testSend() throws IOException {
        sub.socket.start();
        sub.socket.send(data);
        System.out.println(sch.upQueue.isEmpty());
        assertTrue(sch.upQueue.isEmpty());
    }
}
