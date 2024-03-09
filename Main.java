import javax.xml.crypto.Data;
import java.io.IOException;

public class Main {
    public static void main (String args[]) throws IOException {
        InputReader datafile = new InputReader("data.txt");
        FloorSubsystem floor_sys = new FloorSubsystem();
        Thread elevator = new ElevatorSubsystem(5,1);
        Thread floorSubsystem = new Thread(floor_sys);
        Thread scheduler =  new Thread(new Scheduler());

        while(floor_sys.addPacketToQueue(datafile.getNextPacket())){};
        floor_sys.addPacketToQueue(datafile.getNextPacket());
        
        floorSubsystem.start();
        scheduler.start();
        elevator.start();
    }
}
