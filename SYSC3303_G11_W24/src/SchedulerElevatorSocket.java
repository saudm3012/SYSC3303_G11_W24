
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
public class SchedulerElevatorSocket extends Thread implements  AutoCloseable{
    DatagramSocket elevatorReceiveSocket;
    InetAddress elevatorAddress;
    DatagramSocket sendSocket;
    Scheduler scheduler;
    DatagramPacket sendPacket;
    DatagramPacket receivePacket;
    private final int ELEVATOR_PORT = 2000;
    public SchedulerElevatorSocket(Scheduler scheduler){
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 5000
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets from floor subsystem and elevators.
            elevatorReceiveSocket = new DatagramSocket(4998);

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            elevatorAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.scheduler = scheduler;
    }

    public SchedulerElevatorSocket(Scheduler scheduler, String elevatorAddress){
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 5000
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets from floor subsystem and elevators.
            elevatorReceiveSocket = new DatagramSocket(4999);

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            this.elevatorAddress = InetAddress.getByName(elevatorAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.scheduler = scheduler;
    }

    void sendToElevator(FloorRequest packet, int elevatorNum) {
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
        sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, elevatorAddress, ELEVATOR_PORT+elevatorNum);

        // log the datagram packet to be sent
        if (!packet.isEnd()) {
            printSendingInfo(packet.toString());
        } else {
            System.out.println("[SCHEDULER]: SENDING END PACKET to ELEVATOR-" + elevatorNum + "\n");
        }

        // Send the datagram packet to the server via the send/receive socket.
        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Scheduler: Packet sent to [ELEVATOR-" + elevatorNum + "] .\n");
    }

    int receiveFromElevator() {
        // Construct a DatagramPacket for receiving packets up to 1024 bytes long (the length of the byte array).
        ElevatorData incoming_elev_data = new ElevatorData();
        byte receiveDataBytes[] = new byte[1024];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);

        try {
            // Block until a datagram is received via sendReceiveSocket.
            elevatorReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            if(e instanceof SocketTimeoutException){
                return 0;
            }
            e.printStackTrace();
            System.exit(1);
        }

        try {
            incoming_elev_data.bytesToDataPacket(receivePacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.print("[SCHEDULER]: Received request from ELEVATOR-" + incoming_elev_data.getElevatorNum()+"\n");
        scheduler.elevatorQueueAdd(incoming_elev_data);
        return 1;
    }

    /**
     * Outputs sendPacket to the console
     */
    void printSendingInfo(String dataPacket) {
        System.out.println("\nScheduler: Sending packet to: Elevator");
        System.out.println("To host: " + sendPacket.getAddress());
        System.out.println("Destination host port: " + sendPacket.getPort());
        int len = sendPacket.getLength();
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

        if (elevatorReceiveSocket != null && !elevatorReceiveSocket.isClosed()) {
            elevatorReceiveSocket.close();
        }
    }

    public void run(){
        while(true){
            receiveFromElevator();
        }
    }

}
