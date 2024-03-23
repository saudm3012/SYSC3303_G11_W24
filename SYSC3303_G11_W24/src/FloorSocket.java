import java.io.*;
import java.net.*;

public class FloorSocket extends Thread
{
   
   private DatagramPacket sendPacket, receivePacket; // packet sent and received 
   private DatagramSocket sendReceiveSocket; // socket at which data is sent or received
   private FloorSubsystem floorSubsystem; // System which will process the received data
   private InetAddress schedulerAddress;
   private final int SCHEDULER_PORT = 5000;

    /**
     * The constructor for this class.
     */
    public FloorSocket (FloorSubsystem floorSubsystem) {
        try {
            // Construct a datagram socket and bind it to any available 
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendReceiveSocket = new DatagramSocket();
            
            //receiveSocket.setSoTimeout(2000);
         } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
         }
         try {
            schedulerAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
         this.floorSubsystem = floorSubsystem; 
    }

    /**
     * Outputs sendPacket to the console
     */
    private void printSendingInfo(String dataPacket){
       System.out.println("\nFloorSubsystem: Sending packet:");
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
    private void printReceivingInfo(String dataPacket){
       System.out.println("\nFloorSubsystem: Packet received:");
       System.out.println("From host: " + receivePacket.getAddress());
       System.out.println("Host port: " + receivePacket.getPort());
       int len = receivePacket.getLength();
       System.out.println("Length: " + len);
       System.out.print("Containing: ");
       System.out.println(dataPacket);
    }
    
    /**
     *  Sends a packet to the scheduler and receives the expected reply.
     *  Both packet sent and received packet are logged to the console.
     * @param objToSend the data to send to the Scheduler
     */
    public void send(FloorRequest objToSend) {
        // serialize data into byte array
        byte[] sendDataBytes = new byte[0];
        try{
            sendDataBytes = objToSend.dataPacketToBytes();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        } 

        // Construct a datagram packet that is to be sent to port 5000 (scheduler)
        // on a specified host.
        sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, schedulerAddress, SCHEDULER_PORT);


        // log the datagram packet to be sent
        printSendingInfo(objToSend.toString());

        // Send the datagram packet to the server via the send/receive socket. 
        try {
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("FloorSubsystem: Packet sent.\n");
    }

    /**
     * Wait until a packet is received on socket
     */
    private void receive() {
        // Construct a DatagramPacket for receiving packets up 
        // to 1024 bytes long (the length of the byte array).
        FloorRequest receiveData = new FloorRequest();
        byte receiveDataBytes[] = new byte[1024];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);

        try {
            // Block until a datagram is received via sendReceiveSocket.  
            sendReceiveSocket.receive(receivePacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // convert received data and have floor subsystem process it 
        try {
            receiveData.bytesToDataPacket(receiveDataBytes);
            // log the received datagram.
            printReceivingInfo(receiveData.toString());
            floorSubsystem.processData(receiveData);
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        } 
    }
    
    public void run() {
        while(true) {
            receive();
        }
    }
}