import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.*;

public class ElevatorStateTest {
    public static Elevator elevator;
    DatagramSocket sendSocket;
    private FloorRequest data;
    private DatagramPacket sendPacket;

    InetAddress elevatorAddress;
    /**
     * Initialize the test harness.
     * Creates an Elevator and DataPacket to send.
     * @throws SocketException
     */
    @BeforeEach
    public void setup() throws SocketException {
        //create new elevator that listens on port 2005
        elevator = new Elevator(5);
        sendSocket = new DatagramSocket();

        //data to send as a DataPacket
        data = new FloorRequest("14:05:15.0","2" , "Up", "4", false);

        byte[] sendDataBytes = new byte[0];

        try {
            sendDataBytes = data.dataPacketToBytes();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            elevatorAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //DatagramPacket to send to elevator
        sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, elevatorAddress, 2005);

        elevator.start();
    }

    /**
     * Tests elevator state machine through all states.
     * Sequence: Sends a request that moves the elevator to floor 2,
     * loads then moves to floor 4, unloads and goes back to an idle state
     *
     * @author Jatin Jain
     */
    @Test
    public void testElevatorStateMachine() {

        //Start at IDLE state
        assertTrue(elevator.state == ElevatorStates.IDLE);

        try {
            sendSocket.send(sendPacket);

            Thread.sleep(1000);

            //Wait for elevator to move to floor 1
            assertTrue(elevator.state == ElevatorStates.MOVING);
            assertTrue(elevator.currFloor == 1);

            Thread.sleep(1000);

            //Elevator moves 1 floor every 1 second
            assertTrue(elevator.currFloor == 2);

            Thread.sleep(1000);

            //Reaches floor to load passenger
            assertTrue(elevator.state == ElevatorStates.LOADING);

            Thread.sleep(2000);

            //moving towards destination floor
            assertTrue(elevator.state == ElevatorStates.MOVING);

            Thread.sleep(3000);


            // reaches destination floor then unloads
            assertTrue(elevator.state == ElevatorStates.UNLOADING);
            Thread.sleep(2000);


            //back to idle after opening/closing doors for 2 seconds
            assertTrue(elevator.state == ElevatorStates.IDLE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        }
    }


