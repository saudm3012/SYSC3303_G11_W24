import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class SchedulerSocket extends Thread implements  AutoCloseable{
    DatagramPacket sendPacket;
    DatagramPacket receivePacket;
    DatagramSocket sendSocket;
    DatagramSocket floorReceiveSocket;
    DatagramSocket elevatorReceiveSocket;
    private Scheduler scheduler;
    InetAddress floorAddress;
    InetAddress elevatorAddress;
    private int elevatorPort;
    private int floorPort;


    public SchedulerSocket(Scheduler scheduler){
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 5000
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets from floor subsystem and elevators.
            floorReceiveSocket = new DatagramSocket(5000);
            elevatorReceiveSocket = new DatagramSocket(4999);

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
        elevatorPort = 2000;
        this.scheduler = scheduler;
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


    void sendToElevator(DataPacket packet) {
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
        sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, elevatorAddress, elevatorPort);

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
            floorReceiveSocket.receive(receivePacket);
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
            scheduler.receiveQueueAdd(receiveData);
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


    void receiveFromElevator(){
        // Construct a DatagramPacket for receiving packets up to 1024 bytes long (the length of the byte array).
        DataPacket receiveData = new DataPacket();
        byte receiveDataBytes[] = new byte[0];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);

        try {
            // Block until a datagram is received via sendReceiveSocket.
            elevatorReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }




    /**
     * Outputs sendPacket to the console
     */
    void printSendingInfo(String dataPacket, boolean isToFloor) {
        System.out.println("\nScheduler: Sending packet to: " + (isToFloor ? "Elevator" : "Floor Subsystem"));
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
        System.out.println("\nScheduler: Packet received from: " + (isFromFloor ? "Floor Subsystem" : "Elevator"));
        System.out.println("From host: " + receivePacket.getAddress());
        System.out.println("Host port: " + receivePacket.getPort());
        int len = receivePacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        System.out.println(dataPacket);
    }

    /**
     * Close resources when the Scheduler is no longer needed.
     */
    @Override
    public void close() {
        if (sendSocket != null && !sendSocket.isClosed()) {
            sendSocket.close();
        }

        if (floorReceiveSocket != null && !floorReceiveSocket.isClosed()) {
            floorReceiveSocket.close();
        }

        if (elevatorReceiveSocket != null && !elevatorReceiveSocket.isClosed()) {
            elevatorReceiveSocket.close();
        }
    }


    public void run(){
        while(true){
            receive();
        }
    }


}
