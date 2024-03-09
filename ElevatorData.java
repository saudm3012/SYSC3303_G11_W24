import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ElevatorData implements Serializable {
    private int floor;
    private Direction direction;
    private boolean isEmpty;


    /**
     * Creates a new empty Data packet.
     */
    public ElevatorData() {
        this.isEmpty = true;
        this.floor = 0;
    }

     /**
     * Instantiates a new Data packet
     * with the specified information.
     *
     * @param floor     int, floor elevator is on
     * @param direction Direction, elevator direction
     * @param isEmpty   boolean, if elevator is empty or not
     */
    public ElevatorData(int floor, Direction direction, boolean empty) {
        this.floor = floor;
        this.direction = direction;
        this.isEmpty = empty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Direction getDirection() {
        return direction;
    }
    
    public boolean isUp() {
        return (direction==Direction.UP ? true : false);
    }

    public boolean isDown() {
        return (direction==Direction.UP ? false : true);
    }

    public int getFloor() {
        return floor;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * @return String, all fields of Dataacket in String representation
     */
    public String toString() {
        return "ElevatorData { \n" +
                "\t Floor: " + this.floor + "\n" +
                "\t Direction: " + ((this.isUp()) ? "UP" : "DOWN") + "\n" +
                "\t isEmpty: " + ((this.isEmpty) ? "True" : "False") + "\n" +
                "}\n";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        final ElevatorData that = (ElevatorData) o;
        return (this.direction.equals(that.direction)) &&
                (this.floor == that.floor) && (this.isEmpty == that.isEmpty);
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
            ElevatorData temp = (ElevatorData) in.readObject();
            this.direction = temp.direction;
            this.floor = temp.floor;
            this.isEmpty = temp.isEmpty;
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
