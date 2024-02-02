import java.io.*;
import java.time.LocalTime;

/**
 * DataPacket that holds info from input file
 * Object to be sent from floor to scheduler to elevator
 * @Author Jatin Jain
 */
public class DataPacket implements Serializable {
    private LocalTime time;
    private int floor;
    private boolean up_direction;
    private int car;
    private boolean isEmpty;
    private boolean isFromFloor;

    /**
     * Creates a new empty Data packet.
     */
    public DataPacket() {
        this.isEmpty = true;
        this.isFromFloor = false;
    }

    /**
     * Instantiates a new Data packet
     * with the specified information.
     *
     * @param time      String, time of request
     * @param floor     String, floor where request was made
     * @param direction String, car direction
     * @param car       String, requested car number
     */
    public DataPacket(String time, String floor, String direction, String car){
        this.time = LocalTime.parse(time);
        this.floor = Integer.parseInt(floor);
        System.out.println(direction);
        if(direction.equals("Up")){this.up_direction = true;}
        else if (direction.equals("Down")) {this.up_direction = false;}
        this.car = Integer.parseInt(car);
        this.isEmpty = false;
    }

    /**
     *
     * @return String, time of request
     */
    public LocalTime getTime() {
        return this.time;
    }

    /**
     * *
     * @return String, floor where request was made
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     *
     * @return String, car direction
     */
    public boolean getDirection() {
        return this.up_direction;
    }

    /**
     *
     * @return String, requested car number
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
     *
     * @return boolean, true if was sent by floor subsystem, false otherwise.
     */
    public boolean isFromFloor() {
        return this.isFromFloor;
    }

    /**
     * set isFromFloor to true
     */
    public void setFromFloor() {
        this.isFromFloor = true;
    }
    
    /**
     * set isFromFloor to false
     */
    public void setFromElevator() {
        this.isFromFloor = false;
    }

    /**
     * @return String, all fields of Dataacket in String representation
     */
    public String toString() {
        return "DataPacket { \n" +
                "\t time: " + this.time + "\n" +
                "\t floor: " + this.floor + "\n" +
                "\t directionIsUp: " + this.up_direction + "\n" +
                "\t carButton: " + this.car + "\n" +
                "}\n";
    }
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

    public void bytesToDataPacket(byte[] dataPacket) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(dataPacket);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            DataPacket temp = (DataPacket) in.readObject();
            this.time = temp.time;
            this.floor = temp.floor;
            this.up_direction = temp.up_direction;
            this.car = temp.car;
            this.isFromFloor = temp.isFromFloor;
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