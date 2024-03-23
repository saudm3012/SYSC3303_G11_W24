import java.io.*;
import java.net.*;

public class ElevatorSocket extends Thread
{
   
   private DatagramPacket sendPacket, receivePacket; // packet sent and received 
   private DatagramSocket sendReceiveSocket; // socket at which data is sent or received
   private Elevator elevator; // System which will process the received data
   private InetAddress schedulerAddress;
   private final int SCHEDULER_PORT = 4998;
   private boolean printlatch; // used to control prints

    /**
     * The constructor for this class.
     */
    public ElevatorSocket (int port, Elevator elevator) {
        try {
            // Construct a datagram socket and bind it to 2000 + elevator num
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendReceiveSocket = new DatagramSocket(port);
            
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
         this.elevator = elevator; 
         this.printlatch = false;
    }

    /**
     * Outputs sendPacket to the console
     */
    private void printSendingInfo(String dataPacket){
       System.out.println("\nElevator-"+sendReceiveSocket.getLocalPort()%2000+": Sending packet:");
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
       System.out.println("\nElevator-"+sendReceiveSocket.getLocalPort()%2000+": Packet received:");
       System.out.println("From host: " + receivePacket.getAddress());
       System.out.println("Host port: " + receivePacket.getPort());
       int len = receivePacket.getLength();
       System.out.println("Length: " + len);
       System.out.print("Containing: ");
       System.out.println(dataPacket);
    }
    public void setprintLatch(boolean set){
        this.printlatch = set;
    }
    /**
     *  Sends a packet to the scheduler and receives the expected reply.
     *  Both packet sent and received packet are logged to the console.
     * @param objToSend the data to send to the Scheduler
     */
    public void send(ElevatorData objToSend) {
        // serialize data into byte array
        byte[] sendDataBytes = new byte[0];
        try{
            sendDataBytes = objToSend.dataPacketToBytes();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        } 

        // Construct a datagram packet that is to be sent to port 4999 (scheduler)
        // on a specified host.

            sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length, schedulerAddress, SCHEDULER_PORT);


        // log the datagram packet to be sent
        if (printlatch) printSendingInfo(objToSend.toString());

        // Send the datagram packet to the server via the send/receive socket. 
        try {
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Elevator: Packet sent.\n");
    }

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

        // convert received data and have elevator process it 
        try {
            receiveData.bytesToDataPacket(receiveDataBytes);
            // log the received datagram.
            if (!receiveData.isEnd()) {
                 if (printlatch) printReceivingInfo(receiveData.toString());
            } else {
                System.out.println("End packet received");
            }
            elevator.processData(receiveData);
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        } 
    }
    
     /**
      * Close the socket of elevators
      */
      public void closeSockets() {
        // We're finished, so close the sockets.
        this.sendReceiveSocket.close();
        System.exit(1);
    }

    public void run() {
        while(true) {
            receive();
        }
    }
}