
/**
 * The Scheduler class that manages communication between FloorSocket and ElevatorSocket.
 *
 * This class acts as the central controller, receiving data packets from FloorSocket,
 * and forwarding them to ElevatorSocket. It transitions between the IDLE and WAIT_ACK states.
 *
 * @author Mohammad Saud
 * @author Riya Arora (101190033)
 */

import javax.swing.plaf.ListUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Iterator;

public class Scheduler implements Runnable {

    /**
     * The socket responsible for communication with elevators.
     */
    public SchedulerSocket socket;
    public SchedulerElevatorSocket elevatorSocket;

    /**
     * The current state of the scheduler.
     */
    public SchedulerState state = SchedulerState.IDLE;

    /**
     * Queue for storing received data packets.
     */
    Queue<FloorRequest> receiveQueue;
    List<FloorRequest> upQueue;
    List<FloorRequest> downQueue;
    FloorRequest elevatorEndPacket;
    Queue<ElevatorData> elevatorQueue;
    Queue<Integer> emptyElevatorList;


    /**
     * The constructor for this class.
     */
    public Scheduler() {
        socket = new SchedulerSocket(this);
        elevatorSocket = new SchedulerElevatorSocket(this);
        receiveQueue = new LinkedList<>();
        upQueue = new ArrayList<>();
        downQueue = new ArrayList<>();
        elevatorQueue = new LinkedList<>();
        elevatorEndPacket = new FloorRequest();
        emptyElevatorList = new LinkedList<>();
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
     * Checks if the receive queue is empty.
     *
     * @return true if the receive queue is empty, false otherwise.
     */
    public boolean receiveUpQueueIsEmpty() {
        return upQueue.isEmpty();
    }

    /**
     * Checks if the receive queue is empty.
     *
     * @return true if the receive queue is empty, false otherwise.
     */
    public boolean receiveDownQueueIsEmpty() {
        return upQueue.isEmpty();
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

    public void elevatorQueueAdd(ElevatorData data) {
        elevatorQueue.add(data);
    }

    public ElevatorData elevatorQueueRemove(){
        return elevatorQueue.remove();
    }

    /**
     * Handles the IDLE state, waiting for the receive queue to fill up and then
     * sending data to the elevator.
     */

    private void idle() {
        while(true) {
            if(!elevatorQueue.isEmpty()){
                // Elevator has reached a floor and wants a request
                this.state = SchedulerState.SELECT_REQ;
                return;
            }
            if(!receiveQueue.isEmpty()) {
                // We have a new floor request
                sleep(100);
                this.state = SchedulerState.PROCESS_REQ;
                return;
            }
            sleep(100);
        }
    }

    private void process_request() {
        FloorRequest currentReq = receiveQueue.remove();
        if(!emptyElevatorList.isEmpty()){
            int elevNum = emptyElevatorList.remove();
            elevatorSocket.sendToElevator(currentReq, elevNum);
            elevatorSocket.sendToElevator(elevatorEndPacket, elevNum);
        }
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
        sleep(1000);
        ElevatorData elevatorData;
        if(!elevatorQueue.isEmpty()) {
            elevatorData = elevatorQueueRemove();
        } else {
            this.state = SchedulerState.IDLE;
            return;
        }
        if(elevatorData.isEmpty()){
            //Give it any request
            if(upQueue.size() > downQueue.size() && !upQueue.isEmpty()){
                elevatorSocket.sendToElevator(upQueue.removeFirst(), elevatorData.getElevatorNum());
            } else if(!downQueue.isEmpty()) {
                elevatorSocket.sendToElevator(downQueue.removeFirst(), elevatorData.getElevatorNum());
            } else {
                //elevatorSocket.sendToElevator(elevatorEndPacket, elevatorData.getElevatorNum());
                //Store the empty elevator if not already in there
                if(!emptyElevatorList.contains(elevatorData.getElevatorNum())) {
                    emptyElevatorList.add(elevatorData.getElevatorNum());
                }
                return;
            }
        } else if(elevatorData.isUp()){
            if(upQueue.isEmpty()){
                elevatorSocket.sendToElevator(elevatorEndPacket, elevatorData.getElevatorNum());
                return;
            }
            for(int i=0;i<upQueue.size();i++){
                if(upQueue.get(i).getFloor() == elevatorData.getFloor()){
                    elevatorSocket.sendToElevator(upQueue.remove(i), elevatorData.getElevatorNum());
                }
            }
        } else {
            if(downQueue.isEmpty()){
                elevatorSocket.sendToElevator(elevatorEndPacket, elevatorData.getElevatorNum());
                return;
            }
            for(int i=0;i<downQueue.size();i++){
                if(downQueue.get(i).getFloor() == elevatorData.getFloor()){
                    elevatorSocket.sendToElevator(downQueue.remove(i), elevatorData.getElevatorNum());
                }
            }
        }
        elevatorSocket.sendToElevator(elevatorEndPacket, elevatorData.getElevatorNum());
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
        elevatorSocket.start();
        while (true) {
            execute();
        }
    }

    public static void main (String args[]) throws IOException {
        Thread scheduler =  new Thread(new Scheduler());
        scheduler.start();
    }

}
