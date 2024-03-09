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
         * Tests the behavior of the Traffic Light state machine when a pedestrian arrives early.
         */
        public void testEarlyPedestrian() {
        //Start at IDLE state
        assertTrue(elevator.state == ElevatorStates.STOP);

        try {
            sendSocket.send(sendPacket);
            Thread.sleep(1000);
            sendSocket.send(sendEndPacket);


            Thread.sleep(1000);
            sendSocket.send(sendEndPacket);
            //Wait for elevator to move to floor 1
            assertTrue(elevator.state == ElevatorStates.MOVING);
            assertTrue(elevator.currFloor == 1);

            Thread.sleep(1000);
            sendSocket.send(sendEndPacket);

            //Elevator moves 1 floor every 1 second
            assertTrue(elevator.currFloor == 2);

            Thread.sleep(1000);
            sendSocket.send(sendEndPacket);

            //Reaches floor to load passenger
            assertTrue(elevator.state == ElevatorStates.STOP);

            Thread.sleep(2000);
            sendSocket.send(sendEndPacket);

            //moving towards destination floor
            assertTrue(elevator.state == ElevatorStates.MOVING);

            Thread.sleep(3000);
            sendSocket.send(sendEndPacket);

            // reaches destination floor then unloads
            assertTrue(elevator.state == ElevatorStates.STOP);
            Thread.sleep(2000);
            sendSocket.send(sendEndPacket);


            //back to idle after opening/closing doors for 2 seconds
            assertTrue(elevator.state == ElevatorStates.STOP);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
