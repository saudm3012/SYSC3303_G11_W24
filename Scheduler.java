
/**
 * The Scheduler class that manages communication between FloorSocket and ElevatorSocket.
 *
 * This class acts as the central controller, receiving data packets from FloorSocket,
 * and forwarding them to ElevatorSocket. It transitions between the IDLE and WAIT_ACK states.
 *
 * @author Mohammad Saud
 * @author Riya Arora (101190033)
 */

import java.util.LinkedList;
import java.util.Queue;

public class Scheduler implements Runnable {

    /**
     * The socket responsible for communication with elevators.
     */
    private SchedulerSocket socket;

    /**
     * The current state of the scheduler.
     */
    public SchedulerState state = SchedulerState.IDLE;

    /**
     * Queue for storing received data packets.
     */
    Queue<FloorRequest> receiveQueue;
    Queue<FloorRequest> upQueue;
    Queue<FloorRequest> downQueue;
    FloorRequest endPacket;

    /**
     * The constructor for this class.
     */
    public Scheduler() {
        socket = new SchedulerSocket(this);
        receiveQueue = new LinkedList<>();
        endPacket = new FloorRequest("","","","",true);
    }

    /**
     * Delays the execution of the thread for the specified time.
     *
     * @param ms Time in milliseconds to sleep.
     */
    void sleep(int ms) {
        // Slow things down (wait for the specified time)
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Checks if the receive queue is empty.
     *
     * @return true if the receive queue is empty, false otherwise.
     */
    public boolean receiveQueueIsEmpty() {
        return receiveQueue.isEmpty();
    }

    /**
     * Removes and returns the next data packet from the receive queue.
     *
     * @return The next data packet in the receive queue.
     */
    public FloorRequest receiveQueueRemove() {
        return receiveQueue.remove();
    }

    /**
     * Adds a data packet to the receive queue.
     *
     * @param data The data packet to be added.
     */
    public void receiveQueueAdd(FloorRequest data) {
        receiveQueue.add(data);
    }

    /**
     * Handles the IDLE state, waiting for the receive queue to fill up and then
     * sending data to the elevator.
     */

    private void idle() {
        while(true) {
            if (socket.receiveFromElevator() == 1) {
                this.state = SchedulerState.SELECT_REQ;
                break;
            } else {
                if (!receiveQueue.isEmpty()) {
                    sleep(100);
                    this.state = SchedulerState.PROCESS_REQ;
                    break;
                }
            }
        }
    }

    private void process_request() {
        FloorRequest currentReq = receiveQueue.remove();
        if(currentReq.isUp()){
            upQueue.add(currentReq);
        } else {
            downQueue.add(currentReq);
        }
        this.state = SchedulerState.IDLE;
    }

    private void select_request() {
        // Give the current elevator a req/ multiple request or nothing.
        //socket.sendToElevator(); whatever requests we want to give it
    }

    /**
     * Executes the current state's logic.
     */
    public void execute() {
        switch (this.state) {
            case IDLE:
                System.out.println("[SCHEDULER]: IDLE");
                this.idle();
                break;
            case PROCESS_REQ:
                System.out.println("[SCHEDULER]: PROCESS_REQ");
                this.process_request();
                break;
            case SELECT_REQ:
                System.out.println("[SCHEDULER] SELECT_REQ");
                this.select_request();
                break;
        }
    }



    /**
     * The main run method of the scheduler thread.
     */
    public void run() {
        socket.start();
        while (true) {
            execute();
        }
    }



}
