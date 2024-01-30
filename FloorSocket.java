import java.io.*;
import java.net.*;

public class FloorSocket extends Thread
{
   
   private DatagramPacket sendPacket, receivePacket; // packet sent and received 
   private DatagramSocket sendReceiveSocket; // socket at which data is sent or received
   private FloorSubsystem floorSubsystem; // data to send from input file

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
         this.floorSubsystem = floorSubsystem; 
    }

    /**
     * TODO MOVE TO ABSTRACT CLASS THAT RETURNS JUST AN OBJECT
     * Converts DataPacket class to bytes
     */
    public byte[] dataPacketToBytes(DataPacket data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(data);
            out.flush();
            return bos.toByteArray();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return new byte[0]; /* Should not run */
    }

    /**
     * TODO MOVE TO ABSTRACT CLASS THAT RETURNS JUST AN OBJECT
     * Converts DataPacket class to bytes
     */
    public DataPacket bytesToDataPacket(byte[] data_packet) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data_packet);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (DataPacket) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    /**
     * Outputs sendPacket to the console
     */
    private void printSendingInfo(String dataPacket){
       System.out.println("FloorSubsystem: Sending packet:");
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
       System.out.println("FloorSubsystem: Packet received:");
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
    public void send(DataPacket objToSend) {
        // serialize data into byte array
        byte[] sendData = new byte[0];
        try{
            sendData = dataPacketToBytes(objToSend);
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        } 

        // Construct a datagram packet that is to be sent to a specified port 
        // on a specified host.
        try {
            sendPacket = new DatagramPacket(sendData, sendData.length,
                                            InetAddress.getLocalHost(), 5000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

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

    private void receive() {
        // Construct a DatagramPacket for receiving packets up 
        // to 100 bytes long (the length of the byte array).
        DataPacket receiveData;
        byte receiveDataBytes[] = new byte[100];
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
            receiveData = bytesToDataPacket(receiveDataBytes);
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