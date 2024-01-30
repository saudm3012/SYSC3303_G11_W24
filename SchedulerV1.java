import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//Socket version
// Riya Arora 101190033
public class SchedulerV1 {
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

    // Method to process requests and coordinate between the subsystems
    public void processRequests() {
        DatagramSocket receiveSocket = null;
        ElevatorDataPacket elevatorDataPacket = new ElevatorDataPacket();

        try {
            // Create a DatagramSocket to receive requests from FloorSubsystem
            receiveSocket = new DatagramSocket(5000);

            while (true) {
                byte[] receiveData = new byte[100];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Block until a datagram is received via receiveSocket
                receiveSocket.receive(receivePacket);

                // Deserialize the received data into ElevatorData
                ElevatorData receivedData = elevatorDataPacket.bytes_to_elevatordata(receivePacket.getData());

                // Process the received request (add your logic here)
                System.out.println("Received Floor Request: " + receivedData);

                // Add your logic to determine how to handle the request (e.g., assign an elevator)

                // Print a message indicating that the scheduler received the request
                System.out.println("Scheduler received: " + receivedData);

                // Simulate some processing time
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (receiveSocket != null && !receiveSocket.isClosed()) {
                receiveSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        SchedulerV1 scheduler = new SchedulerV1();

        // Start processing requests
        scheduler.processRequests();
    }
}
