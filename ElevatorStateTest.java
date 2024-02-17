import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.*;
import java.util.*;
public class ElevatorStateTest {
    public static Elevator elevator;
    DatagramSocket sendSocket;
    private DataPacket data;
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
        data = new DataPacket("14:05:15.0","2" , "Up", "4");

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
    public void testElevatorStateMachine(){

        //Start at IDLE state
        assertTrue(elevator.state == ElevatorStates.IDLE);

        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Wait for elevator to move to floor 1
        assertTrue(elevator.state == ElevatorStates.MOVING);
        assertTrue(elevator.currFloor == 1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Elevator moves 1 floor every 1 second
        assertTrue(elevator.currFloor == 2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Reaches floor to load passenger
        assertTrue(elevator.state == ElevatorStates.LOADING_UNLOADING);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //moving towards destination floor
        assertTrue(elevator.state == ElevatorStates.MOVING);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // reaches destination floor then unloads
        assertTrue(elevator.state == ElevatorStates.LOADING_UNLOADING);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //back to idle after opening/closing doors for 2 seconds
        assertTrue(elevator.state == ElevatorStates.IDLE);

    }

}


