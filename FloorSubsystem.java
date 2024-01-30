/**
 * The FloorSubsystem thread simulates the arrival of passengers to the elevators and for simulating all button 
 * presses and lamps at the floor.
 *
 * @author  Zakariya Khan 101186641
 */


import java.io.*;
import java.net.*;
import java.util.*;

public class FloorSubsystem implements Runnable
{
   
   private DatagramPacket sendPacket, receivePacket; // packet sent and received 
   private DatagramSocket sendReceiveSocket; // socket at which data is sent or received
   private Queue<ElevatorData> inputQueue; // data to send from input file
   private ElevatorDataPacket packetObj; // Class to convert Elevator data to bytes and back

    /**
     * The constructor for this class.
     */
    public FloorSubsystem () {
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
    }

    /**
     * Outputs sendPacket to the console
     */
    private void printSendingInfo(){
       System.out.println("FloorSubsystem: Sending packet:");
       System.out.println("To host: " + sendPacket.getAddress());
       System.out.println("Destination host port: " + sendPacket.getPort());
       int len = sendPacket.getLength();
       System.out.println("Length: " + len);
       System.out.print("Containing: ");
       System.out.println(new String(sendPacket.getData(),0,len)); 
    }

    /**
     * Outputs receivePacket to the console
     */
    private void printReceivingInfo(){
       System.out.println("FloorSubsystem: Packet received:");
       System.out.println("From host: " + receivePacket.getAddress());
       System.out.println("Host port: " + receivePacket.getPort());
       int len = receivePacket.getLength();
       System.out.println("Length: " + len);
       System.out.print("Containing: ");
       String received = new String(receivePacket.getData(),0,len);   
       System.out.println(received);
    }
    
    /**
     *  Sends a packet to the scheduler and receives the expected reply.
     *  Both packet sent and received packet are logged to the console.
     * @param objToSend the data to send to the Scheduler
     */
    public void sendAndReceive(ElevatorData objToSend) {
        // serialize data into byte array
        byte[] sendData = new byte[0];
        try{
            sendData = packetObj.elevatordata_to_bytes(objToSend);
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
        printSendingInfo();

        // Send the datagram packet to the server via the send/receive socket. 
        try {
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("FloorSubsystem: Packet sent.\n");

        // Construct a DatagramPacket for receiving packets up 
        // to 100 bytes long (the length of the byte array).

        byte receiveData[] = new byte[100];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);

        try {
            // Block until a datagram is received via sendReceiveSocket.  
            sendReceiveSocket.receive(receivePacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // log the received datagram.
        printReceivingInfo();
    }

    public void run() {
        while(inputQueue.size() != 0) {
            sendAndReceive(inputQueue.remove());
        }
        // close the socket.
       sendReceiveSocket.close();
    }
}