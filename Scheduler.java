
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
    private SchedulerState state = SchedulerState.IDLE;

    /**
     * Queue for storing received data packets.
     */
    Queue<DataPacket> receiveQueue;

    /**
     * The constructor for this class.
     */
    public Scheduler() {
        socket = new SchedulerSocket(this);
        receiveQueue = new LinkedList<>();
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
    public DataPacket receiveQueueRemove() {
        return receiveQueue.remove();
    }

    /**
     * Adds a data packet to the receive queue.
     *
     * @param data The data packet to be added.
     */
    public void receiveQueueAdd(DataPacket data) {
        receiveQueue.add(data);
    }

    /**
     * Handles the IDLE state, waiting for the receive queue to fill up and then
     * sending data to the elevator.
     */

    private void idle() {
        while (receiveQueue.isEmpty()) {
            // Wait for the receive queue to fill up
            sleep(1000);
        }
        sleep(100);
        socket.sendToElevator(receiveQueue.remove());
        this.state = SchedulerState.WAIT_ACK;
    }

    /**
     * Handles the WAIT_ACK state, receiving data from the elevator.
     */
    private void wait_ack() {
        socket.receiveFromElevator();
        sleep(100);
        this.state = SchedulerState.IDLE;
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
            case WAIT_ACK:
                System.out.println("[SCHEDULER]: WAIT_ACK");
                this.wait_ack();
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
