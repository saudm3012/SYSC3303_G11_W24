package Metrics;

import Floor.FloorRequest;

import java.util.Queue;
import java.util.stream.StreamSupport;

public class Monitor extends Thread{
    private int time;

    private int requestCompleted;

    private int throughput;

    /**
     * Constructs a new monitor object
     *
     */
    public Monitor(){
        this.time = 0;
        this.requestCompleted = 0;
        this.throughput = 0;
    }

    /**
     * placeholder for future
     * @param receivedData
     */
    public void processData(byte[] receivedData) {
        int intValue = 0;
        for (byte b : receivedData) {
            intValue = (intValue << 8) + (b & 0xFF);
        }
        requestCompleted += intValue;
    }

    public void run(){
        while(true){
            try {
                Thread.sleep(1000);
                time += 1;
                throughput = requestCompleted/time;
                System.out.println("Througput:" + throughput);

            } catch (InterruptedException e){
            }

        }
    }
}
