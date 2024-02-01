import java.io.*;
import java.time.LocalTime;

public class ElevatorData implements Serializable {
    public LocalTime time; // Time
    public int floor; // Floor
    public boolean up_button; // True if button is up, false if button is down
    public int car_button;

    public ElevatorData(){
        this.time = LocalTime.of(14, 5,5, 0);
        this.floor = 2;
        this.up_button = true;
        this.car_button = 4;
    }
    public byte[] elevatordata_to_bytes() throws IOException {
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

    public void bytes_to_elevatordata(byte[] data_packet) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data_packet);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            ElevatorData temp = (ElevatorData) in.readObject();
            this.time = temp.time;
            this.floor = temp.floor;
            this.up_button = temp.up_button;
            this.car_button = temp.car_button;
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

