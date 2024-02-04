import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Scheduler class that sends and receives packets from the FloorSocket to the ElevatorSocket
 * @Author Mohammad Saud
 * @Author Zakariya Khan
 * @Author Riya Arora 101190033
 */
public class Scheduler implements Runnable, AutoCloseable {

    DatagramPacket sendPacket;
    DatagramPacket receivePacket;
    DatagramSocket sendSocket;
    DatagramSocket receiveSocket;

    InetAddress floorAddress;
    InetAddress elevatorAddress;

    private int floorPort;
    private int elevatorPort;

    Queue<DataPacket> receiveQueue;

    /**
     * The constructor for this class.
     */
    public Scheduler() {
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 5000
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets from floor subsystem and elevators.
            receiveSocket = new DatagramSocket(5000);

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            floorAddress = InetAddress.getLocalHost();
            elevatorAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        elevatorPort = 5001;
        receiveQueue = new LinkedList<>();
    }

    /**
     * Issue a sleep
     *
     * @param ms time in milliseconds
     */
    void sleep(int ms) {
        // Slow things down (wait 5 seconds)
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Outputs sendPacket to the console
     */
    void printSendingInfo(String dataPacket, boolean isToFloor) {
        System.out.println("Scheduler: Sending packet to: " + (isToFloor ? "Elevator" : "Floor Subsystem"));
        System.out.println("To host: " + sendPacket.getAddress());
        System.out.println("Destination host port: " + sendPacket.getPort());
        int len = sendPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        System.out.println(dataPacket);
    }

    /**
     * Outputs receivePacket to the console
     */
    void printReceivingInfo(String dataPacket, boolean isFromFloor) {
        System.out.println("Scheduler: Packet received from: " + (isFromFloor ? "Floor Subsystem" : "Elevator"));
        System.out.println("From host: " + receivePacket.getAddress());
        System.out.println("Host port: " + receivePacket.getPort());
        int len = receivePacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        System.out.println(dataPacket);
    }

    /**
     * Sends packets from the receive queue to either elevator or floor class.
     * packets sent are logged to the console.
     */
    void send() {
        while (!receiveQueue.isEmpty()) {
            DataPacket packet = receiveQueue.remove();
            // serialize data into byte array
            byte[] sendDataBytes = new byte[0];
            try {
                sendDataBytes = packet.dataPacketToBytes();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // Construct a datagram packet that is to be sent to either floor subsystem or
            // elevator.
            sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length,
                    (packet.isFromFloor() ? elevatorAddress : floorAddress),
                    (packet.isFromFloor() ? elevatorPort : floorPort));

            // log the datagram packet to be sent
            printSendingInfo(packet.toString(), packet.isFromFloor());

            // Send the datagram packet to the server via the send/receive socket.
            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Scheduler: Packet sent.\n");
            sleep(100);
        }
    }

    /**
     * Receive on receiveSocket
     */
    void receive() {
        //System.out.println("Inside receive method");

        // Construct a DatagramPacket for receiving packets up to 1024 bytes long (the length of the byte array).
        DataPacket receiveData = new DataPacket();
        byte receiveDataBytes[] = new byte[1024];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);

        //System.out.println("Before receiving packet");

        try {
            // Block until a datagram is received via sendReceiveSocket.
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // convert received data and have the elevator process it
        try {
            receiveData.bytesToDataPacket(receiveDataBytes);
            // log the received datagram.
            printReceivingInfo(receiveData.toString(), receiveData.isFromFloor());
            // add to receive queue
            receiveQueue.add(receiveData);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // get IP and port info from floor (necessary for the first transmission)
        if (receiveData.isFromFloor()) {
            floorAddress = receivePacket.getAddress();
            floorPort = receivePacket.getPort();
        }
    }

    /**
     * Close resources when the Scheduler is no longer needed.
     */
    @Override
    public void close() {
        if (sendSocket != null && !sendSocket.isClosed()) {
            sendSocket.close();
        }

        if (receiveSocket != null && !receiveSocket.isClosed()) {
            receiveSocket.close();
        }
    }

    public void run() {
        while (true) {
            receive();
            send();
        }
    }
}

