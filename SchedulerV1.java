import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//Socket version
// Riya Arora 101190033
public class SchedulerV1 implements Runnable{
    private BlockingQueue<String> floorRequestsQueue;  // Queue for receiving floor requests from the Floor subsystem

    public SchedulerV1() {
        this.floorRequestsQueue = new LinkedBlockingQueue<>();
    }

    // Method to receive floor requests from the Floor subsystem
    public void receiveFloorRequest(String request) {
        try {
            floorRequestsQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to receive packet from floor subsystem
    public void getPacketFromFloor() throws InterruptedException {
        DatagramSocket receiveSocket = null;

        // Open a new socket on 5000, the receiving port
        try {
            receiveSocket = new DatagramSocket(5000);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            // Block until a datagram is received via receiveSocket
            receiveSocket.receive(receivePacket);
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            if (receiveSocket != null && !receiveSocket.isClosed()) {
                receiveSocket.close();
            }
        }
        // Deserialize the received data into ElevatorData
        ElevatorData receivedData = new ElevatorData();
        try {
            receivedData.bytes_to_elevatordata(receivePacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Process the received request (add your logic here)
        System.out.println("Received Floor Request: " + receivedData);

        // Add your logic to determine how to handle the request (e.g., assign an elevator)

        // Print a message indicating that the scheduler received the request
        System.out.println("Scheduler received: " + receivedData);
    }
    //Method send packet to elevator
    public void sendPacketToElevator(){
        
    }

    public void getPacketFromElevator(){

    }

    public void sendPacketToFloor(){

    }
    public void run() {
        // Start processing requests
        System.out.println("ENTER");
        try {
            this.getPacketFromFloor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done");
        return;
    }
}
