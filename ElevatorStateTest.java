import java.io.IOException;
import java.net.*;
import junit.framework.TestCase;

    public class ElevatorStateTest extends TestCase {
        
        public static Elevator elevator;
        DatagramSocket sendSocket;
        private FloorRequest data, endData;
        private DatagramPacket sendPacket, sendEndPacket;

        InetAddress elevatorAddress;
        
        /**
         * Sets up the test fixture.
         *
         * Called before every test case method.
         */
        protected void setUp() {
            //create new elevator that listens on port 2005
            elevator = new Elevator(5,5);
            try {
                sendSocket = new DatagramSocket();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            //data to send as a DataPacket
            data = new FloorRequest("14:05:15.0","2" , "Up", "4", false);
            endData = new FloorRequest();

            byte[] sendDataBytes = new byte[0];
            byte[] endDataBytes = new byte[0];
            try {
                sendDataBytes = data.dataPacketToBytes();
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
            sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, elevatorAddress, 2005);
            sendEndPacket = new DatagramPacket(endDataBytes, endDataBytes.length, elevatorAddress, 2005);

            elevator.start();
        }
    
        /**
         * Tears down the test fixture.
         *
         * Called after every test case method.
         */
        protected void tearDown()
        {
        }
    
        /**
         * Tests the behavior of the Elevator state machine
         */
        public void testElevator() {
        //Start at NOTIFY state
        assertTrue(elevator.state == ElevatorStates.NOTIFY);

        try {
            Thread.sleep(1);
            // Send a floor request
            sendSocket.send(sendPacket);
            Thread.sleep(1);
            sendSocket.send(sendEndPacket);
            Thread.sleep(20);
            assertTrue(elevator.state == ElevatorStates.MOVING);
            assertTrue(elevator.currFloor == 1);

            // Wait for elevator to move to floor 1
            Thread.sleep(2000);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
