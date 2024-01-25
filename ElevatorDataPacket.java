import java.io.*;

public class ElevatorDataPacket {


    public byte[] elevatordata_to_bytes(ElevatorData data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(data);
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

    public ElevatorData bytes_to_elevatordata(byte[] data_packet) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data_packet);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (ElevatorData) in.readObject();
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
