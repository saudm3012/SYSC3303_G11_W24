import java.io.Serializable;
import java.util.ArrayList;

/**
 * DataPacket that holds info from input file
 * Object to be sent from floor to scheduler to elevator
 * @Author Jatin Jain
 */
public class DataPacket implements Serializable {
    private String time;
    private String floor;
    private String direction;
    private String car;
    private boolean isEmpty;

    /**
     * Creates a new empty Data packet.
     */
    public DataPacket() {
        this.isEmpty = true;
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
        this.time = time;
        this.floor = floor;
        this.direction = direction;
        this.car = car;
        this.isEmpty = false;
    }

    /**
     *
     * @return String, time of request
     */
    public String getTime() {
        return this.time;
    }

    /**
     * *
     * @return String, floor where request was made
     */
    public String getFloor() {
        return this.floor;
    }

    /**
     *
     * @return String, car direction
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     *
     * @return String, requested car number
     */
    public String getCarButton() {
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
        return "DataPacket { \n" +
                "\t time: " + this.time + "\n" +
                "\t floor: " + this.floor + "\n" +
                "\t floorButton: " + this.direction + "\n" +
                "\t carButton: " + this.car + "\n" +
                "}\n";
    }
}