import java.io.IOException;
import java.net.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorStateTest{
        
    public static Elevator elevator;
    DatagramSocket sendSocket;
    private FloorRequest data, dataFault, endData;
    private DatagramPacket sendPacket, sendFaultPacket, sendEndPacket;

    InetAddress elevatorAddress;
        
    /**
     * Initialize the test harness.
     * 
     * @throws SocketException
     */
    @BeforeEach
    public void setup() throws SocketException {
        //create new elevator that listens on port 2006
        elevator = new Elevator(6,5);
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        //data to send as a DataPacket
        data = new FloorRequest("14:05:15.0","2" , "Up", "4", false);
        dataFault = new FloorRequest("15:05:15.0","3" , "Down", "1", false, "DOOR");

        endData = new FloorRequest();

        byte[] sendDataBytes = new byte[0];
        byte[] sendFaultDataBytes = new byte[0];
        byte[] endDataBytes = new byte[0];
        try {
            sendDataBytes = data.dataPacketToBytes();
            sendFaultDataBytes = dataFault.dataPacketToBytes();
            endDataBytes = endData.dataPacketToBytes();
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
        sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, elevatorAddress, 2006);
        sendFaultPacket = new DatagramPacket(sendFaultDataBytes, sendDataBytes.length, elevatorAddress, 2006);
        sendEndPacket = new DatagramPacket(endDataBytes, endDataBytes.length, elevatorAddress, 2006);

        elevator.start();
    }
    
        /**
         * Tears down the test fixture.
         *
         * Called after every test case method.
         */
        protected void tearDown()
        {
            sendSocket.close();
        }
    
        /**
         * Tests the behavior of the Elevator state machine
         */
        @Test
        public void testElevatorStateMachine() {
            //Start at NOTIFY state
            assertTrue(elevator.state == ElevatorStates.NOTIFY);

            try {
                Thread.sleep(1);
                // Send a floor request
                sendSocket.send(sendPacket);
                Thread.sleep(1);
                sendSocket.send(sendEndPacket);
                Thread.sleep(50);
                assertTrue(elevator.state == ElevatorStates.MOVING);
                assertTrue(elevator.currFloor == 1);

                // Wait for elevator to move to floor 1
                Thread.sleep(2010);
                assertTrue(elevator.currFloor == 2);
                // elevator should notfiy we reached a new floor
                assertTrue(elevator.state == ElevatorStates.NOTIFY);
                sendSocket.send(sendEndPacket);
                Thread.sleep(10);
                // We are at the floor to pickup passenger so we should stop
                assertTrue(elevator.state == ElevatorStates.STOP);
                // wait for doors to close
                Thread.sleep(6020);
                // Elevator moves 1 floor towards drop-off
                assertTrue(elevator.state == ElevatorStates.MOVING);
                // wait to move floor
                Thread.sleep(2001);
                assertTrue(elevator.currFloor == 3);
                // elevator should notify we reached a new floor
                assertTrue(elevator.state == ElevatorStates.NOTIFY);
                sendSocket.send(sendEndPacket);
                Thread.sleep(10);
                //Elevator moves 1 floor towards drop-off
                assertTrue(elevator.state == ElevatorStates.MOVING);
                Thread.sleep(2020);
                assertTrue(elevator.currFloor == 4);
                // elevator should notify we reached a new floor
                assertTrue(elevator.state == ElevatorStates.NOTIFY);
                sendSocket.send(sendEndPacket);
                Thread.sleep(10);
                // we are at the destination floor so we should let the passenger off
                assertTrue(elevator.state == ElevatorStates.STOP);
                // wait for doors to close
                Thread.sleep(6001);
                // Elevator should notify of no requests to handle
                assertTrue(elevator.state == ElevatorStates.NOTIFY);
                Thread.sleep(1000);

                // Iteration 4
                // Fault recovery
                sendSocket.send(sendFaultPacket);
                Thread.sleep(10);
                sendSocket.send(sendEndPacket);
                Thread.sleep(10);
                assertTrue(elevator.state == ElevatorStates.MOVING);
                Thread.sleep(2000);
                assertTrue(elevator.state == ElevatorStates.NOTIFY);

                sendSocket.send(sendEndPacket);
                Thread.sleep(10);
                assertTrue(elevator.currFloor == 3);

                //reaches beginning floor
                assertTrue(elevator.state == ElevatorStates.STOP);
                Thread.sleep(6000);

                //Check that door was stuck
                assertTrue(elevator.state == ElevatorStates.STOP);

                Thread.sleep(3000);

                //Check that door is unstuck
                assertTrue(elevator.state == ElevatorStates.MOVING);

                Thread.sleep(3000);


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
