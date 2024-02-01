import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * The Scheduler class that sends and receives packets from the FloorSocket to the ElevatorSocket
 * @Author Mohammad Saud
 */
public class Scheduler implements Runnable{
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
        DataPacket receivedData = new DataPacket();
        try {
            receivedData.bytesToDataPacket(receivePacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Process the received request (add your logic here)
        System.out.println("Scheduler Received Floor Request: " + receivedData);
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
        System.out.println("SCHEDULER STARTS");
        try {
            this.getPacketFromFloor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("SCHEDULER ENDS");
        return;
    }
}
