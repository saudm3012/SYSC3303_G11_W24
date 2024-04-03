package Floor;

import java.io.*;
import java.time.LocalTime;

/**
 * DataPacket that holds info from input file
 * Object to be sent from floor to scheduler to elevator
 * @Author Jatin Jain
 */
public class FloorRequest implements Serializable {
    private LocalTime time;
    private int floor;
    private boolean goingUp;
    private int car;
    private boolean isEmpty;
    private boolean endPacket;
    private Fault fault;

    /**
     * Creates a new empty Data packet.
     */
    public FloorRequest() {
        this.isEmpty = true;
        this.endPacket = true;
        this.fault = Fault.NONE;
    }

    /**
     * Instantiates a new Data packet
     * with the specified information.
     *
     * @param time      String, time of request
     * @param floor     String, floor where request was made
     * @param direction String, car direction
     * @param car       String, requested car number
     * @param endPacket boolean, end packet of transmition? true false otherwise
     * @param fault     Floor.Fault, enum specifying fault associated with request
     */
    public FloorRequest(String time, String floor, String direction, String car, boolean end){
        this.time = LocalTime.parse(time);
        this.floor = Integer.parseInt(floor);
        if(direction.equals("Up")){this.goingUp = true;}
        else if (direction.equals("Down")) {this.goingUp = false;}
        this.car = Integer.parseInt(car);
        this.isEmpty = false;
        this.endPacket = end;
        this.fault = Fault.NONE;
    }

    /**
     * Instantiates a new Data packet
     * with the specified information.
     *
     * @param time      String, time of request
     * @param floor     String, floor where request was made
     * @param direction String, car direction
     * @param car       String, requested car number
     * @param endPacket boolean, end packet of transmition? true false otherwise
     * @param fault     Floor.Fault, enum specifying fault associated with request
     */
    public FloorRequest(String time, String floor, String direction, String car, boolean end, String fault){
        this.time = LocalTime.parse(time);
        this.floor = Integer.parseInt(floor);
        if(direction.equals("Up")){this.goingUp = true;}
        else if (direction.equals("Down")) {this.goingUp = false;}
        this.car = Integer.parseInt(car);
        this.isEmpty = false;
        this.endPacket = end;
        try{
            this.fault = Fault.valueOf(fault);
        }
        catch (IllegalArgumentException e){
            this.fault = Fault.NONE;
        }
    }

    /**
     *
     * @return LocalTime, time of request
     */
    public LocalTime getTime() {
        return this.time;
    }

    /**
     * *
     * @return int, floor where request was made
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * *
     * @return Floor.Fault the fault associated with the request if there is one.
     */
    public Fault getFault() {
        return this.fault;
    }

    /**
     *
     * @return
     */
    public boolean isUp() { return this.goingUp;}

    /**
     *
     * @return
     */
    public boolean isEnd() { return this.endPacket;}

    /**
     *
     * @return boolean, car direction
     */
    public boolean getDirection() {
        return this.goingUp;
    }

    /**
     *
     * @return int, requested car number
     */
    public int getCarButton() {
        return this.car;
    }

    /**
     *
     * @return boolean, true if packet is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }

    /**
     * @return String, all fields of Dataacket in String representation
     */
    public String toString() {
        return (!endPacket) ? "Floor.FloorRequest { \n" +
                "\t time: " + this.time + "\n" +
                "\t floor: " + this.floor + "\n" +
                "\t directionIsUp: " + this.goingUp + "\n" +
                "\t carButton: " + this.car + "\n" +
                "\t fault: " + this.fault + "\n" +
                "}\n": "End DataPacket\n";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        final FloorRequest that = (FloorRequest) o;
        return (this.time.equals(that.time)) &&
                (this.floor == that.floor) && (this.goingUp == that.goingUp) && (this.car == that.car);
    }

    /**
     *
     * @return byte[], returns this DataPacket object converted to bytes
     * @throws IOException
     */
    public byte[] dataPacketToBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
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
     * Set this DataPacket object from byte array input
     * @param dataPacket byte array to be converted
     * @throws IOException
     */
    public void bytesToDataPacket(byte[] dataPacket) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(dataPacket);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            FloorRequest temp = (FloorRequest) in.readObject();
            this.time = temp.time;
            this.floor = temp.floor;
            this.goingUp = temp.goingUp;
            this.car = temp.car;
            this.endPacket = temp.endPacket;
            this.fault = temp.fault;
            in.close();
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

}