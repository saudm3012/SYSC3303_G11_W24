package Scheduler;

import Floor.FloorRequest;

import java.io.IOException;
import java.net.*;

public class SchedulerSocket extends Thread implements  AutoCloseable{
    DatagramPacket sendPacket;
    DatagramPacket receivePacket;
    DatagramSocket sendSocket;
    DatagramSocket floorReceiveSocket;
    private Scheduler scheduler;
    InetAddress elevatorAddress;


    /*
     * The Constructor of this class.
     */
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
     * Receive on receiveSocket
     */
    void receive() {
        //System.out.println("Inside receive method");

        // Construct a DatagramPacket for receiving packets up to 1024 bytes long (the length of the byte array).
        FloorRequest receiveData = new FloorRequest();
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
            printReceivingInfo(receiveData.toString(), true);
            // add to receive queue
            scheduler.receiveQueueAdd(receiveData);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Outputs sendPacket to the console
     */
    void printSendingInfo(String dataPacket, boolean isToFloor) {
        System.out.println("\nScheduler.Scheduler: Sending packet to: " + (isToFloor ? "Floor Subsystem": "Elevator"));
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
        System.out.println("\nScheduler.Scheduler: Packet received from: " + (isFromFloor ? "Floor Subsystem" : "Elevator"));
        System.out.println("From host: " + receivePacket.getAddress());
        System.out.println("Host port: " + receivePacket.getPort());
        int len = receivePacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        System.out.println(dataPacket);
    }

    /**
     * Close resources when the Scheduler.Scheduler is no longer needed.
     */
    @Override
    public void close() {
        if (sendSocket != null && !sendSocket.isClosed()) {
            sendSocket.close();
        }

        if (floorReceiveSocket != null && !floorReceiveSocket.isClosed()) {
            floorReceiveSocket.close();
        }
    }


    public void run(){
        while(true){
            receive();
        }
    }


}
