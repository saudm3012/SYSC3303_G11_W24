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
public class Scheduler implements Runnable {
    private SchedulerSocket socket;
    private int floorPort;
    private SchedulerState state = SchedulerState.IDLE;
    Queue<DataPacket> receiveQueue;

    /**
     * The constructor for this class.
     */
    public Scheduler() {
        socket = new SchedulerSocket(this);

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

    public boolean receiveQueueIsEmpty(){
        return receiveQueue.isEmpty();
    }

    public DataPacket receiveQueueRemove(){
        return receiveQueue.remove();
    }

    public void receiveQueueAdd(DataPacket data){
        receiveQueue.add(data);
    }

    private void idle(){
        // How we actually want to do it with a separate Scheduler socket class, for now just wait here
        while(receiveQueue.isEmpty()){
            //Wait for the receive queue to fill up
            System.out.println("Wait for fill up");
            sleep(1000);

        }
        System.out.println("Filled up");
        sleep(100);
        socket.sendToElevator(receiveQueue.remove());
        this.state = SchedulerState.WAIT_ACK;
    }

    private void wait_ack(){
        socket.receiveFromElevator();
        System.out.println("RECEIVED FROM ELEVATOR");
        sleep(100);
        this.state = SchedulerState.IDLE;
    }

    public void execute(){
        switch (this.state){
            case IDLE:{
                System.out.println("[SCHEDULER] IDLE");
                this.idle();
            }

            case WAIT_ACK:{
                System.out.println("[SCHEDULER] WAIT_ACK");
                this.wait_ack();
            }
        }
    }

    public void run() {
        socket.start();
        while (true) {
            execute();
        }
    }
}

