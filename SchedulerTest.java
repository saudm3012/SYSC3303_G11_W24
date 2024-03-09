import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.*;

/**
 * Tests scheduler state machine through all states.
 **/
public class SchedulerTest {
    public static Scheduler schedulerObj;
    DatagramSocket sendSocket;
    private FloorRequest data;
    private DatagramPacket sendFloorPacket;
    private DatagramPacket sendElevatorPacket;

    InetAddress localAddress;

    /**
     * Initialize the test harness.
     * Creates a scheduler and DataPacket to send acting as the floor request and elevator reply
     *
     * @throws SocketException
     */
    @BeforeEach
    public void setup() throws SocketException {
        sendSocket = new DatagramSocket();
        schedulerObj = new Scheduler();
        //data to send as a DataPacket
        data = new FloorRequest("14:05:15.0", "2", "Up", "4", false);

        byte[] sendDataBytes = new byte[0];

        try {
            sendDataBytes = data.dataPacketToBytes();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            localAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //DatagramPacket to send to elevator
        sendFloorPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, localAddress, 5000);
        sendElevatorPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, localAddress, 4999);

        Thread scheduler =  new Thread(schedulerObj);
        scheduler.start();
    }

    /**
     * Tests scheduler state machine through all states.
     * Sequence: Sends a packet to port 5000 acting as a request from floor
     * Check for transition from idle to wait
     * Sends a packet to port 4999 acting as a reply back from elevator
     * Check for transition from wait to idle
     *
     * @author Jatin Jain
     */
    @Test
    public void testElevatorStateMachine(){
        try {
            // Check if scheduler is in idle
            assertTrue(schedulerObj.state == SchedulerState.IDLE);

            //Send a packet to port 5000 to act like it comes from floor
            sendSocket.send(sendFloorPacket);
            System.out.println(schedulerObj.state);

            //Send a packet to port 4999 to act like it comes from elevator
            sendSocket.send(sendElevatorPacket);
            Thread.sleep(1500);
            assertTrue(schedulerObj.state == SchedulerState.IDLE);


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}



