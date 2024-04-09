package Scheduler;
/**
 * The Scheduler.Scheduler class that manages communication between Floor.FloorSocket and Elevator.ElevatorSocket.
 *
 * This class acts as the central controller, receiving data packets from Floor.FloorSocket,
 * and forwarding them to Elevator.ElevatorSocket. It transitions between the IDLE and WAIT_ACK states.
 *
 * @author Mohammad Saud
 * @author Riya Arora (101190033)
 */

import Elevator.ElevatorData;
import Floor.FloorRequest;
import Gui.ElevatorGUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.lang.Math;

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
    List<FloorRequest> receiveQueue;
    List<FloorRequest> upQueue;
    List<FloorRequest> downQueue;
    FloorRequest elevatorEndPacket;
    Queue<ElevatorData> elevatorQueue;
    List<ElevatorData> emptyElevatorList;
    private ElevatorGUI elevatorGUI;


    /**
     * The constructor for this class.
     */
    public Scheduler(ElevatorGUI ui) {
        socket = new SchedulerSocket(this);
        elevatorSocket = new SchedulerElevatorSocket(this);
        receiveQueue = new LinkedList<>();
        upQueue = new ArrayList<>();
        downQueue = new ArrayList<>();
        elevatorQueue = new LinkedList<>();
        elevatorEndPacket = new FloorRequest();
        emptyElevatorList = new LinkedList<>();
        this.elevatorGUI = ui;
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
                // Elevator.Elevator has reached a floor and wants a request
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
        FloorRequest currentReq = receiveQueue.removeFirst();
        if(!emptyElevatorList.isEmpty()){
            //sleep(1000);
            int closest_floor = -1; //Index of the closest floor
            int delta = 99999; //Smallest we have had so far
            for(int i = 0;i<emptyElevatorList.size();i++){
                if(Math.abs(emptyElevatorList.get(i).getFloor() - currentReq.getFloor()) < delta){
                    closest_floor = i;
                    delta = Math.abs(emptyElevatorList.get(i).getFloor() - currentReq.getFloor());
                }
            }
            if(closest_floor == -1){System.out.println("Error in delta calculation");}
            ElevatorData closestElev = emptyElevatorList.remove(closest_floor);
            int elevNum = closestElev.getElevatorNum();
            elevatorSocket.sendToElevator(currentReq, elevNum);
            for(int i = 0; i<receiveQueue.size(); i++){
                if(receiveQueue.get(i).getFloor() == currentReq.getFloor()){
                    elevatorSocket.sendToElevator(receiveQueue.remove(i), elevNum);
                }
            }
            elevatorSocket.sendToElevator(elevatorEndPacket, elevNum);
        } else if(currentReq.isUp()){
            upQueue.add(currentReq);
        } else {
            downQueue.add(currentReq);
        }
        this.state = SchedulerState.IDLE;
    }


    private void select_request() {
        // Give the current elevator a req/ multiple request or nothing.
        //socket.sendToElevator(); whatever requests we want to give it
        //sleep(1000);
        ElevatorData elevatorData;
        List<Integer> floorList = new ArrayList<>();
        if(!elevatorQueue.isEmpty()) {
            elevatorData = elevatorQueueRemove();
        } else {
            this.state = SchedulerState.IDLE;
            return;
        }
        if(elevatorData.isEmpty()){
            //Give it any request
            if(upQueue.size() > downQueue.size() && !upQueue.isEmpty()){
                //FInd closest elevator to send
                int closest_floor = -1; //Index of the closest floor
                int delta = 99999; //Smallest we have had so far
                for(int i = 0;i<upQueue.size();i++){
                    if(Math.abs(upQueue.get(i).getFloor() - elevatorData.getFloor()) < delta){
                        closest_floor = i;
                        delta = Math.abs(upQueue.get(i).getFloor() - elevatorData.getFloor());
                    }
                }
                elevatorSocket.sendToElevator(upQueue.remove(closest_floor), elevatorData.getElevatorNum());
            } else if(!downQueue.isEmpty()) {
                //FInd closest elevator to send
                int closest_floor = -1; //Index of the closest floor
                int delta = 99999; //Smallest we have had so far
                for(int i = 0;i<downQueue.size();i++){
                    if(Math.abs(downQueue.get(i).getFloor() - elevatorData.getFloor()) < delta){
                        closest_floor = i;
                        delta = Math.abs(downQueue.get(i).getFloor() - elevatorData.getFloor());
                    }
                }
                elevatorSocket.sendToElevator(downQueue.remove(closest_floor), elevatorData.getElevatorNum());
                //Find closest elevator to send
            } else {
                //elevatorSocket.sendToElevator(elevatorEndPacket, elevatorData.getElevatorNum());
                //Store the empty elevator if not already in there
                if(!emptyElevatorList.contains(elevatorData.getElevatorNum())) {
                    emptyElevatorList.add(elevatorData);
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
                    floorList.add(i);
                }
            }
            for(int i=0;i<floorList.size();i++){
                elevatorSocket.sendToElevator(upQueue.remove(i), elevatorData.getElevatorNum());
            }
        } else {
            if(downQueue.isEmpty()){
                elevatorSocket.sendToElevator(elevatorEndPacket, elevatorData.getElevatorNum());
                return;
            }
            for(int i=0;i<downQueue.size();i++){
                if(downQueue.get(i).getFloor() == elevatorData.getFloor()){
                    floorList.add(i);
                }
            }
            for(int i=0;i<floorList.size();i++){
                elevatorSocket.sendToElevator(downQueue.remove(i), elevatorData.getElevatorNum());
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
            case SchedulerState.IDLE:
                System.out.println("[SCHEDULER]: IDLE");
                this.idle();
                break;
            case SchedulerState.PROCESS_REQ:
                System.out.println("[SCHEDULER]: PROCESS_REQ");
                this.process_request();
                break;
            case SchedulerState.SELECT_REQ:
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
            sleep(1000);
            execute();
        }
    }

}
