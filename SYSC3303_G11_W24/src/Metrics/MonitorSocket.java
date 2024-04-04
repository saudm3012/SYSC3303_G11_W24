package Metrics;

import java.io.IOException;
import java.net.*;

import Floor.FloorRequest;

public class MonitorSocket extends Thread
{

    private DatagramPacket receivePacket; // packet received
    private DatagramSocket ReceiveSocket; // socket at which data is sent or received
    private Monitor monitor; // System which will process the received data

    /**
     * The constructor for this class.
     */
    public MonitorSocket (Monitor monitor) {
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            ReceiveSocket = new DatagramSocket();

            //receiveSocket.setSoTimeout(2000);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        this.monitor = monitor;
    }

    private void receive() {
        // Construct a DatagramPacket for receiving packets up
        // to 1024 bytes long (the length of the byte array).
        byte receiveDataBytes[] = new byte[8];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);

        try {
            // Block until a datagram is received via sendReceiveSocket.
            ReceiveSocket.receive(receivePacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        monitor.processData(receiveDataBytes);
    }

    public void run() {
        while(true) {
            receive();
        }
    }
}